package net.nostalgia.alphalogic.ritual;

import com.example.api.Gravity;
import com.example.api.GravityChanger;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.TicketType;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.phys.Vec3;
import net.nostalgia.network.S2CSkyPortalPayload;
import org.slf4j.LoggerFactory;

public class SkyPortalManager {
  private static final ConcurrentHashMap<UUID, SkyPortalEventInstance> activePortals = new ConcurrentHashMap<>();
  private static final ConcurrentHashMap<UUID, Long> lastLandingTime = new ConcurrentHashMap<>();

  public SkyPortalManager() {
  }

  public static SkyPortalEventInstance findByUUID(UUID id) {
    return id != null ? activePortals.get(id) : null;
  }

  public static SkyPortalEventInstance findByCenter(BlockPos center, String sourceDimension) {
    if (center == null) return null;
    for (SkyPortalEventInstance inst : activePortals.values()) {
      if (inst.center().equals(center) && inst.sourceDimension().equals(sourceDimension)) {
        return inst;
      }
    }
    return null;
  }

  public static SkyPortalEventInstance findNearest(BlockPos pos, String dimension) {
    if (pos == null) return null;
    SkyPortalEventInstance nearest = null;
    double minRemainingSq = Double.MAX_VALUE;
    for (SkyPortalEventInstance inst : activePortals.values()) {
      if (dimension == null || dimension.equals(inst.sourceDimension()) || dimension.equals(inst.targetDimension())) {
        double distSq = inst.center().distSqr(pos);
        if (distSq < minRemainingSq) {
          minRemainingSq = distSq;
          nearest = inst;
        }
      }
    }
    return nearest;
  }

  public static Collection<SkyPortalEventInstance> allPortals() {
    return activePortals.values();
  }

  public static SkyPortalEventInstance getActive() {
    return activePortals.values().stream().findFirst().orElse(null);
  }

  public static boolean isAnyActive() {
    return !activePortals.isEmpty();
  }

  public static boolean isTooCloseToExisting(BlockPos center, String sourceDimension) {
    if (center == null) return false;
    for (SkyPortalEventInstance existing : activePortals.values()) {
      if (!sourceDimension.equals(existing.sourceDimension()) && !sourceDimension.equals(existing.targetDimension())) {
        continue;
      }
      double dx = center.getX() - existing.center().getX();
      double dz = center.getZ() - existing.center().getZ();
      if (dx * dx + dz * dz < 90000.0) {
        return true;
      }
    }
    return false;
  }

  public static SkyPortalEventInstance start(
    BlockPos center,
    int crackPlaneY,
    int crackPlaneYTarget,
    boolean inverted,
    long seed,
    String sourceDimension,
    String targetDimension,
    int durationTicks,
    MinecraftServer server
  ) {
    SkyPortalEventInstance inst = new SkyPortalEventInstance(
      center, crackPlaneY, crackPlaneYTarget, inverted, seed, sourceDimension, targetDimension, durationTicks
    );
    activePortals.put(inst.id(), inst);
    if (server != null) {
      ServerLevel sourceLevel = server.getLevel(ResourceKey.create(Registries.DIMENSION, Identifier.tryParse(sourceDimension)));
      if (sourceLevel != null) {
        PortalSavedData.get(sourceLevel).savePortal(inst.id(), center, crackPlaneY, crackPlaneYTarget, inverted, seed, sourceDimension, targetDimension, durationTicks);
      }
    }

    return inst;
  }

  public static void stopPortal(MinecraftServer server, UUID portalId) {
    if (portalId == null) return;
    org.slf4j.LoggerFactory.getLogger("SkyPortalEvent").info("[SKYPORTAL-EVENT] Server stopPortal called for id={}", portalId, new Exception("stopPortal caller stacktrace"));
    SkyPortalEventInstance inst = activePortals.remove(portalId);
    if (inst != null && server != null) {
      ServerLevel sourceLevel = server.getLevel(ResourceKey.create(Registries.DIMENSION, Identifier.tryParse(inst.sourceDimension())));
      if (sourceLevel != null) {
        PortalSavedData.get(sourceLevel).clearPortal(portalId);
      }
    }
  }

  public static void stop(MinecraftServer server, String sourceDimension) {
    if (sourceDimension == null) return;
    for (SkyPortalEventInstance inst : activePortals.values()) {
      if (inst.sourceDimension().equals(sourceDimension)) {
        stopPortal(server, inst.id());
      }
    }
  }

  public static void loadFromDisk(MinecraftServer server) {
    if (server != null) {
      activePortals.clear();
      for (ServerLevel level : server.getAllLevels()) {
        PortalSavedData data = PortalSavedData.get(level);
        for (PortalSavedData.PortalEntry entry : data.portals) {
          SkyPortalEventInstance inst = new SkyPortalEventInstance(
            entry.portalId(),
            entry.center(),
            entry.crackPlaneY(),
            entry.crackPlaneYTarget(),
            entry.inverted(),
            entry.seed(),
            entry.sourceDimension(),
            entry.targetDimension(),
            entry.timerTicks()
          );
          activePortals.put(inst.id(), inst);
        }
      }
    }
  }

  public static void sendPortalToPlayer(ServerPlayer player, MinecraftServer server) {
    org.slf4j.LoggerFactory.getLogger("SkyPortalEvent").info("[SKYPORTAL-EVENT] Server sendPortalToPlayer called for player={}, activePortalsCount={}", player.getScoreboardName(), activePortals.size());
    for (SkyPortalEventInstance inst : activePortals.values()) {
      ServerPlayNetworking.send(
        player,
        new S2CSkyPortalPayload(
          inst.id(), true, inst.crackPlaneY(), inst.crackPlaneYTarget(), inst.inverted(), inst.seed(), inst.center(), inst.sourceDimension(), inst.targetDimension(), true
        )
      );
      ServerLevel targetLevel = server.getLevel(ResourceKey.create(Registries.DIMENSION, Identifier.tryParse(inst.targetDimension())));
      if (targetLevel != null && DimensionUtil.isClientGenerated(inst.targetDimension())) {
        Map<BlockPos, BlockState> deltas = HologramWorldData.get(targetLevel).getDeltasInRadius(inst.center(), 300.0);
        DeltaSyncService.sendBulkDeltasToPlayer(player, deltas, inst.targetDimension());
      }
    }
  }

  public static void toggleGlobal(MinecraftServer server, BlockPos center, boolean inverted, long seed, String sourceDimension, String targetDimension) {
    SkyPortalEventInstance existing = findByCenter(center, sourceDimension);
    boolean activating = existing == null;
    if (activating) {
      if (isTooCloseToExisting(center, sourceDimension)) {
        return;
      }
      int crackPlaneY = center.getY() + 70;
      ServerLevel sourceLevel = server.getLevel(ResourceKey.create(Registries.DIMENSION, Identifier.tryParse(sourceDimension)));
      if (sourceLevel != null) {
        int highestY = center.getY();
        int r = 320;
        int cxCenter = center.getX() >> 4;
        int czCenter = center.getZ() >> 4;

        for (int cx = -20; cx <= 20; cx++) {
          for (int cz = -20; cz <= 20; cz++) {
            if (!sourceLevel.hasChunk(cxCenter + cx, czCenter + cz)) {
              if (cx % 2 == 0 && cz % 2 == 0) {
                int vx = (cxCenter + cx) * 16 + 8;
                int vz = (czCenter + cz) * 16 + 8;
                double dx = vx - center.getX();
                double dz = vz - center.getZ();
                if (dx * dx + dz * dz <= r * r) {
                  int h = sourceLevel.getChunkSource()
                    .getGenerator()
                    .getBaseHeight(vx, vz, Types.WORLD_SURFACE, sourceLevel, sourceLevel.getChunkSource().randomState());
                  if (h > highestY) {
                    highestY = h;
                  }
                }
              }
            } else {
              ChunkAccess chunk = sourceLevel.getChunk(cxCenter + cx, czCenter + cz);

              for (int lx = 0; lx < 16; lx++) {
                for (int lz = 0; lz < 16; lz++) {
                  int vx = (cxCenter + cx) * 16 + lx;
                  int vz = (czCenter + cz) * 16 + lz;
                  double dx = vx - center.getX();
                  double dz = vz - center.getZ();
                  if (dx * dx + dz * dz <= r * r) {
                    int h = chunk.getHeight(Types.WORLD_SURFACE, lx, lz);
                    if (h > highestY) {
                      highestY = h;
                    }
                  }
                }
              }
            }
          }
        }

        crackPlaneY = Math.max(center.getY() + 70, highestY + 10);
      }

      int crackPlaneYTarget = center.getY() + 70;
      ServerLevel targetLevel = server.getLevel(ResourceKey.create(Registries.DIMENSION, Identifier.tryParse(targetDimension)));
      if (targetLevel == null) {
        crackPlaneYTarget = crackPlaneY;
      } else {
        int centerCx = center.getX() >> 4;
        int centerCz = center.getZ() >> 4;
        int targetSurfaceY;
        if (targetLevel.hasChunk(centerCx, centerCz)) {
          targetSurfaceY = targetLevel.getChunk(centerCx, centerCz).getHeight(Types.WORLD_SURFACE, center.getX() & 15, center.getZ() & 15);
        } else {
          targetSurfaceY = targetLevel.getChunkSource()
            .getGenerator()
            .getBaseHeight(center.getX(), center.getZ(), Types.WORLD_SURFACE, targetLevel, targetLevel.getChunkSource().randomState());
        }

        int highestYTarget = targetSurfaceY;
        int r = 320;
        int cxCenter = center.getX() >> 4;
        int czCenter = center.getZ() >> 4;

        for (int cx = -20; cx <= 20; cx++) {
          for (int czx = -20; czx <= 20; czx++) {
            if (!targetLevel.hasChunk(cxCenter + cx, czCenter + czx)) {
              if (cx % 2 == 0 && czx % 2 == 0) {
                int vx = (cxCenter + cx) * 16 + 8;
                int vz = (czCenter + czx) * 16 + 8;
                double dx = vx - center.getX();
                double dz = vz - center.getZ();
                if (dx * dx + dz * dz <= r * r) {
                  int h = targetLevel.getChunkSource()
                    .getGenerator()
                    .getBaseHeight(vx, vz, Types.WORLD_SURFACE, targetLevel, targetLevel.getChunkSource().randomState());
                  if (h > highestYTarget) {
                    highestYTarget = h;
                  }
                }
              }
            } else {
              ChunkAccess chunk = targetLevel.getChunk(cxCenter + cx, czCenter + czx);

              for (int lx = 0; lx < 16; lx++) {
                for (int lzx = 0; lzx < 16; lzx++) {
                  int vx = (cxCenter + cx) * 16 + lx;
                  int vz = (czCenter + czx) * 16 + lzx;
                  double dx = vx - center.getX();
                  double dz = vz - center.getZ();
                  if (dx * dx + dz * dz <= r * r) {
                    int h = chunk.getHeight(Types.WORLD_SURFACE, lx, lzx);
                    if (h > highestYTarget) {
                      highestYTarget = h;
                    }
                  }
                }
              }
            }
          }
        }

        crackPlaneYTarget = Math.max(targetSurfaceY + 70, highestYTarget + 10);
      }

      SkyPortalEventInstance newInst = start(center, crackPlaneY, crackPlaneYTarget, inverted, seed, sourceDimension, targetDimension, 6000, server);
      if (sourceLevel != null) {
        TimestopZoneManager.removeZone(sourceLevel, center);
      }

      S2CSkyPortalPayload payload = new S2CSkyPortalPayload(
        newInst.id(), true, crackPlaneY, crackPlaneYTarget, inverted, seed, center, sourceDimension, targetDimension
      );

      for (ServerPlayer p : server.getPlayerList().getPlayers()) {
        ServerPlayNetworking.send(p, payload);
      }

      ServerLevel targetLevelx = server.getLevel(ResourceKey.create(Registries.DIMENSION, Identifier.tryParse(targetDimension)));
      if (targetLevelx != null) {
        int cX = center.getX() >> 4;
        int cZ = center.getZ() >> 4;
        targetLevelx.getChunkSource().addTicketWithRadius(TicketType.PORTAL, new ChunkPos(cX, cZ), 3);
        CompletableFuture.runAsync(() -> {
          for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
              targetLevelx.getChunk(cX + x, cZ + z, ChunkStatus.FULL, true);
            }
          }
        });
        if (DimensionUtil.isClientGenerated(targetDimension)) {
          Map<BlockPos, BlockState> deltas = HologramWorldData.get(targetLevelx).getDeltasInRadius(center, 300.0);
          DeltaSyncService.broadcastBulkDeltas(server, deltas, targetDimension, null);
        }
      }
    } else {
      UUID idToStop = existing.id();
      String targetDim = existing.targetDimension();
      String sourceDim = existing.sourceDimension();
      stopPortal(server, idToStop);

      S2CSkyPortalPayload payload = new S2CSkyPortalPayload(
        idToStop, false, 256, 256, false, 0L, center, sourceDim, targetDim
      );

      for (ServerPlayer p : server.getPlayerList().getPlayers()) {
        ServerPlayNetworking.send(p, payload);
      }
    }
  }

  public static void handleClientLandingRequest(MinecraftServer server, ServerPlayer p, double px, double py, double pz, float yRot, float xRot) {
    String pDim = p.level().dimension().identifier().toString();
    BlockPos pPos = p.blockPosition();
    SkyPortalEventInstance inst = findNearest(pPos, pDim);
    if (inst == null || !inst.containsOverworldPos(pPos, pDim)) {
      for (SkyPortalEventInstance candidate : activePortals.values()) {
        if (pDim.equals(candidate.sourceDimension()) || pDim.equals(candidate.targetDimension())) {
          inst = candidate;
          break;
        }
      }
    }
    if (inst != null) {
      long now = System.currentTimeMillis();
      UUID uuid = p.getUUID();
      Long lastTime = lastLandingTime.get(uuid);
      if (lastTime == null || now - lastTime >= 1500L) {
        lastLandingTime.put(uuid, now);
        boolean inSource = pDim.equals(inst.sourceDimension());
        boolean inTarget = pDim.equals(inst.targetDimension());
        if (inSource || inTarget) {
          String oppositeDim = inSource ? inst.targetDimension() : inst.sourceDimension();
          ServerLevel oppositeLevel = server.getLevel(ResourceKey.create(Registries.DIMENSION, Identifier.tryParse(oppositeDim)));
          if (oppositeLevel != null) {
            int inversionConstant = inst.crackPlaneY() + inst.crackPlaneYTarget();
            int feetY = Mth.floor(py - 0.1);
            int targetY = inversionConstant - feetY;
            double newY = targetY + 1.0;
            double newZ = inst.inverted() ? 2 * inst.center().getZ() - pz : pz;
            float newYaw = inst.inverted() ? yRot + 180.0F : yRot;
            newY = Math.max(oppositeLevel.getMinY() + 1.0, Math.min(oppositeLevel.getMaxY() - 2.0, newY));
            if (p instanceof GravityChanger gc) {
              gc.clearInfection();
              gc.setGravityInstant(Gravity.DOWN);
            }

            if (p.containerMenu != null && p.containerMenu != p.inventoryMenu) {
              p.closeContainer();
            }

            Vec3 motion = p.getDeltaMovement();
            p.teleportTo(oppositeLevel, px, newY, newZ, Collections.emptySet(), newYaw, xRot, true);
            p.setDeltaMovement(motion);
            p.hurtMarked = true;
            p.level().playSound(null, p.getX(), p.getY(), p.getZ(), SoundEvents.PORTAL_TRAVEL, SoundSource.PLAYERS, 0.5F, 1.2F);
            LoggerFactory.getLogger("SkyPortalManager")
              .info(
                "[SkyPortal] Player {} landed (client-reported), teleported to {} at ({}, {}, {})",
                new Object[]{p.getName().getString(), oppositeDim, px, newY, newZ}
              );
          }
        }
      }
    }
  }
}
