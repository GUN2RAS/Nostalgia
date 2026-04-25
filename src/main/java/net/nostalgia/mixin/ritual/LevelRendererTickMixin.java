package net.nostalgia.mixin.ritual;

import net.minecraft.client.renderer.LevelRenderer;
import net.nostalgia.alphalogic.ritual.RitualManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class LevelRendererTickMixin {
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void freezeVisualTicks(CallbackInfo ci) {
        if (RitualManager.getClientState() == RitualManager.State.FROZEN) {

            ci.cancel();
        }
    }
}
