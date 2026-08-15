package net.nostalgia.world.rules;

public class LegacyRuleSet {
  public final boolean disableCriticalHits;
  public final boolean disableHunger;
  public final boolean instantBowShoot;
  public final boolean instantFoodConsume;
  public final boolean tntIgnitesOnPunch;
  public final boolean disableWeaponCooldown;
  public final boolean infiniteFireSpread;
  public final boolean fragileBoats;
  public final boolean legacyChest;
  public final boolean legacySounds;
  public final boolean farmlandTrampleOnWalk;
  public final boolean disableSweepAttack;
  public final boolean legacyKnockback;
  public final boolean disableAttackSounds;
  public final boolean legacySkeleton;
  public final boolean legacyMobAI;

  private LegacyRuleSet(LegacyRuleSet.Builder builder) {
    this.disableCriticalHits = builder.disableCriticalHits;
    this.disableHunger = builder.disableHunger;
    this.instantBowShoot = builder.instantBowShoot;
    this.instantFoodConsume = builder.instantFoodConsume;
    this.tntIgnitesOnPunch = builder.tntIgnitesOnPunch;
    this.disableWeaponCooldown = builder.disableWeaponCooldown;
    this.infiniteFireSpread = builder.infiniteFireSpread;
    this.fragileBoats = builder.fragileBoats;
    this.legacyChest = builder.legacyChest;
    this.legacySounds = builder.legacySounds;
    this.farmlandTrampleOnWalk = builder.farmlandTrampleOnWalk;
    this.disableSweepAttack = builder.disableSweepAttack;
    this.legacyKnockback = builder.legacyKnockback;
    this.disableAttackSounds = builder.disableAttackSounds;
    this.legacySkeleton = builder.legacySkeleton;
    this.legacyMobAI = builder.legacyMobAI;
  }

  public static class Builder {
    private boolean disableCriticalHits = false;
    private boolean disableHunger = false;
    private boolean instantBowShoot = false;
    private boolean instantFoodConsume = false;
    private boolean tntIgnitesOnPunch = false;
    private boolean disableWeaponCooldown = false;
    private boolean infiniteFireSpread = false;
    private boolean fragileBoats = false;
    private boolean legacyChest = false;
    private boolean legacySounds = false;
    private boolean farmlandTrampleOnWalk = false;
    private boolean disableSweepAttack = false;
    private boolean legacyKnockback = false;
    private boolean disableAttackSounds = false;
    private boolean legacySkeleton = false;
    private boolean legacyMobAI = false;

    public Builder() {
    }

    public LegacyRuleSet.Builder disableCriticalHits() {
      this.disableCriticalHits = true;
      return this;
    }

    public LegacyRuleSet.Builder disableHunger() {
      this.disableHunger = true;
      return this;
    }

    public LegacyRuleSet.Builder instantBowShoot() {
      this.instantBowShoot = true;
      return this;
    }

    public LegacyRuleSet.Builder instantFoodConsume() {
      this.instantFoodConsume = true;
      return this;
    }

    public LegacyRuleSet.Builder tntIgnitesOnPunch() {
      this.tntIgnitesOnPunch = true;
      return this;
    }

    public LegacyRuleSet.Builder disableWeaponCooldown() {
      this.disableWeaponCooldown = true;
      return this;
    }

    public LegacyRuleSet.Builder infiniteFireSpread() {
      this.infiniteFireSpread = true;
      return this;
    }

    public LegacyRuleSet.Builder fragileBoats() {
      this.fragileBoats = true;
      return this;
    }

    public LegacyRuleSet.Builder legacyChest() {
      this.legacyChest = true;
      return this;
    }

    public LegacyRuleSet.Builder legacySounds() {
      this.legacySounds = true;
      return this;
    }

    public LegacyRuleSet.Builder farmlandTrampleOnWalk() {
      this.farmlandTrampleOnWalk = true;
      return this;
    }

    public LegacyRuleSet.Builder disableSweepAttack() {
      this.disableSweepAttack = true;
      return this;
    }

    public LegacyRuleSet.Builder legacyKnockback() {
      this.legacyKnockback = true;
      return this;
    }

    public LegacyRuleSet.Builder disableAttackSounds() {
      this.disableAttackSounds = true;
      return this;
    }

    public LegacyRuleSet.Builder legacySkeleton() {
      this.legacySkeleton = true;
      return this;
    }

    public LegacyRuleSet.Builder legacyMobAI() {
      this.legacyMobAI = true;
      return this;
    }

    public LegacyRuleSet build() {
      return new LegacyRuleSet(this);
    }
  }
}
