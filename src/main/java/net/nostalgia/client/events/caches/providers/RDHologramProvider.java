package net.nostalgia.client.events.caches.providers;

import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.nostalgia.block.ModBlocks;

public class RDHologramProvider implements DimensionHologramProvider {
  public RDHologramProvider() {
  }

  @Override
  public boolean selfGenerated() {
    return true;
  }

  @Override
  public void generateChunkData(int chunkX, int chunkZ, byte[] target, long seed) {
    for (int x = 0; x < 16; x++) {
      int wx = chunkX * 16 + x;

      for (int z = 0; z < 16; z++) {
        int wz = chunkZ * 16 + z;
        if (wx >= 0 && wx < 256 && wz >= 0 && wz < 256) {
          for (int y = 0; y <= 42; y++) {
            int index = (x * 16 + z) * 128 + y;
            if (y == 0) {
              target[index] = 7;
            } else if (y < 42) {
              target[index] = 1;
            } else {
              target[index] = 2;
            }
          }
        }
      }
    }
  }

  @Override
  public void decorateChunk(ChunkPos cp, long seed) {
  }

  @Override
  public BlockState getBlockState(byte id, boolean isSkyInverted) {
    if (id == 0) {
      return null;
    } else if (id == 7) {
      return Blocks.BEDROCK.defaultBlockState();
    } else if (id == 1) {
      return ModBlocks.RD_STONE.defaultBlockState();
    } else {
      return id == 2 ? ModBlocks.RD_GRASS.defaultBlockState() : Blocks.DIRT.defaultBlockState();
    }
  }
}
