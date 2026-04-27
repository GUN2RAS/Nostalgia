package net.nostalgia.client.render;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.nostalgia.alphalogic.gen.AlphaLevelSource;
import net.nostalgia.client.ritual.RitualVisualManager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CompletableFuture;

public class NostalgiaChunkCache implements net.sha.api.HologramProvider {
    public static final NostalgiaChunkCache INSTANCE = new NostalgiaChunkCache();

    public static final Map<ChunkPos, byte[]> CHUNK_CACHE = new ConcurrentHashMap<>();
    public static volatile it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap<byte[]> FAST_CACHE = new it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap<>();
    private static final java.util.Set<ChunkPos> DECORATED_CHUNKS = ConcurrentHashMap.newKeySet();
    public static volatile boolean cacheGenerated = false;

    public static void putChunkData(ChunkPos pos, byte[] data) {
        CHUNK_CACHE.put(pos, data);
    }

    public static void clear() {
        CHUNK_CACHE.clear();
        DECORATED_CHUNKS.clear();
        FAST_CACHE = new it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap<>();
    }

    private static final Map<String, net.nostalgia.client.render.cache.DimensionHologramProvider> PROVIDERS = new ConcurrentHashMap<>();

    static {
        PROVIDERS.put("alpha", new net.nostalgia.client.render.cache.AlphaHologramProvider());
        PROVIDERS.put("rd", new net.nostalgia.client.render.cache.RDHologramProvider());
        PROVIDERS.put("nostalgia:rd_132211", new net.nostalgia.client.render.cache.RDHologramProvider());
        PROVIDERS.put("overworld", new net.nostalgia.client.render.cache.EmptyHologramProvider());
    }

    public static net.nostalgia.client.render.cache.DimensionHologramProvider getProvider(String dimensionId) {
        if (dimensionId == null) return PROVIDERS.get("alpha");
        return PROVIDERS.getOrDefault(dimensionId, PROVIDERS.get("alpha"));
    }

    public static void generateCache(BlockPos center, long seed, String dimensionId) {
        cacheGenerated = false;

        CompletableFuture.runAsync(() -> {
            int radiusChunks = 300 / 16 + 1;
            int aX = (center.getX() + net.nostalgia.alphalogic.ritual.RitualActiveState.offsetX) >> 4;
            int aZ = (center.getZ() + net.nostalgia.alphalogic.ritual.RitualActiveState.offsetZ) >> 4;
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

            try {

                toGen.parallelStream().forEach(cp -> {
                    if (!CHUNK_CACHE.containsKey(cp)) {
                        byte[] data = new byte[32768];
                        net.nostalgia.client.render.cache.DimensionHologramProvider provider = getProvider(dimensionId);
                        provider.generateChunkData(cp.x(), cp.z(), data, seed);
                        NostalgiaChunkCache.putChunkData(cp, data);
                    }
                });

                net.nostalgia.client.render.cache.DimensionHologramProvider provider = getProvider(dimensionId);
                toGen.forEach(cp -> {
                    if (DECORATED_CHUNKS.add(cp)) {
                        provider.decorateChunk(cp, seed);
                    }
                });

                it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap<byte[]> fastCache = new it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap<>(CHUNK_CACHE.size());
                CHUNK_CACHE.forEach((pos, data) -> fastCache.put(pos.pack(), data));
                FAST_CACHE = fastCache;

                cacheGenerated = true;
                if (net.fabricmc.loader.api.FabricLoader.getInstance().getEnvironmentType() == net.fabricmc.api.EnvType.CLIENT) {
                    net.minecraft.client.Minecraft.getInstance().execute(() -> {
                        net.nostalgia.client.ritual.RitualVisualManager.onCacheGenerated();
                        if (net.minecraft.client.Minecraft.getInstance().getConnection() != null) {
                            net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.send(new net.nostalgia.network.C2SCacheReadyPayload());
                        }
                    });
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static byte getBlockSafely(int worldX, int worldY, int worldZ) {
        if (worldY < 0 || worldY >= 128) return 0;
        int targetChunkX = worldX >> 4;
        int targetChunkZ = worldZ >> 4;
        ChunkPos chunkPos = new ChunkPos(targetChunkX, targetChunkZ);
        byte[] chunkData = CHUNK_CACHE.get(chunkPos);
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
        }
    }

    public static int getHighestBlockY(int worldX, int worldZ) {
        int targetChunkX = worldX >> 4;
        int targetChunkZ = worldZ >> 4;
        ChunkPos chunkPos = new ChunkPos(targetChunkX, targetChunkZ);
        byte[] chunkData = CHUNK_CACHE.get(chunkPos);
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

    public static BlockState getPredictedAlphaBlock(int worldX, int y, int worldZ) {
        return getPredictedAlphaBlock(worldX, y, worldZ, false);
    }

    public static BlockState getPredictedAlphaBlock(int worldX, int y, int worldZ, boolean ignoreRadius) {
        if (!net.nostalgia.client.ritual.RitualVisualManager.isTransitioning && !net.nostalgia.client.render.PortalSkyRenderer.isDebugging) return null;

        boolean inNew = net.nostalgia.client.ritual.RitualVisualManager.isInNewDimension();
        if (inNew) return null;

        long rawPos;
        rawPos = BlockPos.asLong(worldX, y, worldZ);

        if (!cacheGenerated) {
            return null;
        }

        if (!ignoreRadius) {
            BlockPos center;
            if (net.nostalgia.client.render.PortalSkyRenderer.isDebugging) {
                center = net.nostalgia.client.render.PortalSkyRenderer.debugCenter;
            } else {
                center = net.nostalgia.alphalogic.ritual.RitualActiveState.ritualCenter;
            }
            if (center == null) return null;

            float currentRadius = net.nostalgia.client.ritual.RitualVisualManager.getAlphaRadius();
            if (currentRadius <= 0.01f) {
                return null;
            }

            double trueCenterX = center.getX();
            double trueCenterY = center.getY();
            double trueCenterZ = center.getZ();

            double dx = worldX - trueCenterX;
            double dy = y - trueCenterY;
            double dz = worldZ - trueCenterZ;

            double noise = (Math.sin(worldX * 12.9898 + y * 78.233 + worldZ * 45.164) * 43758.5453) % 1.0;

            boolean isSky = net.nostalgia.client.render.PortalSkyRenderer.isDebugging;
            double dist;
            if (isSky) {
                dist = Math.sqrt(dx*dx + dz*dz) + (noise * 2.0);
                if (dist > 288.0f) {
                    return null;
                }
            } else {
                dist = Math.sqrt(dx*dx + dy*dy + dz*dz) + (noise * 2.0);
                if (dist > currentRadius) {
                    return null;
                }
            }
        }

        if (net.nostalgia.client.ritual.ClientVirtualBlockCache.has(rawPos)) {
            return net.nostalgia.client.ritual.ClientVirtualBlockCache.get(rawPos);
        }

        int sourceX = worldX + net.nostalgia.alphalogic.ritual.RitualActiveState.offsetX;
        int sourceZ = worldZ + net.nostalgia.alphalogic.ritual.RitualActiveState.offsetZ;
        
        boolean isSkyInverted = net.nostalgia.client.render.PortalSkyRenderer.isDebuggingInverted;
        int sourceY;
        if (isSkyInverted) {
            if (y <= 165) return null;
            sourceY = 320 - y;
        } else {
            sourceY = y - net.nostalgia.alphalogic.ritual.RitualActiveState.yOffset;
        }

        if ("overworld".equals(net.nostalgia.client.ritual.RitualVisualManager.targetDimension)) {
            net.minecraft.world.level.block.state.BlockState state = net.nostalgia.client.render.cache.OverworldHologramCache.getBlockState(sourceX, sourceY, sourceZ);
            if (state != null && !state.isAir()) {
                return state;
            }
            return isSkyInverted ? null : net.minecraft.world.level.block.Blocks.AIR.defaultBlockState();
        }

        int chunkX = sourceX >> 4;
        int chunkZ = sourceZ >> 4;

        long chunkHash = ChunkPos.pack(chunkX, chunkZ);
        byte[] chunkData = FAST_CACHE.get(chunkHash);

        if (chunkData != null) {
            if (sourceY >= 0 && sourceY < 128) {
                int localX = sourceX & 15;
                int localZ = sourceZ & 15;
                int index = (localX * 16 + localZ) * 128 + sourceY;
                byte blockId = chunkData[index];
                if (blockId == 0) {
                    return isSkyInverted ? null : net.minecraft.world.level.block.Blocks.AIR.defaultBlockState();
                }
                net.nostalgia.client.render.cache.DimensionHologramProvider provider = getProvider(net.nostalgia.client.ritual.RitualVisualManager.targetDimension);
                return provider.getBlockState(blockId, isSkyInverted);
            } else {
                return isSkyInverted ? null : net.minecraft.world.level.block.Blocks.AIR.defaultBlockState();
            }
        }
        return null;
    }

    @Override
    public boolean isActive() {
        if (net.nostalgia.client.render.PortalSkyRenderer.isDebugging) {
            return net.nostalgia.client.render.PortalSkyRenderer.islandVisible;
        }
        return net.nostalgia.client.ritual.RitualVisualManager.isTransitioning;
    }

    @Override
    public boolean providesCollision() {
        return true;
    }

    @Override
    public net.sha.api.HologramBounds getBounds() {

        return null;
    }

    @Override
    public BlockState getSpoofedBlock(int worldX, int y, int worldZ) {
        return getPredictedAlphaBlock(worldX, y, worldZ);
    }
}
