package net.nostalgia.mixin;

import net.minecraft.world.entity.Entity;
import net.nostalgia.alphalogic.ritual.SkyPortalManager;
import net.nostalgia.alphalogic.ritual.SkyPortalEventInstance;
import com.example.api.GravityChanger;
import com.example.api.Gravity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityGravityAnomalyMixin {

    @org.spongepowered.asm.mixin.Unique
    private boolean portalControlledGravity = false;

    @Inject(method = "baseTick", at = @At("HEAD"))
    private void applyIslandGravity(CallbackInfo ci) {
        Entity entity = (Entity) (Object) this;
        if (entity.level().isClientSide()) return;

        SkyPortalEventInstance portal = SkyPortalManager.getActive();
        if (portal == null) {
            if (this.portalControlledGravity) {
                this.portalControlledGravity = false;
            }
            return;
        }

        String currentDim = entity.level().dimension().identifier().toString();
        if (!currentDim.equals(portal.sourceDimension()) && !currentDim.equals(portal.targetDimension())) {
            return;
        }

        if (!(entity instanceof GravityChanger gc)) return;

        if (gc.isInfected()) {
            gc.setGravityAnomalyStrength(0.0f);
            return;
        }

        Entity vehicle = entity.getVehicle();
        if (vehicle instanceof GravityChanger vehicleGc) {
            float vehicleAnomaly = vehicleGc.getGravityAnomalyStrength();
            gc.setGravityAnomalyStrength(vehicleAnomaly);

            if (vehicleAnomaly >= 1.0f && gc.getGravityDirection() != Gravity.UP) {
                gc.setGravityInstant(Gravity.UP);
                gc.setGravityAnomalyStrength(0.0f);
                this.portalControlledGravity = true;
            } else if (vehicleAnomaly <= 0.0f && gc.getGravityDirection() == Gravity.UP && this.portalControlledGravity) {
                gc.setGravityInstant(Gravity.DOWN);
                gc.setGravityAnomalyStrength(0.0f);
                this.portalControlledGravity = false;
            }
            return;
        }

        double dx = entity.getX() - portal.center().getX();
        double dz = entity.getZ() - portal.center().getZ();
        boolean inCylinder = dx * dx + dz * dz <= 300 * 300;

        if (!inCylinder) {
            gc.setGravityAnomalyStrength(0.0f);
            return;
        }

        double transitionStart = portal.crackPlaneY() + 10.0;
        double transitionEnd = portal.crackPlaneY() + 15.0;
        double entityY = entity.getY();

        if (gc.getGravityDirection() == Gravity.DOWN) {
            if (entityY < transitionStart) {
                gc.setGravityAnomalyStrength(0.0f);
            } else if (entityY >= transitionEnd) {
                gc.setGravityInstant(Gravity.UP);
                gc.setGravityAnomalyStrength(0.0f);
                this.portalControlledGravity = true;
            } else {
                float t = (float) ((entityY - transitionStart) / (transitionEnd - transitionStart));
                gc.setGravityAnomalyStrength(t);
            }
        } else if (gc.getGravityDirection() == Gravity.UP && this.portalControlledGravity) {
            if (entityY >= transitionEnd) {
                gc.setGravityAnomalyStrength(0.0f);
            } else if (entityY < transitionStart) {
                gc.setGravityInstant(Gravity.DOWN);
                gc.setGravityAnomalyStrength(0.0f);
                this.portalControlledGravity = false;
            } else {
                float t = (float) ((transitionEnd - entityY) / (transitionEnd - transitionStart));
                gc.setGravityAnomalyStrength(t);
            }
        }
    }
}
