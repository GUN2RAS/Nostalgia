package net.nostalgia.mixin.alpha;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractRecipeBookScreen;
import net.nostalgia.world.dimension.ModDimensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractRecipeBookScreen.class)
public abstract class AlphaAbstractRecipeBookScreenMixin {

    @Inject(method = "initButton", at = @At("HEAD"), cancellable = true)
    private void hideRecipeBookButton(CallbackInfo ci) {
        Minecraft client = Minecraft.getInstance();
        if (client.level != null && client.level.dimension() == ModDimensions.ALPHA_112_01_LEVEL_KEY) {
            ci.cancel();
        }
    }
}
