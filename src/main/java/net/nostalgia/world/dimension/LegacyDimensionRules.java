package net.nostalgia.world.dimension;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.nostalgia.world.rules.LegacyProfiles;

public class LegacyDimensionRules {
  public LegacyDimensionRules() {
  }

  public static boolean isLegacyDimension(Level level) {
    ResourceKey<Level> dimensionKey = level.dimension();
    return dimensionKey.equals(ModDimensions.ALPHA_112_01_LEVEL_KEY);
  }

  public static boolean hasHunger(Level level) {
    return !isLegacyDimension(level);
  }

  public static boolean canSprint(Level level) {
    return !isLegacyDimension(level);
  }

  public static boolean foodHealsInstantly(Level level) {
    return isLegacyDimension(level);
  }

  public static boolean hasWeather(Level level) {
    return LegacyProfiles.get(level).hasWeather();
  }
}
