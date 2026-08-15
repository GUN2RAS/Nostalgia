package net.nostalgia.client.events.echo;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.nostalgia.alphalogic.ritual.TimestopZoneManager;

public class TrailManager {
  public static final Map<UUID, LinkedList<TrailManager.TrailSnapshot>> TRAILS = new ConcurrentHashMap<>();
  private static final Map<UUID, Vec3> lastSnapshotPos = new ConcurrentHashMap<>();

  public TrailManager() {
  }

  public static void tick(Iterable<? extends Player> players) {
    if (players == null) {
      TRAILS.clear();
      lastSnapshotPos.clear();
    } else {
      Set<UUID> activePlayers = new HashSet<>();

      for (Player player : players) {
        if (player != null && player.level() != null) {
          TimestopZoneManager.ActiveZone zone = TimestopZoneManager.findZoneContaining(player.level().dimension(), player.blockPosition());
          if (zone != null) {
            UUID uuid = player.getUUID();
            activePlayers.add(uuid);
            LinkedList<TrailManager.TrailSnapshot> trail = TRAILS.computeIfAbsent(uuid, k -> new LinkedList<>());
            Vec3 currPos = player.position();
            Vec3 lastPos = lastSnapshotPos.getOrDefault(uuid, Vec3.ZERO);
            Iterator<TrailManager.TrailSnapshot> it = trail.iterator();

            while (it.hasNext()) {
              TrailManager.TrailSnapshot p = it.next();
              p.alpha -= 0.05F;
              if (p.alpha <= 0.0F) {
                it.remove();
              }
            }

            if (trail.isEmpty() || currPos.distanceToSqr(lastPos) > 0.09) {
              trail.addFirst(new TrailManager.TrailSnapshot(currPos, player.yBodyRot, 0.4F));
              lastSnapshotPos.put(uuid, currPos);
            }

            while (trail.size() > 5) {
              trail.removeLast();
            }
          }
        }
      }

      TRAILS.keySet().removeIf(uuidx -> !activePlayers.contains(uuidx));
      lastSnapshotPos.keySet().removeIf(uuidx -> !activePlayers.contains(uuidx));
    }
  }

  public static class TrailSnapshot {
    public final Vec3 pos;
    public final float yBodyRot;
    public float alpha;

    public TrailSnapshot(Vec3 pos, float yBodyRot, float alpha) {
      this.pos = pos;
      this.yBodyRot = yBodyRot;
      this.alpha = alpha;
    }
  }
}
