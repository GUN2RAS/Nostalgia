package net.nostalgia.mixin.server;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Level.class)
public class AlphaBlockChangeMixin {
    @Inject(method = "setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;II)Z", at = @At("HEAD"))
    private void onAlphaSetBlock(BlockPos pos, BlockState newState, int flags, int maxUpdateDepth, CallbackInfoReturnable<Boolean> cir) {
        Level level = (Level) (Object) this;
        if (!level.isClientSide() && level instanceof ServerLevel sl && sl.dimension() == net.nostalgia.world.dimension.ModDimensions.ALPHA_112_01_LEVEL_KEY) {
            
            if (sl.getChunkSource().hasChunk(pos.getX() >> 4, pos.getZ() >> 4)) {
                net.minecraft.world.level.chunk.ChunkAccess chunk = sl.getChunk(pos.getX() >> 4, pos.getZ() >> 4, net.minecraft.world.level.chunk.status.ChunkStatus.FULL, false);
                if (chunk != null) {
                    BlockState oldState = chunk.getBlockState(pos);
                    if (oldState != newState) {
                        net.nostalgia.alphalogic.ritual.AlphaWorldData.get(sl).addDelta(pos.immutable(), newState);
                    }
                }
            }
        }
    }
}
