package net.nostalgia.mixin.ritual;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.nostalgia.alphalogic.ritual.RitualManager;
import net.nostalgia.alphalogic.ritual.event.RitualEventRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public class RitualBlockPlaceMixin {
    @Inject(method = "place", at = @At("RETURN"))
    private void onBlockPlaced(BlockPlaceContext context, CallbackInfoReturnable<InteractionResult> cir) {
        if (cir.getReturnValue().consumesAction() && context.getLevel() instanceof ServerLevel level) {
            if (RitualEventRegistry.activeRitual() != null) return;

            BlockPos currentPos = context.getClickedPos();
            BlockState placed = level.getBlockState(currentPos);
            
            if (placed.is(Blocks.BEACON) || placed.is(Blocks.RESPAWN_ANCHOR)) {
                
                for (int yOffset = -1; yOffset <= 0; yOffset++) {
                    BlockPos anchorPos = currentPos.above(yOffset);
                    BlockPos beaconPos = anchorPos.above();

                    BlockState anchorState = level.getBlockState(anchorPos);
                    BlockState beaconState = level.getBlockState(beaconPos);

                    if (anchorState.is(Blocks.RESPAWN_ANCHOR) && 
                        beaconState.is(Blocks.BEACON)) {
                        
                        if (anchorState.getValue(RespawnAnchorBlock.CHARGE) == 4) {
                            if (!RitualManager.checkZoneStability(level, beaconPos)) {
                                level.playSound(null, beaconPos, net.minecraft.sounds.SoundEvents.BEACON_DEACTIVATE, net.minecraft.sounds.SoundSource.BLOCKS, 1.0F, 1.0F);
                                return;
                            }
                            RitualManager.startRitual(level, beaconPos);
                            return;
                        }
                    }
                }
            }
        }
    }
}
