package net.nostalgia.mixin.ritual;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BeaconBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BeaconBlock.class)
public class BeaconInteractMixin {
    @Inject(method = "useWithoutItem", at = @At("HEAD"), cancellable = true)
    private void interceptBeaconUse(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir) {
        
        BlockPos anchorPos = pos.below();
        boolean isTimeMachine = false;

        if (level.getBlockState(anchorPos).is(net.minecraft.world.level.block.Blocks.RESPAWN_ANCHOR)) {
            BlockState anchorState = level.getBlockState(anchorPos);
            if (anchorState.hasProperty(net.minecraft.world.level.block.RespawnAnchorBlock.CHARGE) && 
                anchorState.getValue(net.minecraft.world.level.block.RespawnAnchorBlock.CHARGE) == 4) {
                isTimeMachine = true;
            }
        }

        if (isTimeMachine) {
            if (!level.isClientSide() && player instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
                if (!net.nostalgia.alphalogic.ritual.RitualManager.checkZoneStability((net.minecraft.server.level.ServerLevel) level, pos)) {
                    net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(serverPlayer, new net.nostalgia.network.S2CSetTerminalErrorPayload());
                }
                net.nostalgia.alphalogic.ritual.RitualManager.selectBeacon(serverPlayer.getUUID(), pos);
                serverPlayer.openMenu(new net.minecraft.world.SimpleMenuProvider(
                        (syncId, inv, p) -> new net.nostalgia.inventory.TimeMachineMenu(syncId, inv),
                        net.minecraft.network.chat.Component.literal("Time Machine")
                ));
            }
            cir.setReturnValue(InteractionResult.SUCCESS);
            return;
        }

        if (net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.activeRitual() != null) {
            cir.setReturnValue(InteractionResult.SUCCESS);
            return;
        }
    }
}
