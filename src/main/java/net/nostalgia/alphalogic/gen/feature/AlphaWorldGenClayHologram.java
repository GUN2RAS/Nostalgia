package net.nostalgia.alphalogic.gen.feature;

import java.util.Random;
import net.nostalgia.alphalogic.core.AlphaMathHelper;
import net.nostalgia.client.events.caches.impl.AlphaByteCache;

public class AlphaWorldGenClayHologram {
  private int numberOfBlocks;

  public AlphaWorldGenClayHologram(int num) {
    this.numberOfBlocks = num;
  }

  public boolean generate(Random random, int x, int y, int z) {
    byte material = AlphaByteCache.getBlockSafely(x, y, z);
    if (material != 8 && material != 9) {
      return false;
    } else {
      float f = random.nextFloat() * 3.1415927F;
      double d0 = x + AlphaMathHelper.sin(f) * this.numberOfBlocks / 8.0F;
      double d1 = x - AlphaMathHelper.sin(f) * this.numberOfBlocks / 8.0F;
      double d2 = z + AlphaMathHelper.cos(f) * this.numberOfBlocks / 8.0F;
      double d3 = z - AlphaMathHelper.cos(f) * this.numberOfBlocks / 8.0F;
      double d4 = y + random.nextInt(3) - 2;
      double d5 = y + random.nextInt(3) - 2;

      for (int i = 0; i <= this.numberOfBlocks; i++) {
        double d6 = d0 + (d1 - d0) * i / this.numberOfBlocks;
        double d7 = d4 + (d5 - d4) * i / this.numberOfBlocks;
        double d8 = d2 + (d3 - d2) * i / this.numberOfBlocks;
        double d9 = random.nextDouble() * this.numberOfBlocks / 16.0;
        double d10 = (Math.sin(i * 3.1415927F / this.numberOfBlocks) + 1.0) * d9 + 1.0;
        double d11 = (Math.sin(i * 3.1415927F / this.numberOfBlocks) + 1.0) * d9 + 1.0;
        int j = AlphaMathHelper.floor(d6 - d10 / 2.0);
        int k = AlphaMathHelper.floor(d6 + d10 / 2.0);
        int l = AlphaMathHelper.floor(d7 - d11 / 2.0);
        int i1 = AlphaMathHelper.floor(d7 + d11 / 2.0);
        int j1 = AlphaMathHelper.floor(d8 - d10 / 2.0);
        int k1 = AlphaMathHelper.floor(d8 + d10 / 2.0);

        for (int l1 = j; l1 <= k; l1++) {
          double d12 = (l1 + 0.5 - d6) / (d10 / 2.0);
          if (d12 * d12 < 1.0) {
            for (int i2 = l; i2 <= i1; i2++) {
              double d13 = (i2 + 0.5 - d7) / (d11 / 2.0);
              if (d12 * d12 + d13 * d13 < 1.0) {
                for (int j2 = j1; j2 <= k1; j2++) {
                  double d14 = (j2 + 0.5 - d8) / (d10 / 2.0);
                  if (d12 * d12 + d13 * d13 + d14 * d14 < 1.0) {
                    byte currentBlockId = AlphaByteCache.getBlockSafely(l1, i2, j2);
                    if (currentBlockId == 12) {
                      AlphaByteCache.setBlockSafely(l1, i2, j2, (byte)82);
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
}
