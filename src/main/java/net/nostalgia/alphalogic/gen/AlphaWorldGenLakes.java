package net.nostalgia.alphalogic.gen;

import java.util.Random;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class AlphaWorldGenLakes {
  private final BlockState liquidState;

  public AlphaWorldGenLakes(BlockState state) {
    this.liquidState = state;
  }

  public boolean generate(WorldGenLevel level, Random rand, int x, int y, int z) {
    x -= 8;
    MutableBlockPos pos = new MutableBlockPos();
    z -= 8;

    while (y > 0 && level.isEmptyBlock(pos.set(x, y, z))) {
      y--;
    }

    y -= 4;
    boolean[] flags = new boolean[2048];
    int n = rand.nextInt(4) + 4;

    for (int i = 0; i < n; i++) {
      double d0 = rand.nextDouble() * 6.0 + 3.0;
      double d1 = rand.nextDouble() * 4.0 + 2.0;
      double d2 = rand.nextDouble() * 6.0 + 3.0;
      double d3 = rand.nextDouble() * 16.0 - d0 / 2.0;
      double d4 = rand.nextDouble() * 8.0 - d1 / 2.0;
      double d5 = rand.nextDouble() * 16.0 - d2 / 2.0;

      for (int j = 1; j < 15; j++) {
        for (int k = 1; k < 15; k++) {
          for (int l = 1; l < 7; l++) {
            double d6 = (j - d3) / (d0 / 2.0);
            double d7 = (l - d4) / (d1 / 2.0);
            double d8 = (k - d5) / (d2 / 2.0);
            double d9 = d6 * d6 + d7 * d7 + d8 * d8;
            if (d9 < 1.0) {
              flags[(j * 16 + k) * 8 + l] = true;
            }
          }
        }
      }
    }

    for (int j = 0; j < 16; j++) {
      for (int k = 0; k < 16; k++) {
        for (int lx = 0; lx < 8; lx++) {
          boolean flag = !flags[(j * 16 + k) * 8 + lx]
            && (
              j < 15 && flags[((j + 1) * 16 + k) * 8 + lx]
                || j > 0 && flags[((j - 1) * 16 + k) * 8 + lx]
                || k < 15 && flags[(j * 16 + k + 1) * 8 + lx]
                || k > 0 && flags[(j * 16 + (k - 1)) * 8 + lx]
                || lx < 7 && flags[(j * 16 + k) * 8 + lx + 1]
                || lx > 0 && flags[(j * 16 + k) * 8 + (lx - 1)]
            );
          if (flag) {
            pos.set(x + j, y + lx, z + k);
            BlockState material = level.getBlockState(pos);
            if (lx >= 4 && !material.getFluidState().isEmpty()) {
              return false;
            }

            if (lx < 4 && !material.isSolid() && !level.getBlockState(pos).is(this.liquidState.getBlock())) {
              return false;
            }
          }
        }
      }
    }

    for (int j = 0; j < 16; j++) {
      for (int k = 0; k < 16; k++) {
        for (int lxx = 0; lxx < 8; lxx++) {
          if (flags[(j * 16 + k) * 8 + lxx]) {
            pos.set(x + j, y + lxx, z + k);
            level.setBlock(pos, lxx >= 4 ? Blocks.AIR.defaultBlockState() : this.liquidState, 2);
          }
        }
      }
    }

    return true;
  }
}
