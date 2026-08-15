package net.nostalgia.mixin.client;

import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.Minecraft;
import net.nostalgia.client.gui.EpilepsyWarningScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen {

    protected TitleScreenMixin(net.minecraft.network.chat.Component component) {
        super(component);
    }

    @Inject(method = "init", at = @At("HEAD"), cancellable = true)
    private void onInit(CallbackInfo ci) {
        if (!EpilepsyWarningScreen.hasAcceptedWarning() && this.minecraft != null) {
            Minecraft.getInstance().setScreen(new EpilepsyWarningScreen(this));
            ci.cancel();
        }
    }
}
