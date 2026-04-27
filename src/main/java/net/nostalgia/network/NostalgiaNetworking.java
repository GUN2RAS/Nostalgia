package net.nostalgia.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerLevel;
import net.nostalgia.command.TeleportCommand;
import net.nostalgia.alphalogic.ritual.RitualManager;
import net.minecraft.resources.ResourceKey;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.network.codec.StreamCodec;

public class NostalgiaNetworking {
    private static final java.util.concurrent.ConcurrentHashMap<java.util.UUID, Long> lastBoatCrashByPlayer = new java.util.concurrent.ConcurrentHashMap<>();
    private static final java.util.concurrent.ConcurrentHashMap<java.util.UUID, Long> lastTravelByPlayer = new java.util.concurrent.ConcurrentHashMap<>();

    public static net.nostalgia.network.S2CTimestopZoneStartPayload pendingZoneStart = null;

    public static void nostalgia$markZoneChunksDirty(net.minecraft.client.Minecraft mc, net.minecraft.core.BlockPos beaconPos, int chunkRadius) {
        if (mc == null || mc.levelRenderer == null || beaconPos == null) return;
        int blockRadius = (chunkRadius + 1) * 16;
        int minX = beaconPos.getX() - blockRadius;
        int maxX = beaconPos.getX() + blockRadius;
        int minZ = beaconPos.getZ() - blockRadius;
        int maxZ = beaconPos.getZ() + blockRadius;
        int minY = mc.level != null ? mc.level.getMinY() : -64;
        int maxY = mc.level != null ? mc.level.getMaxY() : 320;
        mc.levelRenderer.setBlocksDirty(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public static void register() {
        PayloadTypeRegistry.clientboundPlay().register(S2COpenNodeMapPayload.TYPE, S2COpenNodeMapPayload.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(S2CSyncAlphaDeltasPayload.TYPE, S2CSyncAlphaDeltasPayload.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(S2CStartTransitionVisualsPayload.TYPE, S2CStartTransitionVisualsPayload.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(S2CEndTransitionVisualsPayload.TYPE, S2CEndTransitionVisualsPayload.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(S2CSyncParticipantsPayload.TYPE, S2CSyncParticipantsPayload.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(S2CRitualPhasePayload.TYPE, S2CRitualPhasePayload.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(S2CBystanderVisualsPayload.TYPE, S2CBystanderVisualsPayload.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(S2CPortalDebugPayload.TYPE, S2CPortalDebugPayload.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(S2CGlassBreakPayload.TYPE, S2CGlassBreakPayload.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(S2CTimestopZoneStartPayload.TYPE, S2CTimestopZoneStartPayload.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(S2CTimestopZoneEndPayload.TYPE, S2CTimestopZoneEndPayload.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(S2CZoneCollapsePayload.ID, S2CZoneCollapsePayload.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(S2CSetTerminalErrorPayload.TYPE, S2CSetTerminalErrorPayload.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(S2COverworldSectionsPayload.TYPE, S2COverworldSectionsPayload.CODEC);
        PayloadTypeRegistry.serverboundPlay().register(C2STravelRequestPayload.TYPE, C2STravelRequestPayload.CODEC);
        PayloadTypeRegistry.serverboundPlay().register(C2SCacheReadyPayload.TYPE, C2SCacheReadyPayload.CODEC);
        PayloadTypeRegistry.serverboundPlay().register(C2SBoatCrashPayload.TYPE, C2SBoatCrashPayload.CODEC);
        PayloadTypeRegistry.serverboundPlay().register(C2SReportHologramSurfacePayload.TYPE, C2SReportHologramSurfacePayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(C2SReportHologramSurfacePayload.TYPE, (payload, context) -> {
            context.server().execute(() -> {
                net.nostalgia.alphalogic.ritual.RitualManager.clientHologramSurfaces.put(context.player().getUUID(), payload.surfaceY());
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(C2SCacheReadyPayload.TYPE, (payload, context) -> {
            context.server().execute(() -> {
                net.nostalgia.alphalogic.ritual.RitualManager.markClientReady(context.player().getUUID());
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(C2STravelRequestPayload.TYPE, (payload, context) -> {
            context.server().execute(() -> {
                String v = payload.targetVersion();
                if (!"alpha".equals(v) && !"rd".equals(v) && !"overworld".equals(v)) return;
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
                    
                    if (!isEchoShard && !isAmethystShard) {
                        context.player().sendSystemMessage(net.minecraft.network.chat.Component.literal("§cОшибка запуска: Критический уровень энергии. Требуется Осколок Эха или Аметиста."));
                        return;
                    }
                    menu.container.getItem(0).shrink(1); 
                }

                net.minecraft.core.BlockPos beaconPos = RitualManager.getTargetBeaconPos();
                RitualManager.ActiveZone currentZone = RitualManager.findZoneContaining(context.player().level().dimension(), context.player().blockPosition());
                if (currentZone != null) {
                    beaconPos = currentZone.beaconPos();
                    RitualManager.setTargetBeaconPos(beaconPos);
                } else if (beaconPos == null) {
                    beaconPos = context.player().blockPosition();
                    RitualManager.setTargetBeaconPos(beaconPos);
                }

                if (isAmethystShard) {
                    net.nostalgia.command.ModCommands.toggleGlobalPortal(context.server(), beaconPos, true, context.player().level().getSeed());
                    
                    net.nostalgia.entity.SkyPortalBeamEntity beam = new net.nostalgia.entity.SkyPortalBeamEntity(net.nostalgia.entity.AlphaEntities.SKY_PORTAL_BEAM, context.player().level());
                    beam.setPos(beaconPos.getX() + 0.5, beaconPos.getY() + 1.0, beaconPos.getZ() + 0.5);
                    context.player().level().addFreshEntity(beam);
                    
                    return;
                }

                if ("alpha".equals(payload.targetVersion())) {
                    ServerLevel alphaLevel = context.server().getLevel(ResourceKey.create(Registries.DIMENSION, Identifier.fromNamespaceAndPath("nostalgia", "alpha_112_01")));
                    if (alphaLevel != null) {
                        RitualManager.startTeleportTransition((net.minecraft.server.level.ServerPlayer) context.player(), alphaLevel, "alpha");
                    }
                } else if ("rd".equals(payload.targetVersion())) {
                    ServerLevel rdLevel = context.server().getLevel(ResourceKey.create(Registries.DIMENSION, Identifier.fromNamespaceAndPath("nostalgia", "rd_132211")));
                    if (rdLevel != null) {
                        RitualManager.startTeleportTransition((net.minecraft.server.level.ServerPlayer) context.player(), rdLevel, "rd");
                    }
                } else if ("overworld".equals(payload.targetVersion())) {
                    ServerLevel overworldLevel = context.server().getLevel(net.minecraft.world.level.Level.OVERWORLD);
                    if (overworldLevel != null) {
                        RitualManager.startTeleportTransition((net.minecraft.server.level.ServerPlayer) context.player(), overworldLevel, "overworld");
                    }
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

    public static void registerClientReceivers() {
        net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.registerGlobalReceiver(S2CSetTerminalErrorPayload.TYPE, (payload, context) -> {
            context.client().execute(() -> {
                net.nostalgia.client.gui.TimeMachineScreen.nextScreenIsError = true;
            });
        });
        net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.registerGlobalReceiver(S2CSyncAlphaDeltasPayload.TYPE, (payload, context) -> {
            if (payload.positions() == null || payload.states() == null) return;
            if (payload.positions().length != payload.states().length) return;
            context.client().execute(() -> {
                net.nostalgia.client.ritual.ClientVirtualBlockCache.syncDeltas(payload.positions(), payload.states());
                for (long posAsLong : payload.positions()) {
                    net.minecraft.core.BlockPos pos = net.minecraft.core.BlockPos.of(posAsLong);
                    net.sha.api.SHAHologramManager.markAreaDirty(pos.getX(), pos.getY(), pos.getZ(), pos.getX(), pos.getY(), pos.getZ());
                }
            });
        });
        net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.registerGlobalReceiver(S2COverworldSectionsPayload.TYPE, (payload, context) -> {
            context.client().execute(() -> {
                int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE, minZ = Integer.MAX_VALUE;
                int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE, maxZ = Integer.MIN_VALUE;
                
                for (S2COverworldSectionsPayload.SectionData sd : payload.sections()) {
                    net.minecraft.world.level.block.state.BlockState[] palette = new net.minecraft.world.level.block.state.BlockState[sd.paletteIds().length];
                    for (int i = 0; i < sd.paletteIds().length; i++) {
                        palette[i] = net.minecraft.world.level.block.Block.stateById(sd.paletteIds()[i]);
                    }
                    net.nostalgia.client.render.cache.HologramSection section = new net.nostalgia.client.render.cache.HologramSection(palette, sd.indices());
                    net.nostalgia.client.render.cache.OverworldHologramCache.putSection(sd.chunkX(), sd.sectionY(), sd.chunkZ(), section);
                    
                    int bx = sd.chunkX() << 4;
                    int by = sd.sectionY() << 4;
                    int bz = sd.chunkZ() << 4;
                    minX = Math.min(minX, bx); minY = Math.min(minY, by); minZ = Math.min(minZ, bz);
                    maxX = Math.max(maxX, bx + 15); maxY = Math.max(maxY, by + 15); maxZ = Math.max(maxZ, bz + 15);
                }
                if (payload.sections().size() > 0) {
                    net.sha.api.SHAHologramManager.markAreaDirty(minX, minY, minZ, maxX, maxY, maxZ);
                }
            });
        });
        net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.registerGlobalReceiver(S2CStartTransitionVisualsPayload.TYPE, (payload, context) -> {
            context.client().execute(() -> {
                net.nostalgia.alphalogic.bridge.AlphaEngineManager.setWorldSeed(payload.seed());
                net.nostalgia.client.ritual.RitualVisualManager.startTransition(payload.beaconPos(), payload.dimensionId(), payload.safeSpawnPos());
            });
        });
        net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.registerGlobalReceiver(S2CEndTransitionVisualsPayload.TYPE, (payload, context) -> {
            context.client().execute(() -> {
                if (net.nostalgia.client.ritual.RitualVisualManager.isBystander) {
                    net.nostalgia.client.ritual.RitualVisualManager.endTransition();
                } else {
                    net.nostalgia.client.ritual.RitualVisualManager.onDimensionChanged();
                }
            });
        });
        net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.registerGlobalReceiver(S2CSyncParticipantsPayload.TYPE, (payload, context) -> {
            context.client().execute(() -> {
                net.nostalgia.alphalogic.ritual.RitualActiveState.participants.clear();
                net.nostalgia.alphalogic.ritual.RitualActiveState.participants.addAll(payload.participants());
            });
        });
        net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.registerGlobalReceiver(S2CRitualPhasePayload.TYPE, (payload, context) -> {
            context.client().execute(() -> {
                net.nostalgia.client.ritual.RitualVisualManager.setPhase(payload.phase());
            });
        });
        net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.registerGlobalReceiver(S2CBystanderVisualsPayload.TYPE, (payload, context) -> {
            context.client().execute(() -> {
                net.nostalgia.client.ritual.RitualVisualManager.triggerBystanderVisuals(payload.center(), payload.offsetX(), payload.offsetY(), payload.offsetZ(), payload.targetDimensionId(), payload.phase());
            });
        });
        net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.registerGlobalReceiver(S2CGlassBreakPayload.TYPE, (payload, context) -> {
            context.client().execute(() -> {
                if (payload.active()) {
                    net.nostalgia.client.render.GlassBreakRenderer.start(payload.anchor());
                    if (context.player() != null) {
                        context.player().sendSystemMessage(net.minecraft.network.chat.Component.literal("§bGlassBreak: §aРАЗБИВАЕМ"));
                    }
                } else {
                    net.nostalgia.client.render.GlassBreakRenderer.stop();
                    if (context.player() != null) {
                        context.player().sendSystemMessage(net.minecraft.network.chat.Component.literal("§bGlassBreak: §cСТОП"));
                    }
                }
            });
        });
        net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.registerGlobalReceiver(S2CTimestopZoneStartPayload.TYPE, (payload, context) -> {
            context.client().execute(() -> {
                net.minecraft.resources.ResourceKey<net.minecraft.world.level.Level> dimKey = 
                    net.minecraft.resources.ResourceKey.create(net.minecraft.core.registries.Registries.DIMENSION, net.minecraft.resources.Identifier.tryParse(payload.dimensionId()));
                net.nostalgia.alphalogic.ritual.RitualManager.ActiveZone newZone = new net.nostalgia.alphalogic.ritual.RitualManager.ActiveZone(
                    dimKey, payload.beaconPos(), payload.radiusChunks(),
                    payload.snapGameTime(), payload.snapClockTicks(), payload.snapRain(), payload.snapThunder()
                );
                if (net.nostalgia.alphalogic.ritual.RitualManager.findZoneByBeacon(payload.beaconPos()) == null) {
                    net.nostalgia.alphalogic.ritual.RitualManager.activeZones.add(newZone);
                }

                net.nostalgia.client.ritual.ClientFreezeRegions.snapshots.put(
                    payload.beaconPos(),
                    new net.nostalgia.client.ritual.ClientFreezeRegions.ZoneSnapshot(
                        payload.snapGameTime(), payload.snapClockTicks(), payload.snapRain(), payload.snapThunder()
                    )
                );

                net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
                if (mc.level == null) {
                    pendingZoneStart = payload;
                    return;
                }

                if (mc.level.tickRateManager() instanceof net.nostalgia.alphalogic.ritual.TickRateManagerAccess access) {
                    net.minecraft.resources.ResourceKey<net.minecraft.world.level.Level> dim = mc.level.dimension();
                    if (dim.identifier().toString().equals(payload.dimensionId())) {
                        access.nostalgia$addRegion(new net.nostalgia.alphalogic.ritual.FreezeRegion(dim, payload.beaconPos(), payload.radiusChunks()));
                        nostalgia$markZoneChunksDirty(mc, payload.beaconPos(), payload.radiusChunks());
                        if (payload.instant()) {
                            net.nostalgia.client.ritual.ClientZoneTime.forceInstantSnapshot(
                                    payload.snapGameTime(), payload.snapClockTicks(),
                                    payload.snapRain(), payload.snapThunder());
                        }
                    }
                }
            });
        });
        net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.registerGlobalReceiver(S2CTimestopZoneEndPayload.TYPE, (payload, context) -> {
            context.client().execute(() -> {
                net.nostalgia.alphalogic.ritual.RitualManager.activeZones.removeIf(z -> z.beaconPos().equals(payload.beaconPos()));
                net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
                net.nostalgia.client.ritual.ClientFreezeRegions.snapshots.remove(payload.beaconPos());
                if (mc.level != null && mc.level.tickRateManager() instanceof net.nostalgia.alphalogic.ritual.TickRateManagerAccess access) {
                    nostalgia$markZoneChunksDirty(mc, payload.beaconPos(), net.nostalgia.alphalogic.ritual.RitualManager.ZONE_RADIUS_CHUNKS);
                    access.nostalgia$removeRegionAt(mc.level.dimension(), payload.beaconPos());
                }
            });
        });
        net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.registerGlobalReceiver(S2CZoneCollapsePayload.ID, (payload, context) -> {
            context.client().execute(() -> {
                net.nostalgia.client.ritual.ClientZoneTime.startZoneCollapse(payload.collapseDurationMs());
            });
        });
        net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.registerGlobalReceiver(S2CPortalDebugPayload.TYPE, (payload, context) -> {
            context.client().execute(() -> {
                if (payload.state()) {
                    net.nostalgia.client.render.PortalSkyRenderer.isDebugging = true;
                    net.nostalgia.client.render.PortalSkyRenderer.isAnimatingOut = false;
                    net.nostalgia.client.render.PortalSkyRenderer.isDebuggingInverted = payload.inverted();
                    net.nostalgia.client.render.PortalSkyRenderer.debugTime = 0.0f;
                    net.nostalgia.client.ritual.RitualVisualManager.soundPhase1Played = false;
                    net.nostalgia.client.ritual.RitualVisualManager.soundPhase2Played = false;
                    net.nostalgia.client.ritual.RitualVisualManager.soundPhase3Played = false;

                    net.nostalgia.alphalogic.ritual.RitualActiveState.offsetX = 0;
                    net.nostalgia.alphalogic.ritual.RitualActiveState.offsetZ = 0;
                    net.nostalgia.alphalogic.ritual.RitualActiveState.yOffset = 0;
                    net.nostalgia.client.ritual.ClientVirtualBlockCache.clear();
                    net.nostalgia.client.render.NostalgiaChunkCache.clear();

                    if (context.player() != null) {
                        net.nostalgia.client.render.PortalSkyRenderer.debugCenter = payload.center();
                        context.player().sendSystemMessage(net.minecraft.network.chat.Component.literal("Portal Sky Rip ENABLED"));
                        net.nostalgia.client.ritual.RitualVisualManager.portalMirageEntity = null;
                        net.nostalgia.client.render.NostalgiaChunkCache.generateCache(payload.center(), payload.seed(), "nostalgia:alpha_112_01");
                    }
                } else {
                    net.nostalgia.client.render.PortalSkyRenderer.startCloseAnimation();
                    if (context.player() != null) {
                        context.player().sendSystemMessage(net.minecraft.network.chat.Component.literal("Portal Sky Rip CLOSING"));
                    }
                }
            });
        });
    }
}
