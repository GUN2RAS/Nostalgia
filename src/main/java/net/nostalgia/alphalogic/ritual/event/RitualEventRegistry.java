package net.nostalgia.alphalogic.ritual.event;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.nostalgia.alphalogic.ritual.ActiveZoneEvent;
import net.nostalgia.alphalogic.ritual.MonolithicEchoRitualEvent;
import net.nostalgia.alphalogic.ritual.EchoRitualManager;
import net.nostalgia.alphalogic.ritual.TimestopZoneManager;
import net.nostalgia.alphalogic.ritual.EchoRitualEventInstance;

import java.util.UUID;

public final class RitualEventRegistry {

    private static final java.util.concurrent.ConcurrentHashMap<UUID, EchoRitualEventInstance> activeInstances = new java.util.concurrent.ConcurrentHashMap<>();

    private RitualEventRegistry() {}

    public static EchoRitualEvent activeTransition() {
        return MonolithicEchoRitualEvent.activeOrNull();
    }

    public static EchoRitualEvent activeRitual() {
        return MonolithicEchoRitualEvent.activeRitualOrNull();
    }

    public static EchoRitualEventInstance activeInstance() {
        for (EchoRitualEventInstance i : activeInstances.values()) return i;
        return null;
    }

    public static java.util.Collection<EchoRitualEventInstance> allActiveInstances() {
        return activeInstances.values();
    }

    public static java.util.Collection<EchoRitualEventInstance> allInstances() {
        return activeInstances.values();
    }

    public static EchoRitualEventInstance findInstanceByBeacon(BlockPos beaconPos) {
        if (beaconPos == null) return null;
        for (EchoRitualEventInstance i : activeInstances.values()) {
            if (beaconPos.equals(i.beaconPos())) return i;
        }
        return null;
    }

    public static EchoRitualEventInstance findInstanceForParticipant(UUID playerUuid) {
        for (EchoRitualEventInstance i : activeInstances.values()) {
            if (i.participants().contains(playerUuid)) return i;
        }
        return null;
    }

    public static EchoRitualEventInstance startEvent(BlockPos beaconPos, ServerLevel sourceLevel) {
        EchoRitualEventInstance existing = findInstanceByBeacon(beaconPos);
        if (existing != null) {
            if (sourceLevel != null && existing.sourceLevel() == null) {
                existing.setSourceLevel(sourceLevel);
            }
            return existing;
        }
        EchoRitualEventInstance instance = new EchoRitualEventInstance(UUID.randomUUID(), beaconPos, sourceLevel);
        activeInstances.put(instance.id(), instance);
        return instance;
    }

    public static void endEvent() {
        EchoRitualEventInstance first = activeInstance();
        if (first != null) activeInstances.remove(first.id());
    }

    public static void endEvent(UUID id) {
        if (id != null) activeInstances.remove(id);
    }

    public static void endAllEvents() {
        activeInstances.clear();
    }

    public static java.util.Set<UUID> participants() {
        EchoRitualEventInstance i = activeInstance();
        return i != null ? i.participants() : new java.util.HashSet<>();
    }

    public static boolean isParticipant(net.minecraft.world.entity.Entity entity) {
        return isParticipantAny(entity);
    }

    public static boolean isParticipantAny(net.minecraft.world.entity.Entity entity) {
        if (entity == null) return false;
        java.util.UUID uuid = entity.getUUID();
        for (EchoRitualEventInstance i : activeInstances.values()) {
            if (i.participants().contains(uuid)) return true;
        }
        return false;
    }

    public static boolean isParticipantAny(java.util.UUID uuid) {
        if (uuid == null) return false;
        for (EchoRitualEventInstance i : activeInstances.values()) {
            if (i.participants().contains(uuid)) return true;
        }
        return false;
    }

    public static boolean inSameInstance(java.util.UUID a, java.util.UUID b) {
        if (a == null || b == null) return false;
        EchoRitualEventInstance ia = findInstanceForParticipant(a);
        EchoRitualEventInstance ib = findInstanceForParticipant(b);
        return ia != null && ia == ib;
    }

    public static java.util.Set<java.util.UUID> allParticipants() {
        java.util.Set<java.util.UUID> out = new java.util.HashSet<>();
        for (EchoRitualEventInstance i : activeInstances.values()) {
            out.addAll(i.participants());
        }
        return out;
    }

    public static boolean addParticipant(UUID uuid) {
        EchoRitualEventInstance i = activeInstance();
        return i != null && i.participants().add(uuid);
    }

    public static boolean removeParticipantUuid(UUID uuid) {
        EchoRitualEventInstance i = activeInstance();
        return i != null && i.participants().remove(uuid);
    }

    public static void clearParticipants() {
        EchoRitualEventInstance i = activeInstance();
        if (i != null) i.participants().clear();
    }

    public static void setParticipants(java.util.Collection<UUID> uuids) {
        EchoRitualEventInstance i = activeInstance();
        if (i != null) {
            i.participants().clear();
            i.participants().addAll(uuids);
        }
    }

    public static int offsetX() {
        EchoRitualEventInstance i = activeInstance();
        return i != null ? i.offsetX() : 0;
    }

    public static int yOffset() {
        EchoRitualEventInstance i = activeInstance();
        return i != null ? i.yOffset() : 0;
    }

    public static int offsetZ() {
        EchoRitualEventInstance i = activeInstance();
        return i != null ? i.offsetZ() : 0;
    }

    public static void setOffsets(int dx, int dy, int dz) {
        EchoRitualEventInstance i = activeInstance();
        if (i != null) i.setOffsets(dx, dy, dz);
    }

    public static boolean isTransitioning() {
        EchoRitualEventInstance i = activeInstance();
        return i != null && i.isTransitioning();
    }

    public static void setTransitioning(boolean v) {
        EchoRitualEventInstance i = activeInstance();
        if (i != null) i.setTransitioning(v);
    }

    public static BlockPos ritualCenter() {
        EchoRitualEventInstance i = activeInstance();
        return i != null ? i.beaconPos() : null;
    }

    public static void setRitualCenter(BlockPos pos) {
        EchoRitualEventInstance i = activeInstance();
        if (i != null) i.setBeaconPos(pos);
    }

    public static ServerLevel transitionTarget() {
        EchoRitualEventInstance i = activeInstance();
        return i != null ? i.targetServerLevel() : null;
    }

    public static void setTransitionTarget(ServerLevel level) {
        EchoRitualEventInstance i = activeInstance();
        if (i != null) i.setTargetServerLevel(level);
    }

    public static String transitionDimensionId() {
        EchoRitualEventInstance i = activeInstance();
        return i != null ? i.targetDimensionId() : null;
    }

    public static void setTransitionDimensionId(String id) {
        EchoRitualEventInstance i = activeInstance();
        if (i != null) i.setTargetDimensionId(id);
    }

    public static BlockPos transitionTargetPos() {
        EchoRitualEventInstance i = activeInstance();
        return i != null ? i.targetPos() : null;
    }

    public static void setTransitionTargetPos(BlockPos pos) {
        EchoRitualEventInstance i = activeInstance();
        if (i != null) i.setTargetPos(pos);
    }

    public static net.nostalgia.alphalogic.ritual.EchoRitualManager.State state() {
        EchoRitualEventInstance i = activeInstance();
        return i != null ? i.state() : net.nostalgia.alphalogic.ritual.EchoRitualManager.State.INACTIVE;
    }

    public static void setState(net.nostalgia.alphalogic.ritual.EchoRitualManager.State s) {
        EchoRitualEventInstance i = activeInstance();
        if (i != null) i.setState(s);
    }

    public static int currentSyncPhase() {
        EchoRitualEventInstance i = activeInstance();
        return i != null ? i.phase() : 0;
    }

    public static void setCurrentSyncPhase(int phase) {
        EchoRitualEventInstance i = activeInstance();
        if (i != null) i.setPhase(phase);
    }

    public static long phaseStartTime() {
        EchoRitualEventInstance i = activeInstance();
        return i != null ? i.phaseStartTime() : 0L;
    }

    public static void setPhaseStartTime(long t) {
        EchoRitualEventInstance i = activeInstance();
        if (i != null) i.setPhaseStartTime(t);
    }

    public static long timeStopStartTime() {
        EchoRitualEventInstance i = activeInstance();
        return i != null ? i.timeStopStartTime() : 0L;
    }

    public static void setTimeStopStartTime(long t) {
        EchoRitualEventInstance i = activeInstance();
        if (i != null) i.setTimeStopStartTime(t);
    }

    public static java.util.List<net.minecraft.world.entity.Entity> entities() {
        EchoRitualEventInstance i = activeInstance();
        return i != null ? i.entities() : new java.util.ArrayList<>();
    }

    public static java.util.Set<UUID> readyClients() {
        EchoRitualEventInstance i = activeInstance();
        return i != null ? i.readyClients() : new java.util.HashSet<>();
    }

    public static void markClientReady(UUID uuid) {
        EchoRitualEventInstance i = activeInstance();
        if (i != null) i.readyClients().add(uuid);
    }

    public static java.util.Set<UUID> clientsReadyForNextPhase() {
        EchoRitualEventInstance i = activeInstance();
        return i != null ? i.clientsReadyForNextPhase() : new java.util.HashSet<>();
    }

    public static java.util.Map<UUID, Integer> clientHologramSurfaces() {
        EchoRitualEventInstance i = activeInstance();
        return i != null ? i.clientHologramSurfaces() : new java.util.HashMap<>();
    }

    public static void setClientHologramSurface(UUID uuid, int surfaceY) {
        EchoRitualEventInstance i = findInstanceForParticipant(uuid);
        if (i == null) i = activeInstance();
        if (i != null) i.clientHologramSurfaces().put(uuid, surfaceY);
    }

    public static void removeClientHologramSurface(UUID uuid) {
        EchoRitualEventInstance i = findInstanceForParticipant(uuid);
        if (i == null) i = activeInstance();
        if (i != null) i.clientHologramSurfaces().remove(uuid);
    }


    public static EchoRitualEvent findTransitionFor(ServerPlayer player) {
        EchoRitualEvent t = activeTransition();
        if (t == null || player == null) return null;
        return t.participants().contains(player.getUUID()) ? t : null;
    }

    public static TimestopZoneEvent findZoneAt(ResourceKey<Level> dim, BlockPos pos) {
        TimestopZoneManager.ActiveZone zone = TimestopZoneManager.getZoneAt(dim, pos);
        return zone != null ? new ActiveZoneEvent(zone) : null;
    }

    public static TimestopZoneEvent findZoneByBeacon(BlockPos beaconPos) {
        TimestopZoneManager.ActiveZone zone = TimestopZoneManager.findZoneByBeacon(beaconPos);
        return zone != null ? new ActiveZoneEvent(zone) : null;
    }

    public static TimestopZoneEvent findZoneContaining(ResourceKey<Level> dim, BlockPos pos) {
        TimestopZoneManager.ActiveZone zone = TimestopZoneManager.findZoneContaining(dim, pos);
        return zone != null ? new ActiveZoneEvent(zone) : null;
    }

    public static boolean hasAnyZoneInDimension(ResourceKey<Level> dim) {
        for (TimestopZoneManager.ActiveZone z : TimestopZoneManager.activeZones) {
            if (z.dimension().equals(dim)) return true;
        }
        return false;
    }

    public static boolean hasAnyRainingZone(ResourceKey<Level> dim) {
        for (TimestopZoneManager.ActiveZone z : TimestopZoneManager.activeZones) {
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
        if (TimestopZoneManager.findZoneByBeacon(beaconPos) != null) return;
        TimestopZoneManager.activeZones.add(new TimestopZoneManager.ActiveZone(
            dim, beaconPos, radiusChunks, snapGameTime, snapClockTicks, snapRain, snapThunder
        ));
    }

    public static void unregisterZoneByBeacon(BlockPos beaconPos) {
        TimestopZoneManager.activeZones.removeIf(z -> z.beaconPos().equals(beaconPos));
    }

    public static boolean isSkyPortalActive() {
        return net.nostalgia.alphalogic.ritual.SkyPortalManager.isAnyActive();
    }
}
