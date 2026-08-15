package net.nostalgia.world.rules;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.nostalgia.world.dimension.ModDimensions;

public class NostalgiaRules {
  private static final Map<ResourceKey<Level>, LegacyRuleSet> REGISTRY = new HashMap<>();
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
    .disableSweepAttack()
    .legacyKnockback()
    .disableAttackSounds()
    .legacySkeleton()
    .legacyMobAI()
    .build();

  public NostalgiaRules() {
  }

  public static void register(ResourceKey<Level> dim, LegacyRuleSet rules) {
    REGISTRY.put(dim, rules);
  }

  public static LegacyRuleSet getForLevel(Level level) {
    if (level == null) {
      return VANILLA;
    } else {
      LegacyRuleSet r = REGISTRY.get(level.dimension());
      return r != null ? r : VANILLA;
    }
  }

  static {
    register(ModDimensions.ALPHA_112_01_LEVEL_KEY, ALPHA);
  }
}
