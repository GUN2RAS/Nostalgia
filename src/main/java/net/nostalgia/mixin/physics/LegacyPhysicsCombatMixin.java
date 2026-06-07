package net.nostalgia.mixin.physics;

import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class LegacyPhysicsCombatMixin {

    @Inject(method = "getAttackStrengthScale", at = @At("HEAD"), cancellable = true)
    private void nostalgia$disableWeaponCooldown(float f, CallbackInfoReturnable<Float> cir) {
        if (net.nostalgia.world.rules.NostalgiaRules.getForLevel(((Player) (Object) this).level()).disableWeaponCooldown) {
            cir.setReturnValue(1.0F);
        }
    }
}
