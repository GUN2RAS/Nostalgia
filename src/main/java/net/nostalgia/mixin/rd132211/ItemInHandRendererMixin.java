package net.nostalgia.mixin.rd132211;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.nostalgia.world.dimension.ModDimensions;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public class ItemInHandRendererMixin {

    @Inject(method = "renderHandsWithItems", at = @At("HEAD"), cancellable = true)
    private void onRenderHands(float partialTick, PoseStack poseStack, SubmitNodeCollector collector,
            LocalPlayer player, int light, CallbackInfo ci) {
        if (player != null && player.level().dimension().equals(ModDimensions.RD_132211_LEVEL_KEY)) {
            ci.cancel();
        }
    }
}
