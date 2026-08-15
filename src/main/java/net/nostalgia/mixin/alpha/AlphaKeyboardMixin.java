package net.nostalgia.mixin.alpha;

import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.nostalgia.alphalogic.core.AlphaRenderState;
import net.nostalgia.world.dimension.ModDimensions;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public abstract class AlphaKeyboardMixin {

    @Inject(method = "keyPress", at = @At("HEAD"), cancellable = true)
    private void onKeyPress(long window, int action, net.minecraft.client.input.KeyEvent keyEvent, CallbackInfo ci) {
        if (keyEvent.key() == GLFW.GLFW_KEY_F && action == GLFW.GLFW_PRESS) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null && mc.level != null && mc.level.dimension() == ModDimensions.ALPHA_112_01_LEVEL_KEY) {
                AlphaRenderState.cycleRenderDistance();
                ci.cancel();
            }
        }
    }
}
