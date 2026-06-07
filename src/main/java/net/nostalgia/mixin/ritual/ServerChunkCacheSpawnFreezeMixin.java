package net.nostalgia.mixin.ritual;

import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.chunk.LevelChunk;
import net.nostalgia.alphalogic.ritual.TickRateManagerAccess;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ServerChunkCache.class)
public abstract class ServerChunkCacheSpawnFreezeMixin {
    @Shadow @Final public ServerLevel level;

    @Inject(method = "tickSpawningChunk", at = @At("HEAD"), cancellable = true)
    private void nostalgia$zoneSpawnFreeze(LevelChunk chunk, long timeDiff, List<MobCategory> spawningCategories, NaturalSpawner.SpawnState spawnCookie, CallbackInfo ci) {
        if (this.level.tickRateManager() instanceof TickRateManagerAccess access && access.nostalgia$hasRegions()) {
            if (access.nostalgia$isChunkFrozen(this.level.dimension(), chunk.getPos())) {
                ci.cancel();
            }
        }
    }
}
