package net.nostalgia.entity;

import net.minecraft.network.syncher.SynchedEntityData.Builder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.nostalgia.alphalogic.ritual.SkyPortalManager;

public class SkyPortalBeamEntity extends Entity {
  public SkyPortalBeamEntity(EntityType<?> entityType, Level level) {
    super(entityType, level);
    this.setNoGravity(true);
  }

  protected void defineSynchedData(Builder builder) {
  }

  protected void readAdditionalSaveData(ValueInput input) {
  }

  protected void addAdditionalSaveData(ValueOutput output) {
  }

  public void tick() {
    super.tick();
    if (!this.level().isClientSide()) {
      boolean myPortalActive = false;
      for (net.nostalgia.alphalogic.ritual.SkyPortalEventInstance p : SkyPortalManager.allPortals()) {
        if (p.center().distSqr(this.blockPosition()) <= 9.0) {
          myPortalActive = true;
          break;
        }
      }
      if (!myPortalActive) {
        this.discard();
      }
    }
  }

  public boolean hurtServer(ServerLevel level, DamageSource damageSource, float damageAmount) {
    return false;
  }
}
