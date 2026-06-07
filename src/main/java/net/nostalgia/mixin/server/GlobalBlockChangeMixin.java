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
public class GlobalBlockChangeMixin {
    @Inject(method = "setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;II)Z", at = @At("HEAD"))
    private void onGlobalSetBlock(BlockPos pos, BlockState newState, int flags, int maxUpdateDepth, CallbackInfoReturnable<Boolean> cir) {
        Level level = (Level) (Object) this;
        if (level.isClientSide() || !(level instanceof ServerLevel sl)) return;

        if (sl.getChunkSource().hasChunk(pos.getX() >> 4, pos.getZ() >> 4)) {
            net.minecraft.world.level.chunk.ChunkAccess chunk = sl.getChunk(pos.getX() >> 4, pos.getZ() >> 4, net.minecraft.world.level.chunk.status.ChunkStatus.FULL, false);
            if (chunk != null) {
                BlockState oldState = chunk.getBlockState(pos);
                if (oldState == newState) return;

                String dimId = sl.dimension().identifier().toString();

                if (net.nostalgia.alphalogic.ritual.DimensionUtil.isClientGenerated(dimId)) {
                    net.nostalgia.alphalogic.ritual.HologramWorldData.get(sl).addDelta(pos.immutable(), newState);
                } else {
                    net.nostalgia.alphalogic.ritual.ServerChunkTracker.get(sl).markDirty(pos.immutable());
                }

                if ((flags & 2) != 0) {
                    net.nostalgia.alphalogic.ritual.DeltaSyncService.broadcastSingleDelta(
                            sl.getServer(), pos.immutable(), newState, dimId, null);
                }
            }
        }
    }
}
