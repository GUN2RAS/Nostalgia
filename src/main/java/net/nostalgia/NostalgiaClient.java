package net.nostalgia;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.api.ClientModInitializer;
import net.nostalgia.client.events.caches.providers.HologramSection;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents.ClientStopping;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.EndTick;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents.Disconnect;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents.Join;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents.BeforeInit;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.nostalgia.alphalogic.ritual.FreezeRegion;
import net.nostalgia.alphalogic.ritual.TickRateManagerAccess;
import net.nostalgia.client.events.caches.UniversalHologramCache;
import net.nostalgia.client.events.caches.providers.DimensionHologramCache;
import net.nostalgia.client.events.caches.providers.DimensionHologramRegistry;
import net.nostalgia.client.events.caches.providers.HologramDiskCache;
import net.nostalgia.client.events.core.ClientRitualEventRegistry;
import net.nostalgia.client.events.core.ClientZoneTime;
import net.nostalgia.client.events.core.ZoneTimeBridge;
import net.nostalgia.client.events.echo.RitualSoundManager;
import net.nostalgia.client.events.echo.RitualVisualManager;
import net.nostalgia.client.events.echo.TrailManager;
import net.nostalgia.client.gui.EpilepsyWarningScreen;
import net.nostalgia.client.gui.LodestoneGravityScreen;
import net.nostalgia.client.gui.TimeMachineScreen;
import net.nostalgia.client.gui.hologram3d.CacheDiagnostics;
import net.nostalgia.client.render.AlphaBoatRenderer;
import net.nostalgia.client.render.PortalSkyRenderer;
import net.nostalgia.client.render.SkyPortalBeamRenderer;
import net.nostalgia.client.render.ThrownAmethystRenderer;
import net.nostalgia.entity.AlphaEntities;
import net.nostalgia.inventory.ModScreenHandlers;
import net.nostalgia.mixin.client.ritual.LevelRainFieldAccessor;
import net.nostalgia.network.NostalgiaClientNetworking;
import net.nostalgia.network.NostalgiaNetworking;
import net.nostalgia.network.S2CTimestopZoneStartPayload;
import net.sha.SHA;
import net.sha.api.SHAHologramManager;

public class NostalgiaClient implements ClientModInitializer {
  public NostalgiaClient() {
  }

  public void onInitializeClient() {
    if (!FabricLoader.getInstance().isModLoaded("sodium")) {
      throw new RuntimeException("Mod 'Alpha Protocol' (nostalgia) requires 'sodium' on the client!");
    } else {
      NostalgiaClientNetworking.registerClientReceivers();
      ClientCommandRegistrationCallback.EVENT
        .register(
          (ClientCommandRegistrationCallback)(dispatcher, registryAccess) -> dispatcher.register(
            (LiteralArgumentBuilder)LiteralArgumentBuilder.literal("ncachediag").executes(ctx -> {
              CacheDiagnostics.runFullDiagnostic();
              return 1;
            })
          )
        );
      SHA.registerProvider(Identifier.fromNamespaceAndPath("nostalgia", "alpha_provider"), UniversalHologramCache.INSTANCE);
      EntityRendererRegistry.register(AlphaEntities.ALPHA_BOAT, AlphaBoatRenderer::new);
      EntityRendererRegistry.register(AlphaEntities.SKY_PORTAL_BEAM, SkyPortalBeamRenderer::new);
      EntityRendererRegistry.register(AlphaEntities.THROWN_AMETHYST, ThrownAmethystRenderer::new);
      MenuScreens.register(ModScreenHandlers.TIME_MACHINE_MENU, TimeMachineScreen::new);
      MenuScreens.register(ModScreenHandlers.LODESTONE_GRAVITY_MENU, LodestoneGravityScreen::new);
      ScreenEvents.BEFORE_INIT.register((BeforeInit)(client, screen, scaledWidth, scaledHeight) -> {
        if (screen instanceof TitleScreen && !EpilepsyWarningScreen.hasAcceptedWarning()) {
          client.execute(() -> client.setScreen(new EpilepsyWarningScreen(screen)));
        }
      });
      ClientPlayConnectionEvents.JOIN.register((Join)(handler, sender, client) -> CompletableFuture.runAsync(() -> {
        HologramDiskCache.DimensionCacheResult diskResult = HologramDiskCache.loadDimensionCache("minecraft:overworld");
        if (diskResult != null && diskResult.sections() != null && !diskResult.sections().isEmpty()) {
          client.execute(() -> {
            DimensionHologramCache cache = DimensionHologramRegistry.getByName("minecraft:overworld");
            if (cache != null) {
              Long2ObjectOpenHashMap<HologramSection> merged = new Long2ObjectOpenHashMap<>(diskResult.sections());
              Long2ObjectOpenHashMap<HologramSection> existing = cache.getSections();
              if (existing != null && !existing.isEmpty()) {
                merged.putAll(existing);
              }
              cache.setSections(merged);
              cache.setChunkVersions(diskResult.chunkVersions());
            }

            UniversalHologramCache.overworldCacheReady = true;
          });
        }
      }));
      ClientPlayConnectionEvents.DISCONNECT.register((Disconnect)(handler, client) -> {
        RitualVisualManager.endTransition();
        ClientZoneTime.clear();
        ZoneTimeBridge.hasClockReal = false;
        ZoneTimeBridge.lastRealClockTicks = 0L;
        RitualSoundManager.clear();
        UniversalHologramCache.debugOwer = false;
        UniversalHologramCache.debugOwerCenter = null;
        UniversalHologramCache.clearMemoryCaches();
        DimensionHologramRegistry.clearAll();
        PortalSkyRenderer.active = false;
        PortalSkyRenderer.islandVisible = false;
        NostalgiaClientNetworking.clientPortals.clear();
        TimeMachineScreen.clearStaticCache();
        PortalSkyRenderer.portalTime = 0.0F;
      });
      ClientLifecycleEvents.CLIENT_STOPPING.register((ClientStopping)client -> {});
      ClientTickEvents.END_CLIENT_TICK.register((EndTick)client -> {
        if (client.level != null && client.player != null && !NostalgiaClientNetworking.clientPortals.isEmpty()) {
          if (client.level.getGameTime() % 20 == 0) {
            NostalgiaClientNetworking.updateNearestPortalRenderer();
          }
        }

        if (client.level != null && client.player != null && NostalgiaNetworking.pendingZoneStart != null) {
          S2CTimestopZoneStartPayload payload = NostalgiaNetworking.pendingZoneStart;
          NostalgiaNetworking.pendingZoneStart = null;
          if (client.level.tickRateManager() instanceof TickRateManagerAccess access) {
            ResourceKey<Level> dim = client.level.dimension();
            if (dim.identifier().toString().equals(payload.dimensionId())) {
              access.nostalgia$addRegion(new FreezeRegion(dim, payload.beaconPos(), payload.radiusChunks()));
              NostalgiaClientNetworking.nostalgia$markZoneChunksDirty(client, payload.beaconPos(), payload.radiusChunks());
              if (payload.instant()) {
                ClientZoneTime.forceInstantSnapshot(payload.snapGameTime(), payload.snapClockTicks(), payload.snapRain(), payload.snapThunder());
              }
            }
          }
        }

        if (client.level != null) {
          long realClockTicks = ZoneTimeBridge.hasClockReal ? ZoneTimeBridge.lastRealClockTicks : 0L;
          long realGameTime = 0L;

          try {
            realGameTime = client.level.getGameTime();
          } catch (Throwable var23) {
          }

          float realRain = 0.0F;
          float realThunder = 0.0F;

          try {
            if (client.level instanceof LevelRainFieldAccessor acc) {
              realRain = acc.nostalgia$getRainLevelField();
              realThunder = acc.nostalgia$getThunderLevelField();
            }
          } catch (Throwable var22) {
          }

          ClientZoneTime.updateReals(realClockTicks, realGameTime, realRain, realThunder);
          ClientZoneTime.tickCollapse();
        }

        RitualVisualManager.tick();
        PortalSkyRenderer.tickSkyPortalTransition();
        if (client.level != null) {
          TrailManager.tick(client.level.players());
        } else {
          TrailManager.tick(null);
        }

        RitualSoundManager.tick(client.player);
        if (PortalSkyRenderer.active && (!RitualVisualManager.isTransitioning || PortalSkyRenderer.isAnimatingOut || RitualVisualManager.isBystander)) {
          if (PortalSkyRenderer.isAnimatingOut) {
            float oldRadius = RitualVisualManager.getPortalAlphaRadius();
            float oldTime = PortalSkyRenderer.portalTime;
            PortalSkyRenderer.portalTime -= 0.075F;
            float newRadius = RitualVisualManager.getPortalAlphaRadius();
            if (client.levelRenderer != null && client.player != null) {
              BlockPos center = PortalSkyRenderer.portalCenter;
              if (center != null) {
                float minR = Math.max(0.0F, newRadius - 8.0F);
                float maxR = oldRadius + 16.0F;
                int minSecX = (int)Math.floor((center.getX() - maxR) / 16.0);
                int maxSecX = (int)Math.ceil((center.getX() + maxR) / 16.0);
                int minSecZ = (int)Math.floor((center.getZ() - maxR) / 16.0);
                int maxSecZ = (int)Math.ceil((center.getZ() + maxR) / 16.0);
                float minSq = minR * minR;
                float maxSq = maxR * maxR;

                for (int x = minSecX; x <= maxSecX; x++) {
                  for (int z = minSecZ; z <= maxSecZ; z++) {
                    double dx = x * 16 + 8 - center.getX();
                    double dz = z * 16 + 8 - center.getZ();
                    double distSq = dx * dx + dz * dz;
                    if (distSq >= minSq && distSq <= maxSq) {
                      for (int y = 10; y <= 21; y++) {
                        client.levelRenderer.setSectionDirtyWithNeighbors(x, y, z);
                      }
                    }
                  }
                }
              }
            }

            if (oldTime >= 2.85F && PortalSkyRenderer.portalTime < 2.85F) {
              PortalSkyRenderer.islandVisible = false;
              if (client.levelRenderer != null && client.player != null) {
                BlockPos center = PortalSkyRenderer.portalCenter;
                SHAHologramManager.markRadiusShellDirty(center, 0.0F, 320.0F);
              }
            }

            if (PortalSkyRenderer.portalTime <= 0.0F) {
              PortalSkyRenderer.active = false;
              PortalSkyRenderer.isAnimatingOut = false;
              PortalSkyRenderer.portalTime = 0.0F;
              PortalSkyRenderer.islandVisible = false;
              PortalSkyRenderer.capturedProjectionMatrix = null;
              PortalSkyRenderer.capturedModelViewMatrix = null;
              if (ClientRitualEventRegistry.activeTransition() == null && !PortalSkyRenderer.skyPortalTransitioning) {
                UniversalHologramCache.clearMemoryCaches();
                DimensionHologramRegistry.clearAll();
                if (client.levelRenderer != null && client.player != null) {
                  BlockPos center = PortalSkyRenderer.portalCenter;
                  SHAHologramManager.markRadiusShellDirty(center, 0.0F, 320.0F);
                }
              }
            }
          } else {
            PortalSkyRenderer.islandVisible = true;
            PortalSkyRenderer.portalTime += 0.05F;
          }
        }
      });
    }
  }
}
