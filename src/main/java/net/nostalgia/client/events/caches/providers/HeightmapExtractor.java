package net.nostalgia.client.events.caches.providers;

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongIterator;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

public class HeightmapExtractor {
  private static final AlphaHologramProvider ALPHA_PROVIDER = new AlphaHologramProvider();

  public HeightmapExtractor() {
  }

  public static HeightmapDiskCache.HeightmapData extractFromAlphaChunk(ChunkPos cp, byte[] chunkData) {
    HeightmapDiskCache.HeightmapData data = HeightmapDiskCache.HeightmapData.empty();
    int chunkWorldX = cp.x() << 4;
    int chunkWorldZ = cp.z() << 4;

    for (int lx = 0; lx < 16; lx++) {
      for (int lz = 0; lz < 16; lz++) {
        int worldX = chunkWorldX + lx;
        int worldZ = chunkWorldZ + lz;
        int packedXZ = HeightmapDiskCache.packXZ(worldX, worldZ);
        int highY = 64;

        for (int y = 127; y >= 0; y--) {
          if (chunkData[(lx * 16 + lz) * 128 + y] != 0) {
            highY = y + 1;
            break;
          }
        }

        int blockY = Math.max(0, highY - 1);
        byte blockId = chunkData[(lx * 16 + lz) * 128 + blockY];
        BlockState state = ALPHA_PROVIDER.getBlockState(blockId, false);
        int color = mapColorToARGB(state);
        int stateId = state != null ? Block.getId(state) : 0;
        data.put(packedXZ, highY, color, stateId);
      }
    }

    return data;
  }

  public static HeightmapDiskCache.HeightmapData extractFromAlphaCache(Map<ChunkPos, byte[]> cache) {
    HeightmapDiskCache.HeightmapData data = HeightmapDiskCache.HeightmapData.empty();

    for (Entry<ChunkPos, byte[]> entry : cache.entrySet()) {
      HeightmapDiskCache.HeightmapData chunkData = extractFromAlphaChunk(entry.getKey(), entry.getValue());
      data.mergeFrom(chunkData);
    }

    return data;
  }

  public static HeightmapDiskCache.HeightmapData extractFromSections(Long2ObjectOpenHashMap<HologramSection> sections, int centerX, int centerZ, int radius) {
    HeightmapDiskCache.HeightmapData data = HeightmapDiskCache.HeightmapData.empty();
    int maxSectionY = -100;
    LongIterator scanTop = sections.keySet().iterator();

    while (scanTop.hasNext()) {
      long key = (Long)scanTop.next();
      int sy = (int)(key >> 22 & 1048575L);
      if (sy > 524287) {
        sy -= 1048576;
      }

      if (sy > maxSectionY) {
        maxSectionY = sy;
      }
    }

    int scanTopx = (maxSectionY + 1) * 16 - 1;
    if (scanTopx < 0) {
      scanTopx = 319;
    }

    int minX = centerX - radius;
    int maxX = centerX + radius;
    int minZ = centerZ - radius;
    int maxZ = centerZ + radius;

    for (int worldX = minX; worldX < maxX; worldX++) {
      for (int worldZ = minZ; worldZ < maxZ; worldZ++) {
        int packedXZ = HeightmapDiskCache.packXZ(worldX, worldZ);
        int highY = -1;
        BlockState topState = null;

        for (int y = scanTopx; y >= -64; y--) {
          int cx = worldX >> 4;
          int syx = y >> 4;
          int cz = worldZ >> 4;
          long keyx = (cx & 4194303L) << 42 | (syx & 1048575L) << 22 | cz & 4194303L;
          HologramSection section = (HologramSection)sections.get(keyx);
          if (section != null) {
            BlockState state = section.getBlockState(worldX & 15, y & 15, worldZ & 15);
            if (state != null && !state.isAir()) {
              highY = y;
              topState = state;
              break;
            }
          }
        }

        if (highY < -64) {
          highY = 63;
        }

        int color = mapColorToARGB(topState);
        int stateId = topState != null ? Block.getId(topState) : 0;
        data.put(packedXZ, highY, color, stateId);
      }
    }

    return data;
  }

  private static int mapColorToARGB(BlockState state) {
    if (state != null && !state.isAir()) {
      try {
        MapColor mapColor = state.getMapColor(null, BlockPos.ZERO);
        return mapColor != null && mapColor != MapColor.NONE ? 0xFF000000 | mapColor.col : -9408400;
      } catch (Exception var2) {
        return -9408400;
      }
    } else {
      return 0;
    }
  }
}
