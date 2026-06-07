package net.nostalgia.mixin.alpha;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.nostalgia.world.dimension.ModDimensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryScreen.class)
public abstract class AlphaInventoryScreenMixin {

    @Inject(method = "extractBackground", at = @At("TAIL"))
    private void hideOffhandVisuals(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a, CallbackInfo ci) {
        Minecraft client = Minecraft.getInstance();
        if (client.level != null && client.level.dimension() == ModDimensions.ALPHA_112_01_LEVEL_KEY) {
            AbstractContainerScreenAccessor accessor = (AbstractContainerScreenAccessor) (Object) this;
            int leftPos = accessor.getLeftPos();
            int topPos = accessor.getTopPos();

            graphics.fill(leftPos + 76, topPos + 61, leftPos + 76 + 18, topPos + 61 + 18, 0xFFC6C6C6);
        }
    }
}
