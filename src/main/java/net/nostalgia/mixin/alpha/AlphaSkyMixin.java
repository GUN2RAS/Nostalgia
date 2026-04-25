package net.nostalgia.mixin.alpha;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.SkyRenderer;
import net.minecraft.resources.Identifier;
import net.nostalgia.world.dimension.ModDimensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.textures.GpuTextureView;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import java.util.OptionalDouble;
import java.util.OptionalInt;

import com.mojang.blaze3d.textures.GpuSampler;
import com.mojang.blaze3d.textures.FilterMode;

@Mixin(SkyRenderer.class)
public abstract class AlphaSkyMixin {

    @org.spongepowered.asm.mixin.Shadow
    private GpuBuffer bottomSkyBuffer;

    @Unique
    private static final Identifier ALPHA_SUN_LOCATION = Identifier.fromNamespaceAndPath("nostalgia", "textures/environment/alpha_sun.png");
    @Unique
    private static final Identifier ALPHA_MOON_LOCATION = Identifier.fromNamespaceAndPath("nostalgia", "textures/environment/alpha_moon.png");

    @Unique
    private GpuBuffer alphaCelestialsBuffer;

    @Unique
    private GpuBuffer getAlphaCelestialsBuffer() {
        if (this.alphaCelestialsBuffer == null) {
            try (ByteBufferBuilder builder = ByteBufferBuilder.exactlySized(4 * DefaultVertexFormat.POSITION_TEX.getVertexSize())) {
                BufferBuilder buf = new BufferBuilder(builder, VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
                buf.addVertex(-1.0F, 0.0F, -1.0F).setUv(0.0F, 0.0F);
                buf.addVertex(1.0F, 0.0F, -1.0F).setUv(1.0F, 0.0F);
                buf.addVertex(1.0F, 0.0F, 1.0F).setUv(1.0F, 1.0F);
                buf.addVertex(-1.0F, 0.0F, 1.0F).setUv(0.0F, 1.0F);
                try (MeshData mesh = buf.buildOrThrow()) {
                    this.alphaCelestialsBuffer = RenderSystem.getDevice().createBuffer(() -> "Alpha Celestials Buffer", 32, mesh.vertexBuffer());
                }
            }
        }
        return this.alphaCelestialsBuffer;
    }

    @Unique
    private float calculateFogFactor(float targetAlpha) {
        float fogAlpha = net.nostalgia.alphalogic.core.AlphaRenderState.getCelestialAlpha();
        return targetAlpha * fogAlpha;
    }

    @Inject(method = "renderSun", at = @At("HEAD"), cancellable = true)
    private void renderAlphaSun(float alpha, com.mojang.blaze3d.vertex.PoseStack poseStack, CallbackInfo ci) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level != null && level.dimension().equals(net.nostalgia.world.dimension.ModDimensions.ALPHA_112_01_LEVEL_KEY)) {
            ci.cancel();

            float foggedAlpha = calculateFogFactor(alpha);
            if (foggedAlpha <= 0.01F) return;

            org.joml.Matrix4fStack matrixStack = RenderSystem.getModelViewStack();
            matrixStack.pushMatrix();
            matrixStack.mul(poseStack.last().pose()); 

            matrixStack.translate(0.0F, 100.0F, 0.0F);
            matrixStack.scale(30.0F, 1.0F, 30.0F); 

            GpuBufferSlice transforms = RenderSystem.getDynamicUniforms().writeTransform(matrixStack, new Vector4f(1.0F, 1.0F, 1.0F, foggedAlpha), new Vector3f(), new Matrix4f());
            GpuTextureView colorView = Minecraft.getInstance().getMainRenderTarget().getColorTextureView();
            GpuTextureView depthView = Minecraft.getInstance().getMainRenderTarget().getDepthTextureView();
            GpuTextureView texView = Minecraft.getInstance().getTextureManager().getTexture(ALPHA_SUN_LOCATION).getTextureView();

            try (RenderPass cmd = RenderSystem.getDevice()
                    .createCommandEncoder()
                    .createRenderPass(() -> "Alpha Sky Sun", colorView, OptionalInt.empty(), depthView, OptionalDouble.empty())) {
                cmd.setPipeline(RenderPipelines.CELESTIAL);
                RenderSystem.bindDefaultUniforms(cmd);
                cmd.setUniform("DynamicTransforms", transforms);
                
                GpuSampler sampler = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST);
                cmd.bindTexture("Sampler0", texView, sampler);
                
                cmd.setVertexBuffer(0, getAlphaCelestialsBuffer());
                com.mojang.blaze3d.systems.RenderSystem.AutoStorageIndexBuffer quadIndices = RenderSystem.getSequentialBuffer(VertexFormat.Mode.QUADS);
                cmd.setIndexBuffer(quadIndices.getBuffer(6), quadIndices.type());
                cmd.drawIndexed(0, 0, 6, 1);
            }

            matrixStack.popMatrix();
        }
    }

    @Inject(method = "renderMoon", at = @At("HEAD"), cancellable = true)
    private void renderAlphaMoon(net.minecraft.world.level.MoonPhase moonPhase, float alpha, com.mojang.blaze3d.vertex.PoseStack poseStack, CallbackInfo ci) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level != null && level.dimension().equals(net.nostalgia.world.dimension.ModDimensions.ALPHA_112_01_LEVEL_KEY)) {
            ci.cancel();

            float foggedAlpha = calculateFogFactor(alpha);
            if (foggedAlpha <= 0.01F) return;

            org.joml.Matrix4fStack matrixStack = RenderSystem.getModelViewStack();
            matrixStack.pushMatrix();
            matrixStack.mul(poseStack.last().pose());

            matrixStack.translate(0.0F, 100.0F, 0.0F);
            matrixStack.scale(20.0F, 1.0F, 20.0F); 

            GpuBufferSlice transforms = RenderSystem.getDynamicUniforms().writeTransform(matrixStack, new Vector4f(1.0F, 1.0F, 1.0F, foggedAlpha), new Vector3f(), new Matrix4f());
            GpuTextureView colorView = Minecraft.getInstance().getMainRenderTarget().getColorTextureView();
            GpuTextureView depthView = Minecraft.getInstance().getMainRenderTarget().getDepthTextureView();
            GpuTextureView texView = Minecraft.getInstance().getTextureManager().getTexture(ALPHA_MOON_LOCATION).getTextureView();

            try (RenderPass cmd = RenderSystem.getDevice()
                    .createCommandEncoder()
                    .createRenderPass(() -> "Alpha Sky Moon", colorView, OptionalInt.empty(), depthView, OptionalDouble.empty())) {
                cmd.setPipeline(RenderPipelines.CELESTIAL);
                RenderSystem.bindDefaultUniforms(cmd);
                cmd.setUniform("DynamicTransforms", transforms);
                
                GpuSampler sampler = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST);
                cmd.bindTexture("Sampler0", texView, sampler);
                
                cmd.setVertexBuffer(0, getAlphaCelestialsBuffer());
                com.mojang.blaze3d.systems.RenderSystem.AutoStorageIndexBuffer quadIndices = RenderSystem.getSequentialBuffer(VertexFormat.Mode.QUADS);
                cmd.setIndexBuffer(quadIndices.getBuffer(6), quadIndices.type());
                cmd.drawIndexed(0, 0, 6, 1);
            }

            matrixStack.popMatrix();
        }
    }

    @org.spongepowered.asm.mixin.injection.Inject(method = "extractRenderState", at = @org.spongepowered.asm.mixin.injection.At("RETURN"))
    private void overrideAlphaSkyState(net.minecraft.client.multiplayer.ClientLevel level, float tickDelta, net.minecraft.client.Camera camera, net.minecraft.client.renderer.state.level.SkyRenderState state, org.spongepowered.asm.mixin.injection.callback.CallbackInfo ci) {
        if (level != null && level.dimension().equals(net.nostalgia.world.dimension.ModDimensions.ALPHA_112_01_LEVEL_KEY)) {
            
            state.sunriseAndSunsetColor = 0; 

            float f1 = camera.attributeProbe().getValue(net.minecraft.world.attribute.EnvironmentAttributes.SUN_ANGLE, tickDelta) / 360.0F;
            float f2 = net.minecraft.util.Mth.cos(f1 * 3.1415927F * 2.0F) * 2.0F + 0.5F;
            if (f2 < 0.0F) f2 = 0.0F;
            if (f2 > 1.0F) f2 = 1.0F;

            float red = 136.0F / 255.0F;
            float green = 187.0F / 255.0F;
            float blue = 255.0F / 255.0F;
            
            red *= f2;
            green *= f2;
            blue *= f2;
            
            int finalRed = (int) (red * 255.0F);
            int finalGreen = (int) (green * 255.0F);
            int finalBlue = (int) (blue * 255.0F);
            state.skyColor = 0xFF000000 | (finalRed << 16) | (finalGreen << 8) | finalBlue;
        }
    }
    
    @Inject(method = "renderDarkDisc", at = @At("HEAD"), cancellable = true)
    private void renderAlphaDarkDisc(CallbackInfo ci) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level != null && level.dimension().equals(net.nostalgia.world.dimension.ModDimensions.ALPHA_112_01_LEVEL_KEY)) {
            ci.cancel();

            net.minecraft.client.Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
            float tickDelta = Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaTicks();
            float f1 = camera.attributeProbe().getValue(net.minecraft.world.attribute.EnvironmentAttributes.SUN_ANGLE, tickDelta) / 360.0F;
            float f2 = net.minecraft.util.Mth.cos(f1 * 3.1415927F * 2.0F) * 2.0F + 0.5F;
            f2 = net.minecraft.util.Mth.clamp(f2, 0.0F, 1.0F);

            float red = 0.7529412F;
            float green = 0.8470588F;
            float blue = 1.0F;
            red *= f2 * 0.94F + 0.06F;
            green *= f2 * 0.94F + 0.06F;
            blue *= f2 * 0.91F + 0.09F;

            float voidRed = red * 0.2F + 0.04F;
            float voidGreen = green * 0.2F + 0.04F;
            float voidBlue = blue * 0.6F + 0.1F;

            org.joml.Matrix4fStack matrixStack = RenderSystem.getModelViewStack();
            matrixStack.pushMatrix();
            matrixStack.translate(0.0F, 12.0F, 0.0F);
            
            com.mojang.blaze3d.buffers.GpuBufferSlice transforms = RenderSystem.getDynamicUniforms().writeTransform(matrixStack, new org.joml.Vector4f(voidRed, voidGreen, voidBlue, 1.0F), new org.joml.Vector3f(), new org.joml.Matrix4f());
            com.mojang.blaze3d.textures.GpuTextureView colorView = Minecraft.getInstance().getMainRenderTarget().getColorTextureView();
            com.mojang.blaze3d.textures.GpuTextureView depthView = Minecraft.getInstance().getMainRenderTarget().getDepthTextureView();

            try (com.mojang.blaze3d.systems.RenderPass cmd = RenderSystem.getDevice()
                    .createCommandEncoder()
                    .createRenderPass(() -> "Alpha Sky dark", colorView, java.util.OptionalInt.empty(), depthView, java.util.OptionalDouble.empty())) {
                cmd.setPipeline(RenderPipelines.SKY);
                RenderSystem.bindDefaultUniforms(cmd);
                cmd.setUniform("DynamicTransforms", transforms);
                cmd.setVertexBuffer(0, this.bottomSkyBuffer);
                cmd.draw(0, 10);
            }

            matrixStack.popMatrix();
        }
    }

    @Inject(method = "renderStars(FLcom/mojang/blaze3d/vertex/PoseStack;)V", at = @At("HEAD"), cancellable = true)
    private void interceptStarsFog(float alpha, com.mojang.blaze3d.vertex.PoseStack poseStack, CallbackInfo ci) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level != null && level.dimension().equals(net.nostalgia.world.dimension.ModDimensions.ALPHA_112_01_LEVEL_KEY)) {
            float foggedAlpha = calculateFogFactor(alpha);
            if (foggedAlpha <= 0.01F) { 
                ci.cancel(); 
            }

        }
    }
}
