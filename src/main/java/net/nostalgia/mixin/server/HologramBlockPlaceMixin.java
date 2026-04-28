package net.nostalgia.mixin.server;

import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.nostalgia.alphalogic.ritual.RitualActiveState;
import net.nostalgia.alphalogic.ritual.RitualManager;
import net.nostalgia.alphalogic.ritual.VirtualBlockCache;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.Set;
import java.util.UUID;

@Mixin(ServerPlayerGameMode.class)
public class HologramBlockPlaceMixin {

    @Shadow @Final protected ServerPlayer player;

    @Inject(method = "useItemOn", at = @At("HEAD"), cancellable = true)
    private void onPlaceHologramBlock(ServerPlayer player, Level level, ItemStack stack, InteractionHand hand, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir) {
        net.nostalgia.alphalogic.ritual.event.TransitionEvent transition = net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.activeTransition();
        boolean skyPortal = net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.isSkyPortalActive();
        if (transition != null || skyPortal) {
            BlockPos beacon = transition != null ? transition.beaconPos() : RitualManager.getTargetBeaconPos();
            BlockPos targetPos = hitResult.getBlockPos().relative(hitResult.getDirection());
            if (beacon != null && targetPos.closerThan(beacon, 250.0)) {
                if (stack.getItem() instanceof net.minecraft.world.item.BlockItem blockItem) {
                    net.minecraft.world.level.block.state.BlockState stateToPlace = blockItem.getBlock().defaultBlockState();
                    VirtualBlockCache.put(targetPos, stateToPlace);
                    
                    if (!player.getAbilities().instabuild) {
                        stack.shrink(1);
                    }
                    
                    long[] posArr = new long[] { targetPos.asLong() };
                    int[] stateArr = new int[] { net.minecraft.world.level.block.Block.getId(stateToPlace) };
                    MinecraftServer server = ((ServerLevel) player.level()).getServer();
                    Set<UUID> targets = RitualActiveState.participants;
                    if (targets.isEmpty() || !RitualManager.isServerActive()) {
                        net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(player, new net.nostalgia.network.S2CSyncAlphaDeltasPayload(posArr, stateArr));
                    } else {
                        for (UUID uuid : targets) {
                            ServerPlayer target = server.getPlayerList().getPlayer(uuid);
                            if (target != null) net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(target, new net.nostalgia.network.S2CSyncAlphaDeltasPayload(posArr, stateArr));
                        }
                    }
                    cir.setReturnValue(InteractionResult.SUCCESS);
                }
            }
        }
    }
}
