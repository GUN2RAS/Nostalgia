package net.nostalgia.mixin.alpha;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.nostalgia.world.dimension.ModDimensions;
import net.nostalgia.world.gen.AlphaSounds;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class AlphaWaterSoundsMixin {

    @Inject(method = "getSwimSplashSound", at = @At("HEAD"), cancellable = true)
    private void nostalgia$overrideSplashSound(CallbackInfoReturnable<SoundEvent> cir) {
        Entity entity = (Entity) (Object) this;
        if (entity.level().dimension() == ModDimensions.ALPHA_112_01_LEVEL_KEY) {
            cir.setReturnValue(AlphaSounds.RANDOM_SPLASH.value());
        }
    }

    @Inject(method = "getSwimSound", at = @At("HEAD"), cancellable = true)
    private void nostalgia$overrideSwimSound(CallbackInfoReturnable<SoundEvent> cir) {
        Entity entity = (Entity) (Object) this;
        if (entity.level().dimension() == ModDimensions.ALPHA_112_01_LEVEL_KEY) {

            cir.setReturnValue(AlphaSounds.RANDOM_SPLASH.value());
        }
    }

    @Inject(method = "isSwimming", at = @At("HEAD"), cancellable = true)
    private void nostalgia$disableModernSwimmingPosture(CallbackInfoReturnable<Boolean> cir) {
        Entity entity = (Entity) (Object) this;
        if (entity.level().dimension() == ModDimensions.ALPHA_112_01_LEVEL_KEY) {
            
            cir.setReturnValue(false);
        }
    }
}
