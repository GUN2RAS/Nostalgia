package net.nostalgia.mixin.physics;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FarmlandBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.nostalgia.block.AlphaBlocks;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FarmlandBlock.class)
public class AlphaFarmlandMixin {
    @Inject(method = "turnToDirt", at = @At("HEAD"), cancellable = true)
    private static void turnToAlphaDirt(@Nullable Entity sourceEntity, BlockState state, Level level, BlockPos pos, CallbackInfo ci) {
        if (state.is(AlphaBlocks.ALPHA_FARMLAND)) {
            BlockState newState = Block.pushEntitiesUp(state, AlphaBlocks.ALPHA_DIRT.defaultBlockState(), level, pos);
            level.setBlockAndUpdate(pos, newState);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(sourceEntity, newState));
            ci.cancel();
        }
    }
}
