package net.nostalgia.client.events.caches.providers;

import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import net.fabricmc.loader.api.FabricLoader;
import net.jpountz.lz4.LZ4BlockInputStream;
import net.jpountz.lz4.LZ4BlockOutputStream;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.LevelResource;
import net.nostalgia.client.performance.SHAMetricsCollector;

public class HologramDiskCache {
  private static final int MAGIC_ALPHA = -1578041343;
  private static final int MAGIC_OVERWORLD = 18743298;
  private static final Object SAVE_LOCK = new Object();

  public HologramDiskCache() {
  }

  public static Path getServerFolder() {
    Minecraft mc = Minecraft.getInstance();
    String id = "unknown";
    if (mc.getSingleplayerServer() != null) {
      id = "local_" + mc.getSingleplayerServer().getWorldPath(LevelResource.LEVEL_DATA_FILE).getParent().getFileName().toString();
    } else if (mc.getCurrentServer() != null) {
      id = "remote_" + mc.getCurrentServer().ip;
    }

    id = sanitizeCacheId(id);
    Path dir = FabricLoader.getInstance().getGameDir().resolve("nostalgia_cache").resolve(id);

    try {
      Files.createDirectories(dir);
    } catch (IOException var4) {
      var4.printStackTrace();
    }

    return dir;
  }

  public static String sanitizeCacheId(String raw) {
    return raw.replaceAll("[^\\p{L}\\p{N}_\\-]", "_");
  }

  public static Path getCacheFolderForLevel(String levelFolderName) {
    String id = sanitizeCacheId("local_" + levelFolderName);
    return FabricLoader.getInstance().getGameDir().resolve("nostalgia_cache").resolve(id);
  }

  public static void saveAlphaCache(String dimensionId, long seed, Map<ChunkPos, byte[]> cache) {
    synchronized (SAVE_LOCK) {
      String cleanDim = dimensionId.replace(":", "_").replace("minecraft_", "");
      Path dir = getServerFolder();
      Path file = dir.resolve(cleanDim + ".bin");
      Path tmpFile = dir.resolve(cleanDim + ".tmp");

      try {
        long startTime = System.nanoTime();

        try (DataOutputStream out = new DataOutputStream(new LZ4BlockOutputStream(new FileOutputStream(tmpFile.toFile())))) {
          out.writeInt(-1578041343);
          out.writeInt(cache.size());

          for (Entry<ChunkPos, byte[]> entry : cache.entrySet()) {
            out.writeInt(entry.getKey().x());
            out.writeInt(entry.getKey().z());
            out.write(entry.getValue());
          }
        }

        Files.move(tmpFile, file, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        long var21 = System.nanoTime() - startTime;
        long var22 = Files.size(file);
        SHAMetricsCollector.recordDiskWrite("Alpha (" + cleanDim + ")", var21, var22);
      } catch (Exception var19) {
        System.err.println("Failed to save alpha cache: " + var19.getMessage());

        try {
          Files.deleteIfExists(tmpFile);
        } catch (Exception var16) {
        }
      }
    }
  }

  public static Map<ChunkPos, byte[]> loadAlphaCache(String dimensionId, long seed) {
    String cleanDim = dimensionId.replace(":", "_").replace("minecraft_", "");
    Path file = getServerFolder().resolve(cleanDim + ".bin");
    if (!Files.exists(file)) {
      return null;
    } else {
      try {
        long startTime = System.nanoTime();
        long bytes = Files.size(file);

        Object var21;
        try (DataInputStream in = new DataInputStream(new LZ4BlockInputStream(new FileInputStream(file.toFile())))) {
          if (in.readInt() != -1578041343) {
            return null;
          }

          int size = in.readInt();
          Map<ChunkPos, byte[]> cache = new ConcurrentHashMap<>();

          for (int i = 0; i < size; i++) {
            int cx = in.readInt();
            int cz = in.readInt();
            byte[] data = new byte[32768];
            in.readFully(data);
            cache.put(new ChunkPos(cx, cz), data);
          }

          long duration = System.nanoTime() - startTime;
          SHAMetricsCollector.recordDiskRead("Alpha (" + cleanDim + ")", duration, bytes);
          var21 = cache;
        }

        return (Map<ChunkPos, byte[]>)var21;
      } catch (Exception var18) {
        System.err.println("Failed to load alpha cache: " + var18.getMessage());
        return null;
      }
    }
  }

  public static void saveDimensionCache(String dimensionId, Long2ObjectOpenHashMap<HologramSection> sections, Long2LongOpenHashMap chunkVersions) {
    String cleanDim = dimensionId.replace(":", "_").replace("minecraft_", "");
    Path dir = getServerFolder();
    Path file = dir.resolve(cleanDim + "_base.bin");
    Path tmpFile = dir.resolve(cleanDim + "_base.tmp");

    try {
      long startTime = System.nanoTime();
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      DataOutputStream memOut = new DataOutputStream(baos);
      memOut.writeInt(18743298);
      memOut.writeInt(sections.size());
      if (chunkVersions != null) {
        memOut.writeInt(chunkVersions.size());
        ObjectIterator mc = chunkVersions.long2LongEntrySet().iterator();

        while (mc.hasNext()) {
          it.unimi.dsi.fastutil.longs.Long2LongMap.Entry entry = (it.unimi.dsi.fastutil.longs.Long2LongMap.Entry)mc.next();
          memOut.writeLong(entry.getLongKey());
          memOut.writeLong(entry.getLongValue());
        }
      } else {
        memOut.writeInt(0);
      }

      Minecraft mc = Minecraft.getInstance();
      Registry<Biome> biomeRegistry = mc.level != null ? mc.level.registryAccess().lookupOrThrow(Registries.BIOME) : null;
      ObjectIterator uncompressedData = sections.long2ObjectEntrySet().iterator();

      while (uncompressedData.hasNext()) {
        it.unimi.dsi.fastutil.longs.Long2ObjectMap.Entry<HologramSection> entry = (it.unimi.dsi.fastutil.longs.Long2ObjectMap.Entry<HologramSection>)uncompressedData.next();
        memOut.writeLong(entry.getLongKey());
        HologramSection section = (HologramSection)entry.getValue();
        section.resolveLazy();
        if (section.palette == null) {
          memOut.writeShort(0);
        } else {
          memOut.writeShort(section.palette.length);

          for (BlockState state : section.palette) {
            memOut.writeInt(Block.getId(state));
          }

          if (section.palette.length > 1 && section.indices != null) {
            memOut.write(section.indices);
          }
        }

        if (section.biomePalette != null && biomeRegistry != null) {
          memOut.writeShort(section.biomePalette.length);

          for (Holder<Biome> holder : section.biomePalette) {
            memOut.writeInt(biomeRegistry.getId((Biome)holder.value()));
          }

          if (section.biomePalette.length > 1 && section.biomeIndices != null) {
            memOut.write(section.biomeIndices);
          }
        } else {
          memOut.writeShort(0);
        }
      }

      memOut.flush();
      byte[] uncompressedDatax = baos.toByteArray();
      DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(tmpFile.toFile())));

      try {
        out.writeInt(uncompressedDatax.length);
        LZ4BlockOutputStream lzOut = new LZ4BlockOutputStream(out);

        try {
          lzOut.write(uncompressedDatax);
        } catch (Throwable var25) {
          try {
            lzOut.close();
          } catch (Throwable var23) {
            var25.addSuppressed(var23);
          }

          throw var25;
        }

        lzOut.close();
      } catch (Throwable var26) {
        try {
          out.close();
        } catch (Throwable var22) {
          var26.addSuppressed(var22);
        }

        throw var26;
      }

      out.close();
      synchronized (SAVE_LOCK) {
        Files.move(tmpFile, file, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
      }

      long duration = System.nanoTime() - startTime;
      long bytes = Files.size(file);
      SHAMetricsCollector.recordDiskWrite(cleanDim, duration, bytes);
    } catch (Exception var27) {
      System.err.println("Failed to save dimension cache for " + dimensionId + ": " + var27.getMessage());

      try {
        Files.deleteIfExists(tmpFile);
      } catch (Exception var21) {
      }
    }
  }

  public static HologramDiskCache.DimensionCacheResult loadDimensionCache(String dimensionId) {
    String cleanDim = dimensionId.replace(":", "_").replace("minecraft_", "");
    Path file = getServerFolder().resolve(cleanDim + "_base.bin");
    if (!Files.exists(file)) {
      return null;
    } else {
      try {
        long startTime = System.nanoTime();
        long bytes = Files.size(file);

        HologramDiskCache.DimensionCacheResult var17;
        try (DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(file.toFile())))) {
          int uncompressedSize = in.readInt();
          if (uncompressedSize <= 0 || uncompressedSize > 200000000) {
            return null;
          }

          byte[] allBytes = new byte[uncompressedSize];
          LZ4BlockInputStream lzIn = new LZ4BlockInputStream(in);

          try {
            int bytesRead = 0;

            while (bytesRead < uncompressedSize) {
              int r = lzIn.read(allBytes, bytesRead, uncompressedSize - bytesRead);
              if (r == -1) {
                break;
              }

              bytesRead += r;
            }
          } catch (Throwable var25) {
            try {
              lzIn.close();
            } catch (Throwable var24) {
              var25.addSuppressed(var24);
            }

            throw var25;
          }

          lzIn.close();
          ByteBuffer buf = ByteBuffer.wrap(allBytes);
          int magic = buf.getInt();
          if (magic != 18743298 && magic != 18743297) {
            return null;
          }

          int size = buf.getInt();
          Long2LongOpenHashMap chunkVersions = new Long2LongOpenHashMap();
          if (magic == 18743298) {
            int versionsSize = buf.getInt();

            for (int i = 0; i < versionsSize; i++) {
              chunkVersions.put(buf.getLong(), buf.getLong());
            }
          }

          Long2ObjectOpenHashMap<HologramSection> sections = new Long2ObjectOpenHashMap(size);

          for (int i = 0; i < size; i++) {
            long key = buf.getLong();
            int startPos = buf.position();
            int palSize = buf.getShort() & '\uffff';
            buf.position(buf.position() + palSize * 4);
            if (palSize > 1) {
              buf.position(buf.position() + 4096);
            }

            int biomePalSize = buf.getShort() & '\uffff';
            buf.position(buf.position() + biomePalSize * 4);
            if (biomePalSize > 1) {
              buf.position(buf.position() + 64);
            }

            int endPos = buf.position();
            byte[] sectionBytes = Arrays.copyOfRange(allBytes, startPos, endPos);
            sections.put(key, new HologramSection(sectionBytes, 0));
          }

          long duration = System.nanoTime() - startTime;
          SHAMetricsCollector.recordDiskRead(cleanDim, duration, bytes);
          var17 = new HologramDiskCache.DimensionCacheResult(sections, chunkVersions);
        }

        return var17;
      } catch (Exception var27) {
        System.err.println("Failed to load dimension cache for " + dimensionId + ": " + var27.getMessage());
        return null;
      }
    }
  }

  public record DimensionCacheResult(Long2ObjectOpenHashMap<HologramSection> sections, Long2LongOpenHashMap chunkVersions) {
  }
}
