package net.nostalgia.mixin.client.ritual;

import net.minecraft.client.Camera;
import net.minecraft.client.renderer.LevelRenderer;
import net.nostalgia.client.events.core.ClientFreezeRegions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererZoneTickMixin {

    @Inject(method = "tick(Lnet/minecraft/client/Camera;)V", at = @At("HEAD"), cancellable = true)
    private void nostalgia$zoneCancelLevelRendererTick(Camera camera, CallbackInfo ci) {
        if (net.nostalgia.client.events.core.ClientZoneTime.isTransitioning()) return;
        if (!ClientFreezeRegions.isLocalPlayerInZone()) return;
        ci.cancel();
    }
}
