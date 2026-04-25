package net.nostalgia.mixin.alpha;

import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.attribute.EnvironmentAttributeProbe;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.level.Level;
import net.nostalgia.world.dimension.ModDimensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.jspecify.annotations.Nullable;

@Mixin(EnvironmentAttributeProbe.class)
public abstract class AlphaEnvironmentProbeMixin {

    @Shadow @Nullable Level level;

    @Inject(method = "getValue", at = @At("RETURN"), cancellable = true)
    private <Value> void alphaStepLightingVars(EnvironmentAttribute<Value> attribute, float partialTick, CallbackInfoReturnable<Value> cir) {
        if (this.level != null && this.level.dimension() == ModDimensions.ALPHA_112_01_LEVEL_KEY) {
            if (attribute == EnvironmentAttributes.SKY_LIGHT_FACTOR) {
                float val = (Float) cir.getReturnValue();

                float stepped = Math.round(val * 11.0f) / 11.0f;
                cir.setReturnValue((Value) (Float) stepped);
            }
        }
    }
}
