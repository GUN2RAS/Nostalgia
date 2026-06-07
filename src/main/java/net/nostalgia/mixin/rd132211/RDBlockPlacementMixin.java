package net.nostalgia.mixin.rd132211;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.nostalgia.block.ModBlocks;
import net.nostalgia.world.dimension.ModDimensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Level.class)
public abstract class RDBlockPlacementMixin {

    @ModifyVariable(method = "setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;II)Z", at = @At("HEAD"), argsOnly = true)
    private BlockState modifyPlacedBlockState(BlockState state, BlockPos pos, BlockState sameState, int flags,
            int recursion) {
        Level level = (Level) (Object) this;
        if (level.dimension().equals(ModDimensions.RD_132211_LEVEL_KEY)) {
            
            if (!state.isAir()) {
                if (pos.getY() == 42) {
                    return ModBlocks.RD_GRASS.defaultBlockState();
                } else {
                    return ModBlocks.RD_STONE.defaultBlockState();
                }
            }
        }
        return state;
    }
}
