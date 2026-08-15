package net.nostalgia.mixin.rd132211;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.nostalgia.world.dimension.ModDimensions;
import net.nostalgia.world.rules.NostalgiaRules;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemInHandRenderer.class)
public abstract class ItemInHandRendererMixin {
    @Shadow @Final private Minecraft minecraft;

    @Inject(method = "renderHandsWithItems", at = @At("HEAD"), cancellable = true)
    private void onRenderHands(float partialTick, PoseStack poseStack, SubmitNodeCollector collector,
            LocalPlayer player, int light, CallbackInfo ci) {
        if (player != null && player.level() != null && player.level().dimension().equals(ModDimensions.RD_132211_LEVEL_KEY)) {
            ci.cancel();
        }
    }

    @Inject(method = "itemUsed", at = @At("HEAD"), cancellable = true)
    private void nostalgia$disableBowItemUsedDip(InteractionHand hand, CallbackInfo ci) {
        if (this.minecraft.player != null && this.minecraft.player.level() != null) {
            if (NostalgiaRules.getForLevel(this.minecraft.player.level()).instantBowShoot) {
                ItemStack stack = this.minecraft.player.getItemInHand(hand);
                if (stack.is(Items.BOW)) {
                    ci.cancel();
                }
            }
        }
    }

    @Inject(method = "shouldInstantlyReplaceVisibleItem", at = @At("HEAD"), cancellable = true)
    private void nostalgia$disableBowDurabilitySwap(ItemStack currentlyVisibleItem, ItemStack expectedItem, CallbackInfoReturnable<Boolean> cir) {
        if (this.minecraft.player != null && this.minecraft.player.level() != null) {
            if (NostalgiaRules.getForLevel(this.minecraft.player.level()).instantBowShoot) {
                if (currentlyVisibleItem.is(Items.BOW) && expectedItem.is(Items.BOW)) {
                    cir.setReturnValue(true);
                }
            }
        }
    }
}
