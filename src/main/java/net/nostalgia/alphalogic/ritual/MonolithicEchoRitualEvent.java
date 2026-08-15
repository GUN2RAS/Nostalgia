package net.nostalgia.alphalogic.ritual;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.nostalgia.alphalogic.ritual.event.EchoRitualEvent;
import net.nostalgia.alphalogic.ritual.event.RitualEventRegistry;

public final class MonolithicEchoRitualEvent implements EchoRitualEvent {
  public static final MonolithicEchoRitualEvent INSTANCE = new MonolithicEchoRitualEvent();
  private static final UUID FIXED_ID = UUID.nameUUIDFromBytes("nostalgia.monolithic_transition".getBytes());

  private MonolithicEchoRitualEvent() {
  }

  public static EchoRitualEvent activeOrNull() {
    return RitualEventRegistry.isTransitioning() ? INSTANCE : null;
  }

  public static EchoRitualEvent activeRitualOrNull() {
    if (RitualEventRegistry.isTransitioning()) {
      return INSTANCE;
    } else {
      return EchoRitualManager.getClientState() != EchoRitualManager.State.INACTIVE ? INSTANCE : null;
    }
  }

  @Override
  public UUID id() {
    return FIXED_ID;
  }

  @Override
  public EchoRitualManager.State state() {
    return RitualEventRegistry.state();
  }

  @Override
  public BlockPos beaconPos() {
    return RitualEventRegistry.ritualCenter();
  }

  @Override
  public ResourceKey<Level> dimension() {
    EchoRitualEventInstance i = RitualEventRegistry.activeInstance();
    ServerLevel src = i != null ? i.sourceLevel() : null;
    return src != null ? src.dimension() : null;
  }

  @Override
  public BlockPos targetPos() {
    return RitualEventRegistry.transitionTargetPos();
  }

  @Override
  public String targetDimensionId() {
    return RitualEventRegistry.transitionDimensionId();
  }

  @Override
  public ServerLevel sourceLevel() {
    EchoRitualEventInstance i = RitualEventRegistry.activeInstance();
    return i != null ? i.sourceLevel() : null;
  }

  @Override
  public ServerLevel targetServerLevel() {
    return RitualEventRegistry.transitionTarget();
  }

  @Override
  public int offsetX() {
    return RitualEventRegistry.offsetX();
  }

  @Override
  public int yOffset() {
    return RitualEventRegistry.yOffset();
  }

  @Override
  public int offsetZ() {
    return RitualEventRegistry.offsetZ();
  }

  @Override
  public void setOffsets(int dx, int dy, int dz) {
    RitualEventRegistry.setOffsets(dx, dy, dz);
  }

  @Override
  public int phase() {
    return RitualEventRegistry.currentSyncPhase();
  }

  @Override
  public void setPhase(int phase) {
    RitualEventRegistry.setCurrentSyncPhase(phase);
  }

  @Override
  public long phaseStartTime() {
    return RitualEventRegistry.phaseStartTime();
  }

  @Override
  public void setPhaseStartTime(long t) {
    RitualEventRegistry.setPhaseStartTime(t);
  }

  @Override
  public boolean isTransitioning() {
    return RitualEventRegistry.isTransitioning();
  }

  @Override
  public void setTransitioning(boolean v) {
    RitualEventRegistry.setTransitioning(v);
  }

  @Override
  public Set<UUID> participants() {
    EchoRitualEventInstance i = RitualEventRegistry.activeInstance();
    return i != null ? i.participants() : Set.of();
  }

  @Override
  public Set<UUID> readyClients() {
    return RitualEventRegistry.readyClients();
  }

  @Override
  public Set<UUID> clientsReadyForNextPhase() {
    return RitualEventRegistry.clientsReadyForNextPhase();
  }

  @Override
  public Map<UUID, Integer> clientHologramSurfaces() {
    return RitualEventRegistry.clientHologramSurfaces();
  }

  @Override
  public List<Entity> entities() {
    return RitualEventRegistry.entities();
  }

  @Override
  public void cachePut(BlockPos pos, BlockState state) {
    EchoRitualEventInstance i = RitualEventRegistry.activeInstance();
    if (i != null) {
      i.cachePut(pos, state);
    }
  }

  @Override
  public BlockState cacheGet(BlockPos pos) {
    EchoRitualEventInstance i = RitualEventRegistry.activeInstance();
    return i != null ? i.cacheGet(pos) : null;
  }

  @Override
  public boolean cacheHas(BlockPos pos) {
    EchoRitualEventInstance i = RitualEventRegistry.activeInstance();
    return i != null && i.cacheHas(pos);
  }

  @Override
  public void cacheClear() {
    EchoRitualEventInstance i = RitualEventRegistry.activeInstance();
    if (i != null) {
      i.cacheClear();
    }
  }

  @Override
  public Map<BlockPos, BlockState> cacheEntries() {
    EchoRitualEventInstance i = RitualEventRegistry.activeInstance();
    return i != null ? i.cacheEntries() : Map.of();
  }
}
