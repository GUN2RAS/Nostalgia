package net.nostalgia.mixin.client.ritual;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LevelChunk.class)
public class ClientLevelChunkHologramMixin {

    @Inject(method = "getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;", at = @At("HEAD"), cancellable = true)
    private void onGetBlockState(BlockPos pos, CallbackInfoReturnable<BlockState> cir) {
        if (net.nostalgia.client.events.echo.RitualVisualManager.isTransitioning && net.nostalgia.client.events.echo.RitualVisualManager.isBystander) {
            net.minecraft.core.BlockPos center = net.nostalgia.client.events.echo.RitualVisualManager.ritualCenter;
            if (center != null && (pos.equals(center) || pos.equals(center.below()))) {
                cir.setReturnValue(net.minecraft.world.level.block.Blocks.AIR.defaultBlockState());
                return;
            }
        }

        if (net.nostalgia.client.events.caches.UniversalHologramCache.INSTANCE.isActive()) {
            LevelChunk chunk = (LevelChunk) (Object) this;
            if (chunk.getLevel().isClientSide()) {
                BlockState spoofed = net.nostalgia.client.events.caches.UniversalHologramCache.INSTANCE.getSpoofedBlock(pos.getX(), pos.getY(), pos.getZ());
                if (spoofed != null) {
                    cir.setReturnValue(spoofed);
                }
            }
        }
    }

    @Inject(method = "getFluidState(III)Lnet/minecraft/world/level/material/FluidState;", at = @At("HEAD"), cancellable = true)
    private void onGetFluidState(int x, int y, int z, CallbackInfoReturnable<net.minecraft.world.level.material.FluidState> cir) {
        if (net.nostalgia.client.events.echo.RitualVisualManager.isTransitioning && net.nostalgia.client.events.echo.RitualVisualManager.isBystander) {
            net.minecraft.core.BlockPos center = net.nostalgia.client.events.echo.RitualVisualManager.ritualCenter;
            if (center != null && x == center.getX() && z == center.getZ() && (y == center.getY() || y == center.getY() - 1)) {
                cir.setReturnValue(net.minecraft.world.level.material.Fluids.EMPTY.defaultFluidState());
                return;
            }
        }

        if (net.nostalgia.client.events.caches.UniversalHologramCache.INSTANCE.isActive()) {
            LevelChunk chunk = (LevelChunk) (Object) this;
            if (chunk.getLevel().isClientSide()) {
                BlockState spoofed = net.nostalgia.client.events.caches.UniversalHologramCache.INSTANCE.getSpoofedBlock(x, y, z);
                if (spoofed != null) {
                    cir.setReturnValue(spoofed.getFluidState());
                }
            }
        }
    }
}
