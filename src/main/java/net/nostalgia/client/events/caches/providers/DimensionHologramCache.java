package net.nostalgia.client.events.caches.providers;

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

    private volatile Long2ObjectOpenHashMap<HologramSection> sections = new Long2ObjectOpenHashMap<>();
    private volatile it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap chunkVersions = new it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap();
    private final ConcurrentHashMap<Long, BlockState> overrides = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, BlockState> ritualOverrides = new ConcurrentHashMap<>();

    public int overrideCount() { return overrides.size() + ritualOverrides.size(); }

    public DimensionHologramCache(ResourceKey<Level> dimension, DimensionHologramProvider provider) {
        this.dimension = dimension;
        this.provider = provider;
    }

    public ResourceKey<Level> dimension() { return dimension; }
    public DimensionHologramProvider provider() { return provider; }

    public synchronized void setChunkVersions(it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap versions) {
        this.chunkVersions = versions;
    }

    public it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap getChunkVersions() {
        return this.chunkVersions;
    }

    public void setOverride(BlockPos pos, BlockState state) {
        overrides.put(pos.asLong(), state);
    }

    public void setOverrideRaw(long posAsLong, BlockState state) {
        overrides.put(posAsLong, state);
    }

    public void setRitualOverride(long posAsLong, BlockState state) {
        ritualOverrides.put(posAsLong, state);
    }

    public BlockState getOverride(BlockPos pos) {
        return overrides.get(pos.asLong());
    }

    public BlockState getOverrideRaw(long posAsLong) {
        return overrides.get(posAsLong);
    }

    public BlockState getRitualOverride(long posAsLong) {
        return ritualOverrides.get(posAsLong);
    }

    public boolean hasOverride(BlockPos pos) {
        return overrides.containsKey(pos.asLong());
    }

    public boolean hasOverrideRaw(long posAsLong) {
        return overrides.containsKey(posAsLong);
    }

    public boolean hasRitualOverride(long posAsLong) {
        return ritualOverrides.containsKey(posAsLong);
    }

    public void removeOverride(BlockPos pos) {
        overrides.remove(pos.asLong());
    }

    public Map<BlockPos, BlockState> getAllOverrides() {
        Map<BlockPos, BlockState> result = new HashMap<>();
        overrides.forEach((longPos, state) -> result.put(BlockPos.of(longPos), state));
        return result;
    }

    public void clearOverrides() {
        overrides.clear();
        ritualOverrides.clear();
    }

    public synchronized void putSection(int chunkX, int sectionY, int chunkZ, HologramSection section) {
        long key = (((long) chunkX & 0x3FFFFF) << 42) | (((long) sectionY & 0xFFFFF) << 22) | ((long) chunkZ & 0x3FFFFF);
        Long2ObjectOpenHashMap<HologramSection> copy = new Long2ObjectOpenHashMap<>(this.sections);
        copy.put(key, section);
        this.sections = copy;
        recalculateHighestY(copy);
    }

    public synchronized void putSections(Long2ObjectOpenHashMap<HologramSection> newSections) {
        Long2ObjectOpenHashMap<HologramSection> copy = new Long2ObjectOpenHashMap<>(this.sections);
        copy.putAll(newSections);
        this.sections = copy;
        recalculateHighestY(copy);
    }

    public BlockState getSectionBlock(int x, int y, int z) {
        Long2ObjectOpenHashMap<HologramSection> snap = this.sections;
        int cx = x >> 4, sy = y >> 4, cz = z >> 4;
        long key = (((long) cx & 0x3FFFFF) << 42) | (((long) sy & 0xFFFFF) << 22) | ((long) cz & 0x3FFFFF);
        HologramSection section = snap.get(key);
        if (section == null) return null;
        int lx = x & 15, ly = y & 15, lz = z & 15;
        return section.getBlockState(lx, ly, lz);
    }

    public net.minecraft.core.Holder<net.minecraft.world.level.biome.Biome> getSectionBiome(int x, int y, int z) {
        Long2ObjectOpenHashMap<HologramSection> snap = this.sections;
        int cx = x >> 4, sy = y >> 4, cz = z >> 4;
        long key = (((long) cx & 0x3FFFFF) << 42) | (((long) sy & 0xFFFFF) << 22) | ((long) cz & 0x3FFFFF);
        HologramSection section = snap.get(key);
        if (section == null) return null;
        int lx = x & 15, ly = y & 15, lz = z & 15;
        return section.getBiome(lx, ly, lz);
    }

    public synchronized void clearSections() {
        this.sections = new Long2ObjectOpenHashMap<>();
        this.chunkVersions = new it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap();
    }

    public Long2ObjectOpenHashMap<HologramSection> getSections() {
        return this.sections;
    }

    private volatile int cachedHighestY = 120;

    public synchronized void setSections(Long2ObjectOpenHashMap<HologramSection> sections) {
        this.sections = sections;
        recalculateHighestY(sections);
    }

    private void recalculateHighestY(Long2ObjectOpenHashMap<HologramSection> sectionsSnap) {
        int highestSectionY = -1;
        for (long key : sectionsSnap.keySet()) {
            int sy = (int) ((key >> 22) & 0xFFFFF);
            if ((sy & 0x80000) != 0) {
                sy |= 0xFFF00000;
            }
            if (sy > highestSectionY) {
                highestSectionY = sy;
            }
        }
        if (highestSectionY == -1) {
            this.cachedHighestY = 120;
        } else {
            this.cachedHighestY = (highestSectionY * 16) + 15;
        }
    }

    public int getHighestY() {
        return this.cachedHighestY;
    }

    public int getHighestBlockY(int x, int z) {
        int maxY = getHighestY();
        if (maxY <= 0) maxY = 319;
        for (int y = maxY; y >= -64; y--) {
            BlockState state = getSectionBlock(x, y, z);
            if (state != null && !state.isAir()) {
                return y;
            }
        }
        return -1;
    }

    public void clear() {
        clearOverrides();
        clearSections();
    }
}
