package net.nostalgia.client.render.cache;

import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class RDHologramProvider implements DimensionHologramProvider {
    @Override
    public void generateChunkData(int chunkX, int chunkZ, byte[] target, long seed) {
        for (int x = 0; x < 16; x++) {
            int wx = chunkX * 16 + x;
            for (int z = 0; z < 16; z++) {
                int wz = chunkZ * 16 + z;
                if (wx < 0 || wx >= 256 || wz < 0 || wz >= 256) {
                    continue;
                }
                for (int y = 0; y <= 42; y++) {
                    int index = (x * 16 + z) * 128 + y;
                    if (y == 0) target[index] = 7; // Bedrock
                    else if (y < 42) target[index] = 1; // Stone
                    else target[index] = 2; // Grass
                }
            }
        }
    }

    @Override
    public void decorateChunk(ChunkPos cp, long seed) {
        // RD has no decorations
    }

    @Override
    public BlockState getBlockState(byte id, boolean isSkyInverted) {
        if (id == 0) return null;
        if (id == 7) return Blocks.BEDROCK.defaultBlockState();
        if (id == 1) return net.nostalgia.block.ModBlocks.RD_STONE.defaultBlockState();
        if (id == 2) return net.nostalgia.block.ModBlocks.RD_GRASS.defaultBlockState();
        return Blocks.DIRT.defaultBlockState();
    }
}
