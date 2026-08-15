package net.nostalgia.mixin.physics;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.FuelValues;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FuelValues.class)
public class LegacyPhysicsFuelMixin {

    @Inject(method = "isFuel", at = @At("HEAD"), cancellable = true)
    private void nostalgia$isFuel(ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {
        if (itemStack.is(net.nostalgia.item.AlphaItems.ALPHA_COAL)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "burnDuration", at = @At("HEAD"), cancellable = true)
    private void nostalgia$burnDuration(ItemStack itemStack, CallbackInfoReturnable<Integer> cir) {
        if (itemStack.is(net.nostalgia.item.AlphaItems.ALPHA_COAL)) {
            cir.setReturnValue(1600);
        }
    }
}
