package net.nostalgia.mixin.ritual;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.nostalgia.alphalogic.ritual.TransitionEventInstance;
import net.nostalgia.alphalogic.ritual.event.RitualEventRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityIsolationMixin {

    private boolean nostalgia$isIsolatedFrom(Entity other) {
        Entity self = (Entity)(Object)this;

        if (self.level().isClientSide()) {
            net.nostalgia.alphalogic.ritual.event.ClientTransitionView ct = net.nostalgia.client.ritual.ClientRitualEventRegistry.activeTransition();
            if (ct == null) return false;
            if (ct.currentPhase() < 2) return false;
            if (ct.visualTime() - ct.phase2StartTime() < 1000) return false;
            boolean selfP = RitualEventRegistry.isParticipantAny(self);
            boolean otherP = RitualEventRegistry.isParticipantAny(other);
            if (!selfP && !otherP) return false;
            return selfP != otherP;
        }

        TransitionEventInstance selfInst = RitualEventRegistry.findInstanceForParticipant(self.getUUID());
        TransitionEventInstance otherInst = RitualEventRegistry.findInstanceForParticipant(other.getUUID());
        if (selfInst == null && otherInst == null) return false;

        boolean selfActive = selfInst != null && selfInst.phase() >= 2 && (selfInst.activeMs() - selfInst.phaseStartTime() >= 1000);
        boolean otherActive = otherInst != null && otherInst.phase() >= 2 && (otherInst.activeMs() - otherInst.phaseStartTime() >= 1000);
        if (!selfActive && !otherActive) return false;

        if (selfInst != null && selfInst == otherInst) return false;
        return true;
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
        if (self.level().isClientSide()) return;

        TransitionEventInstance selfInst = RitualEventRegistry.findInstanceForParticipant(self.getUUID());
        boolean phaseGate = false;
        if (selfInst != null && selfInst.phase() >= 2 && (selfInst.activeMs() - selfInst.phaseStartTime() >= 1000)) {
            phaseGate = true;
        } else {
            for (TransitionEventInstance i : RitualEventRegistry.allInstances()) {
                if (i.phase() >= 2 && (i.activeMs() - i.phaseStartTime() >= 1000)) {
                    phaseGate = true;
                    break;
                }
            }
        }
        if (!phaseGate) return;

        ci.cancel();

        if (self.isSilent()) return;
        ServerLevel sLevel = (ServerLevel) self.level();
        for (ServerPlayer sp : sLevel.players()) {
            TransitionEventInstance spInst = RitualEventRegistry.findInstanceForParticipant(sp.getUUID());
            boolean sameGroup;
            if (selfInst == null && spInst == null) {
                sameGroup = true;
            } else if (selfInst != null && selfInst == spInst) {
                sameGroup = true;
            } else {
                sameGroup = false;
            }
            if (sameGroup) {
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
