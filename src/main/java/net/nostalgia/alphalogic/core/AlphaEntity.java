package net.nostalgia.alphalogic.core;

import java.util.List;
import java.util.Random;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;

public abstract class AlphaEntity {
  private static int nextEntityID = 0;
  public int entityId = nextEntityID++;
  public double renderDistanceWeight = 1.0;
  public boolean preventEntitySpawning = false;
  public AlphaEntity riddenByEntity;
  public AlphaEntity ridingEntity;
  public AlphaWorld worldObj;
  public double prevPosX;
  public double prevPosY;
  public double prevPosZ;
  public double posX;
  public double posY;
  public double posZ;
  public double motionX;
  public double motionY;
  public double motionZ;
  public float rotationYaw;
  public float rotationPitch;
  public float prevRotationYaw;
  public float prevRotationPitch;
  public final AlphaAABB boundingBox = AlphaAABB.create(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
  public boolean onGround = false;
  public boolean isCollidedHorizontally;
  public boolean isCollidedVertically;
  public boolean isCollided;
  public boolean velocityChanged = false;
  protected boolean isImmuneToFire = false;
  public boolean isDead = false;
  public float yOffset = 0.0F;
  public float width = 0.6F;
  public float height = 1.8F;
  public float prevDistanceWalkedModified = 0.0F;
  public float distanceWalkedModified = 0.0F;
  public float fallDistance = 0.0F;
  public int nextStepDistance = 1;
  public double lastTickPosX;
  public double lastTickPosY;
  public double lastTickPosZ;
  public float ySize = 0.0F;
  public float stepHeight = 0.0F;
  public boolean noClip = false;
  public float entityCollisionReduction = 0.0F;
  protected Random rand = new Random();
  public int ticksExisted = 0;
  public int fireResistance = 1;
  public int fire = 0;
  protected int maxAir = 300;
  public boolean inWater = false;
  public int heartsHalvesLife = 0;
  public int air = 300;
  private boolean isFirstUpdate = true;
  public String skinUrl;

  public AlphaEntity(AlphaWorld world) {
    this.worldObj = world;
    this.setPosition(0.0, 0.0, 0.0);
  }

  public void setDead() {
    this.isDead = true;
  }

  protected void setSize(float width, float height) {
    this.width = width;
    this.height = height;
  }

  public void setPosition(double x, double y, double z) {
    this.posX = x;
    this.posY = y;
    this.posZ = z;
    float halfWidth = this.width / 2.0F;
    this.boundingBox
      .setBounds(x - halfWidth, y - this.yOffset + this.ySize, z - halfWidth, x + halfWidth, y - this.yOffset + this.ySize + this.height, z + halfWidth);
  }

  public void onUpdate() {
    this.onEntityUpdate();
  }

  public void onEntityUpdate() {
    if (this.ridingEntity != null && this.ridingEntity.isDead) {
      this.ridingEntity = null;
    }

    this.ticksExisted++;
    this.prevDistanceWalkedModified = this.distanceWalkedModified;
    this.prevPosX = this.posX;
    this.prevPosY = this.posY;
    this.prevPosZ = this.posZ;
    this.prevRotationPitch = this.rotationPitch;
    this.prevRotationYaw = this.rotationYaw;
    if (this.posY < -64.0) {
      this.kill();
    }

    if (this.fire > 0) {
      if (this.fire % 20 == 0) {
        this.hurt(null, 1.0F);
      }

      this.fire--;
    }

    if (this.inWater) {
      if (this.air > 0) {
        this.air--;
      } else if (this.ticksExisted % 20 == 0) {
        this.hurt(null, 2.0F);
      }

      this.fire = 0;
    } else if (this.air < this.maxAir) {
      this.air += 10;
      if (this.air > this.maxAir) {
        this.air = this.maxAir;
      }
    }

    this.isFirstUpdate = false;
  }

  protected void kill() {
    this.setDead();
  }

  public void moveEntity(double x, double y, double z) {
    if (this.noClip) {
      this.boundingBox.move(x, y, z);
      this.posX = (this.boundingBox.minX + this.boundingBox.maxX) / 2.0;
      this.posY = this.boundingBox.minY + this.yOffset - this.ySize;
      this.posZ = (this.boundingBox.minZ + this.boundingBox.maxZ) / 2.0;
    } else {
      double orgX = this.posX;
      double orgZ = this.posZ;
      double inputX = x;
      double inputY = y;
      double inputZ = z;
      AlphaAABB orgBoundingBox = this.boundingBox.copy();
      boolean sneaking = this.onGround && this.isSneaking();
      if (sneaking) {
        double sneakExt;
        for (sneakExt = 0.05; x != 0.0 && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox.offset(x, -1.0, 0.0)).isEmpty(); inputX = x) {
          if (x < sneakExt && x >= -sneakExt) {
            x = 0.0;
          } else if (x > 0.0) {
            x -= sneakExt;
          } else {
            x += sneakExt;
          }
        }

        for (; z != 0.0 && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox.offset(0.0, -1.0, z)).isEmpty(); inputZ = z) {
          if (z < sneakExt && z >= -sneakExt) {
            z = 0.0;
          } else if (z > 0.0) {
            z -= sneakExt;
          } else {
            z += sneakExt;
          }
        }
      }

      List<AlphaAABB> collidingBoxes = this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox.expand(x, y, z));

      for (int i = 0; i < collidingBoxes.size(); i++) {
        y = collidingBoxes.get(i).calculateYOffset(this.boundingBox, y);
      }

      this.boundingBox.move(0.0, y, 0.0);
      boolean fell = this.onGround || inputY != y && inputY < 0.0;

      for (int i = 0; i < collidingBoxes.size(); i++) {
        x = collidingBoxes.get(i).calculateXOffset(this.boundingBox, x);
      }

      this.boundingBox.move(x, 0.0, 0.0);

      for (int i = 0; i < collidingBoxes.size(); i++) {
        z = collidingBoxes.get(i).calculateZOffset(this.boundingBox, z);
      }

      this.boundingBox.move(0.0, 0.0, z);
      if (this.stepHeight > 0.0F && fell && this.ySize < 0.05F && (inputX != x || inputZ != z)) {
        double cachedX = x;
        double cachedY = y;
        double cachedZ = z;
        x = inputX;
        y = this.stepHeight;
        z = inputZ;
        AlphaAABB cachedBox = this.boundingBox.copy();
        this.boundingBox.set(orgBoundingBox);
        List<AlphaAABB> stepBoxes = this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox.expand(inputX, y, inputZ));

        for (int i = 0; i < stepBoxes.size(); i++) {
          y = stepBoxes.get(i).calculateYOffset(this.boundingBox, y);
        }

        this.boundingBox.move(0.0, y, 0.0);

        for (int i = 0; i < stepBoxes.size(); i++) {
          x = stepBoxes.get(i).calculateXOffset(this.boundingBox, x);
        }

        this.boundingBox.move(x, 0.0, 0.0);

        for (int i = 0; i < stepBoxes.size(); i++) {
          z = stepBoxes.get(i).calculateZOffset(this.boundingBox, z);
        }

        this.boundingBox.move(0.0, 0.0, z);
        if (cachedX * cachedX + cachedZ * cachedZ >= x * x + z * z) {
          x = cachedX;
          y = cachedY;
          z = cachedZ;
          this.boundingBox.set(cachedBox);
        } else {
          this.ySize = (float)(this.ySize + 0.5);
        }
      }

      this.posX = (this.boundingBox.minX + this.boundingBox.maxX) / 2.0;
      this.posY = this.boundingBox.minY + this.yOffset - this.ySize;
      this.posZ = (this.boundingBox.minZ + this.boundingBox.maxZ) / 2.0;
      this.isCollidedHorizontally = inputX != x || inputZ != z;
      this.isCollidedVertically = inputY != y;
      this.onGround = inputY != y && inputY < 0.0;
      this.isCollided = this.isCollidedHorizontally || this.isCollidedVertically;
      if (this.onGround) {
        if (this.fallDistance > 0.0F) {
          this.fall(this.fallDistance);
          this.fallDistance = 0.0F;
        }
      } else if (y < 0.0) {
        this.fallDistance = (float)(this.fallDistance - y);
      }

      if (inputX != x) {
        this.motionX = 0.0;
      }

      if (inputY != y) {
        this.motionY = 0.0;
      }

      if (inputZ != z) {
        this.motionZ = 0.0;
      }

      double distCalcX = this.posX - orgX;
      double distCalcZ = this.posZ - orgZ;
      this.distanceWalkedModified = (float)(this.distanceWalkedModified + AlphaMathHelper.sqrt(distCalcX * distCalcX + distCalcZ * distCalcZ) * 0.6);
      if (this.distanceWalkedModified > this.nextStepDistance && this.onGround) {
        this.nextStepDistance = (int)this.distanceWalkedModified + 1;
        int stepX = AlphaMathHelper.floor(this.posX);
        int stepY = AlphaMathHelper.floor(this.boundingBox.minY - 0.2);
        int stepZ = AlphaMathHelper.floor(this.posZ);
        int blockId = this.worldObj.getBlockId(stepX, stepY, stepZ);
        if (blockId > 0) {
          this.playStepSound(stepX, stepY, stepZ, blockId);
        }
      }

      int startX = AlphaMathHelper.floor(this.boundingBox.minX);
      int startY = AlphaMathHelper.floor(this.boundingBox.minY);
      int startZ = AlphaMathHelper.floor(this.boundingBox.minZ);
      int endX = AlphaMathHelper.floor(this.boundingBox.maxX);
      int endY = AlphaMathHelper.floor(this.boundingBox.maxY);
      int endZ = AlphaMathHelper.floor(this.boundingBox.maxZ);

      for (int bX = startX; bX <= endX; bX++) {
        for (int bY = startY; bY <= endY; bY++) {
          for (int bZ = startZ; bZ <= endZ; bZ++) {
            int blockId = this.worldObj.getBlockId(bX, bY, bZ);
            if (blockId > 0) {
              AlphaBlockBehaviors.onEntityCollidedWithBlock(this, bX, bY, bZ, blockId);
            }
          }
        }
      }

      boolean wasInWater = this.inWater;
      this.inWater = this.worldObj.handleMaterialAcceleration(this.boundingBox.expand(0.0, -0.4, 0.0), 8, this);
      if (!wasInWater && this.inWater) {
        float velocity = AlphaMathHelper.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ) * 0.2F;
        if (velocity > 1.0F) {
          velocity = 1.0F;
        }

        if (velocity > 0.05F && this.worldObj.mcLevel instanceof ServerLevel serverLevel) {
          serverLevel.sendParticles(
            ParticleTypes.SPLASH, this.posX, this.posY + 1.0, this.posZ, (int)(1.0F + this.width * 20.0F), this.width, 0.0, this.width, velocity
          );
          serverLevel.sendParticles(
            ParticleTypes.BUBBLE, this.posX, this.posY + 1.0, this.posZ, (int)(1.0F + this.width * 20.0F), this.width, 0.0, this.width, velocity
          );
        }
      }

      boolean inLava = this.worldObj.handleMaterialAcceleration(this.boundingBox.expand(0.0, -0.4, 0.0), 10, this);
      if (inLava && !this.isImmuneToFire) {
        this.hurt(null, 4.0F);
        this.fire = 600;
      }

      this.ySize *= 0.4F;
    }
  }

  protected void fall(float distance) {
  }

  public boolean isSneaking() {
    return false;
  }

  public void moveFlying(float x, float z, float friction) {
    float f = AlphaMathHelper.sqrt(x * x + z * z);
    if (!(f < 0.01F)) {
      if (f < 1.0F) {
        f = 1.0F;
      }

      f = friction / f;
      x *= f;
      z *= f;
      float sinYaw = AlphaMathHelper.sin(this.rotationYaw * 3.1415927F / 180.0F);
      float cosYaw = AlphaMathHelper.cos(this.rotationYaw * 3.1415927F / 180.0F);
      this.motionX += x * cosYaw - z * sinYaw;
      this.motionZ += z * cosYaw + x * sinYaw;
    }
  }

  public boolean hurt(DamageSource source, float amount) {
    return false;
  }

  public void playStepSound(int x, int y, int z, int blockId) {
  }
}
