package net.nostalgia.mixin.physics;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LegacyPhysicsKnockbackMixin {

    @Inject(method = "knockback", at = @At("HEAD"), cancellable = true)
    private void nostalgia$legacyKnockback(double power, double xd, double zd, CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (net.nostalgia.world.rules.NostalgiaRules.getForLevel(self.level()).legacyKnockback) {
            power *= 1.0 - self.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
            if (power > 0.0) {
                self.needsSync = true;
                Vec3 delta = self.getDeltaMovement();
                while (xd * xd + zd * zd < 9.999999747378752E-6) {
                    xd = (self.getRandom().nextDouble() - self.getRandom().nextDouble()) * 0.01;
                    zd = (self.getRandom().nextDouble() - self.getRandom().nextDouble()) * 0.01;
                }
                Vec3 norm = new Vec3(xd, 0.0, zd).normalize().scale(power);
                double newX = delta.x / 2.0 - norm.x;
                double newY = delta.y / 2.0 + 0.4;
                if (newY > 0.4) {
                    newY = 0.4;
                }
                double newZ = delta.z / 2.0 - norm.z;
                self.setDeltaMovement(newX, newY, newZ);
            }
            ci.cancel();
        }
    }
}
