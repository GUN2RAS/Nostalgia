package net.nostalgia.client.events.echo;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.nostalgia.client.events.caches.providers.HologramSection;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.nostalgia.alphalogic.bridge.AlphaEngineManager;
import net.nostalgia.alphalogic.ritual.DimensionUtil;
import net.nostalgia.alphalogic.ritual.event.RitualEventRegistry;
import net.nostalgia.client.NostalgiaConfig;
import net.nostalgia.client.events.caches.UniversalHologramCache;
import net.nostalgia.client.events.caches.impl.AlphaByteCache;
import net.nostalgia.client.events.caches.providers.DimensionHologramCache;
import net.nostalgia.client.events.caches.providers.DimensionHologramRegistry;
import net.nostalgia.client.events.caches.providers.HologramDiskCache;
import net.nostalgia.client.render.PortalSkyRenderer;
import net.nostalgia.network.C2SCacheReadyPayload;
import net.nostalgia.network.C2SReportHologramSurfacePayload;
import net.nostalgia.network.C2SRequestPortalDeltasPayload;
import net.nostalgia.world.dimension.ModDimensions;
import net.nostalgia.network.NostalgiaClientNetworking;
import net.sha.api.SHAHologramManager;
import net.sha.api.SHAMirageManager;
import net.sha.api.entity.VirtualStructureEntity;

@Environment(EnvType.CLIENT)
public class RitualVisualManager {
  public static volatile UUID myInstanceId = null;
  public static volatile boolean isTransitioning = false;
  public static long suppressZoneAudioUntil = 0L;
  public static long transitionStartTime = 0L;
  private static volatile boolean inNewDimension = false;
  private static long arrivalTime = 0L;
  public static boolean waitingForChunks = false;
  public static volatile BlockPos ritualCenter;
  public static float lastMarkedPortalRadius = -1.0F;
  public static float lastMarkedTransitionRadius = -1.0F;
  public static volatile String targetDimension = null;
  public static volatile BlockPos targetBeaconPos = null;
  public static volatile int yOffset = 0;
  public static volatile int offsetX = 0;
  public static volatile int offsetZ = 0;
  public static int lastReportedSurfaceY = -1;
  public static int targetSkyColor = -1;
  public static int targetFogColor = -1;
  public static volatile int currentPhase = 0;
  public static long phase2StartTime = 0L;
  public static long phase3StartTime = 0L;
  public static volatile boolean isBystander = false;
  private static float lastMarkedRadius = -1.0F;
  private static long dimensionChangeTime = 0L;
  private static long pauseOffset = 0L;
  private static long lastRealTime = System.currentTimeMillis();
  public static boolean soundPhase1Played = false;
  public static boolean soundPhase2Played = false;
  public static boolean soundPhase3Played = false;
  private static boolean wasPaused = false;
  public static VirtualStructureEntity portalMirageEntity = null;
  private static double visualTime = -1.0;
  private static double visualTimeOffset = 0.0;
  private static long lastFrameTime = 0L;
  private static boolean inertiaHooked = false;
  private static long totalInertiaDistance = 0L;
  private static long inertiaDecelStart = 0L;
  private static double inertiaStartVisualTime = 0.0;
  public static double cloudVisualTimeOffset = 0.0;
  private static long lastCloudOriginalTime = -1L;
  private static double globalCloudOffset = 0.0;

  public RitualVisualManager() {
  }

  public static long getVisualTime() {
    long now = System.currentTimeMillis();
    Minecraft mc = Minecraft.getInstance();
    boolean isPaused = mc.isPaused();
    if (isPaused) {
      if (!wasPaused) {
        wasPaused = true;
        lastRealTime = now;
      }

      return lastRealTime - pauseOffset;
    } else {
      if (wasPaused) {
        wasPaused = false;
        pauseOffset = pauseOffset + (now - lastRealTime);
      }

      return now - pauseOffset;
    }
  }

  public static Iterable<VoxelShape> getExtraCollisions(Entity entity, AABB aabb) {
    return null;
  }

  public static void startTransition(UUID instanceId, BlockPos pos, String dimensionId, BlockPos safePos, int beaconStateId, int anchorStateId) {
    visualTime = -1.0;
    inertiaHooked = false;
    myInstanceId = instanceId;
    isTransitioning = true;
    inNewDimension = false;
    waitingForChunks = false;
    isBystander = false;
    lastReportedSurfaceY = -1;
    soundPhase1Played = false;
    soundPhase2Played = false;
    soundPhase3Played = false;
    if (PortalSkyRenderer.active) {
      PortalSkyRenderer.startCloseAnimation();
    }

    Minecraft mc = Minecraft.getInstance();
    boolean isLeavingRD = mc.level != null && mc.level.dimension().equals(ModDimensions.RD_132211_LEVEL_KEY);
    if (isLeavingRD) {
      currentPhase = 2;
      phase2StartTime = getVisualTime();
      phase3StartTime = 0L;
    } else {
      currentPhase = 1;
      phase2StartTime = 0L;
      phase3StartTime = 0L;
    }

    transitionStartTime = getVisualTime();
    ritualCenter = pos;
    targetDimension = dimensionId;
    lastMarkedPortalRadius = PortalSkyRenderer.active ? getPortalAlphaRadius() : -1.0F;
    lastMarkedTransitionRadius = -1.0F;
    RitualEventRegistry.setRitualCenter(pos);
    RitualEventRegistry.setTransitioning(true);
    SHAMirageManager.beginHandoff(60, RitualEventRegistry.offsetX(), RitualEventRegistry.yOffset(), RitualEventRegistry.offsetZ());
    int tX = pos.getX() + RitualEventRegistry.offsetX();
    int tY = pos.getY() - RitualEventRegistry.yOffset();
    int tZ = pos.getZ() + RitualEventRegistry.offsetZ();
    targetBeaconPos = new BlockPos(tX, tY, tZ);
    long seed = AlphaEngineManager.getWorldSeed();
    DimensionHologramRegistry.clearAllOverrides();
    if (beaconStateId > 0) {
      DimensionHologramCache cache = DimensionHologramRegistry.getByName(dimensionId);
      if (cache != null) {
        cache.setRitualOverride(targetBeaconPos.asLong(), Block.stateById(beaconStateId));
        cache.setRitualOverride(targetBeaconPos.below().asLong(), Block.stateById(anchorStateId));
      }
    }

    if (DimensionUtil.isClientGenerated(dimensionId)) {
      AlphaByteCache.generateCache(targetBeaconPos, seed, dimensionId);
      SHAHologramManager.updateSpatialMap(UniversalHologramCache.INSTANCE);
    } else if (dimensionId != null) {
      CompletableFuture.runAsync(() -> {
        HologramDiskCache.DimensionCacheResult diskResult = HologramDiskCache.loadDimensionCache(dimensionId);
        Minecraft.getInstance().execute(() -> {
          if (diskResult != null && diskResult.sections() != null && !diskResult.sections().isEmpty()) {
            DimensionHologramCache cache = DimensionHologramRegistry.getByName(dimensionId);
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
            UniversalHologramCache.cacheGenerated = true;
            DimensionHologramCache owCache = DimensionHologramRegistry.getByName(dimensionId);
            if (owCache != null) {
              Long2LongOpenHashMap cv = owCache.getChunkVersions();
              long[] cks = cv.keySet().toLongArray();
              long[] vrs = new long[cks.length];
              for (int i = 0; i < cks.length; i++) vrs[i] = cv.get(cks[i]);
              ClientPlayNetworking.send(new C2SCacheReadyPayload(true, cks, vrs));
            } else {
              ClientPlayNetworking.send(new C2SCacheReadyPayload(true, new long[0], new long[0]));
            }
          } else {
            UniversalHologramCache.cacheGenerated = false;
            DimensionHologramRegistry.getByName(dimensionId);
            ClientPlayNetworking.send(new C2SCacheReadyPayload(false, new long[0], new long[0]));
          }

          SHAHologramManager.updateSpatialMap(UniversalHologramCache.INSTANCE);
        });
      });
    } else {
      AlphaByteCache.generateCache(targetBeaconPos, seed, dimensionId);
      SHAHologramManager.updateSpatialMap(UniversalHologramCache.INSTANCE);
    }

    if (beaconStateId > 0) {
      SHAHologramManager.markAreaDirty(pos.getX(), pos.below().getY(), pos.getZ(), pos.getX(), pos.getY(), pos.getZ());
    }
  }

  public static void setPhase(int newPhase) {
    if (isTransitioning) {
      if (currentPhase != newPhase) {
        currentPhase = newPhase;
        if (currentPhase == 2) {
          phase2StartTime = getVisualTime();
          SHAHologramManager.updateSpatialMap(UniversalHologramCache.INSTANCE);
        } else if (currentPhase == 3) {
          phase3StartTime = getVisualTime();
          lastMarkedRadius = -1.0F;
          lastMarkedTransitionRadius = -1.0F;
        }
      }
    }
  }

  public static void triggerBystanderVisuals(BlockPos pos) {
    visualTime = -1.0;
    inertiaHooked = false;
    isTransitioning = true;
    inNewDimension = false;
    waitingForChunks = false;
    isBystander = true;
    ritualCenter = pos;
    currentPhase = 2;
    phase2StartTime = getVisualTime();
    phase3StartTime = 0L;
    transitionStartTime = getVisualTime();
    Minecraft mc = Minecraft.getInstance();
    if (mc.levelRenderer != null && ritualCenter != null) {
      mc.levelRenderer.setSectionDirtyWithNeighbors(ritualCenter.getX() >> 4, ritualCenter.getY() >> 4, ritualCenter.getZ() >> 4);
    }
  }

  public static void triggerBystanderVisuals(BlockPos center, int offsetX, int offsetY, int offsetZ, String targetDimensionId, int phase) {
    visualTime = -1.0;
    inertiaHooked = false;
    ritualCenter = center;
    RitualEventRegistry.setOffsets(offsetX, offsetY, offsetZ);
    targetDimension = targetDimensionId;
    currentPhase = 2;
    RitualEventRegistry.setTransitioning(true);
    isTransitioning = true;
    inNewDimension = false;
    waitingForChunks = false;
    isBystander = true;
    phase2StartTime = getVisualTime();
    phase3StartTime = 0L;
    transitionStartTime = getVisualTime();
    Minecraft mc = Minecraft.getInstance();
    if (mc.levelRenderer != null && ritualCenter != null) {
      mc.levelRenderer.setSectionDirtyWithNeighbors(ritualCenter.getX() >> 4, ritualCenter.getY() >> 4, ritualCenter.getZ() >> 4);
    }
  }

  public static void endTransition() {
    if (isTransitioning) {
      if (SHAMirageManager.isTransitioning) {
        SHAMirageManager.endTransition();
      }

      globalCloudOffset = globalCloudOffset + getVisualTimeOffsetExact();
      int r = (int)getAlphaRadius();
      int minX = ritualCenter.getX() - r - 16;
      int maxX = ritualCenter.getX() + r + 16;
      int minZ = ritualCenter.getZ() - r - 16;
      int maxZ = ritualCenter.getZ() + r + 16;
      boolean portalStillActive = PortalSkyRenderer.active;
      isTransitioning = false;
      inNewDimension = false;
      currentPhase = 0;
      myInstanceId = null;
      Minecraft mc = Minecraft.getInstance();
      if (mc.levelRenderer != null && ritualCenter != null) {
        mc.levelRenderer.setSectionDirtyWithNeighbors(ritualCenter.getX() >> 4, ritualCenter.getY() >> 4, ritualCenter.getZ() >> 4);
      }

      DimensionHologramRegistry.clearAllOverrides();
      RitualEventRegistry.setOffsets(0, 0, 0);
      RitualEventRegistry.endEvent();
      RitualEventRegistry.setTransitioning(false);
      targetDimension = null;
      targetBeaconPos = null;
      lastReportedSurfaceY = -1;
      if (portalMirageEntity != null) {
        portalMirageEntity.discard();
        portalMirageEntity = null;
      }

      NostalgiaClientNetworking.updateNearestPortalRenderer();
      if (Minecraft.getInstance().getConnection() != null) {
        ClientPlayNetworking.send(new C2SRequestPortalDeltasPayload());
      }
    }
  }

  public static void onDimensionChanged() {
    if (isTransitioning && !inNewDimension) {
      inNewDimension = true;
      waitingForChunks = true;
      dimensionChangeTime = getVisualTime();
      lastMarkedTransitionRadius = 300.0F;
      SHAHologramManager.updateSpatialMap(UniversalHologramCache.INSTANCE);
      if (UniversalHologramCache.cacheGenerated) {
      }
    }
  }

  public static boolean isInNewDimension() {
    return inNewDimension;
  }

  public static void tick() {
    Minecraft sndClient = Minecraft.getInstance();
    boolean isPortalActive = PortalSkyRenderer.active;
    boolean portalOnly = isPortalActive && (!isTransitioning || isBystander);
    float tSec = portalOnly ? PortalSkyRenderer.portalTime : getTransitionTimeSeconds();
    if (portalOnly) {
      if (tSec >= 0.0F && tSec < 0.1F && !soundPhase1Played) {
        soundPhase1Played = true;
        if (sndClient.player != null) {
          sndClient.player.playSound((SoundEvent)SoundEvents.AMBIENT_BASALT_DELTAS_MOOD.value(), 10.0F, 0.7F);
          sndClient.player.playSound(SoundEvents.WARDEN_HEARTBEAT, 10.0F, 0.5F);
          sndClient.player.playSound(SoundEvents.END_PORTAL_FRAME_FILL, 8.0F, 0.6F);
        }
      }

      if (tSec >= 0.6F && !soundPhase2Played) {
        soundPhase2Played = true;
        if (sndClient.player != null) {
          sndClient.player.playSound(SoundEvents.WARDEN_SONIC_BOOM, 15.0F, 0.9F);
          sndClient.player.playSound(SoundEvents.CONDUIT_ACTIVATE, 10.0F, 0.6F);
          sndClient.player.playSound((SoundEvent)SoundEvents.TRIDENT_THUNDER.value(), 8.0F, 1.2F);
        }
      }

      if (tSec >= 1.5F && !soundPhase3Played) {
        soundPhase3Played = true;
        if (sndClient.player != null) {
          sndClient.player.playSound(SoundEvents.END_PORTAL_SPAWN, 20.0F, 0.8F);
          sndClient.player.playSound(SoundEvents.GLASS_BREAK, 15.0F, 0.5F);
          sndClient.player.playSound(SoundEvents.DRAGON_FIREBALL_EXPLODE, 15.0F, 0.4F);
        }
      }
    }

    if (isTransitioning || isPortalActive) {
      if (isTransitioning && sndClient.player != null) {
        int aX = (int)sndClient.player.getX() + RitualEventRegistry.offsetX();
        int aZ = (int)sndClient.player.getZ() + RitualEventRegistry.offsetZ();
        int surfaceY;
        if (DimensionUtil.isClientGenerated(targetDimension)) {
          surfaceY = AlphaByteCache.getHighestBlockY(aX, aZ);
        } else {
          DimensionHologramCache cache = DimensionHologramRegistry.getByName(targetDimension);
          surfaceY = cache != null ? cache.getHighestBlockY(aX, aZ) : -1;
        }

        if (surfaceY != lastReportedSurfaceY && sndClient.getConnection() != null) {
          ClientPlayNetworking.send(new C2SReportHologramSurfacePayload(surfaceY));
          lastReportedSurfaceY = surfaceY;
        }
      }

      if (!isBystander) {
        if (isPortalActive) {
          float portalR = getPortalAlphaRadius();
          if ((Math.abs(portalR - lastMarkedPortalRadius) > 16.0F || lastMarkedPortalRadius == -1.0F)
            && UniversalHologramCache.hasAnyCacheData()) {
            SHAHologramManager.updateSpatialMap(UniversalHologramCache.INSTANCE);
            float minR = Math.min(portalR, lastMarkedPortalRadius == -1.0F ? 0.0F : lastMarkedPortalRadius);
            float maxR = Math.max(portalR, lastMarkedPortalRadius == -1.0F ? 0.0F : lastMarkedPortalRadius);
            SHAHologramManager.markRadiusShellDirty(PortalSkyRenderer.portalCenter, minR, maxR + 16.0F);
            lastMarkedPortalRadius = portalR;
          }
        }

        if (isTransitioning && !isBystander) {
          float transR = getTransitionAlphaRadius();
          if (sndClient.player != null) {
            double dx = sndClient.player.getX() - ritualCenter.getX();
            double dz = sndClient.player.getZ() - ritualCenter.getZ();
            if (dx * dx + dz * dz <= transR * transR) {
              int aXx = (int)Math.floor(sndClient.player.getX()) + RitualEventRegistry.offsetX();
              int aZx = (int)Math.floor(sndClient.player.getZ()) + RitualEventRegistry.offsetZ();
              int startY = (int)Math.floor(sndClient.player.getY() - RitualEventRegistry.yOffset());
              BlockState feetState = UniversalHologramCache.getBlockState(targetDimension, aXx, startY, aZx, false);
              BlockPos feetPos = new BlockPos(aXx, startY, aZx);
              if (feetState != null && (!feetState.getCollisionShape(sndClient.level, feetPos).isEmpty() || !feetState.getFluidState().isEmpty())) {
                int safeY = UniversalHologramCache.getSafeSurfaceYUpwards(targetDimension, aXx, startY, aZx);
                if (safeY > startY) {
                  double expectedHologramY = safeY + RitualEventRegistry.yOffset();
                  sndClient.player.setPos(sndClient.player.getX(), expectedHologramY, sndClient.player.getZ());
                }
              }
            }
          }

          if (transR > lastMarkedTransitionRadius) {
            if (UniversalHologramCache.hasAnyCacheData()) {
              SHAHologramManager.updateSpatialMap(UniversalHologramCache.INSTANCE);
              float expandedRadius = Math.max(0.0F, lastMarkedTransitionRadius);
              SHAHologramManager.markRadiusShellDirty(ritualCenter, expandedRadius, transR);
              if (Math.random() < 0.4 && sndClient.player != null) {
                sndClient.player.playSound(SoundEvents.CHORUS_FLOWER_GROW, 0.2F, 0.5F + (float)Math.random() * 0.5F);
              }
            }

            lastMarkedTransitionRadius = transR;
          }
        }

        if (inNewDimension) {
          if (waitingForChunks) {
            long timeSinceArrival = getVisualTime() - dimensionChangeTime;
            if (timeSinceArrival < 2000L) {
              return;
            }

            Minecraft client = Minecraft.getInstance();
            boolean chunksReady = false;
            if (client.level != null && client.player != null) {
              int px = client.player.getBlockX() >> 4;
              int pz = client.player.getBlockZ() >> 4;
              int loaded = 0;

              for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                  if (client.level.getChunkSource().hasChunk(px + dx, pz + dz)) {
                    loaded++;
                  }
                }
              }

              chunksReady = loaded >= 5;
            }

            if (chunksReady || timeSinceArrival > 15000L) {
              waitingForChunks = false;
              arrivalTime = getVisualTime();
            }
          } else if (getVisualTime() - arrivalTime > 2000L) {
            endTransition();
          }
        }
      }
    }
  }

  public static void onCacheGenerated() {
    if (isTransitioning || PortalSkyRenderer.active) {
      BlockPos centerP = PortalSkyRenderer.active ? PortalSkyRenderer.portalCenter : ritualCenter;
      if (centerP != null) {
        SHAHologramManager.updateSpatialMap(UniversalHologramCache.INSTANCE);
        lastMarkedPortalRadius = -1.0F;
        lastMarkedTransitionRadius = -1.0F;
      }
    }
  }

  public static float getWhiteoutAlpha() {
    if (!isTransitioning) {
      return 0.0F;
    } else if (NostalgiaConfig.get().ritualType == NostalgiaConfig.RitualType.SEAMLESS_PORTAL) {
      return 1.0F;
    } else if (isBystander) {
      long elapsed = getVisualTime() - phase2StartTime;
      return elapsed > 1000L ? Math.max(0.0F, 1.0F - (float)(elapsed - 1000L) / 500.0F) : 1.0F;
    } else {
      return inNewDimension && !waitingForChunks ? 1.0F - getFadeProgress() : 1.0F;
    }
  }

  public static float getFadeProgress() {
    if (isTransitioning && inNewDimension && !waitingForChunks) {
      long timeSinceChunks = getVisualTime() - arrivalTime;
      long fadeTime = 2000L;
      return timeSinceChunks > fadeTime ? 1.0F : (float)timeSinceChunks / (float)fadeTime;
    } else {
      return 0.0F;
    }
  }

  public static long getVisualTimeOffset() {
    return (long)visualTimeOffset;
  }

  public static double getVisualTimeOffsetExact() {
    return cloudVisualTimeOffset;
  }

  public static double getDynamicCloudOffset(long originalTime, boolean update) {
    if (update) {
      if (lastCloudOriginalTime == -1L) {
        lastCloudOriginalTime = originalTime;
      } else {
        long diff = originalTime - lastCloudOriginalTime;
        if (Math.abs(diff) > 100L) {
          globalCloudOffset += diff;
        }

        lastCloudOriginalTime = originalTime;
      }
    }

    double totalOffset = globalCloudOffset;
    if (isTransitioning) {
      totalOffset += getVisualTimeOffsetExact();
    }

    return totalOffset;
  }

  public static long calculateInertialTime(long trueTime) {
    if (NostalgiaConfig.get().ritualType == NostalgiaConfig.RitualType.SEAMLESS_PORTAL) {
      return trueTime;
    } else {
      long now = getVisualTime();
      if (visualTime == -1.0) {
        visualTime = trueTime;
        visualTimeOffset = 0.0;
        cloudVisualTimeOffset = 0.0;
        lastFrameTime = now;
        return trueTime;
      } else {
        double dt = (now - lastFrameTime) / 1000.0;
        lastFrameTime = now;
        if (dt > 0.1) {
          dt = 0.1;
        }

        long prevVisualTime = (long)visualTime;
        if (inNewDimension && !waitingForChunks) {
          if (!inertiaHooked) {
            long targetTimeIn2Sec = trueTime + 40L;
            double currentVelocity = 2.8 * Math.pow(3.0, 1.8) * 1500.0;
            long desiredDistance = (long)currentVelocity;
            long diff = ((long)visualTime - targetTimeIn2Sec) % 192000L;
            if (diff < 0L) {
              diff += 192000L;
            }

            long K = Math.round((desiredDistance - diff) / 192000.0);
            totalInertiaDistance = diff + 192000L * K;
            inertiaDecelStart = now;
            inertiaStartVisualTime = visualTime;
            inertiaHooked = true;
          }

          long elapsed = now - inertiaDecelStart;
          if (elapsed >= 2000L) {
            visualTimeOffset = visualTimeOffset + (trueTime - visualTime);
            visualTime = trueTime;
            return trueTime;
          }

          double progress = elapsed / 2000.0;
          double easeOut = 1.0 - Math.pow(1.0 - progress, 3.0);
          double newVisual = inertiaStartVisualTime - totalInertiaDistance * easeOut;
          double delta = newVisual - visualTime;
          visualTimeOffset += delta;
          cloudVisualTimeOffset += delta;
          visualTime = newVisual;
        } else {
          long elapsed = now - transitionStartTime;
          double seconds = elapsed / 1000.0;
          double baseSeconds = Math.min(seconds, 3.0);
          double velocity = 2.8 * Math.pow(baseSeconds, 1.8) * 1500.0;
          if (seconds > 3.0) {
            double bonus = Math.min(seconds - 3.0, 5.0) / 5.0;
            velocity *= 1.0 + bonus * 1.7;
          }

          double delta = velocity * dt;
          visualTime -= delta;
          visualTimeOffset -= delta;
          cloudVisualTimeOffset -= delta;
        }

        long prevIndex = Math.floorDiv(prevVisualTime - 6000L, 12000L);
        long currIndex = Math.floorDiv((long)visualTime - 6000L, 12000L);
        if (prevIndex != currIndex) {
          Minecraft client = Minecraft.getInstance();
          boolean isRD = client.level != null && client.level.dimension().equals(ModDimensions.RD_132211_LEVEL_KEY);
          if (!isRD && client.player != null) {
            float velocityMagnitude = (float)Math.abs(prevVisualTime - visualTime) / (float)(dt > 0.0 ? dt : 0.05);
            float pitch = 1.0F + Math.min(velocityMagnitude / 20000.0F, 1.0F);
            client.player.playSound(SoundEvents.AMETHYST_CLUSTER_STEP, 0.4F, pitch);
            client.player.playSound(SoundEvents.ILLUSIONER_MIRROR_MOVE, 0.1F, pitch * 1.5F);
          }
        }

        return (long)visualTime;
      }
    }
  }

  public static float getTransitionTimeSeconds() {
    if (!isTransitioning) {
      return 0.0F;
    } else {
      long elapsed = getVisualTime() - transitionStartTime;
      return (float)(elapsed / 1000.0);
    }
  }

  public static float getWhiteRadius() {
    if (NostalgiaConfig.get().ritualType == NostalgiaConfig.RitualType.SEAMLESS_PORTAL) {
      return 0.0F;
    } else if (isTransitioning && !inNewDimension && currentPhase >= 2) {
      long elapsed = getVisualTime() - phase2StartTime;
      float speedMultiplier = isBystander ? 2.0F : 1.0F;
      return (float)elapsed / 1000.0F * 50.0F * speedMultiplier;
    } else {
      return 0.0F;
    }
  }

  public static float getPortalAlphaRadius() {
    if (!PortalSkyRenderer.active) {
      return 0.0F;
    } else {
      float progress = Math.min(PortalSkyRenderer.portalTime / 1.5F, 1.0F);
      return progress * 256.0F;
    }
  }

  public static float getTransitionAlphaRadius() {
    if (!isTransitioning) {
      return 0.0F;
    } else if (NostalgiaConfig.get().ritualType == NostalgiaConfig.RitualType.SEAMLESS_PORTAL) {
      return 0.0F;
    } else if (currentPhase < 3) {
      return 1.5F;
    } else if (inNewDimension) {
      return 300.0F;
    } else {
      long elapsed = getVisualTime() - phase3StartTime;
      float progress = Math.min((float)elapsed / 1000.0F / 3.0F, 1.0F);
      return 1.5F + progress * 298.5F;
    }
  }

  public static float getAlphaRadius() {
    return Math.max(getPortalAlphaRadius(), getTransitionAlphaRadius());
  }
}
