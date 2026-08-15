package net.nostalgia.client.events.caches.providers;

import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.nostalgia.alphalogic.gen.AlphaChunkDecorator;
import net.nostalgia.alphalogic.gen.AlphaLevelSource;
import net.nostalgia.block.AlphaBlocks;

public class AlphaHologramProvider implements DimensionHologramProvider {
  private static final ThreadLocal<AlphaHologramProvider.ThreadState> THREAD_LOCAL_SOURCE = new ThreadLocal<>();

  public AlphaHologramProvider() {
  }

  @Override
  public boolean selfGenerated() {
    return true;
  }

  @Override
  public void generateChunkData(int chunkX, int chunkZ, byte[] target, long seed) {
    AlphaHologramProvider.ThreadState state = THREAD_LOCAL_SOURCE.get();
    if (state == null || state.seed != seed) {
      state = new AlphaHologramProvider.ThreadState(seed, new AlphaLevelSource(seed));
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
    if (id == 0) {
      return null;
    } else if (id == 1) {
      return AlphaBlocks.ALPHA_STONE.defaultBlockState();
    } else if (id == 2) {
      return AlphaBlocks.ALPHA_GRASS_BLOCK.defaultBlockState();
    } else if (id == 3) {
      return AlphaBlocks.ALPHA_DIRT.defaultBlockState();
    } else if (id == 4) {
      return AlphaBlocks.ALPHA_COBBLESTONE.defaultBlockState();
    } else if (id == 7) {
      return AlphaBlocks.ALPHA_BEDROCK.defaultBlockState();
    } else if (id == 8 || id == 9) {
      return Blocks.WATER.defaultBlockState();
    } else if (id == 48) {
      return Blocks.MOSSY_COBBLESTONE.defaultBlockState();
    } else if (id == 10 || id == 11) {
      return Blocks.LAVA.defaultBlockState();
    } else if (id == 12) {
      return AlphaBlocks.ALPHA_SAND.defaultBlockState();
    } else if (id == 13) {
      return AlphaBlocks.ALPHA_GRAVEL.defaultBlockState();
    } else if (id == 14) {
      return AlphaBlocks.ALPHA_GOLD_ORE.defaultBlockState();
    } else if (id == 15) {
      return AlphaBlocks.ALPHA_IRON_ORE.defaultBlockState();
    } else if (id == 16) {
      return AlphaBlocks.ALPHA_COAL_ORE.defaultBlockState();
    } else if (id == 17) {
      return AlphaBlocks.ALPHA_OAK_LOG.defaultBlockState();
    } else if (id == 18) {
      return AlphaBlocks.ALPHA_LEAVES.defaultBlockState();
    } else if (id == 20) {
      return AlphaBlocks.ALPHA_GLASS.defaultBlockState();
    } else if (id == 37) {
      return AlphaBlocks.ALPHA_YELLOW_FLOWER.defaultBlockState();
    } else if (id == 38) {
      return AlphaBlocks.ALPHA_RED_FLOWER.defaultBlockState();
    } else if (id == 39) {
      return AlphaBlocks.ALPHA_BROWN_MUSHROOM.defaultBlockState();
    } else if (id == 40) {
      return AlphaBlocks.ALPHA_RED_MUSHROOM.defaultBlockState();
    } else if (id == 56) {
      return AlphaBlocks.ALPHA_DIAMOND_ORE.defaultBlockState();
    } else if (id == 73) {
      return Blocks.REDSTONE_ORE.defaultBlockState();
    } else if (id == 78) {
      return net.nostalgia.block.AlphaBlocks.ALPHA_SNOW.defaultBlockState();
    } else if (id == 79) {
      return AlphaBlocks.ALPHA_ICE.defaultBlockState();
    } else if (id == 81) {
      return AlphaBlocks.ALPHA_CACTUS.defaultBlockState();
    } else if (id == 82) {
      return AlphaBlocks.ALPHA_CLAY.defaultBlockState();
    } else {
      return id == 83 ? AlphaBlocks.ALPHA_SUGAR_CANE.defaultBlockState() : Blocks.DIRT.defaultBlockState();
    }
  }

  private static class ThreadState {
    long seed;
    AlphaLevelSource source;

    ThreadState(long s, AlphaLevelSource src) {
      this.seed = s;
      this.source = src;
    }
  }
}
