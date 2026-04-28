package net.nostalgia.mixin.ritual;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.nostalgia.alphalogic.ritual.RitualActiveState;
import net.nostalgia.alphalogic.ritual.RitualManager;
import net.nostalgia.alphalogic.ritual.event.RitualEventRegistry;
import net.nostalgia.alphalogic.ritual.event.TransitionEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityIsolationMixin {

    private boolean nostalgia$isIsolatedFrom(Entity other) {
        TransitionEvent t = RitualEventRegistry.activeTransition();
        if (t == null) return false;
        Entity self = (Entity)(Object)this;

        boolean isolate = false;
        if (!self.level().isClientSide()) {
            if (t.phase() >= 2 && (RitualManager.activeRitualMillis - t.phaseStartTime() >= 1000)) {
                isolate = true;
            }
        } else {
            isolate = nostalgia$isClientIsolated();
        }

        if (isolate) {
            return RitualActiveState.isParticipant(self) != RitualActiveState.isParticipant(other);
        }
        return false;
    }

    private boolean nostalgia$isClientIsolated() {
        if (net.fabricmc.loader.api.FabricLoader.getInstance().getEnvironmentType() == net.fabricmc.api.EnvType.CLIENT) {
            return net.nostalgia.client.ritual.RitualVisualManager.currentPhase >= 2 && 
                   (net.nostalgia.client.ritual.RitualVisualManager.getVisualTime() - net.nostalgia.client.ritual.RitualVisualManager.phase2StartTime >= 1000);
        }
        return false;
    }

    @Inject(method = "push(Lnet/minecraft/world/entity/Entity;)V", at = @At("HEAD"), cancellable = true)
    private void nostalgia$isolationPush(Entity entity, CallbackInfo ci) {
        if (nostalgia$isIsolatedFrom(entity)) {
            ci.cancel();
        }
    }

    @Inject(method = "playSound(Lnet/minecraft/sounds/SoundEvent;FF)V", at = @At("HEAD"), cancellable = true)
    private void nostalgia$isolationPlaySound(SoundEvent sound, float volume, float pitch, CallbackInfo ci) {
        Entity self = (Entity)(Object)this;
        TransitionEvent t = RitualEventRegistry.activeTransition();
        if (t == null) return;

        boolean isolate = false;
        if (!self.level().isClientSide()) {
            if (t.phase() >= 2 && (RitualManager.activeRitualMillis - t.phaseStartTime() >= 1000)) {
                isolate = true;
            }
        } else {
            isolate = nostalgia$isClientIsolated();
        }

        if (isolate) {
            // Cancel default broadcast
            ci.cancel();

            // Manually broadcast on server to players in the same participant group
            if (!self.level().isClientSide() && !self.isSilent()) {
                ServerLevel sLevel = (ServerLevel) self.level();
                boolean selfParticipant = RitualActiveState.isParticipant(self);

                for (ServerPlayer sp : sLevel.players()) {
                    if (RitualActiveState.isParticipant(sp) == selfParticipant) {
                        sp.connection.send(new net.minecraft.network.protocol.game.ClientboundSoundPacket(
                            net.minecraft.core.registries.BuiltInRegistries.SOUND_EVENT.wrapAsHolder(sound),
                            self.getSoundSource(),
                            self.getX(), self.getY(), self.getZ(),
                            volume, pitch, sLevel.getRandom().nextLong()
                        ));
                    }
                }
            }
        }
    }
}
