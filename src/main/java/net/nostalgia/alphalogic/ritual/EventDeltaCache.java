package net.nostalgia.alphalogic.ritual;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class EventDeltaCache {
  private final ConcurrentHashMap<BlockPos, BlockState> cache = new ConcurrentHashMap<>();

  public EventDeltaCache() {
  }

  public void put(BlockPos pos, BlockState state) {
    this.cache.put(pos.immutable(), state);
  }

  public BlockState get(BlockPos pos) {
    return this.cache.get(pos);
  }

  public boolean has(BlockPos pos) {
    return this.cache.containsKey(pos);
  }

  public void clear() {
    this.cache.clear();
  }

  public Map<BlockPos, BlockState> getAll() {
    return Collections.unmodifiableMap(this.cache);
  }
}
