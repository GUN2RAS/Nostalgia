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

    private LegacyRuleSet(Builder builder) {
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

        public Builder disableCriticalHits() {
            this.disableCriticalHits = true;
            return this;
        }

        public Builder disableHunger() {
            this.disableHunger = true;
            return this;
        }

        public Builder instantBowShoot() {
            this.instantBowShoot = true;
            return this;
        }

        public Builder instantFoodConsume() {
            this.instantFoodConsume = true;
            return this;
        }

        public Builder tntIgnitesOnPunch() {
            this.tntIgnitesOnPunch = true;
            return this;
        }

        public Builder disableWeaponCooldown() {
            this.disableWeaponCooldown = true;
            return this;
        }

        public Builder infiniteFireSpread() {
            this.infiniteFireSpread = true;
            return this;
        }

        public Builder fragileBoats() {
            this.fragileBoats = true;
            return this;
        }

        public Builder legacyChest() {
            this.legacyChest = true;
            return this;
        }

        public Builder legacySounds() {
            this.legacySounds = true;
            return this;
        }

        public Builder farmlandTrampleOnWalk() {
            this.farmlandTrampleOnWalk = true;
            return this;
        }

        public LegacyRuleSet build() {
            return new LegacyRuleSet(this);
        }
    }
}
