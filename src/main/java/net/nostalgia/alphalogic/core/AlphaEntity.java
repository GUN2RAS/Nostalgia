package net.nostalgia.alphalogic.core;

import java.util.List;
import java.util.Random;

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
    
    public float yOffset = 0.0f;
    public float width = 0.6f;
    public float height = 1.8f;
    public float prevDistanceWalkedModified = 0.0f;
    public float distanceWalkedModified = 0.0f;
    
    public float fallDistance = 0.0f;
    public int nextStepDistance = 1;
    
    public double lastTickPosX;
    public double lastTickPosY;
    public double lastTickPosZ;
    
    public float ySize = 0.0f;
    public float stepHeight = 0.0f;
    public boolean noClip = false;
    public float entityCollisionReduction = 0.0f;
    
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
        float halfWidth = this.width / 2.0f;
        this.boundingBox.setBounds(
            x - halfWidth, 
            y - this.yOffset + this.ySize, 
            z - halfWidth, 
            x + halfWidth, 
            y - this.yOffset + this.ySize + this.height, 
            z + halfWidth
        );
    }

    public void onUpdate() {
        this.onEntityUpdate();
    }

    public void onEntityUpdate() {
        if (this.ridingEntity != null && this.ridingEntity.isDead) {
            this.ridingEntity = null;
        }
        ++this.ticksExisted;
        this.prevDistanceWalkedModified = this.distanceWalkedModified;
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.prevRotationPitch = this.rotationPitch;
        this.prevRotationYaw = this.rotationYaw;

        if (this.posY < -64.0) {
            this.kill();
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
            return;
        }

        double orgX = this.posX;
        double orgZ = this.posZ;
        double inputX = x;
        double inputY = y;
        double inputZ = z;

        AlphaAABB orgBoundingBox = this.boundingBox.copy();
        boolean sneaking = this.onGround && this.isSneaking();
        
        if (sneaking) {
            double sneakExt = 0.05;
            while (x != 0.0 && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox.offset(x, -1.0, 0.0)).isEmpty()) {
                if (x < sneakExt && x >= -sneakExt) x = 0.0;
                else if (x > 0.0) x -= sneakExt;
                else x += sneakExt;
                inputX = x;
            }
            while (z != 0.0 && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox.offset(0.0, -1.0, z)).isEmpty()) {
                if (z < sneakExt && z >= -sneakExt) z = 0.0;
                else if (z > 0.0) z -= sneakExt;
                else z += sneakExt;
                inputZ = z;
            }
        }

        List<AlphaAABB> collidingBoxes = this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox.expand(x, y, z));
        
        for (int i = 0; i < collidingBoxes.size(); ++i) {
            y = collidingBoxes.get(i).calculateYOffset(this.boundingBox, y);
        }
        this.boundingBox.move(0.0, y, 0.0);

        boolean fell = this.onGround || inputY != y && inputY < 0.0;
        
        for (int i = 0; i < collidingBoxes.size(); ++i) {
            x = collidingBoxes.get(i).calculateXOffset(this.boundingBox, x);
        }
        this.boundingBox.move(x, 0.0, 0.0);

        for (int i = 0; i < collidingBoxes.size(); ++i) {
            z = collidingBoxes.get(i).calculateZOffset(this.boundingBox, z);
        }
        this.boundingBox.move(0.0, 0.0, z);

        if (this.stepHeight > 0.0f && fell && this.ySize < 0.05f && (inputX != x || inputZ != z)) {
            double cachedX = x;
            double cachedY = y;
            double cachedZ = z;
            x = inputX;
            y = this.stepHeight;
            z = inputZ;

            AlphaAABB cachedBox = this.boundingBox.copy();
            this.boundingBox.set(orgBoundingBox);
            List<AlphaAABB> stepBoxes = this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox.expand(x, y, z));
            
            for (int i = 0; i < stepBoxes.size(); ++i) {
                y = stepBoxes.get(i).calculateYOffset(this.boundingBox, y);
            }
            this.boundingBox.move(0.0, y, 0.0);

            for (int i = 0; i < stepBoxes.size(); ++i) {
                x = stepBoxes.get(i).calculateXOffset(this.boundingBox, x);
            }
            this.boundingBox.move(x, 0.0, 0.0);

            for (int i = 0; i < stepBoxes.size(); ++i) {
                z = stepBoxes.get(i).calculateZOffset(this.boundingBox, z);
            }
            this.boundingBox.move(0.0, 0.0, z);

            if (cachedX * cachedX + cachedZ * cachedZ >= x * x + z * z) {
                x = cachedX;
                y = cachedY;
                z = cachedZ;
                this.boundingBox.set(cachedBox);
            } else {
                this.ySize = (float)((double)this.ySize + 0.5);
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
            if (this.fallDistance > 0.0f) {
                this.fall(this.fallDistance);
                this.fallDistance = 0.0f;
            }
        } else if (y < 0.0) {
            this.fallDistance = (float)((double)this.fallDistance - y);
        }

        if (inputX != x) this.motionX = 0.0;
        if (inputY != y) this.motionY = 0.0;
        if (inputZ != z) this.motionZ = 0.0;

        double distCalcX = this.posX - orgX;
        double distCalcZ = this.posZ - orgZ;
        this.distanceWalkedModified = (float)((double)this.distanceWalkedModified + AlphaMathHelper.sqrt(distCalcX * distCalcX + distCalcZ * distCalcZ) * 0.6);

        this.ySize *= 0.4f;
    }

    protected void fall(float distance) {
        
    }

    public boolean isSneaking() {
        return false;
    }

    public void moveFlying(float x, float z, float friction) {
        float f = AlphaMathHelper.sqrt(x * x + z * z);
        if (f < 0.01f) {
            return;
        }
        if (f < 1.0f) {
            f = 1.0f;
        }
        f = friction / f;
        x *= f;
        z *= f;
        float sinYaw = AlphaMathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0f);
        float cosYaw = AlphaMathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0f);
        this.motionX += (double)(x * cosYaw - z * sinYaw);
        this.motionZ += (double)(z * cosYaw + x * sinYaw);
    }
}
