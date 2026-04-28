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

    private static volatile TransitionEventInstance activeInstance = null;

    private RitualEventRegistry() {}

    public static TransitionEvent activeTransition() {
        return MonolithicTransitionEvent.activeOrNull();
    }

    public static TransitionEvent activeRitual() {
        return MonolithicTransitionEvent.activeRitualOrNull();
    }

    public static TransitionEventInstance activeInstance() {
        return activeInstance;
    }

    public static TransitionEventInstance startEvent(BlockPos beaconPos, ServerLevel sourceLevel) {
        TransitionEventInstance instance = new TransitionEventInstance(UUID.randomUUID(), beaconPos, sourceLevel);
        activeInstance = instance;
        return instance;
    }

    public static void endEvent() {
        activeInstance = null;
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
