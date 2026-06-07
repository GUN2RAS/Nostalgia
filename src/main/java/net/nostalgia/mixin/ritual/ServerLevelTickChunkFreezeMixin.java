package net.nostalgia.mixin.ritual;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.LevelChunk;
import net.nostalgia.alphalogic.ritual.TickRateManagerAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public abstract class ServerLevelTickChunkFreezeMixin {

    @Inject(method = "tickChunk", at = @At("HEAD"), cancellable = true)
    private void nostalgia$zoneRandomFreeze(LevelChunk chunk, int tickSpeed, CallbackInfo ci) {
        ServerLevel self = (ServerLevel) (Object) this;
        if (self.tickRateManager() instanceof TickRateManagerAccess access && access.nostalgia$hasRegions()) {
            if (access.nostalgia$isChunkFrozen(self.dimension(), chunk.getPos())) {
                ci.cancel();
            }
        }
    }
}
