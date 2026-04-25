package net.nostalgia.mixin.physics;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.nostalgia.world.dimension.ModDimensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class AlphaConsumableMixin {

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void alphaInstantConsume(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        if (net.nostalgia.world.rules.NostalgiaRules.getForLevel(level).instantFoodConsume) {
            ItemStack stack = (ItemStack) (Object) this;
            if (stack.has(DataComponents.FOOD) || stack.has(DataComponents.CONSUMABLE)) {
                ItemStack result = stack.finishUsingItem(level, player);
                player.setItemInHand(hand, result);
                cir.setReturnValue(InteractionResult.CONSUME);
            }
        }
    }
}
