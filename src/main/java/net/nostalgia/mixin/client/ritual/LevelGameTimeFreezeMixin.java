package net.nostalgia.mixin.client.ritual;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.nostalgia.client.ritual.ClientZoneTime;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LevelRenderer.class)
public abstract class LevelGameTimeFreezeMixin {

    @Redirect(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;getGameTime()J"))
    private long nostalgia$freezeRenderGameTime(ClientLevel level) {
        long real = level.getGameTime();
        if (Minecraft.getInstance().level != level) return real;
        if (!net.nostalgia.client.ritual.ClientFreezeRegions.hasRegions() && !ClientZoneTime.isActive()) return real;
        return ClientZoneTime.getEffectiveGameTime(real);
    }
}
