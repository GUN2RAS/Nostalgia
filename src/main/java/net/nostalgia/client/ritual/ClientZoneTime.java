package net.nostalgia.client.ritual;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ClientZoneTime {
    public static final long FADE_MILLIS = 1500L;

    private static boolean wasInZone = false;
    private static boolean hasSnapshot = false;

    private static long snapshotClockTicks = 0L;
    private static long snapshotGameTime = 0L;
    private static float snapshotRain = 0.0f;
    private static float snapshotThunder = 0.0f;

    private static long lastRealClockTicks = 0L;
    private static long lastRealGameTime = 0L;
    private static float lastRealRain = 0.0f;
    private static float lastRealThunder = 0.0f;

    private static long transitionStartMillis = 0L;

    public static boolean isCollapsing = false;
    private static long collapseStartMillis = 0L;
    private static int collapseDurationMs = 0;

    private static long fromClockTicks = 0L;
    private static long toClockTicks = 0L;
    private static long fromGameTime = 0L;
    private static long toGameTime = 0L;
    private static float fromRain = 0.0f;
    private static float toRain = 0.0f;
    private static float fromThunder = 0.0f;
    private static float toThunder = 0.0f;

    private static void maybeTrigger() {
        boolean inZone = ClientFreezeRegions.hasRegions() && ClientFreezeRegions.isLocalPlayerInZone();

        if (inZone && !hasSnapshot) {
            net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
            if (mc.player != null && mc.level != null) {
                net.nostalgia.alphalogic.ritual.TickRateManagerAccess a = ClientFreezeRegions.access();
                if (a != null) {
                    for (net.nostalgia.alphalogic.ritual.FreezeRegion r : a.nostalgia$regions()) {
                        if (r.containsChunk(mc.level.dimension(), mc.player.chunkPosition())) {
                            ClientFreezeRegions.ZoneSnapshot snap = ClientFreezeRegions.snapshots.get(r.beaconPos());
                            if (snap != null) {
                                snapshotClockTicks = snap.clockTicks();
                                snapshotGameTime = snap.gameTime();
                                snapshotRain = snap.rain();
                                snapshotThunder = snap.thunder();
                                hasSnapshot = true;
                                break;
                            }
                        }
                    }
                }
            }
            if (!hasSnapshot) {
                snapshotClockTicks = lastRealClockTicks;
                snapshotGameTime = lastRealGameTime;
                snapshotRain = lastRealRain;
                snapshotThunder = lastRealThunder;
                hasSnapshot = true;
            }
        }

        if (inZone != wasInZone) {
            if (net.nostalgia.client.ritual.ClientRitualEventRegistry.activeTransition() != null && !inZone) {
                wasInZone = inZone;
                return;
            }
            transitionStartMillis = System.currentTimeMillis();
            if (inZone) {
                fromClockTicks = lastRealClockTicks;
                toClockTicks = snapshotClockTicks;
                fromGameTime = lastRealGameTime;
                toGameTime = snapshotGameTime;
                fromRain = lastRealRain;
                toRain = snapshotRain;
                fromThunder = lastRealThunder;
                toThunder = snapshotThunder;
            } else {
                fromClockTicks = snapshotClockTicks;
                toClockTicks = lastRealClockTicks;
                fromGameTime = snapshotGameTime;
                toGameTime = lastRealGameTime;
                fromRain = snapshotRain;
                toRain = lastRealRain;
                fromThunder = snapshotThunder;
                toThunder = lastRealThunder;
            }
            wasInZone = inZone;
        }

        if (!inZone && hasSnapshot && progress() >= 1.0f) {
            hasSnapshot = false;
        }
    }

    public static void updateReals(long realClockTicks, long realGameTime, float realRain, float realThunder) {
        lastRealClockTicks = realClockTicks;
        lastRealGameTime = realGameTime;
        lastRealRain = realRain;
        lastRealThunder = realThunder;
        maybeTrigger();
    }

    private static float progress() {
        if (transitionStartMillis == 0L) return 1.0f;
        long elapsed = System.currentTimeMillis() - transitionStartMillis;
        if (elapsed >= FADE_MILLIS) return 1.0f;
        return (float) elapsed / (float) FADE_MILLIS;
    }

    private static float easeInOut(float t) {
        return 1.0f - (1.0f - t) * (1.0f - t);
    }

    public static long getEffectiveClockTicks(long realClockTicks) {
        maybeTrigger();
        if (net.nostalgia.client.ritual.ClientRitualEventRegistry.activeTransition() != null && hasSnapshot) return snapshotClockTicks;
        boolean inZone = ClientFreezeRegions.isLocalPlayerInZone();
        float p = progress();
        if (p >= 1.0f) {
            if (inZone && hasSnapshot) return snapshotClockTicks;
            return realClockTicks;
        }
        float eased = easeInOut(p);
        long target = inZone ? toClockTicks : realClockTicks;
        return fromClockTicks + (long) ((target - fromClockTicks) * eased);
    }

    public static long getEffectiveGameTime(long realGameTime) {
        maybeTrigger();
        if (net.nostalgia.client.ritual.ClientRitualEventRegistry.activeTransition() != null && hasSnapshot) return snapshotGameTime;
        boolean inZone = ClientFreezeRegions.isLocalPlayerInZone();
        float p = progress();
        if (p >= 1.0f) {
            if (inZone && hasSnapshot) return snapshotGameTime;
            return realGameTime;
        }
        float eased = easeInOut(p);
        long target = inZone ? toGameTime : realGameTime;
        return fromGameTime + (long) ((target - fromGameTime) * eased);
    }

    public static float getEffectiveRain(float realRain) {
        maybeTrigger();
        if (net.nostalgia.client.ritual.ClientRitualEventRegistry.activeTransition() != null && hasSnapshot) return snapshotRain;
        boolean inZone = ClientFreezeRegions.isLocalPlayerInZone();
        float p = progress();
        if (p >= 1.0f) {
            if (inZone && hasSnapshot) return snapshotRain;
            return realRain;
        }
        float eased = easeInOut(p);
        float target = inZone ? toRain : realRain;
        return fromRain + (target - fromRain) * eased;
    }

    public static float getEffectiveThunder(float realThunder) {
        maybeTrigger();
        if (net.nostalgia.client.ritual.ClientRitualEventRegistry.activeTransition() != null && hasSnapshot) return snapshotThunder;
        boolean inZone = ClientFreezeRegions.isLocalPlayerInZone();
        float p = progress();
        if (p >= 1.0f) {
            if (inZone && hasSnapshot) return snapshotThunder;
            return realThunder;
        }
        float eased = easeInOut(p);
        float target = inZone ? toThunder : realThunder;
        return fromThunder + (target - fromThunder) * eased;
    }

    public static boolean isActive() {
        return hasSnapshot || (transitionStartMillis > 0L && (System.currentTimeMillis() - transitionStartMillis) < FADE_MILLIS);
    }

    public static boolean isTransitioning() {
        if (transitionStartMillis == 0L) return false;
        return (System.currentTimeMillis() - transitionStartMillis) < FADE_MILLIS;
    }

    public static void clear() {
        wasInZone = false;
        hasSnapshot = false;
        transitionStartMillis = 0L;
        snapshotClockTicks = 0L;
        snapshotGameTime = 0L;
        snapshotRain = 0.0f;
        snapshotThunder = 0.0f;
        lastRealClockTicks = 0L;
        lastRealGameTime = 0L;
        lastRealRain = 0.0f;
        lastRealThunder = 0.0f;
        isCollapsing = false;
    }

    public static void forceInstantSnapshot(long gameTime, long clockTicks, float rain, float thunder) {
        snapshotGameTime = gameTime;
        snapshotClockTicks = clockTicks;
        snapshotRain = rain;
        snapshotThunder = thunder;
        hasSnapshot = true;
        wasInZone = true;
        transitionStartMillis = 0L;
    }

    public static void startZoneCollapse(int durationMs) {
        isCollapsing = true;
        collapseStartMillis = System.currentTimeMillis();
        collapseDurationMs = durationMs;
    }

    public static void tickCollapse() {
        if (!isCollapsing) return;
        long elapsed = System.currentTimeMillis() - collapseStartMillis;
        float progress = Math.min(1.0f, (float) elapsed / collapseDurationMs);
        int newRadius = (int) (5 * (1.0f - progress));
        
        net.nostalgia.alphalogic.ritual.TickRateManagerAccess access = ClientFreezeRegions.access();
        if (access != null) {
            java.util.List<net.nostalgia.alphalogic.ritual.FreezeRegion> regions = new java.util.ArrayList<>(access.nostalgia$regions());
            if (!regions.isEmpty()) {
                net.nostalgia.alphalogic.ritual.FreezeRegion old = regions.get(0);
                if (old.chunkRadius() != newRadius) {
                    access.nostalgia$clearRegions();
                    access.nostalgia$addRegion(new net.nostalgia.alphalogic.ritual.FreezeRegion(old.dimension(), old.beaconPos(), newRadius));
                    net.nostalgia.network.NostalgiaNetworking.nostalgia$markZoneChunksDirty(net.minecraft.client.Minecraft.getInstance(), old.beaconPos(), 5);
                }
            }
        }
        
        if (progress >= 1.0f) {
            if (access != null) access.nostalgia$clearRegions();
            isCollapsing = false;
        }
    }
}
