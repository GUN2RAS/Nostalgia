package net.nostalgia.mixin.ritual;

import net.minecraft.server.level.ServerLevel;
import net.nostalgia.alphalogic.ritual.TickRateManagerAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerLevel.class)
public abstract class ServerLevelShouldTickBlocksAtMixin {

    @Inject(method = "shouldTickBlocksAt(J)Z", at = @At("HEAD"), cancellable = true)
    private void nostalgia$zoneGate(long chunkKey, CallbackInfoReturnable<Boolean> cir) {
        ServerLevel self = (ServerLevel) (Object) this;
        if (self.tickRateManager() instanceof TickRateManagerAccess access && access.nostalgia$hasRegions()) {
            if (access.nostalgia$isChunkFrozen(self.dimension(), chunkKey)) {
                cir.setReturnValue(false);
            }
        }
    }
}
