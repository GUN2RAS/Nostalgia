package net.nostalgia.alphalogic.ritual;

import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicInteger;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents.EndTick;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ChunkLevel;
import net.minecraft.server.level.ChunkResult;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.Ticket;
import net.minecraft.server.level.TicketType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.PalettedContainerRO;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.nostalgia.network.S2CDimensionSectionsPayload;
import net.nostalgia.network.S2CHologramReadyPayload;

public class HologramChunkLoader {
  private static final Queue<HologramChunkLoader.Task> tasks = new ConcurrentLinkedQueue<>();
  private static boolean registered = false;
  private static final AtomicInteger globalInflight = new AtomicInteger(0);
  private static final int MAX_INFLIGHT = 40;

  public HologramChunkLoader() {
  }

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
    if (!DimensionUtil.isClientGenerated(dimId)) {
      Iterator<HologramChunkLoader.Task> it = tasks.iterator();
      while (it.hasNext()) {
        HologramChunkLoader.Task existing = it.next();
        if (!existing.stressTest && existing.level.dimension().equals(level.dimension())) {
          for (ServerPlayer ep : existing.players) {
            for (ServerPlayer np : players) {
              if (ep.getUUID().equals(np.getUUID())) {
                if (existing.center.equals(center)) {
                  return;
                }
                it.remove();
                break;
              }
            }
          }
        }
      }

      if (!registered) {
        registered = true;
        ServerTickEvents.END_SERVER_TICK.register((EndTick)server -> tick());
      }

      HologramChunkLoader.Task task = new HologramChunkLoader.Task();
      task.players = players;
      task.level = level;
      task.center = center;
      task.radius = radius;
      List<ChunkPos> sortedChunks = new ArrayList<>(dirtyChunks);
      int centerCX = center.getX() >> 4;
      int centerCZ = center.getZ() >> 4;
      sortedChunks.sort(Comparator.comparingDouble(pos -> {
        double dx = pos.x() - centerCX;
        double dz = pos.z() - centerCZ;
        return dx * dx + dz * dz;
      }));
      task.pendingTicketsToAdd.addAll(sortedChunks);
      task.remaining = new AtomicInteger(sortedChunks.size());
      if (!sortedChunks.isEmpty()) {
        task.startTimeMs = System.currentTimeMillis();
        task.totalChunks = sortedChunks.size();
        tasks.add(task);
      } else {
        S2CHologramReadyPayload readyPayload = new S2CHologramReadyPayload(task.level.dimension().identifier().toString(), task.center, task.radius);

        for (ServerPlayer player : task.players) {
          ServerPlayNetworking.send(player, readyPayload);
        }
      }
    }
  }

  public static void startStressLoading(ServerLevel level, BlockPos center, int radius, Runnable onComplete) {
    if (!registered) {
      registered = true;
      ServerTickEvents.END_SERVER_TICK.register((EndTick)server -> tick());
    }

    List<ChunkPos> allChunks = getAllChunksInRadius(center, radius);
    HologramChunkLoader.Task task = new HologramChunkLoader.Task();
    task.players = Collections.emptyList();
    task.level = level;
    task.center = center;
    task.radius = radius;
    task.stressTest = true;
    task.onComplete = onComplete;
    task.startTimeMs = System.currentTimeMillis();
    task.totalChunks = allChunks.size();
    int centerCX = center.getX() >> 4;
    int centerCZ = center.getZ() >> 4;
    allChunks.sort(Comparator.comparingDouble(pos -> {
      double dx = pos.x() - centerCX;
      double dz = pos.z() - centerCZ;
      return dx * dx + dz * dz;
    }));
    task.pendingTicketsToAdd.addAll(allChunks);
    task.remaining = new AtomicInteger(allChunks.size());
    tasks.add(task);
  }

  public static int getActiveTaskCount() {
    return tasks.size();
  }

  public static int getGlobalInflight() {
    return globalInflight.get();
  }

  public static void cancelStressTasks() {
    tasks.removeIf(t -> t.stressTest);
  }

  public static void tick() {
    for (HologramChunkLoader.Task task : tasks) {
      ChunkPos waitingPos;
      while ((waitingPos = task.waitingForDistanceManager.poll()) != null) {
        task.pendingFutures.add(waitingPos);
      }

      int addedThisTick = 0;

      ChunkPos posToAdd;
      for (int ticketLevel = ChunkLevel.byStatus(ChunkStatus.FEATURES);
        addedThisTick < 10 && globalInflight.get() < 40 && (posToAdd = task.pendingTicketsToAdd.poll()) != null;
        addedThisTick++
      ) {
        Ticket ticket = new Ticket(TicketType.PORTAL, ticketLevel);
        task.level.getChunkSource().addTicket(ticket, posToAdd);
        task.waitingForDistanceManager.add(posToAdd);
        globalInflight.incrementAndGet();
      }

      ChunkPos posToPoll;
      while ((posToPoll = task.pendingFutures.poll()) != null) {
        final ChunkPos finalPos = posToPoll;
        CompletableFuture.<CompletableFuture>supplyAsync(
            () -> task.level.getChunkSource().getChunkFuture(finalPos.x(), finalPos.z(), ChunkStatus.FEATURES, true), ForkJoinPool.commonPool()
          )
          .thenCompose(f -> (CompletionStage<ChunkResult>)f)
          .thenAcceptAsync(result -> {
            ChunkAccess chunk = result != null ? (ChunkAccess)result.orElse(null) : null;
            processChunk(task, finalPos, chunk);
            globalInflight.decrementAndGet();
          }, ForkJoinPool.commonPool());
      }
    }
  }

  private static void processChunk(HologramChunkLoader.Task task, ChunkPos pos, ChunkAccess chunk) {
    if (chunk != null) {
      List<S2CDimensionSectionsPayload.SectionData> localList = new ArrayList<>();
      int minSec = chunk.getMinSectionY();
      int maxSec = chunk.getMaxSectionY();
      long radSq = (long)task.radius * task.radius;
      long centerX = task.center.getX();
      long centerZ = task.center.getZ();

      for (int sy = minSec; sy <= maxSec; sy++) {
        LevelChunkSection section = chunk.getSections()[sy - chunk.getMinSectionY()];
        if (section != null && !section.hasOnlyAir()) {
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
              if (dx * dx + dz * dz <= radSq) {
                for (int ly = 0; ly < 16; ly++) {
                  BlockState state = (BlockState)states.get(lx, ly, lz);
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

                    indices[ly << 8 | lz << 4 | lx] = (byte)palIdx;
                    hasNonAir = true;
                  }
                }
              }
            }
          }

          if (hasNonAir) {
            Int2IntOpenHashMap biomePaletteMap = new Int2IntOpenHashMap();
            IntArrayList biomePaletteList = new IntArrayList();
            byte[] biomeIndices = new byte[64];
            PalettedContainerRO<Holder<Biome>> biomes = section.getBiomes().copy();
            Registry<Biome> biomeRegistry = task.level.registryAccess().lookupOrThrow(Registries.BIOME);

            for (int by = 0; by < 4; by++) {
              for (int bz = 0; bz < 4; bz++) {
                for (int bx = 0; bx < 4; bx++) {
                  Holder<Biome> biome = (Holder<Biome>)biomes.get(bx, by, bz);
                  int biomeId = biomeRegistry.getId((Biome)biome.value());
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

                  biomeIndices[by << 4 | bz << 2 | bx] = (byte)palIdx;
                }
              }
            }

            localList.add(
              new S2CDimensionSectionsPayload.SectionData(pos.x(), sy, pos.z(), paletteList.toIntArray(), indices, biomePaletteList.toIntArray(), biomeIndices)
            );
          }
        }
      }

      if (!localList.isEmpty()) {
        task.processedSections.addAndGet(localList.size());
        if (!task.stressTest) {
          synchronized (task.buffer) {
            task.buffer.addAll(localList);
            long chunkKey = pos.pack();
            task.bufferChunkPos.add(chunkKey);
            task.bufferChunkVer.add(ServerChunkTracker.get(task.level).getVersion(chunkKey));
          }
        }
      }
    }

    int remaining = task.remaining.decrementAndGet();
    if (!task.stressTest) {
      synchronized (task.buffer) {
        if (task.buffer.size() >= 20 || task.bufferChunkPos.size() >= 10 || remaining == 0 && !task.bufferChunkPos.isEmpty()) {
          long[] posArr = new long[task.bufferChunkPos.size()];
          long[] verArr = new long[task.bufferChunkVer.size()];

          for (int i = 0; i < posArr.length; i++) {
            posArr[i] = task.bufferChunkPos.get(i);
            verArr[i] = task.bufferChunkVer.get(i);
          }

          S2CDimensionSectionsPayload payloadOw = new S2CDimensionSectionsPayload(
            task.level.dimension().identifier().toString(), new ArrayList<>(task.buffer), posArr, verArr
          );

          for (ServerPlayer player : task.players) {
            ServerPlayNetworking.send(player, payloadOw);
          }

          task.buffer.clear();
          task.bufferChunkPos.clear();
          task.bufferChunkVer.clear();
        }
      }
    }

    if (remaining == 0) {
      task.completionTimeMs = System.currentTimeMillis();
      if (!task.stressTest) {
        S2CHologramReadyPayload readyPayload = new S2CHologramReadyPayload(task.level.dimension().identifier().toString(), task.center, task.radius);

        for (ServerPlayer player : task.players) {
          ServerPlayNetworking.send(player, readyPayload);
        }
      }

      if (task.onComplete != null) {
        task.onComplete.run();
      }

      tasks.remove(task);
    }
  }

  private static class Task {
    List<ServerPlayer> players;
    ServerLevel level;
    BlockPos center;
    int radius;
    Queue<ChunkPos> pendingTicketsToAdd = new ConcurrentLinkedQueue<>();
    Queue<ChunkPos> waitingForDistanceManager = new ConcurrentLinkedQueue<>();
    Queue<ChunkPos> pendingFutures = new ConcurrentLinkedQueue<>();
    int ticks = 0;
    AtomicInteger remaining;
    List<S2CDimensionSectionsPayload.SectionData> buffer = Collections.synchronizedList(new ArrayList<>());
    List<Long> bufferChunkPos = Collections.synchronizedList(new ArrayList<>());
    List<Long> bufferChunkVer = Collections.synchronizedList(new ArrayList<>());
    boolean stressTest = false;
    long startTimeMs;
    long completionTimeMs;
    int totalChunks;
    AtomicInteger processedSections = new AtomicInteger(0);
    Runnable onComplete;

    private Task() {
    }
  }
}
