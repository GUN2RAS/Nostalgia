package net.nostalgia.alphalogic.ritual;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.nostalgia.world.dimension.ModDimensions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlphaWorldData extends SavedData {

    private static final String DATA_NAME = "nostalgia_alpha_deltas";

    public final Long2ObjectOpenHashMap<BlockState> deltas = new Long2ObjectOpenHashMap<>();

    public AlphaWorldData() {
        super();
    }

    public AlphaWorldData(List<Long> positions, List<Integer> states) {
        super();
        int minSize = Math.min(positions.size(), states.size());
        for (int i = 0; i < minSize; i++) {
            deltas.put(positions.get(i).longValue(), Block.stateById(states.get(i)));
        }
    }

    public static final Codec<AlphaWorldData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.LONG.listOf().fieldOf("positions").forGetter(data -> {
                List<Long> list = new ArrayList<>(data.deltas.size());
                for (Long2ObjectMap.Entry<BlockState> entry : data.deltas.long2ObjectEntrySet()) {
                    list.add(entry.getLongKey());
                }
                return list;
            }),
            Codec.INT.listOf().fieldOf("states").forGetter(data -> {
                List<Integer> list = new ArrayList<>(data.deltas.size());
                for (Long2ObjectMap.Entry<BlockState> entry : data.deltas.long2ObjectEntrySet()) {
                    list.add(Block.getId(entry.getValue()));
                }
                return list;
            })
    ).apply(instance, AlphaWorldData::new));

    public static SavedDataType<AlphaWorldData> type() {
        return new SavedDataType<>(net.minecraft.resources.Identifier.fromNamespaceAndPath("nostalgia", DATA_NAME), AlphaWorldData::new, CODEC, DataFixTypes.SAVED_DATA_COMMAND_STORAGE);
    }

    public static AlphaWorldData get(ServerLevel overworld) {
        ServerLevel alphaLevel = overworld.getServer().getLevel(ModDimensions.ALPHA_112_01_LEVEL_KEY);
        if (alphaLevel == null) {
            
            alphaLevel = overworld;
        }

        return alphaLevel.getDataStorage().computeIfAbsent(type());
    }

    public void addDelta(BlockPos pos, BlockState state) {
        deltas.put(pos.asLong(), state);
        this.setDirty();
    }

    public BlockState getDelta(BlockPos pos) {
        return deltas.get(pos.asLong());
    }

    public Map<BlockPos, BlockState> getDeltasInRadius(BlockPos center, double radius) {
        Map<BlockPos, BlockState> result = new HashMap<>();
        double radiusSq = radius * radius;
        for (Long2ObjectMap.Entry<BlockState> entry : deltas.long2ObjectEntrySet()) {
            BlockPos pos = BlockPos.of(entry.getLongKey());
            if (pos.distSqr(center) <= radiusSq) {
                result.put(pos, entry.getValue());
            }
        }
        return result;
    }
}
