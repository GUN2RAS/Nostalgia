package net.nostalgia.mixin.client.ritual;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LevelRenderer.class)
public abstract class DeltaTrackerFreezeMixin {

    @Redirect(method = "extractVisibleEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/DeltaTracker;getGameTimeDeltaPartialTick(Z)F"))
    private float nostalgia$zoneEntityPartial(DeltaTracker tracker, boolean ignoreFrozenGame) {
        if (!ignoreFrozenGame) {
            return 1.0F;
        }
        return tracker.getGameTimeDeltaPartialTick(ignoreFrozenGame);
    }
}
