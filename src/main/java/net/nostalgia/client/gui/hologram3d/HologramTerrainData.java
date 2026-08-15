package net.nostalgia.client.gui.hologram3d;

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongIterator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.nostalgia.client.events.caches.providers.AlphaHologramProvider;
import net.nostalgia.client.events.caches.providers.DeltaLayer;
import net.nostalgia.client.events.caches.providers.DimensionHologramCache;
import net.nostalgia.client.events.caches.providers.DimensionHologramRegistry;
import net.nostalgia.client.events.caches.providers.HeightmapDiskCache;
import net.nostalgia.client.events.caches.providers.HologramDiskCache;
import net.nostalgia.client.events.caches.providers.HologramSection;

public class HologramTerrainData {
  private static final AlphaHologramProvider ALPHA_PROVIDER = new AlphaHologramProvider();
  private final int centerX;
  private final int centerZ;
  private final int radius;
  private final int diameter;
  private final int[] heightmap;
  private final int[] colormap;
  private final int[] stateIdMap;
  private boolean ready;

  public HologramTerrainData(int centerX, int centerZ, int radius) {
    this.centerX = centerX;
    this.centerZ = centerZ;
    this.radius = radius;
    this.diameter = radius * 2;
    int size = this.diameter * this.diameter;
    this.heightmap = new int[size];
    this.colormap = new int[size];
    this.stateIdMap = new int[size];
    this.ready = false;
  }

  public void extractFromAlphaCache(String dimensionId) {
    Map<ChunkPos, byte[]> diskData = HologramDiskCache.loadAlphaCache(dimensionId, 0L);
    if (diskData != null && !diskData.isEmpty()) {
      Map<Long, byte[]> fastLookup = new HashMap<>();

      for (Entry<ChunkPos, byte[]> entry : diskData.entrySet()) {
        fastLookup.put(entry.getKey().pack(), entry.getValue());
      }

      for (int dx = 0; dx < this.diameter; dx++) {
        for (int dz = 0; dz < this.diameter; dz++) {
          int worldX = this.centerX - this.radius + dx;
          int worldZ = this.centerZ - this.radius + dz;
          int idx = dx * this.diameter + dz;
          int chunkX = worldX >> 4;
          int chunkZ = worldZ >> 4;
          byte[] chunkData = fastLookup.get(new ChunkPos(chunkX, chunkZ).pack());
          if (chunkData == null) {
            this.heightmap[idx] = 64;
            this.colormap[idx] = 0;
            this.stateIdMap[idx] = 0;
          } else {
            int localX = worldX & 15;
            int localZ = worldZ & 15;
            int highY = 64;

            for (int y = 127; y >= 0; y--) {
              if (chunkData[(localX * 16 + localZ) * 128 + y] != 0) {
                highY = y + 1;
                break;
              }
            }

            this.heightmap[idx] = highY;
            int blockY = Math.max(0, highY - 1);
            byte blockId = chunkData[(localX * 16 + localZ) * 128 + blockY];
            BlockState state = ALPHA_PROVIDER.getBlockState(blockId, false);
            this.colormap[idx] = alphaBlockColor(state);
            this.stateIdMap[idx] = state != null ? Block.getId(state) : 0;
          }
        }
      }

      this.ready = true;
    } else {
      this.ready = false;
    }
  }

  public void extractFromDimensionCache(String dimensionId) {
    Long2ObjectOpenHashMap<HologramSection> sections = new Long2ObjectOpenHashMap<>();

    HologramDiskCache.DimensionCacheResult diskResult = HologramDiskCache.loadDimensionCache(dimensionId);
    if (diskResult != null && diskResult.sections() != null && !diskResult.sections().isEmpty()) {
      sections.putAll(diskResult.sections());
    }

    DimensionHologramCache memCache = DimensionHologramRegistry.getByName(dimensionId);
    if (memCache != null) {
      Long2ObjectOpenHashMap<HologramSection> memSections = memCache.getSections();
      if (memSections != null && !memSections.isEmpty()) {
        sections.putAll(memSections);
      }
    }

    if (sections != null && !sections.isEmpty()) {
      int maxSectionY = -100;
      LongIterator var24 = sections.keySet().iterator();

      while (var24.hasNext()) {
        long key = (Long)var24.next();
        int sy = (int)(key >> 22 & 1048575L);
        if (sy > 524287) {
          sy -= 1048576;
        }

        if (sy > maxSectionY) {
          maxSectionY = sy;
        }
      }

      int scanTop = (maxSectionY + 1) * 16 - 1;
      if (scanTop < 0) {
        int var26 = 319;
      }

      int minSectionY = -4;

      for (int dx = 0; dx < this.diameter; dx++) {
        for (int dz = 0; dz < this.diameter; dz++) {
          int worldX = this.centerX - this.radius + dx;
          int worldZ = this.centerZ - this.radius + dz;
          int idx = dx * this.diameter + dz;
          int highY = -1;
          BlockState topState = null;
          int cx = worldX >> 4;
          int cz = worldZ >> 4;

          for (int syx = maxSectionY; syx >= minSectionY; syx--) {
            long keyx = (cx & 4194303L) << 42 | (syx & 1048575L) << 22 | cz & 4194303L;
            HologramSection section = (HologramSection)sections.get(keyx);
            if (section != null) {
              boolean found = false;

              for (int ly = 15; ly >= 0; ly--) {
                BlockState state = section.getBlockState(worldX & 15, ly, worldZ & 15);
                if (state != null && !state.isAir()) {
                  highY = (syx << 4) + ly;
                  topState = state;
                  found = true;
                  break;
                }
              }

              if (found) {
                break;
              }
            }
          }

          if (highY < -64) {
            highY = 63;
          }

          this.heightmap[idx] = highY;
          this.colormap[idx] = blockStateColor(topState);
          this.stateIdMap[idx] = topState != null ? Block.getId(topState) : 0;
        }
      }

      this.ready = true;
    } else {
      this.ready = false;
    }
  }

  private static int alphaBlockColor(BlockState state) {
    return state != null && !state.isAir() ? mapColorToARGB(state) : 0;
  }

  private static int blockStateColor(BlockState state) {
    return state != null && !state.isAir() ? mapColorToARGB(state) : 0;
  }

  private static int mapColorToARGB(BlockState state) {
    try {
      MapColor mapColor = state.getMapColor(null, BlockPos.ZERO);
      return mapColor != null && mapColor != MapColor.NONE ? 0xFF000000 | mapColor.col : -9408400;
    } catch (Exception var2) {
      return -9408400;
    }
  }

  public BlockState getBlockState(int dx, int dz) {
    if (dx >= 0 && dx < this.diameter && dz >= 0 && dz < this.diameter) {
      int stateId = this.stateIdMap[dx * this.diameter + dz];
      return stateId == 0 ? null : Block.stateById(stateId);
    } else {
      return null;
    }
  }

  public int getStateId(int dx, int dz) {
    return dx >= 0 && dx < this.diameter && dz >= 0 && dz < this.diameter ? this.stateIdMap[dx * this.diameter + dz] : 0;
  }

  public int getHeight(int dx, int dz) {
    return dx >= 0 && dx < this.diameter && dz >= 0 && dz < this.diameter ? this.heightmap[dx * this.diameter + dz] : 64;
  }

  public int getColor(int dx, int dz) {
    return dx >= 0 && dx < this.diameter && dz >= 0 && dz < this.diameter ? this.colormap[dx * this.diameter + dz] : 0;
  }

  public void applyDeltas(String dimensionId) {
    DimensionHologramCache cache = DimensionHologramRegistry.getByName(dimensionId);
    if (cache != null) {
      DeltaLayer delta = cache.deltaLayer();
      if (delta.size() != 0) {
        Map<BlockPos, BlockState> overrides = delta.getAllOverrides();

        for (Entry<BlockPos, BlockState> entry : overrides.entrySet()) {
          BlockPos pos = entry.getKey();
          BlockState state = entry.getValue();
          int dx = pos.getX() - (this.centerX - this.radius);
          int dz = pos.getZ() - (this.centerZ - this.radius);
          if (dx >= 0 && dx < this.diameter && dz >= 0 && dz < this.diameter) {
            int idx = dx * this.diameter + dz;
            int y = pos.getY();
            if (state != null && !state.isAir()) {
              if (y >= this.heightmap[idx]) {
                this.heightmap[idx] = y;
                this.colormap[idx] = mapColorToARGB(state);
                this.stateIdMap[idx] = Block.getId(state);
              }
            } else if (y >= this.heightmap[idx]) {
              this.heightmap[idx] = y - 1;
              this.colormap[idx] = -9408400;
              this.stateIdMap[idx] = 0;
            }
          }
        }
      }
    }
  }

  public boolean isReady() {
    return this.ready;
  }

  public int getCenterX() {
    return this.centerX;
  }

  public int getCenterZ() {
    return this.centerZ;
  }

  public int getRadius() {
    return this.radius;
  }

  public int getDiameter() {
    return this.diameter;
  }

  public void markReady() {
    this.ready = true;
  }

  public void loadFromHeightmap(String dimensionId) {
    HeightmapDiskCache.HeightmapData data = HeightmapDiskCache.load(dimensionId);
    if (data != null && data.size() != 0) {
      for (int dx = 0; dx < this.diameter; dx++) {
        for (int dz = 0; dz < this.diameter; dz++) {
          int worldX = this.centerX - this.radius + dx;
          int worldZ = this.centerZ - this.radius + dz;
          int packed = HeightmapDiskCache.packXZ(worldX, worldZ);
          int idx = dx * this.diameter + dz;
          if (data.has(packed)) {
            this.heightmap[idx] = data.getHeight(packed);
            this.colormap[idx] = data.getColor(packed);
            this.stateIdMap[idx] = data.getStateId(packed);
          } else {
            this.heightmap[idx] = 0;
            this.colormap[idx] = 0;
            this.stateIdMap[idx] = 0;
          }
        }
      }

      this.ready = true;
    } else {
      this.ready = false;
    }
  }

  public void updateChunkRegion(ChunkPos cp, byte[] chunkData) {
    int chunkWorldX = cp.x() << 4;
    int chunkWorldZ = cp.z() << 4;

    for (int lx = 0; lx < 16; lx++) {
      for (int lz = 0; lz < 16; lz++) {
        int worldX = chunkWorldX + lx;
        int worldZ = chunkWorldZ + lz;
        int dx = worldX - (this.centerX - this.radius);
        int dz = worldZ - (this.centerZ - this.radius);
        if (dx >= 0 && dx < this.diameter && dz >= 0 && dz < this.diameter) {
          int idx = dx * this.diameter + dz;
          int highY = 64;

          for (int y = 127; y >= 0; y--) {
            if (chunkData[(lx * 16 + lz) * 128 + y] != 0) {
              highY = y + 1;
              break;
            }
          }

          this.heightmap[idx] = highY;
          int blockY = Math.max(0, highY - 1);
          byte blockId = chunkData[(lx * 16 + lz) * 128 + blockY];
          BlockState state = ALPHA_PROVIDER.getBlockState(blockId, false);
          this.colormap[idx] = alphaBlockColor(state);
          this.stateIdMap[idx] = state != null ? Block.getId(state) : 0;
        }
      }
    }

    this.ready = true;
  }
}
