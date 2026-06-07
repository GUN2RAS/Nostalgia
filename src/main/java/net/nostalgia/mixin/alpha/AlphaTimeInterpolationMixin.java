package net.nostalgia.mixin.alpha;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightmapRenderStateExtractor;
import net.minecraft.client.renderer.state.LightmapRenderState;
import net.nostalgia.world.dimension.ModDimensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LightmapRenderStateExtractor.class)
public abstract class AlphaTimeInterpolationMixin {

    @Inject(method = "extract", at = @At("RETURN"))
    private void hijackSkyDarkenSmoothness(LightmapRenderState renderState, float partialTicks, CallbackInfo ci) {
        Minecraft client = Minecraft.getInstance();
        if (client.level != null && client.level.dimension() == ModDimensions.ALPHA_112_01_LEVEL_KEY) {
            float smoothDarkness = renderState.skyFactor;
            
            float steppedDarkness = Math.round(smoothDarkness * 15.0F) / 15.0F;
            renderState.skyFactor = steppedDarkness;
        }
    }
}
