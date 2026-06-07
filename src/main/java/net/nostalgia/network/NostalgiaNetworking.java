package net.nostalgia.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerLevel;
import net.nostalgia.command.TeleportCommand;
import net.nostalgia.alphalogic.ritual.EchoRitualManager;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.network.codec.StreamCodec;

public class NostalgiaNetworking {
    private static final java.util.concurrent.ConcurrentHashMap<java.util.UUID, Long> lastBoatCrashByPlayer = new java.util.concurrent.ConcurrentHashMap<>();
    private static final java.util.concurrent.ConcurrentHashMap<java.util.UUID, Long> lastTravelByPlayer = new java.util.concurrent.ConcurrentHashMap<>();

    public static net.nostalgia.network.S2CTimestopZoneStartPayload pendingZoneStart = null;

    public static void register() {
        net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            java.util.UUID uuid = handler.player.getUUID();
            lastBoatCrashByPlayer.remove(uuid);
            lastTravelByPlayer.remove(uuid);
            net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.removeClientHologramSurface(uuid);
        });
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

        ServerPlayNetworking.registerGlobalReceiver(C2SReportHologramSurfacePayload.TYPE, (payload, context) -> {
            context.server().execute(() -> {
                net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.setClientHologramSurface(context.player().getUUID(), payload.surfaceY());
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(C2SSkyPortalLandingRequestPayload.TYPE, (payload, context) -> {
            context.server().execute(() -> {
                net.nostalgia.alphalogic.ritual.SkyPortalManager.handleClientLandingRequest(
                    context.server(), context.player(), payload.playerX(), payload.playerY(), payload.playerZ(),
                    payload.yRot(), payload.xRot());
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(C2SCacheReadyPayload.TYPE, (payload, context) -> {
            context.server().execute(() -> {
                net.nostalgia.alphalogic.ritual.EchoRitualManager.markClientReady(context.player().getUUID());
                
                net.nostalgia.alphalogic.ritual.SkyPortalEventInstance portalInst = net.nostalgia.alphalogic.ritual.SkyPortalManager.getActive();
                net.nostalgia.alphalogic.ritual.EchoRitualEventInstance transInst = net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.findInstanceForParticipant(context.player().getUUID());
                
                String targetDimStr = null;
                net.minecraft.core.BlockPos center = null;
                
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
                
                if (targetDimStr != null && center != null && !net.nostalgia.alphalogic.ritual.DimensionUtil.isClientGenerated(targetDimStr)) {
                    String normalizedDim = net.nostalgia.alphalogic.ritual.DimensionUtil.normalize(targetDimStr);
                    net.minecraft.server.level.ServerLevel targetLevel = context.server().getLevel(net.minecraft.resources.ResourceKey.create(
                        net.minecraft.core.registries.Registries.DIMENSION, net.minecraft.resources.Identifier.tryParse(normalizedDim)));
                    if (targetLevel != null) {
                        java.util.List<net.minecraft.world.level.ChunkPos> dirtyChunks = new java.util.ArrayList<>();
                        if (!payload.hasOverworldCache()) {
                            dirtyChunks = net.nostalgia.alphalogic.ritual.HologramChunkLoader.getAllChunksInRadius(center, 300);
                        } else {
                            java.util.List<net.minecraft.world.level.ChunkPos> allChunks = net.nostalgia.alphalogic.ritual.HologramChunkLoader.getAllChunksInRadius(center, 300);
                            it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap clientVersions = new it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap();
                            if (payload.chunks() != null && payload.versions() != null) {
                                for (int i = 0; i < payload.chunks().length; i++) {
                                    clientVersions.put(payload.chunks()[i], payload.versions()[i]);
                                }
                            }
                            net.nostalgia.alphalogic.ritual.ServerChunkTracker tracker = net.nostalgia.alphalogic.ritual.ServerChunkTracker.get(targetLevel);
                            for (net.minecraft.world.level.ChunkPos pos : allChunks) {
                                long key = pos.pack();
                                long serverVer = tracker.getVersion(key);
                                long clientVer = clientVersions.getOrDefault(key, -1L);
                                if (serverVer > clientVer) {
                                    dirtyChunks.add(pos);
                                }
                            }
                        }
                        net.nostalgia.alphalogic.ritual.HologramChunkLoader.startLoading(java.util.Collections.singletonList(context.player()), targetLevel, center, 300, dirtyChunks);
                    }
                }
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(C2SRequestPortalDeltasPayload.TYPE, (payload, context) -> {
            context.server().execute(() -> {
                net.nostalgia.alphalogic.ritual.SkyPortalEventInstance portalInst = net.nostalgia.alphalogic.ritual.SkyPortalManager.getActive();
                if (portalInst == null) return;
                net.minecraft.server.level.ServerLevel targetLevel = context.server().getLevel(net.minecraft.resources.ResourceKey.create(
                    net.minecraft.core.registries.Registries.DIMENSION, net.minecraft.resources.Identifier.tryParse(portalInst.targetDimension())));
                if (targetLevel == null) return;
                java.util.Map<net.minecraft.core.BlockPos, net.minecraft.world.level.block.state.BlockState> deltas = net.nostalgia.alphalogic.ritual.HologramWorldData.get(targetLevel).getDeltasInRadius(portalInst.center(), 300.0);
                net.nostalgia.alphalogic.ritual.DeltaSyncService.sendBulkDeltasToPlayer(context.player(), deltas, portalInst.targetDimension());
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(C2SProgramAmethystPayload.TYPE, (payload, context) -> {
            int directionIdx = payload.direction();
            net.minecraft.server.level.ServerPlayer player = context.player();
            context.server().execute(() -> {
                if (player.containerMenu instanceof net.nostalgia.inventory.LodestoneGravityMenu menu) {
                    menu.programAmethyst(directionIdx);
                }
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(C2STravelRequestPayload.TYPE, (payload, context) -> {
            context.server().execute(() -> {
                String v = payload.targetVersion();
                if (!"alpha".equals(v) && !"rd".equals(v) && !"overworld".equals(v)) return;

                String sourceDim = context.player().level().dimension().identifier().toString();
                if (sourceDim.equals(net.nostalgia.alphalogic.ritual.DimensionUtil.normalize(v))) {
                    return;
                }

                java.util.UUID travelUuid = context.player().getUUID();
                long nowTravel = System.currentTimeMillis();
                Long prevTravel = lastTravelByPlayer.get(travelUuid);
                if (prevTravel != null && nowTravel - prevTravel < 5000L) return;
                lastTravelByPlayer.put(travelUuid, nowTravel);
                boolean isEchoShard = false;
                boolean isAmethystShard = false;

                if (context.player().containerMenu instanceof net.nostalgia.inventory.TimeMachineMenu menu) {
                    net.minecraft.world.item.ItemStack slotItem = menu.container.getItem(0);
                    isEchoShard = !slotItem.isEmpty() && slotItem.is(net.minecraft.world.item.Items.ECHO_SHARD);
                    isAmethystShard = !slotItem.isEmpty() && slotItem.is(net.minecraft.world.item.Items.AMETHYST_SHARD);
                    
                    if (isAmethystShard && sourceDim.contains("the_nether")) {
                        context.player().sendSystemMessage(net.minecraft.network.chat.Component.literal("The temporal fabric here is too unstable..."), true);
                        return;
                    }
                    
                    if (!isEchoShard && !isAmethystShard) {
                        context.player().sendSystemMessage(net.minecraft.network.chat.Component.literal("§cОшибка запуска: Критический уровень энергии. Требуется Осколок Эха или Аметиста."));
                        return;
                    }
                    slotItem.shrink(1);
                    menu.container.setChanged();
                }

                java.util.UUID playerUuid = context.player().getUUID();
                net.minecraft.core.BlockPos beaconPos = EchoRitualManager.getSelectedBeacon(playerUuid);
                net.nostalgia.alphalogic.ritual.TimestopZoneManager.ActiveZone currentZone = net.nostalgia.alphalogic.ritual.TimestopZoneManager.findZoneContaining(context.player().level().dimension(), context.player().blockPosition());
                if (currentZone != null) {
                    beaconPos = currentZone.beaconPos();
                    EchoRitualManager.selectBeacon(playerUuid, beaconPos);
                } else if (beaconPos == null) {
                    beaconPos = context.player().blockPosition();
                    EchoRitualManager.selectBeacon(playerUuid, beaconPos);
                }
                final net.minecraft.core.BlockPos finalBeacon = beaconPos;

                if (isAmethystShard) {
                    net.nostalgia.alphalogic.ritual.SkyPortalEventInstance existingPortal = net.nostalgia.alphalogic.ritual.SkyPortalManager.getActive();
                    if (existingPortal != null) {
                        double distSq = context.player().blockPosition().distSqr(existingPortal.center());
                        if (distSq > 160.0 * 160.0) return;
                    }
                    String portalDim = net.nostalgia.alphalogic.ritual.DimensionUtil.normalize(payload.targetVersion());
                    sourceDim = context.player().level().dimension().identifier().toString();
                    net.nostalgia.alphalogic.ritual.SkyPortalManager.toggleGlobal(context.server(), beaconPos, true, context.player().level().getSeed(), sourceDim, portalDim);
                    
                    net.nostalgia.entity.SkyPortalBeamEntity beam = new net.nostalgia.entity.SkyPortalBeamEntity(net.nostalgia.entity.AlphaEntities.SKY_PORTAL_BEAM, context.player().level());
                    beam.setPos(beaconPos.getX() + 0.5, beaconPos.getY() + 1.0, beaconPos.getZ() + 0.5);
                    context.player().level().addFreshEntity(beam);
                    
                    return;
                }

                String targetDimId;
                if ("overworld".equals(payload.targetVersion()) 
                        && net.nostalgia.alphalogic.ritual.DimensionUtil.isRD(sourceDim)) {
                    targetDimId = net.nostalgia.alphalogic.ritual.EchoRitualManager.playerReturnDimensions.getOrDefault(
                        context.player().getUUID(), net.nostalgia.alphalogic.ritual.DimensionUtil.OW_FULL);
                } else {
                    targetDimId = net.nostalgia.alphalogic.ritual.DimensionUtil.normalize(payload.targetVersion());
                }
                net.minecraft.server.level.ServerLevel targetLevel = net.nostalgia.alphalogic.ritual.DimensionUtil.resolveLevel(context.server(), targetDimId);
                if (targetLevel == null) {
                    targetLevel = context.server().getLevel(net.minecraft.world.level.Level.OVERWORLD);
                    targetDimId = net.nostalgia.alphalogic.ritual.DimensionUtil.OW_FULL;
                }
                if (targetLevel != null) {
                    EchoRitualManager.startTeleportTransition((net.minecraft.server.level.ServerPlayer) context.player(), targetLevel, targetDimId, finalBeacon);
                }
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(C2SBoatCrashPayload.TYPE, (payload, context) -> {
            context.server().execute(() -> {
                java.util.UUID boatUuid = context.player().getUUID();
                long nowBoat = System.currentTimeMillis();
                Long prevBoat = lastBoatCrashByPlayer.get(boatUuid);
                if (prevBoat != null && nowBoat - prevBoat < 3000L) return;
                lastBoatCrashByPlayer.put(boatUuid, nowBoat);
                net.minecraft.server.level.ServerLevel sLevel = (net.minecraft.server.level.ServerLevel) context.player().level();
                net.minecraft.world.entity.Entity entity = sLevel.getEntity(payload.boatId());
                if (entity == null) return;
                if (!(entity instanceof net.minecraft.world.entity.vehicle.boat.AbstractBoat)) return;
                if (entity.hasPassenger(context.player())) {
                    double pdx = context.player().getX() - payload.x();
                    double pdy = context.player().getY() - payload.y();
                    double pdz = context.player().getZ() - payload.z();
                    if ((pdx * pdx + pdy * pdy + pdz * pdz) >= 256.0) return;
                    for (int i = 0; i < 3; ++i) {
                        net.minecraft.world.entity.item.ItemEntity plank = new net.minecraft.world.entity.item.ItemEntity(sLevel, payload.x(), payload.y(), payload.z(), new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.OAK_PLANKS));
                        plank.setDefaultPickUpDelay();
                        sLevel.addFreshEntity(plank);
                    }
                    for (int j = 0; j < 2; ++j) {
                        net.minecraft.world.entity.item.ItemEntity stick = new net.minecraft.world.entity.item.ItemEntity(sLevel, payload.x(), payload.y(), payload.z(), new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.STICK));
                        stick.setDefaultPickUpDelay();
                        sLevel.addFreshEntity(stick);
                    }
                    entity.discard();
                }
            });
        });
    }
}
