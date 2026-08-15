package net.nostalgia.alphalogic.ritual;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

public interface TickRateManagerAccess {
  void nostalgia$addRegion(FreezeRegion var1);

  void nostalgia$removeRegionAt(ResourceKey<Level> var1, BlockPos var2);

  void nostalgia$clearRegions();

  List<FreezeRegion> nostalgia$regions();

  boolean nostalgia$hasRegions();

  boolean nostalgia$isChunkFrozen(ResourceKey<Level> var1, long var2);

  boolean nostalgia$isChunkFrozen(ResourceKey<Level> var1, ChunkPos var2);

  boolean nostalgia$isBlockFrozen(ResourceKey<Level> var1, BlockPos var2);
}
