package net.nostalgia.mixin.client.punchy;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import punchy.client.state.SpearStateMachine;

@Mixin(value = SpearStateMachine.class, remap = false)
public class SpearStateMachineMixin {

    @Inject(method = "isSpearStack", at = @At("HEAD"), cancellable = true)
    private static void nostalgia$amethystIsNotSpear(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (stack != null && stack.is(Items.AMETHYST_SHARD)) {
            cir.setReturnValue(false);
        }
    }
}
