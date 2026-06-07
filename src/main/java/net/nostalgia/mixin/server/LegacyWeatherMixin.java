package net.nostalgia.mixin.server;

import net.minecraft.world.level.Level;
import net.nostalgia.world.dimension.LegacyDimensionRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Level.class)
public class LegacyWeatherMixin {

    @Inject(method = "isRaining", at = @At("HEAD"), cancellable = true)
    private void enforceNoRainInLegacy(CallbackInfoReturnable<Boolean> cir) {
        if (!LegacyDimensionRules.hasWeather((Level) (Object) this)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "isThundering", at = @At("HEAD"), cancellable = true)
    private void enforceNoThunderInLegacy(CallbackInfoReturnable<Boolean> cir) {
        if (!LegacyDimensionRules.hasWeather((Level) (Object) this)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "getRainLevel", at = @At("HEAD"), cancellable = true)
    private void enforceNoVisualRainInLegacy(float delta, CallbackInfoReturnable<Float> cir) {
        if (!LegacyDimensionRules.hasWeather((Level) (Object) this)) {
            cir.setReturnValue(0.0f);
        }
    }

    @Inject(method = "getThunderLevel", at = @At("HEAD"), cancellable = true)
    private void enforceNoVisualThunderInLegacy(float delta, CallbackInfoReturnable<Float> cir) {
        if (!LegacyDimensionRules.hasWeather((Level) (Object) this)) {
            cir.setReturnValue(0.0f);
        }
    }
}
