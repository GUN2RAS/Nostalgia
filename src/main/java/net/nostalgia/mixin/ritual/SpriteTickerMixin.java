package net.nostalgia.mixin.ritual;

import net.minecraft.client.renderer.texture.SpriteContents;
import net.nostalgia.alphalogic.ritual.EchoRitualManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SpriteContents.AnimationState.class)
public class SpriteTickerMixin {
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void freezeAnimatedTextures(CallbackInfo ci) {
        if (EchoRitualManager.getClientState() == EchoRitualManager.State.FROZEN) {
            
            ci.cancel();
        }
    }
}
