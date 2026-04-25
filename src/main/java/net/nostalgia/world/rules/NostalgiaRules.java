package net.nostalgia.world.rules;

import net.minecraft.world.level.Level;
import net.nostalgia.world.dimension.ModDimensions;

public class NostalgiaRules {

    public static final LegacyRuleSet VANILLA = new LegacyRuleSet.Builder().build();

    public static final LegacyRuleSet ALPHA = new LegacyRuleSet.Builder()
            .disableCriticalHits()
            .disableHunger()
            .instantBowShoot()
            .instantFoodConsume()
            .tntIgnitesOnPunch()
            .disableWeaponCooldown()
            .infiniteFireSpread()
            .fragileBoats()
            .legacyChest()
            .legacySounds()
            .farmlandTrampleOnWalk()
            .build();

    public static LegacyRuleSet getForLevel(Level level) {
        if (level.dimension() == ModDimensions.ALPHA_112_01_LEVEL_KEY) {
            return ALPHA;
        }
        return VANILLA;
    }
}
