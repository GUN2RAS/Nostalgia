package net.nostalgia.mixin.client;

import net.minecraft.client.renderer.CloudRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CloudRenderer.class)
public class CloudRendererCancelMixin {
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void nost$hideCloudsDuringWhiteout(int color, net.minecraft.client.CloudStatus cloudStatus, float bottomY, int range, net.minecraft.world.phys.Vec3 cameraPosition, long gameTime, float partialTicks, CallbackInfo ci) {
        if (net.nostalgia.client.ritual.RitualVisualManager.isTransitioning) {
            ci.cancel();
        }
    }
}
