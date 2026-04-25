package net.nostalgia.client.render;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.Std140Builder;
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
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.shaders.UniformType;
import net.minecraft.core.BlockPos;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

import java.util.OptionalDouble;
import java.util.OptionalInt;

@Environment(EnvType.CLIENT)
public class PortalSkyRenderer {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final RenderPipeline PIPELINE = RenderPipeline.builder()
            .withLocation(net.minecraft.resources.Identifier.fromNamespaceAndPath("nostalgia", "portal_sky_rip_v2"))
            .withVertexShader(net.minecraft.resources.Identifier.fromNamespaceAndPath("nostalgia", "core/portal_sky_rip_v2"))
            .withFragmentShader(net.minecraft.resources.Identifier.fromNamespaceAndPath("nostalgia", "core/portal_sky_rip_v2"))
            .withSampler("Sampler1")
            .withSampler("Sampler2")
            .withUniform("WhiteoutData", UniformType.UNIFORM_BUFFER)
            .withColorTargetState(new com.mojang.blaze3d.pipeline.ColorTargetState(com.mojang.blaze3d.pipeline.BlendFunction.TRANSLUCENT))
            .withDepthStencilState(new com.mojang.blaze3d.pipeline.DepthStencilState(com.mojang.blaze3d.platform.CompareOp.ALWAYS_PASS, false))
            .withVertexFormat(DefaultVertexFormat.EMPTY, VertexFormat.Mode.TRIANGLES)
            .build();

    private static MappableRingBuffer uboBuffer;

    public static boolean isDebugging = false;
    public static boolean isDebuggingInverted = false;
    public static boolean isAnimatingOut = false;
    public static boolean islandVisible = false;
    public static float debugTime = 0.0f;
    public static BlockPos debugCenter = BlockPos.ZERO;

    public static void startCloseAnimation() {
        if (isDebugging) {
            isAnimatingOut = true;
            if (debugTime > 3.3f) {
                debugTime = 3.3f;
            }
        }
    }

    public static org.joml.Matrix4f capturedProjectionMatrix = null;
    public static org.joml.Matrix4f capturedModelViewMatrix = null;

    private static void initUbo() {
        uboBuffer = new MappableRingBuffer(() -> "Nostalgia Portal UBO", 130, 128);
    }

    public static void render(DeltaTracker tracker) {
        if (!isDebugging && !net.nostalgia.client.ritual.RitualVisualManager.isTransitioning) {
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

        net.minecraft.client.Camera camera = mc.gameRenderer.getMainCamera();

        org.joml.Matrix4f invViewProj;
        if (capturedProjectionMatrix != null && capturedModelViewMatrix != null) {
            invViewProj = new org.joml.Matrix4f(capturedProjectionMatrix).mul(capturedModelViewMatrix).invert();
        } else if (capturedProjectionMatrix != null) {
            org.joml.Matrix4f viewMatrix = camera.getViewRotationMatrix(new org.joml.Matrix4f());
            invViewProj = new org.joml.Matrix4f(capturedProjectionMatrix).mul(viewMatrix).invert();
        } else {
            invViewProj = camera.getViewRotationProjectionMatrix(new org.joml.Matrix4f()).invert();
        }

        CommandEncoder encoder = RenderSystem.getDevice().createCommandEncoder();
        try (GpuBuffer.MappedView viewMapped = encoder.mapBuffer(uboBuffer.currentBuffer(), false, true)) {
            Std140Builder builder = Std140Builder.intoBuffer(viewMapped.data());

            net.minecraft.world.phys.Vec3 camPos = camera.position();

            double trueCenterX = isDebugging ? debugCenter.getX() : net.nostalgia.client.ritual.RitualVisualManager.ritualCenter.getX();
            double trueCenterY = isDebugging ? debugCenter.getY() : net.nostalgia.client.ritual.RitualVisualManager.ritualCenter.getY();
            double trueCenterZ = isDebugging ? debugCenter.getZ() : net.nostalgia.client.ritual.RitualVisualManager.ritualCenter.getZ();

            float tTime = isDebugging ? debugTime : net.nostalgia.client.ritual.RitualVisualManager.getTransitionTimeSeconds();

            builder.putVec4(
                    (float) (trueCenterX + 0.5d - camPos.x),
                    (float) (trueCenterY + 0.5d - camPos.y),
                    (float) (trueCenterZ + 0.5d - camPos.z),
                    tTime
            );
            builder.putMat4f(invViewProj);

            float camY = (float) camPos.y;
            int skyColorARGB = camera.attributeProbe().getValue(net.minecraft.world.attribute.EnvironmentAttributes.SKY_COLOR, tracker.getGameTimeDeltaTicks());
            float r = (float)(skyColorARGB >> 16 & 255) / 255.0F;
            float g = (float)(skyColorARGB >> 8 & 255) / 255.0F;
            float b = (float)(skyColorARGB & 255) / 255.0F;

            builder.putVec4(0.0f, camY, 0.0f, 0.0f);
            builder.putVec4(0.0f, 0.0f, 0.0f, 256.0f);
            builder.putVec4(r, g, b, 1.0f);
        } catch (Exception e) {
            LOGGER.error("Failed to map Portal Sky UBO", e);
            return;
        }

        RenderSystem.backupProjectionMatrix();

        net.minecraft.client.renderer.texture.AbstractTexture riftTexture = mc.getTextureManager().getTexture(net.minecraft.resources.Identifier.fromNamespaceAndPath("nostalgia", "textures/environment/rift_data.png"));

        try (RenderPass pass = encoder.createRenderPass(
                () -> "Nostalgia Portal Sky Rip",
                target.getColorTextureView(),
                OptionalInt.empty(),
                null,
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
