package net.nostalgia.mixin.alpha;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class AlphaNoCritMixin {

    @Inject(method = "canCriticalAttack", at = @At("HEAD"), cancellable = true)
    private void nostalgia$disableCritsInAlpha(Entity target, CallbackInfoReturnable<Boolean> cir) {
        if (net.nostalgia.world.rules.NostalgiaRules.getForLevel(((Player) (Object) this).level()).disableCriticalHits) {
            cir.setReturnValue(false);
        }
    }
}
