package net.nostalgia.world.dimension;

import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;

public class LegacyDimensionRules {

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
        if (level.dimension().equals(ModDimensions.RD_132211_LEVEL_KEY)) return false;
        if (level.dimension().equals(ModDimensions.ALPHA_112_01_LEVEL_KEY)) return false;
        return true;
    }
}
