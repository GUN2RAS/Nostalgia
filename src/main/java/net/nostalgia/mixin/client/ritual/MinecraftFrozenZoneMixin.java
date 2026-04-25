package net.nostalgia.mixin.client.ritual;

import net.minecraft.client.Minecraft;
import net.nostalgia.client.ritual.ClientFreezeRegions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public abstract class MinecraftFrozenZoneMixin {

    @Inject(method = "isLevelRunningNormally", at = @At("HEAD"), cancellable = true)
    private void nostalgia$freezeInZone(CallbackInfoReturnable<Boolean> cir) {
        if (net.nostalgia.client.ritual.ClientZoneTime.isTransitioning()) return;
        if (!ClientFreezeRegions.isLocalPlayerInZone()) return;
        cir.setReturnValue(false);
    }
}
