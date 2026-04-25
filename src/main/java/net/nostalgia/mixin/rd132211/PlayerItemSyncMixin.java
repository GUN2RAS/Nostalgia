package net.nostalgia.mixin.rd132211;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.nostalgia.block.ModBlocks;
import net.nostalgia.world.dimension.ModDimensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class PlayerItemSyncMixin {

    @Inject(method = "getItemInHand", at = @At("HEAD"), cancellable = true)
    private void onGetItemInHand(InteractionHand hand, CallbackInfoReturnable<ItemStack> cir) {
        if ((Object) this instanceof Player player) {
            if (player.level() != null && player.level().dimension().equals(ModDimensions.RD_132211_LEVEL_KEY)) {
                
                if (hand == InteractionHand.MAIN_HAND) {

                    if (player.getY() > 41) {
                        cir.setReturnValue(new ItemStack(ModBlocks.RD_GRASS));
                    } else {
                        cir.setReturnValue(new ItemStack(ModBlocks.RD_STONE));
                    }
                }
            }
        }
    }
}
