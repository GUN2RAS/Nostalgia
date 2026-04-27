package net.nostalgia.client.render.cache;

import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;

public interface DimensionHologramProvider {
    void generateChunkData(int chunkX, int chunkZ, byte[] target, long seed);
    void decorateChunk(ChunkPos cp, long seed);
    BlockState getBlockState(byte id, boolean inverted);
}
