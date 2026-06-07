package net.nostalgia.client.events.caches.providers;

import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class EmptyHologramProvider implements DimensionHologramProvider {
    @Override
    public void generateChunkData(int chunkX, int chunkZ, byte[] target, long seed) {
    }

    @Override
    public void decorateChunk(ChunkPos cp, long seed) {
    }

    @Override
    public BlockState getBlockState(byte id, boolean isSkyInverted) {
        return null;
    }
}
