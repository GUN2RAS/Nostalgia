package net.nostalgia.mixin.ritual;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.nostalgia.alphalogic.ritual.RitualManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.world.InteractionResult;

@Mixin(RespawnAnchorBlock.class)
public class RitualAnchorMixin {
    @Inject(method = "useItemOn", at = @At("RETURN"))
    private void onAnchorCharged(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir) {
        if (cir.getReturnValue().consumesAction() && level instanceof ServerLevel) {
            if (RitualManager.getClientState() != RitualManager.State.INACTIVE) return;

            BlockPos beaconPos = pos.above();

            if (level.getBlockState(beaconPos).is(Blocks.BEACON)) {
                
                BlockState currentState = level.getBlockState(pos);
                if (currentState.hasProperty(RespawnAnchorBlock.CHARGE) && currentState.getValue(RespawnAnchorBlock.CHARGE) == 4) {
                    RitualManager.startRitual((ServerLevel) level, beaconPos);
                }
            }
        }
    }
}
