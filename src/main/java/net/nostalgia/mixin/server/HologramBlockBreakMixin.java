package net.nostalgia.mixin.server;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.level.block.Blocks;
import net.nostalgia.alphalogic.ritual.RitualManager;
import net.nostalgia.alphalogic.ritual.VirtualBlockCache;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.Set;
import java.util.UUID;

@Mixin(ServerPlayerGameMode.class)
public class HologramBlockBreakMixin {

    @Shadow @Final protected ServerPlayer player;
    @Shadow protected ServerLevel level;

    @Inject(method = "handleBlockBreakAction", at = @At("HEAD"), cancellable = true)
    private void onBreakHologramBlock(BlockPos pos, ServerboundPlayerActionPacket.Action action, Direction direction, int buildHeight, int sequence, CallbackInfo ci) {
        net.nostalgia.alphalogic.ritual.TransitionEventInstance inst = net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.findInstanceForParticipant(player.getUUID());
        boolean skyPortal = net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.isSkyPortalActive();
        if (inst == null && !skyPortal) return;

        BlockPos beacon = inst != null ? inst.beaconPos() : RitualManager.getTargetBeaconPos();
        if (beacon == null || !pos.closerThan(beacon, 250.0)) return;

        boolean shouldBreak = false;
        boolean shouldDrop = false;
        if (action == ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK && player.isCreative()) {
            shouldBreak = true;
        } else if (action == ServerboundPlayerActionPacket.Action.STOP_DESTROY_BLOCK && !player.isCreative()) {
            shouldBreak = true;
            shouldDrop = true;
        }
        if (!shouldBreak) return;

        int ox = inst != null ? inst.offsetX() : net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.offsetX();
        int oy = inst != null ? inst.yOffset() : net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.yOffset();
        int oz = inst != null ? inst.offsetZ() : net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.offsetZ();

        net.minecraft.world.level.block.state.BlockState oldState = VirtualBlockCache.get(pos);
        if (oldState == null) {
            net.minecraft.server.level.ServerLevel alphaLvl = level.getServer().getLevel(net.nostalgia.world.dimension.ModDimensions.ALPHA_112_01_LEVEL_KEY);
            if (alphaLvl != null) {
                int ax = pos.getX() + ox;
                int ay = pos.getY() - oy;
                int az = pos.getZ() + oz;
                oldState = alphaLvl.getBlockState(new BlockPos(ax, ay, az));
            }
        }
        if (oldState == null || oldState.isAir()) oldState = Blocks.DIRT.defaultBlockState();

        if (shouldDrop) {
            net.minecraft.world.level.block.Block.dropResources(oldState, level, pos, null, player, player.getMainHandItem());
        }

        VirtualBlockCache.put(pos, Blocks.AIR.defaultBlockState());

        long[] posArr = new long[] { pos.asLong() };
        int[] stateArr = new int[] { net.minecraft.world.level.block.Block.getId(Blocks.AIR.defaultBlockState()) };
        MinecraftServer server = ((ServerLevel) player.level()).getServer();
        Set<UUID> targets = inst != null ? inst.participants() : java.util.Collections.emptySet();
        if (targets.isEmpty()) {
            net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(player, new net.nostalgia.network.S2CSyncAlphaDeltasPayload(posArr, stateArr));
        } else {
            for (UUID uuid : targets) {
                ServerPlayer target = server.getPlayerList().getPlayer(uuid);
                if (target != null) net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(target, new net.nostalgia.network.S2CSyncAlphaDeltasPayload(posArr, stateArr));
            }
        }
        ci.cancel();
    }

    @Inject(method = "destroyBlock", at = @At("RETURN"))
    private void onPhysicalAlphaBreak(BlockPos pos, org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValueZ() && level.dimension() == net.nostalgia.world.dimension.ModDimensions.ALPHA_112_01_LEVEL_KEY) {
            net.nostalgia.alphalogic.ritual.AlphaWorldData.get(level).addDelta(pos, Blocks.AIR.defaultBlockState());
        }
    }
}
