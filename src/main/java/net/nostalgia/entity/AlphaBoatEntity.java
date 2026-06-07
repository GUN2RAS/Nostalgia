package net.nostalgia.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.boat.Boat;
import net.minecraft.world.level.Level;
import net.nostalgia.item.AlphaItems;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.MoverType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.entity.Entity;

public class AlphaBoatEntity extends Boat {
    private int alphaLerpSteps;
    private double alphaLerpX;
    private double alphaLerpY;
    private double alphaLerpZ;
    private double alphaLerpYRot;
    private double alphaLerpXRot;

    public AlphaBoatEntity(EntityType<? extends Boat> type, Level level) {
        super(type, level, () -> AlphaItems.ALPHA_BOAT);
    }

    @Override
    public void tick() {
        this.baseTick();

        if (this.getHurtTime() > 0) {
            this.setHurtTime(this.getHurtTime() - 1);
        }

        if (this.getDamage() > 0.0F) {
            this.setDamage(this.getDamage() - 1.0F);
        }

        this.xo = this.getX();
        this.yo = this.getY();
        this.zo = this.getZ();

        if (this.isLocalInstanceAuthoritative()) {
            int sliceCount = 5;
            double subMergedRatio = 0.0D;
            AABB box = this.getBoundingBox();

            for (int i = 0; i < sliceCount; ++i) {
                double d0 = box.minY + (box.maxY - box.minY) * (double)(i + 0) / (double)sliceCount - 0.125D;
                double d1 = box.minY + (box.maxY - box.minY) * (double)(i + 1) / (double)sliceCount - 0.125D;
                AABB sliceBox = new AABB(box.minX, d0, box.minZ, box.maxX, d1, box.maxZ);
                
                boolean inWater = false;
                int x0 = Mth.floor(sliceBox.minX);
                int x1 = Mth.ceil(sliceBox.maxX);
                int y0 = Mth.floor(sliceBox.minY);
                int y1 = Mth.ceil(sliceBox.maxY);
                int z0 = Mth.floor(sliceBox.minZ);
                int z1 = Mth.ceil(sliceBox.maxZ);
                net.minecraft.core.BlockPos.MutableBlockPos pos = new net.minecraft.core.BlockPos.MutableBlockPos();
                
                for (int x = x0; x < x1 && !inWater; x++) {
                    for (int y = y0; y < y1 && !inWater; y++) {
                        for (int z = z0; z < z1 && !inWater; z++) {
                            pos.set(x, y, z);
                            net.minecraft.world.level.material.FluidState fs = this.level().getFluidState(pos);
                            if (fs.is(net.minecraft.tags.FluidTags.WATER)) {
                                float fluidY = (float)y + fs.getHeight(this.level(), pos);
                                if (sliceBox.minY < (double)fluidY) {
                                    inWater = true;
                                }
                            }
                        }
                    }
                }
                if (inWater) {
                    subMergedRatio += 1.0D / (double)sliceCount;
                }
            }

            Vec3 velocity = this.getDeltaMovement();
            double vx = velocity.x;
            double vy = velocity.y;
            double vz = velocity.z;

            double d8 = subMergedRatio * 2.0D - 1.0D;
            vy += 0.04D * d8;

            Entity passenger = this.getFirstPassenger();
            
            if (passenger != null) {
                vx += passenger.getDeltaMovement().x * 0.2D;
                vz += passenger.getDeltaMovement().z * 0.2D;
            }

            double maxVelocity = 0.25D;
            vx = Mth.clamp(vx, -maxVelocity, maxVelocity);
            vz = Mth.clamp(vz, -maxVelocity, maxVelocity);

            if (this.onGround()) {
                vx *= 0.5D;
                vy *= 0.5D;
                vz *= 0.5D;
            }

            double preCrashSpeedSqr = vx * vx + vz * vz;
            double preCrashDist = Math.sqrt(preCrashSpeedSqr);

            this.setXRot(0.0F);
            double targetYaw = (double)this.getYRot();
            if (preCrashSpeedSqr > 0.001D) {
                targetYaw = (double)(Mth.atan2(vz, vx) * (double)(180F / (float)Math.PI)) - 90.0D;
            }

            double yawDiff = Mth.wrapDegrees(targetYaw - (double)this.getYRot());
            if (yawDiff > 20.0D) {
                yawDiff = 20.0D;
            }
            if (yawDiff < -20.0D) {
                yawDiff = -20.0D;
            }
            
            this.setYRot((float)((double)this.getYRot() + yawDiff));

            boolean isLocalAuthoritative = this.isLocalInstanceAuthoritative();
            if (isLocalAuthoritative) {
                this.setDeltaMovement(new Vec3(vx, vy, vz));
                this.move(MoverType.SELF, this.getDeltaMovement());
            } else {
                this.setDeltaMovement(Vec3.ZERO);
            }
            
            velocity = this.getDeltaMovement();
            vx = velocity.x; vy = velocity.y; vz = velocity.z;

            double horizontalSpeedSqr = vx * vx + vz * vz;
            double horizontalDist = Math.sqrt(horizontalSpeedSqr);

            if (isLocalAuthoritative && preCrashDist > 0.15D) {
                double d10 = Math.cos((double)this.getYRot() * Math.PI / 180.0D);
                double d3 = Math.sin((double)this.getYRot() * Math.PI / 180.0D);
                int n3 = 0;
                while ((double)n3 < 1.0D + preCrashDist * 60.0D) {
                    double d13 = (double)(this.random.nextFloat() * 2.0F - 1.0F);
                    double d14 = (double)(this.random.nextInt(2) * 2 - 1) * 0.7D;
                    if (this.random.nextBoolean()) {
                        double d12 = this.getX() - d10 * d13 * 0.8D + d3 * d14;
                        double d11 = this.getZ() - d3 * d13 * 0.8D - d10 * d14;
                        if (this.level() instanceof ServerLevel serverLevel) {
                            serverLevel.sendParticles(ParticleTypes.SPLASH, d12, this.getY() - 0.125D, d11, 1, vx, vy, vz, 0);
                        } else {
                            this.level().addParticle(ParticleTypes.SPLASH, d12, this.getY() - 0.125D, d11, vx, vy, vz);
                        }
                    } else {
                        double d12 = this.getX() + d10 + d3 * d13 * 0.7D;
                        double d11 = this.getZ() + d3 - d10 * d13 * 0.7D;
                        if (this.level() instanceof ServerLevel serverLevel) {
                            serverLevel.sendParticles(ParticleTypes.SPLASH, d12, this.getY() - 0.125D, d11, 1, vx, vy, vz, 0);
                        } else {
                            this.level().addParticle(ParticleTypes.SPLASH, d12, this.getY() - 0.125D, d11, vx, vy, vz);
                        }
                    }
                    ++n3;
                }
            }

            if (this.horizontalCollision && preCrashDist > 0.15D) {
                if (!this.level().isClientSide() && this.level() instanceof ServerLevel serverLevel) {
                    for (int n4 = 0; n4 < 3; ++n4) {
                        net.minecraft.world.entity.item.ItemEntity plank = new net.minecraft.world.entity.item.ItemEntity(serverLevel, this.getX(), this.getY(), this.getZ(), new ItemStack(Items.OAK_PLANKS));
                        plank.setDefaultPickUpDelay();
                        serverLevel.addFreshEntity(plank);
                    }
                    for (int n4 = 0; n4 < 2; ++n4) {
                        net.minecraft.world.entity.item.ItemEntity stick = new net.minecraft.world.entity.item.ItemEntity(serverLevel, this.getX(), this.getY(), this.getZ(), new ItemStack(Items.STICK));
                        stick.setDefaultPickUpDelay();
                        serverLevel.addFreshEntity(stick);
                    }
                    this.discard();
                } else if (this.level().isClientSide()) {
                    if (preCrashSpeedSqr > 0.01D) {
                        net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.send(new net.nostalgia.network.C2SBoatCrashPayload(this.getId(), this.getX(), this.getY(), this.getZ()));
                        this.discard();
                    }
                }
            } else {
                vx *= 0.9900000095367432D;
                vy *= 0.949999988079071D;
                vz *= 0.9900000095367432D;
                this.setDeltaMovement(new Vec3(vx, vy, vz));
                
                if (this.level().isClientSide() && this.alphaLerpSteps > 0) {
                    double d0 = this.getX() + (this.alphaLerpX - this.getX()) / (double)this.alphaLerpSteps;
                    double d1 = this.getY() + (this.alphaLerpY - this.getY()) / (double)this.alphaLerpSteps;
                    double d2 = this.getZ() + (this.alphaLerpZ - this.getZ()) / (double)this.alphaLerpSteps;
                    double d3 = net.minecraft.util.Mth.wrapDegrees(this.alphaLerpYRot - (double)this.getYRot());
                    this.setYRot(this.getYRot() + (float)(d3 / (double)this.alphaLerpSteps));
                    this.setXRot(this.getXRot() + (float)((this.alphaLerpXRot - (double)this.getXRot()) / (double)this.alphaLerpSteps));
                    --this.alphaLerpSteps;
                    this.setPos(d0, d1, d2);
                    this.setRot(this.getYRot(), this.getXRot());
                }
            }
        } else {
            this.setDeltaMovement(Vec3.ZERO);
        }

        java.util.List<Entity> list = this.level().getEntities(this, this.getBoundingBox().inflate(0.2D, -0.01D, 0.2D), net.minecraft.world.entity.EntitySelector.pushableBy(this));
        if (!list.isEmpty()) {
            for (Entity e : list) {
                if (!e.hasPassenger(this) && e instanceof AlphaBoatEntity) {
                    e.push(this);
                }
            }
        }

        Entity currentPassenger = this.getFirstPassenger();
        if (currentPassenger != null && currentPassenger.isRemoved()) {
            currentPassenger.stopRiding();
        }
    }

    @Override
    protected void positionRider(Entity passenger, Entity.MoveFunction moveFunction) {
        Vec3 position = this.getPassengerRidingPosition(passenger);
        Vec3 offset = passenger.getVehicleAttachmentPoint(this);
        moveFunction.accept(passenger, position.x - offset.x, position.y - offset.y, position.z - offset.z);
    }

    @Override
    protected Vec3 getPassengerAttachmentPoint(Entity passenger, net.minecraft.world.entity.EntityDimensions dimensions, float scale) {
        return new Vec3(0.0D, dimensions.height() / 3.0F - 0.25D, 0.0D);
    }

    @Override
    public boolean hurtServer(ServerLevel level, net.minecraft.world.damagesource.DamageSource source, float amount) {
        if (this.isInvulnerableToBase(source)) {
            return false;
        } else if (!this.level().isClientSide() && !this.isRemoved()) {
            this.setHurtDir(-this.getHurtDir());
            this.setHurtTime(10);
            this.setDamage(this.getDamage() + amount * 10.0F);
            this.markHurt();
            if (source.getEntity() instanceof net.minecraft.world.entity.player.Player && ((net.minecraft.world.entity.player.Player)source.getEntity()).getAbilities().instabuild) {
                this.discard();
                return true;
            } else if (this.getDamage() > 40.0F) {
                this.discard();
                for (int n4 = 0; n4 < 3; ++n4) {
                    net.minecraft.world.entity.item.ItemEntity plank = new net.minecraft.world.entity.item.ItemEntity(level, this.getX(), this.getY(), this.getZ(), new ItemStack(Items.OAK_PLANKS));
                    plank.setDefaultPickUpDelay();
                    level.addFreshEntity(plank);
                }
                for (int n4 = 0; n4 < 2; ++n4) {
                    net.minecraft.world.entity.item.ItemEntity stick = new net.minecraft.world.entity.item.ItemEntity(level, this.getX(), this.getY(), this.getZ(), new ItemStack(Items.STICK));
                    stick.setDefaultPickUpDelay();
                    level.addFreshEntity(stick);
                }
            }
            return true;
        } else {
            return true;
        }
    }
    
    @Override
    protected void lerpPositionAndRotationStep(int steps, double x, double y, double z, double yRot, double xRot) {
        this.alphaLerpX = x;
        this.alphaLerpY = y;
        this.alphaLerpZ = z;
        this.alphaLerpYRot = yRot;
        this.alphaLerpXRot = xRot;
        this.alphaLerpSteps = 10;
        super.lerpPositionAndRotationStep(steps, x, y, z, yRot, xRot);
    }
}
