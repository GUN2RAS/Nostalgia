package net.nostalgia.mixin.client;

import com.mojang.blaze3d.framegraph.FrameGraphBuilder;
import com.mojang.blaze3d.framegraph.FramePass;
import com.mojang.blaze3d.pipeline.RenderTarget;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LevelTargetBundle;
import net.nostalgia.client.render.PortalDepthMaskRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererDepthMaskMixin {

    @Shadow @Final private LevelTargetBundle targets;

    @Inject(method = "addMainPass", at = @At("HEAD"))
    private void nost$addDepthMaskPass(
            FrameGraphBuilder frame,
            net.minecraft.client.renderer.culling.Frustum frustum,
            org.joml.Matrix4fc modelViewMatrix,
            com.mojang.blaze3d.buffers.GpuBufferSlice terrainFog,
            boolean renderOutline,
            net.minecraft.client.renderer.state.level.LevelRenderState levelRenderState,
            DeltaTracker deltaTracker,
            net.minecraft.util.profiling.ProfilerFiller profiler,
            net.minecraft.client.renderer.chunk.ChunkSectionsToRender chunkSectionsToRender,
            CallbackInfo ci
    ) {
        if (PortalDepthMaskRenderer.shouldRender()) {
            net.nostalgia.client.render.PortalSkyRenderer.capturedModelViewMatrix = new org.joml.Matrix4f(modelViewMatrix);
            
            FramePass pass = frame.addPass("nostalgia_depth_mask");
            com.mojang.blaze3d.resource.ResourceHandle<RenderTarget> handle = pass.readsAndWrites(this.targets.main);
            this.targets.main = handle;
            pass.executes(() -> {
                RenderTarget target = handle.get();
                PortalDepthMaskRenderer.render(target, deltaTracker);
            });
        }
    }
}
