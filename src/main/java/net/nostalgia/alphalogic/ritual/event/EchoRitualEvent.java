package net.nostalgia.alphalogic.ritual.event;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.nostalgia.alphalogic.ritual.EchoRitualManager;

public interface EchoRitualEvent extends RitualEvent {
  EchoRitualManager.State state();

  BlockPos targetPos();

  String targetDimensionId();

  ServerLevel sourceLevel();

  ServerLevel targetServerLevel();

  int offsetX();

  int yOffset();

  int offsetZ();

  void setOffsets(int var1, int var2, int var3);

  int phase();

  void setPhase(int var1);

  long phaseStartTime();

  void setPhaseStartTime(long var1);

  boolean isTransitioning();

  void setTransitioning(boolean var1);

  Set<UUID> participants();

  Set<UUID> readyClients();

  Set<UUID> clientsReadyForNextPhase();

  Map<UUID, Integer> clientHologramSurfaces();

  List<Entity> entities();

  void cachePut(BlockPos var1, BlockState var2);

  BlockState cacheGet(BlockPos var1);

  boolean cacheHas(BlockPos var1);

  void cacheClear();

  Map<BlockPos, BlockState> cacheEntries();

  @Override
  default RitualEvent.Kind kind() {
    return RitualEvent.Kind.TRANSITION;
  }
}
