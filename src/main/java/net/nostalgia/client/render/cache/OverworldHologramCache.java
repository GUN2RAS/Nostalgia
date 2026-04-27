package net.nostalgia.client.render.cache;

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class OverworldHologramCache {
    private static final Long2ObjectOpenHashMap<HologramSection> CACHE = new Long2ObjectOpenHashMap<>();

    public static void putSection(int chunkX, int sectionY, int chunkZ, HologramSection section) {
        long key = (((long) chunkX & 0x3FFFFF) << 42) | (((long) sectionY & 0xFFFFF) << 22) | ((long) chunkZ & 0x3FFFFF);
        CACHE.put(key, section);
    }

    public static BlockState getBlockState(int x, int y, int z) {
        int cx = x >> 4;
        int sy = y >> 4;
        int cz = z >> 4;
        long key = (((long) cx & 0x3FFFFF) << 42) | (((long) sy & 0xFFFFF) << 22) | ((long) cz & 0x3FFFFF);
        
        HologramSection section = CACHE.get(key);
        if (section == null) return null;
        
        int lx = x & 15;
        int ly = y & 15;
        int lz = z & 15;
        return section.getBlockState(lx, ly, lz);
    }

    public static void clear() {
        CACHE.clear();
        CACHE.trim();
    }
}
