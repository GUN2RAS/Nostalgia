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
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MappableRingBuffer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.phys.Vec3;
import net.nostalgia.client.events.echo.RitualVisualManager;
import org.joml.Matrix4f;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public class GlassBreakRenderer {
  private static final Logger LOGGER = LogUtils.getLogger();
  public static final float GLASS_Y_OFFSET = 10.0F;
  public static final int GLASS_CELLS_X = 5;
  public static final int GLASS_CELLS_Y = 3;
  public static final float CELL_SIZE = 1.6F;
  public static final float GLASS_WIDTH = 8.0F;
  public static final float GLASS_HEIGHT = 4.8F;
  public static final float INTACT_TIME = 3.5F;
  public static final float DURATION = 99999.0F;
  public static final RenderPipeline PIPELINE = RenderPipeline.builder(new Snippet[0])
    .withLocation(Identifier.fromNamespaceAndPath("nostalgia", "glass_break"))
    .withVertexShader(Identifier.fromNamespaceAndPath("nostalgia", "core/portal_sky_rip_v2"))
    .withFragmentShader(Identifier.fromNamespaceAndPath("nostalgia", "core/glass_break"))
    .withSampler("Sampler1")
    .withUniform("GlassData", UniformType.UNIFORM_BUFFER)
    .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
    .withDepthStencilState(new DepthStencilState(CompareOp.ALWAYS_PASS, false))
    .withVertexFormat(DefaultVertexFormat.EMPTY, Mode.TRIANGLES)
    .build();
  private static MappableRingBuffer uboBuffer;
  public static boolean active = false;
  public static long startMs = 0L;
  public static BlockPos anchor = BlockPos.ZERO;

  public GlassBreakRenderer() {
  }

  public static void start(BlockPos pos) {
    active = true;
    startMs = RitualVisualManager.getVisualTime();
    anchor = pos;
  }

  public static void stop() {
    active = false;
  }

  public static float getTimeSeconds() {
    if (!active) {
      return 0.0F;
    } else {
      long elapsed = RitualVisualManager.getVisualTime() - startMs;
      return (float)elapsed / 1000.0F;
    }
  }

  private static void initUbo() {
    uboBuffer = new MappableRingBuffer(() -> "Nostalgia GlassBreak UBO", 130, 128);
  }

  public static void render(DeltaTracker tracker) {
    if (active) {
      float tSec = getTimeSeconds();
      if (tSec > 99999.0F) {
        stop();
      } else {
        Minecraft mc = Minecraft.getInstance();
        RenderTarget target = mc.getMainRenderTarget();
        if (target != null && target.getColorTextureView() != null && target.getDepthTextureView() != null) {
          if (uboBuffer == null) {
            initUbo();
          }

          Camera camera = mc.gameRenderer.getMainCamera();
          Matrix4f invViewProj;
          if (PortalSkyRenderer.capturedProjectionMatrix != null && PortalSkyRenderer.capturedModelViewMatrix != null) {
            invViewProj = new Matrix4f(PortalSkyRenderer.capturedProjectionMatrix).mul(PortalSkyRenderer.capturedModelViewMatrix).invert();
          } else if (PortalSkyRenderer.capturedProjectionMatrix != null) {
            Matrix4f viewMatrix = camera.getViewRotationMatrix(new Matrix4f());
            invViewProj = new Matrix4f(PortalSkyRenderer.capturedProjectionMatrix).mul(viewMatrix).invert();
          } else {
            invViewProj = camera.getViewRotationProjectionMatrix(new Matrix4f()).invert();
          }

          CommandEncoder encoder = RenderSystem.getDevice().createCommandEncoder();

          try {
            MappedView viewMapped = encoder.mapBuffer(uboBuffer.currentBuffer(), false, true);

            try {
              Std140Builder builder = Std140Builder.intoBuffer(viewMapped.data());
              Vec3 camPos = camera.position();
              double glassCenterX = anchor.getX() + 0.5;
              double glassCenterY = anchor.getY() + 10.0F;
              double glassCenterZ = anchor.getZ() + 0.5;
              float glassRelX = (float)(glassCenterX - camPos.x);
              float glassRelY = (float)(glassCenterY - camPos.y);
              float glassRelZ = (float)(glassCenterZ - camPos.z);
              float crashTime = Math.max(0.0F, tSec - 3.5F);
              float globalFade = 1.0F;
              int skyColorARGB = (Integer)camera.attributeProbe().getValue(EnvironmentAttributes.SKY_COLOR, tracker.getGameTimeDeltaTicks());
              float r = (skyColorARGB >> 16 & 0xFF) / 255.0F;
              float g = (skyColorARGB >> 8 & 0xFF) / 255.0F;
              float b = (skyColorARGB & 0xFF) / 255.0F;
              builder.putVec4(glassRelX, glassRelY, glassRelZ, crashTime);
              builder.putMat4f(invViewProj);
              builder.putVec4(8.0F, 4.8F, 5.0F, 3.0F);
              builder.putVec4(3.5F, globalFade, tSec, 1.0F);
              builder.putVec4(r, g, b, 1.0F);
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
            LOGGER.error("Failed to map GlassBreak UBO", var29);
            return;
          }

          RenderSystem.backupProjectionMatrix();
          RenderPass pass = encoder.createRenderPass(
            () -> "Nostalgia GlassBreak", target.getColorTextureView(), OptionalInt.empty(), null, OptionalDouble.empty()
          );

          try {
            pass.setPipeline(PIPELINE);
            pass.bindTexture("Sampler1", target.getDepthTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
            pass.setUniform("GlassData", uboBuffer.currentBuffer());
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
