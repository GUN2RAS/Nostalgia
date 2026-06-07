package net.nostalgia.alphalogic.gen;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.nostalgia.block.AlphaBlocks;

import java.util.Random;

public class AlphaWorldGenLiquids {
    private final BlockState liquidState;

    public AlphaWorldGenLiquids(BlockState state) {
        this.liquidState = state;
    }

    public boolean generate(WorldGenLevel level, Random rand, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        
        if (!level.getBlockState(pos.above()).is(AlphaBlocks.ALPHA_STONE)) return false;
        if (!level.getBlockState(pos.below()).is(AlphaBlocks.ALPHA_STONE)) return false;

        BlockState current = level.getBlockState(pos);
        if (!current.isAir() && !current.is(AlphaBlocks.ALPHA_STONE)) return false;

        int solidFaces = 0;
        if (level.getBlockState(pos.west()).is(AlphaBlocks.ALPHA_STONE)) ++solidFaces;
        if (level.getBlockState(pos.east()).is(AlphaBlocks.ALPHA_STONE)) ++solidFaces;
        if (level.getBlockState(pos.north()).is(AlphaBlocks.ALPHA_STONE)) ++solidFaces;
        if (level.getBlockState(pos.south()).is(AlphaBlocks.ALPHA_STONE)) ++solidFaces;

        int airFaces = 0;
        if (level.getBlockState(pos.west()).isAir()) ++airFaces;
        if (level.getBlockState(pos.east()).isAir()) ++airFaces;
        if (level.getBlockState(pos.north()).isAir()) ++airFaces;
        if (level.getBlockState(pos.south()).isAir()) ++airFaces;

        if (solidFaces == 3 && airFaces == 1) {
            level.setBlock(pos, this.liquidState, 2);
            level.scheduleTick(pos, this.liquidState.getFluidState().getType(), 0);
        }
        return true;
    }
}
