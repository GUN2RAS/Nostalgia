package net.nostalgia.alphalogic.gen;

import java.util.Random;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.nostalgia.alphalogic.core.AlphaMathHelper;
import net.nostalgia.block.AlphaBlocks;

public class AlphaWorldGenMinable {
  private final BlockState minableBlockState;
  private final int numberOfBlocks;

  public AlphaWorldGenMinable(BlockState state, int amount) {
    this.minableBlockState = state;
    this.numberOfBlocks = amount;
  }

  public boolean generate(WorldGenLevel level, Random random, int x, int y, int z) {
    float f = random.nextFloat() * 3.1415927F;
    double d = x + 8 + AlphaMathHelper.sin(f) * this.numberOfBlocks / 8.0F;
    double d1 = x + 8 - AlphaMathHelper.sin(f) * this.numberOfBlocks / 8.0F;
    double d2 = z + 8 + AlphaMathHelper.cos(f) * this.numberOfBlocks / 8.0F;
    double d3 = z + 8 - AlphaMathHelper.cos(f) * this.numberOfBlocks / 8.0F;
    double d4 = y + random.nextInt(3) + 2;
    double d5 = y + random.nextInt(3) + 2;
    MutableBlockPos pos = new MutableBlockPos();

    for (int l = 0; l <= this.numberOfBlocks; l++) {
      double d6 = d + (d1 - d) * l / this.numberOfBlocks;
      double d7 = d4 + (d5 - d4) * l / this.numberOfBlocks;
      double d8 = d2 + (d3 - d2) * l / this.numberOfBlocks;
      double d9 = random.nextDouble() * this.numberOfBlocks / 16.0;
      double d10 = (AlphaMathHelper.sin(l * 3.1415927F / this.numberOfBlocks) + 1.0F) * d9 + 1.0;
      double d11 = (AlphaMathHelper.sin(l * 3.1415927F / this.numberOfBlocks) + 1.0F) * d9 + 1.0;
      int i1 = AlphaMathHelper.floor(d6 - d10 / 2.0);
      int j1 = AlphaMathHelper.floor(d7 - d11 / 2.0);
      int k1 = AlphaMathHelper.floor(d8 - d10 / 2.0);
      int l1 = AlphaMathHelper.floor(d6 + d10 / 2.0);
      int i2 = AlphaMathHelper.floor(d7 + d11 / 2.0);
      int j2 = AlphaMathHelper.floor(d8 + d10 / 2.0);

      for (int k2 = i1; k2 <= l1; k2++) {
        double d12 = (k2 + 0.5 - d6) / (d10 / 2.0);
        if (d12 * d12 < 1.0) {
          for (int l2 = j1; l2 <= i2; l2++) {
            double d13 = (l2 + 0.5 - d7) / (d11 / 2.0);
            if (d12 * d12 + d13 * d13 < 1.0) {
              for (int i3 = k1; i3 <= j2; i3++) {
                double d14 = (i3 + 0.5 - d8) / (d10 / 2.0);
                if (d12 * d12 + d13 * d13 + d14 * d14 < 1.0) {
                  pos.set(k2, l2, i3);
                  if (level.getBlockState(pos).is(AlphaBlocks.ALPHA_STONE)) {
                    level.setBlock(pos, this.minableBlockState, 2);
                  }
                }
              }
            }
          }
        }
      }
    }

    return true;
  }
}
