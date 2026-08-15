package net.nostalgia.network;

import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents.Disconnect;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.nostalgia.alphalogic.ritual.DeltaSyncService;
import net.nostalgia.alphalogic.ritual.DimensionUtil;
import net.nostalgia.alphalogic.ritual.EchoRitualEventInstance;
import net.nostalgia.alphalogic.ritual.EchoRitualManager;
import net.nostalgia.alphalogic.ritual.HologramChunkLoader;
import net.nostalgia.alphalogic.ritual.HologramWorldData;
import net.nostalgia.alphalogic.ritual.ServerChunkTracker;
import net.nostalgia.alphalogic.ritual.SkyPortalEventInstance;
import net.nostalgia.alphalogic.ritual.SkyPortalManager;
import net.nostalgia.alphalogic.ritual.TimestopZoneManager;
import net.nostalgia.alphalogic.ritual.event.RitualEventRegistry;
import net.nostalgia.entity.AlphaEntities;
import net.nostalgia.entity.SkyPortalBeamEntity;
import net.nostalgia.inventory.LodestoneGravityMenu;
import net.nostalgia.inventory.TimeMachineMenu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NostalgiaNetworking {
  private static final Logger LOGGER = LoggerFactory.getLogger("NostalgiaCache");
  private static final ConcurrentHashMap<UUID, Long> lastBoatCrashByPlayer = new ConcurrentHashMap<>();
  private static final ConcurrentHashMap<UUID, Long> lastTravelByPlayer = new ConcurrentHashMap<>();
  public static S2CTimestopZoneStartPayload pendingZoneStart = null;

  public NostalgiaNetworking() {
  }

  public static void register() {
    ServerPlayConnectionEvents.DISCONNECT.register((Disconnect)(handler, server) -> {
      UUID uuid = handler.player.getUUID();
      lastBoatCrashByPlayer.remove(uuid);
      lastTravelByPlayer.remove(uuid);
      RitualEventRegistry.removeClientHologramSurface(uuid);
    });
    PayloadTypeRegistry.clientboundPlay().register(S2CWorldSeedPayload.TYPE, S2CWorldSeedPayload.CODEC);
    PayloadTypeRegistry.clientboundPlay().register(S2CSyncAlphaDeltasPayload.TYPE, S2CSyncAlphaDeltasPayload.CODEC);
    PayloadTypeRegistry.clientboundPlay().register(S2CStartTransitionVisualsPayload.TYPE, S2CStartTransitionVisualsPayload.CODEC);
    PayloadTypeRegistry.clientboundPlay().register(S2CEndTransitionVisualsPayload.TYPE, S2CEndTransitionVisualsPayload.CODEC);
    PayloadTypeRegistry.clientboundPlay().register(S2CSyncParticipantsPayload.TYPE, S2CSyncParticipantsPayload.CODEC);
    PayloadTypeRegistry.clientboundPlay().register(S2CRitualPhasePayload.TYPE, S2CRitualPhasePayload.CODEC);
    PayloadTypeRegistry.clientboundPlay().register(S2CBystanderVisualsPayload.TYPE, S2CBystanderVisualsPayload.CODEC);
    PayloadTypeRegistry.clientboundPlay().register(S2CSkyPortalPayload.TYPE, S2CSkyPortalPayload.CODEC);
    PayloadTypeRegistry.clientboundPlay().register(S2CGlassBreakPayload.TYPE, S2CGlassBreakPayload.CODEC);
    PayloadTypeRegistry.clientboundPlay().register(S2CTimestopZoneStartPayload.TYPE, S2CTimestopZoneStartPayload.CODEC);
    PayloadTypeRegistry.clientboundPlay().register(S2CTimestopZoneEndPayload.TYPE, S2CTimestopZoneEndPayload.CODEC);
    PayloadTypeRegistry.clientboundPlay().register(S2CZoneCollapsePayload.ID, S2CZoneCollapsePayload.CODEC);
    PayloadTypeRegistry.clientboundPlay().register(S2CSetTerminalErrorPayload.TYPE, S2CSetTerminalErrorPayload.CODEC);
    PayloadTypeRegistry.clientboundPlay().register(S2CDimensionSectionsPayload.TYPE, S2CDimensionSectionsPayload.CODEC);
    PayloadTypeRegistry.clientboundPlay().register(S2CHologramReadyPayload.TYPE, S2CHologramReadyPayload.CODEC);
    PayloadTypeRegistry.clientboundPlay().register(S2CDebugOwerPayload.TYPE, S2CDebugOwerPayload.CODEC);
    PayloadTypeRegistry.clientboundPlay().register(S2CSkyPortalCancelPayload.TYPE, S2CSkyPortalCancelPayload.STREAM_CODEC);
    PayloadTypeRegistry.clientboundPlay().register(S2CSkyPortalLandingPayload.TYPE, S2CSkyPortalLandingPayload.CODEC);
    PayloadTypeRegistry.serverboundPlay().register(C2STravelRequestPayload.TYPE, C2STravelRequestPayload.CODEC);
    PayloadTypeRegistry.serverboundPlay().register(C2SCacheReadyPayload.TYPE, C2SCacheReadyPayload.CODEC);
    PayloadTypeRegistry.serverboundPlay().register(C2SBoatCrashPayload.TYPE, C2SBoatCrashPayload.CODEC);
    PayloadTypeRegistry.serverboundPlay().register(C2SReportHologramSurfacePayload.TYPE, C2SReportHologramSurfacePayload.CODEC);
    PayloadTypeRegistry.serverboundPlay().register(C2SRequestPortalDeltasPayload.TYPE, C2SRequestPortalDeltasPayload.CODEC);
    PayloadTypeRegistry.serverboundPlay().register(C2SProgramAmethystPayload.TYPE, C2SProgramAmethystPayload.CODEC);
    PayloadTypeRegistry.serverboundPlay().register(C2SSkyPortalLandingRequestPayload.TYPE, C2SSkyPortalLandingRequestPayload.CODEC);
    PayloadTypeRegistry.serverboundPlay().register(C2STerminalCacheRequestPayload.TYPE, C2STerminalCacheRequestPayload.CODEC);
    ServerPlayNetworking.registerGlobalReceiver(
      C2SReportHologramSurfacePayload.TYPE,
      (payload, context) -> context.server().execute(() -> RitualEventRegistry.setClientHologramSurface(context.player().getUUID(), payload.surfaceY()))
    );
    ServerPlayNetworking.registerGlobalReceiver(
      C2SSkyPortalLandingRequestPayload.TYPE,
      (payload, context) -> context.server()
        .execute(
          () -> SkyPortalManager.handleClientLandingRequest(
            context.server(), context.player(), payload.playerX(), payload.playerY(), payload.playerZ(), payload.yRot(), payload.xRot()
          )
        )
    );
    ServerPlayNetworking.registerGlobalReceiver(C2SCacheReadyPayload.TYPE, (payload, context) -> context.server().execute(() -> {
      EchoRitualManager.markClientReady(context.player().getUUID());
      SkyPortalEventInstance portalInst = SkyPortalManager.findNearest(context.player().blockPosition(), context.player().level().dimension().identifier().toString());
      EchoRitualEventInstance transInst = RitualEventRegistry.findInstanceForParticipant(context.player().getUUID());
      String targetDimStr = null;
      BlockPos center = null;
      if (transInst != null) {
        targetDimStr = transInst.targetDimensionId();
        center = transInst.targetPos() != null ? transInst.targetPos() : transInst.beaconPos();
      } else if (portalInst != null) {
        String pDim = context.player().level().dimension().identifier().toString();
        if (pDim.equals(portalInst.targetDimension())) {
          targetDimStr = portalInst.sourceDimension();
        } else {
          targetDimStr = portalInst.targetDimension();
        }

        center = portalInst.center();
      }

      if (targetDimStr != null && center != null && !DimensionUtil.isClientGenerated(targetDimStr)) {
        String normalizedDim = DimensionUtil.normalize(targetDimStr);
        ServerLevel targetLevel = context.server().getLevel(ResourceKey.create(Registries.DIMENSION, Identifier.tryParse(normalizedDim)));
        if (targetLevel != null) {
          List<ChunkPos> dirtyChunks = new ArrayList<>();
          if (!payload.hasOverworldCache()) {
            dirtyChunks = HologramChunkLoader.getAllChunksInRadius(center, 300);
          } else {
            List<ChunkPos> allChunks = HologramChunkLoader.getAllChunksInRadius(center, 300);
            Long2LongOpenHashMap clientVersions = new Long2LongOpenHashMap();
            if (payload.chunks() != null && payload.versions() != null) {
              for (int i = 0; i < payload.chunks().length; i++) {
                clientVersions.put(payload.chunks()[i], payload.versions()[i]);
              }
            }

            ServerChunkTracker tracker = ServerChunkTracker.get(targetLevel);

            for (ChunkPos pos : allChunks) {
              long key = pos.pack();
              long serverVer = tracker.getVersion(key);
              long clientVer = clientVersions.getOrDefault(key, -1L);
              if (serverVer > clientVer) {
                dirtyChunks.add(pos);
              }
            }
          }

          HologramChunkLoader.startLoading(Collections.singletonList(context.player()), targetLevel, center, 300, dirtyChunks);
        }
      }
    }));
    ServerPlayNetworking.registerGlobalReceiver(
      C2STerminalCacheRequestPayload.TYPE,
      (payload, context) -> context.server()
        .execute(
          () -> {
            String targetDimStr = payload.targetDimension();
            if (targetDimStr != null && !DimensionUtil.isClientGenerated(targetDimStr)) {
              String normalizedDim = DimensionUtil.normalize(targetDimStr);
              ServerLevel targetLevel = context.server().getLevel(ResourceKey.create(Registries.DIMENSION, Identifier.tryParse(normalizedDim)));
              if (targetLevel == null) {
                LOGGER.warn("[CACHE-SRV] targetLevel null for dim='{}' (normalized='{}')", targetDimStr, normalizedDim);
              } else {
                BlockPos center = context.player().blockPosition();
                List<ChunkPos> dirtyChunks;
                if (!payload.hasCache()) {
                  dirtyChunks = HologramChunkLoader.getAllChunksInRadius(center, 300);
                  LOGGER.info(
                    "[CACHE-SRV] {} requested dim='{}' hasCache=false -> all {} chunks dirty",
                    new Object[]{context.player().getName().getString(), targetDimStr, dirtyChunks.size()}
                  );
                } else {
                  List<ChunkPos> allChunks = HologramChunkLoader.getAllChunksInRadius(center, 300);
                  Long2LongOpenHashMap clientVersions = new Long2LongOpenHashMap();
                  if (payload.chunks() != null && payload.versions() != null) {
                    for (int i = 0; i < payload.chunks().length; i++) {
                      clientVersions.put(payload.chunks()[i], payload.versions()[i]);
                    }
                  }

                  ServerChunkTracker tracker = ServerChunkTracker.get(targetLevel);
                  dirtyChunks = new ArrayList<>();

                  for (ChunkPos pos : allChunks) {
                    long key = pos.pack();
                    long serverVer = tracker.getVersion(key);
                    long clientVer = clientVersions.getOrDefault(key, -1L);
                    if (serverVer > clientVer) {
                      dirtyChunks.add(pos);
                    }
                  }

                  LOGGER.info(
                    "[CACHE-SRV] {} requested dim='{}' hasCache=true clientChunks={} -> {} dirty of {} total",
                    new Object[]{context.player().getName().getString(), targetDimStr, payload.chunks().length, dirtyChunks.size(), allChunks.size()}
                  );
                }

                LOGGER.info(
                  "[CACHE-SRV] starting HologramChunkLoader for dim='{}' dirtyChunks={} center=({},{})",
                  new Object[]{normalizedDim, dirtyChunks.size(), center.getX(), center.getZ()}
                );
                HologramChunkLoader.startLoading(Collections.singletonList(context.player()), targetLevel, center, 300, dirtyChunks);
              }
            }
          }
        )
    );
    ServerPlayNetworking.registerGlobalReceiver(C2SRequestPortalDeltasPayload.TYPE, (payload, context) -> context.server().execute(() -> {
      SkyPortalEventInstance portalInst = SkyPortalManager.findNearest(context.player().blockPosition(), context.player().level().dimension().identifier().toString());
      if (portalInst != null) {
        ServerLevel targetLevel = context.server().getLevel(ResourceKey.create(Registries.DIMENSION, Identifier.tryParse(portalInst.targetDimension())));
        if (targetLevel != null) {
          Map<BlockPos, BlockState> deltas = HologramWorldData.get(targetLevel).getDeltasInRadius(portalInst.center(), 300.0);
          DeltaSyncService.sendBulkDeltasToPlayer(context.player(), deltas, portalInst.targetDimension());
        }
      }
    }));
    ServerPlayNetworking.registerGlobalReceiver(C2SProgramAmethystPayload.TYPE, (payload, context) -> {
      int directionIdx = payload.direction();
      ServerPlayer player = context.player();
      context.server().execute(() -> {
        if (player.containerMenu instanceof LodestoneGravityMenu menu) {
          menu.programAmethyst(directionIdx);
        }
      });
    });
    ServerPlayNetworking.registerGlobalReceiver(
      C2STravelRequestPayload.TYPE,
      (payload, context) -> context.server()
        .execute(
          () -> {
            String v = payload.targetVersion();
            if ("alpha".equals(v) || "rd".equals(v) || "overworld".equals(v)) {
              String sourceDim = context.player().level().dimension().identifier().toString();
              if (!sourceDim.equals(DimensionUtil.normalize(v))) {
                UUID travelUuid = context.player().getUUID();
                long nowTravel = System.currentTimeMillis();
                Long prevTravel = lastTravelByPlayer.get(travelUuid);
                if (prevTravel == null || nowTravel - prevTravel >= 5000L) {
                  lastTravelByPlayer.put(travelUuid, nowTravel);
                  boolean isEchoShard = false;
                  boolean isAmethystShard = false;
                  if (context.player().containerMenu instanceof TimeMachineMenu menu) {
                    ItemStack slotItem = menu.container.getItem(0);
                    isEchoShard = !slotItem.isEmpty() && slotItem.is(Items.ECHO_SHARD);
                    isAmethystShard = !slotItem.isEmpty() && slotItem.is(Items.AMETHYST_SHARD);
                    if (isAmethystShard && sourceDim.contains("the_nether")) {
                      context.player().sendSystemMessage(Component.literal("The temporal fabric here is too unstable..."), true);
                      return;
                    }

                    if (!isEchoShard && !isAmethystShard) {
                      context.player()
                        .sendSystemMessage(
                          Component.literal(
                            "\u00a7c\u041e\u0448\u0438\u0431\u043a\u0430 \u0437\u0430\u043f\u0443\u0441\u043a\u0430: \u041a\u0440\u0438\u0442\u0438\u0447\u0435\u0441\u043a\u0438\u0439 \u0443\u0440\u043e\u0432\u0435\u043d\u044c \u044d\u043d\u0435\u0440\u0433\u0438\u0438. \u0422\u0440\u0435\u0431\u0443\u0435\u0442\u0441\u044f \u041e\u0441\u043a\u043e\u043b\u043e\u043a \u042d\u0445\u0430 \u0438\u043b\u0438 \u0410\u043c\u0435\u0442\u0438\u0441\u0442\u0430."
                          )
                        );
                      return;
                    }

                    slotItem.shrink(1);
                    menu.container.setChanged();
                  }

                  UUID playerUuid = context.player().getUUID();
                  BlockPos beaconPos = EchoRitualManager.getSelectedBeacon(playerUuid);
                  if (beaconPos == null) {
                    TimestopZoneManager.ActiveZone currentZone = TimestopZoneManager.findZoneContaining(
                      context.player().level().dimension(), context.player().blockPosition()
                    );
                    if (currentZone != null) {
                      beaconPos = currentZone.beaconPos();
                    } else {
                      beaconPos = context.player().blockPosition();
                    }
                    EchoRitualManager.selectBeacon(playerUuid, beaconPos);
                  }

                  if (isAmethystShard) {
                    for (SkyPortalEventInstance existing : SkyPortalManager.allPortals()) {
                      if (!sourceDim.equals(existing.sourceDimension()) && !sourceDim.equals(existing.targetDimension())) {
                        continue;
                      }
                      double dist = Math.sqrt(context.player().blockPosition().distSqr(existing.center()));
                      if (dist < 300.0) {
                        if (existing.center().equals(beaconPos)) {
                          SkyPortalManager.toggleGlobal(context.server(), beaconPos, true, context.player().level().getSeed(), sourceDim, DimensionUtil.normalize(payload.targetVersion()));
                          return;
                        }
                        context.player().sendSystemMessage(Component.literal("\u00a7c\u0422\u0440\u0435\u0449\u0438\u043d\u0430 \u0441\u043b\u0438\u0448\u043a\u043e\u043c \u0431\u043b\u0438\u0437\u043a\u043e \u043a \u0441\u0443\u0449\u0435\u0441\u0442\u0432\u0443\u044e\u0449\u0435\u0439 (\u043c\u0438\u043d. 300 \u0431\u043b\u043e\u043a\u043e\u0432)"));
                        return;
                      }
                    }

                    String portalDim = DimensionUtil.normalize(payload.targetVersion());
                    sourceDim = context.player().level().dimension().identifier().toString();
                    SkyPortalManager.toggleGlobal(context.server(), beaconPos, true, context.player().level().getSeed(), sourceDim, portalDim);
                  } else {
                    String targetDimId;
                    if ("overworld".equals(payload.targetVersion()) && DimensionUtil.isRD(sourceDim)) {
                      targetDimId = EchoRitualManager.playerReturnDimensions.getOrDefault(context.player().getUUID(), "minecraft:overworld");
                    } else {
                      targetDimId = DimensionUtil.normalize(payload.targetVersion());
                    }

                    ServerLevel targetLevel = DimensionUtil.resolveLevel(context.server(), targetDimId);
                    if (targetLevel == null) {
                      targetLevel = context.server().getLevel(Level.OVERWORLD);
                      targetDimId = "minecraft:overworld";
                    }

                    if (targetLevel != null) {
                      EchoRitualManager.startTeleportTransition(context.player(), targetLevel, targetDimId, beaconPos, payload.landingOverride());
                    }
                  }
                }
              }
            }
          }
        )
    );
    ServerPlayNetworking.registerGlobalReceiver(C2SBoatCrashPayload.TYPE, (payload, context) -> context.server().execute(() -> {
      UUID boatUuid = context.player().getUUID();
      long nowBoat = System.currentTimeMillis();
      Long prevBoat = lastBoatCrashByPlayer.get(boatUuid);
      if (prevBoat == null || nowBoat - prevBoat >= 3000L) {
        lastBoatCrashByPlayer.put(boatUuid, nowBoat);
        ServerLevel sLevel = context.player().level();
        Entity entity = sLevel.getEntity(payload.boatId());
        if (entity != null) {
          if (entity instanceof AbstractBoat) {
            if (entity.hasPassenger(context.player())) {
              double pdx = context.player().getX() - payload.x();
              double pdy = context.player().getY() - payload.y();
              double pdz = context.player().getZ() - payload.z();
              if (pdx * pdx + pdy * pdy + pdz * pdz >= 256.0) {
                return;
              }

              for (int i = 0; i < 3; i++) {
                ItemEntity plank = new ItemEntity(sLevel, payload.x(), payload.y(), payload.z(), new ItemStack(Items.OAK_PLANKS));
                plank.setDefaultPickUpDelay();
                sLevel.addFreshEntity(plank);
              }

              for (int j = 0; j < 2; j++) {
                ItemEntity stick = new ItemEntity(sLevel, payload.x(), payload.y(), payload.z(), new ItemStack(Items.STICK));
                stick.setDefaultPickUpDelay();
                sLevel.addFreshEntity(stick);
              }

              entity.discard();
            }
          }
        }
      }
    }));
  }
}
