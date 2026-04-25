package net.nostalgia.mixin.client.ritual;

import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;
import net.nostalgia.client.ritual.ClientZoneTime;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Level.class)
public abstract class ClientLevelWeatherFreezeMixin {

    @Inject(method = "getRainLevel", at = @At("RETURN"), cancellable = true)
    private void nostalgia$freezeRain(float partial, CallbackInfoReturnable<Float> cir) {
        Level self = (Level) (Object) this;
        if (!self.isClientSide()) return;
        if (Minecraft.getInstance().level != self) return;
        if (!net.nostalgia.client.ritual.ClientFreezeRegions.hasRegions() && !ClientZoneTime.isActive()) return;
        float real = cir.getReturnValue();
        float effective = ClientZoneTime.getEffectiveRain(real);
        if (effective != real) {
            cir.setReturnValue(effective);
        }
    }

    @Inject(method = "getThunderLevel", at = @At("RETURN"), cancellable = true)
    private void nostalgia$freezeThunder(float partial, CallbackInfoReturnable<Float> cir) {
        Level self = (Level) (Object) this;
        if (!self.isClientSide()) return;
        if (Minecraft.getInstance().level != self) return;
        if (!net.nostalgia.client.ritual.ClientFreezeRegions.hasRegions() && !ClientZoneTime.isActive()) return;
        float real = cir.getReturnValue();
        float effective = ClientZoneTime.getEffectiveThunder(real);
        if (effective != real) {
            cir.setReturnValue(effective);
        }
    }
}
