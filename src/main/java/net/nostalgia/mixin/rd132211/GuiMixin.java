package net.nostalgia.mixin.rd132211;

import net.minecraft.client.gui.Gui;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.nostalgia.world.dimension.ModDimensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.GuiGraphicsExtractor;

@Mixin(Gui.class)
public class GuiMixin {

    @Inject(method = "extractRenderState", at = @At("HEAD"), cancellable = true)
    private void onExtractRenderState(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        Minecraft client = Minecraft.getInstance();
        if (client.level != null && client.level.dimension().equals(ModDimensions.RD_132211_LEVEL_KEY)) {
            
            ci.cancel();
        }
    }
}
