package net.nostalgia.alphalogic.ritual.event;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface TransitionEvent extends RitualEvent {
    BlockPos targetPos();
    String targetDimensionId();
    ServerLevel sourceLevel();
    ServerLevel targetServerLevel();

    int offsetX();
    int yOffset();
    int offsetZ();
    void setOffsets(int dx, int dy, int dz);

    int phase();
    void setPhase(int phase);

    long phaseStartTime();
    void setPhaseStartTime(long t);

    boolean isTransitioning();
    void setTransitioning(boolean v);

    Set<UUID> participants();
    Set<UUID> readyClients();
    Set<UUID> clientsReadyForNextPhase();
    Map<UUID, Integer> clientHologramSurfaces();
    List<Entity> entities();

    @Override
    default Kind kind() { return Kind.TRANSITION; }
}
