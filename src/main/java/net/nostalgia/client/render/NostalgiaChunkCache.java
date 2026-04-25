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

    private static class ThreadState {
        long seed;
        AlphaLevelSource source;
        ThreadState(long s, AlphaLevelSource src) { this.seed = s; this.source = src; }
    }
    private static final ThreadLocal<ThreadState> THREAD_LOCAL_SOURCE = new ThreadLocal<>();

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

                        if ("rd".equals(dimensionId)) {

                            for (int x = 0; x < 16; x++) {
                                int wx = cp.x() * 16 + x;
                                for (int z = 0; z < 16; z++) {
                                    int wz = cp.z() * 16 + z;
                                    if (wx < 0 || wx >= 256 || wz < 0 || wz >= 256) {
                                        continue;
                                    }
                                    for (int y = 0; y <= 42; y++) {
                                        int index = (x * 16 + z) * 128 + y;
                                        if (y == 0) data[index] = 7;
                                        else if (y < 42) data[index] = 1;
                                        else data[index] = 2;
                                    }
                                }
                            }
                        } else {
                            ThreadState state = THREAD_LOCAL_SOURCE.get();
                            if (state == null || state.seed != seed) {
                                state = new ThreadState(seed, new AlphaLevelSource(seed));
                                THREAD_LOCAL_SOURCE.set(state);
                            }
                            state.source.provideChunk(cp.x(), cp.z(), data);
                        }

                        NostalgiaChunkCache.putChunkData(cp, data);
                    }
                });

                if (!"rd".equals(dimensionId)) {
                    toGen.forEach(cp -> {
                        if (DECORATED_CHUNKS.add(cp)) {
                            net.nostalgia.alphalogic.gen.AlphaChunkDecorator.decorate(cp, seed);
                        }
                    });
                }

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

        if ("overworld".equals(net.nostalgia.client.ritual.RitualVisualManager.targetDimension)) {

            return net.minecraft.world.level.block.Blocks.AIR.defaultBlockState();
        }

        if ("rd".equals(net.nostalgia.client.ritual.RitualVisualManager.targetDimension)
            || "nostalgia:rd_132211".equals(net.nostalgia.client.ritual.RitualVisualManager.targetDimension)) {
            int rdX = worldX + net.nostalgia.alphalogic.ritual.RitualActiveState.offsetX;
            int rdZ = worldZ + net.nostalgia.alphalogic.ritual.RitualActiveState.offsetZ;
            int rdY = y - net.nostalgia.alphalogic.ritual.RitualActiveState.yOffset;

            if (rdX < 0 || rdX >= net.nostalgia.world.gen.RD132211ChunkGenerator.WORLD_SIZE
                || rdZ < 0 || rdZ >= net.nostalgia.world.gen.RD132211ChunkGenerator.WORLD_SIZE) {
                return net.minecraft.world.level.block.Blocks.AIR.defaultBlockState();
            }
            int surfaceY = net.nostalgia.world.gen.RD132211ChunkGenerator.SURFACE_Y;
            if (rdY < 0 || rdY > surfaceY) {
                return net.minecraft.world.level.block.Blocks.AIR.defaultBlockState();
            }
            if (rdY == surfaceY) {
                return net.nostalgia.block.ModBlocks.RD_GRASS.defaultBlockState();
            }
            return net.nostalgia.block.ModBlocks.RD_STONE.defaultBlockState();
        }

        int alphaX, alphaZ, alphaY;

        alphaX = worldX + net.nostalgia.alphalogic.ritual.RitualActiveState.offsetX;
        alphaZ = worldZ + net.nostalgia.alphalogic.ritual.RitualActiveState.offsetZ;

        boolean isSkyInverted = net.nostalgia.client.render.PortalSkyRenderer.isDebuggingInverted;
        if (isSkyInverted) {
            if (y <= 165) return null;
            alphaY = 320 - y;
        } else {
            alphaY = y - net.nostalgia.alphalogic.ritual.RitualActiveState.yOffset;
        }

        int alphaChunkX = alphaX >> 4;
        int alphaChunkZ = alphaZ >> 4;

        long chunkHash = ChunkPos.pack(alphaChunkX, alphaChunkZ);
        byte[] chunkData = FAST_CACHE.get(chunkHash);

        if (chunkData != null) {
            if (alphaY >= 0 && alphaY < 128) {
                int localX = alphaX & 15;
                int localZ = alphaZ & 15;
                int index = (localX * 16 + localZ) * 128 + alphaY;
                byte blockId = chunkData[index];
                if (blockId == 0) {
                    return isSkyInverted ? null : net.minecraft.world.level.block.Blocks.AIR.defaultBlockState();
                }
                return getStateForAlphaId(blockId, isSkyInverted);
            } else {
                return isSkyInverted ? null : net.minecraft.world.level.block.Blocks.AIR.defaultBlockState();
            }
        } else {
            if (!isSkyInverted) {

            }
        }
        return null;
    }

    private static BlockState getStateForAlphaId(byte id, boolean isSkyInverted) {
        if (id == 0) return null;
        if (id == 1) return net.nostalgia.block.AlphaBlocks.ALPHA_STONE.defaultBlockState();
        if (id == 2) {
            return isSkyInverted ? net.nostalgia.block.AlphaBlocks.ALPHA_GRASS_BLOCK_FLIPPED.defaultBlockState() : net.nostalgia.block.AlphaBlocks.ALPHA_GRASS_BLOCK.defaultBlockState();
        }
        if (id == 3) return net.nostalgia.block.AlphaBlocks.ALPHA_DIRT.defaultBlockState();
        if (id == 4) return net.nostalgia.block.AlphaBlocks.ALPHA_COBBLESTONE.defaultBlockState();
        if (id == 7) return net.nostalgia.block.AlphaBlocks.ALPHA_BEDROCK.defaultBlockState();
        if (id == 8 || id == 9) return Blocks.WATER.defaultBlockState();
        if (id == 10 || id == 11) return Blocks.LAVA.defaultBlockState();
        if (id == 12) return net.nostalgia.block.AlphaBlocks.ALPHA_SAND.defaultBlockState();
        if (id == 13) return net.nostalgia.block.AlphaBlocks.ALPHA_GRAVEL.defaultBlockState();
        if (id == 14) return net.nostalgia.block.AlphaBlocks.ALPHA_GOLD_ORE.defaultBlockState();
        if (id == 15) return net.nostalgia.block.AlphaBlocks.ALPHA_IRON_ORE.defaultBlockState();
        if (id == 16) return net.nostalgia.block.AlphaBlocks.ALPHA_COAL_ORE.defaultBlockState();
        if (id == 17) return net.nostalgia.block.AlphaBlocks.ALPHA_OAK_LOG.defaultBlockState();
        if (id == 18) return net.nostalgia.block.AlphaBlocks.ALPHA_LEAVES.defaultBlockState();
        if (id == 20) return net.nostalgia.block.AlphaBlocks.ALPHA_GLASS.defaultBlockState();
        if (id == 37) return isSkyInverted ? net.nostalgia.block.AlphaBlocks.ALPHA_YELLOW_FLOWER_FLIPPED.defaultBlockState() : net.nostalgia.block.AlphaBlocks.ALPHA_YELLOW_FLOWER.defaultBlockState();
        if (id == 38) return isSkyInverted ? net.nostalgia.block.AlphaBlocks.ALPHA_RED_FLOWER_FLIPPED.defaultBlockState() : net.nostalgia.block.AlphaBlocks.ALPHA_RED_FLOWER.defaultBlockState();
        if (id == 56) return net.nostalgia.block.AlphaBlocks.ALPHA_DIAMOND_ORE.defaultBlockState();
        if (id == 73) return net.minecraft.world.level.block.Blocks.REDSTONE_ORE.defaultBlockState();
        if (id == 78) return Blocks.SNOW.defaultBlockState();
        if (id == 79) return net.nostalgia.block.AlphaBlocks.ALPHA_ICE.defaultBlockState();
        if (id == 81) return isSkyInverted ? net.nostalgia.block.AlphaBlocks.ALPHA_CACTUS_FLIPPED.defaultBlockState() : net.nostalgia.block.AlphaBlocks.ALPHA_CACTUS.defaultBlockState();
        if (id == 82) return net.nostalgia.block.AlphaBlocks.ALPHA_CLAY.defaultBlockState();
        if (id == 83) return isSkyInverted ? net.nostalgia.block.AlphaBlocks.ALPHA_SUGAR_CANE_FLIPPED.defaultBlockState() : net.nostalgia.block.AlphaBlocks.ALPHA_SUGAR_CANE.defaultBlockState();

        return Blocks.DIRT.defaultBlockState();
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
