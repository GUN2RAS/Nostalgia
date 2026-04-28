package net.nostalgia.mixin.ritual;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.nostalgia.alphalogic.ritual.TransitionEventInstance;
import net.nostalgia.alphalogic.ritual.event.RitualEventRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class PlayerIsolationMixin {

    private boolean nostalgia$isIsolatedFrom(Entity other) {
        Player self = (Player)(Object)this;

        if (self.level().isClientSide()) {
            net.nostalgia.alphalogic.ritual.event.ClientTransitionView ct = net.nostalgia.client.ritual.ClientRitualEventRegistry.activeTransition();
            if (ct == null) return false;
            if (ct.currentPhase() < 2) return false;
            if (ct.visualTime() - ct.phase2StartTime() < 1000) return false;
            boolean selfP = RitualEventRegistry.isParticipantAny(self);
            boolean otherP = RitualEventRegistry.isParticipantAny(other);
            if (!selfP && !otherP) return false;
            return selfP != otherP;
        }

        TransitionEventInstance selfInst = RitualEventRegistry.findInstanceForParticipant(self.getUUID());
        TransitionEventInstance otherInst = RitualEventRegistry.findInstanceForParticipant(other.getUUID());
        if (selfInst == null && otherInst == null) return false;

        boolean selfActive = selfInst != null && selfInst.phase() >= 2 && (selfInst.activeMs() - selfInst.phaseStartTime() >= 1000);
        boolean otherActive = otherInst != null && otherInst.phase() >= 2 && (otherInst.activeMs() - otherInst.phaseStartTime() >= 1000);
        if (!selfActive && !otherActive) return false;

        if (selfInst != null && selfInst == otherInst) return false;
        return true;
    }

    @Inject(method = "attack", at = @At("HEAD"), cancellable = true)
    private void nostalgia$isolationAttack(Entity target, CallbackInfo ci) {
        if (nostalgia$isIsolatedFrom(target)) {
            ci.cancel();
        }
    }

    @Inject(method = "interactOn", at = @At("HEAD"), cancellable = true)
    private void nostalgia$isolationInteractOn(Entity entityToInteractOn, InteractionHand hand, net.minecraft.world.phys.Vec3 location, CallbackInfoReturnable<InteractionResult> cir) {
        if (nostalgia$isIsolatedFrom(entityToInteractOn)) {
            cir.setReturnValue(InteractionResult.PASS);
        }
    }
}
