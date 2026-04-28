package net.nostalgia.mixin.render;

import net.minecraft.world.level.chunk.LevelChunkSection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LevelChunkSection.class)
public class ForceSkyRenderMixin {
    @Inject(method = "hasOnlyAir", at = @At("HEAD"), cancellable = true)
    private void forceMeshingDuringRitual(CallbackInfoReturnable<Boolean> cir) {
        if (net.nostalgia.client.ritual.ClientRitualEventRegistry.activeSkyPortal() != null) {
            cir.setReturnValue(false);
        }
    }
}
