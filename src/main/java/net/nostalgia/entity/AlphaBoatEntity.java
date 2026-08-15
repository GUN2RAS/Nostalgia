package net.nostalgia.entity;

import java.util.List;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Entity.MoveFunction;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.boat.Boat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.nostalgia.item.AlphaItems;
import net.nostalgia.network.C2SBoatCrashPayload;

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
      double subMergedRatio = 0.0;
      AABB box = this.getBoundingBox();

      for (int i = 0; i < sliceCount; i++) {
        double d0 = box.minY + (box.maxY - box.minY) * (i + 0) / sliceCount - 0.125;
        double d1 = box.minY + (box.maxY - box.minY) * (i + 1) / sliceCount - 0.125;
        AABB sliceBox = new AABB(box.minX, d0, box.minZ, box.maxX, d1, box.maxZ);
        boolean inWater = false;
        int x0 = Mth.floor(sliceBox.minX);
        int x1 = Mth.ceil(sliceBox.maxX);
        int y0 = Mth.floor(sliceBox.minY);
        int y1 = Mth.ceil(sliceBox.maxY);
        int z0 = Mth.floor(sliceBox.minZ);
        int z1 = Mth.ceil(sliceBox.maxZ);
        MutableBlockPos pos = new MutableBlockPos();

        for (int x = x0; x < x1 && !inWater; x++) {
          for (int y = y0; y < y1 && !inWater; y++) {
            for (int z = z0; z < z1 && !inWater; z++) {
              pos.set(x, y, z);
              FluidState fs = this.level().getFluidState(pos);
              if (fs.is(FluidTags.WATER)) {
                float fluidY = y + fs.getHeight(this.level(), pos);
                if (sliceBox.minY < fluidY) {
                  inWater = true;
                }
              }
            }
          }
        }

        if (inWater) {
          subMergedRatio += 1.0 / sliceCount;
        }
      }

      Vec3 velocity = this.getDeltaMovement();
      double vx = velocity.x;
      double vy = velocity.y;
      double vz = velocity.z;
      double d8 = subMergedRatio * 2.0 - 1.0;
      vy += 0.04 * d8;
      Entity passenger = this.getFirstPassenger();
      if (passenger != null) {
        vx += passenger.getDeltaMovement().x * 0.2;
        vz += passenger.getDeltaMovement().z * 0.2;
      }

      double maxVelocity = 0.25;
      vx = Mth.clamp(vx, -maxVelocity, maxVelocity);
      vz = Mth.clamp(vz, -maxVelocity, maxVelocity);
      if (this.onGround()) {
        vx *= 0.5;
        vy *= 0.5;
        vz *= 0.5;
      }

      double preCrashSpeedSqr = vx * vx + vz * vz;
      double preCrashDist = Math.sqrt(preCrashSpeedSqr);
      this.setXRot(0.0F);
      double targetYaw = this.getYRot();
      if (preCrashSpeedSqr > 0.001) {
        targetYaw = Mth.atan2(vz, vx) * 57.2957763671875 - 90.0;
      }

      double yawDiff = Mth.wrapDegrees(targetYaw - this.getYRot());
      if (yawDiff > 20.0) {
        yawDiff = 20.0;
      }

      if (yawDiff < -20.0) {
        yawDiff = -20.0;
      }

      this.setYRot((float)(this.getYRot() + yawDiff));
      boolean isLocalAuthoritative = this.isLocalInstanceAuthoritative();
      if (isLocalAuthoritative) {
        this.setDeltaMovement(new Vec3(vx, vy, vz));
        this.move(MoverType.SELF, this.getDeltaMovement());
      } else {
        this.setDeltaMovement(Vec3.ZERO);
      }

      velocity = this.getDeltaMovement();
      vx = velocity.x;
      vy = velocity.y;
      vz = velocity.z;
      double horizontalSpeedSqr = vx * vx + vz * vz;
      double horizontalDist = Math.sqrt(horizontalSpeedSqr);
      if (isLocalAuthoritative && preCrashDist > 0.15) {
        double d10 = Math.cos(this.getYRot() * 3.141592653589793 / 180.0);
        double d3 = Math.sin(this.getYRot() * 3.141592653589793 / 180.0);

        for (int n3 = 0; n3 < 1.0 + preCrashDist * 60.0; n3++) {
          double d13 = this.random.nextFloat() * 2.0F - 1.0F;
          double d14 = (this.random.nextInt(2) * 2 - 1) * 0.7;
          if (this.random.nextBoolean()) {
            double d12 = this.getX() - d10 * d13 * 0.8 + d3 * d14;
            double d11 = this.getZ() - d3 * d13 * 0.8 - d10 * d14;
            if (this.level() instanceof ServerLevel serverLevel) {
              serverLevel.sendParticles(ParticleTypes.SPLASH, d12, this.getY() - 0.125, d11, 1, vx, vy, vz, 0.0);
            } else {
              this.level().addParticle(ParticleTypes.SPLASH, d12, this.getY() - 0.125, d11, vx, vy, vz);
            }
          } else {
            double d12 = this.getX() + d10 + d3 * d13 * 0.7;
            double d11 = this.getZ() + d3 - d10 * d13 * 0.7;
            if (this.level() instanceof ServerLevel serverLevel) {
              serverLevel.sendParticles(ParticleTypes.SPLASH, d12, this.getY() - 0.125, d11, 1, vx, vy, vz, 0.0);
            } else {
              this.level().addParticle(ParticleTypes.SPLASH, d12, this.getY() - 0.125, d11, vx, vy, vz);
            }
          }
        }
      }

      if (this.horizontalCollision && preCrashDist > 0.15) {
        if (!this.level().isClientSide() && this.level() instanceof ServerLevel serverLevel) {
          for (int n4 = 0; n4 < 3; n4++) {
            ItemEntity plank = new ItemEntity(serverLevel, this.getX(), this.getY(), this.getZ(), new ItemStack(Items.OAK_PLANKS));
            plank.setDefaultPickUpDelay();
            serverLevel.addFreshEntity(plank);
          }

          for (int n4 = 0; n4 < 2; n4++) {
            ItemEntity stick = new ItemEntity(serverLevel, this.getX(), this.getY(), this.getZ(), new ItemStack(Items.STICK));
            stick.setDefaultPickUpDelay();
            serverLevel.addFreshEntity(stick);
          }

          this.discard();
        } else if (this.level().isClientSide() && preCrashSpeedSqr > 0.01) {
          ClientPlayNetworking.send(new C2SBoatCrashPayload(this.getId(), this.getX(), this.getY(), this.getZ()));
          this.discard();
        }
      } else {
        vx *= 0.9900000095367432;
        vy *= 0.949999988079071;
        vz *= 0.9900000095367432;
        this.setDeltaMovement(new Vec3(vx, vy, vz));
        if (this.level().isClientSide() && this.alphaLerpSteps > 0) {
          double d0 = this.getX() + (this.alphaLerpX - this.getX()) / this.alphaLerpSteps;
          double d1 = this.getY() + (this.alphaLerpY - this.getY()) / this.alphaLerpSteps;
          double d2 = this.getZ() + (this.alphaLerpZ - this.getZ()) / this.alphaLerpSteps;
          double d3 = Mth.wrapDegrees(this.alphaLerpYRot - this.getYRot());
          this.setYRot(this.getYRot() + (float)(d3 / this.alphaLerpSteps));
          this.setXRot(this.getXRot() + (float)((this.alphaLerpXRot - this.getXRot()) / this.alphaLerpSteps));
          this.alphaLerpSteps--;
          this.setPos(d0, d1, d2);
          this.setRot(this.getYRot(), this.getXRot());
        }
      }
    } else {
      this.setDeltaMovement(Vec3.ZERO);
    }

    List<Entity> list = this.level().getEntities(this, this.getBoundingBox().inflate(0.2, -0.01, 0.2), EntitySelector.pushableBy(this));
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

  protected void positionRider(Entity passenger, MoveFunction moveFunction) {
    Vec3 position = this.getPassengerRidingPosition(passenger);
    Vec3 offset = passenger.getVehicleAttachmentPoint(this);
    moveFunction.accept(passenger, position.x - offset.x, position.y - offset.y, position.z - offset.z);
  }

  protected Vec3 getPassengerAttachmentPoint(Entity passenger, EntityDimensions dimensions, float scale) {
    return new Vec3(0.0, dimensions.height() / 3.0F - 0.25, 0.0);
  }

  public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
    if (this.isInvulnerableToBase(source)) {
      return false;
    } else if (!this.level().isClientSide() && !this.isRemoved()) {
      this.setHurtDir(-this.getHurtDir());
      this.setHurtTime(10);
      this.setDamage(this.getDamage() + amount * 10.0F);
      this.markHurt();
      if (source.getEntity() instanceof Player && ((Player)source.getEntity()).getAbilities().instabuild) {
        this.discard();
        return true;
      } else {
        if (this.getDamage() > 40.0F) {
          this.discard();

          for (int n4 = 0; n4 < 3; n4++) {
            ItemEntity plank = new ItemEntity(level, this.getX(), this.getY(), this.getZ(), new ItemStack(Items.OAK_PLANKS));
            plank.setDefaultPickUpDelay();
            level.addFreshEntity(plank);
          }

          for (int n4 = 0; n4 < 2; n4++) {
            ItemEntity stick = new ItemEntity(level, this.getX(), this.getY(), this.getZ(), new ItemStack(Items.STICK));
            stick.setDefaultPickUpDelay();
            level.addFreshEntity(stick);
          }
        }

        return true;
      }
    } else {
      return true;
    }
  }

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
