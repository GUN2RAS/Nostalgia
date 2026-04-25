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
                net.nostalgia.network.NostalgiaNetworking.registerClientReceivers();

                net.sha.SHA.registerProvider(net.minecraft.resources.Identifier.fromNamespaceAndPath("nostalgia", "alpha_provider"), net.nostalgia.client.render.NostalgiaChunkCache.INSTANCE);

                net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry.register(net.nostalgia.entity.AlphaEntities.ALPHA_BOAT, net.nostalgia.client.render.AlphaBoatRenderer::new);
                net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry.register(net.nostalgia.entity.AlphaEntities.SKY_PORTAL_BEAM, net.nostalgia.client.render.SkyPortalBeamRenderer::new);

                net.minecraft.client.gui.screens.MenuScreens.register(net.nostalgia.inventory.ModScreenHandlers.TIME_MACHINE_MENU, net.nostalgia.client.gui.TimeMachineScreen::new);

                net.fabricmc.fabric.api.client.screen.v1.ScreenEvents.BEFORE_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
                        if (screen instanceof net.minecraft.client.gui.screens.TitleScreen) {
                                if (!net.nostalgia.client.gui.EpilepsyWarningScreen.hasAcceptedWarning()) {
                                        client.execute(() -> client.setScreen(new net.nostalgia.client.gui.EpilepsyWarningScreen(screen)));
                                }
                        }
                });

                net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
                        net.nostalgia.client.ritual.RitualVisualManager.endTransition();
                        net.nostalgia.client.ritual.ClientZoneTime.clear();
                        net.nostalgia.client.ritual.ZoneTimeBridge.hasClockReal = false;
                        net.nostalgia.client.ritual.ZoneTimeBridge.lastRealClockTicks = 0L;
                        net.nostalgia.client.ritual.RitualSoundManager.clear();
                });

                net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.END_CLIENT_TICK.register(client -> {
                        if (client.level != null && client.player != null && net.nostalgia.network.NostalgiaNetworking.pendingZoneStart != null) {
                                net.nostalgia.network.S2CTimestopZoneStartPayload payload = net.nostalgia.network.NostalgiaNetworking.pendingZoneStart;
                                net.nostalgia.network.NostalgiaNetworking.pendingZoneStart = null;
                                if (client.level.tickRateManager() instanceof net.nostalgia.alphalogic.ritual.TickRateManagerAccess access) {
                                        net.minecraft.resources.ResourceKey<net.minecraft.world.level.Level> dim = client.level.dimension();
                                        if (dim.identifier().toString().equals(payload.dimensionId())) {
                                                access.nostalgia$addRegion(new net.nostalgia.alphalogic.ritual.FreezeRegion(dim, payload.beaconPos(), payload.radiusChunks()));
                                                net.nostalgia.network.NostalgiaNetworking.nostalgia$markZoneChunksDirty(client, payload.beaconPos(), payload.radiusChunks());
                                                if (payload.instant()) {
                                                        net.nostalgia.client.ritual.ClientZoneTime.forceInstantSnapshot(
                                                                payload.snapGameTime(), payload.snapClockTicks(),
                                                                payload.snapRain(), payload.snapThunder());
                                                }
                                        }
                                }
                        }

                        if (client.level != null) {
                                long realClockTicks = net.nostalgia.client.ritual.ZoneTimeBridge.hasClockReal
                                        ? net.nostalgia.client.ritual.ZoneTimeBridge.lastRealClockTicks
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
                                net.nostalgia.client.ritual.ClientZoneTime.updateReals(realClockTicks, realGameTime, realRain, realThunder);
                        net.nostalgia.client.ritual.ClientZoneTime.tickCollapse();
                        }
                        net.nostalgia.client.ritual.RitualVisualManager.tick();
                        net.nostalgia.client.ritual.ScreenFreezer.tick();
                        if (client.level != null) {
                                net.nostalgia.client.ritual.TrailManager.tick(client.level.players());
                        } else {
                                net.nostalgia.client.ritual.TrailManager.tick(null);
                        }
                        net.nostalgia.client.ritual.RitualSoundManager.tick(client.player);
                        if (net.nostalgia.client.render.PortalSkyRenderer.isDebugging) {
                                if (net.nostalgia.client.render.PortalSkyRenderer.isAnimatingOut) {
                                        float oldTime = net.nostalgia.client.render.PortalSkyRenderer.debugTime;
                                        net.nostalgia.client.render.PortalSkyRenderer.debugTime -= 0.075f;

                                        if (oldTime >= 2.5f && net.nostalgia.client.render.PortalSkyRenderer.debugTime < 2.5f) {
                                                net.nostalgia.client.render.PortalSkyRenderer.islandVisible = false;

                                                if (!net.nostalgia.client.ritual.RitualVisualManager.isTransitioning) {
                                                        net.nostalgia.client.render.NostalgiaChunkCache.clear();
                                                }
                                                if (client.levelRenderer != null && client.player != null) {
                                                        net.minecraft.core.BlockPos center = net.nostalgia.client.render.PortalSkyRenderer.debugCenter;
                                                        net.sha.api.SHAHologramManager.markRadiusShellDirty(center, 0.0f, 320.0f);
                                                }
                                        }

                                        if (net.nostalgia.client.render.PortalSkyRenderer.debugTime <= 0) {
                                                net.nostalgia.client.render.PortalSkyRenderer.isDebugging = false;
                                                net.nostalgia.client.render.PortalSkyRenderer.isAnimatingOut = false;
                                                net.nostalgia.client.render.PortalSkyRenderer.debugTime = 0;
                                                net.nostalgia.client.render.PortalSkyRenderer.capturedProjectionMatrix = null;
                                                net.nostalgia.client.render.PortalSkyRenderer.capturedModelViewMatrix = null;
                                                if (client.player != null) {
                                                        client.player.sendSystemMessage(net.minecraft.network.chat.Component.literal("Portal Sky Rip CLOSED"));
                                                }
                                        }
                                } else {
                                        net.nostalgia.client.render.PortalSkyRenderer.islandVisible = true;
                                        net.nostalgia.client.render.PortalSkyRenderer.debugTime += 0.05f;
                                }
                        }
                });
        }
}
