package net.nostalgia.client.render;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
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
public class GlassBreakRenderer {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final float GLASS_Y_OFFSET = 10.0f;
    public static final int   GLASS_CELLS_X  = 5;
    public static final int   GLASS_CELLS_Y  = 3;
    public static final float CELL_SIZE      = 1.6f;
    public static final float GLASS_WIDTH    = GLASS_CELLS_X * CELL_SIZE;
    public static final float GLASS_HEIGHT   = GLASS_CELLS_Y * CELL_SIZE;
    public static final float INTACT_TIME    = 3.5f;
    public static final float DURATION       = 99999.0f;

    public static final RenderPipeline PIPELINE = RenderPipeline.builder()
            .withLocation(net.minecraft.resources.Identifier.fromNamespaceAndPath("nostalgia", "glass_break"))
            .withVertexShader(net.minecraft.resources.Identifier.fromNamespaceAndPath("nostalgia", "core/portal_sky_rip_v2"))
            .withFragmentShader(net.minecraft.resources.Identifier.fromNamespaceAndPath("nostalgia", "core/glass_break"))
            .withSampler("Sampler1")
            .withUniform("GlassData", UniformType.UNIFORM_BUFFER)
            .withColorTargetState(new com.mojang.blaze3d.pipeline.ColorTargetState(com.mojang.blaze3d.pipeline.BlendFunction.TRANSLUCENT))
            .withDepthStencilState(new com.mojang.blaze3d.pipeline.DepthStencilState(com.mojang.blaze3d.platform.CompareOp.ALWAYS_PASS, false))
            .withVertexFormat(DefaultVertexFormat.EMPTY, VertexFormat.Mode.TRIANGLES)
            .build();

    private static MappableRingBuffer uboBuffer;

    public static boolean active = false;
    public static long startMs = 0L;
    public static BlockPos anchor = BlockPos.ZERO;

    public static void start(BlockPos pos) {
        active = true;
        startMs = net.nostalgia.client.ritual.RitualVisualManager.getVisualTime();
        anchor = pos;
    }

    public static void stop() {
        active = false;
    }

    public static float getTimeSeconds() {
        if (!active) return 0.0f;
        long elapsed = net.nostalgia.client.ritual.RitualVisualManager.getVisualTime() - startMs;
        return elapsed / 1000.0f;
    }

    private static void initUbo() {
        uboBuffer = new MappableRingBuffer(() -> "Nostalgia GlassBreak UBO", 130, 128);
    }

    public static void render(DeltaTracker tracker) {
        if (!active) return;

        float tSec = getTimeSeconds();
        if (tSec > DURATION) {
            stop();
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

            double glassCenterX = anchor.getX() + 0.5d;
            double glassCenterY = anchor.getY() + GLASS_Y_OFFSET;
            double glassCenterZ = anchor.getZ() + 0.5d;

            float glassRelX = (float) (glassCenterX - camPos.x);
            float glassRelY = (float) (glassCenterY - camPos.y);
            float glassRelZ = (float) (glassCenterZ - camPos.z);

            float crashTime = Math.max(0.0f, tSec - INTACT_TIME);

            float globalFade = 1.0f;

            int skyColorARGB = camera.attributeProbe().getValue(
                net.minecraft.world.attribute.EnvironmentAttributes.SKY_COLOR,
                tracker.getGameTimeDeltaTicks()
            );
            float r = ((skyColorARGB >> 16) & 255) / 255.0F;
            float g = ((skyColorARGB >> 8) & 255) / 255.0F;
            float b = (skyColorARGB & 255) / 255.0F;

            builder.putVec4(glassRelX, glassRelY, glassRelZ, crashTime);
            builder.putMat4f(invViewProj);
            builder.putVec4(GLASS_WIDTH, GLASS_HEIGHT, (float) GLASS_CELLS_X, (float) GLASS_CELLS_Y);
            builder.putVec4(INTACT_TIME, globalFade, tSec, 1.0f);
            builder.putVec4(r, g, b, 1.0f);
        } catch (Exception e) {
            LOGGER.error("Failed to map GlassBreak UBO", e);
            return;
        }

        RenderSystem.backupProjectionMatrix();

        try (RenderPass pass = encoder.createRenderPass(
                () -> "Nostalgia GlassBreak",
                target.getColorTextureView(),
                OptionalInt.empty(),
                null,
                OptionalDouble.empty()
        )) {
            pass.setPipeline(PIPELINE);
            pass.bindTexture("Sampler1", target.getDepthTextureView(),
                    RenderSystem.getSamplerCache().getClampToEdge(com.mojang.blaze3d.textures.FilterMode.NEAREST));
            pass.setUniform("GlassData", uboBuffer.currentBuffer());

            pass.draw(0, 3);
        }

        uboBuffer.rotate();
        RenderSystem.restoreProjectionMatrix();
    }
}
