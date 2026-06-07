package net.nostalgia.mixin.alpha;

import net.minecraft.client.Minecraft;
import net.minecraft.world.inventory.Slot;
import net.nostalgia.world.dimension.ModDimensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Slot.class)
public class AlphaSlotMixin {
    @Inject(method = "isActive", at = @At("HEAD"), cancellable = true)
    private void hideAlphaOffhand(CallbackInfoReturnable<Boolean> cir) {
        Minecraft client = Minecraft.getInstance();
        if (client != null && client.level != null && client.player != null && client.player.inventoryMenu != null) {
            if (client.level.dimension() == ModDimensions.ALPHA_112_01_LEVEL_KEY) {
                Slot slot = (Slot) (Object) this;
                
                if (client.player.inventoryMenu.slots.size() > 45 && client.player.inventoryMenu.slots.get(45) == slot) {
                    cir.setReturnValue(false);
                }
            }
        }
    }
}
