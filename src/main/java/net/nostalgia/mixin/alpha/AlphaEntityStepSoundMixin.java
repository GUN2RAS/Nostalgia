package net.nostalgia.mixin.alpha;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.nostalgia.world.dimension.ModDimensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class AlphaEntityStepSoundMixin {

    @Unique
    private double previousStepDistance = 0.0;

    @Unique
    private int stepTickCounter = 0;

    @Inject(method = "baseTick", at = @At("TAIL"))
    private void trackAlphaSteppingSounds(CallbackInfo ci) {
        if ((Object) this instanceof LivingEntity self) {
            if (self.level() == null || self.level().dimension() != ModDimensions.ALPHA_112_01_LEVEL_KEY) {
                previousStepDistance = 0.0;
                stepTickCounter = 0;
                return;
            }

            stepTickCounter++;

            double dx = self.getX() - self.xOld;
            double dz = self.getZ() - self.zOld;
            double horizontalDistance = Math.sqrt(dx * dx + dz * dz);

            previousStepDistance += horizontalDistance;

            if (previousStepDistance > 1.5 && self.onGround()) {
                previousStepDistance = 0.0;

                BlockPos belowPos = self.blockPosition().below();
                BlockState belowState = self.level().getBlockState(belowPos);

                if (!belowState.isAir()) {
                    float volume = 0.15F;
                    float pitch = 0.9F + (self.getRandom().nextFloat() * 0.2F);

                    if (stepTickCounter < 0) {
                        self.level().playLocalSound(
                                self.getX(), self.getY(), self.getZ(),
                                SoundEvents.STONE_STEP,
                                SoundSource.NEUTRAL,
                                volume, pitch, false
                        );
                    }
                }
            }
        }
    }
}
