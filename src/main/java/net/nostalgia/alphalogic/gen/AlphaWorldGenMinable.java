package net.nostalgia.alphalogic.gen;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.nostalgia.alphalogic.core.AlphaMathHelper;
import net.nostalgia.block.AlphaBlocks;

import java.util.Random;

public class AlphaWorldGenMinable {
    private final BlockState minableBlockState;
    private final int numberOfBlocks;

    public AlphaWorldGenMinable(BlockState state, int amount) {
        this.minableBlockState = state;
        this.numberOfBlocks = amount;
    }

    public boolean generate(WorldGenLevel level, Random random, int x, int y, int z) {
        float f = random.nextFloat() * (float)Math.PI;
        double d = (float)(x + 8) + AlphaMathHelper.sin(f) * (float)this.numberOfBlocks / 8.0F;
        double d1 = (float)(x + 8) - AlphaMathHelper.sin(f) * (float)this.numberOfBlocks / 8.0F;
        double d2 = (float)(z + 8) + AlphaMathHelper.cos(f) * (float)this.numberOfBlocks / 8.0F;
        double d3 = (float)(z + 8) - AlphaMathHelper.cos(f) * (float)this.numberOfBlocks / 8.0F;
        double d4 = y + random.nextInt(3) - 2;
        double d5 = y + random.nextInt(3) - 2;

        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        for(int l = 0; l <= this.numberOfBlocks; ++l) {
            double d6 = d + (d1 - d) * (double)l / (double)this.numberOfBlocks;
            double d7 = d4 + (d5 - d4) * (double)l / (double)this.numberOfBlocks;
            double d8 = d2 + (d3 - d2) * (double)l / (double)this.numberOfBlocks;
            double d9 = random.nextDouble() * (double)this.numberOfBlocks / 16.0D;
            double d10 = (double)(AlphaMathHelper.sin((float)l * (float)Math.PI / (float)this.numberOfBlocks) + 1.0F) * d9 + 1.0D;
            double d11 = (double)(AlphaMathHelper.sin((float)l * (float)Math.PI / (float)this.numberOfBlocks) + 1.0F) * d9 + 1.0D;
            int i1 = AlphaMathHelper.floor(d6 - d10 / 2.0D);
            int j1 = AlphaMathHelper.floor(d7 - d11 / 2.0D);
            int k1 = AlphaMathHelper.floor(d8 - d10 / 2.0D);
            int l1 = AlphaMathHelper.floor(d6 + d10 / 2.0D);
            int i2 = AlphaMathHelper.floor(d7 + d11 / 2.0D);
            int j2 = AlphaMathHelper.floor(d8 + d10 / 2.0D);

            for(int k2 = i1; k2 <= l1; ++k2) {
                double d12 = ((double)k2 + 0.5D - d6) / (d10 / 2.0D);
                if (d12 * d12 < 1.0D) {
                    for(int l2 = j1; l2 <= i2; ++l2) {
                        double d13 = ((double)l2 + 0.5D - d7) / (d11 / 2.0D);
                        if (d12 * d12 + d13 * d13 < 1.0D) {
                            for(int i3 = k1; i3 <= j2; ++i3) {
                                double d14 = ((double)i3 + 0.5D - d8) / (d10 / 2.0D);
                                if (d12 * d12 + d13 * d13 + d14 * d14 < 1.0D) {
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
