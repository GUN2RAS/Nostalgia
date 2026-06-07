package net.nostalgia.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.nostalgia.world.dimension.ModDimensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class AlphaEntityStateMixin {

    @Unique
    private float lastSyncedHealth = -1.0F;

    @Unique
    private int syncCooldown = 0;

    @Inject(method = "baseTick", at = @At("TAIL"))
    private void globallyInjectAlphaStateSync(CallbackInfo ci) {
        if ((Object) this instanceof LivingEntity self) {
            if (self.level() == null || self.level().dimension() != ModDimensions.ALPHA_112_01_LEVEL_KEY) {
                return;
            }

            if (syncCooldown > 0) {
                syncCooldown--;
                return;
            }

            float currentHealth = self.getHealth();
            if (Math.abs(currentHealth - lastSyncedHealth) > 0.5F) {
                lastSyncedHealth = currentHealth;
                syncCooldown = 10;

                net.minecraft.core.BlockPos pos = self.blockPosition();
                BlockState stateBelow = self.level().getBlockState(pos.below());

                if (stateBelow.isAir() && self.onGround()) {
                    syncCooldown = 5;
                }
            }
        }
    }
}
