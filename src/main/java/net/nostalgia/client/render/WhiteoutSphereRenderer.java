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
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import net.nostalgia.alphalogic.ritual.event.ClientEchoRitualView;
import net.nostalgia.client.events.core.ClientRitualEventRegistry;
import org.joml.Matrix4f;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public class WhiteoutSphereRenderer {
  private static final Logger LOGGER = LogUtils.getLogger();
  public static final RenderPipeline PIPELINE = RenderPipeline.builder(new Snippet[0])
    .withLocation(Identifier.fromNamespaceAndPath("nostalgia", "radial_whiteout"))
    .withVertexShader(Identifier.fromNamespaceAndPath("nostalgia", "core/radial_whiteout"))
    .withFragmentShader(Identifier.fromNamespaceAndPath("nostalgia", "core/radial_whiteout"))
    .withSampler("Sampler1")
    .withUniform("WhiteoutData", UniformType.UNIFORM_BUFFER)
    .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
    .withDepthStencilState(new DepthStencilState(CompareOp.ALWAYS_PASS, false))
    .withVertexFormat(DefaultVertexFormat.EMPTY, Mode.TRIANGLES)
    .build();
  private static MappableRingBuffer uboBuffer;

  public WhiteoutSphereRenderer() {
  }

  private static void initUbo() {
    uboBuffer = new MappableRingBuffer(() -> "Nostalgia Whiteout UBO", 130, 128);
  }

  public static void render(DeltaTracker tracker) {
    ClientEchoRitualView transition = ClientRitualEventRegistry.activeTransition();
    if (transition != null && !(transition.transitionTimeSeconds() <= 0.0F)) {
      Minecraft mc = Minecraft.getInstance();
      RenderTarget target = mc.getMainRenderTarget();
      if (target != null && target.getColorTextureView() != null && target.getDepthTextureView() != null) {
        if (uboBuffer == null) {
          initUbo();
        }

        float fov = ((Integer)mc.options.fov().get()).floatValue();
        Camera camera = mc.gameRenderer.getMainCamera();
        Matrix4f invViewProj = UboShaderUtil.getInverseViewProjMatrix(camera, TimestopBorderRenderer.capturedProjectionMatrix);
        CommandEncoder encoder = RenderSystem.getDevice().createCommandEncoder();

        try {
          MappedView viewMapped = encoder.mapBuffer(uboBuffer.currentBuffer(), false, true);

          try {
            Std140Builder builder = Std140Builder.intoBuffer(viewMapped.data());
            Vec3 camPos = camera.position();
            double trueCenterX = transition.ritualCenter().getX();
            double trueCenterY = transition.ritualCenter().getY();
            double trueCenterZ = transition.ritualCenter().getZ();
            if (transition.isInNewDimension()) {
              trueCenterX += transition.offsetX();
              trueCenterY -= transition.yOffset();
              trueCenterZ += transition.offsetZ();
            }

            builder.putVec4((float)(trueCenterX + 0.5), (float)(trueCenterY + 0.5), (float)(trueCenterZ + 0.5), transition.transitionTimeSeconds());
            builder.putMat4f(invViewProj);
            builder.putVec4(transition.whiteoutAlpha(), transition.isInNewDimension() ? 1.0F : 0.0F, transition.whiteRadius(), transition.alphaRadius());
            float cloudHeight = 192.0F;
            boolean isScreenAlpha = false;
            if (transition.targetDimension().equals("nostalgia:alpha")) {
              isScreenAlpha = transition.isInNewDimension();
            } else if (transition.targetDimension().equals("overworld")) {
              isScreenAlpha = !transition.isInNewDimension();
            }

            if (isScreenAlpha) {
              cloudHeight = 108.0F;
            }

            builder.putVec4((float)camPos.x, (float)camPos.y, (float)camPos.z, cloudHeight);
            long dayTime = mc.level.getDefaultClockTime();
            float timeOfDay = (float)(mc.level.getDefaultClockTime() % 24000L) / 24000.0F;
            float cosTime = (float)Math.cos((timeOfDay - 0.25F) * 3.1415927F * 2.0F) * 2.0F + 0.5F;
            cosTime = Math.max(0.0F, Math.min(cosTime, 1.0F));
            builder.putVec4(0.47F * cosTime, 0.66F * cosTime, 1.0F * cosTime, 1.0F);
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
          LOGGER.error("Failed to map Whiteout UBO", var27);
          return;
        }

        RenderSystem.backupProjectionMatrix();
        RenderPass pass = encoder.createRenderPass(
          () -> "Nostalgia Radial Whiteout", target.getColorTextureView(), OptionalInt.empty(), null, OptionalDouble.empty()
        );

        try {
          pass.setPipeline(PIPELINE);
          pass.bindTexture("Sampler1", target.getDepthTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
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
}
