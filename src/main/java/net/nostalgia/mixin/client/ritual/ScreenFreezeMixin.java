package net.nostalgia.mixin.client.ritual;

import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundRespawnPacket;
import net.nostalgia.client.ritual.ScreenFreezer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class ScreenFreezeMixin {
    @Inject(method = "handleRespawn", at = @At("HEAD"))
    private void nostalgia$onRespawn(ClientboundRespawnPacket packet, CallbackInfo ci) {
        if (!com.mojang.blaze3d.systems.RenderSystem.isOnRenderThread()) {
            return;
        }
        
        net.nostalgia.alphalogic.ritual.event.ClientTransitionView t = net.nostalgia.client.ritual.ClientRitualEventRegistry.activeTransition();
        if (t != null && !t.isBystander()) {
            ScreenFreezer.takeSnapshot();
        }
    }
}
