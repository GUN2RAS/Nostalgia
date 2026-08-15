package net.nostalgia.client.events.caches.providers;

import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;

public interface DimensionHologramProvider {
  void generateChunkData(int var1, int var2, byte[] var3, long var4);

  void decorateChunk(ChunkPos var1, long var2);

  BlockState getBlockState(byte var1, boolean var2);

  default boolean selfGenerated() {
    return false;
  }
}
