package net.nostalgia;

import net.fabricmc.api.ClientModInitializer;

import net.nostalgia.block.AlphaBlocks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;

import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.client.multiplayer.ClientLevel;
import net.nostalgia.network.C2STravelRequestPayload;
import net.nostalgia.world.dimension.ModDimensions;

public class NostalgiaClient implements ClientModInitializer {

        public void onInitializeClient() {
                if (!net.fabricmc.loader.api.FabricLoader.getInstance().isModLoaded("sodium")) {
                        throw new RuntimeException("Mod 'Alpha Protocol' (nostalgia) requires 'sodium' on the client!");
                }
                net.nostalgia.network.NostalgiaClientNetworking.registerClientReceivers();

                net.sha.SHA.registerProvider(net.minecraft.resources.Identifier.fromNamespaceAndPath("nostalgia", "alpha_provider"), net.nostalgia.client.events.caches.UniversalHologramCache.INSTANCE);

                net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry.register(net.nostalgia.entity.AlphaEntities.ALPHA_BOAT, net.nostalgia.client.render.AlphaBoatRenderer::new);
                net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry.register(net.nostalgia.entity.AlphaEntities.SKY_PORTAL_BEAM, net.nostalgia.client.render.SkyPortalBeamRenderer::new);
                net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry.register(net.nostalgia.entity.AlphaEntities.THROWN_AMETHYST, net.nostalgia.client.render.ThrownAmethystRenderer::new);

                net.minecraft.client.gui.screens.MenuScreens.register(net.nostalgia.inventory.ModScreenHandlers.TIME_MACHINE_MENU, net.nostalgia.client.gui.TimeMachineScreen::new);
                net.minecraft.client.gui.screens.MenuScreens.register(net.nostalgia.inventory.ModScreenHandlers.LODESTONE_GRAVITY_MENU, net.nostalgia.client.gui.LodestoneGravityScreen::new);

                net.fabricmc.fabric.api.client.screen.v1.ScreenEvents.BEFORE_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
                        if (screen instanceof net.minecraft.client.gui.screens.TitleScreen) {
                                if (!net.nostalgia.client.gui.EpilepsyWarningScreen.hasAcceptedWarning()) {
                                        client.execute(() -> client.setScreen(new net.nostalgia.client.gui.EpilepsyWarningScreen(screen)));
                                }
                        }
                });

                net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
                        java.util.concurrent.CompletableFuture.runAsync(() -> {
                                net.nostalgia.client.events.caches.providers.HologramDiskCache.DimensionCacheResult diskResult = net.nostalgia.client.events.caches.providers.HologramDiskCache.loadDimensionCache("minecraft:overworld");
                                if (diskResult != null && diskResult.sections() != null && !diskResult.sections().isEmpty()) {
                                        client.execute(() -> {
                                                net.nostalgia.client.events.caches.providers.DimensionHologramCache cache = net.nostalgia.client.events.caches.providers.DimensionHologramRegistry.getByName("minecraft:overworld");
                                                if (cache != null) {
                                                    cache.setSections(diskResult.sections());
                                                    cache.setChunkVersions(diskResult.chunkVersions());
                                                }
                                                net.nostalgia.client.events.caches.UniversalHologramCache.overworldCacheReady = true;
                                        });
                                }
                        });
                });

                net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
                        net.nostalgia.client.events.echo.RitualVisualManager.endTransition();
                        net.nostalgia.client.events.core.ClientZoneTime.clear();
                        net.nostalgia.client.events.core.ZoneTimeBridge.hasClockReal = false;
                        net.nostalgia.client.events.core.ZoneTimeBridge.lastRealClockTicks = 0L;
                        net.nostalgia.client.events.echo.RitualSoundManager.clear();
                        net.nostalgia.client.events.caches.UniversalHologramCache.debugOwer = false;
                        net.nostalgia.client.events.caches.UniversalHologramCache.debugOwerCenter = null;
                        net.nostalgia.client.events.caches.UniversalHologramCache.clearMemoryCaches();
                        net.nostalgia.client.events.caches.providers.DimensionHologramRegistry.clearAll();
                        net.nostalgia.client.render.PortalSkyRenderer.active = false;
                        net.nostalgia.client.render.PortalSkyRenderer.islandVisible = false;
                        net.nostalgia.client.render.PortalSkyRenderer.portalTime = 0.0f;
                });

                net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents.CLIENT_STOPPING.register(client -> {
                });

                net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.END_CLIENT_TICK.register(client -> {
                        if (client.level != null && client.player != null && net.nostalgia.network.NostalgiaNetworking.pendingZoneStart != null) {
                                net.nostalgia.network.S2CTimestopZoneStartPayload payload = net.nostalgia.network.NostalgiaNetworking.pendingZoneStart;
                                net.nostalgia.network.NostalgiaNetworking.pendingZoneStart = null;
                                if (client.level.tickRateManager() instanceof net.nostalgia.alphalogic.ritual.TickRateManagerAccess access) {
                                        net.minecraft.resources.ResourceKey<net.minecraft.world.level.Level> dim = client.level.dimension();
                                        if (dim.identifier().toString().equals(payload.dimensionId())) {
                                                access.nostalgia$addRegion(new net.nostalgia.alphalogic.ritual.FreezeRegion(dim, payload.beaconPos(), payload.radiusChunks()));
                                                net.nostalgia.network.NostalgiaClientNetworking.nostalgia$markZoneChunksDirty(client, payload.beaconPos(), payload.radiusChunks());
                                                if (payload.instant()) {
                                                        net.nostalgia.client.events.core.ClientZoneTime.forceInstantSnapshot(
                                                                payload.snapGameTime(), payload.snapClockTicks(),
                                                                payload.snapRain(), payload.snapThunder());
                                                }
                                        }
                                }
                        }

                        if (client.level != null) {
                                long realClockTicks = net.nostalgia.client.events.core.ZoneTimeBridge.hasClockReal
                                        ? net.nostalgia.client.events.core.ZoneTimeBridge.lastRealClockTicks
                                        : 0L;
                                long realGameTime = 0L;
                                try {
                                        realGameTime = client.level.getGameTime();
                                } catch (Throwable ignored) {}
                                float realRain = 0.0f;
                                float realThunder = 0.0f;
                                try {
                                        if (client.level instanceof net.nostalgia.mixin.client.ritual.LevelRainFieldAccessor acc) {
                                                realRain = acc.nostalgia$getRainLevelField();
                                                realThunder = acc.nostalgia$getThunderLevelField();
                                        }
                                } catch (Throwable ignored) {}
                                net.nostalgia.client.events.core.ClientZoneTime.updateReals(realClockTicks, realGameTime, realRain, realThunder);
                        net.nostalgia.client.events.core.ClientZoneTime.tickCollapse();
                        }
                        net.nostalgia.client.events.echo.RitualVisualManager.tick();
                        net.nostalgia.client.render.PortalSkyRenderer.tickSkyPortalTransition();
                        if (client.level != null) {
                                net.nostalgia.client.events.echo.TrailManager.tick(client.level.players());
                        } else {
                                net.nostalgia.client.events.echo.TrailManager.tick(null);
                        }
                        net.nostalgia.client.events.echo.RitualSoundManager.tick(client.player);
                        if (net.nostalgia.client.render.PortalSkyRenderer.active && (!net.nostalgia.client.events.echo.RitualVisualManager.isTransitioning || net.nostalgia.client.render.PortalSkyRenderer.isAnimatingOut || net.nostalgia.client.events.echo.RitualVisualManager.isBystander)) {
                                if (net.nostalgia.client.render.PortalSkyRenderer.isAnimatingOut) {
                                        float oldRadius = net.nostalgia.client.events.echo.RitualVisualManager.getPortalAlphaRadius();
                                        float oldTime = net.nostalgia.client.render.PortalSkyRenderer.portalTime;
                                        net.nostalgia.client.render.PortalSkyRenderer.portalTime -= 0.075f;
                                        float newRadius = net.nostalgia.client.events.echo.RitualVisualManager.getPortalAlphaRadius();

                                        if (client.levelRenderer != null && client.player != null) {
                                            net.minecraft.core.BlockPos center = net.nostalgia.client.render.PortalSkyRenderer.portalCenter;
                                            if (center != null) {
                                                float minR = Math.max(0, newRadius - 8.0f);
                                                float maxR = oldRadius + 16.0f;
                                                int minSecX = (int) Math.floor((center.getX() - maxR) / 16.0);
                                                int maxSecX = (int) Math.ceil((center.getX() + maxR) / 16.0);
                                                int minSecZ = (int) Math.floor((center.getZ() - maxR) / 16.0);
                                                int maxSecZ = (int) Math.ceil((center.getZ() + maxR) / 16.0);
                                                
                                                float minSq = minR * minR;
                                                float maxSq = maxR * maxR;
                                                
                                                for (int x = minSecX; x <= maxSecX; x++) {
                                                    for (int z = minSecZ; z <= maxSecZ; z++) {
                                                        double dx = (x * 16 + 8) - center.getX();
                                                        double dz = (z * 16 + 8) - center.getZ();
                                                        double distSq = dx*dx + dz*dz;
                                                        if (distSq >= minSq && distSq <= maxSq) {
                                                            for (int y = 10; y <= 21; y++) {
                                                                client.levelRenderer.setSectionDirtyWithNeighbors(x, y, z);
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                        if (oldTime >= 2.85f && net.nostalgia.client.render.PortalSkyRenderer.portalTime < 2.85f) {
                                                net.nostalgia.client.render.PortalSkyRenderer.islandVisible = false;
                                                if (client.levelRenderer != null && client.player != null) {
                                                        net.minecraft.core.BlockPos center = net.nostalgia.client.render.PortalSkyRenderer.portalCenter;
                                                        net.sha.api.SHAHologramManager.markRadiusShellDirty(center, 0.0f, 320.0f);
                                                }
                                        }

                                        if (net.nostalgia.client.render.PortalSkyRenderer.portalTime <= 0) {
                                                net.nostalgia.client.render.PortalSkyRenderer.active = false;
                                                net.nostalgia.client.render.PortalSkyRenderer.isAnimatingOut = false;
                                                net.nostalgia.client.render.PortalSkyRenderer.portalTime = 0;
                                                net.nostalgia.client.render.PortalSkyRenderer.islandVisible = false;
                                                net.nostalgia.client.render.PortalSkyRenderer.capturedProjectionMatrix = null;
                                                net.nostalgia.client.render.PortalSkyRenderer.capturedModelViewMatrix = null;

                                                // ВОТ ЗДЕСЬ очищаем кэш и обновляем чанки, когда active УЖЕ false,
                                                // чтобы визуальные блоки Скай Портала 100% пропали из мира и не зависали в небе!
                                                if (net.nostalgia.client.events.core.ClientRitualEventRegistry.activeTransition() == null && !net.nostalgia.client.render.PortalSkyRenderer.skyPortalTransitioning) {
                                                        net.nostalgia.client.events.caches.UniversalHologramCache.clearMemoryCaches();
                                                        net.nostalgia.client.events.caches.providers.DimensionHologramRegistry.clearAll();
                                                        if (client.levelRenderer != null && client.player != null) {
                                                                net.minecraft.core.BlockPos center = net.nostalgia.client.render.PortalSkyRenderer.portalCenter;
                                                                net.sha.api.SHAHologramManager.markRadiusShellDirty(center, 0.0f, 320.0f);
                                                        }
                                                }
                                        }
                                } else {
                                        net.nostalgia.client.render.PortalSkyRenderer.islandVisible = true;
                                        net.nostalgia.client.render.PortalSkyRenderer.portalTime += 0.05f;
                                }
                        }
                });
        }
}
