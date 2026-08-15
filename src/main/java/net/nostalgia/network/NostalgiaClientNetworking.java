package net.nostalgia.network;

import com.example.api.GravityAnomalyZone;
import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import net.nostalgia.client.events.skyportal.ClientSkyPortalData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.Holder.Reference;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.nostalgia.alphalogic.bridge.AlphaEngineManager;
import net.nostalgia.alphalogic.ritual.DimensionUtil;
import net.nostalgia.alphalogic.ritual.FreezeRegion;
import net.nostalgia.alphalogic.ritual.TickRateManagerAccess;
import net.nostalgia.alphalogic.ritual.event.ClientEchoRitualView;
import net.nostalgia.alphalogic.ritual.event.RitualEventRegistry;
import net.nostalgia.client.events.caches.UniversalHologramCache;
import net.nostalgia.client.events.caches.impl.AlphaByteCache;
import net.nostalgia.client.events.caches.providers.DimensionHologramCache;
import net.nostalgia.client.events.caches.providers.DimensionHologramRegistry;
import net.nostalgia.client.events.caches.providers.HologramDiskCache;
import net.nostalgia.client.events.caches.providers.HologramSection;
import net.nostalgia.client.events.core.ClientFreezeRegions;
import net.nostalgia.client.events.core.ClientRitualEventRegistry;
import net.nostalgia.client.events.core.ClientZoneTime;
import net.nostalgia.client.events.core.DebugOwerContext;
import net.nostalgia.client.events.core.IHologramContext;
import net.nostalgia.client.events.echo.RitualVisualManager;
import net.nostalgia.client.gui.TimeMachineScreen;
import net.nostalgia.client.render.GlassBreakRenderer;
import net.nostalgia.client.render.PortalSkyRenderer;
import net.nostalgia.world.dimension.ModDimensions;
import net.sha.api.SHAHologramManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Environment(EnvType.CLIENT)
public class NostalgiaClientNetworking {
  private static final Logger LOGGER = LoggerFactory.getLogger("NostalgiaCache");
  public static final ConcurrentHashMap<UUID, ClientSkyPortalData> clientPortals = new ConcurrentHashMap<>();
  private static UUID lastRenderedPortalId = null;

  public static void updateNearestPortalRenderer() {
    if (RitualVisualManager.isTransitioning) {
      return;
    }
    Minecraft mc = Minecraft.getInstance();
    if (mc == null || mc.player == null) {
      if (clientPortals.isEmpty()) {
        PortalSkyRenderer.active = false;
        PortalSkyRenderer.startCloseAnimation();
        GravityAnomalyZone.clear();
        lastRenderedPortalId = null;
      }
      return;
    }
    String playerDim = mc.player.level().dimension().identifier().toString();
    BlockPos playerPos = mc.player.blockPosition();
    ClientSkyPortalData nearest = null;
    double minRemainingSq = 400.0 * 400.0;
    for (ClientSkyPortalData portal : clientPortals.values()) {
      if (playerDim.equals(portal.sourceDimension()) || playerDim.equals(portal.targetDimension())) {
        double distSq = portal.center().distSqr(playerPos);
        if (distSq < minRemainingSq) {
          minRemainingSq = distSq;
          nearest = portal;
        }
      }
    }
    if (nearest != null) {
      PortalSkyRenderer.active = true;
      PortalSkyRenderer.isAnimatingOut = false;
      PortalSkyRenderer.inverted = nearest.inverted();
      PortalSkyRenderer.portalCenter = nearest.center();
      PortalSkyRenderer.crackPlaneY = nearest.crackPlaneY();
      PortalSkyRenderer.crackPlaneYTarget = nearest.crackPlaneYTarget();
      int activeCrackY = playerDim.equals(nearest.targetDimension()) ? nearest.crackPlaneYTarget() : nearest.crackPlaneY();
      GravityAnomalyZone.set(activeCrackY, activeCrackY + 5.0, nearest.center().getX(), nearest.center().getZ(), 300.0);
      PortalSkyRenderer.originalSourceDimension = nearest.sourceDimension();
      PortalSkyRenderer.originalTargetDimension = nearest.targetDimension();
      String renderDim = playerDim.equals(nearest.targetDimension()) ? nearest.sourceDimension() : nearest.targetDimension();
      PortalSkyRenderer.targetDimension = renderDim;
      PortalSkyRenderer.islandVisible = true;
      boolean portalChanged = !nearest.id().equals(lastRenderedPortalId);
      lastRenderedPortalId = nearest.id();
      if (portalChanged) {
        ensureDimensionCacheLoaded(renderDim, nearest.center(), nearest.seed());
        SHAHologramManager.updateSpatialMap(UniversalHologramCache.INSTANCE);
        SHAHologramManager.markRadiusShellDirty(nearest.center(), 0.0F, 320.0F);
      }
    } else {
      if (PortalSkyRenderer.active) {
        PortalSkyRenderer.startCloseAnimation();
        if (PortalSkyRenderer.portalCenter != null) {
          SHAHologramManager.markRadiusShellDirty(PortalSkyRenderer.portalCenter, 0.0F, 320.0F);
        }
      }
      GravityAnomalyZone.clear();
      lastRenderedPortalId = null;
    }
  }

  public NostalgiaClientNetworking() {
  }

  public static void nostalgia$markZoneChunksDirty(Minecraft mc, BlockPos beaconPos, int chunkRadius) {
    if (mc != null && mc.levelRenderer != null && beaconPos != null) {
      int blockRadius = (chunkRadius + 1) * 16;
      int minX = beaconPos.getX() - blockRadius;
      int maxX = beaconPos.getX() + blockRadius;
      int minZ = beaconPos.getZ() - blockRadius;
      int maxZ = beaconPos.getZ() + blockRadius;
      int minY = mc.level != null ? mc.level.getMinY() : -64;
      int maxY = mc.level != null ? mc.level.getMaxY() : 320;
      mc.levelRenderer.setBlocksDirty(minX, minY, minZ, maxX, maxY, maxZ);
    }
  }

  public static void registerClientReceivers() {
    ClientPlayNetworking.registerGlobalReceiver(
      S2CWorldSeedPayload.TYPE, (payload, context) -> context.client().execute(() -> AlphaEngineManager.setWorldSeed(payload.seed()))
    );
    ClientPlayNetworking.registerGlobalReceiver(
      S2CSetTerminalErrorPayload.TYPE, (payload, context) -> context.client().execute(() -> TimeMachineScreen.nextScreenIsError = true)
    );
    ClientPlayNetworking.registerGlobalReceiver(S2CSyncAlphaDeltasPayload.TYPE, (payload, context) -> {
      if (payload.positions() != null && payload.states() != null) {
        if (payload.positions().length == payload.states().length) {
          context.client().execute(() -> {
            String dimId = payload.dimensionId();
            DimensionHologramCache cache = DimensionHologramRegistry.getByName(dimId);
            if (cache != null) {
              for (int i = 0; i < payload.positions().length; i++) {
                cache.setOverrideRaw(payload.positions()[i], Block.stateById(payload.states()[i]));
              }
            }

            for (long posAsLong : payload.positions()) {
              BlockPos pos = BlockPos.of(posAsLong);
              SHAHologramManager.markAreaDirty(pos.getX(), pos.getY(), pos.getZ(), pos.getX(), pos.getY(), pos.getZ());

              for (IHologramContext ctx : UniversalHologramCache.ACTIVE_CONTEXTS) {
                if (ctx.isActive() && dimId.equals(ctx.getTargetDimension())) {
                  int worldX;
                  int worldY;
                  int worldZ;
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

                  SHAHologramManager.markAreaDirty(worldX, worldY, worldZ, worldX, worldY, worldZ);
                }
              }
            }
          });
        }
      }
    });
    ClientPlayNetworking.registerGlobalReceiver(S2CDebugOwerPayload.TYPE, (payload, context) -> context.client().execute(() -> {
      if (payload.active()) {
        DebugOwerContext.INSTANCE.setActive(true, payload.center());
        UniversalHologramCache.overworldCacheReady = false;
      } else {
        DebugOwerContext.INSTANCE.setActive(false, null);
        UniversalHologramCache.clearMemoryCaches();
        SHAHologramManager.markRadiusShellDirty(payload.center(), 0.0F, 320.0F);
      }
    }));
    ClientPlayNetworking.registerGlobalReceiver(
      S2CDimensionSectionsPayload.TYPE,
      (payload, context) -> {
        Long2ObjectOpenHashMap<HologramSection> map = new Long2ObjectOpenHashMap();
        Registry<Biome> biomeRegistry = context.client().level.registryAccess().lookupOrThrow(Registries.BIOME);

        for (S2CDimensionSectionsPayload.SectionData sd : payload.sections()) {
          BlockState[] palette = new BlockState[sd.paletteIds().length];

          for (int i = 0; i < sd.paletteIds().length; i++) {
            palette[i] = Block.stateById(sd.paletteIds()[i]);
          }

          Holder<Biome>[] biomePalette = null;
          if (sd.biomePaletteIds() != null && sd.biomePaletteIds().length > 0) {
            biomePalette = new Holder[sd.biomePaletteIds().length];

            for (int i = 0; i < sd.biomePaletteIds().length; i++) {
              biomePalette[i] = (Holder<Biome>)biomeRegistry.get(sd.biomePaletteIds()[i]).orElse((Reference)biomeRegistry.getAny().get());
            }
          }

          HologramSection section = new HologramSection(palette, sd.indices(), biomePalette, sd.biomeIndices());
          long key = (sd.chunkX() & 4194303L) << 42 | (sd.sectionY() & 1048575L) << 22 | sd.chunkZ() & 4194303L;
          map.put(key, section);
        }

        DimensionHologramCache cache = DimensionHologramRegistry.getByName(payload.dimensionId());
        if (cache != null) {
          cache.putSections(map);
          if (payload.chunkPositions() != null && payload.chunkVersions() != null) {
            Long2LongOpenHashMap versions = cache.getChunkVersions();

            for (int i = 0; i < payload.chunkPositions().length; i++) {
              versions.put(payload.chunkPositions()[i], payload.chunkVersions()[i]);
            }
          }

          Minecraft.getInstance().execute(() -> {
            if (!DimensionUtil.isClientGenerated(payload.dimensionId())) {
              UniversalHologramCache.overworldCacheReady = true;
            }
          });
        }
      }
    );
    ClientPlayNetworking.registerGlobalReceiver(S2CHologramReadyPayload.TYPE, (payload, context) -> context.client().execute(() -> {
      UniversalHologramCache.overworldCacheReady = true;
      SHAHologramManager.updateSpatialMap(UniversalHologramCache.INSTANCE);
      TimeMachineScreen.terminalCacheArrived = true;
      DimensionHologramCache cache = DimensionHologramRegistry.getByName(payload.dimensionId());
      if (cache != null) {
        Long2ObjectOpenHashMap<HologramSection> copyForDisk = cache.getSections().clone();
        Long2LongOpenHashMap versionsForDisk = cache.getChunkVersions().clone();
        CompletableFuture.runAsync(() -> HologramDiskCache.saveDimensionCache(payload.dimensionId(), copyForDisk, versionsForDisk));
      }

      if (context.player() != null && context.player().level() != null) {
        ResourceKey<Level> dim = context.player().level().dimension();
        boolean isAlpha = dim == ModDimensions.ALPHA_112_01_LEVEL_KEY || dim == ModDimensions.RD_132211_LEVEL_KEY;
        ClientEchoRitualView t = ClientRitualEventRegistry.activeTransition();
        boolean isTransition = t != null && !t.isBystander();
        if (isTransition) {
          SHAHologramManager.markRadiusShellDirty(payload.center(), 0.0F, payload.radius());
        } else if (!isAlpha) {
          int cx = payload.center().getX();
          int cz = payload.center().getZ();
          int r = payload.radius();
          SHAHologramManager.markAreaDirty(cx - r, 150, cz - r, cx + r, 320, cz + r);
        }
      }
    }));
    ClientPlayNetworking.registerGlobalReceiver(
      S2CStartTransitionVisualsPayload.TYPE,
      (payload, context) -> context.client()
        .execute(
          () -> {
            AlphaEngineManager.setWorldSeed(payload.seed());
            if (RitualEventRegistry.activeInstance() == null) {
              RitualEventRegistry.startEvent(payload.beaconPos(), null);
            }

            RitualVisualManager.targetSkyColor = payload.targetSkyColor();
            RitualVisualManager.targetFogColor = payload.targetFogColor();
            RitualEventRegistry.setOffsets(payload.offsetX(), payload.offsetY(), payload.offsetZ());
            RitualVisualManager.startTransition(
              payload.instanceId(), payload.beaconPos(), payload.dimensionId(), payload.safeSpawnPos(), payload.beaconStateId(), payload.anchorStateId()
            );
          }
        )
    );
    ClientPlayNetworking.registerGlobalReceiver(S2CEndTransitionVisualsPayload.TYPE, (payload, context) -> context.client().execute(() -> {
      UUID myId = RitualVisualManager.myInstanceId;
      if (myId == null || myId.equals(payload.instanceId())) {
        ClientEchoRitualView t = ClientRitualEventRegistry.activeTransition();
        if (t != null && t.isBystander()) {
          RitualVisualManager.endTransition();
        } else {
          RitualVisualManager.onDimensionChanged();
        }
      }
    }));
    ClientPlayNetworking.registerGlobalReceiver(
      S2CSyncParticipantsPayload.TYPE, (payload, context) -> context.client().execute(() -> RitualEventRegistry.setParticipants(payload.participants()))
    );
    ClientPlayNetworking.registerGlobalReceiver(S2CRitualPhasePayload.TYPE, (payload, context) -> context.client().execute(() -> {
      UUID myId = RitualVisualManager.myInstanceId;
      if (myId == null || myId.equals(payload.instanceId())) {
        RitualVisualManager.setPhase(payload.phase());
      }
    }));
    ClientPlayNetworking.registerGlobalReceiver(
      S2CBystanderVisualsPayload.TYPE,
      (payload, context) -> context.client()
        .execute(
          () -> {
            UUID myId = RitualVisualManager.myInstanceId;
            if (myId == null || myId.equals(payload.instanceId())) {
              if (RitualEventRegistry.activeInstance() == null) {
                RitualEventRegistry.startEvent(payload.center(), null);
              }

              RitualVisualManager.triggerBystanderVisuals(
                payload.center(), payload.offsetX(), payload.offsetY(), payload.offsetZ(), payload.targetDimensionId(), payload.phase()
              );
            }
          }
        )
    );
    ClientPlayNetworking.registerGlobalReceiver(S2CGlassBreakPayload.TYPE, (payload, context) -> context.client().execute(() -> {
      if (payload.active()) {
        GlassBreakRenderer.start(payload.anchor());
      } else {
        GlassBreakRenderer.stop();
      }
    }));
    ClientPlayNetworking.registerGlobalReceiver(
      S2CTimestopZoneStartPayload.TYPE,
      (payload, context) -> context.client()
        .execute(
          () -> {
            ResourceKey<Level> dimKey = ResourceKey.create(Registries.DIMENSION, Identifier.tryParse(payload.dimensionId()));
            RitualEventRegistry.registerZoneLocal(
              dimKey, payload.beaconPos(), payload.radiusChunks(), payload.snapGameTime(), payload.snapClockTicks(), payload.snapRain(), payload.snapThunder()
            );
            ClientFreezeRegions.snapshots
              .put(
                payload.beaconPos(),
                new ClientFreezeRegions.ZoneSnapshot(payload.snapGameTime(), payload.snapClockTicks(), payload.snapRain(), payload.snapThunder())
              );
            Minecraft mc = Minecraft.getInstance();
            if (mc.level == null) {
              NostalgiaNetworking.pendingZoneStart = payload;
            } else {
              if (mc.level.tickRateManager() instanceof TickRateManagerAccess access) {
                ResourceKey<Level> dim = mc.level.dimension();
                if (dim.identifier().toString().equals(payload.dimensionId())) {
                  access.nostalgia$addRegion(new FreezeRegion(dim, payload.beaconPos(), payload.radiusChunks()));
                  nostalgia$markZoneChunksDirty(mc, payload.beaconPos(), payload.radiusChunks());
                  if (payload.instant()) {
                    ClientZoneTime.forceInstantSnapshot(payload.snapGameTime(), payload.snapClockTicks(), payload.snapRain(), payload.snapThunder());
                  }
                }
              }
            }
          }
        )
    );
    ClientPlayNetworking.registerGlobalReceiver(S2CTimestopZoneEndPayload.TYPE, (payload, context) -> context.client().execute(() -> {
      RitualEventRegistry.unregisterZoneByBeacon(payload.beaconPos());
      Minecraft mc = Minecraft.getInstance();
      ClientFreezeRegions.snapshots.remove(payload.beaconPos());
      if (mc.level != null && mc.level.tickRateManager() instanceof TickRateManagerAccess access) {
        nostalgia$markZoneChunksDirty(mc, payload.beaconPos(), 5);
        access.nostalgia$removeRegionAt(mc.level.dimension(), payload.beaconPos());
      }
    }));
    ClientPlayNetworking.registerGlobalReceiver(S2CSkyPortalCancelPayload.TYPE, (payload, context) -> context.client().execute(() -> {
      if (context.player() != null) {
        context.player().level().playSound(context.player(), context.player().blockPosition(), SoundEvents.BEACON_DEACTIVATE, SoundSource.MASTER, 1.0F, 0.5F);
      }
    }));
    ClientPlayNetworking.registerGlobalReceiver(
      S2CZoneCollapsePayload.ID, (payload, context) -> context.client().execute(() -> ClientZoneTime.startZoneCollapse(payload.collapseDurationMs()))
    );
    ClientPlayNetworking.registerGlobalReceiver(
      S2CSkyPortalPayload.TYPE,
      (payload, context) -> context.client()
        .execute(
          () -> {

            if (payload.state()) {
              org.slf4j.LoggerFactory.getLogger("SkyPortalEvent").info("[SKYPORTAL-EVENT] Client RECEIVED OPEN portal id={}, center={}, targetDim={}", payload.portalId(), payload.center(), payload.targetDimension());
              ClientSkyPortalData data = new ClientSkyPortalData(
                payload.portalId(),
                payload.center(),
                payload.crackPlaneY(),
                payload.crackPlaneYTarget(),
                payload.inverted(),
                payload.seed(),
                payload.sourceDimension(),
                payload.targetDimension()
              );
              clientPortals.put(payload.portalId(), data);
              updateNearestPortalRenderer();
              if (payload.restored()) {
                PortalSkyRenderer.portalTime = 10.0F;
                RitualVisualManager.soundPhase1Played = true;
                RitualVisualManager.soundPhase2Played = true;
                RitualVisualManager.soundPhase3Played = true;
              } else {
                PortalSkyRenderer.portalTime = 0.0F;
                RitualVisualManager.soundPhase1Played = false;
                RitualVisualManager.soundPhase2Played = false;
                RitualVisualManager.soundPhase3Played = false;
              }

              if (context.player() != null) {
                String targetDim = payload.targetDimension();
                String sourceDim = payload.sourceDimension();
                String renderDim = targetDim;
                if (context.player().level().dimension().identifier().toString().equals(targetDim)) {
                  renderDim = sourceDim;
                }

                BlockPos readCenter = payload.center();
                if (!RitualVisualManager.isTransitioning) {
                  RitualEventRegistry.setOffsets(0, 0, 0);
                }

                RitualVisualManager.portalMirageEntity = null;
                if (!RitualVisualManager.isTransitioning) {
                  ensureDimensionCacheLoaded(renderDim, readCenter, payload.seed());
                }
              }
            } else {
              org.slf4j.LoggerFactory.getLogger("SkyPortalEvent").info("[SKYPORTAL-EVENT] Client RECEIVED CLOSE portal id={}", payload.portalId());
              ClientSkyPortalData removed = clientPortals.remove(payload.portalId());
              if (removed != null && removed.id().equals(lastRenderedPortalId)) {
                if (PortalSkyRenderer.active) {
                  PortalSkyRenderer.startCloseAnimation();
                  if (PortalSkyRenderer.portalCenter != null) {
                    SHAHologramManager.markRadiusShellDirty(PortalSkyRenderer.portalCenter, 0.0F, 320.0F);
                  }
                }
              }
              if (clientPortals.isEmpty()) {
                PortalSkyRenderer.inverted = false;
                net.sha.api.SHAMirageManager.flipY = false;
              }
              updateNearestPortalRenderer();
            }
          }
        )
    );
  }

  public static void ensureDimensionCacheLoaded(String renderDim, BlockPos center, long seed) {
    Minecraft mc = Minecraft.getInstance();
    if (mc.player == null) return;
    LOGGER.info("[CACHE-DIAG] ensureDimensionCacheLoaded called: renderDim={}, center={}, seed={}, playerDim={}", renderDim, center, seed, mc.player.level().dimension().identifier().toString());
    if (!mc.player.level().dimension().identifier().toString().equals(renderDim)) {
      if (DimensionUtil.isClientGenerated(renderDim)) {
        AlphaByteCache.generateCache(center, seed, renderDim);
      } else {
        CompletableFuture.runAsync(() -> {
          HologramDiskCache.DimensionCacheResult diskResult = HologramDiskCache.loadDimensionCache(renderDim);
          if (diskResult != null && diskResult.sections() != null && !diskResult.sections().isEmpty()) {
            Minecraft.getInstance().execute(() -> {
              DimensionHologramCache cache = DimensionHologramRegistry.getByName(renderDim);
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
              SHAHologramManager.updateSpatialMap(UniversalHologramCache.INSTANCE);
              SHAHologramManager.markRadiusShellDirty(center, 0.0F, 320.0F);
              DimensionHologramCache readyCache = DimensionHologramRegistry.getByName(renderDim);
              if (readyCache != null) {
                Long2LongOpenHashMap cv = readyCache.getChunkVersions();
                long[] cks = cv.keySet().toLongArray();
                long[] vrs = new long[cks.length];
                for (int i = 0; i < cks.length; i++) vrs[i] = cv.get(cks[i]);
                ClientPlayNetworking.send(new C2SCacheReadyPayload(true, cks, vrs));
              } else {
                ClientPlayNetworking.send(new C2SCacheReadyPayload(true, new long[0], new long[0]));
              }
            });
          } else {
            Minecraft.getInstance().execute(() -> ClientPlayNetworking.send(new C2SCacheReadyPayload(false, new long[0], new long[0])));
          }
        });
      }
    }
  }
}
