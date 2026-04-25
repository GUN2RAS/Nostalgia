package net.nostalgia.mixin.ritual;

import net.minecraft.client.renderer.texture.SpriteContents;
import net.nostalgia.alphalogic.ritual.RitualManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SpriteContents.AnimationState.class)
public class SpriteTickerMixin {
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void freezeAnimatedTextures(CallbackInfo ci) {
        if (RitualManager.getClientState() == RitualManager.State.FROZEN) {
            
            ci.cancel();
        }
    }
}
