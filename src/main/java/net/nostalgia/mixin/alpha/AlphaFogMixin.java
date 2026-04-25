package net.nostalgia.mixin.alpha;

import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogData;
import net.nostalgia.alphalogic.core.AlphaRenderState;
import net.minecraft.client.renderer.fog.environment.AtmosphericFogEnvironment;
import net.nostalgia.world.dimension.ModDimensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AtmosphericFogEnvironment.class)
public abstract class AlphaFogMixin {

    @Inject(method = "setupFog", at = @At("RETURN"))
    private void applyAlphaFogDistance(FogData fogData, Camera camera, ClientLevel level, float renderDistance, DeltaTracker deltaTracker, CallbackInfo ci) {
        if (level.dimension() == ModDimensions.ALPHA_112_01_LEVEL_KEY) {
            fogData.environmentalEnd = AlphaRenderState.getFogEnd();
            fogData.environmentalStart = AlphaRenderState.getFogStart();
            fogData.skyEnd = AlphaRenderState.getFogEnd();
        }
    }

    @Inject(method = "getBaseColor", at = @At("RETURN"), cancellable = true)
    private void applyAlphaSkyColor(ClientLevel level, Camera camera, int i, float f, CallbackInfoReturnable<Integer> cir) {
        if (level.dimension() == ModDimensions.ALPHA_112_01_LEVEL_KEY) {
            float f1 = camera.attributeProbe().getValue(net.minecraft.world.attribute.EnvironmentAttributes.SUN_ANGLE, f) / 360.0F; 
            float f2 = net.minecraft.util.Mth.cos(f1 * 3.1415927F * 2.0F) * 2.0F + 0.5F;
            if (f2 < 0.0F) {
                f2 = 0.0F;
            }
            if (f2 > 1.0F) {
                f2 = 1.0F;
            }

            float red = 0.7529412F;
            float green = 0.8470588F;
            float blue = 1.0F;

            red *= f2 * 0.94F + 0.06F;
            green *= f2 * 0.94F + 0.06F;
            blue *= f2 * 0.91F + 0.09F;

            int finalRed = (int) (red * 255.0F);
            int finalGreen = (int) (green * 255.0F);
            int finalBlue = (int) (blue * 255.0F);

            cir.setReturnValue(0xFF000000 | (finalRed << 16) | (finalGreen << 8) | finalBlue);
        }
    }
}
