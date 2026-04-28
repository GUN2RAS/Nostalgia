package net.nostalgia.mixin.client.ritual;

import net.minecraft.client.ClientClockManager;
import net.minecraft.core.Holder;
import net.minecraft.world.clock.WorldClock;
import net.nostalgia.client.ritual.ClientZoneTime;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientClockManager.class)
public abstract class ClientClockManagerFreezeMixin {

    @Inject(method = "getTotalTicks", at = @At("RETURN"), cancellable = true)
    private void nostalgia$freezeTime(Holder<WorldClock> definition, CallbackInfoReturnable<Long> cir) {
        long real = cir.getReturnValue();
        net.nostalgia.client.ritual.ZoneTimeBridge.lastRealClockTicks = real;
        net.nostalgia.client.ritual.ZoneTimeBridge.hasClockReal = true;
        
        net.nostalgia.alphalogic.ritual.event.ClientTransitionView t = net.nostalgia.client.ritual.ClientRitualEventRegistry.activeTransition();
        if (t != null && !t.isBystander()) {
            long newTime = net.nostalgia.client.ritual.RitualVisualManager.calculateInertialTime(real);
            cir.setReturnValue(newTime);
            return;
        }
        
        if (!net.nostalgia.client.ritual.ClientFreezeRegions.hasRegions() && !ClientZoneTime.isActive()) return;
        long effective = ClientZoneTime.getEffectiveClockTicks(real);
        if (effective != real) {
            cir.setReturnValue(effective);
        }
    }
}
