package net.nostalgia.client.events.caches.providers;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.jpountz.lz4.LZ4BlockInputStream;
import net.jpountz.lz4.LZ4BlockOutputStream;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HologramDiskCache {

    private static final int MAGIC_ALPHA = 0xA1F10001;
    private static final int MAGIC_OVERWORLD = 0x011E0002;
    private static final Object SAVE_LOCK = new Object();

    public static Path getServerFolder() {
        Minecraft mc = Minecraft.getInstance();
        String id = "unknown";
        if (mc.getSingleplayerServer() != null) {
            id = "local_" + mc.getSingleplayerServer().getWorldPath(net.minecraft.world.level.storage.LevelResource.LEVEL_DATA_FILE).getParent().getFileName().toString();
        } else if (mc.getCurrentServer() != null) {
            id = "remote_" + mc.getCurrentServer().ip;
        }
        id = sanitizeCacheId(id);
        Path dir = FabricLoader.getInstance().getGameDir().resolve("nostalgia_cache").resolve(id);
        try {
            Files.createDirectories(dir);
        } catch (IOException e) {
            e.printStackTrace();
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
        synchronized(SAVE_LOCK) {
            String cleanDim = dimensionId.replace(":", "_").replace("minecraft_", "");
            Path dir = getServerFolder();
            Path file = dir.resolve(cleanDim + ".bin");
            Path tmpFile = dir.resolve(cleanDim + ".tmp");
            try {
                long startTime = System.nanoTime();
                try (DataOutputStream out = new DataOutputStream(new LZ4BlockOutputStream(new FileOutputStream(tmpFile.toFile())))) {
                    out.writeInt(MAGIC_ALPHA);
                    out.writeInt(cache.size());
                    for (Map.Entry<ChunkPos, byte[]> entry : cache.entrySet()) {
                        out.writeInt(entry.getKey().x());
                        out.writeInt(entry.getKey().z());
                        out.write(entry.getValue());
                    }
                }
                Files.move(tmpFile, file, java.nio.file.StandardCopyOption.REPLACE_EXISTING, java.nio.file.StandardCopyOption.ATOMIC_MOVE);
                long duration = System.nanoTime() - startTime;
                long bytes = Files.size(file);
                net.nostalgia.client.performance.SHAMetricsCollector.recordDiskWrite("Alpha (" + cleanDim + ")", duration, bytes);
            } catch (Exception e) {
                System.err.println("Failed to save alpha cache: " + e.getMessage());
                try { Files.deleteIfExists(tmpFile); } catch (Exception ignored) {}
            }
        }
    }

    public static Map<ChunkPos, byte[]> loadAlphaCache(String dimensionId, long seed) {
        String cleanDim = dimensionId.replace(":", "_").replace("minecraft_", "");
        Path file = getServerFolder().resolve(cleanDim + ".bin");
        if (!Files.exists(file)) return null;

        try {
            long startTime = System.nanoTime();
            long bytes = Files.size(file);
            try (DataInputStream in = new DataInputStream(new LZ4BlockInputStream(new FileInputStream(file.toFile())))) {
                if (in.readInt() != MAGIC_ALPHA) return null;
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
                net.nostalgia.client.performance.SHAMetricsCollector.recordDiskRead("Alpha (" + cleanDim + ")", duration, bytes);
                return cache;
            }
        } catch (Exception e) {
            System.err.println("Failed to load alpha cache: " + e.getMessage());
            return null;
        }
    }


    public static void saveDimensionCache(String dimensionId, Long2ObjectOpenHashMap<HologramSection> sections, it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap chunkVersions) {
        String cleanDim = dimensionId.replace(":", "_").replace("minecraft_", "");
        Path dir = getServerFolder();
        Path file = dir.resolve(cleanDim + "_base.bin");
        Path tmpFile = dir.resolve(cleanDim + "_base.tmp");
        
        try {
            long startTime = System.nanoTime();
            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            DataOutputStream memOut = new DataOutputStream(baos);
            
            memOut.writeInt(MAGIC_OVERWORLD);
            memOut.writeInt(sections.size());
            
            if (chunkVersions != null) {
                memOut.writeInt(chunkVersions.size());
                for (it.unimi.dsi.fastutil.longs.Long2LongMap.Entry entry : chunkVersions.long2LongEntrySet()) {
                    memOut.writeLong(entry.getLongKey());
                    memOut.writeLong(entry.getLongValue());
                }
            } else {
                memOut.writeInt(0);
            }

            Minecraft mc = Minecraft.getInstance();
            Registry<Biome> biomeRegistry = mc.level != null ? mc.level.registryAccess().lookupOrThrow(Registries.BIOME) : null;

            for (Long2ObjectOpenHashMap.Entry<HologramSection> entry : sections.long2ObjectEntrySet()) {
                memOut.writeLong(entry.getLongKey());
                HologramSection section = entry.getValue();
                section.resolveLazy();


                if (section.palette != null) {
                    memOut.writeShort(section.palette.length);
                    for (BlockState state : section.palette) {
                        memOut.writeInt(Block.getId(state));
                    }
                    if (section.palette.length > 1 && section.indices != null) {
                        memOut.write(section.indices);
                    }
                } else {
                    memOut.writeShort(0);
                }


                if (section.biomePalette != null && biomeRegistry != null) {
                    memOut.writeShort(section.biomePalette.length);
                    for (Holder<Biome> holder : section.biomePalette) {
                        memOut.writeInt(biomeRegistry.getId(holder.value()));
                    }
                    if (section.biomePalette.length > 1 && section.biomeIndices != null) {
                        memOut.write(section.biomeIndices);
                    }
                } else {
                    memOut.writeShort(0);
                }
            }
            memOut.flush();
            byte[] uncompressedData = baos.toByteArray();
            
            try (DataOutputStream out = new DataOutputStream(new java.io.BufferedOutputStream(new FileOutputStream(tmpFile.toFile())))) {
                out.writeInt(uncompressedData.length);
                try (LZ4BlockOutputStream lzOut = new LZ4BlockOutputStream(out)) {
                    lzOut.write(uncompressedData);
                }
            }
            
            synchronized(SAVE_LOCK) {
                Files.move(tmpFile, file, java.nio.file.StandardCopyOption.REPLACE_EXISTING, java.nio.file.StandardCopyOption.ATOMIC_MOVE);
            }
            long duration = System.nanoTime() - startTime;
            long bytes = Files.size(file);
            net.nostalgia.client.performance.SHAMetricsCollector.recordDiskWrite(cleanDim, duration, bytes);
        } catch (Exception e) {
            System.err.println("Failed to save dimension cache for " + dimensionId + ": " + e.getMessage());
            try { Files.deleteIfExists(tmpFile); } catch (Exception ignored) {}
        }
    }

    public static DimensionCacheResult loadDimensionCache(String dimensionId) {
        String cleanDim = dimensionId.replace(":", "_").replace("minecraft_", "");
        Path file = getServerFolder().resolve(cleanDim + "_base.bin");
        if (!Files.exists(file)) return null;

        try {
            long startTime = System.nanoTime();
            long bytes = Files.size(file);
            try (java.io.DataInputStream in = new java.io.DataInputStream(new java.io.BufferedInputStream(new FileInputStream(file.toFile())))) {
                int uncompressedSize = in.readInt();
                if (uncompressedSize <= 0 || uncompressedSize > 200_000_000) {
                    return null;
                }
                
                byte[] allBytes = new byte[uncompressedSize];
                try (LZ4BlockInputStream lzIn = new LZ4BlockInputStream(in)) {
                    int bytesRead = 0;
                    while (bytesRead < uncompressedSize) {
                        int r = lzIn.read(allBytes, bytesRead, uncompressedSize - bytesRead);
                        if (r == -1) break;
                        bytesRead += r;
                    }
                }
                
                java.nio.ByteBuffer buf = java.nio.ByteBuffer.wrap(allBytes);

                int magic = buf.getInt();
                if (magic != MAGIC_OVERWORLD && magic != 0x011E0001) return null; // Поддержка старого кэша
                int size = buf.getInt();
                
                it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap chunkVersions = new it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap();
                if (magic == MAGIC_OVERWORLD) {
                    int versionsSize = buf.getInt();
                    for (int i = 0; i < versionsSize; i++) {
                        chunkVersions.put(buf.getLong(), buf.getLong());
                    }
                }

                Long2ObjectOpenHashMap<HologramSection> sections = new Long2ObjectOpenHashMap<>(size);
                
                for (int i = 0; i < size; i++) {
                    long key = buf.getLong();
                    int startPos = buf.position();


                    int palSize = buf.getShort() & 0xFFFF;
                    buf.position(buf.position() + palSize * 4);
                    if (palSize > 1) {
                        buf.position(buf.position() + 4096);
                    }


                    int biomePalSize = buf.getShort() & 0xFFFF;
                    buf.position(buf.position() + biomePalSize * 4);
                    if (biomePalSize > 1) {
                        buf.position(buf.position() + 64);
                    }

                    int endPos = buf.position();
                    byte[] sectionBytes = java.util.Arrays.copyOfRange(allBytes, startPos, endPos);
                    sections.put(key, new HologramSection(sectionBytes, 0));
                }
                long duration = System.nanoTime() - startTime;
                net.nostalgia.client.performance.SHAMetricsCollector.recordDiskRead(cleanDim, duration, bytes);
                return new DimensionCacheResult(sections, chunkVersions);
            }
        } catch (Exception e) {
            System.err.println("Failed to load dimension cache for " + dimensionId + ": " + e.getMessage());
            return null;
        }
    }

    public record DimensionCacheResult(Long2ObjectOpenHashMap<HologramSection> sections, it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap chunkVersions) {}
}
