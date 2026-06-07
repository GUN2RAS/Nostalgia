package net.nostalgia.client.events.caches.providers;

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

    // Я ЕГО МАМУ ЕБАЛ БЛЯДСКОГО БАГА Я НЕДЕЛЮ УБИЛ НА ФИКС СЫН ХУЙНИ ЕБАНОЙ
    // кэш ебучего getByName чтоб не парсил строку 50к раз за кадр
    // в рот маму и папу мода ебал бля как он заебал
    private static final Map<String, DimensionHologramCache> NAME_CACHE = new ConcurrentHashMap<>();

    public static DimensionHologramCache getByName(String dimensionId) {
        if (dimensionId == null) return null;
        DimensionHologramCache cached = NAME_CACHE.get(dimensionId);
        if (cached != null) return cached;
        String normalized = net.nostalgia.alphalogic.ritual.DimensionUtil.normalize(dimensionId);
        net.minecraft.resources.Identifier id = net.minecraft.resources.Identifier.tryParse(normalized);
        if (id != null) {
            net.minecraft.resources.ResourceKey<net.minecraft.world.level.Level> key =
                net.minecraft.resources.ResourceKey.create(net.minecraft.core.registries.Registries.DIMENSION, id);
            cached = get(key);
            NAME_CACHE.put(dimensionId, cached);
            return cached;
        }
        return null;
    }

    public static void clearAll() {
        for (DimensionHologramCache c : CACHES.values()) c.clear();
    }

    public static void clearAllOverrides() {
        for (DimensionHologramCache c : CACHES.values()) c.clearOverrides();
    }
}
