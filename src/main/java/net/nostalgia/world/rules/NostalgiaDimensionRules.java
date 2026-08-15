package net.nostalgia.world.rules;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.nostalgia.world.dimension.ModDimensions;
import org.jspecify.annotations.Nullable;

public class NostalgiaDimensionRules {
    private static final Map<ResourceKey<Level>, DimensionRules> RULESETS = new HashMap<>();

    static {
        RULESETS.put(ModDimensions.ALPHA_112_01_LEVEL_KEY, new Alpha112DimensionRules());
    }

    public static @Nullable DimensionRules getRules(@Nullable Level level) {
        return level == null ? null : RULESETS.get(level.dimension());
    }
}
