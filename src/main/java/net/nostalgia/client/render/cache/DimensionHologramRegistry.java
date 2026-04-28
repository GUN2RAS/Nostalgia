package net.nostalgia.client.render.cache;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.nostalgia.world.dimension.ModDimensions;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class DimensionHologramRegistry {
    private static final Map<ResourceKey<Level>, DimensionHologramCache> CACHES = new ConcurrentHashMap<>();

    static {
        CACHES.put(ModDimensions.ALPHA_112_01_LEVEL_KEY, new DimensionHologramCache(ModDimensions.ALPHA_112_01_LEVEL_KEY, new AlphaHologramProvider()));
        CACHES.put(ModDimensions.RD_132211_LEVEL_KEY, new DimensionHologramCache(ModDimensions.RD_132211_LEVEL_KEY, new RDHologramProvider()));
        CACHES.put(Level.OVERWORLD, new DimensionHologramCache(Level.OVERWORLD, new EmptyHologramProvider()));
    }

    private DimensionHologramRegistry() {}

    public static DimensionHologramCache get(ResourceKey<Level> dim) {
        return CACHES.computeIfAbsent(dim, k -> new DimensionHologramCache(k, new EmptyHologramProvider()));
    }

    public static DimensionHologramCache getByName(String dimensionId) {
        if (dimensionId == null) return null;
        if ("alpha".equals(dimensionId) || "nostalgia:alpha_112_01".equals(dimensionId)) {
            return get(ModDimensions.ALPHA_112_01_LEVEL_KEY);
        }
        if ("rd".equals(dimensionId) || "nostalgia:rd_132211".equals(dimensionId)) {
            return get(ModDimensions.RD_132211_LEVEL_KEY);
        }
        if ("overworld".equals(dimensionId) || "minecraft:overworld".equals(dimensionId)) {
            return get(Level.OVERWORLD);
        }
        return null;
    }

    public static void clearAll() {
        for (DimensionHologramCache c : CACHES.values()) c.clear();
    }
}
