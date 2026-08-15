package net.nostalgia.mixin.ritual;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.class)
public class LodestoneInteractMixin {
    @Inject(method = "useWithoutItem", at = @At("HEAD"), cancellable = true)
    private void interceptLodestoneUse(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir) {
        if (state.is(Blocks.LODESTONE)) {
            if (!level.isClientSide() && player instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
                net.minecraft.world.Container temporaryContainer = new net.minecraft.world.SimpleContainer(1);
                serverPlayer.openMenu(new net.minecraft.world.SimpleMenuProvider(
                        (syncId, inv, p) -> new net.nostalgia.inventory.LodestoneGravityMenu(syncId, inv, temporaryContainer),
                        net.minecraft.network.chat.Component.literal("Lodestone Programmer")
                ));
            }
            cir.setReturnValue(InteractionResult.SUCCESS);
        }
    }
}
