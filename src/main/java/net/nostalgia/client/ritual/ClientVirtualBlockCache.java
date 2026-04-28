package net.nostalgia.client.ritual;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

public class ClientVirtualBlockCache {
    private static volatile Long2ObjectOpenHashMap<BlockState> CACHE = new Long2ObjectOpenHashMap<>();

    public static void put(BlockPos pos, BlockState state) {
        synchronized(ClientVirtualBlockCache.class) {
            Long2ObjectOpenHashMap<BlockState> newCache = new Long2ObjectOpenHashMap<>(CACHE);
            newCache.put(pos.asLong(), state);
            CACHE = newCache;
        }
    }

    public static void remove(BlockPos pos) {
        synchronized(ClientVirtualBlockCache.class) {
            Long2ObjectOpenHashMap<BlockState> newCache = new Long2ObjectOpenHashMap<>(CACHE);
            newCache.remove(pos.asLong());
            CACHE = newCache;
        }
    }

    public static BlockState get(long posAsLong) {
        return CACHE.get(posAsLong);
    }

    public static boolean has(long posAsLong) {
        return CACHE.containsKey(posAsLong);
    }

    public static void syncDeltas(long[] positions, int[] states) {
        synchronized(ClientVirtualBlockCache.class) {
            Long2ObjectOpenHashMap<BlockState> newCache = new Long2ObjectOpenHashMap<>(CACHE);
            for (int i = 0; i < positions.length; i++) {
                newCache.put(positions[i], net.minecraft.world.level.block.Block.stateById(states[i]));
            }
            CACHE = newCache;
        }
    }

    public static void clear() {
        synchronized(ClientVirtualBlockCache.class) {
            CACHE = new Long2ObjectOpenHashMap<>();
        }
    }
}
