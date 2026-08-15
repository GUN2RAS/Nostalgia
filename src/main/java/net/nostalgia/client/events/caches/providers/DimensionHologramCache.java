package net.nostalgia.client.events.caches.providers;

import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongIterator;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;

public class DimensionHologramCache {
  private final ResourceKey<Level> dimension;
  private final DimensionHologramProvider provider;
  private volatile Long2ObjectOpenHashMap<HologramSection> sections = new Long2ObjectOpenHashMap();
  private volatile Long2LongOpenHashMap chunkVersions = new Long2LongOpenHashMap();
  private final DeltaLayer deltaLayer = new DeltaLayer();
  private volatile int cachedHighestY = 120;

  public boolean isClientGenerated() {
    return this.provider.selfGenerated();
  }

  public int overrideCount() {
    return this.deltaLayer.size();
  }

  public DimensionHologramCache(ResourceKey<Level> dimension, DimensionHologramProvider provider) {
    this.dimension = dimension;
    this.provider = provider;
  }

  public ResourceKey<Level> dimension() {
    return this.dimension;
  }

  public DimensionHologramProvider provider() {
    return this.provider;
  }

  public DeltaLayer deltaLayer() {
    return this.deltaLayer;
  }

  public synchronized void setChunkVersions(Long2LongOpenHashMap versions) {
    this.chunkVersions = versions;
  }

  public Long2LongOpenHashMap getChunkVersions() {
    return this.chunkVersions;
  }

  public synchronized void putChunkVersions(long[] positions, long[] versions) {
    Long2LongOpenHashMap copy = new Long2LongOpenHashMap(this.chunkVersions);

    for (int i = 0; i < positions.length; i++) {
      copy.put(positions[i], versions[i]);
    }

    this.chunkVersions = copy;
  }

  public void setOverride(BlockPos pos, BlockState state) {
    this.deltaLayer.setOverride(pos, state);
  }

  public void setOverrideRaw(long posAsLong, BlockState state) {
    this.deltaLayer.setOverrideRaw(posAsLong, state);
  }

  public void setRitualOverride(long posAsLong, BlockState state) {
    this.deltaLayer.setRitualOverride(posAsLong, state);
  }

  public BlockState getOverride(BlockPos pos) {
    return this.deltaLayer.getOverride(pos);
  }

  public BlockState getOverrideRaw(long posAsLong) {
    return this.deltaLayer.getOverrideRaw(posAsLong);
  }

  public BlockState getRitualOverride(long posAsLong) {
    return this.deltaLayer.getRitualOverride(posAsLong);
  }

  public boolean hasOverride(BlockPos pos) {
    return this.deltaLayer.hasOverride(pos);
  }

  public boolean hasOverrideRaw(long posAsLong) {
    return this.deltaLayer.hasOverrideRaw(posAsLong);
  }

  public boolean hasRitualOverride(long posAsLong) {
    return this.deltaLayer.hasRitualOverride(posAsLong);
  }

  public void removeOverride(BlockPos pos) {
    this.deltaLayer.removeOverride(pos);
  }

  public Map<BlockPos, BlockState> getAllOverrides() {
    return this.deltaLayer.getAllOverrides();
  }

  public void clearOverrides() {
    this.deltaLayer.clear();
  }

  public synchronized void putSection(int chunkX, int sectionY, int chunkZ, HologramSection section) {
    long key = (chunkX & 4194303L) << 42 | (sectionY & 1048575L) << 22 | chunkZ & 4194303L;
    Long2ObjectOpenHashMap<HologramSection> copy = new Long2ObjectOpenHashMap(this.sections);
    copy.put(key, section);
    this.sections = copy;
    this.recalculateHighestY(copy);
  }

  public synchronized void putSections(Long2ObjectOpenHashMap<HologramSection> newSections) {
    Long2ObjectOpenHashMap<HologramSection> copy = new Long2ObjectOpenHashMap(this.sections);
    copy.putAll(newSections);
    this.sections = copy;
    this.recalculateHighestY(copy);
  }

  public BlockState getSectionBlock(int x, int y, int z) {
    Long2ObjectOpenHashMap<HologramSection> snap = this.sections;
    int cx = x >> 4;
    int sy = y >> 4;
    int cz = z >> 4;
    long key = (cx & 4194303L) << 42 | (sy & 1048575L) << 22 | cz & 4194303L;
    HologramSection section = (HologramSection)snap.get(key);
    if (section == null) {
      return null;
    } else {
      int lx = x & 15;
      int ly = y & 15;
      int lz = z & 15;
      return section.getBlockState(lx, ly, lz);
    }
  }

  public Holder<Biome> getSectionBiome(int x, int y, int z) {
    Long2ObjectOpenHashMap<HologramSection> snap = this.sections;
    int cx = x >> 4;
    int sy = y >> 4;
    int cz = z >> 4;
    long key = (cx & 4194303L) << 42 | (sy & 1048575L) << 22 | cz & 4194303L;
    HologramSection section = (HologramSection)snap.get(key);
    if (section == null) {
      return null;
    } else {
      int lx = x & 15;
      int ly = y & 15;
      int lz = z & 15;
      return section.getBiome(lx, ly, lz);
    }
  }

  public synchronized void clearSections() {
    this.sections = new Long2ObjectOpenHashMap();
    this.chunkVersions = new Long2LongOpenHashMap();
  }

  public Long2ObjectOpenHashMap<HologramSection> getSections() {
    return this.sections;
  }

  public synchronized void setSections(Long2ObjectOpenHashMap<HologramSection> sections) {
    this.sections = sections;
    this.recalculateHighestY(sections);
  }

  private void recalculateHighestY(Long2ObjectOpenHashMap<HologramSection> sectionsSnap) {
    int highestSectionY = -1;
    LongIterator var3 = sectionsSnap.keySet().iterator();

    while (var3.hasNext()) {
      long key = (Long)var3.next();
      int sy = (int)(key >> 22 & 1048575L);
      if ((sy & 524288) != 0) {
        sy |= -1048576;
      }

      if (sy > highestSectionY) {
        highestSectionY = sy;
      }
    }

    if (highestSectionY == -1) {
      this.cachedHighestY = 120;
    } else {
      this.cachedHighestY = highestSectionY * 16 + 15;
    }
  }

  public int getHighestY() {
    return this.cachedHighestY;
  }

  public int getHighestBlockY(int x, int z) {
    int maxY = this.getHighestY();
    if (maxY <= 0) {
      maxY = 319;
    }

    for (int y = maxY; y >= -64; y--) {
      BlockState state = this.getSectionBlock(x, y, z);
      if (state != null && !state.isAir()) {
        return y;
      }
    }

    return -1;
  }

  public void clear() {
    this.clearOverrides();
    this.clearSections();
  }
}
