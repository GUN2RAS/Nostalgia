package net.nostalgia.alphalogic.ritual;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.PalettedContainerRO;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.nostalgia.network.S2CDimensionSectionsPayload;
import net.nostalgia.network.S2CHologramReadyPayload;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;

import java.util.Queue;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.ForkJoinPool;

public class HologramChunkLoader {
    private static class Task {
        List<ServerPlayer> players;
        ServerLevel level;
        BlockPos center;
        int radius;
        Queue<ChunkPos> pendingTicketsToAdd = new java.util.concurrent.ConcurrentLinkedQueue<>();
        Queue<ChunkPos> waitingForDistanceManager = new java.util.concurrent.ConcurrentLinkedQueue<>();
        Queue<ChunkPos> pendingFutures = new java.util.concurrent.ConcurrentLinkedQueue<>();
        int ticks = 0;
        AtomicInteger remaining;
        List<S2CDimensionSectionsPayload.SectionData> buffer = Collections.synchronizedList(new ArrayList<>());
        List<Long> bufferChunkPos = Collections.synchronizedList(new ArrayList<>());
        List<Long> bufferChunkVer = Collections.synchronizedList(new ArrayList<>());
    }

    private static final Queue<Task> tasks = new ConcurrentLinkedQueue<>();
    private static boolean registered = false;

    public static List<ChunkPos> getAllChunksInRadius(BlockPos center, int radius) {
        int centerCX = center.getX() >> 4;
        int centerCZ = center.getZ() >> 4;
        int chunkRadius = (radius >> 4) + 1;
        List<ChunkPos> chunks = new ArrayList<>();
        for (int cx = -chunkRadius; cx <= chunkRadius; cx++) {
            for (int cz = -chunkRadius; cz <= chunkRadius; cz++) {
                chunks.add(new ChunkPos(centerCX + cx, centerCZ + cz));
            }
        }
        return chunks;
    }

    public static void startLoading(List<ServerPlayer> players, ServerLevel level, BlockPos center, int radius, List<ChunkPos> dirtyChunks) {
        String dimId = level.dimension().identifier().toString();
        if (DimensionUtil.isClientGenerated(dimId)) {
            return;
        }

        if (!registered) {
            registered = true;
            ServerTickEvents.END_SERVER_TICK.register(server -> tick());
        }

        Task task = new Task();
        task.players = players;
        task.level = level;
        task.center = center;
        task.radius = radius;
        
        List<ChunkPos> sortedChunks = new ArrayList<>(dirtyChunks);
        int centerCX = center.getX() >> 4;
        int centerCZ = center.getZ() >> 4;
        
        sortedChunks.sort(java.util.Comparator.comparingDouble(pos -> {
            double dx = pos.x() - centerCX;
            double dz = pos.z() - centerCZ;
            return dx * dx + dz * dz;
        }));
        
        task.pendingTicketsToAdd.addAll(sortedChunks);
        task.remaining = new AtomicInteger(sortedChunks.size());
        
        if (sortedChunks.isEmpty()) {
            S2CHologramReadyPayload readyPayload = new S2CHologramReadyPayload(task.level.dimension().identifier().toString(), task.center, task.radius);
            for (ServerPlayer player : task.players) {
                ServerPlayNetworking.send(player, readyPayload);
            }
            return;
        }

        tasks.add(task);
    }

    public static void tick() {
        java.util.Iterator<Task> iterator = tasks.iterator();
        while (iterator.hasNext()) {
            Task task = iterator.next();

            ChunkPos waitingPos;
            while ((waitingPos = task.waitingForDistanceManager.poll()) != null) {
                task.pendingFutures.add(waitingPos);
            }

            int addedThisTick = 0;
            ChunkPos posToAdd;
            int ticketLevel = net.minecraft.server.level.ChunkLevel.byStatus(ChunkStatus.FEATURES);
            while (addedThisTick < 10 && (posToAdd = task.pendingTicketsToAdd.poll()) != null) {
                net.minecraft.server.level.Ticket ticket = new net.minecraft.server.level.Ticket(net.minecraft.server.level.TicketType.PORTAL, ticketLevel);
                task.level.getChunkSource().addTicket(ticket, posToAdd);
                task.waitingForDistanceManager.add(posToAdd);
                addedThisTick++;
            }

            ChunkPos posToPoll;
            while ((posToPoll = task.pendingFutures.poll()) != null) {
                final ChunkPos finalPos = posToPoll;
                java.util.concurrent.CompletableFuture.supplyAsync(() -> 
                    task.level.getChunkSource().getChunkFuture(finalPos.x(), finalPos.z(), ChunkStatus.FEATURES, true),
                    java.util.concurrent.ForkJoinPool.commonPool()
                ).thenCompose(f -> f).thenAcceptAsync(result -> {
                    ChunkAccess chunk = result != null ? result.orElse(null) : null;
                    processChunk(task, finalPos, chunk);
                }, java.util.concurrent.ForkJoinPool.commonPool());
            }
        }
    }

    private static void processChunk(Task task, ChunkPos pos, ChunkAccess chunk) {
        if (chunk != null) {
            List<S2CDimensionSectionsPayload.SectionData> localList = new ArrayList<>();
            int minSec = chunk.getMinSectionY();
            int maxSec = chunk.getMaxSectionY();
            long radSq = (long) task.radius * task.radius;
            long centerX = task.center.getX();
            long centerZ = task.center.getZ();

            for (int sy = minSec; sy <= maxSec; sy++) {
                LevelChunkSection section = chunk.getSections()[sy - chunk.getMinSectionY()];
                if (section == null || section.hasOnlyAir()) continue;

                Int2IntOpenHashMap paletteMap = new Int2IntOpenHashMap();
                IntArrayList paletteList = new IntArrayList();
                int airId = Block.getId(Blocks.AIR.defaultBlockState());
                paletteMap.put(airId, 0);
                paletteList.add(airId);

                byte[] indices = new byte[4096];
                boolean hasNonAir = false;
                PalettedContainerRO<BlockState> states = section.getStates().copy();

                for (int lx = 0; lx < 16; lx++) {
                    for (int lz = 0; lz < 16; lz++) {
                        int worldX = chunk.getPos().getMinBlockX() + lx;
                        int worldZ = chunk.getPos().getMinBlockZ() + lz;
                        long dx = worldX - centerX;
                        long dz = worldZ - centerZ;
                        if (dx * dx + dz * dz > radSq) continue;

                        for (int ly = 0; ly < 16; ly++) {
                            BlockState state = states.get(lx, ly, lz);
                            if (!state.isAir()) {
                                int stateId = Block.getId(state);
                                int palIdx = paletteMap.getOrDefault(stateId, -1);
                                if (palIdx == -1) {
                                    palIdx = paletteList.size();
                                    if (palIdx < 256) {
                                        paletteMap.put(stateId, palIdx);
                                        paletteList.add(stateId);
                                    } else {
                                        palIdx = 0;
                                    }
                                }
                                indices[(ly << 8) | (lz << 4) | lx] = (byte) palIdx;
                                hasNonAir = true;
                            }
                        }
                    }
                }

                if (hasNonAir) {
                    Int2IntOpenHashMap biomePaletteMap = new Int2IntOpenHashMap();
                    IntArrayList biomePaletteList = new IntArrayList();
                    byte[] biomeIndices = new byte[64];
                    PalettedContainerRO<net.minecraft.core.Holder<net.minecraft.world.level.biome.Biome>> biomes = section.getBiomes().copy();
                    net.minecraft.core.Registry<net.minecraft.world.level.biome.Biome> biomeRegistry = task.level.registryAccess().lookupOrThrow(net.minecraft.core.registries.Registries.BIOME);

                    for (int by = 0; by < 4; by++) {
                        for (int bz = 0; bz < 4; bz++) {
                            for (int bx = 0; bx < 4; bx++) {
                                net.minecraft.core.Holder<net.minecraft.world.level.biome.Biome> biome = biomes.get(bx, by, bz);
                                int biomeId = biomeRegistry.getId(biome.value());
                                int palIdx = biomePaletteMap.getOrDefault(biomeId, -1);
                                if (palIdx == -1) {
                                    palIdx = biomePaletteList.size();
                                    if (palIdx < 256) {
                                        biomePaletteMap.put(biomeId, palIdx);
                                        biomePaletteList.add(biomeId);
                                    } else {
                                        palIdx = 0;
                                    }
                                }
                                biomeIndices[(by << 4) | (bz << 2) | bx] = (byte) palIdx;
                            }
                        }
                    }

                    localList.add(new S2CDimensionSectionsPayload.SectionData(pos.x(), sy, pos.z(), paletteList.toIntArray(), indices, biomePaletteList.toIntArray(), biomeIndices));
                }
            }

            if (!localList.isEmpty() || chunk != null) {
                synchronized (task.buffer) {
                    task.buffer.addAll(localList);
                    long chunkKey = pos.pack();
                    task.bufferChunkPos.add(chunkKey);
                    task.bufferChunkVer.add(ServerChunkTracker.get(task.level).getVersion(chunkKey));
                }
            }
        }

        int remaining = task.remaining.decrementAndGet();
        
        synchronized (task.buffer) {
            if (task.buffer.size() >= 20 || task.bufferChunkPos.size() >= 10 || (remaining == 0 && !task.bufferChunkPos.isEmpty())) {
                long[] posArr = new long[task.bufferChunkPos.size()];
                long[] verArr = new long[task.bufferChunkVer.size()];
                for (int i = 0; i < posArr.length; i++) {
                    posArr[i] = task.bufferChunkPos.get(i);
                    verArr[i] = task.bufferChunkVer.get(i);
                }
                S2CDimensionSectionsPayload payloadOw = new S2CDimensionSectionsPayload(task.level.dimension().identifier().toString(), new ArrayList<>(task.buffer), posArr, verArr);
                for (ServerPlayer player : task.players) {
                    ServerPlayNetworking.send(player, payloadOw);
                }
                task.buffer.clear();
                task.bufferChunkPos.clear();
                task.bufferChunkVer.clear();
            }
        }

        if (remaining == 0) {
            S2CHologramReadyPayload readyPayload = new S2CHologramReadyPayload(task.level.dimension().identifier().toString(), task.center, task.radius);
            for (ServerPlayer player : task.players) {
                ServerPlayNetworking.send(player, readyPayload);
            }
            tasks.remove(task);
        }
    }
}
