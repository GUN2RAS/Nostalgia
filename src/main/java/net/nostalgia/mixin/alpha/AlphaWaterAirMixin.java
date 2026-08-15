package net.nostalgia.mixin.alpha;

import net.minecraft.world.entity.LivingEntity;
import net.nostalgia.world.dimension.ModDimensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class AlphaWaterAirMixin {
    @Inject(method = "increaseAirSupply", at = @At("HEAD"), cancellable = true)
    private void instantAirAlpha(int currentSupply, CallbackInfoReturnable<Integer> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity.level() != null && entity.level().dimension() == ModDimensions.ALPHA_112_01_LEVEL_KEY) {
            
            cir.setReturnValue(entity.getMaxAirSupply());
        }
    }
}
