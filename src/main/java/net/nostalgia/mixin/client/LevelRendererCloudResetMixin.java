package net.nostalgia.mixin.client;

import com.mojang.blaze3d.framegraph.FrameGraphBuilder;
import com.mojang.blaze3d.framegraph.FramePass;
import com.mojang.blaze3d.pipeline.RenderTarget;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LevelTargetBundle;
import net.nostalgia.client.render.CloudDepthResetRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererCloudResetMixin {

    @Shadow @Final private LevelTargetBundle targets;

    @Inject(
        method = "renderLevel",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/LevelRenderer;addMainPass(Lcom/mojang/blaze3d/framegraph/FrameGraphBuilder;Lnet/minecraft/client/renderer/culling/Frustum;Lorg/joml/Matrix4fc;Lcom/mojang/blaze3d/buffers/GpuBufferSlice;ZLnet/minecraft/client/renderer/state/level/LevelRenderState;Lnet/minecraft/client/DeltaTracker;Lnet/minecraft/util/profiling/ProfilerFiller;Lnet/minecraft/client/renderer/chunk/ChunkSectionsToRender;)V",
            shift = At.Shift.AFTER
        )
    )
    private void nost$addCloudDepthResetPass(
            com.mojang.blaze3d.resource.GraphicsResourceAllocator resourceAllocator,
            net.minecraft.client.DeltaTracker deltaTracker,
            boolean renderOutline,
            net.minecraft.client.renderer.state.level.CameraRenderState cameraState,
            org.joml.Matrix4fc modelViewMatrix,
            com.mojang.blaze3d.buffers.GpuBufferSlice terrainFog,
            org.joml.Vector4f fogColor,
            boolean shouldRenderSky,
            net.minecraft.client.renderer.chunk.ChunkSectionsToRender chunkSectionsToRender,
            CallbackInfo ci,
            @com.llamalad7.mixinextras.sugar.Local FrameGraphBuilder frame
    ) {
        if (!CloudDepthResetRenderer.shouldRender()) return;

        FramePass pass = frame.addPass("nostalgia_cloud_depth_reset");
        com.mojang.blaze3d.resource.ResourceHandle<RenderTarget> handle = pass.readsAndWrites(this.targets.main);
        this.targets.main = handle;
        pass.executes(() -> {
            RenderTarget target = handle.get();
            CloudDepthResetRenderer.render(target, deltaTracker);
        });
    }
}
