package net.nostalgia.mixin.ritual;

import net.minecraft.world.level.Level;
import net.nostalgia.alphalogic.ritual.TickRateManagerAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Level.class)
public abstract class LevelShouldTickBlocksAtMixin {

    @Inject(method = "shouldTickBlocksAt(J)Z", at = @At("HEAD"), cancellable = true)
    private void nostalgia$zoneGate(long chunkKey, CallbackInfoReturnable<Boolean> cir) {
        Level self = (Level) (Object) this;
        if (self.tickRateManager() instanceof TickRateManagerAccess access && access.nostalgia$hasRegions()) {
            if (access.nostalgia$isChunkFrozen(self.dimension(), chunkKey)) {
                cir.setReturnValue(false);
            }
        }
    }
}
