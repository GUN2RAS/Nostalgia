package net.nostalgia.alphalogic.gen;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.nostalgia.alphalogic.core.AlphaMathHelper;
import net.nostalgia.block.AlphaBlocks;

import java.util.Random;

public class AlphaWorldGenBigTree {
    static final byte[] otherCoordPairs = new byte[]{2, 0, 0, 1, 2, 1};
    Random rand = new Random();
    WorldGenLevel level;
    int[] basePos = new int[]{0, 0, 0};
    int heightLimit = 0;
    int height;
    double heightAttenuation = 0.618D;
    double branchDensity = 1.0D;
    double branchSlope = 0.381D;
    double scaleWidth = 1.0D;
    double leafDensity = 1.0D;
    int trunkSize = 1;
    int heightLimitLimit = 12;
    int leafDistanceLimit = 4;
    int[][] leafNodes;

    void generateLeafNodeList() {
        this.height = (int)((double)this.heightLimit * this.heightAttenuation);
        if (this.height >= this.heightLimit) {
            this.height = this.heightLimit - 1;
        }

        int i = (int)(1.382D + Math.pow(this.leafDensity * (double)this.heightLimit / 13.0D, 2.0D));
        if (i < 1) {
            i = 1;
        }

        int[][] aint = new int[i * this.heightLimit][4];
        int j = this.basePos[1] + this.heightLimit - this.leafDistanceLimit;
        int k = 1;
        int l = this.basePos[1] + this.height;
        int i1 = j - this.basePos[1];
        aint[0][0] = this.basePos[0];
        aint[0][1] = j--;
        aint[0][2] = this.basePos[2];
        aint[0][3] = l;

        while(i1 >= 0) {
            float f = this.layerSize(i1);
            if (f < 0.0F) {
                --j;
                --i1;
            } else {
                for(int j1 = 0; j1 < i; ++j1) {
                    double d0 = this.scaleWidth * ((double)f * ((double)this.rand.nextFloat() + 0.328D));
                    double d1 = (double)this.rand.nextFloat() * 2.0D * Math.PI;
                    int k1 = AlphaMathHelper.floor(d0 * Math.sin(d1) + (double)this.basePos[0] + 0.5D);
                    int l1 = AlphaMathHelper.floor(d0 * Math.cos(d1) + (double)this.basePos[2] + 0.5D);
                    int[] aint1 = new int[]{k1, j, l1};
                    int[] aint2 = new int[]{k1, j + this.leafDistanceLimit, l1};
                    if (this.checkBlockLine(aint1, aint2) == -1) {
                        int[] aint3 = new int[]{this.basePos[0], this.basePos[1], this.basePos[2]};
                        double d2 = Math.sqrt(Math.pow((double)Math.abs(this.basePos[0] - aint1[0]), 2.0D) + Math.pow((double)Math.abs(this.basePos[2] - aint1[2]), 2.0D));
                        double d3 = d2 * this.branchSlope;
                        if ((double)aint1[1] - d3 > (double)l) {
                            aint3[1] = l;
                        } else {
                            aint3[1] = (int)((double)aint1[1] - d3);
                        }

                        if (this.checkBlockLine(aint3, aint1) == -1) {
                            aint[k][0] = k1;
                            aint[k][1] = j;
                            aint[k][2] = l1;
                            aint[k][3] = aint3[1];
                            ++k;
                        }
                    }
                }

                --j;
                --i1;
            }
        }

        this.leafNodes = new int[k][4];
        System.arraycopy(aint, 0, this.leafNodes, 0, k);
    }

    void genTreeLayer(int x, int y, int z, float size, byte axis, BlockState state) {
        int i = (int)((double)size + 0.618D);
        byte b0 = otherCoordPairs[axis];
        byte b1 = otherCoordPairs[axis + 3];
        int[] aint = new int[]{x, y, z};
        int[] aint1 = new int[]{0, 0, 0};
        int j = -i;
        aint1[axis] = aint[axis];

        for(int k = -i; k <= i; ++k) {
            aint1[b0] = aint[b0] + k;
            j = -i;

            while(j <= i) {
                double d0 = Math.sqrt(Math.pow((double)Math.abs(k) + 0.5D, 2.0D) + Math.pow((double)Math.abs(j) + 0.5D, 2.0D));
                if (d0 > (double)size) {
                    ++j;
                } else {
                    aint1[b1] = aint[b1] + j;
                    BlockPos pos = new BlockPos(aint1[0], aint1[1], aint1[2]);
                    BlockState block = this.level.getBlockState(pos);
                    if (!block.isAir() && !block.is(AlphaBlocks.ALPHA_LEAVES)) {
                        ++j;
                    } else {
                        this.level.setBlock(pos, state, 2);
                        ++j;
                    }
                }
            }
        }

    }

    float layerSize(int y) {
        if ((double)y < (double)this.heightLimit * 0.3D) {
            return -1.618F;
        } else {
            float f = (float)this.heightLimit / 2.0F;
            float f1 = (float)this.heightLimit / 2.0F - (float)y;
            float f2;
            if (f1 == 0.0F) {
                f2 = f;
            } else if (Math.abs(f1) >= f) {
                f2 = 0.0F;
            } else {
                f2 = (float)Math.sqrt(Math.pow((double)Math.abs(f), 2.0D) - Math.pow((double)Math.abs(f1), 2.0D));
            }

            f2 *= 0.5F;
            return f2;
        }
    }

    float leafSize(int y) {
        if (y >= 0 && y < this.leafDistanceLimit) {
            return y != 0 && y != this.leafDistanceLimit - 1 ? 3.0F : 2.0F;
        } else {
            return -1.0F;
        }
    }

    void generateLeafNode(int x, int y, int z) {
        int i = y + this.leafDistanceLimit;

        for(int j = y; j < i; ++j) {
            float f = this.leafSize(j - y);
            this.genTreeLayer(x, j, z, f, (byte)1, AlphaBlocks.ALPHA_LEAVES.defaultBlockState());
        }

    }

    void placeBlockLine(int[] start, int[] end, BlockState state) {
        int[] aint = new int[]{0, 0, 0};
        byte b0 = 0;

        for(byte b1 = 0; b1 < 3; ++b1) {
            aint[b1] = end[b1] - start[b1];
            if (Math.abs(aint[b1]) > Math.abs(aint[b0])) {
                b0 = b1;
            }
        }

        if (aint[b0] != 0) {
            byte b2 = otherCoordPairs[b0];
            byte b3 = otherCoordPairs[b0 + 3];
            byte b4;
            if (aint[b0] > 0) {
                b4 = 1;
            } else {
                b4 = -1;
            }

            double d0 = (double)aint[b2] / (double)aint[b0];
            double d1 = (double)aint[b3] / (double)aint[b0];
            int[] aint1 = new int[]{0, 0, 0};
            int i = 0;

            for(int j = aint[b0] + b4; i != j; i += b4) {
                aint1[b0] = AlphaMathHelper.floor((double)(start[b0] + i) + 0.5D);
                aint1[b2] = AlphaMathHelper.floor((double)start[b2] + (double)i * d0 + 0.5D);
                aint1[b3] = AlphaMathHelper.floor((double)start[b3] + (double)i * d1 + 0.5D);
                this.level.setBlock(new BlockPos(aint1[0], aint1[1], aint1[2]), state, 2);
            }

        }
    }

    void generateLeaves() {
        int i = 0;

        for(int j = this.leafNodes.length; i < j; ++i) {
            int k = this.leafNodes[i][0];
            int l = this.leafNodes[i][1];
            int i1 = this.leafNodes[i][2];
            this.generateLeafNode(k, l, i1);
        }

    }

    boolean leafNodeNeedsBase(int y) {
        return (double)y >= (double)this.heightLimit * 0.2D;
    }

    void generateTrunk() {
        int i = this.basePos[0];
        int j = this.basePos[1];
        int k = this.basePos[1] + this.height;
        int l = this.basePos[2];
        int[] aint = new int[]{i, j, l};
        int[] aint1 = new int[]{i, k, l};
        this.placeBlockLine(aint, aint1, AlphaBlocks.ALPHA_OAK_LOG.defaultBlockState());
        if (this.trunkSize == 2) {
            ++aint[0];
            ++aint1[0];
            this.placeBlockLine(aint, aint1, AlphaBlocks.ALPHA_OAK_LOG.defaultBlockState());
            ++aint[2];
            ++aint1[2];
            this.placeBlockLine(aint, aint1, AlphaBlocks.ALPHA_OAK_LOG.defaultBlockState());
            aint[0] += -1;
            aint1[0] += -1;
            this.placeBlockLine(aint, aint1, AlphaBlocks.ALPHA_OAK_LOG.defaultBlockState());
        }

    }

    void generateLeafNodeBases() {
        int i = 0;
        int j = this.leafNodes.length;

        for(int[] aint = new int[]{this.basePos[0], this.basePos[1], this.basePos[2]}; i < j; ++i) {
            int[] aint1 = this.leafNodes[i];
            int[] aint2 = new int[]{aint1[0], aint1[1], aint1[2]};
            aint[1] = aint1[3];
            int k = aint[1] - this.basePos[1];
            if (this.leafNodeNeedsBase(k)) {
                this.placeBlockLine(aint, aint2, AlphaBlocks.ALPHA_OAK_LOG.defaultBlockState());
            }
        }

    }

    int checkBlockLine(int[] start, int[] end) {
        int[] aint = new int[]{0, 0, 0};
        byte b0 = 0;

        for(byte b1 = 0; b1 < 3; ++b1) {
            aint[b1] = end[b1] - start[b1];
            if (Math.abs(aint[b1]) > Math.abs(aint[b0])) {
                b0 = b1;
            }
        }

        if (aint[b0] == 0) {
            return -1;
        } else {
            byte b2 = otherCoordPairs[b0];
            byte b3 = otherCoordPairs[b0 + 3];
            byte b4;
            if (aint[b0] > 0) {
                b4 = 1;
            } else {
                b4 = -1;
            }

            double d0 = (double)aint[b2] / (double)aint[b0];
            double d1 = (double)aint[b3] / (double)aint[b0];
            int[] aint1 = new int[]{0, 0, 0};
            int i = 0;

            int j;
            for(j = aint[b0] + b4; i != j; i += b4) {
                aint1[b0] = start[b0] + i;
                aint1[b2] = AlphaMathHelper.floor((double)start[b2] + (double)i * d0);
                aint1[b3] = AlphaMathHelper.floor((double)start[b3] + (double)i * d1);
                BlockState block = this.level.getBlockState(new BlockPos(aint1[0], aint1[1], aint1[2]));
                if (!block.isAir() && !block.is(AlphaBlocks.ALPHA_LEAVES)) {
                    break;
                }
            }

            return i == j ? -1 : Math.abs(i);
        }
    }

    boolean validTreeLocation() {
        int[] aint = new int[]{this.basePos[0], this.basePos[1], this.basePos[2]};
        int[] aint1 = new int[]{this.basePos[0], this.basePos[1] + this.heightLimit - 1, this.basePos[2]};
        BlockState below = this.level.getBlockState(new BlockPos(this.basePos[0], this.basePos[1] - 1, this.basePos[2]));
        if (!below.is(AlphaBlocks.ALPHA_GRASS_BLOCK) && !below.is(AlphaBlocks.ALPHA_DIRT)) {
            return false;
        } else {
            int i = this.checkBlockLine(aint, aint1);
            if (i == -1) {
                return true;
            } else if (i < 6) {
                return false;
            } else {
                this.heightLimit = i;
                return true;
            }
        }
    }

    public boolean generate(WorldGenLevel level, Random random, int x, int y, int z) {
        this.level = level;
        long i = random.nextLong();
        this.rand.setSeed(i);
        this.basePos[0] = x;
        this.basePos[1] = y;
        this.basePos[2] = z;
        if (this.heightLimit == 0) {
            this.heightLimit = 5 + this.rand.nextInt(this.heightLimitLimit);
        }

        if (!this.validTreeLocation()) {
            return false;
        } else {
            this.generateLeafNodeList();
            this.generateLeaves();
            this.generateTrunk();
            this.generateLeafNodeBases();
            return true;
        }
    }
}
