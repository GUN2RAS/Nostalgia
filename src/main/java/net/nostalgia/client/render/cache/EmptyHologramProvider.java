package net.nostalgia.client.render.cache;

import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class EmptyHologramProvider implements DimensionHologramProvider {
    @Override
    public void generateChunkData(int chunkX, int chunkZ, byte[] target, long seed) {
        // Empty hologram generates nothing
    }

    @Override
    public void decorateChunk(ChunkPos cp, long seed) {
        // No decorations
    }

    @Override
    public BlockState getBlockState(byte id, boolean isSkyInverted) {
        return null;
    }
}
