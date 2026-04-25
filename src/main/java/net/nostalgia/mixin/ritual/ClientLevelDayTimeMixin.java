package net.nostalgia.mixin.ritual;

import net.minecraft.client.ClientClockManager;
import net.minecraft.core.Holder;
import net.minecraft.world.clock.WorldClock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientClockManager.class)
public class ClientLevelDayTimeMixin {

    @Inject(method = "getTotalTicks", at = @At("RETURN"), cancellable = true)
    private void rewindDayTime(Holder<WorldClock> definition, CallbackInfoReturnable<Long> cir) {
        if (net.nostalgia.client.ritual.RitualVisualManager.isTransitioning && !net.nostalgia.client.ritual.RitualVisualManager.isBystander) {
            long newTime = net.nostalgia.client.ritual.RitualVisualManager.calculateInertialTime(cir.getReturnValue());
            cir.setReturnValue(newTime);
        }
    }
}
