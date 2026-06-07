package net.nostalgia.mixin.server;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
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
        BlockPos clickedPos = hitResult.getBlockPos();
        BlockPos targetPos = clickedPos.relative(hitResult.getDirection());
        
        net.nostalgia.alphalogic.ritual.HologramInteractionHandler.HologramMatch match = 
            net.nostalgia.alphalogic.ritual.HologramInteractionHandler.checkHologramInteraction((ServerLevel) level, targetPos, player);
        
        if (match != null) {
            if (!(stack.getItem() instanceof net.minecraft.world.item.BlockItem blockItem)) return;

            net.minecraft.world.level.block.state.BlockState stateToPlace = blockItem.getBlock().defaultBlockState();
            
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }

            match.targetLevel.getChunk(match.targetPos.getX() >> 4, match.targetPos.getZ() >> 4, net.minecraft.world.level.chunk.status.ChunkStatus.FULL, true);
            match.targetLevel.setBlock(match.targetPos, stateToPlace, 3);

            cir.setReturnValue(InteractionResult.SUCCESS);
            return;
        }

        if (net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.isParticipantAny(player.getUUID())) {
            cir.setReturnValue(InteractionResult.FAIL);
        }
    }
}
