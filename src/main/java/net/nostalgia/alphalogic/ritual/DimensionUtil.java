package net.nostalgia.alphalogic.ritual;

public final class DimensionUtil {

    public static final String ALPHA_FULL = "nostalgia:alpha_112_01";
    public static final String RD_FULL    = "nostalgia:rd_132211";
    public static final String OW_FULL    = "minecraft:overworld";

    private DimensionUtil() {}

    public static String normalize(String dimId) {
        if (dimId == null) return null;
        return switch (dimId) {
            case "overworld" -> OW_FULL;
            case "alpha"     -> ALPHA_FULL;
            case "rd"        -> RD_FULL;
            default          -> dimId;
        };
    }

    public static boolean isClientGenerated(String dimId) {
        if (dimId == null) return false;
        String n = normalize(dimId);
        return n.equals(ALPHA_FULL) || n.equals(RD_FULL);
    }

    public static boolean isRD(String dimId) {
        if (dimId == null) return false;
        return normalize(dimId).equals(RD_FULL);
    }

    public static boolean isAlpha(String dimId) {
        if (dimId == null) return false;
        return normalize(dimId).equals(ALPHA_FULL);
    }

    public static boolean isOverworld(String dimId) {
        if (dimId == null) return false;
        return normalize(dimId).equals(OW_FULL);
    }

    public static net.minecraft.server.level.ServerLevel resolveLevel(
            net.minecraft.server.MinecraftServer server, String dimId) {
        if (server == null || dimId == null) return null;
        String n = normalize(dimId);
        net.minecraft.resources.Identifier id = net.minecraft.resources.Identifier.tryParse(n);
        if (id == null) return null;
        return server.getLevel(net.minecraft.resources.ResourceKey.create(
                net.minecraft.core.registries.Registries.DIMENSION, id));
    }
}
