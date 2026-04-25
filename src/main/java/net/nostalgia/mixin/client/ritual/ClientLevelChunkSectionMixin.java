package net.nostalgia.mixin.client.ritual;

import net.minecraft.world.level.chunk.LevelChunkSection;
import net.nostalgia.client.ritual.RitualVisualManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LevelChunkSection.class)
public class ClientLevelChunkSectionMixin {

    @Inject(method = "hasFluid()Z", at = @At("HEAD"), cancellable = true)
    private void onHasFluid(CallbackInfoReturnable<Boolean> cir) {
        if (RitualVisualManager.isTransitioning && !RitualVisualManager.isBystander) {
            cir.setReturnValue(true);
        }
    }
}
