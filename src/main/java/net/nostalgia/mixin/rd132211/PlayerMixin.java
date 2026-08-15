package net.nostalgia.mixin.rd132211;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.nostalgia.world.dimension.ModDimensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class PlayerMixin {

    @Inject(method = "setShiftKeyDown", at = @At("HEAD"), cancellable = true)
    private void onSetShiftKeyDown(boolean shiftKeyDown, CallbackInfo ci) {
        if ((Object) this instanceof Player player) {
            if (player.level() != null && player.level().dimension().equals(ModDimensions.RD_132211_LEVEL_KEY)) {
                ci.cancel();
            }
        }
    }

    @Inject(method = "isSprinting", at = @At("HEAD"), cancellable = true)
    private void onIsSprinting(CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this instanceof Player player) {
            if (player.level() != null && player.level().dimension().equals(ModDimensions.RD_132211_LEVEL_KEY)) {
                cir.setReturnValue(false);
            }
        }
    }

    @Inject(method = "setSprinting", at = @At("HEAD"), cancellable = true)
    private void onSetSprinting(boolean sprinting, CallbackInfo ci) {
        if ((Object) this instanceof Player player) {
            if (player.level() != null && player.level().dimension().equals(ModDimensions.RD_132211_LEVEL_KEY)) {
                ci.cancel();
            }
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        if ((Object) this instanceof Player player) {
            if (player.level() != null && player.level().dimension().equals(ModDimensions.RD_132211_LEVEL_KEY)) {
                player.getAbilities().mayfly = false;
                player.getAbilities().flying = false;
                player.getAbilities().mayBuild = true;
            }
        }
    }
}
