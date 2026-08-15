package net.nostalgia.client.events.caches.providers;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.nostalgia.alphalogic.ritual.DimensionUtil;
import net.nostalgia.world.dimension.ModDimensions;

public final class DimensionHologramRegistry {
  private static final Map<ResourceKey<Level>, DimensionHologramCache> CACHES = new ConcurrentHashMap<>();
  private static final Map<String, DimensionHologramCache> NAME_CACHE = new ConcurrentHashMap<>();

  private DimensionHologramRegistry() {
  }

  public static DimensionHologramCache get(ResourceKey<Level> dim) {
    return CACHES.computeIfAbsent(dim, k -> new DimensionHologramCache((ResourceKey<Level>)k, new EmptyHologramProvider()));
  }

  public static DimensionHologramCache getByName(String dimensionId) {
    if (dimensionId == null) {
      return null;
    } else {
      DimensionHologramCache cached = NAME_CACHE.get(dimensionId);
      if (cached != null) {
        return cached;
      } else {
        String normalized = DimensionUtil.normalize(dimensionId);
        Identifier id = Identifier.tryParse(normalized);
        if (id != null) {
          ResourceKey<Level> key = ResourceKey.create(Registries.DIMENSION, id);
          cached = get(key);
          NAME_CACHE.put(dimensionId, cached);
          return cached;
        } else {
          return null;
        }
      }
    }
  }

  public static void clearAll() {
    for (DimensionHologramCache c : CACHES.values()) {
      c.clear();
    }
  }

  public static void clearAllOverrides() {
    for (DimensionHologramCache c : CACHES.values()) {
      c.clearOverrides();
    }
  }

  public static boolean hasAnySections() {
    for (DimensionHologramCache c : CACHES.values()) {
      if (c.getSections() != null && !c.getSections().isEmpty()) {
        return true;
      }
    }
    return false;
  }

  public static boolean isClientGenerated(String dimensionId) {
    DimensionHologramCache cache = getByName(dimensionId);
    return cache != null && cache.isClientGenerated();
  }

  static {
    CACHES.put(ModDimensions.ALPHA_112_01_LEVEL_KEY, new DimensionHologramCache(ModDimensions.ALPHA_112_01_LEVEL_KEY, new AlphaHologramProvider()));
    CACHES.put(ModDimensions.RD_132211_LEVEL_KEY, new DimensionHologramCache(ModDimensions.RD_132211_LEVEL_KEY, new RDHologramProvider()));
    CACHES.put(Level.OVERWORLD, new DimensionHologramCache(Level.OVERWORLD, new EmptyHologramProvider()));
  }
}
