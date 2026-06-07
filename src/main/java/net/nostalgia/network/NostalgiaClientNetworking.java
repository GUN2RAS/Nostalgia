package net.nostalgia.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.nostalgia.network.*;
import net.nostalgia.client.gui.TimeMachineScreen;
import net.nostalgia.client.events.caches.providers.DimensionHologramCache;
import net.nostalgia.client.events.caches.providers.DimensionHologramRegistry;
import net.nostalgia.client.events.caches.providers.HologramSection;
import net.nostalgia.client.events.caches.providers.HologramDiskCache;
import net.nostalgia.client.events.caches.UniversalHologramCache;
import net.nostalgia.client.events.core.ClientFreezeRegions;
import net.nostalgia.client.events.core.ClientZoneTime;
import net.nostalgia.client.events.echo.RitualVisualManager;
import net.nostalgia.client.events.echo.RitualSoundManager;
import net.nostalgia.client.events.echo.TrailManager;
import net.nostalgia.client.render.PortalSkyRenderer;
import net.nostalgia.client.render.GlassBreakRenderer;
import net.nostalgia.alphalogic.ritual.event.ClientEchoRitualView;
import net.nostalgia.alphalogic.ritual.TickRateManagerAccess;
import net.nostalgia.alphalogic.ritual.FreezeRegion;
import net.nostalgia.alphalogic.ritual.TimestopZoneManager;
import net.nostalgia.alphalogic.bridge.AlphaEngineManager;
import net.nostalgia.mixin.client.ritual.LevelRainFieldAccessor;

@Environment(EnvType.CLIENT)
public class NostalgiaClientNetworking {

    public static void nostalgia$markZoneChunksDirty(Minecraft mc, BlockPos beaconPos, int chunkRadius) {
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

    public static void registerClientReceivers() {
        ClientPlayNetworking.registerGlobalReceiver(S2CSetTerminalErrorPayload.TYPE, (payload, context) -> {
            context.client().execute(() -> {
                TimeMachineScreen.nextScreenIsError = true;
            });
        });
        ClientPlayNetworking.registerGlobalReceiver(S2CSyncAlphaDeltasPayload.TYPE, (payload, context) -> {
            if (payload.positions() == null || payload.states() == null) return;
            if (payload.positions().length != payload.states().length) return;
            context.client().execute(() -> {
                String dimId = payload.dimensionId();
                DimensionHologramCache cache = DimensionHologramRegistry.getByName(dimId);
                if (cache != null) {
                    for (int i = 0; i < payload.positions().length; i++) {
                        cache.setOverrideRaw(payload.positions()[i], net.minecraft.world.level.block.Block.stateById(payload.states()[i]));
                    }
                }
                for (long posAsLong : payload.positions()) {
                    BlockPos pos = BlockPos.of(posAsLong);
                    net.sha.api.SHAHologramManager.markAreaDirty(pos.getX(), pos.getY(), pos.getZ(), pos.getX(), pos.getY(), pos.getZ());
                    for (net.nostalgia.client.events.core.IHologramContext ctx : UniversalHologramCache.ACTIVE_CONTEXTS) {
                        if (ctx.isActive() && dimId.equals(ctx.getTargetDimension())) {
                            int worldX, worldY, worldZ;
                            if (ctx.isSkyInverted()) {
                                int crackPlaneY = PortalSkyRenderer.crackPlaneY;
                                int crackPlaneYTarget = PortalSkyRenderer.crackPlaneYTarget;
                                int inversionConstant = crackPlaneY + crackPlaneYTarget;
                                worldX = pos.getX();
                                int portalZ = PortalSkyRenderer.portalCenter.getZ();
                                worldZ = 2 * portalZ - pos.getZ();
                                worldY = inversionConstant - pos.getY();
                            } else {
                                worldX = pos.getX() - ctx.getOffsetX();
                                worldY = pos.getY() + ctx.getOffsetY();
                                worldZ = pos.getZ() - ctx.getOffsetZ();
                            }
                            net.sha.api.SHAHologramManager.markAreaDirty(worldX, worldY, worldZ, worldX, worldY, worldZ);
                        }
                    }
                }
            });
        });
        ClientPlayNetworking.registerGlobalReceiver(S2CDebugOwerPayload.TYPE, (payload, context) -> {
            context.client().execute(() -> {
                if (payload.active()) {
                    net.nostalgia.client.events.core.DebugOwerContext.INSTANCE.setActive(true, payload.center());
                    UniversalHologramCache.overworldCacheReady = false;
                } else {
                    net.nostalgia.client.events.core.DebugOwerContext.INSTANCE.setActive(false, null);
                    UniversalHologramCache.clearMemoryCaches();
                    net.sha.api.SHAHologramManager.markRadiusShellDirty(payload.center(), 0.0f, 320.0f);
                }
            });
        });
        ClientPlayNetworking.registerGlobalReceiver(S2CDimensionSectionsPayload.TYPE, (payload, context) -> {
            it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap<HologramSection> map = new it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap<>();
            net.minecraft.core.Registry<net.minecraft.world.level.biome.Biome> biomeRegistry = context.client().level.registryAccess().lookupOrThrow(Registries.BIOME);
            
            for (S2CDimensionSectionsPayload.SectionData sd : payload.sections()) {
                net.minecraft.world.level.block.state.BlockState[] palette = new net.minecraft.world.level.block.state.BlockState[sd.paletteIds().length];
                for (int i = 0; i < sd.paletteIds().length; i++) {
                    palette[i] = net.minecraft.world.level.block.Block.stateById(sd.paletteIds()[i]);
                }
                
                net.minecraft.core.Holder<net.minecraft.world.level.biome.Biome>[] biomePalette = null;
                if (sd.biomePaletteIds() != null && sd.biomePaletteIds().length > 0) {
                    biomePalette = new net.minecraft.core.Holder[sd.biomePaletteIds().length];
                    for (int i = 0; i < sd.biomePaletteIds().length; i++) {
                        biomePalette[i] = biomeRegistry.get(sd.biomePaletteIds()[i]).orElse(biomeRegistry.getAny().get());
                    }
                }
                
                HologramSection section = new HologramSection(palette, sd.indices(), biomePalette, sd.biomeIndices());
                long key = (((long) sd.chunkX() & 0x3FFFFF) << 42) | (((long) sd.sectionY() & 0xFFFFF) << 22) | ((long) sd.chunkZ() & 0x3FFFFF);
                map.put(key, section);
            }

            DimensionHologramCache cache = DimensionHologramRegistry.getByName(payload.dimensionId());
            if (cache != null) {
                cache.putSections(map);
                
                if (payload.chunkPositions() != null && payload.chunkVersions() != null) {
                    it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap versions = cache.getChunkVersions();
                    for (int i = 0; i < payload.chunkPositions().length; i++) {
                        versions.put(payload.chunkPositions()[i], payload.chunkVersions()[i]);
                    }
                }

                Minecraft.getInstance().execute(() -> {
                    if (!net.nostalgia.alphalogic.ritual.DimensionUtil.isClientGenerated(payload.dimensionId())) {
                        UniversalHologramCache.overworldCacheReady = true;
                    }
                });
            }
        });
        ClientPlayNetworking.registerGlobalReceiver(S2CHologramReadyPayload.TYPE, (payload, context) -> {
            context.client().execute(() -> {
                UniversalHologramCache.overworldCacheReady = true;
                net.sha.api.SHAHologramManager.updateSpatialMap(UniversalHologramCache.INSTANCE);
                
                DimensionHologramCache cache = DimensionHologramRegistry.getByName(payload.dimensionId());
                if (cache != null) {
                    it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap<HologramSection> copyForDisk = cache.getSections().clone();
                    it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap versionsForDisk = cache.getChunkVersions().clone();
                    java.util.concurrent.CompletableFuture.runAsync(() -> {
                        HologramDiskCache.saveDimensionCache(payload.dimensionId(), copyForDisk, versionsForDisk);
                    });
                }
                
                if (context.player() == null || context.player().level() == null) return;
                
                ResourceKey<Level> dim = context.player().level().dimension();
                boolean isAlpha = dim == net.nostalgia.world.dimension.ModDimensions.ALPHA_112_01_LEVEL_KEY || dim == net.nostalgia.world.dimension.ModDimensions.RD_132211_LEVEL_KEY;
                
                ClientEchoRitualView t = net.nostalgia.client.events.core.ClientRitualEventRegistry.activeTransition();
                boolean isTransition = t != null && !t.isBystander();
                
                if (isTransition) {
                    net.sha.api.SHAHologramManager.markRadiusShellDirty(payload.center(), 0.0f, (float) payload.radius());
                } else {
                    if (!isAlpha) {
                        int cx = payload.center().getX();
                        int cz = payload.center().getZ();
                        int r = payload.radius();
                        net.sha.api.SHAHologramManager.markAreaDirty(cx - r, 150, cz - r, cx + r, 320, cz + r);
                    }
                }
            });
        });
        ClientPlayNetworking.registerGlobalReceiver(S2CStartTransitionVisualsPayload.TYPE, (payload, context) -> {
            context.client().execute(() -> {
                AlphaEngineManager.setWorldSeed(payload.seed());
                if (net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.activeInstance() == null) {
                    net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.startEvent(payload.beaconPos(), null);
                }
                RitualVisualManager.targetSkyColor = payload.targetSkyColor();
                RitualVisualManager.targetFogColor = payload.targetFogColor();
                net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.setOffsets(payload.offsetX(), payload.offsetY(), payload.offsetZ());
                RitualVisualManager.startTransition(payload.instanceId(), payload.beaconPos(), payload.dimensionId(), payload.safeSpawnPos(), payload.beaconStateId(), payload.anchorStateId());
            });
        });
        ClientPlayNetworking.registerGlobalReceiver(S2CEndTransitionVisualsPayload.TYPE, (payload, context) -> {
            context.client().execute(() -> {
                java.util.UUID myId = RitualVisualManager.myInstanceId;
                if (myId != null && !myId.equals(payload.instanceId())) return;
                ClientEchoRitualView t = net.nostalgia.client.events.core.ClientRitualEventRegistry.activeTransition();
                if (t != null && t.isBystander()) {
                    RitualVisualManager.endTransition();
                } else {
                    RitualVisualManager.onDimensionChanged();
                }
            });
        });
        ClientPlayNetworking.registerGlobalReceiver(S2CSyncParticipantsPayload.TYPE, (payload, context) -> {
            context.client().execute(() -> {
                net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.setParticipants(payload.participants());
            });
        });
        ClientPlayNetworking.registerGlobalReceiver(S2CRitualPhasePayload.TYPE, (payload, context) -> {
            context.client().execute(() -> {
                java.util.UUID myId = RitualVisualManager.myInstanceId;
                if (myId != null && !myId.equals(payload.instanceId())) return;
                RitualVisualManager.setPhase(payload.phase());
            });
        });
        ClientPlayNetworking.registerGlobalReceiver(S2CBystanderVisualsPayload.TYPE, (payload, context) -> {
            context.client().execute(() -> {
                java.util.UUID myId = RitualVisualManager.myInstanceId;
                if (myId != null && !myId.equals(payload.instanceId())) {
                    return;
                }
                if (net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.activeInstance() == null) {
                    net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.startEvent(payload.center(), null);
                }
                RitualVisualManager.triggerBystanderVisuals(payload.center(), payload.offsetX(), payload.offsetY(), payload.offsetZ(), payload.targetDimensionId(), payload.phase());
            });
        });
        ClientPlayNetworking.registerGlobalReceiver(S2CGlassBreakPayload.TYPE, (payload, context) -> {
            context.client().execute(() -> {
                if (payload.active()) {
                    GlassBreakRenderer.start(payload.anchor());
                } else {
                    GlassBreakRenderer.stop();
                }
            });
        });
        ClientPlayNetworking.registerGlobalReceiver(S2CTimestopZoneStartPayload.TYPE, (payload, context) -> {
            context.client().execute(() -> {
                ResourceKey<Level> dimKey =
                    ResourceKey.create(Registries.DIMENSION, Identifier.tryParse(payload.dimensionId()));
                net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.registerZoneLocal(
                    dimKey, payload.beaconPos(), payload.radiusChunks(),
                    payload.snapGameTime(), payload.snapClockTicks(), payload.snapRain(), payload.snapThunder()
                );

                net.nostalgia.client.events.core.ClientFreezeRegions.snapshots.put(
                    payload.beaconPos(),
                    new net.nostalgia.client.events.core.ClientFreezeRegions.ZoneSnapshot(
                        payload.snapGameTime(), payload.snapClockTicks(), payload.snapRain(), payload.snapThunder()
                    )
                );

                Minecraft mc = Minecraft.getInstance();
                if (mc.level == null) {
                    NostalgiaNetworking.pendingZoneStart = payload;
                    return;
                }

                if (mc.level.tickRateManager() instanceof TickRateManagerAccess access) {
                    ResourceKey<Level> dim = mc.level.dimension();
                    if (dim.identifier().toString().equals(payload.dimensionId())) {
                        access.nostalgia$addRegion(new FreezeRegion(dim, payload.beaconPos(), payload.radiusChunks()));
                        nostalgia$markZoneChunksDirty(mc, payload.beaconPos(), payload.radiusChunks());
                        if (payload.instant()) {
                            ClientZoneTime.forceInstantSnapshot(
                                    payload.snapGameTime(), payload.snapClockTicks(),
                                    payload.snapRain(), payload.snapThunder());
                        }
                    }
                }
            });
        });
        ClientPlayNetworking.registerGlobalReceiver(S2CTimestopZoneEndPayload.TYPE, (payload, context) -> {
            context.client().execute(() -> {
                net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.unregisterZoneByBeacon(payload.beaconPos());
                Minecraft mc = Minecraft.getInstance();
                net.nostalgia.client.events.core.ClientFreezeRegions.snapshots.remove(payload.beaconPos());
                if (mc.level != null && mc.level.tickRateManager() instanceof TickRateManagerAccess access) {
                    nostalgia$markZoneChunksDirty(mc, payload.beaconPos(), TimestopZoneManager.ZONE_RADIUS_CHUNKS);
                    access.nostalgia$removeRegionAt(mc.level.dimension(), payload.beaconPos());
                }
            });
        });
        ClientPlayNetworking.registerGlobalReceiver(S2CSkyPortalCancelPayload.TYPE, (payload, context) -> {
            context.client().execute(() -> {
                if (context.player() != null) {
                    context.player().level().playSound(context.player(), context.player().blockPosition(), net.minecraft.sounds.SoundEvents.BEACON_DEACTIVATE, net.minecraft.sounds.SoundSource.MASTER, 1.0f, 0.5f);
                }
            });
        });
        ClientPlayNetworking.registerGlobalReceiver(S2CZoneCollapsePayload.ID, (payload, context) -> {
            context.client().execute(() -> {
                ClientZoneTime.startZoneCollapse(payload.collapseDurationMs());
            });
        });
        ClientPlayNetworking.registerGlobalReceiver(S2CSkyPortalPayload.TYPE, (payload, context) -> {
            context.client().execute(() -> {
                if (!RitualVisualManager.isTransitioning) {
                    UniversalHologramCache.clearMemoryCaches();
                }

                if (payload.state()) {
                    PortalSkyRenderer.active = true;
                    PortalSkyRenderer.isAnimatingOut = false;
                    PortalSkyRenderer.inverted = payload.inverted();

                    if (payload.restored()) {
                        PortalSkyRenderer.portalTime = 10.0f;
                        RitualVisualManager.soundPhase1Played = true;
                        RitualVisualManager.soundPhase2Played = true;
                        RitualVisualManager.soundPhase3Played = true;
                    } else {
                        PortalSkyRenderer.portalTime = 0.0f;
                        RitualVisualManager.soundPhase1Played = false;
                        RitualVisualManager.soundPhase2Played = false;
                        RitualVisualManager.soundPhase3Played = false;
                    }

                    if (context.player() != null) {
                        PortalSkyRenderer.portalCenter = payload.center();
                        PortalSkyRenderer.crackPlaneY = payload.crackPlaneY();
                        PortalSkyRenderer.crackPlaneYTarget = payload.crackPlaneYTarget();

                        com.example.api.GravityAnomalyZone.set(
                                payload.crackPlaneY() + 10.0, payload.crackPlaneY() + 15.0,
                                payload.center().getX(), payload.center().getZ(), 300.0);
                        PortalSkyRenderer.originalSourceDimension = payload.sourceDimension();
                        PortalSkyRenderer.originalTargetDimension = payload.targetDimension();
                        String targetDim = payload.targetDimension();
                        String sourceDim = payload.sourceDimension();
                        String renderDim = targetDim;
                        
                        if (context.player().level().dimension().identifier().toString().equals(targetDim)) {
                            renderDim = sourceDim;
                        }

                        BlockPos readCenter = payload.center();
                        int offsetX = 0;
                        int offsetY = 0;
                        int offsetZ = 0;
                        if (net.nostalgia.alphalogic.ritual.DimensionUtil.isRD(renderDim)) {
                            readCenter = new BlockPos(128, 43, 128);
                            offsetX = payload.center().getX() - readCenter.getX();
                            offsetY = payload.center().getY() - readCenter.getY();
                            offsetZ = payload.center().getZ() - readCenter.getZ();
                        }

                        if (!RitualVisualManager.isTransitioning) {
                            net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.setOffsets(offsetX, offsetY, offsetZ);
                        }
                        
                        PortalSkyRenderer.targetDimension = renderDim;
                        PortalSkyRenderer.islandVisible = true;
                        RitualVisualManager.portalMirageEntity = null;
                        
                        if (!RitualVisualManager.isTransitioning) {
                            final String finalRenderDim = renderDim;
                            if (!context.player().level().dimension().identifier().toString().equals(finalRenderDim)) {
                                if (net.nostalgia.alphalogic.ritual.DimensionUtil.isClientGenerated(finalRenderDim)) {
                                    net.nostalgia.client.events.caches.impl.AlphaByteCache.generateCache(readCenter, payload.seed(), finalRenderDim);
                                } else {
                                    java.util.concurrent.CompletableFuture.runAsync(() -> {
                                        HologramDiskCache.DimensionCacheResult diskResult = HologramDiskCache.loadDimensionCache(finalRenderDim);
                                        if (diskResult != null && diskResult.sections() != null && !diskResult.sections().isEmpty()) {
                                            Minecraft.getInstance().execute(() -> {
                                                DimensionHologramCache cache = DimensionHologramRegistry.getByName(finalRenderDim);
                                                if (cache != null) {
                                                    cache.setSections(diskResult.sections());
                                                    cache.setChunkVersions(diskResult.chunkVersions());
                                                }
                                                UniversalHologramCache.overworldCacheReady = true;
                                                net.sha.api.SHAHologramManager.updateSpatialMap(UniversalHologramCache.INSTANCE);
                                                net.sha.api.SHAHologramManager.markRadiusShellDirty(payload.center(), 0.0f, 320.0f);
                                                ClientPlayNetworking.send(new C2SCacheReadyPayload(true, cache != null ? cache.getChunkVersions().keySet().toLongArray() : new long[0], cache != null ? cache.getChunkVersions().values().toLongArray() : new long[0]));
                                            });
                                        } else {
                                            Minecraft.getInstance().execute(() -> {
                                                ClientPlayNetworking.send(new C2SCacheReadyPayload(false));
                                            });
                                        }
                                    });
                                }
                            }
                        }
                    }
                } else {
                    PortalSkyRenderer.startCloseAnimation();
                    com.example.api.GravityAnomalyZone.clear();
                }
            });
        });
    }
}
