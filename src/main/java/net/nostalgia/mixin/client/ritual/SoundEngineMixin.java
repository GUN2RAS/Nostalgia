package net.nostalgia.mixin.client.ritual;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import net.nostalgia.client.ritual.OpenALReverbHandler;
import net.nostalgia.client.ritual.RitualVisualManager;
import net.nostalgia.alphalogic.ritual.RitualActiveState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.mojang.blaze3d.audio.Channel;
import java.util.Map;
import net.minecraft.client.sounds.ChannelAccess;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SoundEngine.class)
public class SoundEngineMixin {

    @Shadow private Map<SoundInstance, ChannelAccess.ChannelHandle> instanceToChannel;

    @Inject(method = "loadLibrary", at = @At("TAIL"))
    private void nostalgia$onLoadLibrary(CallbackInfo ci) {
        OpenALReverbHandler.initialize();
    }

    @Inject(method = "calculatePitch", at = @At("RETURN"), cancellable = true)
    private void nostalgia$onCalculatePitch(SoundInstance instance, CallbackInfoReturnable<Float> cir) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null && mc.level != null) {
            boolean inZone = net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.findZoneContaining(mc.player.level().dimension(), mc.player.blockPosition()) != null;
            boolean isSuppressed = System.currentTimeMillis() < RitualVisualManager.suppressZoneAudioUntil;
            boolean shouldDistort = inZone && net.nostalgia.client.ritual.ClientRitualEventRegistry.activeTransition() == null && !isSuppressed;
            if (shouldDistort) {
                float originalPitch = cir.getReturnValue();
                
                cir.setReturnValue(originalPitch * 0.65f);
            }
        }
    }

    @Inject(method = "play", at = @At("TAIL"))
    private void nostalgia$onPlay(SoundInstance instance, CallbackInfoReturnable<SoundEngine.PlayResult> cir) {
        if (cir.getReturnValue() != SoundEngine.PlayResult.NOT_STARTED) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null && mc.level != null) {
                boolean inZone = net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.findZoneContaining(mc.player.level().dimension(), mc.player.blockPosition()) != null;
                boolean isSuppressed = System.currentTimeMillis() < RitualVisualManager.suppressZoneAudioUntil;
                boolean shouldDistort = inZone && !RitualVisualManager.isTransitioning && !isSuppressed;
                if (shouldDistort) {
                    ChannelAccess.ChannelHandle handle = this.instanceToChannel.get(instance);
                    if (handle != null) {
                        handle.execute(channel -> {
                            int sourceId = ((ChannelAccessor) channel).getSourceId();
                            OpenALReverbHandler.applyReverb(sourceId);
                        });
                    }
                }
            }
        }
    }

    @Inject(method = "tickInGameSound", at = @At("TAIL"))
    private void nostalgia$onTickSound(CallbackInfo ci) {
        
    }
}
