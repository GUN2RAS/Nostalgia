package net.nostalgia.mixin.server;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerGameMode.class)
public class HologramBlockBreakMixin {

    @Shadow @Final protected ServerPlayer player;
    @Shadow protected ServerLevel level;

    @Inject(method = "handleBlockBreakAction", at = @At("HEAD"), cancellable = true)
    private void onBreakHologramBlock(BlockPos pos, ServerboundPlayerActionPacket.Action action, Direction direction, int buildHeight, int sequence, CallbackInfo ci) {
        net.nostalgia.alphalogic.ritual.HologramInteractionHandler.HologramMatch match = 
            net.nostalgia.alphalogic.ritual.HologramInteractionHandler.checkHologramInteraction(level, pos, player);
        if (match == null) {
            if (net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.isParticipantAny(player.getUUID())) {
                ci.cancel(); // Защита: участники ритуала не могут ломать физические блоки
            }
            return;
        }

        net.minecraft.world.level.block.state.BlockState breakingState = level.getBlockState(pos);
        if (breakingState.is(net.minecraft.world.level.block.Blocks.BEACON)) return;

        boolean shouldBreak = false;
        boolean shouldDrop = false;
        if (action == ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK && player.isCreative()) {
            shouldBreak = true;
        } else if (action == ServerboundPlayerActionPacket.Action.STOP_DESTROY_BLOCK && !player.isCreative()) {
            shouldBreak = true;
            shouldDrop = true;
        }
        if (!shouldBreak) return;

        net.minecraft.world.level.block.state.BlockState oldState = match.targetLevel.getBlockState(match.targetPos);
        if (oldState.isAir()) oldState = Blocks.DIRT.defaultBlockState();

        if (shouldDrop) {
            net.minecraft.world.level.block.Block.dropResources(oldState, match.targetLevel, match.targetPos, null, player, player.getMainHandItem());
        }

        match.targetLevel.getChunk(match.targetPos.getX() >> 4, match.targetPos.getZ() >> 4, net.minecraft.world.level.chunk.status.ChunkStatus.FULL, true);
        match.targetLevel.setBlock(match.targetPos, Blocks.AIR.defaultBlockState(), 3);

        ci.cancel();
    }

    @Inject(method = "destroyBlock", at = @At("RETURN"))
    private void onPhysicalAlphaBreak(BlockPos pos, org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValueZ() && net.nostalgia.alphalogic.ritual.DimensionUtil.isClientGenerated(level.dimension().identifier().toString())) {
            net.nostalgia.alphalogic.ritual.HologramWorldData.get(level).addDelta(pos, Blocks.AIR.defaultBlockState());
        }
    }
}
