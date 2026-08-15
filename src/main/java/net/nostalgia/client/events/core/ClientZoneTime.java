package net.nostalgia.client.events.core;

import java.util.ArrayList;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.nostalgia.alphalogic.ritual.FreezeRegion;
import net.nostalgia.alphalogic.ritual.TickRateManagerAccess;
import net.nostalgia.network.NostalgiaClientNetworking;

@Environment(EnvType.CLIENT)
public class ClientZoneTime {
  public static final long FADE_MILLIS = 1500L;
  private static boolean wasInZone = false;
  private static boolean hasSnapshot = false;
  private static long snapshotClockTicks = 0L;
  private static long snapshotGameTime = 0L;
  private static float snapshotRain = 0.0F;
  private static float snapshotThunder = 0.0F;
  private static long lastRealClockTicks = 0L;
  private static long lastRealGameTime = 0L;
  private static float lastRealRain = 0.0F;
  private static float lastRealThunder = 0.0F;
  private static long transitionStartMillis = 0L;
  public static boolean isCollapsing = false;
  private static long collapseStartMillis = 0L;
  private static int collapseDurationMs = 0;
  private static long fromClockTicks = 0L;
  private static long toClockTicks = 0L;
  private static long fromGameTime = 0L;
  private static long toGameTime = 0L;
  private static float fromRain = 0.0F;
  private static float toRain = 0.0F;
  private static float fromThunder = 0.0F;
  private static float toThunder = 0.0F;

  public ClientZoneTime() {
  }

  private static void maybeTrigger() {
    boolean inZone = ClientFreezeRegions.hasRegions() && ClientFreezeRegions.isLocalPlayerInZone();
    if (inZone && !hasSnapshot) {
      Minecraft mc = Minecraft.getInstance();
      if (mc.player != null && mc.level != null) {
        TickRateManagerAccess a = ClientFreezeRegions.access();
        if (a != null) {
          for (FreezeRegion r : a.nostalgia$regions()) {
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
      if (ClientRitualEventRegistry.activeTransition() != null && !inZone) {
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

    if (!inZone && hasSnapshot && progress() >= 1.0F) {
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
    if (transitionStartMillis == 0L) {
      return 1.0F;
    } else {
      long elapsed = System.currentTimeMillis() - transitionStartMillis;
      return elapsed >= 1500L ? 1.0F : (float)elapsed / 1500.0F;
    }
  }

  private static float easeInOut(float t) {
    return 1.0F - (1.0F - t) * (1.0F - t);
  }

  public static long getEffectiveClockTicks(long realClockTicks) {
    maybeTrigger();
    if (ClientRitualEventRegistry.activeTransition() != null && hasSnapshot) {
      return snapshotClockTicks;
    } else {
      boolean inZone = ClientFreezeRegions.isLocalPlayerInZone();
      float p = progress();
      if (p >= 1.0F) {
        return inZone && hasSnapshot ? snapshotClockTicks : realClockTicks;
      } else {
        float eased = easeInOut(p);
        long target = inZone ? toClockTicks : realClockTicks;
        return fromClockTicks + (long)((float)(target - fromClockTicks) * eased);
      }
    }
  }

  public static long getEffectiveGameTime(long realGameTime) {
    maybeTrigger();
    if (ClientRitualEventRegistry.activeTransition() != null && hasSnapshot) {
      return snapshotGameTime;
    } else {
      boolean inZone = ClientFreezeRegions.isLocalPlayerInZone();
      float p = progress();
      if (p >= 1.0F) {
        return inZone && hasSnapshot ? snapshotGameTime : realGameTime;
      } else {
        float eased = easeInOut(p);
        long target = inZone ? toGameTime : realGameTime;
        return fromGameTime + (long)((float)(target - fromGameTime) * eased);
      }
    }
  }

  public static float getEffectiveRain(float realRain) {
    maybeTrigger();
    if (ClientRitualEventRegistry.activeTransition() != null && hasSnapshot) {
      return snapshotRain;
    } else {
      boolean inZone = ClientFreezeRegions.isLocalPlayerInZone();
      float p = progress();
      if (p >= 1.0F) {
        return inZone && hasSnapshot ? snapshotRain : realRain;
      } else {
        float eased = easeInOut(p);
        float target = inZone ? toRain : realRain;
        return fromRain + (target - fromRain) * eased;
      }
    }
  }

  public static float getEffectiveThunder(float realThunder) {
    maybeTrigger();
    if (ClientRitualEventRegistry.activeTransition() != null && hasSnapshot) {
      return snapshotThunder;
    } else {
      boolean inZone = ClientFreezeRegions.isLocalPlayerInZone();
      float p = progress();
      if (p >= 1.0F) {
        return inZone && hasSnapshot ? snapshotThunder : realThunder;
      } else {
        float eased = easeInOut(p);
        float target = inZone ? toThunder : realThunder;
        return fromThunder + (target - fromThunder) * eased;
      }
    }
  }

  public static boolean isActive() {
    return hasSnapshot || transitionStartMillis > 0L && System.currentTimeMillis() - transitionStartMillis < 1500L;
  }

  public static boolean isTransitioning() {
    return transitionStartMillis == 0L ? false : System.currentTimeMillis() - transitionStartMillis < 1500L;
  }

  public static void clear() {
    wasInZone = false;
    hasSnapshot = false;
    transitionStartMillis = 0L;
    snapshotClockTicks = 0L;
    snapshotGameTime = 0L;
    snapshotRain = 0.0F;
    snapshotThunder = 0.0F;
    lastRealClockTicks = 0L;
    lastRealGameTime = 0L;
    lastRealRain = 0.0F;
    lastRealThunder = 0.0F;
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
    if (isCollapsing) {
      long elapsed = System.currentTimeMillis() - collapseStartMillis;
      float progress = Math.min(1.0F, (float)elapsed / collapseDurationMs);
      int newRadius = (int)(5.0F * (1.0F - progress));
      TickRateManagerAccess access = ClientFreezeRegions.access();
      if (access != null) {
        List<FreezeRegion> regions = new ArrayList<>(access.nostalgia$regions());
        if (!regions.isEmpty()) {
          FreezeRegion old = regions.get(0);
          if (old.chunkRadius() != newRadius) {
            access.nostalgia$clearRegions();
            access.nostalgia$addRegion(new FreezeRegion(old.dimension(), old.beaconPos(), newRadius));
            NostalgiaClientNetworking.nostalgia$markZoneChunksDirty(Minecraft.getInstance(), old.beaconPos(), 5);
          }
        }
      }

      if (progress >= 1.0F) {
        if (access != null) {
          access.nostalgia$clearRegions();
        }

        isCollapsing = false;
      }
    }
  }
}
