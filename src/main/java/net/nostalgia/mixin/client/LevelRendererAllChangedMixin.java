package net.nostalgia.mixin.client;

import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererAllChangedMixin {

    @Inject(method = "allChanged", at = @At("HEAD"))
    private void nostalgia$clearHologramCaches(CallbackInfo ci) {
        net.nostalgia.client.render.NostalgiaChunkCache.clear();
        net.nostalgia.client.ritual.ClientVirtualBlockCache.clear();
    }
}
