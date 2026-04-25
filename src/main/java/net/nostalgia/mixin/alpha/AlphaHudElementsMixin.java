package net.nostalgia.mixin.alpha;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.entity.player.Player;
import net.nostalgia.world.dimension.ModDimensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.AttackIndicatorStatus;

@Mixin(Gui.class)
public abstract class AlphaHudElementsMixin {

    private boolean isAlphaMode() {
        Minecraft client = Minecraft.getInstance();
        return client.level != null && client.level.dimension() == ModDimensions.ALPHA_112_01_LEVEL_KEY;
    }

    @Inject(method = "extractFood", at = @At("HEAD"), cancellable = true)
    private void hideAlphaFood(GuiGraphicsExtractor graphics, Player player, int y, int right, CallbackInfo ci) {
        if (isAlphaMode()) {
            ci.cancel();
        }
    }

    @ModifyArg(method = "extractPlayerHealth", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;extractHearts(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/world/entity/player/Player;IIIIFIIIZ)V"), index = 3)
    private int lowerAlphaHearts(int yLineBase) {
        return isAlphaMode() ? yLineBase + 3 : yLineBase;
    }

    @ModifyArg(method = "extractPlayerHealth", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;extractArmor(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/world/entity/player/Player;IIII)V"), index = 2)
    private int lowerAlphaArmor(int yLineBase) {
        return isAlphaMode() ? yLineBase + 3 : yLineBase;
    }

    @ModifyArg(method = "extractPlayerHealth", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;extractAirBubbles(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/world/entity/player/Player;III)V"), index = 3)
    private int lowerAlphaAirBubbles(int yLineAir) {

        return isAlphaMode() ? yLineAir + 10 + 3 : yLineAir;
    }

    @Redirect(method = { "extractCrosshair", "extractHotbar" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/OptionInstance;get()Ljava/lang/Object;"))
    private Object redirectAttackIndicatorGet(OptionInstance<?> instance) {
        if (isAlphaMode()) {
            Object val = instance.get();
            if (val instanceof AttackIndicatorStatus) {
                return AttackIndicatorStatus.OFF;
            }
            return val;
        }
        return instance.get();
    }
}
