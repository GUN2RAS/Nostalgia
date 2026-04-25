package net.nostalgia.alphalogic.ritual;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

import java.util.List;

public interface TickRateManagerAccess {
    void nostalgia$addRegion(FreezeRegion region);
    void nostalgia$removeRegionAt(ResourceKey<Level> dim, BlockPos beaconPos);
    void nostalgia$clearRegions();
    List<FreezeRegion> nostalgia$regions();
    boolean nostalgia$hasRegions();
    boolean nostalgia$isChunkFrozen(ResourceKey<Level> dim, long chunkKey);
    boolean nostalgia$isChunkFrozen(ResourceKey<Level> dim, ChunkPos chunkPos);
    boolean nostalgia$isBlockFrozen(ResourceKey<Level> dim, BlockPos pos);
}
