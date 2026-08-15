package net.nostalgia.alphalogic.ritual;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap.Entry;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

public class HologramWorldData extends SavedData {
  private static final String DATA_NAME = "nostalgia_alpha_deltas";
  public final Long2ObjectOpenHashMap<BlockState> deltas = new Long2ObjectOpenHashMap();
  public static final Codec<HologramWorldData> CODEC = RecordCodecBuilder.create(
    instance -> instance.group(Codec.LONG.listOf().fieldOf("positions").forGetter(data -> {
      List<Long> list = new ArrayList<>(data.deltas.size());
      Iterator i$ = data.deltas.long2ObjectEntrySet().iterator();

      while (i$.hasNext()) {
        Entry<BlockState> entry = (Entry<BlockState>)i$.next();
        list.add(entry.getLongKey());
      }

      return list;
    }), Codec.INT.listOf().fieldOf("states").forGetter(data -> {
      List<Integer> list = new ArrayList<>(data.deltas.size());
      Iterator i$ = data.deltas.long2ObjectEntrySet().iterator();

      while (i$.hasNext()) {
        Entry<BlockState> entry = (Entry<BlockState>)i$.next();
        list.add(Block.getId((BlockState)entry.getValue()));
      }

      return list;
    })).apply(instance, HologramWorldData::new)
  );

  public HologramWorldData() {
  }

  public HologramWorldData(List<Long> positions, List<Integer> states) {
    int minSize = Math.min(positions.size(), states.size());

    for (int i = 0; i < minSize; i++) {
      this.deltas.put(positions.get(i), Block.stateById(states.get(i)));
    }
  }

  public static SavedDataType<HologramWorldData> type() {
    return new SavedDataType(
      Identifier.fromNamespaceAndPath("nostalgia", "nostalgia_alpha_deltas"), HologramWorldData::new, CODEC, DataFixTypes.SAVED_DATA_COMMAND_STORAGE
    );
  }

  public static HologramWorldData get(ServerLevel level) {
    return (HologramWorldData)level.getDataStorage().computeIfAbsent(type());
  }

  public void addDelta(BlockPos pos, BlockState state) {
    this.deltas.put(pos.asLong(), state);
    this.setDirty();
  }

  public BlockState getDelta(BlockPos pos) {
    return (BlockState)this.deltas.get(pos.asLong());
  }

  public Map<BlockPos, BlockState> getDeltasInRadius(BlockPos center, double radius) {
    Map<BlockPos, BlockState> result = new HashMap<>();
    double radiusSq = radius * radius;
    ObjectIterator var7 = this.deltas.long2ObjectEntrySet().iterator();

    while (var7.hasNext()) {
      Entry<BlockState> entry = (Entry<BlockState>)var7.next();
      BlockPos pos = BlockPos.of(entry.getLongKey());
      if (pos.distSqr(center) <= radiusSq) {
        result.put(pos, (BlockState)entry.getValue());
      }
    }

    return result;
  }
}
