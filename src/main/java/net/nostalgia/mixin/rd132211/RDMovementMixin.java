package net.nostalgia.mixin.rd132211;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.nostalgia.world.dimension.ModDimensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import org.spongepowered.asm.mixin.Unique;

@Mixin(LivingEntity.class)
public abstract class RDMovementMixin {

    @Unique
    private boolean nostalgia$isTimeStopZone(LivingEntity entity) {
        if (entity.level().tickRateManager() instanceof net.nostalgia.alphalogic.ritual.TickRateManagerAccess access) {
            return access.nostalgia$isChunkFrozen(entity.level().dimension(), entity.chunkPosition());
        }
        return false;
    }

    @Unique
    private boolean nostalgia$isFloatyZone(LivingEntity entity) {
        if (entity.level().dimension().equals(ModDimensions.RD_132211_LEVEL_KEY)) return true;
        return nostalgia$isTimeStopZone(entity);
    }

    @Redirect(method = "checkFallDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;sendParticles(Lnet/minecraft/core/particles/ParticleOptions;DDDIDDDD)I"))
    private int cancelFallParticles(ServerLevel instance, ParticleOptions options, double x, double y, double z,
            int count, double dx, double dy, double dz, double speed) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (nostalgia$isFloatyZone(entity)) {
            return 0;
        }
        return instance.sendParticles(options, x, y, z, count, dx, dy, dz, speed);
    }

    @Inject(method = "causeFallDamage", at = @At("HEAD"), cancellable = true)
    private void onCauseFallDamage(double fallDistance, float multiplier, DamageSource source,
            CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (nostalgia$isFloatyZone(entity)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "playBlockFallSound", at = @At("HEAD"), cancellable = true)
    private void onPlayBlockFallSound(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (nostalgia$isFloatyZone(entity)) {
            ci.cancel();
        }
    }

    @Inject(method = "travel", at = @At("HEAD"), cancellable = true)
    private void onTravel(Vec3 movementInput, CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (entity instanceof Player && nostalgia$isFloatyZone(entity)) {
            if (entity.isInWater() || entity.isInLava() || entity.isFallFlying()) {
                return;
            }

            Vec3 delta = entity.getDeltaMovement();
            double xd = delta.x;
            double yd = delta.y;
            double zd = delta.z;

            boolean inTimeStop = nostalgia$isTimeStopZone(entity);
            float baseSpeed = inTimeStop ? 0.10F : 0.08F;
            float airSpeed = inTimeStop ? 0.03F : 0.02F;
            float speed = entity.onGround() ? baseSpeed : airSpeed;
            float xa = (float) movementInput.x;
            float za = (float) movementInput.z;

            float dist = xa * xa + za * za;
            if (dist >= 0.01F) {
                dist = speed / (float) Math.sqrt(dist);
                xa *= dist;
                za *= dist;
                float sin = (float) Math.sin(entity.getYRot() * Math.PI / 180.0);
                float cos = (float) Math.cos(entity.getYRot() * Math.PI / 180.0);
                xd += (xa * cos - za * sin);
                zd += (za * cos + xa * sin);
            }

            yd -= 0.02;

            entity.move(MoverType.SELF, new Vec3(xd, yd, zd));

            xd *= 0.91F;
            yd *= 0.98F;
            zd *= 0.91F;
            if (entity.onGround()) {
                xd *= 0.6F; 
                zd *= 0.6F;
            }

            entity.setDeltaMovement(xd, yd, zd);

            ci.cancel();
        }
    }

    @Inject(method = "jumpFromGround", at = @At("HEAD"), cancellable = true)
    private void onJump(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity instanceof Player && nostalgia$isFloatyZone(entity)) {
            if (entity.isInWater() || entity.isInLava()) {
                return;
            }
            boolean inTimeStop = nostalgia$isTimeStopZone(entity);
            float jumpHeight = inTimeStop ? 0.40F : 0.25F;
            entity.setDeltaMovement(entity.getDeltaMovement().x, jumpHeight, entity.getDeltaMovement().z);
            ci.cancel();
        }
    }
}
