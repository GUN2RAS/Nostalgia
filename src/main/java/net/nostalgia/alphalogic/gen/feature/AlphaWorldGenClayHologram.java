package net.nostalgia.alphalogic.gen.feature;

import java.util.Random;
import net.nostalgia.client.render.NostalgiaChunkCache;
import net.nostalgia.alphalogic.core.AlphaMathHelper;

public class AlphaWorldGenClayHologram {
    private int numberOfBlocks;

    public AlphaWorldGenClayHologram(int num) {
        this.numberOfBlocks = num;
    }

    public boolean generate(Random random, int x, int y, int z) {
        byte material = NostalgiaChunkCache.getBlockSafely(x, y, z);
        if (material != 8 && material != 9) {
            return false;
        } else {
            float f = random.nextFloat() * (float)Math.PI;
            double d0 = (double)((float)x + 8.0F * Math.sin((double)f));
            double d1 = (double)((float)x - 8.0F * Math.sin((double)f));
            double d2 = (double)((float)z + 8.0F * Math.cos((double)f));
            double d3 = (double)((float)z - 8.0F * Math.cos((double)f));
            double d4 = (double)(y + random.nextInt(3) - 2);
            double d5 = (double)(y + random.nextInt(3) - 2);

            for(int i = 0; i <= this.numberOfBlocks; ++i) {
                double d6 = d0 + (d1 - d0) * (double)i / (double)this.numberOfBlocks;
                double d7 = d4 + (d5 - d4) * (double)i / (double)this.numberOfBlocks;
                double d8 = d2 + (d3 - d2) * (double)i / (double)this.numberOfBlocks;
                double d9 = random.nextDouble() * (double)this.numberOfBlocks / 16.0D;
                double d10 = (double)(Math.sin((float)i * (float)Math.PI / (float)this.numberOfBlocks) + 1.0F) * d9 + 1.0D;
                double d11 = (double)(Math.sin((float)i * (float)Math.PI / (float)this.numberOfBlocks) + 1.0F) * d9 + 1.0D;
                int j = AlphaMathHelper.floor(d6 - d10 / 2.0D);
                int k = AlphaMathHelper.floor(d6 + d10 / 2.0D);
                int l = AlphaMathHelper.floor(d7 - d11 / 2.0D);
                int i1 = AlphaMathHelper.floor(d7 + d11 / 2.0D);
                int j1 = AlphaMathHelper.floor(d8 - d10 / 2.0D);
                int k1 = AlphaMathHelper.floor(d8 + d10 / 2.0D);

                for(int l1 = j; l1 <= k; ++l1) {
                    double d12 = ((double)l1 + 0.5D - d6) / (d10 / 2.0D);
                    if (d12 * d12 < 1.0D) {
                        for(int i2 = l; i2 <= i1; ++i2) {
                            double d13 = ((double)i2 + 0.5D - d7) / (d11 / 2.0D);
                            if (d12 * d12 + d13 * d13 < 1.0D) {
                                for(int j2 = j1; j2 <= k1; ++j2) {
                                    double d14 = ((double)j2 + 0.5D - d8) / (d10 / 2.0D);
                                    if (d12 * d12 + d13 * d13 + d14 * d14 < 1.0D) {
                                        byte currentBlockId = NostalgiaChunkCache.getBlockSafely(l1, i2, j2);
                                        if (currentBlockId == 12) { // SAND
                                            NostalgiaChunkCache.setBlockSafely(l1, i2, j2, (byte) 82); // CLAY
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
