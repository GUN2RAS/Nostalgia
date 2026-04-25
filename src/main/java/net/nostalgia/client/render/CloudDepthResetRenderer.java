package net.nostalgia.client.render;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.textures.FilterMode;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MappableRingBuffer;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.shaders.UniformType;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

import java.util.OptionalDouble;
import java.util.OptionalInt;

@Environment(EnvType.CLIENT)
public class CloudDepthResetRenderer {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final RenderPipeline PIPELINE = RenderPipeline.builder()
            .withLocation(net.minecraft.resources.Identifier.fromNamespaceAndPath("nostalgia", "cloud_depth_reset"))
            .withVertexShader(net.minecraft.resources.Identifier.fromNamespaceAndPath("nostalgia", "core/portal_sky_rip_v2"))
            .withFragmentShader(net.minecraft.resources.Identifier.fromNamespaceAndPath("nostalgia", "core/cloud_depth_reset"))
            .withSampler("Sampler1")
            .withSampler("Sampler2")
            .withUniform("WhiteoutData", UniformType.UNIFORM_BUFFER)
            .withColorTargetState(new com.mojang.blaze3d.pipeline.ColorTargetState(java.util.Optional.empty(), com.mojang.blaze3d.pipeline.ColorTargetState.WRITE_NONE))
            .withDepthStencilState(new com.mojang.blaze3d.pipeline.DepthStencilState(com.mojang.blaze3d.platform.CompareOp.ALWAYS_PASS, true))
            .withVertexFormat(DefaultVertexFormat.EMPTY, VertexFormat.Mode.TRIANGLES)
            .build();

    private static MappableRingBuffer uboBuffer;

    private static void initUbo() {
        uboBuffer = new MappableRingBuffer(() -> "Nostalgia Cloud Depth Reset UBO", 130, 128);
    }

    public static boolean shouldRender() {
        if (IrisCompat.isShaderPackActive()) return false;
        return PortalSkyRenderer.isDebugging || net.nostalgia.client.ritual.RitualVisualManager.isTransitioning;
    }

    public static void render(RenderTarget target, DeltaTracker tracker) {
        if (target == null || target.getColorTextureView() == null || target.getDepthTextureView() == null) {
            return;
        }

        if (uboBuffer == null) {
            initUbo();
        }

        Minecraft mc = Minecraft.getInstance();
        net.minecraft.client.Camera camera = mc.gameRenderer.getMainCamera();

        org.joml.Matrix4f invViewProj;
        if (PortalSkyRenderer.capturedProjectionMatrix != null && PortalSkyRenderer.capturedModelViewMatrix != null) {
            invViewProj = new org.joml.Matrix4f(PortalSkyRenderer.capturedProjectionMatrix).mul(PortalSkyRenderer.capturedModelViewMatrix).invert();
        } else if (PortalSkyRenderer.capturedProjectionMatrix != null) {
            org.joml.Matrix4f viewMatrix = camera.getViewRotationMatrix(new org.joml.Matrix4f());
            invViewProj = new org.joml.Matrix4f(PortalSkyRenderer.capturedProjectionMatrix).mul(viewMatrix).invert();
        } else {
            invViewProj = camera.getViewRotationProjectionMatrix(new org.joml.Matrix4f()).invert();
        }

        CommandEncoder encoder = RenderSystem.getDevice().createCommandEncoder();
        try (GpuBuffer.MappedView viewMapped = encoder.mapBuffer(uboBuffer.currentBuffer(), false, true)) {
            Std140Builder builder = Std140Builder.intoBuffer(viewMapped.data());

            net.minecraft.world.phys.Vec3 camPos = camera.position();

            double trueCenterX = PortalSkyRenderer.isDebugging ? PortalSkyRenderer.debugCenter.getX() : net.nostalgia.client.ritual.RitualVisualManager.ritualCenter.getX();
            double trueCenterY = PortalSkyRenderer.isDebugging ? PortalSkyRenderer.debugCenter.getY() : net.nostalgia.client.ritual.RitualVisualManager.ritualCenter.getY();
            double trueCenterZ = PortalSkyRenderer.isDebugging ? PortalSkyRenderer.debugCenter.getZ() : net.nostalgia.client.ritual.RitualVisualManager.ritualCenter.getZ();

            float tTime = PortalSkyRenderer.isDebugging ? PortalSkyRenderer.debugTime : net.nostalgia.client.ritual.RitualVisualManager.getTransitionTimeSeconds();

            builder.putVec4(
                    (float) (trueCenterX + 0.5d - camPos.x),
                    (float) (trueCenterY + 0.5d - camPos.y),
                    (float) (trueCenterZ + 0.5d - camPos.z),
                    tTime
            );
            builder.putMat4f(invViewProj);

            float camY = (float) camPos.y;
            builder.putVec4(0.0f, camY, 0.0f, 0.0f);
            builder.putVec4(0.0f, 0.0f, 0.0f, 256.0f);
            builder.putVec4(0.0f, 0.0f, 0.0f, 1.0f);
        } catch (Exception e) {
            LOGGER.error("Failed to map Cloud Depth Reset UBO", e);
            return;
        }

        RenderSystem.backupProjectionMatrix();

        net.minecraft.client.renderer.texture.AbstractTexture riftTexture = mc.getTextureManager().getTexture(net.minecraft.resources.Identifier.fromNamespaceAndPath("nostalgia", "textures/environment/rift_data.png"));

        try (RenderPass pass = encoder.createRenderPass(
                () -> "Nostalgia Cloud Depth Reset",
                target.getColorTextureView(),
                OptionalInt.empty(),
                target.getDepthTextureView(),
                OptionalDouble.empty()
        )) {
            pass.setPipeline(PIPELINE);
            pass.bindTexture("Sampler1", target.getDepthTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
            pass.bindTexture("Sampler2", riftTexture.getTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
            pass.setUniform("WhiteoutData", uboBuffer.currentBuffer());

            pass.draw(0, 3);
        }

        uboBuffer.rotate();
        RenderSystem.restoreProjectionMatrix();
    }
}
