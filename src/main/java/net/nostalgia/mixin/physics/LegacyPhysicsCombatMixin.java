package net.nostalgia.mixin.physics;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class LegacyPhysicsCombatMixin {

    @Inject(method = "getAttackStrengthScale", at = @At("HEAD"), cancellable = true)
    private void nostalgia$disableWeaponCooldown(float f, CallbackInfoReturnable<Float> cir) {
        if (net.nostalgia.world.rules.NostalgiaRules.getForLevel(((Player) (Object) this).level()).disableWeaponCooldown) {
            cir.setReturnValue(1.0F);
        }
    }

    @Inject(method = "isSweepAttack", at = @At("HEAD"), cancellable = true)
    private void nostalgia$disableSweepAttack(boolean fullStrengthAttack, boolean criticalAttack, boolean knockbackAttack, CallbackInfoReturnable<Boolean> cir) {
        if (net.nostalgia.world.rules.NostalgiaRules.getForLevel(((Player) (Object) this).level()).disableSweepAttack) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "canCriticalAttack", at = @At("HEAD"), cancellable = true)
    private void nostalgia$disableCriticalHits(Entity target, CallbackInfoReturnable<Boolean> cir) {
        if (net.nostalgia.world.rules.NostalgiaRules.getForLevel(((Player) (Object) this).level()).disableCriticalHits) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "causeExtraKnockback", at = @At("HEAD"), cancellable = true)
    private void nostalgia$disableExtraSprintKnockback(Entity entity, float knockbackAmount, Vec3 oldMovement, CallbackInfo ci) {
        if (net.nostalgia.world.rules.NostalgiaRules.getForLevel(((Player) (Object) this).level()).legacyKnockback) {
            ci.cancel();
        }
    }

    @Inject(method = "playServerSideSound", at = @At("HEAD"), cancellable = true)
    private void nostalgia$disablePlayerAttackSounds(net.minecraft.sounds.SoundEvent sound, CallbackInfo ci) {
        if (net.nostalgia.world.rules.NostalgiaRules.getForLevel(((Player) (Object) this).level()).disableAttackSounds) {
            ci.cancel();
        }
    }
}
