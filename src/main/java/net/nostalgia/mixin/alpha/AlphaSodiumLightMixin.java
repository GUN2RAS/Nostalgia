package net.nostalgia.mixin.alpha;

import net.caffeinemc.mods.sodium.client.world.LevelSlice;
import net.minecraft.client.multiplayer.ClientLevel;
import net.nostalgia.world.dimension.ModDimensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LevelSlice.class)
public abstract class AlphaSodiumLightMixin {

    @Shadow(remap = false)
    private ClientLevel level;

    @Inject(method = "useAmbientOcclusion", at = @At("HEAD"), cancellable = true, remap = false)
    private void disableSmoothLightingInAlpha(CallbackInfoReturnable<Boolean> cir) {
        if (this.level != null && this.level.dimension() == ModDimensions.ALPHA_112_01_LEVEL_KEY) {
            cir.setReturnValue(false);
        }
    }
}
