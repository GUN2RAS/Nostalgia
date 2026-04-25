package net.nostalgia.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.world.entity.Mob.class)
public abstract class AlphaBabyMobMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    private void nostalgia$cureBaby(CallbackInfo ci) {
        Object self = this;
        if (self instanceof net.minecraft.world.entity.AgeableMob ageable) {
            if (ageable.isBaby() && ageable.level().dimension() == net.nostalgia.world.dimension.ModDimensions.ALPHA_112_01_LEVEL_KEY) {
                ageable.setBaby(false);
            }
        } else if (self instanceof net.minecraft.world.entity.monster.zombie.Zombie zombie) {
            if (zombie.isBaby() && zombie.level().dimension() == net.nostalgia.world.dimension.ModDimensions.ALPHA_112_01_LEVEL_KEY) {
                zombie.setBaby(false);
            }
        } else if (self instanceof net.minecraft.world.entity.monster.Slime slime) {
        }
    }
}
