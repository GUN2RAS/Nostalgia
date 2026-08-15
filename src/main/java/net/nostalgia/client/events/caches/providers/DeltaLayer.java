package net.nostalgia.client.events.caches.providers;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public final class DeltaLayer {
  private final ConcurrentHashMap<Long, BlockState> overrides = new ConcurrentHashMap<>();
  private final ConcurrentHashMap<Long, BlockState> ritualOverrides = new ConcurrentHashMap<>();

  public DeltaLayer() {
  }

  public int size() {
    return this.overrides.size() + this.ritualOverrides.size();
  }

  public void setOverride(BlockPos pos, BlockState state) {
    this.overrides.put(pos.asLong(), state);
  }

  public void setOverrideRaw(long posAsLong, BlockState state) {
    this.overrides.put(posAsLong, state);
  }

  public void setRitualOverride(long posAsLong, BlockState state) {
    this.ritualOverrides.put(posAsLong, state);
  }

  public BlockState getOverride(BlockPos pos) {
    return this.overrides.get(pos.asLong());
  }

  public BlockState getOverrideRaw(long posAsLong) {
    return this.overrides.get(posAsLong);
  }

  public BlockState getRitualOverride(long posAsLong) {
    return this.ritualOverrides.get(posAsLong);
  }

  public boolean hasOverride(BlockPos pos) {
    return this.overrides.containsKey(pos.asLong());
  }

  public boolean hasOverrideRaw(long posAsLong) {
    return this.overrides.containsKey(posAsLong);
  }

  public boolean hasRitualOverride(long posAsLong) {
    return this.ritualOverrides.containsKey(posAsLong);
  }

  public void removeOverride(BlockPos pos) {
    this.overrides.remove(pos.asLong());
  }

  public Map<BlockPos, BlockState> getAllOverrides() {
    Map<BlockPos, BlockState> result = new HashMap<>();
    this.overrides.forEach((longPos, state) -> result.put(BlockPos.of(longPos), state));
    return result;
  }

  public void clear() {
    this.overrides.clear();
    this.ritualOverrides.clear();
  }
}
