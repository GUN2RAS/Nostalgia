package net.nostalgia.mixin.client.ritual;

import net.minecraft.world.attribute.EnvironmentAttributeProbe;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.nostalgia.client.events.echo.RitualVisualManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnvironmentAttributeProbe.class)
public class EnvironmentAttributeProbeMixin {

    @Inject(method = "getValue", at = @At("RETURN"), cancellable = true)
    private <Value> void nostalgia$overrideSkyColor(EnvironmentAttribute<Value> attribute, float partialTicks, CallbackInfoReturnable<Value> cir) {
        if (RitualVisualManager.isTransitioning) {
            if (attribute == EnvironmentAttributes.SKY_COLOR || attribute == EnvironmentAttributes.FOG_COLOR) {
                Object ret = cir.getReturnValue();
                if (ret instanceof Integer origColor) {
                    float t = (RitualVisualManager.getVisualTime() - RitualVisualManager.transitionStartTime) / 15000.0f;
                    if (t < 0) t = 0;
                    if (t > 1) t = 1;
                    
                    boolean isFog = attribute == EnvironmentAttributes.FOG_COLOR;
                    int targetColor = isFog ? RitualVisualManager.targetFogColor : RitualVisualManager.targetSkyColor;
                    
                    if (targetColor == -1) {
                        return; // Если цвет не пришел, не меняем
                    }

                    int r1 = (origColor >> 16) & 0xFF;
                    int g1 = (origColor >> 8) & 0xFF;
                    int b1 = origColor & 0xFF;
                    
                    int r2 = (targetColor >> 16) & 0xFF;
                    int g2 = (targetColor >> 8) & 0xFF;
                    int b2 = targetColor & 0xFF;
                    
                    int r = (int) (r1 + (r2 - r1) * t);
                    int g = (int) (g1 + (g2 - g1) * t);
                    int b = (int) (b1 + (b2 - b1) * t);
                    
                    int newColor = 0xFF000000 | (r << 16) | (g << 8) | b;
                    cir.setReturnValue((Value) (Integer) newColor);
                }
            }
        }
    }
}
