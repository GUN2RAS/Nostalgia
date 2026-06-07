package net.nostalgia.mixin;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.nostalgia.world.dimension.ModDimensions;
import net.nostalgia.world.gen.AlphaSounds;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class AlphaSoundMixin {

    @Inject(method = "playSound(Lnet/minecraft/sounds/SoundEvent;FF)V", at = @At("HEAD"), cancellable = true)
    private void nostalgia$interceptAlphaSounds(SoundEvent sound, float volume, float pitch, CallbackInfo ci) {
        Player player = (Player) (Object) this;
        if (player.level().dimension().equals(ModDimensions.ALPHA_112_01_LEVEL_KEY)) {
            
            if (sound.equals(SoundEvents.EXPERIENCE_ORB_PICKUP) ||
                    sound.equals(SoundEvents.PLAYER_LEVELUP) ||
                    sound.equals(SoundEvents.EXPERIENCE_BOTTLE_THROW)) {
                ci.cancel();
                return;
            }

            SoundEvent redirect = null;
            if (sound.equals(SoundEvents.PLAYER_HURT) || sound.equals(SoundEvents.PLAYER_HURT_DROWN)
                    || sound.equals(SoundEvents.PLAYER_HURT_ON_FIRE)) {
                redirect = AlphaSounds.RANDOM_HURT.value();
            } else if (sound.equals(SoundEvents.ARROW_SHOOT)) {
                redirect = AlphaSounds.RANDOM_BOW.value();
            } else if (sound.equals(SoundEvents.GENERIC_EXPLODE)) {
                redirect = AlphaSounds.RANDOM_EXPLODE.value();
            } else if (sound.equals(SoundEvents.PLAYER_SPLASH) || sound.equals(SoundEvents.PLAYER_SPLASH_HIGH_SPEED)) {
                redirect = AlphaSounds.RANDOM_SPLASH.value();
            }

            if (redirect != null) {
                player.level().playSound(null, player.getX(), player.getY(), player.getZ(), redirect,
                        player.getSoundSource(), volume, pitch);
                ci.cancel();
            }
        }
    }

    @Inject(method = "getHurtSound", at = @At("HEAD"), cancellable = true)
    private void nostalgia$applyAlphaHurtSound(DamageSource source, CallbackInfoReturnable<SoundEvent> cir) {
        Player player = (Player) (Object) this;
        if (player.level().dimension().equals(ModDimensions.ALPHA_112_01_LEVEL_KEY)) {
            cir.setReturnValue(AlphaSounds.RANDOM_HURT.value());
        }
    }
}
