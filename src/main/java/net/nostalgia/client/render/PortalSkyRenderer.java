package net.nostalgia.client.render;

import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.buffers.GpuBuffer.MappedView;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import com.mojang.logging.LogUtils;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MappableRingBuffer;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.phys.Vec3;
import net.nostalgia.client.events.caches.UniversalHologramCache;
import net.nostalgia.client.events.echo.RitualVisualManager;
import net.nostalgia.network.C2SSkyPortalLandingRequestPayload;
import net.sha.api.SHAHologramManager;
import net.sha.api.SHAMirageManager;
import org.joml.Matrix4f;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public class PortalSkyRenderer {
  private static final Logger LOGGER = LogUtils.getLogger();
  private static final Identifier RIFT_TEXTURE_ID = Identifier.fromNamespaceAndPath("nostalgia", "textures/environment/rift_data.png");
  public static final RenderPipeline PIPELINE = RenderPipeline.builder(new Snippet[0])
    .withLocation(Identifier.fromNamespaceAndPath("nostalgia", "portal_sky_rip_v2"))
    .withVertexShader(Identifier.fromNamespaceAndPath("nostalgia", "core/portal_sky_rip_v2"))
    .withFragmentShader(Identifier.fromNamespaceAndPath("nostalgia", "core/portal_sky_rip_v2"))
    .withSampler("Sampler1")
    .withSampler("Sampler2")
    .withUniform("WhiteoutData", UniformType.UNIFORM_BUFFER)
    .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
    .withDepthStencilState(new DepthStencilState(CompareOp.ALWAYS_PASS, false))
    .withVertexFormat(DefaultVertexFormat.EMPTY, Mode.TRIANGLES)
    .build();
  private static MappableRingBuffer uboBuffer;
  public static boolean active = false;
  public static boolean inverted = false;
  public static boolean isAnimatingOut = false;
  public static boolean islandVisible = false;
  public static float portalTime = 0.0F;
  public static BlockPos portalCenter = BlockPos.ZERO;
  public static int crackPlaneY = 256;
  public static int crackPlaneYTarget = 256;
  public static String targetDimension = "nostalgia:alpha_112_01";
  public static String sourceDimension = "minecraft:overworld";
  public static String originalSourceDimension = "minecraft:overworld";
  public static String originalTargetDimension = "nostalgia:alpha_112_01";
  public static volatile boolean skyPortalTransitioning = false;
  public static long skyPortalTransitionStartTime = 0L;
  private static String skyPortalPreviousDimension = null;
  private static boolean skyPortalWaitingForChunks = false;
  private static long skyPortalArrivalTime = 0L;
  public static Matrix4f capturedProjectionMatrix = null;
  public static Matrix4f capturedModelViewMatrix = null;
  private static long lastLandingSendTime = 0L;

  public PortalSkyRenderer() {
  }

  public static void startCloseAnimation() {
    if (active) {
      isAnimatingOut = true;
      if (portalTime > 3.3F) {
        portalTime = 3.3F;
      }
    }
  }

  private static void initUbo() {
    uboBuffer = new MappableRingBuffer(() -> "Nostalgia Portal UBO", 130, 128);
  }

  public static void render(DeltaTracker tracker) {
    render(Minecraft.getInstance().getMainRenderTarget(), tracker);
  }

  public static void render(RenderTarget target, DeltaTracker tracker) {
    if (active && portalCenter != null) {
      Minecraft mc = Minecraft.getInstance();
      if (target == null) {
        target = mc.getMainRenderTarget();
      }
      if (target != null && target.getColorTextureView() != null && target.getDepthTextureView() != null) {
        if (uboBuffer == null) {
          initUbo();
        }

        Camera camera = mc.gameRenderer.getMainCamera();
        Matrix4f invViewProj = UboShaderUtil.getInverseViewProjMatrix(camera, capturedProjectionMatrix);
        CommandEncoder encoder = RenderSystem.getDevice().createCommandEncoder();

        try {
          MappedView viewMapped = encoder.mapBuffer(uboBuffer.currentBuffer(), false, true);

          try {
            Std140Builder builder = Std140Builder.intoBuffer(viewMapped.data());
            Vec3 camPos = camera.position();
            double trueCenterX = portalCenter.getX();
            double trueCenterY = portalCenter.getY();
            double trueCenterZ = portalCenter.getZ();
            float tTime = portalTime;
            builder.putVec4((float)(trueCenterX + 0.5 - camPos.x), (float)(trueCenterY + 0.5 - camPos.y), (float)(trueCenterZ + 0.5 - camPos.z), tTime);
            builder.putMat4f(invViewProj);
            float camY = (float)camPos.y;
            int skyColorARGB = (Integer)camera.attributeProbe().getValue(EnvironmentAttributes.SKY_COLOR, tracker.getGameTimeDeltaTicks());
            float r = (skyColorARGB >> 16 & 0xFF) / 255.0F;
            float g = (skyColorARGB >> 8 & 0xFF) / 255.0F;
            float b = (skyColorARGB & 0xFF) / 255.0F;
            boolean isTarget = mc.level != null && mc.level.dimension().identifier().toString().equals(originalTargetDimension);
            int cPlaneY = isTarget ? crackPlaneYTarget : crackPlaneY;

            builder.putVec4((float)(cPlaneY - camPos.y), camY, 0.0F, 0.0F);
            builder.putVec4(0.0F, 0.0F, 0.0F, 256.0F);
            builder.putVec4(r, g, b, 1.0F);
          } catch (Throwable var26) {
            if (viewMapped != null) {
              try {
                viewMapped.close();
              } catch (Throwable var24) {
                var26.addSuppressed(var24);
              }
            }

            throw var26;
          }

          if (viewMapped != null) {
            viewMapped.close();
          }
        } catch (Exception var27) {
          LOGGER.error("Failed to map Portal Sky UBO", var27);
          return;
        }

        RenderSystem.backupProjectionMatrix();
        AbstractTexture riftTexture = mc.getTextureManager().getTexture(RIFT_TEXTURE_ID);
        RenderPass pass = encoder.createRenderPass(
          () -> "Nostalgia Portal Sky Rip", target.getColorTextureView(), OptionalInt.empty(), null, OptionalDouble.empty()
        );

        try {
          pass.setPipeline(PIPELINE);
          pass.bindTexture("Sampler1", target.getDepthTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
          pass.bindTexture("Sampler2", riftTexture.getTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
          pass.setUniform("WhiteoutData", uboBuffer.currentBuffer());
          pass.draw(0, 3);
        } catch (Throwable var25) {
          if (pass != null) {
            try {
              pass.close();
            } catch (Throwable var23) {
              var25.addSuppressed(var23);
            }
          }

          throw var25;
        }

        if (pass != null) {
          pass.close();
        }

        uboBuffer.rotate();
        RenderSystem.restoreProjectionMatrix();
      }
    }
  }

  public static void tickSkyPortalTransition() {
    Minecraft mc = Minecraft.getInstance();
    if (mc.level != null && mc.player != null) {
      if (!skyPortalTransitioning && active && islandVisible && !RitualVisualManager.isTransitioning) {
        detectLanding(mc);
      }

      if (skyPortalTransitioning) {
        tickPostTeleportWait(mc);
      }
    }
  }

  private static void detectLanding(Minecraft mc) {
    long now = System.currentTimeMillis();
    if (now - lastLandingSendTime >= 1500L) {
      if (mc.player.onGround()) {
        String currentDim = mc.level.dimension().identifier().toString();
        boolean inTarget = currentDim.equals(originalTargetDimension);
        int currentCrackPlaneY = inTarget ? crackPlaneYTarget : crackPlaneY;
        if (!(mc.player.getY() <= currentCrackPlaneY)) {
          if (portalCenter != null) {
            double dx = mc.player.getX() - portalCenter.getX();
            double dz = mc.player.getZ() - portalCenter.getZ();
            if (!(dx * dx + dz * dz > 82944.0)) {
              lastLandingSendTime = now;
              int inversionConstant = crackPlaneY + crackPlaneYTarget;
              double oldX = mc.player.getX();
              double oldZ = mc.player.getZ();
              double centerZ = portalCenter.getZ();
              double ox = 0.0;
              double oy = 0.0;
              double oz = 0.0;
              SHAMirageManager.isHologramReady = () -> !skyPortalTransitioning;
              SHAMirageManager.beginHandoff(250, ox, oy, oz);
              SHAMirageManager.flipY = true;
              SHAMirageManager.flipZ = inverted;
              SHAMirageManager.flipPivotY = inversionConstant + 1;
              SHAMirageManager.flipPivotZ = 2.0 * centerZ + 1.0;
              skyPortalTransitioning = true;
              skyPortalTransitionStartTime = System.currentTimeMillis();
              skyPortalPreviousDimension = currentDim;
              ClientPlayNetworking.send(
                new C2SSkyPortalLandingRequestPayload(mc.player.getX(), mc.player.getY(), mc.player.getZ(), mc.player.getYRot(), mc.player.getXRot())
              );
              LOGGER.info("[SkyPortal] Client detected landing at Y={}, sending teleport request", mc.player.getY());
            }
          }
        }
      }
    }
  }

  private static void tickPostTeleportWait(Minecraft mc) {
    String currentDim = mc.level.dimension().identifier().toString();
    if (!skyPortalWaitingForChunks) {
      if (!currentDim.equals(skyPortalPreviousDimension)) {
        skyPortalWaitingForChunks = true;
        skyPortalArrivalTime = System.currentTimeMillis();
        SHAHologramManager.updateSpatialMap(UniversalHologramCache.INSTANCE);
      } else if (System.currentTimeMillis() - skyPortalTransitionStartTime > 10000L) {
        endSkyPortalTransition();
      }
    } else {
      long timeSinceArrival = System.currentTimeMillis() - skyPortalArrivalTime;
      if (timeSinceArrival >= 700L) {
        int px = mc.player.getBlockX() >> 4;
        int pz = mc.player.getBlockZ() >> 4;
        int loaded = 0;

        for (int ddx = -1; ddx <= 1; ddx++) {
          for (int ddz = -1; ddz <= 1; ddz++) {
            if (mc.level.getChunkSource().hasChunk(px + ddx, pz + ddz)) {
              loaded++;
            }
          }
        }

        if (loaded >= 5 || timeSinceArrival > 15000L) {
          endSkyPortalTransition();
        }
      }
    }
  }

  private static void endSkyPortalTransition() {
    if (SHAMirageManager.isTransitioning) {
      SHAMirageManager.endTransition();
    }

    if (portalCenter != null) {
      SHAHologramManager.markRadiusShellDirty(portalCenter, 0.0F, 320.0F);
    }

    skyPortalTransitioning = false;
    skyPortalWaitingForChunks = false;
    skyPortalPreviousDimension = null;
    skyPortalArrivalTime = 0L;
  }
}
