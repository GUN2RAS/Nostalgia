package net.nostalgia.mixin.rd132211;

import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.nostalgia.world.dimension.ModDimensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.client.multiplayer.ClientLevel.class)
public abstract class ClientLevelSoundMixin {

    @Inject(method = "playSeededSound(Lnet/minecraft/world/entity/Entity;DDDLnet/minecraft/core/Holder;Lnet/minecraft/sounds/SoundSource;FFJ)V", at = @At("HEAD"), cancellable = true)
    private void onPlaySeededSound(Entity entity, double x, double y, double z, Holder<SoundEvent> sound,
            SoundSource source, float volume, float pitch, long seed, CallbackInfo ci) {
        Level level = (Level) (Object) this;
        if (level.dimension().equals(ModDimensions.RD_132211_LEVEL_KEY)) {
            if (source == SoundSource.BLOCKS) {
                ci.cancel();
            }
        }
    }

    @Inject(method = "playSeededSound(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/core/Holder;Lnet/minecraft/sounds/SoundSource;FFJ)V", at = @At("HEAD"), cancellable = true)
    private void onPlaySeededSoundEntity(Entity entity, Entity entity2, Holder<SoundEvent> sound, SoundSource source,
            float volume, float pitch, long seed, CallbackInfo ci) {
        Level level = (Level) (Object) this;
        if (level.dimension().equals(ModDimensions.RD_132211_LEVEL_KEY)) {
            if (source == SoundSource.BLOCKS) {
                ci.cancel();
            }
        }
    }
}
