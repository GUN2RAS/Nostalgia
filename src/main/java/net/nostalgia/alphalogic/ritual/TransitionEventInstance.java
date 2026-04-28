package net.nostalgia.alphalogic.ritual;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.nostalgia.alphalogic.ritual.event.TransitionEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class TransitionEventInstance implements TransitionEvent {
    private final UUID id;
    private BlockPos beaconPos;
    private BlockPos targetPos;
    private String targetDimensionId = "";
    private ServerLevel sourceLevel;
    private ServerLevel targetServerLevel;

    private int offsetX;
    private int yOffset;
    private int offsetZ;

    private int phase;
    private long phaseStartTime;
    private boolean transitioning;
    private long timeStopStartTime;
    private RitualManager.State state = RitualManager.State.INACTIVE;

    private final Set<UUID> participants = ConcurrentHashMap.newKeySet();
    private final Set<UUID> readyClients = ConcurrentHashMap.newKeySet();
    private final Set<UUID> clientsReadyForNextPhase = ConcurrentHashMap.newKeySet();
    private final Map<UUID, Integer> clientHologramSurfaces = new ConcurrentHashMap<>();
    private final List<Entity> entities = new ArrayList<>();

    public TransitionEventInstance(UUID id, BlockPos beaconPos, ServerLevel sourceLevel) {
        this.id = id;
        this.beaconPos = beaconPos;
        this.sourceLevel = sourceLevel;
    }

    @Override
    public UUID id() { return id; }

    @Override
    public RitualManager.State state() { return state; }

    public void setState(RitualManager.State newState) { this.state = newState; }

    @Override
    public BlockPos beaconPos() { return beaconPos; }

    public void setBeaconPos(BlockPos pos) { this.beaconPos = pos; }

    @Override
    public ResourceKey<Level> dimension() {
        return sourceLevel != null ? sourceLevel.dimension() : null;
    }

    @Override
    public BlockPos targetPos() { return targetPos; }

    public void setTargetPos(BlockPos pos) { this.targetPos = pos; }

    @Override
    public String targetDimensionId() { return targetDimensionId; }

    public void setTargetDimensionId(String id) { this.targetDimensionId = id; }

    @Override
    public ServerLevel sourceLevel() { return sourceLevel; }

    public void setSourceLevel(ServerLevel level) { this.sourceLevel = level; }

    @Override
    public ServerLevel targetServerLevel() { return targetServerLevel; }

    public void setTargetServerLevel(ServerLevel level) { this.targetServerLevel = level; }

    @Override
    public int offsetX() { return offsetX; }

    @Override
    public int yOffset() { return yOffset; }

    @Override
    public int offsetZ() { return offsetZ; }

    @Override
    public void setOffsets(int dx, int dy, int dz) {
        this.offsetX = dx;
        this.yOffset = dy;
        this.offsetZ = dz;
    }

    @Override
    public int phase() { return phase; }

    @Override
    public void setPhase(int phase) { this.phase = phase; }

    @Override
    public long phaseStartTime() { return phaseStartTime; }

    @Override
    public void setPhaseStartTime(long t) { this.phaseStartTime = t; }

    public long timeStopStartTime() { return timeStopStartTime; }

    public void setTimeStopStartTime(long t) { this.timeStopStartTime = t; }

    @Override
    public boolean isTransitioning() { return transitioning; }

    @Override
    public void setTransitioning(boolean v) { this.transitioning = v; }

    @Override
    public Set<UUID> participants() { return participants; }

    @Override
    public Set<UUID> readyClients() { return readyClients; }

    @Override
    public Set<UUID> clientsReadyForNextPhase() { return clientsReadyForNextPhase; }

    @Override
    public Map<UUID, Integer> clientHologramSurfaces() { return clientHologramSurfaces; }

    @Override
    public List<Entity> entities() { return entities; }

    @Override
    public void cachePut(BlockPos pos, BlockState state) { VirtualBlockCache.put(pos, state); }

    @Override
    public BlockState cacheGet(BlockPos pos) { return VirtualBlockCache.get(pos); }

    @Override
    public boolean cacheHas(BlockPos pos) { return VirtualBlockCache.has(pos); }

    @Override
    public void cacheClear() { VirtualBlockCache.clear(); }

    @Override
    public Map<BlockPos, BlockState> cacheEntries() { return VirtualBlockCache.getAll(); }
}
