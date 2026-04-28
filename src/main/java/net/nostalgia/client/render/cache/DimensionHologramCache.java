package net.nostalgia.client.render.cache;

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DimensionHologramCache {
    private final ResourceKey<Level> dimension;
    private final DimensionHologramProvider provider;

    private final Long2ObjectOpenHashMap<HologramSection> sections = new Long2ObjectOpenHashMap<>();
    private final ConcurrentHashMap<Long, BlockState> overrides = new ConcurrentHashMap<>();

    public DimensionHologramCache(ResourceKey<Level> dimension, DimensionHologramProvider provider) {
        this.dimension = dimension;
        this.provider = provider;
    }

    public ResourceKey<Level> dimension() { return dimension; }
    public DimensionHologramProvider provider() { return provider; }

    public void setOverride(BlockPos pos, BlockState state) {
        overrides.put(pos.asLong(), state);
    }

    public BlockState getOverride(BlockPos pos) {
        return overrides.get(pos.asLong());
    }

    public boolean hasOverride(BlockPos pos) {
        return overrides.containsKey(pos.asLong());
    }

    public Map<BlockPos, BlockState> getAllOverrides() {
        Map<BlockPos, BlockState> result = new HashMap<>();
        overrides.forEach((longPos, state) -> result.put(BlockPos.of(longPos), state));
        return result;
    }

    public void clearOverrides() {
        overrides.clear();
    }

    public void putSection(int chunkX, int sectionY, int chunkZ, HologramSection section) {
        long key = (((long) chunkX & 0x3FFFFF) << 42) | (((long) sectionY & 0xFFFFF) << 22) | ((long) chunkZ & 0x3FFFFF);
        sections.put(key, section);
    }

    public BlockState getSectionBlock(int x, int y, int z) {
        int cx = x >> 4, sy = y >> 4, cz = z >> 4;
        long key = (((long) cx & 0x3FFFFF) << 42) | (((long) sy & 0xFFFFF) << 22) | ((long) cz & 0x3FFFFF);
        HologramSection section = sections.get(key);
        if (section == null) return null;
        int lx = x & 15, ly = y & 15, lz = z & 15;
        return section.getBlockState(lx, ly, lz);
    }

    public void clearSections() {
        sections.clear();
        sections.trim();
    }

    public void clear() {
        clearOverrides();
        clearSections();
    }
}
