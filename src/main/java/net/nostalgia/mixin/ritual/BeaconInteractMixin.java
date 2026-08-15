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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
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
                if (!net.nostalgia.alphalogic.ritual.TimestopZoneManager.checkZoneStability((net.minecraft.server.level.ServerLevel) level, pos)) {
                    net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(serverPlayer, new net.nostalgia.network.S2CSetTerminalErrorPayload("UNSTABLE"));
                }
                net.nostalgia.alphalogic.ritual.EchoRitualManager.selectBeacon(serverPlayer.getUUID(), pos);
                net.minecraft.world.level.block.entity.BlockEntity be = level.getBlockEntity(pos);
                if (be instanceof net.nostalgia.alphalogic.ritual.TimeMachineStorage storage) {
                    net.minecraft.world.Container proxyContainer = new net.minecraft.world.SimpleContainer(1) {
                        @Override
                        public net.minecraft.world.item.ItemStack getItem(int slot) {
                            return storage.nostalgia$getShard();
                        }
                        @Override
                        public void setItem(int slot, net.minecraft.world.item.ItemStack stack) {
                            storage.nostalgia$setShard(stack);
                        }
                        @Override
                        public net.minecraft.world.item.ItemStack removeItem(int slot, int amount) {
                            net.minecraft.world.item.ItemStack stack = storage.nostalgia$getShard();
                            if (!stack.isEmpty()) {
                                net.minecraft.world.item.ItemStack result = stack.split(amount);
                                storage.nostalgia$setShard(stack.isEmpty() ? net.minecraft.world.item.ItemStack.EMPTY : stack);
                                return result;
                            }
                            return net.minecraft.world.item.ItemStack.EMPTY;
                        }
                        @Override
                        public net.minecraft.world.item.ItemStack removeItemNoUpdate(int slot) {
                            net.minecraft.world.item.ItemStack stack = storage.nostalgia$getShard();
                            storage.nostalgia$setShard(net.minecraft.world.item.ItemStack.EMPTY);
                            return stack;
                        }
                        @Override
                        public boolean isEmpty() {
                            return storage.nostalgia$getShard().isEmpty();
                        }
                        @Override
                        public void clearContent() {
                            storage.nostalgia$setShard(net.minecraft.world.item.ItemStack.EMPTY);
                        }
                        @Override
                        public void setChanged() {
                            super.setChanged();
                            if (be != null) {
                                be.setChanged();
                            }
                        }
                    };
                    serverPlayer.openMenu(new net.minecraft.world.SimpleMenuProvider(
                            (syncId, inv, p) -> new net.nostalgia.inventory.TimeMachineMenu(syncId, inv, proxyContainer, storage.nostalgia$getEnergyData()),
                            net.minecraft.network.chat.Component.literal("Time Machine")
                    ));
                }
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
