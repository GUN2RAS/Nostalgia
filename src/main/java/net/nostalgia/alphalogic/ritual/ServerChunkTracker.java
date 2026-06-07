package net.nostalgia.alphalogic.ritual;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.longs.Long2LongMap;
import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.ArrayList;
import java.util.List;

public class ServerChunkTracker extends SavedData {

    private static final String DATA_NAME = "nostalgia_overworld_tracker";

    public final Long2LongOpenHashMap chunkVersions = new Long2LongOpenHashMap();

    public ServerChunkTracker() {
        super();
    }

    public ServerChunkTracker(List<Long> chunks, List<Long> versions) {
        super();
        int minSize = Math.min(chunks.size(), versions.size());
        for (int i = 0; i < minSize; i++) {
            chunkVersions.put(chunks.get(i).longValue(), versions.get(i).longValue());
        }
    }

    public static final Codec<ServerChunkTracker> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.LONG.listOf().fieldOf("chunks").forGetter(data -> {
                List<Long> list = new ArrayList<>(data.chunkVersions.size());
                for (Long2LongMap.Entry entry : data.chunkVersions.long2LongEntrySet()) {
                    list.add(entry.getLongKey());
                }
                return list;
            }),
            Codec.LONG.listOf().fieldOf("versions").forGetter(data -> {
                List<Long> list = new ArrayList<>(data.chunkVersions.size());
                for (Long2LongMap.Entry entry : data.chunkVersions.long2LongEntrySet()) {
                    list.add(entry.getLongValue());
                }
                return list;
            })
    ).apply(instance, ServerChunkTracker::new));

    public static SavedDataType<ServerChunkTracker> type() {
        return new SavedDataType<>(net.minecraft.resources.Identifier.fromNamespaceAndPath("nostalgia", DATA_NAME), ServerChunkTracker::new, CODEC, DataFixTypes.SAVED_DATA_COMMAND_STORAGE);
    }

    public static ServerChunkTracker get(ServerLevel overworld) {
        if (overworld.dimension() != net.minecraft.world.level.Level.OVERWORLD) {
            ServerLevel realOverworld = overworld.getServer().getLevel(net.minecraft.world.level.Level.OVERWORLD);
            if (realOverworld != null) {
                return realOverworld.getDataStorage().computeIfAbsent(type());
            }
        }
        return overworld.getDataStorage().computeIfAbsent(type());
    }

    public void markDirty(BlockPos pos) {
        long chunkKey = ChunkPos.pack(pos.getX() >> 4, pos.getZ() >> 4);
        chunkVersions.put(chunkKey, System.currentTimeMillis());
        this.setDirty();
    }

    public long getVersion(long chunkKey) {
        return chunkVersions.getOrDefault(chunkKey, 0L);
    }
}
