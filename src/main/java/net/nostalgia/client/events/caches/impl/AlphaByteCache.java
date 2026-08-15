package net.nostalgia.client.events.caches.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.nostalgia.client.events.caches.UniversalHologramCache;
import net.nostalgia.client.events.caches.providers.DimensionHologramProvider;
import net.nostalgia.client.events.caches.providers.HeightmapDiskCache;
import net.nostalgia.client.events.caches.providers.HeightmapExtractor;
import net.nostalgia.client.events.caches.providers.HologramDiskCache;
import net.nostalgia.client.events.echo.RitualVisualManager;
import net.nostalgia.network.C2SCacheReadyPayload;
import net.sha.api.SHAHologramManager;

public class AlphaByteCache {
  public static final Map<ChunkPos, byte[]> CHUNK_CACHE = new ConcurrentHashMap<>();
  public static volatile Map<Long, byte[]> FAST_CACHE = new ConcurrentHashMap<>();
  private static final Set<ChunkPos> DECORATED_CHUNKS = ConcurrentHashMap.newKeySet();
  public static volatile String cachedDimensionId = null;
  public static volatile int alphaCachedHighestY = 120;
  public static volatile boolean isGenerating = false;

  public AlphaByteCache() {
  }

  public static void recalculateAlphaHighestY() {
    int highest = -1;

    for (byte[] data : FAST_CACHE.values()) {
      for (int x = 0; x < 16; x++) {
        for (int z = 0; z < 16; z++) {
          for (int y = 127; y >= 0; y--) {
            int index = (x * 16 + z) * 128 + y;
            if (data[index] != 0 && data[index] != 18) {
              if (y > highest) {
                highest = y;
              }
              break;
            }
          }
        }
      }
    }

    if (highest == -1) {
      alphaCachedHighestY = 120;
    } else {
      alphaCachedHighestY = highest;
    }
  }

  public static void putChunkData(ChunkPos pos, byte[] data) {
    CHUNK_CACHE.put(pos, data);
  }

  public static void clear() {
    CHUNK_CACHE.clear();
    DECORATED_CHUNKS.clear();
    FAST_CACHE = new ConcurrentHashMap<>();
    isGenerating = false;
  }

  public static void generateCache(BlockPos center, long seed, String dimensionId) {
    if (cachedDimensionId != null && !cachedDimensionId.equals(dimensionId)) {
      clear();
      UniversalHologramCache.cacheGenerated = false;
    }

    cachedDimensionId = dimensionId;
    int radiusChunks = 19;
    int aX = center.getX() >> 4;
    int aZ = center.getZ() >> 4;
    ChunkPos centerChunk = new ChunkPos(aX, aZ);
    List<ChunkPos> toGen = new ArrayList<>();

    for (int cx = -radiusChunks; cx <= radiusChunks; cx++) {
      for (int cz = -radiusChunks; cz <= radiusChunks; cz++) {
        toGen.add(new ChunkPos(centerChunk.x() + cx, centerChunk.z() + cz));
      }
    }

    toGen.sort(Comparator.comparingDouble(cp -> Math.pow(cp.x() - centerChunk.x(), 2.0) + Math.pow(cp.z() - centerChunk.z(), 2.0)));
    if (CHUNK_CACHE.isEmpty()) {
      Map<ChunkPos, byte[]> diskCache = HologramDiskCache.loadAlphaCache(dimensionId, seed);
      if (diskCache != null && !diskCache.isEmpty()) {
        CHUNK_CACHE.putAll(diskCache);
        ConcurrentHashMap<Long, byte[]> fastCache = new ConcurrentHashMap<>(CHUNK_CACHE.size());
        CHUNK_CACHE.forEach((pos, data) -> fastCache.put(pos.pack(), data));
        FAST_CACHE = fastCache;
      }
    }

    List<ChunkPos> missing = toGen.stream().filter(cp -> !CHUNK_CACHE.containsKey(cp)).toList();
    if (missing.isEmpty()) {
      UniversalHologramCache.cacheGenerated = true;
      if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
        Minecraft.getInstance().execute(() -> {
          RitualVisualManager.onCacheGenerated();
          if (Minecraft.getInstance().getConnection() != null) {
            ClientPlayNetworking.send(new C2SCacheReadyPayload(true, new long[0], new long[0]));
          }
          SHAHologramManager.updateSpatialMap(UniversalHologramCache.INSTANCE);
          SHAHologramManager.markRadiusShellDirty(center, 0.0F, 320.0F);
        });
      }
      return;
    }

    CompletableFuture.runAsync(() -> {
      try {
        int sizeBefore = CHUNK_CACHE.size();
        DimensionHologramProvider providerx = UniversalHologramCache.getProvider(dimensionId);
        missing.parallelStream().forEach(cp -> {
          byte[] data = new byte[32768];
          providerx.generateChunkData(cp.x(), cp.z(), data, seed);
          putChunkData(cp, data);
        });
        ConcurrentHashMap<Long, byte[]> fastCache = new ConcurrentHashMap<>(CHUNK_CACHE.size());
        CHUNK_CACHE.forEach((pos, data) -> fastCache.put(pos.pack(), data));
        FAST_CACHE = fastCache;
        toGen.forEach(cp -> {
          if (DECORATED_CHUNKS.add(cp)) {
            providerx.decorateChunk(cp, seed);
          }
        });
        HologramFluidSimulator.simulate();
        recalculateAlphaHighestY();
        boolean cacheGrew = CHUNK_CACHE.size() > sizeBefore;
        if (cacheGrew) {
          HologramDiskCache.saveAlphaCache(dimensionId, seed, CHUNK_CACHE);
        }

        HeightmapDiskCache.mergeAndSave(dimensionId, HeightmapExtractor.extractFromAlphaCache(CHUNK_CACHE));
        UniversalHologramCache.cacheGenerated = true;
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
          Minecraft.getInstance().execute(() -> {
            RitualVisualManager.onCacheGenerated();
            if (Minecraft.getInstance().getConnection() != null) {
              ClientPlayNetworking.send(new C2SCacheReadyPayload(true, new long[0], new long[0]));
            }
            SHAHologramManager.updateSpatialMap(UniversalHologramCache.INSTANCE);
            SHAHologramManager.markRadiusShellDirty(center, 0.0F, 320.0F);
          });
        }
      } catch (Exception var13) {
        var13.printStackTrace();
      }
    });
  }

  public static byte getBlockSafely(int worldX, int worldY, int worldZ) {
    if (worldY >= 0 && worldY < 128) {
      int targetChunkX = worldX >> 4;
      int targetChunkZ = worldZ >> 4;
      ChunkPos chunkPos = new ChunkPos(targetChunkX, targetChunkZ);
      byte[] chunkData = FAST_CACHE.get(chunkPos.pack());
      if (chunkData != null) {
        int localX = worldX & 15;
        int localZ = worldZ & 15;
        int index = (localX * 16 + localZ) * 128 + worldY;
        return chunkData[index];
      } else {
        return 0;
      }
    } else {
      return 0;
    }
  }

  public static void setBlockSafely(int worldX, int worldY, int worldZ, byte blockId) {
    if (worldY >= 0 && worldY < 128) {
      int targetChunkX = worldX >> 4;
      int targetChunkZ = worldZ >> 4;
      ChunkPos chunkPos = new ChunkPos(targetChunkX, targetChunkZ);
      byte[] chunkData = CHUNK_CACHE.get(chunkPos);
      if (chunkData != null) {
        int localX = worldX & 15;
        int localZ = worldZ & 15;
        int index = (localX * 16 + localZ) * 128 + worldY;
        chunkData[index] = blockId;
        FAST_CACHE.put(chunkPos.pack(), chunkData);
      }
    }
  }

  public static int getHighestBlockY(int worldX, int worldZ) {
    int targetChunkX = worldX >> 4;
    int targetChunkZ = worldZ >> 4;
    ChunkPos chunkPos = new ChunkPos(targetChunkX, targetChunkZ);
    byte[] chunkData = FAST_CACHE.get(chunkPos.pack());
    if (chunkData != null) {
      int localX = worldX & 15;
      int localZ = worldZ & 15;

      for (int y = 127; y >= 0; y--) {
        int index = (localX * 16 + localZ) * 128 + y;
        if (chunkData[index] != 0) {
          return y + 1;
        }
      }
    }

    return -1;
  }

  public static void cancelGeneration() {
    isGenerating = false;
  }

  public static void generateCacheProgressive(BlockPos center, long seed, String dimensionId, BiConsumer<ChunkPos, byte[]> onChunkReady, Runnable onComplete) {
    if (cachedDimensionId != null && !cachedDimensionId.equals(dimensionId)) {
      clear();
      UniversalHologramCache.cacheGenerated = false;
    }

    cachedDimensionId = dimensionId;
    if (!isGenerating) {
      isGenerating = true;
      CompletableFuture.runAsync(() -> {
        int radiusChunks = 19;
        int aX = center.getX() >> 4;
        int aZ = center.getZ() >> 4;
        ChunkPos centerChunk = new ChunkPos(aX, aZ);
        List<ChunkPos> toGen = new ArrayList<>();

        for (int cx = -radiusChunks; cx <= radiusChunks; cx++) {
          for (int cz = -radiusChunks; cz <= radiusChunks; cz++) {
            if (cx * cx + cz * cz <= radiusChunks * radiusChunks) {
              toGen.add(new ChunkPos(centerChunk.x() + cx, centerChunk.z() + cz));
            }
          }
        }

        toGen.sort(Comparator.comparingDouble(cpx -> Math.pow(cpx.x() - centerChunk.x(), 2.0) + Math.pow(cpx.z() - centerChunk.z(), 2.0)));
        if (CHUNK_CACHE.isEmpty()) {
          Map<ChunkPos, byte[]> diskCache = HologramDiskCache.loadAlphaCache(dimensionId, seed);
          if (diskCache != null && !diskCache.isEmpty()) {
            CHUNK_CACHE.putAll(diskCache);
          }
        }

        try {
          int sizeBefore = CHUNK_CACHE.size();
          DimensionHologramProvider provider = UniversalHologramCache.getProvider(dimensionId);
          toGen.parallelStream().forEach(cpx -> {
            if (isGenerating) {
              if (!CHUNK_CACHE.containsKey(cpx)) {
                byte[] datax = new byte[32768];
                provider.generateChunkData(cpx.x(), cpx.z(), datax, seed);
                putChunkData(cpx, datax);
              }
            }
          });
          if (!isGenerating) {
            isGenerating = false;
            return;
          }

          ConcurrentHashMap<Long, byte[]> fastCache = new ConcurrentHashMap<>(CHUNK_CACHE.size());
          CHUNK_CACHE.forEach((pos, datax) -> fastCache.put(pos.pack(), datax));
          FAST_CACHE = fastCache;
          toGen.forEach(cpx -> {
            if (DECORATED_CHUNKS.add(cpx)) {
              provider.decorateChunk(cpx, seed);
            }
          });
          HologramFluidSimulator.simulate();
          recalculateAlphaHighestY();
          int BATCH_SIZE = 4;

          for (int i = 0; i < toGen.size() && isGenerating; i += BATCH_SIZE) {
            int end = Math.min(i + BATCH_SIZE, toGen.size());
            if (onChunkReady != null) {
              for (int j = i; j < end; j++) {
                ChunkPos cp = toGen.get(j);
                byte[] data = CHUNK_CACHE.get(cp);
                if (data != null) {
                  onChunkReady.accept(cp, data);
                }
              }
            }

            try {
              Thread.sleep(15L);
            } catch (InterruptedException var20) {
            }
          }

          boolean cacheGrew = CHUNK_CACHE.size() > sizeBefore;
          if (cacheGrew) {
            HologramDiskCache.saveAlphaCache(dimensionId, seed, CHUNK_CACHE);
          }

          HeightmapDiskCache.mergeAndSave(dimensionId, HeightmapExtractor.extractFromAlphaCache(CHUNK_CACHE));
          UniversalHologramCache.cacheGenerated = true;
          isGenerating = false;
          if (onComplete != null) {
            Minecraft.getInstance().execute(onComplete);
          }
        } catch (Exception var21) {
          isGenerating = false;
          var21.printStackTrace();
        }
      });
    }
  }
}
