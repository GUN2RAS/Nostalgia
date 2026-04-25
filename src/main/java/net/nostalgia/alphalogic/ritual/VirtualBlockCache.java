package net.nostalgia.alphalogic.ritual;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class VirtualBlockCache {
    private static final Map<BlockPos, BlockState> CACHE = new ConcurrentHashMap<>();

    public static void put(BlockPos pos, BlockState state) {
        CACHE.put(pos.immutable(), state);
    }

    public static BlockState get(BlockPos pos) {
        return CACHE.get(pos);
    }

    public static boolean has(BlockPos pos) {
        return CACHE.containsKey(pos);
    }

    public static void clear() {
        CACHE.clear();
    }
    
    public static Map<BlockPos, BlockState> getAll() {
        return CACHE;
    }
}
