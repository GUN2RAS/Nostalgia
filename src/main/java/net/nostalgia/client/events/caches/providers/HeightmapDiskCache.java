package net.nostalgia.client.events.caches.providers;

import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2IntMap.Entry;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import net.jpountz.lz4.LZ4BlockInputStream;
import net.jpountz.lz4.LZ4BlockOutputStream;

public class HeightmapDiskCache {
  private static final int MAGIC = 1212494145;
  private static final Object SAVE_LOCK = new Object();

  public HeightmapDiskCache() {
  }

  public static int packXZ(int worldX, int worldZ) {
    return (worldX & 65535) << 16 | worldZ & 65535;
  }

  public static int unpackX(int packed) {
    return (short)(packed >>> 16);
  }

  public static int unpackZ(int packed) {
    return (short)(packed & 65535);
  }

  public static void save(String dimensionId, HeightmapDiskCache.HeightmapData data) {
    if (data != null && data.size() != 0) {
      String cleanDim = dimensionId.replace(":", "_").replace("minecraft_", "");
      Path dir = HologramDiskCache.getServerFolder();
      Path file = dir.resolve(cleanDim + "_heightmap.bin");
      Path tmpFile = dir.resolve(cleanDim + "_heightmap.tmp");
      synchronized (SAVE_LOCK) {
        try {
          try (DataOutputStream out = new DataOutputStream(new LZ4BlockOutputStream(new FileOutputStream(tmpFile.toFile())))) {
            out.writeInt(1212494145);
            out.writeInt(data.size());
            ObjectIterator var8 = data.heights.int2IntEntrySet().iterator();

            while (var8.hasNext()) {
              Entry entry = (Entry)var8.next();
              int key = entry.getIntKey();
              out.writeInt(key);
              out.writeShort(entry.getIntValue());
              out.writeInt(data.colors.getOrDefault(key, 0));
              out.writeInt(data.stateIds.getOrDefault(key, 0));
            }
          }

          Files.move(tmpFile, file, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (Exception var15) {
          System.err.println("Failed to save heightmap for " + dimensionId + ": " + var15.getMessage());

          try {
            Files.deleteIfExists(tmpFile);
          } catch (Exception var12) {
          }
        }
      }
    }
  }

  public static HeightmapDiskCache.HeightmapData load(String dimensionId) {
    String cleanDim = dimensionId.replace(":", "_").replace("minecraft_", "");
    Path file = HologramDiskCache.getServerFolder().resolve(cleanDim + "_heightmap.bin");
    if (!Files.exists(file)) {
      return null;
    } else {
      try {
        HeightmapDiskCache.HeightmapData var16;
        try (DataInputStream in = new DataInputStream(new LZ4BlockInputStream(new FileInputStream(file.toFile())))) {
          int magic = in.readInt();
          if (magic != 1212494145) {
            return null;
          }

          int count = in.readInt();
          HeightmapDiskCache.HeightmapData data = HeightmapDiskCache.HeightmapData.empty();

          for (int i = 0; i < count; i++) {
            int key = in.readInt();
            int height = in.readShort() & '\uffff';
            int color = in.readInt();
            int stateId = in.readInt();
            data.put(key, height, color, stateId);
          }

          var16 = data;
        }

        return var16;
      } catch (Exception var14) {
        System.err.println("Failed to load heightmap for " + dimensionId + ": " + var14.getMessage());
        return null;
      }
    }
  }

  public static HeightmapDiskCache.HeightmapData loadOrEmpty(String dimensionId) {
    HeightmapDiskCache.HeightmapData data = load(dimensionId);
    return data != null ? data : HeightmapDiskCache.HeightmapData.empty();
  }

  public static void mergeAndSave(String dimensionId, HeightmapDiskCache.HeightmapData newData) {
    HeightmapDiskCache.HeightmapData existing = load(dimensionId);
    if (existing != null) {
      existing.mergeFrom(newData);
      save(dimensionId, existing);
    } else {
      save(dimensionId, newData);
    }
  }

  public record HeightmapData(Int2IntOpenHashMap heights, Int2IntOpenHashMap colors, Int2IntOpenHashMap stateIds) {
    public static HeightmapDiskCache.HeightmapData empty() {
      return new HeightmapDiskCache.HeightmapData(new Int2IntOpenHashMap(), new Int2IntOpenHashMap(), new Int2IntOpenHashMap());
    }

    public int size() {
      return this.heights.size();
    }

    public boolean has(int packedXZ) {
      return this.heights.containsKey(packedXZ);
    }

    public int getHeight(int packedXZ) {
      return this.heights.getOrDefault(packedXZ, 64);
    }

    public int getColor(int packedXZ) {
      return this.colors.getOrDefault(packedXZ, 0);
    }

    public int getStateId(int packedXZ) {
      return this.stateIds.getOrDefault(packedXZ, 0);
    }

    public void put(int packedXZ, int height, int color, int stateId) {
      this.heights.put(packedXZ, height);
      this.colors.put(packedXZ, color);
      this.stateIds.put(packedXZ, stateId);
    }

    public void mergeFrom(HeightmapDiskCache.HeightmapData other) {
      ObjectIterator var2 = other.heights.int2IntEntrySet().iterator();

      while (var2.hasNext()) {
        Entry entry = (Entry)var2.next();
        int key = entry.getIntKey();
        this.heights.put(key, entry.getIntValue());
        this.colors.put(key, other.colors.getOrDefault(key, 0));
        this.stateIds.put(key, other.stateIds.getOrDefault(key, 0));
      }
    }
  }
}
