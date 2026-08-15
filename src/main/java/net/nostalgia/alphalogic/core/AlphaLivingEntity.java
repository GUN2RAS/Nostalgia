package net.nostalgia.alphalogic.core;

public abstract class AlphaLivingEntity extends AlphaEntity {
  public int health = 20;
  public float moveStrafing;
  public float moveForward;
  public float randomYawVelocity;
  public boolean isJumping = false;

  public AlphaLivingEntity(AlphaWorld world) {
    super(world);
  }

  public void moveEntityWithHeading(float strafe, float forward) {
    if (this.inWater) {
      this.moveFlying(strafe, forward, 0.02F);
      this.moveEntity(this.motionX, this.motionY, this.motionZ);
      this.motionX *= 0.800000011920929;
      this.motionY *= 0.800000011920929;
      this.motionZ *= 0.800000011920929;
      this.motionY -= 0.02;
      if (this.isCollidedHorizontally) {
        this.motionY = 0.30000001192092896;
      }
    } else if (this.isInLava()) {
      this.moveFlying(strafe, forward, 0.02F);
      this.moveEntity(this.motionX, this.motionY, this.motionZ);
      this.motionX *= 0.5;
      this.motionY *= 0.5;
      this.motionZ *= 0.5;
      this.motionY -= 0.02;
    } else {
      float friction = 0.91F;
      if (this.onGround) {
        friction = 0.546F;
        int blockUnder = this.worldObj
          .getBlockId(AlphaMathHelper.floor(this.posX), AlphaMathHelper.floor(this.boundingBox.minY) - 1, AlphaMathHelper.floor(this.posZ));
        if (blockUnder == 79) {
          friction = 0.8918F;
        }
      }

      float speed = 0.16277136F / (friction * friction * friction);
      float moveSpeed = this.onGround ? 0.1F * speed : 0.02F;
      this.moveFlying(strafe, forward, moveSpeed);
      friction = 0.91F;
      if (this.onGround) {
        friction = 0.546F;
        int blockUnder = this.worldObj
          .getBlockId(AlphaMathHelper.floor(this.posX), AlphaMathHelper.floor(this.boundingBox.minY) - 1, AlphaMathHelper.floor(this.posZ));
        if (blockUnder == 79) {
          friction = 0.8918F;
        }
      }

      this.moveEntity(this.motionX, this.motionY, this.motionZ);
      this.motionY -= 0.08;
      this.motionY *= 0.9800000190734863;
      this.motionX *= friction;
      this.motionZ *= friction;
    }
  }

  public boolean isInLava() {
    return false;
  }

  @Override
  public void onEntityUpdate() {
    super.onEntityUpdate();
    this.onLivingUpdate();
  }

  public void onLivingUpdate() {
    if (this.isJumping) {
      if (this.inWater || this.isInLava()) {
        this.motionY += 0.04;
      } else if (this.onGround) {
        this.motionY = 0.42;
      }
    }

    this.moveStrafing *= 0.98F;
    this.moveForward *= 0.98F;
    this.moveEntityWithHeading(this.moveStrafing, this.moveForward);
  }
}
