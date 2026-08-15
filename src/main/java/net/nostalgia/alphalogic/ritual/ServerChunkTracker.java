package net.nostalgia.alphalogic.ritual;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2LongMap.Entry;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

public class ServerChunkTracker extends SavedData {
  private static final String DATA_NAME = "nostalgia_overworld_tracker";
  public final Long2LongOpenHashMap chunkVersions = new Long2LongOpenHashMap();
  public static final Codec<ServerChunkTracker> CODEC = RecordCodecBuilder.create(
    instance -> instance.group(Codec.LONG.listOf().fieldOf("chunks").forGetter(data -> {
      List<Long> list = new ArrayList<>(data.chunkVersions.size());
      Iterator i$ = data.chunkVersions.long2LongEntrySet().iterator();

      while (i$.hasNext()) {
        Entry entry = (Entry)i$.next();
        list.add(entry.getLongKey());
      }

      return list;
    }), Codec.LONG.listOf().fieldOf("versions").forGetter(data -> {
      List<Long> list = new ArrayList<>(data.chunkVersions.size());
      Iterator i$ = data.chunkVersions.long2LongEntrySet().iterator();

      while (i$.hasNext()) {
        Entry entry = (Entry)i$.next();
        list.add(entry.getLongValue());
      }

      return list;
    })).apply(instance, ServerChunkTracker::new)
  );

  public ServerChunkTracker() {
  }

  public ServerChunkTracker(List<Long> chunks, List<Long> versions) {
    int minSize = Math.min(chunks.size(), versions.size());

    for (int i = 0; i < minSize; i++) {
      this.chunkVersions.put(chunks.get(i), versions.get(i));
    }
  }

  public static SavedDataType<ServerChunkTracker> type() {
    return new SavedDataType(
      Identifier.fromNamespaceAndPath("nostalgia", "nostalgia_overworld_tracker"), ServerChunkTracker::new, CODEC, DataFixTypes.SAVED_DATA_COMMAND_STORAGE
    );
  }

  public static ServerChunkTracker get(ServerLevel overworld) {
    if (overworld.dimension() != Level.OVERWORLD) {
      ServerLevel realOverworld = overworld.getServer().getLevel(Level.OVERWORLD);
      if (realOverworld != null) {
        return (ServerChunkTracker)realOverworld.getDataStorage().computeIfAbsent(type());
      }
    }

    return (ServerChunkTracker)overworld.getDataStorage().computeIfAbsent(type());
  }

  public void markDirty(BlockPos pos) {
    long chunkKey = ChunkPos.pack(pos.getX() >> 4, pos.getZ() >> 4);
    this.chunkVersions.put(chunkKey, System.currentTimeMillis());
    this.setDirty();
  }

  public long getVersion(long chunkKey) {
    return this.chunkVersions.getOrDefault(chunkKey, 0L);
  }
}
