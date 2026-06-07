package net.nostalgia.mixin.ritual;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.nostalgia.alphalogic.ritual.event.RitualEventRegistry;
import net.nostalgia.alphalogic.ritual.event.TimestopZoneEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class SlowSwingMixin {

    @Inject(method = "getCurrentSwingDuration", at = @At("RETURN"), cancellable = true)
    private void slowDownSwingInTimestop(CallbackInfoReturnable<Integer> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity instanceof Player player && player.level() != null) {
            TimestopZoneEvent zone = RitualEventRegistry.findZoneContaining(player.level().dimension(), player.blockPosition());
            if (zone != null) {
                
                cir.setReturnValue(cir.getReturnValueI() * 3);
            }
        }
    }
}
