package net.nostalgia.mixin.rd132211;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SkyRenderer;
import net.nostalgia.world.dimension.ModDimensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SkyRenderer.class)
public class SkyRendererMixin {

    @Inject(method = "renderSunMoonAndStars", at = @At("HEAD"), cancellable = true)
    private void onRenderSunMoonAndStars(CallbackInfo ci) {
        Minecraft client = Minecraft.getInstance();
        if (client.player != null && client.player.level().dimension().equals(ModDimensions.RD_132211_LEVEL_KEY)) {
            ci.cancel(); 
        }
    }

    @Inject(method = "renderSunriseAndSunset", at = @At("HEAD"), cancellable = true)
    private void onRenderSunriseAndSunset(CallbackInfo ci) {
        Minecraft client = Minecraft.getInstance();
        if (client.player != null && client.player.level().dimension().equals(ModDimensions.RD_132211_LEVEL_KEY)) {
            ci.cancel(); 
        }
    }

    @Inject(method = "renderSkyDisc", at = @At("HEAD"), cancellable = true)
    private void onRenderSkyDisc(CallbackInfo ci) {
        Minecraft client = Minecraft.getInstance();
        if (client.player != null && client.player.level().dimension().equals(ModDimensions.RD_132211_LEVEL_KEY)) {
            ci.cancel();
        }
    }

    @Inject(method = "renderDarkDisc", at = @At("HEAD"), cancellable = true)
    private void onRenderDarkDisc(CallbackInfo ci) {
        Minecraft client = Minecraft.getInstance();
        if (client.player != null && client.player.level().dimension().equals(ModDimensions.RD_132211_LEVEL_KEY)) {
            ci.cancel();
        }
    }
}
