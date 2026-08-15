package net.nostalgia.mixin.server;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public class ChunkTickDeltaFilterMixin {
    @Inject(method = "tickChunk", at = @At("HEAD"))
    private void onTickChunkStart(LevelChunk chunk, int tickSpeed, CallbackInfo ci) {
        net.nostalgia.alphalogic.ritual.DeltaRecordingContext.IS_CHUNK_TICK.set(true);
    }

    @Inject(method = "tickChunk", at = @At("RETURN"))
    private void onTickChunkEnd(LevelChunk chunk, int tickSpeed, CallbackInfo ci) {
        net.nostalgia.alphalogic.ritual.DeltaRecordingContext.IS_CHUNK_TICK.set(false);
    }
}
