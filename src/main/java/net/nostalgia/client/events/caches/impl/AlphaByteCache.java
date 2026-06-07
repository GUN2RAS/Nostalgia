package net.nostalgia.client.events.caches.impl;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.nostalgia.alphalogic.gen.AlphaLevelSource;
import net.nostalgia.client.events.caches.UniversalHologramCache;
import net.nostalgia.client.events.echo.RitualVisualManager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CompletableFuture;

public class AlphaByteCache {

    public static final Map<ChunkPos, byte[]> CHUNK_CACHE = new ConcurrentHashMap<>();
    public static volatile java.util.Map<Long, byte[]> FAST_CACHE = new ConcurrentHashMap<>();
    private static final java.util.Set<ChunkPos> DECORATED_CHUNKS = ConcurrentHashMap.newKeySet();

    public static volatile String cachedDimensionId = null;
    public static volatile int alphaCachedHighestY = 120;
    public static volatile boolean isGenerating = false;

    public static void recalculateAlphaHighestY() {
        int highest = -1;
        for (byte[] data : FAST_CACHE.values()) {
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    for (int y = 127; y >= 0; --y) {
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


        if (UniversalHologramCache.cacheGenerated || isGenerating) {
            if (net.fabricmc.loader.api.FabricLoader.getInstance().getEnvironmentType() == net.fabricmc.api.EnvType.CLIENT) {
                net.minecraft.client.Minecraft.getInstance().execute(() -> {
                    net.nostalgia.client.events.echo.RitualVisualManager.onCacheGenerated();
                    if (net.minecraft.client.Minecraft.getInstance().getConnection() != null) {
                        net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.send(new net.nostalgia.network.C2SCacheReadyPayload(true, new long[0], new long[0]));
                    }
                });
            }
            return;
        }
        isGenerating = true;
        CompletableFuture.runAsync(() -> {
            int radiusChunks = 300 / 16 + 1;
            int aX = (center.getX() + net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.offsetX()) >> 4;
            int aZ = (center.getZ() + net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.offsetZ()) >> 4;
            ChunkPos centerChunk = new ChunkPos(aX, aZ);

            java.util.List<ChunkPos> toGen = new java.util.ArrayList<>();
            for (int cx = -radiusChunks; cx <= radiusChunks; cx++) {
                for (int cz = -radiusChunks; cz <= radiusChunks; cz++) {
                    toGen.add(new ChunkPos(centerChunk.x() + cx, centerChunk.z() + cz));
                }
            }
            toGen.sort(java.util.Comparator.comparingDouble(cp ->
                Math.pow(cp.x() - centerChunk.x(), 2) + Math.pow(cp.z() - centerChunk.z(), 2)
            ));

            Map<ChunkPos, byte[]> diskCache = net.nostalgia.client.events.caches.providers.HologramDiskCache.loadAlphaCache(dimensionId, seed);
            boolean loadedFromDisk = false;
            if (diskCache != null && !diskCache.isEmpty()) {
                CHUNK_CACHE.putAll(diskCache);
                loadedFromDisk = true;
            }
            final boolean wasLoaded = loadedFromDisk;

            try {
                toGen.parallelStream().forEach(cp -> {
                    if (!CHUNK_CACHE.containsKey(cp)) {
                        byte[] data = new byte[32768];
                        net.nostalgia.client.events.caches.providers.DimensionHologramProvider provider = UniversalHologramCache.getProvider(dimensionId);
                        provider.generateChunkData(cp.x(), cp.z(), data, seed);
                        putChunkData(cp, data);
                    }
                });

                java.util.concurrent.ConcurrentHashMap<Long, byte[]> fastCache = new java.util.concurrent.ConcurrentHashMap<>(CHUNK_CACHE.size());
                CHUNK_CACHE.forEach((pos, data) -> fastCache.put(pos.pack(), data));
                FAST_CACHE = fastCache;

                net.nostalgia.client.events.caches.providers.DimensionHologramProvider provider = UniversalHologramCache.getProvider(dimensionId);
                toGen.forEach(cp -> {
                    if (DECORATED_CHUNKS.add(cp)) {
                        provider.decorateChunk(cp, seed);
                    }
                });

                recalculateAlphaHighestY();

                if (!wasLoaded) {
                    net.nostalgia.client.events.caches.providers.HologramDiskCache.saveAlphaCache(dimensionId, seed, CHUNK_CACHE);
                }

                UniversalHologramCache.cacheGenerated = true;
                isGenerating = false;
                if (net.fabricmc.loader.api.FabricLoader.getInstance().getEnvironmentType() == net.fabricmc.api.EnvType.CLIENT) {
                    net.minecraft.client.Minecraft.getInstance().execute(() -> {
                        net.nostalgia.client.events.echo.RitualVisualManager.onCacheGenerated();
                        if (net.minecraft.client.Minecraft.getInstance().getConnection() != null) {
                            net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.send(new net.nostalgia.network.C2SCacheReadyPayload(true, new long[0], new long[0]));
                        }
                        net.sha.api.SHAHologramManager.markRadiusShellDirty(center, 0.0f, 320.0f);
                    });
                }
            } catch (Exception e) {
                isGenerating = false;
                e.printStackTrace();
            }
        });
    }

    public static byte getBlockSafely(int worldX, int worldY, int worldZ) {
        if (worldY < 0 || worldY >= 128) return 0;
        int targetChunkX = worldX >> 4;
        int targetChunkZ = worldZ >> 4;
        ChunkPos chunkPos = new ChunkPos(targetChunkX, targetChunkZ);
        byte[] chunkData = FAST_CACHE.get(chunkPos.pack());
        if (chunkData != null) {
            int localX = worldX & 15;
            int localZ = worldZ & 15;
            int index = (localX * 16 + localZ) * 128 + worldY;
            return chunkData[index];
        }
        return 0;
    }

    public static void setBlockSafely(int worldX, int worldY, int worldZ, byte blockId) {
        if (worldY < 0 || worldY >= 128) return;
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

    public static int getHighestBlockY(int worldX, int worldZ) {
        int targetChunkX = worldX >> 4;
        int targetChunkZ = worldZ >> 4;
        ChunkPos chunkPos = new ChunkPos(targetChunkX, targetChunkZ);
        byte[] chunkData = FAST_CACHE.get(chunkPos.pack());
        if (chunkData != null) {
            int localX = worldX & 15;
            int localZ = worldZ & 15;
            for (int y = 127; y >= 0; --y) {
                int index = (localX * 16 + localZ) * 128 + y;
                if (chunkData[index] != 0 && chunkData[index] != 18) {
                    return y + 1;
                }
            }
        }
        return -1;
    }


}
