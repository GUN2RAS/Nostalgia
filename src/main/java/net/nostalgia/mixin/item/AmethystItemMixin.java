package net.nostalgia.mixin.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.nostalgia.entity.ThrownAmethystEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class AmethystItemMixin {

    @Inject(method = "getUseAnimation", at = @At("HEAD"), cancellable = true)
    private void nostalgia$getUseAnimation(ItemStack stack, CallbackInfoReturnable<ItemUseAnimation> cir) {
        if ((Object) this == Items.AMETHYST_SHARD || (Object) this == net.nostalgia.item.ModItems.CHARGED_AMETHYST) {
            cir.setReturnValue(ItemUseAnimation.TRIDENT);
        }
    }

    @Inject(method = "getUseDuration", at = @At("HEAD"), cancellable = true)
    private void nostalgia$getUseDuration(ItemStack stack, LivingEntity entity, CallbackInfoReturnable<Integer> cir) {
        if ((Object) this == Items.AMETHYST_SHARD || (Object) this == net.nostalgia.item.ModItems.CHARGED_AMETHYST) {
            cir.setReturnValue(72000);
        }
    }

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void nostalgia$use(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        if ((Object) this == Items.AMETHYST_SHARD || (Object) this == net.nostalgia.item.ModItems.CHARGED_AMETHYST) {
            player.startUsingItem(hand);
            cir.setReturnValue(InteractionResult.CONSUME);
        }
    }

    @Inject(method = "releaseUsing", at = @At("HEAD"), cancellable = true)
    private void nostalgia$releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeCharged, CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this == Items.AMETHYST_SHARD || (Object) this == net.nostalgia.item.ModItems.CHARGED_AMETHYST) {
            if (livingEntity instanceof Player player) {
                int useTime = 72000 - timeCharged;
                if (useTime >= 10) {
                    if (!level.isClientSide()) {
                        ItemStack thrownStack = stack.copyWithCount(1);
                        stack.consumeAndReturn(1, player);
                        ThrownAmethystEntity amethyst = new ThrownAmethystEntity(level, player, thrownStack);
                        amethyst.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 2.5F, 1.0F);
                        level.addFreshEntity(amethyst);
                        level.playSound(null, amethyst, SoundEvents.SNOWBALL_THROW, SoundSource.PLAYERS, 1.0F, 1.0F);
                    }
                    player.awardStat(Stats.ITEM_USED.get((Item)(Object)this));
                    cir.setReturnValue(true);
                    return;
                }
            }
            cir.setReturnValue(false);
        }
    }
}
