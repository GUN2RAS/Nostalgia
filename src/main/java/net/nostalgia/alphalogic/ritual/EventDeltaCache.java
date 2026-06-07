package net.nostalgia.alphalogic.ritual;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EventDeltaCache {
    private final ConcurrentHashMap<BlockPos, BlockState> cache = new ConcurrentHashMap<>();

    public void put(BlockPos pos, BlockState state) { cache.put(pos.immutable(), state); }
    public BlockState get(BlockPos pos) { return cache.get(pos); }
    public boolean has(BlockPos pos) { return cache.containsKey(pos); }
    public void clear() { cache.clear(); }
    public Map<BlockPos, BlockState> getAll() { return Collections.unmodifiableMap(cache); }
}
