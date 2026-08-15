package net.nostalgia.alphalogic.ritual;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;

public final class DimensionUtil {
  public static final String ALPHA_FULL = "nostalgia:alpha_112_01";
  public static final String RD_FULL = "nostalgia:rd_132211";
  public static final String OW_FULL = "minecraft:overworld";
  private static final Set<String> CLIENT_GENERATED = new HashSet<>();
  private static final Set<String> HAS_HOLOGRAM_MAP = new HashSet<>();
  private static final Map<String, Identifier> PREVIEW_TEXTURES = new HashMap<>();

  private DimensionUtil() {
  }

  public static String normalize(String dimId) {
    if (dimId == null) {
      return null;
    } else {
      return switch (dimId) {
        case "overworld" -> "minecraft:overworld";
        case "alpha" -> "nostalgia:alpha_112_01";
        case "rd" -> "nostalgia:rd_132211";
        default -> dimId;
      };
    }
  }

  public static void registerClientGenerated(String normalizedDimId) {
    CLIENT_GENERATED.add(normalizedDimId);
  }

  public static boolean isClientGenerated(String dimId) {
    return dimId == null ? false : CLIENT_GENERATED.contains(normalize(dimId));
  }

  public static boolean hasHologramMap(String dimId) {
    return dimId == null ? false : HAS_HOLOGRAM_MAP.contains(normalize(dimId));
  }

  public static Identifier getPreviewTexture(String dimId) {
    return dimId == null ? null : PREVIEW_TEXTURES.get(normalize(dimId));
  }

  public static boolean isRD(String dimId) {
    return dimId == null ? false : normalize(dimId).equals("nostalgia:rd_132211");
  }

  public static boolean isAlpha(String dimId) {
    return dimId == null ? false : normalize(dimId).equals("nostalgia:alpha_112_01");
  }

  public static boolean isOverworld(String dimId) {
    return dimId == null ? false : normalize(dimId).equals("minecraft:overworld");
  }

  public static ServerLevel resolveLevel(MinecraftServer server, String dimId) {
    if (server != null && dimId != null) {
      String n = normalize(dimId);
      Identifier id = Identifier.tryParse(n);
      return id == null ? null : server.getLevel(ResourceKey.create(Registries.DIMENSION, id));
    } else {
      return null;
    }
  }

  static {
    CLIENT_GENERATED.add("nostalgia:alpha_112_01");
    CLIENT_GENERATED.add("nostalgia:rd_132211");
    HAS_HOLOGRAM_MAP.add("nostalgia:alpha_112_01");
    HAS_HOLOGRAM_MAP.add("minecraft:overworld");
    PREVIEW_TEXTURES.put("nostalgia:rd_132211", Identifier.fromNamespaceAndPath("nostalgia", "textures/gui/versions/rd.png"));
  }
}
