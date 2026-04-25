package net.nostalgia.alphalogic.gen;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;

import java.util.Random;

public class AlphaWorldGenLakes {
    private final BlockState liquidState;

    public AlphaWorldGenLakes(BlockState state) {
        this.liquidState = state;
    }

    public boolean generate(WorldGenLevel level, Random rand, int x, int y, int z) {
        x -= 8;
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (z -= 8; y > 0 && level.isEmptyBlock(pos.set(x, y, z)); --y) {}
        y -= 4;
        
        boolean[] flags = new boolean[2048];
        int n = rand.nextInt(4) + 4;

        for (int i = 0; i < n; ++i) {
            double d0 = rand.nextDouble() * 6.0D + 3.0D;
            double d1 = rand.nextDouble() * 4.0D + 2.0D;
            double d2 = rand.nextDouble() * 6.0D + 3.0D;
            double d3 = rand.nextDouble() * 16.0D - d0 / 2.0D;
            double d4 = rand.nextDouble() * 8.0D - d1 / 2.0D;
            double d5 = rand.nextDouble() * 16.0D - d2 / 2.0D;

            for (int j = 1; j < 15; ++j) {
                for (int k = 1; k < 15; ++k) {
                    for (int l = 1; l < 7; ++l) {
                        double d6 = ((double)j - d3) / (d0 / 2.0D);
                        double d7 = ((double)l - d4) / (d1 / 2.0D);
                        double d8 = ((double)k - d5) / (d2 / 2.0D);
                        double d9 = d6 * d6 + d7 * d7 + d8 * d8;
                        if (d9 < 1.0D) {
                            flags[(j * 16 + k) * 8 + l] = true;
                        }
                    }
                }
            }
        }
        
        for (int j = 0; j < 16; ++j) {
            for (int k = 0; k < 16; ++k) {
                for (int l = 0; l < 8; ++l) {
                    boolean flag = !flags[(j * 16 + k) * 8 + l] && (
                        j < 15 && flags[((j + 1) * 16 + k) * 8 + l] ||
                        j > 0 && flags[((j - 1) * 16 + k) * 8 + l] ||
                        k < 15 && flags[(j * 16 + (k + 1)) * 8 + l] ||
                        k > 0 && flags[(j * 16 + (k - 1)) * 8 + l] ||
                        l < 7 && flags[(j * 16 + k) * 8 + (l + 1)] ||
                        l > 0 && flags[(j * 16 + k) * 8 + (l - 1)]
                    );

                    if (flag) {
                        pos.set(x + j, y + l, z + k);
                        BlockState material = level.getBlockState(pos);
                        if (l >= 4 && !material.getFluidState().isEmpty()) {
                            return false;
                        }
                        
                        if (l < 4 && !material.isSolid() && !level.getBlockState(pos).is(this.liquidState.getBlock())) {
                            return false;
                        }
                    }
                }
            }
        }
        
        for (int j = 0; j < 16; ++j) {
            for (int k = 0; k < 16; ++k) {
                for (int l = 0; l < 8; ++l) {
                    if (flags[(j * 16 + k) * 8 + l]) {
                        pos.set(x + j, y + l, z + k);
                        level.setBlock(pos, l >= 4 ? Blocks.AIR.defaultBlockState() : this.liquidState, 2);
                    }
                }
            }
        }
        return true;
    }
}
