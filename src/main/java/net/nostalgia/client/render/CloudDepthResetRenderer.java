package net.nostalgia.client.render;

import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.buffers.GpuBuffer.MappedView;
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
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MappableRingBuffer;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import net.nostalgia.alphalogic.ritual.event.ClientEchoRitualView;
import net.nostalgia.alphalogic.ritual.event.SkyPortalEvent;
import net.nostalgia.client.events.core.ClientRitualEventRegistry;
import net.nostalgia.client.events.echo.RitualVisualManager;
import org.joml.Matrix4f;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public class CloudDepthResetRenderer {
  private static final Logger LOGGER = LogUtils.getLogger();
  public static final RenderPipeline PIPELINE = RenderPipeline.builder(new Snippet[0])
    .withLocation(Identifier.fromNamespaceAndPath("nostalgia", "cloud_depth_reset"))
    .withVertexShader(Identifier.fromNamespaceAndPath("nostalgia", "core/portal_sky_rip_v2"))
    .withFragmentShader(Identifier.fromNamespaceAndPath("nostalgia", "core/cloud_depth_reset"))
    .withSampler("Sampler1")
    .withSampler("Sampler2")
    .withUniform("WhiteoutData", UniformType.UNIFORM_BUFFER)
    .withColorTargetState(new ColorTargetState(Optional.empty(), 0))
    .withDepthStencilState(new DepthStencilState(CompareOp.ALWAYS_PASS, true))
    .withVertexFormat(DefaultVertexFormat.EMPTY, Mode.TRIANGLES)
    .build();
  private static MappableRingBuffer uboBuffer;

  public CloudDepthResetRenderer() {
  }

  private static void initUbo() {
    uboBuffer = new MappableRingBuffer(() -> "Nostalgia Cloud Depth Reset UBO", 130, 128);
  }

  public static boolean shouldRender() {
    return IrisCompat.isShaderPackActive()
      ? false
      : ClientRitualEventRegistry.activeSkyPortal() != null || ClientRitualEventRegistry.activeTransition() != null;
  }

  public static void render(RenderTarget target, DeltaTracker tracker) {
    if (target != null && target.getColorTextureView() != null && target.getDepthTextureView() != null) {
      if (uboBuffer == null) {
        initUbo();
      }

      Minecraft mc = Minecraft.getInstance();
      Camera camera = mc.gameRenderer.getMainCamera();
      Matrix4f invViewProj = UboShaderUtil.getInverseViewProjMatrix(camera, PortalSkyRenderer.capturedProjectionMatrix);
      CommandEncoder encoder = RenderSystem.getDevice().createCommandEncoder();

      try {
        MappedView viewMapped = encoder.mapBuffer(uboBuffer.currentBuffer(), false, true);

        try {
          Std140Builder builder = Std140Builder.intoBuffer(viewMapped.data());
          Vec3 camPos = camera.position();
          SkyPortalEvent skyPortal = ClientRitualEventRegistry.activeSkyPortal();
          ClientEchoRitualView transition = ClientRitualEventRegistry.activeTransition();
          BlockPos center = skyPortal != null ? skyPortal.center() : transition.ritualCenter();
          double trueCenterX = center.getX();
          double trueCenterY = center.getY();
          double trueCenterZ = center.getZ();
          float tTime = skyPortal != null ? skyPortal.time() : transition.transitionTimeSeconds();
          builder.putVec4((float)(trueCenterX + 0.5 - camPos.x), (float)(trueCenterY + 0.5 - camPos.y), (float)(trueCenterZ + 0.5 - camPos.z), tTime);
          builder.putMat4f(invViewProj);
          float camY = (float)camPos.y;
          builder.putVec4(0.0F, camY, 0.0F, 0.0F);
          float portalRadius = RitualVisualManager.getAlphaRadius();
          if (portalRadius < 5.0F) {
            portalRadius = 5.0F;
          }

          builder.putVec4(0.0F, 0.0F, 0.0F, portalRadius);
          builder.putVec4(0.0F, 0.0F, 0.0F, 1.0F);
        } catch (Throwable var24) {
          if (viewMapped != null) {
            try {
              viewMapped.close();
            } catch (Throwable var22) {
              var24.addSuppressed(var22);
            }
          }

          throw var24;
        }

        if (viewMapped != null) {
          viewMapped.close();
        }
      } catch (Exception var25) {
        LOGGER.error("Failed to map Cloud Depth Reset UBO", var25);
        return;
      }

      RenderSystem.backupProjectionMatrix();
      AbstractTexture riftTexture = mc.getTextureManager().getTexture(Identifier.fromNamespaceAndPath("nostalgia", "textures/environment/rift_data.png"));
      RenderPass pass = encoder.createRenderPass(
        () -> "Nostalgia Cloud Depth Reset", target.getColorTextureView(), OptionalInt.empty(), target.getDepthTextureView(), OptionalDouble.empty()
      );

      try {
        pass.setPipeline(PIPELINE);
        pass.bindTexture("Sampler1", target.getDepthTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
        pass.bindTexture("Sampler2", riftTexture.getTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
        pass.setUniform("WhiteoutData", uboBuffer.currentBuffer());
        pass.draw(0, 3);
      } catch (Throwable var23) {
        if (pass != null) {
          try {
            pass.close();
          } catch (Throwable var21) {
            var23.addSuppressed(var21);
          }
        }

        throw var23;
      }

      if (pass != null) {
        pass.close();
      }

      uboBuffer.rotate();
      RenderSystem.restoreProjectionMatrix();
    }
  }
}
