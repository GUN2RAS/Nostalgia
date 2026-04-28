package net.nostalgia.mixin.ritual;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.nostalgia.alphalogic.ritual.RitualActiveState;
import net.nostalgia.alphalogic.ritual.RitualManager;
import net.nostalgia.alphalogic.ritual.event.RitualEventRegistry;
import net.nostalgia.alphalogic.ritual.event.TransitionEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class PlayerIsolationMixin {

    private boolean nostalgia$isIsolatedFrom(Entity other) {
        TransitionEvent t = RitualEventRegistry.activeTransition();
        if (t == null) return false;
        Player self = (Player)(Object)this;

        boolean isolate = false;
        if (!self.level().isClientSide()) {
            if (t.phase() >= 2 && (RitualManager.activeRitualMillis - t.phaseStartTime() >= 1000)) {
                isolate = true;
            }
        } else {
            isolate = nostalgia$isClientIsolated();
        }

        if (isolate) {
            return RitualActiveState.isParticipant(self) != RitualActiveState.isParticipant(other);
        }
        return false;
    }

    private boolean nostalgia$isClientIsolated() {
        if (net.fabricmc.loader.api.FabricLoader.getInstance().getEnvironmentType() == net.fabricmc.api.EnvType.CLIENT) {
            return net.nostalgia.client.ritual.RitualVisualManager.currentPhase >= 2 && 
                   (net.nostalgia.client.ritual.RitualVisualManager.getVisualTime() - net.nostalgia.client.ritual.RitualVisualManager.phase2StartTime >= 1000);
        }
        return false;
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
