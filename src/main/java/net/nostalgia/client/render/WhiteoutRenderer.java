package net.nostalgia.client.render;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;

import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MappableRingBuffer;
import org.joml.Matrix4f;
import com.mojang.blaze3d.pipeline.RenderTarget;
import net.nostalgia.client.ritual.RitualVisualManager;
import com.mojang.blaze3d.shaders.UniformType;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

import java.util.OptionalDouble;
import java.util.OptionalInt;

@Environment(EnvType.CLIENT)
public class WhiteoutRenderer {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final RenderPipeline PIPELINE = RenderPipeline.builder()
            .withLocation(net.minecraft.resources.Identifier.fromNamespaceAndPath("nostalgia", "radial_whiteout"))
            .withVertexShader(net.minecraft.resources.Identifier.fromNamespaceAndPath("nostalgia", "core/radial_whiteout"))
            .withFragmentShader(net.minecraft.resources.Identifier.fromNamespaceAndPath("nostalgia", "core/radial_whiteout"))
            .withSampler("Sampler1")
            .withUniform("WhiteoutData", UniformType.UNIFORM_BUFFER)
            .withColorTargetState(new com.mojang.blaze3d.pipeline.ColorTargetState(com.mojang.blaze3d.pipeline.BlendFunction.TRANSLUCENT))
            .withDepthStencilState(new com.mojang.blaze3d.pipeline.DepthStencilState(com.mojang.blaze3d.platform.CompareOp.ALWAYS_PASS, false))
            
            .withVertexFormat(DefaultVertexFormat.EMPTY, VertexFormat.Mode.TRIANGLES)
            .build();

    private static MappableRingBuffer uboBuffer;

    private static void initUbo() {

        uboBuffer = new MappableRingBuffer(() -> "Nostalgia Whiteout UBO", 130, 128);
    }

    public static void render(DeltaTracker tracker) {
        if (!RitualVisualManager.isTransitioning || RitualVisualManager.getTransitionTimeSeconds() <= 0.0f) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        RenderTarget target = mc.getMainRenderTarget();
        if (target == null || target.getColorTextureView() == null || target.getDepthTextureView() == null) {
            return;
        }

        if (uboBuffer == null) {
            initUbo();
        }

        float fov = mc.options.fov().get().floatValue();
        net.minecraft.client.Camera camera = mc.gameRenderer.getMainCamera();
        
        org.joml.Matrix4f invViewProj;
        
        if (net.nostalgia.client.render.PortalSkyRenderer.capturedProjectionMatrix != null && net.nostalgia.client.render.PortalSkyRenderer.capturedModelViewMatrix != null) {
            invViewProj = new org.joml.Matrix4f(net.nostalgia.client.render.PortalSkyRenderer.capturedProjectionMatrix).mul(net.nostalgia.client.render.PortalSkyRenderer.capturedModelViewMatrix).invert();
        } else if (TimestopBorderRenderer.capturedProjectionMatrix != null) {
            org.joml.Matrix4f viewMatrix = camera.getViewRotationMatrix(new org.joml.Matrix4f());
            invViewProj = new org.joml.Matrix4f(TimestopBorderRenderer.capturedProjectionMatrix).mul(viewMatrix).invert();
        } else {
            invViewProj = camera.getViewRotationProjectionMatrix(new org.joml.Matrix4f()).invert();
        }

        CommandEncoder encoder = RenderSystem.getDevice().createCommandEncoder();
        try (GpuBuffer.MappedView viewMapped = encoder.mapBuffer(uboBuffer.currentBuffer(), false, true)) {
            Std140Builder builder = Std140Builder.intoBuffer(viewMapped.data());
            
            net.minecraft.world.phys.Vec3 camPos = camera.position();
            
            double trueCenterX = RitualVisualManager.ritualCenter.getX();
            double trueCenterY = RitualVisualManager.ritualCenter.getY();
            double trueCenterZ = RitualVisualManager.ritualCenter.getZ();
            
            if (RitualVisualManager.isInNewDimension()) {
                trueCenterX += net.nostalgia.alphalogic.ritual.RitualActiveState.offsetX;
                trueCenterY -= net.nostalgia.alphalogic.ritual.RitualActiveState.yOffset;
                trueCenterZ += net.nostalgia.alphalogic.ritual.RitualActiveState.offsetZ;
            }

            builder.putVec4(
                    (float) (trueCenterX + 0.5f),
                    (float) (trueCenterY + 0.5f),
                    (float) (trueCenterZ + 0.5f),
                    RitualVisualManager.getTransitionTimeSeconds() 
            );
            builder.putMat4f(invViewProj);
            
            builder.putVec4(
                RitualVisualManager.getWhiteoutAlpha(), 
                RitualVisualManager.isInNewDimension() ? 1.0f : 0.0f, 
                RitualVisualManager.getWhiteRadius(), 
                RitualVisualManager.getAlphaRadius()
            );

            float cloudHeight = 192.0f;
            boolean isScreenAlpha = false;
            if (RitualVisualManager.targetDimension.equals("nostalgia:alpha")) {
                isScreenAlpha = RitualVisualManager.isInNewDimension(); 
            } else if (RitualVisualManager.targetDimension.equals("overworld")) {
                isScreenAlpha = !RitualVisualManager.isInNewDimension(); 
            }
            if (isScreenAlpha) {
                cloudHeight = 108.0f;
            }
            
            builder.putVec4((float)camPos.x, (float)camPos.y, (float)camPos.z, cloudHeight);

            long dayTime = mc.level.getDefaultClockTime(); 

            float timeOfDay = (float)(mc.level.getDefaultClockTime() % 24000L) / 24000.0F;
            float cosTime = (float) Math.cos((timeOfDay - 0.25F) * 3.1415927F * 2.0F) * 2.0F + 0.5F;
            cosTime = Math.max(0.0f, Math.min(cosTime, 1.0f));
            builder.putVec4(0.47f * cosTime, 0.66f * cosTime, 1.0f * cosTime, 1.0f);
        } catch (Exception e) {
            LOGGER.error("Failed to map Whiteout UBO", e);
            return;
        }

        RenderSystem.backupProjectionMatrix();
        
        try (RenderPass pass = encoder.createRenderPass(
                () -> "Nostalgia Radial Whiteout",
                target.getColorTextureView(),
                OptionalInt.empty(),
                null,
                OptionalDouble.empty()
        )) {
            pass.setPipeline(PIPELINE);
            pass.bindTexture("Sampler1", target.getDepthTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
            pass.setUniform("WhiteoutData", uboBuffer.currentBuffer());
            
            pass.draw(0, 3);
        }

        uboBuffer.rotate();
        RenderSystem.restoreProjectionMatrix();

    }
}
