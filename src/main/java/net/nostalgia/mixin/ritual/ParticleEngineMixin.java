package net.nostalgia.mixin.ritual;

import net.minecraft.client.particle.ParticleEngine;
import net.nostalgia.alphalogic.ritual.RitualManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ParticleEngine.class)
public class ParticleEngineMixin {
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void freezeAllParticlesDuringRitual(CallbackInfo ci) {
        if (RitualManager.getClientState() == RitualManager.State.FROZEN) {
            ci.cancel();
        }
    }
}
