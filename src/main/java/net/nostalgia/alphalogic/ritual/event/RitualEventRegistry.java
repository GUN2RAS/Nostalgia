package net.nostalgia.alphalogic.ritual.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.nostalgia.alphalogic.ritual.ActiveZoneEvent;
import net.nostalgia.alphalogic.ritual.EchoRitualEventInstance;
import net.nostalgia.alphalogic.ritual.EchoRitualManager;
import net.nostalgia.alphalogic.ritual.MonolithicEchoRitualEvent;
import net.nostalgia.alphalogic.ritual.SkyPortalManager;
import net.nostalgia.alphalogic.ritual.TimestopZoneManager;

public final class RitualEventRegistry {
  private static final ConcurrentHashMap<UUID, EchoRitualEventInstance> activeInstances = new ConcurrentHashMap<>();
  private static volatile UUID activeInstanceId = null;

  private RitualEventRegistry() {
  }

  public static EchoRitualEvent activeTransition() {
    return MonolithicEchoRitualEvent.activeOrNull();
  }

  public static EchoRitualEvent activeRitual() {
    return MonolithicEchoRitualEvent.activeRitualOrNull();
  }

  public static EchoRitualEventInstance activeInstance() {
    UUID id = activeInstanceId;
    if (id != null) {
      EchoRitualEventInstance inst = activeInstances.get(id);
      if (inst != null) {
        return inst;
      }
    }

    Iterator var3 = activeInstances.values().iterator();
    return var3.hasNext() ? (EchoRitualEventInstance)var3.next() : null;
  }

  public static Collection<EchoRitualEventInstance> allActiveInstances() {
    return activeInstances.values();
  }

  public static Collection<EchoRitualEventInstance> allInstances() {
    return activeInstances.values();
  }

  public static EchoRitualEventInstance findInstanceByBeacon(BlockPos beaconPos) {
    if (beaconPos == null) {
      return null;
    } else {
      for (EchoRitualEventInstance i : activeInstances.values()) {
        if (beaconPos.equals(i.beaconPos())) {
          return i;
        }
      }

      return null;
    }
  }

  public static EchoRitualEventInstance findInstanceForParticipant(UUID playerUuid) {
    for (EchoRitualEventInstance i : activeInstances.values()) {
      if (i.participants().contains(playerUuid)) {
        return i;
      }
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
    } else {
      EchoRitualEventInstance instance = new EchoRitualEventInstance(UUID.randomUUID(), beaconPos, sourceLevel);
      activeInstances.put(instance.id(), instance);
      activeInstanceId = instance.id();
      return instance;
    }
  }

  public static void endEvent() {
    EchoRitualEventInstance first = activeInstance();
    if (first != null) {
      activeInstances.remove(first.id());
      if (first.id().equals(activeInstanceId)) {
        activeInstanceId = null;
      }
    }
  }

  public static void endEvent(UUID id) {
    if (id != null) {
      activeInstances.remove(id);
      if (id.equals(activeInstanceId)) {
        activeInstanceId = null;
      }
    }
  }

  public static void endAllEvents() {
    activeInstances.clear();
    activeInstanceId = null;
  }

  public static Set<UUID> participants() {
    EchoRitualEventInstance i = activeInstance();
    return (Set<UUID>)(i != null ? i.participants() : new HashSet<>());
  }

  public static boolean isParticipant(Entity entity) {
    return isParticipantAny(entity);
  }

  public static boolean isParticipantAny(Entity entity) {
    if (entity == null) {
      return false;
    } else {
      UUID uuid = entity.getUUID();

      for (EchoRitualEventInstance i : activeInstances.values()) {
        if (i.participants().contains(uuid)) {
          return true;
        }
      }

      return false;
    }
  }

  public static boolean isParticipantAny(UUID uuid) {
    if (uuid == null) {
      return false;
    } else {
      for (EchoRitualEventInstance i : activeInstances.values()) {
        if (i.participants().contains(uuid)) {
          return true;
        }
      }

      return false;
    }
  }

  public static boolean inSameInstance(UUID a, UUID b) {
    if (a != null && b != null) {
      EchoRitualEventInstance ia = findInstanceForParticipant(a);
      EchoRitualEventInstance ib = findInstanceForParticipant(b);
      return ia != null && ia == ib;
    } else {
      return false;
    }
  }

  public static Set<UUID> allParticipants() {
    Set<UUID> out = new HashSet<>();

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
    if (i != null) {
      i.participants().clear();
    }
  }

  public static void setParticipants(Collection<UUID> uuids) {
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
    if (i != null) {
      i.setOffsets(dx, dy, dz);
    }
  }

  public static boolean isTransitioning() {
    EchoRitualEventInstance i = activeInstance();
    return i != null && i.isTransitioning();
  }

  public static void setTransitioning(boolean v) {
    EchoRitualEventInstance i = activeInstance();
    if (i != null) {
      i.setTransitioning(v);
    }
  }

  public static BlockPos ritualCenter() {
    EchoRitualEventInstance i = activeInstance();
    return i != null ? i.beaconPos() : null;
  }

  public static void setRitualCenter(BlockPos pos) {
    EchoRitualEventInstance i = activeInstance();
    if (i != null) {
      i.setBeaconPos(pos);
    }
  }

  public static ServerLevel transitionTarget() {
    EchoRitualEventInstance i = activeInstance();
    return i != null ? i.targetServerLevel() : null;
  }

  public static void setTransitionTarget(ServerLevel level) {
    EchoRitualEventInstance i = activeInstance();
    if (i != null) {
      i.setTargetServerLevel(level);
    }
  }

  public static String transitionDimensionId() {
    EchoRitualEventInstance i = activeInstance();
    return i != null ? i.targetDimensionId() : null;
  }

  public static void setTransitionDimensionId(String id) {
    EchoRitualEventInstance i = activeInstance();
    if (i != null) {
      i.setTargetDimensionId(id);
    }
  }

  public static BlockPos transitionTargetPos() {
    EchoRitualEventInstance i = activeInstance();
    return i != null ? i.targetPos() : null;
  }

  public static void setTransitionTargetPos(BlockPos pos) {
    EchoRitualEventInstance i = activeInstance();
    if (i != null) {
      i.setTargetPos(pos);
    }
  }

  public static EchoRitualManager.State state() {
    EchoRitualEventInstance i = activeInstance();
    return i != null ? i.state() : EchoRitualManager.State.INACTIVE;
  }

  public static void setState(EchoRitualManager.State s) {
    EchoRitualEventInstance i = activeInstance();
    if (i != null) {
      i.setState(s);
    }
  }

  public static int currentSyncPhase() {
    EchoRitualEventInstance i = activeInstance();
    return i != null ? i.phase() : 0;
  }

  public static void setCurrentSyncPhase(int phase) {
    EchoRitualEventInstance i = activeInstance();
    if (i != null) {
      i.setPhase(phase);
    }
  }

  public static long phaseStartTime() {
    EchoRitualEventInstance i = activeInstance();
    return i != null ? i.phaseStartTime() : 0L;
  }

  public static void setPhaseStartTime(long t) {
    EchoRitualEventInstance i = activeInstance();
    if (i != null) {
      i.setPhaseStartTime(t);
    }
  }

  public static long timeStopStartTime() {
    EchoRitualEventInstance i = activeInstance();
    return i != null ? i.timeStopStartTime() : 0L;
  }

  public static void setTimeStopStartTime(long t) {
    EchoRitualEventInstance i = activeInstance();
    if (i != null) {
      i.setTimeStopStartTime(t);
    }
  }

  public static List<Entity> entities() {
    EchoRitualEventInstance i = activeInstance();
    return (List<Entity>)(i != null ? i.entities() : new ArrayList<>());
  }

  public static Set<UUID> readyClients() {
    EchoRitualEventInstance i = activeInstance();
    return (Set<UUID>)(i != null ? i.readyClients() : new HashSet<>());
  }

  public static void markClientReady(UUID uuid) {
    EchoRitualEventInstance i = activeInstance();
    if (i != null) {
      i.readyClients().add(uuid);
    }
  }

  public static Set<UUID> clientsReadyForNextPhase() {
    EchoRitualEventInstance i = activeInstance();
    return (Set<UUID>)(i != null ? i.clientsReadyForNextPhase() : new HashSet<>());
  }

  public static Map<UUID, Integer> clientHologramSurfaces() {
    EchoRitualEventInstance i = activeInstance();
    return (Map<UUID, Integer>)(i != null ? i.clientHologramSurfaces() : new HashMap<>());
  }

  public static void setClientHologramSurface(UUID uuid, int surfaceY) {
    EchoRitualEventInstance i = findInstanceForParticipant(uuid);
    if (i == null) {
      i = activeInstance();
    }

    if (i != null) {
      i.clientHologramSurfaces().put(uuid, surfaceY);
    }
  }

  public static void removeClientHologramSurface(UUID uuid) {
    EchoRitualEventInstance i = findInstanceForParticipant(uuid);
    if (i == null) {
      i = activeInstance();
    }

    if (i != null) {
      i.clientHologramSurfaces().remove(uuid);
    }
  }

  public static EchoRitualEvent findTransitionFor(ServerPlayer player) {
    EchoRitualEvent t = activeTransition();
    if (t != null && player != null) {
      return t.participants().contains(player.getUUID()) ? t : null;
    } else {
      return null;
    }
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
      if (z.dimension().equals(dim)) {
        return true;
      }
    }

    return false;
  }

  public static boolean hasAnyRainingZone(ResourceKey<Level> dim) {
    for (TimestopZoneManager.ActiveZone z : TimestopZoneManager.activeZones) {
      if (z.dimension() == dim && z.snapRain() > 0.0F) {
        return true;
      }
    }

    return false;
  }

  public static float getLocalRainLevel(ResourceKey<Level> dim, BlockPos pos) {
    TimestopZoneEvent zone = findZoneAt(dim, pos);
    return zone != null ? zone.snapRain() : -1.0F;
  }

  public static void registerZoneLocal(
    ResourceKey<Level> dim, BlockPos beaconPos, int radiusChunks, long snapGameTime, long snapClockTicks, float snapRain, float snapThunder
  ) {
    if (TimestopZoneManager.findZoneByBeacon(beaconPos) == null) {
      TimestopZoneManager.activeZones
        .add(new TimestopZoneManager.ActiveZone(dim, beaconPos, radiusChunks, snapGameTime, snapClockTicks, snapRain, snapThunder));
    }
  }

  public static void unregisterZoneByBeacon(BlockPos beaconPos) {
    TimestopZoneManager.activeZones.removeIf(z -> z.beaconPos().equals(beaconPos));
  }

  public static boolean isSkyPortalActive() {
    return SkyPortalManager.isAnyActive();
  }
}
