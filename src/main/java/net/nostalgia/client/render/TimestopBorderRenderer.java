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
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MappableRingBuffer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import net.nostalgia.alphalogic.ritual.FreezeRegion;
import net.nostalgia.alphalogic.ritual.TickRateManagerAccess;
import org.joml.Matrix4f;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public class TimestopBorderRenderer {
  private static final Logger LOGGER = LogUtils.getLogger();
  public static final RenderPipeline PIPELINE = RenderPipeline.builder(new Snippet[0])
    .withLocation(Identifier.fromNamespaceAndPath("nostalgia", "timestop_border"))
    .withVertexShader(Identifier.fromNamespaceAndPath("nostalgia", "core/portal_sky_rip"))
    .withFragmentShader(Identifier.fromNamespaceAndPath("nostalgia", "core/timestop_border"))
    .withSampler("Sampler1")
    .withUniform("TimestopData", UniformType.UNIFORM_BUFFER)
    .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
    .withDepthStencilState(new DepthStencilState(CompareOp.ALWAYS_PASS, false))
    .withVertexFormat(DefaultVertexFormat.EMPTY, Mode.TRIANGLES)
    .build();
  private static MappableRingBuffer uboBuffer;
  private static final List<TimestopBorderRenderer.VisualZone> activeVisualZones = new ArrayList<>();
  public static Matrix4f capturedProjectionMatrix = null;

  public TimestopBorderRenderer() {
  }

  private static void initUbo() {
    uboBuffer = new MappableRingBuffer(() -> "Nostalgia Timestop UBO", 130, 256);
  }

  public static void render(DeltaTracker tracker) {
    Minecraft mc = Minecraft.getInstance();
    if (mc.level != null && mc.player != null) {
      TickRateManagerAccess access = (TickRateManagerAccess)mc.level.tickRateManager();
      if (access != null) {
        List<FreezeRegion> currentRegions = access.nostalgia$regions().stream().filter(r -> r.dimension().equals(mc.level.dimension())).toList();
        long currentTime = System.currentTimeMillis();

        for (FreezeRegion r : currentRegions) {
          boolean found = false;

          for (TimestopBorderRenderer.VisualZone vz : activeVisualZones) {
            if (vz.beaconPos.equals(r.beaconPos())) {
              found = true;
              if (vz.endTime != -1L) {
                vz.endTime = -1L;
              }
              break;
            }
          }

          if (!found) {
            activeVisualZones.add(new TimestopBorderRenderer.VisualZone(r.beaconPos(), r.chunkRadius()));
          }
        }

        for (TimestopBorderRenderer.VisualZone vzx : activeVisualZones) {
          if (vzx.endTime == -1L) {
            boolean found = false;

            for (FreezeRegion r : currentRegions) {
              if (r.beaconPos().equals(vzx.beaconPos)) {
                found = true;
                break;
              }
            }

            if (!found) {
              vzx.endTime = currentTime;
            }
          }
        }

        activeVisualZones.removeIf(vzxx -> vzxx.endTime != -1L && currentTime - vzxx.endTime > 1500L);
        if (!activeVisualZones.isEmpty()) {
          List<TimestopBorderRenderer.VisualZone> renderingZones = activeVisualZones.stream().limit(4L).toList();
          RenderTarget target = mc.getMainRenderTarget();
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

                for (int i = 0; i < 4; i++) {
                  if (i < renderingZones.size()) {
                    TimestopBorderRenderer.VisualZone vzxx = renderingZones.get(i);
                    int bx = vzxx.beaconPos.getX() >> 4;
                    int bz = vzxx.beaconPos.getZ() >> 4;
                    float trueCenterX = bx * 16 + 8.0F;
                    float trueCenterZ = bz * 16 + 8.0F;
                    float targetRadius = vzxx.chunkRadius * 16.0F + 8.0F;
                    float currentRadius;
                    if (vzxx.endTime != -1L) {
                      currentRadius = targetRadius;
                    } else {
                      float progress = (float)(currentTime - vzxx.startTime) / 1500.0F;
                      progress = Math.max(0.0F, Math.min(1.0F, progress));
                      float inv = 1.0F - progress;
                      float smooth = 1.0F - inv * inv * inv;
                      currentRadius = targetRadius * smooth;
                    }

                    float relX = (float)(trueCenterX - camPos.x);
                    float relY = (float)(0.0 - camPos.y);
                    float relZ = (float)(trueCenterZ - camPos.z);
                    builder.putVec4(relX, relY, relZ, currentRadius);
                  } else {
                    builder.putVec4(0.0F, 0.0F, 0.0F, 0.0F);
                  }
                }

                for (int ix = 0; ix < 4; ix++) {
                  if (ix < renderingZones.size()) {
                    TimestopBorderRenderer.VisualZone vzxx = renderingZones.get(ix);
                    float fade = 0.0F;
                    if (vzxx.endTime != -1L) {
                      float progress = (float)(currentTime - vzxx.endTime) / 1500.0F;
                      fade = Math.max(0.0F, Math.min(1.0F, progress));
                    }

                    builder.putVec4(fade, 0.0F, 0.0F, 0.0F);
                  } else {
                    builder.putVec4(0.0F, 0.0F, 0.0F, 0.0F);
                  }
                }

                builder.putMat4f(invViewProj);
                float time = UboShaderUtil.getShaderTimeSeconds(tracker);
                builder.putVec4((float)camPos.x, (float)camPos.y, (float)camPos.z, time);
                builder.putVec4(renderingZones.size(), 0.0F, 0.0F, 0.0F);
              } catch (Throwable var28) {
                if (viewMapped != null) {
                  try {
                    viewMapped.close();
                  } catch (Throwable var26) {
                    var28.addSuppressed(var26);
                  }
                }

                throw var28;
              }

              if (viewMapped != null) {
                viewMapped.close();
              }
            } catch (Exception var29) {
              LOGGER.error("Failed to map Timestop Border UBO", var29);
              return;
            }

            RenderSystem.backupProjectionMatrix();
            RenderPass pass = encoder.createRenderPass(
              () -> "Nostalgia Timestop Border", target.getColorTextureView(), OptionalInt.empty(), null, OptionalDouble.empty()
            );

            try {
              pass.setPipeline(PIPELINE);
              pass.bindTexture("Sampler1", target.getDepthTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
              pass.setUniform("TimestopData", uboBuffer.currentBuffer());
              pass.draw(0, 3);
            } catch (Throwable var27) {
              if (pass != null) {
                try {
                  pass.close();
                } catch (Throwable var25) {
                  var27.addSuppressed(var25);
                }
              }

              throw var27;
            }

            if (pass != null) {
              pass.close();
            }

            uboBuffer.rotate();
            RenderSystem.restoreProjectionMatrix();
          }
        }
      }
    }
  }

  public static class VisualZone {
    public final BlockPos beaconPos;
    public final int chunkRadius;
    public final long startTime;
    public long endTime = -1L;

    public VisualZone(BlockPos beaconPos, int chunkRadius) {
      this.beaconPos = beaconPos;
      this.chunkRadius = chunkRadius;
      this.startTime = System.currentTimeMillis();
    }
  }
}
