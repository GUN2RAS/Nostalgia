package net.nostalgia.mixin.client;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.GameRenderer;
import net.nostalgia.client.render.WhiteoutRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class WhiteoutInjectMixin {

    @Inject(
        method = "renderLevel", 
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/Minecraft;getMainRenderTarget()Lcom/mojang/blaze3d/pipeline/RenderTarget;"
        )
    )
    private void renderTransitionOverlay(net.minecraft.client.DeltaTracker deltaTracker, CallbackInfo ci) {
        if (net.nostalgia.client.ritual.ClientRitualEventRegistry.activeTransition() != null) {
            net.nostalgia.client.render.WhiteoutRenderer.render(deltaTracker);
        }
        if (net.nostalgia.client.ritual.ClientRitualEventRegistry.activeSkyPortal() != null) {
            net.nostalgia.client.render.PortalSkyRenderer.render(deltaTracker);
        }
        if (net.nostalgia.client.render.GlassBreakRenderer.active) {
            net.nostalgia.client.render.GlassBreakRenderer.render(deltaTracker);
        }
        
        
        net.nostalgia.client.render.TimestopBorderRenderer.render(deltaTracker);
    }

    @Inject(
        method = "renderLevel",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/blaze3d/systems/RenderSystem;setProjectionMatrix(Lcom/mojang/blaze3d/buffers/GpuBufferSlice;Lcom/mojang/blaze3d/ProjectionType;)V",
            ordinal = 0
        )
    )
    private void captureProjectionMatrix(net.minecraft.client.DeltaTracker deltaTracker, CallbackInfo ci, @com.llamalad7.mixinextras.sugar.Local(ordinal = 0) org.joml.Matrix4f projectionMatrix) {
        if (net.nostalgia.client.ritual.ClientRitualEventRegistry.activeSkyPortal() != null) {
            net.nostalgia.client.render.PortalSkyRenderer.capturedProjectionMatrix = new org.joml.Matrix4f(projectionMatrix);
        }
        net.nostalgia.client.render.TimestopBorderRenderer.capturedProjectionMatrix = new org.joml.Matrix4f(projectionMatrix);
    }

    @Inject(method = "renderLevel", at = @At("HEAD"), cancellable = true)
    private void preventCameraNPE(net.minecraft.client.DeltaTracker deltaTracker, CallbackInfo ci) {
        if (net.nostalgia.client.ritual.ClientRitualEventRegistry.activeTransition() != null) {
            if (net.minecraft.client.Minecraft.getInstance().getCameraEntity() == null) {
                net.nostalgia.client.render.WhiteoutRenderer.render(deltaTracker);
                ci.cancel();
            }
        }
    }
}
