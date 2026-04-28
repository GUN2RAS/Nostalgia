package net.nostalgia.mixin.alpha;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.damagesource.DamageSource;
import net.nostalgia.alphalogic.ritual.RitualActiveState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class GhostModeEntityMixin {

    @Inject(method = "hurtServer", at = @At("HEAD"), cancellable = true)
    private void preventOverworldDamage(net.minecraft.server.level.ServerLevel level, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity self = (LivingEntity) (Object) this;

        if (net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.isParticipant(self)) {

            if (source.is(net.minecraft.tags.DamageTypeTags.BYPASSES_INVULNERABILITY)) return;
            
            Entity attacker = source.getEntity();
            if (attacker != null && !net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.isParticipant(attacker)) {
                cir.setReturnValue(false); 
            } else if (attacker == null) {

                cir.setReturnValue(false); 
            }
        }
    }

    @Inject(method = "canBeSeenAsEnemy", at = @At("HEAD"), cancellable = true)
    private void dropAggroOnParticipants(CallbackInfoReturnable<Boolean> cir) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.isParticipant(self)) {
            cir.setReturnValue(false); 
        }
    }
}
