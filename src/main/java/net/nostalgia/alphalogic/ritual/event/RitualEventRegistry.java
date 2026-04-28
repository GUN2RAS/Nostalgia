package net.nostalgia.alphalogic.ritual.event;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.nostalgia.alphalogic.ritual.ActiveZoneEvent;
import net.nostalgia.alphalogic.ritual.MonolithicTransitionEvent;
import net.nostalgia.alphalogic.ritual.RitualManager;
import net.nostalgia.alphalogic.ritual.TransitionEventInstance;

import java.util.UUID;

public final class RitualEventRegistry {

    private static final java.util.concurrent.ConcurrentHashMap<UUID, TransitionEventInstance> activeInstances = new java.util.concurrent.ConcurrentHashMap<>();

    private RitualEventRegistry() {}

    public static TransitionEvent activeTransition() {
        return MonolithicTransitionEvent.activeOrNull();
    }

    public static TransitionEvent activeRitual() {
        return MonolithicTransitionEvent.activeRitualOrNull();
    }

    public static TransitionEventInstance activeInstance() {
        for (TransitionEventInstance i : activeInstances.values()) return i;
        return null;
    }

    public static java.util.Collection<TransitionEventInstance> allInstances() {
        return activeInstances.values();
    }

    public static TransitionEventInstance findInstanceByBeacon(BlockPos beaconPos) {
        if (beaconPos == null) return null;
        for (TransitionEventInstance i : activeInstances.values()) {
            if (beaconPos.equals(i.beaconPos())) return i;
        }
        return null;
    }

    public static TransitionEventInstance findInstanceForParticipant(UUID playerUuid) {
        for (TransitionEventInstance i : activeInstances.values()) {
            if (i.participants().contains(playerUuid)) return i;
        }
        return null;
    }

    public static TransitionEventInstance startEvent(BlockPos beaconPos, ServerLevel sourceLevel) {
        TransitionEventInstance existing = findInstanceByBeacon(beaconPos);
        if (existing != null) return existing;
        TransitionEventInstance instance = new TransitionEventInstance(UUID.randomUUID(), beaconPos, sourceLevel);
        activeInstances.put(instance.id(), instance);
        return instance;
    }

    public static void endEvent() {
        TransitionEventInstance first = activeInstance();
        if (first != null) activeInstances.remove(first.id());
    }

    public static void endEvent(UUID id) {
        if (id != null) activeInstances.remove(id);
    }

    public static void endAllEvents() {
        activeInstances.clear();
    }

    public static java.util.Set<UUID> participants() {
        TransitionEventInstance i = activeInstance();
        return i != null ? i.participants() : new java.util.HashSet<>();
    }

    public static boolean isParticipant(net.minecraft.world.entity.Entity entity) {
        return isParticipantAny(entity);
    }

    public static boolean isParticipantAny(net.minecraft.world.entity.Entity entity) {
        if (entity == null) return false;
        java.util.UUID uuid = entity.getUUID();
        for (TransitionEventInstance i : activeInstances.values()) {
            if (i.participants().contains(uuid)) return true;
        }
        return false;
    }

    public static boolean isParticipantAny(java.util.UUID uuid) {
        if (uuid == null) return false;
        for (TransitionEventInstance i : activeInstances.values()) {
            if (i.participants().contains(uuid)) return true;
        }
        return false;
    }

    public static boolean inSameInstance(java.util.UUID a, java.util.UUID b) {
        if (a == null || b == null) return false;
        TransitionEventInstance ia = findInstanceForParticipant(a);
        TransitionEventInstance ib = findInstanceForParticipant(b);
        return ia != null && ia == ib;
    }

    public static java.util.Set<java.util.UUID> allParticipants() {
        java.util.Set<java.util.UUID> out = new java.util.HashSet<>();
        for (TransitionEventInstance i : activeInstances.values()) {
            out.addAll(i.participants());
        }
        return out;
    }

    public static boolean addParticipant(UUID uuid) {
        TransitionEventInstance i = activeInstance();
        return i != null && i.participants().add(uuid);
    }

    public static boolean removeParticipantUuid(UUID uuid) {
        TransitionEventInstance i = activeInstance();
        return i != null && i.participants().remove(uuid);
    }

    public static void clearParticipants() {
        TransitionEventInstance i = activeInstance();
        if (i != null) i.participants().clear();
    }

    public static void setParticipants(java.util.Collection<UUID> uuids) {
        TransitionEventInstance i = activeInstance();
        if (i != null) {
            i.participants().clear();
            i.participants().addAll(uuids);
        }
    }

    public static int offsetX() {
        TransitionEventInstance i = activeInstance();
        return i != null ? i.offsetX() : 0;
    }

    public static int yOffset() {
        TransitionEventInstance i = activeInstance();
        return i != null ? i.yOffset() : 0;
    }

    public static int offsetZ() {
        TransitionEventInstance i = activeInstance();
        return i != null ? i.offsetZ() : 0;
    }

    public static void setOffsets(int dx, int dy, int dz) {
        TransitionEventInstance i = activeInstance();
        if (i != null) i.setOffsets(dx, dy, dz);
    }

    public static boolean isTransitioning() {
        TransitionEventInstance i = activeInstance();
        return i != null && i.isTransitioning();
    }

    public static void setTransitioning(boolean v) {
        TransitionEventInstance i = activeInstance();
        if (i != null) i.setTransitioning(v);
    }

    public static BlockPos ritualCenter() {
        TransitionEventInstance i = activeInstance();
        return i != null ? i.beaconPos() : null;
    }

    public static void setRitualCenter(BlockPos pos) {
        TransitionEventInstance i = activeInstance();
        if (i != null) i.setBeaconPos(pos);
    }

    public static ServerLevel transitionTarget() {
        TransitionEventInstance i = activeInstance();
        return i != null ? i.targetServerLevel() : null;
    }

    public static void setTransitionTarget(ServerLevel level) {
        TransitionEventInstance i = activeInstance();
        if (i != null) i.setTargetServerLevel(level);
    }

    public static String transitionDimensionId() {
        TransitionEventInstance i = activeInstance();
        return i != null ? i.targetDimensionId() : "";
    }

    public static void setTransitionDimensionId(String id) {
        TransitionEventInstance i = activeInstance();
        if (i != null) i.setTargetDimensionId(id);
    }

    public static BlockPos transitionTargetPos() {
        TransitionEventInstance i = activeInstance();
        return i != null ? i.targetPos() : null;
    }

    public static void setTransitionTargetPos(BlockPos pos) {
        TransitionEventInstance i = activeInstance();
        if (i != null) i.setTargetPos(pos);
    }

    public static net.nostalgia.alphalogic.ritual.RitualManager.State state() {
        TransitionEventInstance i = activeInstance();
        return i != null ? i.state() : net.nostalgia.alphalogic.ritual.RitualManager.State.INACTIVE;
    }

    public static void setState(net.nostalgia.alphalogic.ritual.RitualManager.State s) {
        TransitionEventInstance i = activeInstance();
        if (i != null) i.setState(s);
    }

    public static int currentSyncPhase() {
        TransitionEventInstance i = activeInstance();
        return i != null ? i.phase() : 0;
    }

    public static void setCurrentSyncPhase(int phase) {
        TransitionEventInstance i = activeInstance();
        if (i != null) i.setPhase(phase);
    }

    public static long phaseStartTime() {
        TransitionEventInstance i = activeInstance();
        return i != null ? i.phaseStartTime() : 0L;
    }

    public static void setPhaseStartTime(long t) {
        TransitionEventInstance i = activeInstance();
        if (i != null) i.setPhaseStartTime(t);
    }

    public static long timeStopStartTime() {
        TransitionEventInstance i = activeInstance();
        return i != null ? i.timeStopStartTime() : 0L;
    }

    public static void setTimeStopStartTime(long t) {
        TransitionEventInstance i = activeInstance();
        if (i != null) i.setTimeStopStartTime(t);
    }

    public static java.util.List<net.minecraft.world.entity.Entity> entities() {
        TransitionEventInstance i = activeInstance();
        return i != null ? i.entities() : new java.util.ArrayList<>();
    }

    public static java.util.Set<UUID> readyClients() {
        TransitionEventInstance i = activeInstance();
        return i != null ? i.readyClients() : new java.util.HashSet<>();
    }

    public static void markClientReady(UUID uuid) {
        TransitionEventInstance i = activeInstance();
        if (i != null) i.readyClients().add(uuid);
    }

    public static java.util.Set<UUID> clientsReadyForNextPhase() {
        TransitionEventInstance i = activeInstance();
        return i != null ? i.clientsReadyForNextPhase() : new java.util.HashSet<>();
    }

    public static java.util.Map<UUID, Integer> clientHologramSurfaces() {
        TransitionEventInstance i = activeInstance();
        return i != null ? i.clientHologramSurfaces() : new java.util.HashMap<>();
    }

    public static void setClientHologramSurface(UUID uuid, int surfaceY) {
        TransitionEventInstance i = activeInstance();
        if (i != null) i.clientHologramSurfaces().put(uuid, surfaceY);
    }

    public static void removeClientHologramSurface(UUID uuid) {
        TransitionEventInstance i = activeInstance();
        if (i != null) i.clientHologramSurfaces().remove(uuid);
    }


    public static TransitionEvent findTransitionFor(ServerPlayer player) {
        TransitionEvent t = activeTransition();
        if (t == null || player == null) return null;
        return t.participants().contains(player.getUUID()) ? t : null;
    }

    public static TimestopZoneEvent findZoneAt(ResourceKey<Level> dim, BlockPos pos) {
        RitualManager.ActiveZone zone = RitualManager.getZoneAt(dim, pos);
        return zone != null ? new ActiveZoneEvent(zone) : null;
    }

    public static TimestopZoneEvent findZoneByBeacon(BlockPos beaconPos) {
        RitualManager.ActiveZone zone = RitualManager.findZoneByBeacon(beaconPos);
        return zone != null ? new ActiveZoneEvent(zone) : null;
    }

    public static TimestopZoneEvent findZoneContaining(ResourceKey<Level> dim, BlockPos pos) {
        RitualManager.ActiveZone zone = RitualManager.findZoneContaining(dim, pos);
        return zone != null ? new ActiveZoneEvent(zone) : null;
    }

    public static boolean hasAnyZoneInDimension(ResourceKey<Level> dim) {
        for (RitualManager.ActiveZone z : RitualManager.activeZones) {
            if (z.dimension().equals(dim)) return true;
        }
        return false;
    }

    public static boolean hasAnyRainingZone(ResourceKey<Level> dim) {
        for (RitualManager.ActiveZone z : RitualManager.activeZones) {
            if (z.dimension() == dim && z.snapRain() > 0.0F) return true;
        }
        return false;
    }

    public static float getLocalRainLevel(ResourceKey<Level> dim, BlockPos pos) {
        TimestopZoneEvent zone = findZoneAt(dim, pos);
        return zone != null ? zone.snapRain() : -1.0F;
    }

    public static void registerZoneLocal(ResourceKey<Level> dim, BlockPos beaconPos, int radiusChunks,
                                         long snapGameTime, long snapClockTicks, float snapRain, float snapThunder) {
        if (RitualManager.findZoneByBeacon(beaconPos) != null) return;
        RitualManager.activeZones.add(new RitualManager.ActiveZone(
            dim, beaconPos, radiusChunks, snapGameTime, snapClockTicks, snapRain, snapThunder
        ));
    }

    public static void unregisterZoneByBeacon(BlockPos beaconPos) {
        RitualManager.activeZones.removeIf(z -> z.beaconPos().equals(beaconPos));
    }

    public static boolean isSkyPortalActive() {
        return net.nostalgia.command.ModCommands.portalDebugState;
    }
}
