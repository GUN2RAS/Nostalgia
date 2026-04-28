package net.nostalgia.alphalogic.ritual;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.nostalgia.alphalogic.ritual.event.TransitionEvent;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public final class MonolithicTransitionEvent implements TransitionEvent {
    public static final MonolithicTransitionEvent INSTANCE = new MonolithicTransitionEvent();
    private static final UUID FIXED_ID = UUID.nameUUIDFromBytes("nostalgia.monolithic_transition".getBytes());

    private MonolithicTransitionEvent() {}

    public static TransitionEvent activeOrNull() {
        return net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.isTransitioning() ? INSTANCE : null;
    }

    public static TransitionEvent activeRitualOrNull() {
        if (net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.isTransitioning()) return INSTANCE;
        if (RitualManager.getClientState() != RitualManager.State.INACTIVE) return INSTANCE;
        return null;
    }

    @Override
    public UUID id() { return FIXED_ID; }

    @Override
    public RitualManager.State state() { return net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.state(); }

    @Override
    public BlockPos beaconPos() {
        return net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.ritualCenter();
    }

    @Override
    public ResourceKey<Level> dimension() {
        TransitionEventInstance i = net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.activeInstance();
        ServerLevel src = i != null ? i.sourceLevel() : null;
        return src != null ? src.dimension() : null;
    }

    @Override
    public BlockPos targetPos() { return net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.transitionTargetPos(); }

    @Override
    public String targetDimensionId() { return net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.transitionDimensionId(); }

    @Override
    public ServerLevel sourceLevel() {
        TransitionEventInstance i = net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.activeInstance();
        return i != null ? i.sourceLevel() : null;
    }

    @Override
    public ServerLevel targetServerLevel() { return net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.transitionTarget(); }

    @Override
    public int offsetX() { return net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.offsetX(); }

    @Override
    public int yOffset() { return net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.yOffset(); }

    @Override
    public int offsetZ() { return net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.offsetZ(); }

    @Override
    public void setOffsets(int dx, int dy, int dz) {
        net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.setOffsets(dx, dy, dz);
    }

    @Override
    public int phase() { return net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.currentSyncPhase(); }

    @Override
    public void setPhase(int phase) { net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.setCurrentSyncPhase(phase); }

    @Override
    public long phaseStartTime() { return net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.phaseStartTime(); }

    @Override
    public void setPhaseStartTime(long t) { net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.setPhaseStartTime(t); }

    @Override
    public boolean isTransitioning() { return net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.isTransitioning(); }

    @Override
    public void setTransitioning(boolean v) { net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.setTransitioning(v); }

    @Override
    public Set<UUID> participants() {
        TransitionEventInstance i = net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.activeInstance();
        return i != null ? i.participants() : java.util.Set.of();
    }

    @Override
    public Set<UUID> readyClients() { return net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.readyClients(); }

    @Override
    public Set<UUID> clientsReadyForNextPhase() { return net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.clientsReadyForNextPhase(); }

    @Override
    public Map<UUID, Integer> clientHologramSurfaces() { return net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.clientHologramSurfaces(); }

    @Override
    public List<Entity> entities() { return net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.entities(); }

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
