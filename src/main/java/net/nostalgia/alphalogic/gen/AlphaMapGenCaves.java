package net.nostalgia.alphalogic.gen;

import net.nostalgia.alphalogic.core.AlphaMathHelper;
import java.util.Random;

public class AlphaMapGenCaves extends AlphaMapGenBase {

    protected void generateCaveNode(int seedX, int seedZ, byte[] blocks, double x, double y, double z, float radius, float angle1, float angle2, int var12, int var13, double var14) {
        double d4 = (double)(seedX * 16 + 8);
        double d5 = (double)(seedZ * 16 + 8);
        float f2 = 0.0F;
        float f3 = 0.0F;
        Random rand = new Random(this.rand.nextLong());

        if (var13 <= 0) {
            int j1 = this.range * 16 - 16;
            var13 = j1 - rand.nextInt(j1 / 4);
        }

        boolean flag = false;
        if (var12 == -1) {
            var12 = var13 / 2;
            flag = true;
        }

        int k1 = rand.nextInt(var13 / 2) + var13 / 4;
        for (boolean flag1 = rand.nextInt(6) == 0; var12 < var13; ++var12) {
            double d6 = 1.5D + (double)(AlphaMathHelper.sin((float)var12 * 3.1415927F / (float)var13) * radius * 1.0F);
            double d7 = d6 * var14;
            float f4 = AlphaMathHelper.cos(angle2);
            float f5 = AlphaMathHelper.sin(angle2);
            x += (double)(AlphaMathHelper.cos(angle1) * f4);
            y += (double)f5;
            z += (double)(AlphaMathHelper.sin(angle1) * f4);
            if (flag1) {
                angle2 *= 0.92F;
            } else {
                angle2 *= 0.7F;
            }
            angle2 += f3 * 0.1F;
            angle1 += f2 * 0.1F;
            f3 *= 0.9F;
            f2 *= 0.75F;
            f3 += (rand.nextFloat() - rand.nextFloat()) * rand.nextFloat() * 2.0F;
            f2 += (rand.nextFloat() - rand.nextFloat()) * rand.nextFloat() * 4.0F;

            if (!flag && var12 == k1 && radius > 1.0F) {
                this.generateCaveNode(seedX, seedZ, blocks, x, y, z, rand.nextFloat() * 0.5F + 0.5F, angle1 - 1.5707964F, angle2 / 3.0F, var12, var13, 1.0D);
                this.generateCaveNode(seedX, seedZ, blocks, x, y, z, rand.nextFloat() * 0.5F + 0.5F, angle1 + 1.5707964F, angle2 / 3.0F, var12, var13, 1.0D);
                return;
            }

            if (flag || rand.nextInt(4) != 0) {
                double d8 = x - d4;
                double d9 = z - d5;
                double d10 = (double)(var13 - var12);
                double d11 = (double)(radius + 2.0F + 16.0F);

                if (d8 * d8 + d9 * d9 - d10 * d10 > d11 * d11) {
                    return;
                }
                
                if (x >= d4 - 16.0D - d6 * 2.0D && z >= d5 - 16.0D - d6 * 2.0D && x <= d4 + 16.0D + d6 * 2.0D && z <= d5 + 16.0D + d6 * 2.0D) {
                    int l1 = AlphaMathHelper.floor(x - d6) - seedX * 16 - 1;
                    int i2 = AlphaMathHelper.floor(x + d6) - seedX * 16 + 1;
                    int j2 = AlphaMathHelper.floor(y - d7) - 1;
                    int k2 = AlphaMathHelper.floor(y + d7) + 1;
                    int l2 = AlphaMathHelper.floor(z - d6) - seedZ * 16 - 1;
                    int i3 = AlphaMathHelper.floor(z + d6) - seedZ * 16 + 1;

                    if (l1 < 0) l1 = 0;
                    if (i2 > 16) i2 = 16;
                    if (j2 < 1) j2 = 1;
                    if (k2 > 120) k2 = 120;
                    if (l2 < 0) l2 = 0;
                    if (i3 > 16) i3 = 16;
                    
                    boolean hitWater = false;
                    for (int j3 = l1; !hitWater && j3 < i2; ++j3) {
                        for (int l3 = l2; !hitWater && l3 < i3; ++l3) {
                            for (int i4 = k2 + 1; !hitWater && i4 >= j2 - 1; --i4) {
                                int j4 = (j3 * 16 + l3) * 128 + i4;
                                if (i4 >= 0 && i4 < 128) {
                                    if (blocks[j4] == 8 || blocks[j4] == 9) {
                                        hitWater = true;
                                    }
                                    if (i4 != j2 - 1 && j3 != l1 && j3 != i2 - 1 && l3 != l2 && l3 != i3 - 1) {
                                        i4 = j2;
                                    }
                                }
                            }
                        }
                    }

                    if (!hitWater) {
                        for (int k3 = l1; k3 < i2; ++k3) {
                            double d12 = ((double)(k3 + seedX * 16) + 0.5D - x) / d6;
                            for (int i4 = l2; i4 < i3; ++i4) {
                                double d13 = ((double)(i4 + seedZ * 16) + 0.5D - z) / d6;
                                int j4 = (k3 * 16 + i4) * 128 + k2;
                                if (d12 * d12 + d13 * d13 < 1.0D) {
                                    for (int k4 = k2 - 1; k4 >= j2; --k4) {
                                        double d14 = ((double)k4 + 0.5D - y) / d7;
                                        if (d14 > -0.7D && d12 * d12 + d14 * d14 + d13 * d13 < 1.0D) {
                                            byte b0 = blocks[j4];
                                            if (b0 == 2 || b0 == 3 || b0 == 1 || b0 == 4 || b0 == 13 || b0 == 12) {
                                                if (k4 < 10) {
                                                    blocks[j4] = 10;
                                                } else {
                                                    blocks[j4] = 0;
                                                }
                                            }
                                        }
                                        --j4;
                                    }
                                }
                            }
                        }
                        if (flag) {
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void recursiveGenerate(AlphaLevelSource source, int chunkX, int chunkZ, int originX, int originZ, byte[] blocks) {
        int i = this.rand.nextInt(this.rand.nextInt(this.rand.nextInt(40) + 1) + 1);
        if (this.rand.nextInt(15) != 0) {
            i = 0;
        }

        for (int j = 0; j < i; ++j) {
            double d0 = (double)(chunkX * 16 + this.rand.nextInt(16));
            double d1 = (double)this.rand.nextInt(this.rand.nextInt(120) + 8);
            double d2 = (double)(chunkZ * 16 + this.rand.nextInt(16));
            int k = 1;

            if (this.rand.nextInt(4) == 0) {
                this.generateCaveNode(originX, originZ, blocks, d0, d1, d2, 1.0F + this.rand.nextFloat() * 6.0F, 0.0F, 0.0F, -1, -1, 0.5D);
                k += this.rand.nextInt(4);
            }

            for (int l = 0; l < k; ++l) {
                float f = this.rand.nextFloat() * 3.1415927F * 2.0F;
                float f1 = (this.rand.nextFloat() - 0.5F) * 2.0F / 8.0F;
                float f2 = this.rand.nextFloat() * 2.0F + this.rand.nextFloat();
                this.generateCaveNode(originX, originZ, blocks, d0, d1, d2, f2, f, f1, 0, 0, 1.0D);
            }
        }
    }
}
