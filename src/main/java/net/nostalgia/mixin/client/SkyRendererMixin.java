package net.nostalgia.mixin.client;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SkyRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.model.sprite.AtlasManager;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import org.joml.Matrix3f;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SkyRenderer.class)
public abstract class SkyRendererMixin {

    @Shadow
    private GpuBuffer starBuffer;

    @Unique
    private GpuBuffer nostalgia$classicStarBuffer;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void nostalgia$onInit(TextureManager textureManager, AtlasManager atlasManager, CallbackInfo ci) {
        this.nostalgia$classicStarBuffer = this.nostalgia$buildClassicStars();
    }

    @Unique
    private GpuBuffer nostalgia$buildClassicStars() {
        RandomSource random = RandomSource.createThreadLocalInstance(10842L);
        try (ByteBufferBuilder byteBufferBuilder = ByteBufferBuilder.exactlySized(DefaultVertexFormat.POSITION.getVertexSize() * 1500 * 4)) {
            BufferBuilder bufferBuilder = new BufferBuilder(byteBufferBuilder, VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
            for (int i = 0; i < 1500; i++) {
                float x = random.nextFloat() * 2.0F - 1.0F;
                float y = random.nextFloat() * 2.0F - 1.0F;
                float z = random.nextFloat() * 2.0F - 1.0F;
                float starSize = 0.25F + random.nextFloat() * 0.25F;
                float lengthSq = Mth.lengthSquared(x, y, z);
                if (!(lengthSq <= 0.010000001F) && !(lengthSq >= 1.0F)) {
                    Vector3f starCenter = new Vector3f(x, y, z).normalize(100.0F);
                    float zRot = (float)(random.nextDouble() * 3.1415927410125732 * 2.0);
                    Matrix3f rotation = new Matrix3f().rotateTowards(new Vector3f(starCenter).negate(), new Vector3f(0.0F, 1.0F, 0.0F)).rotateZ(-zRot);
                    bufferBuilder.addVertex(new Vector3f(starSize, -starSize, 0.0F).mul(rotation).add(starCenter));
                    bufferBuilder.addVertex(new Vector3f(starSize, starSize, 0.0F).mul(rotation).add(starCenter));
                    bufferBuilder.addVertex(new Vector3f(-starSize, starSize, 0.0F).mul(rotation).add(starCenter));
                    bufferBuilder.addVertex(new Vector3f(-starSize, -starSize, 0.0F).mul(rotation).add(starCenter));
                }
            }
            try (MeshData mesh = bufferBuilder.buildOrThrow()) {
                return RenderSystem.getDevice().createBuffer(() -> "Classic stars vertex buffer", 40, mesh.vertexBuffer());
            }
        }
    }

    @Redirect(method = "renderStars", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/SkyRenderer;starBuffer:Lcom/mojang/blaze3d/buffers/GpuBuffer;"))
    private GpuBuffer nostalgia$getStarBuffer(SkyRenderer instance) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null && net.nostalgia.world.rules.LegacyProfiles.get(mc.level).classicStars() && this.nostalgia$classicStarBuffer != null) {
            return this.nostalgia$classicStarBuffer;
        }
        return this.starBuffer;
    }

    @Inject(method = "close", at = @At("TAIL"))
    private void nostalgia$onClose(CallbackInfo ci) {
        if (this.nostalgia$classicStarBuffer != null) {
            this.nostalgia$classicStarBuffer.close();
        }
    }
}
