package net.nostalgia.mixin.client.ritual;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.nostalgia.client.render.NostalgiaChunkCache;
import net.nostalgia.client.ritual.RitualVisualManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LevelChunk.class)
public class ClientLevelChunkHologramMixin {

    @Inject(method = "getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;", at = @At("HEAD"), cancellable = true)
    private void onGetBlockState(BlockPos pos, CallbackInfoReturnable<BlockState> cir) {
        if ((RitualVisualManager.isTransitioning && !RitualVisualManager.isBystander) || net.nostalgia.client.render.PortalSkyRenderer.isDebugging) {
            LevelChunk chunk = (LevelChunk) (Object) this;
            if (chunk.getLevel().isClientSide()) {
                BlockState alphaState = NostalgiaChunkCache.getPredictedAlphaBlock(pos.getX(), pos.getY(), pos.getZ());
                if (alphaState != null) {
                    cir.setReturnValue(alphaState);
                }
            }
        }
    }

    @Inject(method = "getFluidState(III)Lnet/minecraft/world/level/material/FluidState;", at = @At("HEAD"), cancellable = true)
    private void onGetFluidState(int x, int y, int z, CallbackInfoReturnable<net.minecraft.world.level.material.FluidState> cir) {
        if ((RitualVisualManager.isTransitioning && !RitualVisualManager.isBystander) || net.nostalgia.client.render.PortalSkyRenderer.isDebugging) {
            LevelChunk chunk = (LevelChunk) (Object) this;
            if (chunk.getLevel().isClientSide()) {
                BlockState alphaState = NostalgiaChunkCache.getPredictedAlphaBlock(x, y, z);
                if (alphaState != null) {
                    cir.setReturnValue(alphaState.getFluidState());
                }
            }
        }
    }
}
