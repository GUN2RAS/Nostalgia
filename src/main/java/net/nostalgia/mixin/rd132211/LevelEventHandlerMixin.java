package net.nostalgia.mixin.rd132211;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelEventHandler;
import net.minecraft.core.BlockPos;
import net.nostalgia.world.dimension.ModDimensions;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelEventHandler.class)
public class LevelEventHandlerMixin {
    @Shadow
    @Final
    private ClientLevel level;

    @Inject(method = "levelEvent", at = @At("HEAD"), cancellable = true)
    private void onLevelEvent(int type, BlockPos pos, int data, CallbackInfo ci) {
        if (this.level != null && this.level.dimension().equals(ModDimensions.RD_132211_LEVEL_KEY)) {
            if (type == 2001) {
                ci.cancel();
            }
        }
    }
}
