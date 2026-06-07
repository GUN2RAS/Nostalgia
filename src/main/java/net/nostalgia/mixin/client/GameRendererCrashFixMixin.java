package net.nostalgia.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererCrashFixMixin {

    @Inject(method = "renderLevel", at = @At("HEAD"), cancellable = true)
    private void nostalgia$cancelRenderWithoutPlayer(net.minecraft.client.DeltaTracker tickCounter, CallbackInfo ci) {

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null && mc.getCameraEntity() == null) {
            ci.cancel();
        }
    }
}
