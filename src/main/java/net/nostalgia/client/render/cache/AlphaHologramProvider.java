package net.nostalgia.client.render.cache;

import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.nostalgia.alphalogic.gen.AlphaLevelSource;
import net.nostalgia.alphalogic.gen.AlphaChunkDecorator;

public class AlphaHologramProvider implements DimensionHologramProvider {
    private static class ThreadState {
        long seed;
        AlphaLevelSource source;
        ThreadState(long s, AlphaLevelSource src) { this.seed = s; this.source = src; }
    }
    private static final ThreadLocal<ThreadState> THREAD_LOCAL_SOURCE = new ThreadLocal<>();

    @Override
    public void generateChunkData(int chunkX, int chunkZ, byte[] target, long seed) {
        ThreadState state = THREAD_LOCAL_SOURCE.get();
        if (state == null || state.seed != seed) {
            state = new ThreadState(seed, new AlphaLevelSource(seed));
            THREAD_LOCAL_SOURCE.set(state);
        }
        state.source.provideChunk(chunkX, chunkZ, target);
    }

    @Override
    public void decorateChunk(ChunkPos cp, long seed) {
        AlphaChunkDecorator.decorate(cp, seed);
    }

    @Override
    public BlockState getBlockState(byte id, boolean isSkyInverted) {
        if (id == 0) return null;
        if (id == 1) return net.nostalgia.block.AlphaBlocks.ALPHA_STONE.defaultBlockState();
        if (id == 2) {
            return isSkyInverted ? net.nostalgia.block.AlphaBlocks.ALPHA_GRASS_BLOCK_FLIPPED.defaultBlockState() : net.nostalgia.block.AlphaBlocks.ALPHA_GRASS_BLOCK.defaultBlockState();
        }
        if (id == 3) return net.nostalgia.block.AlphaBlocks.ALPHA_DIRT.defaultBlockState();
        if (id == 4) return net.nostalgia.block.AlphaBlocks.ALPHA_COBBLESTONE.defaultBlockState();
        if (id == 7) return net.nostalgia.block.AlphaBlocks.ALPHA_BEDROCK.defaultBlockState();
        if (id == 8 || id == 9) return Blocks.WATER.defaultBlockState();
        if (id == 48) return Blocks.MOSSY_COBBLESTONE.defaultBlockState();
        if (id == 10 || id == 11) return Blocks.LAVA.defaultBlockState();
        if (id == 12) return net.nostalgia.block.AlphaBlocks.ALPHA_SAND.defaultBlockState();
        if (id == 13) return net.nostalgia.block.AlphaBlocks.ALPHA_GRAVEL.defaultBlockState();
        if (id == 14) return net.nostalgia.block.AlphaBlocks.ALPHA_GOLD_ORE.defaultBlockState();
        if (id == 15) return net.nostalgia.block.AlphaBlocks.ALPHA_IRON_ORE.defaultBlockState();
        if (id == 16) return net.nostalgia.block.AlphaBlocks.ALPHA_COAL_ORE.defaultBlockState();
        if (id == 17) return net.nostalgia.block.AlphaBlocks.ALPHA_OAK_LOG.defaultBlockState();
        if (id == 18) return net.nostalgia.block.AlphaBlocks.ALPHA_LEAVES.defaultBlockState();
        if (id == 20) return net.nostalgia.block.AlphaBlocks.ALPHA_GLASS.defaultBlockState();
        if (id == 37) return isSkyInverted ? net.nostalgia.block.AlphaBlocks.ALPHA_YELLOW_FLOWER_FLIPPED.defaultBlockState() : net.nostalgia.block.AlphaBlocks.ALPHA_YELLOW_FLOWER.defaultBlockState();
        if (id == 38) return isSkyInverted ? net.nostalgia.block.AlphaBlocks.ALPHA_RED_FLOWER_FLIPPED.defaultBlockState() : net.nostalgia.block.AlphaBlocks.ALPHA_RED_FLOWER.defaultBlockState();
        if (id == 39) return net.nostalgia.block.AlphaBlocks.ALPHA_BROWN_MUSHROOM.defaultBlockState();
        if (id == 40) return net.nostalgia.block.AlphaBlocks.ALPHA_RED_MUSHROOM.defaultBlockState();
        if (id == 56) return net.nostalgia.block.AlphaBlocks.ALPHA_DIAMOND_ORE.defaultBlockState();
        if (id == 73) return net.minecraft.world.level.block.Blocks.REDSTONE_ORE.defaultBlockState();
        if (id == 78) return Blocks.SNOW.defaultBlockState();
        if (id == 79) return net.nostalgia.block.AlphaBlocks.ALPHA_ICE.defaultBlockState();
        if (id == 81) return isSkyInverted ? net.nostalgia.block.AlphaBlocks.ALPHA_CACTUS_FLIPPED.defaultBlockState() : net.nostalgia.block.AlphaBlocks.ALPHA_CACTUS.defaultBlockState();
        if (id == 82) return net.nostalgia.block.AlphaBlocks.ALPHA_CLAY.defaultBlockState();
        if (id == 83) return isSkyInverted ? net.nostalgia.block.AlphaBlocks.ALPHA_SUGAR_CANE_FLIPPED.defaultBlockState() : net.nostalgia.block.AlphaBlocks.ALPHA_SUGAR_CANE.defaultBlockState();

        return Blocks.DIRT.defaultBlockState();
    }
}
