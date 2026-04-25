package net.nostalgia.client.ritual;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.nostalgia.alphalogic.ritual.RitualManager;

import java.util.LinkedList;

public class TrailManager {
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

    public static final java.util.Map<java.util.UUID, LinkedList<TrailSnapshot>> TRAILS = new java.util.concurrent.ConcurrentHashMap<>();
    private static final java.util.Map<java.util.UUID, Vec3> lastSnapshotPos = new java.util.concurrent.ConcurrentHashMap<>();

    public static void tick(Iterable<? extends Player> players) {
        if (players == null) {
            TRAILS.clear();
            lastSnapshotPos.clear();
            return;
        }

        java.util.Set<java.util.UUID> activePlayers = new java.util.HashSet<>();

        for (Player player : players) {
            if (player == null || player.level() == null) continue;

            RitualManager.ActiveZone zone = RitualManager.findZoneContaining(player.level().dimension(), player.blockPosition());
            if (zone == null) continue;

            java.util.UUID uuid = player.getUUID();
            activePlayers.add(uuid);

            LinkedList<TrailSnapshot> trail = TRAILS.computeIfAbsent(uuid, k -> new LinkedList<>());
            Vec3 currPos = player.position();
            Vec3 lastPos = lastSnapshotPos.getOrDefault(uuid, Vec3.ZERO);
            
            
            for (var it = trail.iterator(); it.hasNext(); ) {
                TrailSnapshot p = it.next();
                p.alpha -= 0.05f; 
                if (p.alpha <= 0) {
                    it.remove();
                }
            }

            
            if (trail.isEmpty() || currPos.distanceToSqr(lastPos) > (0.3 * 0.3)) {
                trail.addFirst(new TrailSnapshot(currPos, player.yBodyRot, 0.4f));
                lastSnapshotPos.put(uuid, currPos);
            }

            
            while (trail.size() > 5) {
                trail.removeLast();
            }
        }

        
        TRAILS.keySet().removeIf(uuid -> !activePlayers.contains(uuid));
        lastSnapshotPos.keySet().removeIf(uuid -> !activePlayers.contains(uuid));
    }
}
