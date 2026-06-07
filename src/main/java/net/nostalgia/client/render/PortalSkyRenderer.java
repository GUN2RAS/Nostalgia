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
    private static final net.minecraft.resources.Identifier RIFT_TEXTURE_ID = net.minecraft.resources.Identifier.fromNamespaceAndPath("nostalgia", "textures/environment/rift_data.png");

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

    public static boolean active = false;
    public static boolean inverted = false;
    public static boolean isAnimatingOut = false;
    public static boolean islandVisible = false;
    public static float portalTime = 0.0f;
    public static BlockPos portalCenter = BlockPos.ZERO;
    public static int crackPlaneY = 256;
    public static int crackPlaneYTarget = 256;
    public static String targetDimension = "nostalgia:alpha_112_01";
    public static String sourceDimension = "minecraft:overworld";
    public static String originalSourceDimension = "minecraft:overworld";
    public static String originalTargetDimension = "nostalgia:alpha_112_01";


    public static volatile boolean skyPortalTransitioning = false;
    public static long skyPortalTransitionStartTime = 0;
    private static String skyPortalPreviousDimension = null;
    private static boolean skyPortalWaitingForChunks = false;
    private static long skyPortalArrivalTime = 0;

    public static void startCloseAnimation() {
        if (active) {
            isAnimatingOut = true;
            if (portalTime > 3.3f) {
                portalTime = 3.3f;
            }
        }
    }

    public static org.joml.Matrix4f capturedProjectionMatrix = null;
    public static org.joml.Matrix4f capturedModelViewMatrix = null;

    private static void initUbo() {
        uboBuffer = new MappableRingBuffer(() -> "Nostalgia Portal UBO", 130, 128);
    }

    public static void render(DeltaTracker tracker) {
        if (!active && !net.nostalgia.client.events.echo.RitualVisualManager.isTransitioning) {
            return;
        }

        if (active && net.nostalgia.client.events.echo.RitualVisualManager.isTransitioning && !isAnimatingOut && !net.nostalgia.client.events.echo.RitualVisualManager.isBystander) {
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

        org.joml.Matrix4f invViewProj = UboShaderUtil.getInverseViewProjMatrix(camera, capturedProjectionMatrix);

        CommandEncoder encoder = RenderSystem.getDevice().createCommandEncoder();
        try (GpuBuffer.MappedView viewMapped = encoder.mapBuffer(uboBuffer.currentBuffer(), false, true)) {
            Std140Builder builder = Std140Builder.intoBuffer(viewMapped.data());

            net.minecraft.world.phys.Vec3 camPos = camera.position();

            double trueCenterX = active ? portalCenter.getX() : net.nostalgia.client.events.echo.RitualVisualManager.ritualCenter.getX();
            double trueCenterY = active ? portalCenter.getY() : net.nostalgia.client.events.echo.RitualVisualManager.ritualCenter.getY();
            double trueCenterZ = active ? portalCenter.getZ() : net.nostalgia.client.events.echo.RitualVisualManager.ritualCenter.getZ();

            float tTime = active ? portalTime : net.nostalgia.client.events.echo.RitualVisualManager.getTransitionTimeSeconds();

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

            int cPlaneY = 256;
            if (active) {
                boolean isTarget = mc.level != null && mc.level.dimension().identifier().toString().equals(originalTargetDimension);
                cPlaneY = isTarget ? crackPlaneYTarget : crackPlaneY;
            } else if (net.nostalgia.client.events.echo.RitualVisualManager.ritualCenter != null) {
                cPlaneY = net.nostalgia.client.events.echo.RitualVisualManager.ritualCenter.getY() + 90;
            }
            builder.putVec4((float) (cPlaneY - camPos.y), camY, 0.0f, 0.0f);
            builder.putVec4(0.0f, 0.0f, 0.0f, 256.0f);
            builder.putVec4(r, g, b, 1.0f);
        } catch (Exception e) {
            LOGGER.error("Failed to map Portal Sky UBO", e);
            return;
        }

        RenderSystem.backupProjectionMatrix();

        net.minecraft.client.renderer.texture.AbstractTexture riftTexture = mc.getTextureManager().getTexture(RIFT_TEXTURE_ID);

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



    private static long lastLandingSendTime = 0;


    public static void tickSkyPortalTransition() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) return;


        if (!skyPortalTransitioning && active && islandVisible) {
            detectLanding(mc);
        }


        if (skyPortalTransitioning) {
            tickPostTeleportWait(mc);
        }
    }

    private static void detectLanding(Minecraft mc) {

        long now = System.currentTimeMillis();
        if (now - lastLandingSendTime < 1500L) return;


        if (!mc.player.onGround()) return;


        String currentDim = mc.level.dimension().identifier().toString();
        boolean inTarget = currentDim.equals(originalTargetDimension);
        int currentCrackPlaneY = inTarget ? crackPlaneYTarget : crackPlaneY;
        if (mc.player.getY() <= currentCrackPlaneY) return;


        if (portalCenter == null) return;
        double dx = mc.player.getX() - portalCenter.getX();
        double dz = mc.player.getZ() - portalCenter.getZ();
        if (dx * dx + dz * dz > 288.0 * 288.0) return;



        lastLandingSendTime = now;


        int inversionConstant = crackPlaneY + crackPlaneYTarget;

        double oldX = mc.player.getX();
        double oldZ = mc.player.getZ();
        double centerZ = portalCenter.getZ();


        double ox = 0;
        double oy = 0;
        double oz = 0;


        net.sha.api.SHAMirageManager.isHologramReady = () -> !skyPortalTransitioning;
        net.sha.api.SHAMirageManager.beginHandoff(250, ox, oy, oz);

        net.sha.api.SHAMirageManager.flipY = true;
        net.sha.api.SHAMirageManager.flipZ = inverted;
        net.sha.api.SHAMirageManager.flipPivotY = inversionConstant + 1;
        net.sha.api.SHAMirageManager.flipPivotZ = 2.0 * centerZ + 1;


        skyPortalTransitioning = true;
        skyPortalTransitionStartTime = System.currentTimeMillis();
        skyPortalPreviousDimension = currentDim;


        net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.send(
            new net.nostalgia.network.C2SSkyPortalLandingRequestPayload(
                mc.player.getX(), mc.player.getY(), mc.player.getZ(),
                mc.player.getYRot(), mc.player.getXRot()));

        LOGGER.info("[SkyPortal] Client detected landing at Y={}, sending teleport request", mc.player.getY());
    }

    private static void tickPostTeleportWait(Minecraft mc) {
        String currentDim = mc.level.dimension().identifier().toString();

        if (!skyPortalWaitingForChunks) {

            if (!currentDim.equals(skyPortalPreviousDimension)) {

                skyPortalWaitingForChunks = true;
                skyPortalArrivalTime = System.currentTimeMillis();
                net.sha.api.SHAHologramManager.updateSpatialMap(
                    net.nostalgia.client.events.caches.UniversalHologramCache.INSTANCE);
            } else {

                if (System.currentTimeMillis() - skyPortalTransitionStartTime > 10000) {
                    endSkyPortalTransition();
                }
            }
            return;
        }


        long timeSinceArrival = System.currentTimeMillis() - skyPortalArrivalTime;
        if (timeSinceArrival < 700) return;

        int px = mc.player.getBlockX() >> 4;
        int pz = mc.player.getBlockZ() >> 4;
        int loaded = 0;
        for (int ddx = -1; ddx <= 1; ddx++) {
            for (int ddz = -1; ddz <= 1; ddz++) {
                if (mc.level.getChunkSource().hasChunk(px + ddx, pz + ddz)) loaded++;
            }
        }

        if (loaded >= 5 || timeSinceArrival > 15000) {
            endSkyPortalTransition();
        }
    }

    private static void endSkyPortalTransition() {
        if (net.sha.api.SHAMirageManager.isTransitioning) {
            net.sha.api.SHAMirageManager.endTransition();
        }
        if (portalCenter != null) {
            net.sha.api.SHAHologramManager.markRadiusShellDirty(portalCenter, 0.0f, 320.0f);
        }
        skyPortalTransitioning = false;
        skyPortalWaitingForChunks = false;
        skyPortalPreviousDimension = null;
        skyPortalArrivalTime = 0;
    }
}
