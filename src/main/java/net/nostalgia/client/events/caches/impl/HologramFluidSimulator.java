package net.nostalgia.client.events.caches.impl;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;
import net.minecraft.world.level.ChunkPos;

public final class HologramFluidSimulator {
  private static final List<HologramFluidSimulator.FluidProps> FLUID_REGISTRY = new ArrayList<>();
  private static final int[][] HORIZONTAL = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

  public static void registerFluid(byte sourceId, byte flowingId, int dropOff, int maxDistance) {
    FLUID_REGISTRY.add(new HologramFluidSimulator.FluidProps(sourceId, flowingId, dropOff, maxDistance));
  }

  public static void simulate() {
    for (HologramFluidSimulator.FluidProps props : FLUID_REGISTRY) {
      simulateFluid(props);
    }
  }

  private static void simulateFluid(HologramFluidSimulator.FluidProps props) {
    List<int[]> sources = new ArrayList<>();

    for (Entry<ChunkPos, byte[]> entry : AlphaByteCache.CHUNK_CACHE.entrySet()) {
      ChunkPos cp = entry.getKey();
      byte[] data = entry.getValue();
      int baseX = cp.x() << 4;
      int baseZ = cp.z() << 4;

      for (int lx = 0; lx < 16; lx++) {
        for (int lz = 0; lz < 16; lz++) {
          for (int y = 0; y < 128; y++) {
            byte b = data[(lx * 16 + lz) * 128 + y];
            if ((b == props.sourceId || b == props.flowingId) && hasAirNeighbor(baseX + lx, y, baseZ + lz)) {
              sources.add(new int[]{baseX + lx, y, baseZ + lz});
            }
          }
        }
      }
    }

    Set<Long> visited = new HashSet<>();

    for (int[] src : sources) {
      long key = packPos(src[0], src[1], src[2]);
      if (visited.add(key)) {
        spreadFrom(src[0], src[1], src[2], props, visited);
      }
    }
  }

  private static boolean hasAirNeighbor(int x, int y, int z) {
    if (y > 0 && AlphaByteCache.getBlockSafely(x, y - 1, z) == 0) {
      return true;
    } else {
      for (int[] d : HORIZONTAL) {
        if (AlphaByteCache.getBlockSafely(x + d[0], y, z + d[1]) == 0) {
          return true;
        }
      }

      return false;
    }
  }

  private static void spreadFrom(int sx, int sy, int sz, HologramFluidSimulator.FluidProps props, Set<Long> visited) {
    record Pos(int x, int y, int z, int dist, boolean falling) {
    }

    Deque<Pos> queue = new ArrayDeque<>();
    queue.add(new Pos(sx, sy, sz, 0, false));

    while (!queue.isEmpty()) {
      Pos p = queue.poll();
      boolean fellDown = false;
      if (p.y > 0) {
        byte below = AlphaByteCache.getBlockSafely(p.x, p.y - 1, p.z);
        if (below == 0) {
          long downKey = packPos(p.x, p.y - 1, p.z);
          if (visited.add(downKey)) {
            AlphaByteCache.setBlockSafely(p.x, p.y - 1, p.z, props.flowingId);
            queue.add(new Pos(p.x, p.y - 1, p.z, 0, true));
            fellDown = true;
          }
        }
      }

      if (p.falling && !fellDown) {
        queue.add(new Pos(p.x, p.y, p.z, 1, false));
      } else if (!p.falling && p.dist < props.maxDistance) {
        for (int[] d : HORIZONTAL) {
          int nx = p.x + d[0];
          int nz = p.z + d[1];
          long key = packPos(nx, p.y, nz);
          if (visited.add(key)) {
            byte neighbor = AlphaByteCache.getBlockSafely(nx, p.y, nz);
            if (neighbor == 0) {
              AlphaByteCache.setBlockSafely(nx, p.y, nz, props.flowingId);
              queue.add(new Pos(nx, p.y, nz, p.dist + props.dropOff, false));
            }
          }
        }
      }
    }
  }

  private static long packPos(int x, int y, int z) {
    return (long)(x + 30000000) << 34 | (long)(y & 0xFF) << 26 | z + 30000000 & 67108863L;
  }

  private HologramFluidSimulator() {
  }

  static {
    FLUID_REGISTRY.add(new HologramFluidSimulator.FluidProps((byte)9, (byte)8, 1, 7));
    FLUID_REGISTRY.add(new HologramFluidSimulator.FluidProps((byte)11, (byte)10, 2, 3));
  }

  public record FluidProps(byte sourceId, byte flowingId, int dropOff, int maxDistance) {
  }
}
