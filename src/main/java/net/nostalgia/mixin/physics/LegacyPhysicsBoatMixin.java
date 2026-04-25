package net.nostalgia.mixin.physics;

import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.world.entity.Entity.class)
public abstract class LegacyPhysicsBoatMixin {

    @org.spongepowered.asm.mixin.Unique
    private double nostalgia$savedSpeedBeforeCollision;

    @Inject(method = "move", at = @At("HEAD"))
    private void nostalgia$boatFragility(net.minecraft.world.entity.MoverType type, net.minecraft.world.phys.Vec3 movement, CallbackInfo ci) {
        if ((Object) this instanceof AbstractBoat boat) {
            boolean isFragile = net.nostalgia.world.rules.NostalgiaRules.getForLevel(boat.level()).fragileBoats;
            if (isFragile && !(boat instanceof net.nostalgia.entity.AlphaBoatEntity) && boat.level().isClientSide() && boat.isLocalInstanceAuthoritative()) {
                this.nostalgia$savedSpeedBeforeCollision = movement.x * movement.x + movement.z * movement.z;
            }
        }
    }

    @Inject(method = "move", at = @At("TAIL"))
    private void nostalgia$boatFragilityCheck(net.minecraft.world.entity.MoverType type, net.minecraft.world.phys.Vec3 movement, CallbackInfo ci) {
        if ((Object) this instanceof AbstractBoat boat) {
            boolean isFragile = net.nostalgia.world.rules.NostalgiaRules.getForLevel(boat.level()).fragileBoats;
            if (isFragile && !(boat instanceof net.nostalgia.entity.AlphaBoatEntity) && boat.horizontalCollision && boat.level().isClientSide() && boat.isLocalInstanceAuthoritative()) {
                if (this.nostalgia$savedSpeedBeforeCollision > 0.01D) {
                    net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.send(new net.nostalgia.network.C2SBoatCrashPayload(boat.getId(), boat.getX(), boat.getY(), boat.getZ()));
                    
                    double dist = Math.sqrt(this.nostalgia$savedSpeedBeforeCollision);
                    int n3 = 0;
                    while ((double)n3 < 1.0D + dist * 60.0D) {
                        double vx = (double)(boat.getRandom().nextFloat() * 2.0F - 1.0F) * 0.4D;
                        double vy = (double)(boat.getRandom().nextFloat() * 2.0F - 1.0F) * 0.4D;
                        double vz = (double)(boat.getRandom().nextFloat() * 2.0F - 1.0F) * 0.4D;
                        boat.level().addParticle(net.minecraft.core.particles.ParticleTypes.SPLASH, boat.getX(), boat.getY() - 0.125D, boat.getZ(), vx, vy, vz);
                        ++n3;
                    }
                    boat.discard();
                }
            }
        }
    }
}
