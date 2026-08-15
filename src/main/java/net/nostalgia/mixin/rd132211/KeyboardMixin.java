package net.nostalgia.mixin.rd132211;

import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.input.KeyEvent;
import net.nostalgia.world.dimension.ModDimensions;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public class KeyboardMixin {

    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(method = "keyPress", at = @At("HEAD"), cancellable = true)
    private void onKeyPress(long window, int action, KeyEvent event, CallbackInfo ci) {
        if (this.minecraft.player != null
                && this.minecraft.player.level().dimension().equals(ModDimensions.RD_132211_LEVEL_KEY)) {
            int key = event.key();

            if (key == 256 && action == 1) { 
                net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.send(new net.nostalgia.network.C2STravelRequestPayload("overworld"));
                ci.cancel();
                return;
            }

            boolean isAllowed = (key == 87 || key == 65 || key == 83 || key == 68 || key == 32 || key == 256);

            if (!isAllowed) {
                ci.cancel();
            }
        }
    }
}
