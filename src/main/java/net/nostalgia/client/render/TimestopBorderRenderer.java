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
import net.nostalgia.alphalogic.ritual.FreezeRegion;
import net.nostalgia.alphalogic.ritual.TickRateManagerAccess;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

import java.util.List;
import java.util.OptionalDouble;
import java.util.OptionalInt;

@Environment(EnvType.CLIENT)
public class TimestopBorderRenderer {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final RenderPipeline PIPELINE = RenderPipeline.builder()
            .withLocation(net.minecraft.resources.Identifier.fromNamespaceAndPath("nostalgia", "timestop_border"))
            .withVertexShader(net.minecraft.resources.Identifier.fromNamespaceAndPath("nostalgia", "core/portal_sky_rip"))
            .withFragmentShader(net.minecraft.resources.Identifier.fromNamespaceAndPath("nostalgia", "core/timestop_border"))
            .withSampler("Sampler1")
            .withUniform("TimestopData", UniformType.UNIFORM_BUFFER)
            .withColorTargetState(new com.mojang.blaze3d.pipeline.ColorTargetState(com.mojang.blaze3d.pipeline.BlendFunction.TRANSLUCENT))
            .withDepthStencilState(new com.mojang.blaze3d.pipeline.DepthStencilState(com.mojang.blaze3d.platform.CompareOp.ALWAYS_PASS, false))
            .withVertexFormat(DefaultVertexFormat.EMPTY, VertexFormat.Mode.TRIANGLES)
            .build();

    private static MappableRingBuffer uboBuffer;

    private static void initUbo() {
        uboBuffer = new MappableRingBuffer(() -> "Nostalgia Timestop UBO", 130, 256);
    }

    public static class VisualZone {
        public final net.minecraft.core.BlockPos beaconPos;
        public final int chunkRadius;
        public final long startTime;
        public long endTime = -1;

        public VisualZone(net.minecraft.core.BlockPos beaconPos, int chunkRadius) {
            this.beaconPos = beaconPos;
            this.chunkRadius = chunkRadius;
            this.startTime = System.currentTimeMillis();
        }
    }

    private static final List<VisualZone> activeVisualZones = new java.util.ArrayList<>();

    public static org.joml.Matrix4f capturedProjectionMatrix = null;

    public static void render(DeltaTracker tracker) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) return;
        
        TickRateManagerAccess access = (TickRateManagerAccess) mc.level.tickRateManager();
        if (access == null) return;

        List<FreezeRegion> currentRegions = access.nostalgia$regions().stream()
                .filter(r -> r.dimension().equals(mc.level.dimension()))
                .toList();

        long currentTime = System.currentTimeMillis();

        
        for (FreezeRegion r : currentRegions) {
            boolean found = false;
            for (VisualZone vz : activeVisualZones) {
                if (vz.beaconPos.equals(r.beaconPos())) {
                    found = true;
                    if (vz.endTime != -1) vz.endTime = -1; 
                    break;
                }
            }
            if (!found) {
                activeVisualZones.add(new VisualZone(r.beaconPos(), r.chunkRadius()));
            }
        }

        
        for (VisualZone vz : activeVisualZones) {
            if (vz.endTime == -1) {
                boolean found = false;
                for (FreezeRegion r : currentRegions) {
                    if (r.beaconPos().equals(vz.beaconPos)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    vz.endTime = currentTime;
                }
            }
        }

        
        activeVisualZones.removeIf(vz -> vz.endTime != -1 && currentTime - vz.endTime > 1500);

        if (activeVisualZones.isEmpty()) return;

        List<VisualZone> renderingZones = activeVisualZones.stream().limit(4).toList();

        RenderTarget target = mc.getMainRenderTarget();
        if (target == null || target.getColorTextureView() == null || target.getDepthTextureView() == null) {
            return;
        }

        if (uboBuffer == null) {
            initUbo();
        }

        net.minecraft.client.Camera camera = mc.gameRenderer.getMainCamera();
        
        org.joml.Matrix4f invViewProj;
        
        if (net.nostalgia.client.render.PortalSkyRenderer.capturedProjectionMatrix != null && net.nostalgia.client.render.PortalSkyRenderer.capturedModelViewMatrix != null) {
            invViewProj = new org.joml.Matrix4f(net.nostalgia.client.render.PortalSkyRenderer.capturedProjectionMatrix).mul(net.nostalgia.client.render.PortalSkyRenderer.capturedModelViewMatrix).invert();
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
            for (int i = 0; i < 4; i++) {
                if (i < renderingZones.size()) {
                    VisualZone vz = renderingZones.get(i);
                    int bx = vz.beaconPos.getX() >> 4;
                    int bz = vz.beaconPos.getZ() >> 4;
                    
                    float trueCenterX = (bx * 16) + 8.0f;
                    float trueCenterZ = (bz * 16) + 8.0f;
                    
                    float targetRadius = (vz.chunkRadius * 16.0f) + 8.0f;
                    float currentRadius;
                    
                    if (vz.endTime != -1) {
                        currentRadius = targetRadius;
                    } else {
                        
                        float progress = (currentTime - vz.startTime) / 1500.0f;
                        progress = Math.max(0.0f, Math.min(1.0f, progress));
                        float inv = 1.0f - progress;
                        float smooth = 1.0f - (inv * inv * inv);
                        currentRadius = targetRadius * smooth;
                    }
                    
                    float relX = (float) (trueCenterX - camPos.x);
                    float relY = (float) (0.0d - camPos.y);
                    float relZ = (float) (trueCenterZ - camPos.z);
                    builder.putVec4(relX, relY, relZ, currentRadius);
                } else {
                    builder.putVec4(0.0f, 0.0f, 0.0f, 0.0f);
                }
            }

            for (int i = 0; i < 4; i++) {
                if (i < renderingZones.size()) {
                    VisualZone vz = renderingZones.get(i);
                    float fade = 0.0f;
                    if (vz.endTime != -1) {
                        float progress = (currentTime - vz.endTime) / 1500.0f;
                        fade = Math.max(0.0f, Math.min(1.0f, progress));
                    }
                    builder.putVec4(fade, 0.0f, 0.0f, 0.0f);
                } else {
                    builder.putVec4(0.0f, 0.0f, 0.0f, 0.0f);
                }
            }

            
            builder.putMat4f(invViewProj);

            
            float time = (float) (System.currentTimeMillis() % 100000L) / 1000.0f;
            builder.putVec4((float) camPos.x, (float) camPos.y, (float) camPos.z, time);

            
            builder.putVec4((float) renderingZones.size(), 0.0f, 0.0f, 0.0f);

        } catch (Exception e) {
            LOGGER.error("Failed to map Timestop Border UBO", e);
            return;
        }

        RenderSystem.backupProjectionMatrix();

        try (RenderPass pass = encoder.createRenderPass(
                () -> "Nostalgia Timestop Border",
                target.getColorTextureView(),
                OptionalInt.empty(),
                null,
                OptionalDouble.empty()
        )) {
            pass.setPipeline(PIPELINE);
            pass.bindTexture("Sampler1", target.getDepthTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
            pass.setUniform("TimestopData", uboBuffer.currentBuffer());

            pass.draw(0, 3); 
        }

        uboBuffer.rotate();
        RenderSystem.restoreProjectionMatrix();
    }
}
