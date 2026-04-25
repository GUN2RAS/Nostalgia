package net.nostalgia.mixin.rd132211;

import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.environment.AtmosphericFogEnvironment;
import net.nostalgia.world.dimension.ModDimensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AtmosphericFogEnvironment.class)
public class FogRendererMixin {

    @Inject(method = "getBaseColor", at = @At("RETURN"), cancellable = true)
    private void onGetBaseColor(ClientLevel level, Camera camera, int i, float f, CallbackInfoReturnable<Integer> cir) {
        if (level.dimension().equals(ModDimensions.RD_132211_LEVEL_KEY)) {
            
            int red = (int) (0.5f * 255.0f);
            int green = (int) (0.8f * 255.0f);
            int blue = (int) (1.0f * 255.0f);
            cir.setReturnValue(0xFF000000 | (red << 16) | (green << 8) | blue);
        }
    }
}
