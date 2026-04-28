package net.nostalgia.client.ritual;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;

@Environment(EnvType.CLIENT)
public class RitualVisualManager {
    public static boolean isTransitioning = false;
    public static long suppressZoneAudioUntil = 0;
    public static long transitionStartTime = 0;
    private static boolean inNewDimension = false;
    private static long arrivalTime = 0;
    public static boolean waitingForChunks = false;
    public static BlockPos ritualCenter;
    public static String targetDimension = "";
    public static int yOffset = 0;
    public static int offsetX = 0;
    public static int offsetZ = 0;
    public static int lastReportedSurfaceY = -1;

    public static int currentPhase = 0;
    public static long phase2StartTime = 0;
    public static long phase3StartTime = 0;
    public static boolean isBystander = false;

    private static float lastMarkedRadius = -1.0f;
    private static long dimensionChangeTime = 0;

    private static long pauseOffset = 0;
    private static long lastRealTime = System.currentTimeMillis();

    public static boolean soundPhase1Played = false;
    public static boolean soundPhase2Played = false;
    public static boolean soundPhase3Played = false;

    private static boolean wasPaused = false;

    public static long getVisualTime() {
        long now = System.currentTimeMillis();
        net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
        boolean isPaused = mc.isPaused() || net.nostalgia.client.ritual.ScreenFreezer.isFrozen;

        if (isPaused) {
            if (!wasPaused) {
                wasPaused = true;
                lastRealTime = now;
            }
        } else {
            if (wasPaused) {
                wasPaused = false;
                pauseOffset += (now - lastRealTime);
            }
        }
        return now - pauseOffset;
    }

    public static Iterable<net.minecraft.world.phys.shapes.VoxelShape> getExtraCollisions(net.minecraft.world.entity.Entity entity, net.minecraft.world.phys.AABB aabb) {
        return null;
    }

    public static void startTransition(BlockPos pos, String dimensionId, BlockPos safePos) {
        visualTime = -1;
        inertiaHooked = false;

        isTransitioning = true;
        inNewDimension = false;
        waitingForChunks = false;
        isBystander = false;
        lastReportedSurfaceY = -1;

        soundPhase1Played = false;
        soundPhase2Played = false;
        soundPhase3Played = false;

        net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
        boolean isLeavingRD = mc.level != null && mc.level.dimension().equals(net.nostalgia.world.dimension.ModDimensions.RD_132211_LEVEL_KEY);

        if (isLeavingRD) {
            currentPhase = 2;
            phase2StartTime = getVisualTime();
            phase3StartTime = 0;
        } else {
            currentPhase = 1;
            phase2StartTime = 0;
            phase3StartTime = 0;
        }
        transitionStartTime = getVisualTime();
        ritualCenter = pos;
        targetDimension = dimensionId;
        lastMarkedRadius = -1.0f;

        net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.setRitualCenter(pos);
        net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.setOffsets(
            safePos.getX() - pos.getX(),
            pos.getY() - safePos.getY() - 1,
            safePos.getZ() - pos.getZ()
        );
        net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.setTransitioning(true);

        net.sha.api.SHAMirageManager.beginHandoff(60,
            net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.offsetX(),
            net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.yOffset(),
            net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.offsetZ()
        );

        long seed = net.nostalgia.alphalogic.bridge.AlphaEngineManager.getWorldSeed();

        if (dimensionId != null && dimensionId.contains("overworld")) {
            net.nostalgia.client.render.NostalgiaChunkCache.cacheGenerated = true;
            net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.send(new net.nostalgia.network.C2SCacheReadyPayload());
        } else {

            net.nostalgia.client.render.NostalgiaChunkCache.generateCache(pos, seed, dimensionId);
        }
    }

    public static void setPhase(int newPhase) {
        if (!isTransitioning) return;
        if (currentPhase == newPhase) return;

        currentPhase = newPhase;
        if (currentPhase == 2) {
            phase2StartTime = getVisualTime();
        } else if (currentPhase == 3) {
            phase3StartTime = getVisualTime();
        }
    }

    public static void triggerBystanderVisuals(BlockPos pos) {
        visualTime = -1;
        inertiaHooked = false;
        isTransitioning = true;
        inNewDimension = false;
        waitingForChunks = false;
        isBystander = true;
        ritualCenter = pos;
        currentPhase = 2;
        phase2StartTime = getVisualTime();
        phase3StartTime = 0;
        transitionStartTime = getVisualTime();
    }

    public static void triggerBystanderVisuals(BlockPos center, int offsetX, int offsetY, int offsetZ, String targetDimensionId, int phase) {
        visualTime = -1;
        inertiaHooked = false;
        ritualCenter = center;
        net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.setOffsets(offsetX, offsetY, offsetZ);
        targetDimension = targetDimensionId;
        
        
        currentPhase = 2;
        net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.setTransitioning(true);
        isTransitioning = true;
        inNewDimension = false;
        waitingForChunks = false;
        isBystander = true;
        phase2StartTime = getVisualTime();
        phase3StartTime = 0;
        transitionStartTime = getVisualTime();
    }

    public static void endTransition() {
        if (!isTransitioning) return;

        if (net.sha.api.SHAMirageManager.isTransitioning) {
            net.sha.api.SHAMirageManager.endTransition();
        }

        globalCloudOffset += getVisualTimeOffsetExact();

        int r = (int) getAlphaRadius();
        int minX = ritualCenter.getX() - r - 16;
        int maxX = ritualCenter.getX() + r + 16;
        int minZ = ritualCenter.getZ() - r - 16;
        int maxZ = ritualCenter.getZ() + r + 16;

        isTransitioning = false;
        inNewDimension = false;
        currentPhase = 0;

        net.nostalgia.client.render.NostalgiaChunkCache.clear();
        net.nostalgia.client.ritual.ClientVirtualBlockCache.clear();
        net.nostalgia.client.render.cache.OverworldHologramCache.clear();

        net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.endEvent();
        net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.setTransitioning(false);
        targetDimension = "";
        lastReportedSurfaceY = -1;

        if (portalMirageEntity != null) {
            portalMirageEntity.discard();
            portalMirageEntity = null;
        }
    }

    public static void onDimensionChanged() {
        if (isTransitioning && !inNewDimension) {
            inNewDimension = true;
            waitingForChunks = true;
            dimensionChangeTime = getVisualTime();
            lastMarkedRadius = 300.0f;

            net.sha.api.SHAHologramManager.updateSpatialMap(net.nostalgia.client.render.NostalgiaChunkCache.INSTANCE);

            if (net.nostalgia.client.render.NostalgiaChunkCache.cacheGenerated) {

            }
        }
    }

    public static boolean isInNewDimension() {
        return inNewDimension;
    }

    public static void tick() {
        net.minecraft.client.Minecraft sndClient = net.minecraft.client.Minecraft.getInstance();
        boolean isDebug = net.nostalgia.client.render.PortalSkyRenderer.isDebugging;

        float tSec = isDebug ? net.nostalgia.client.render.PortalSkyRenderer.debugTime : getTransitionTimeSeconds();

        if (isDebug) {
            if (tSec >= 0.0f && tSec < 0.1f && !soundPhase1Played) {
                soundPhase1Played = true;
                if (sndClient.player != null) {
                    sndClient.player.playSound(net.minecraft.sounds.SoundEvents.AMBIENT_BASALT_DELTAS_MOOD.value(), 10.0f, 0.7f);
                    sndClient.player.playSound(net.minecraft.sounds.SoundEvents.WARDEN_HEARTBEAT, 10.0f, 0.5f);
                    sndClient.player.playSound(net.minecraft.sounds.SoundEvents.END_PORTAL_FRAME_FILL, 8.0f, 0.6f);
                }
            }
            if (tSec >= 0.6f && !soundPhase2Played) {
                soundPhase2Played = true;
                if (sndClient.player != null) {
                    sndClient.player.playSound(net.minecraft.sounds.SoundEvents.WARDEN_SONIC_BOOM, 15.0f, 0.9f);
                    sndClient.player.playSound(net.minecraft.sounds.SoundEvents.CONDUIT_ACTIVATE, 10.0f, 0.6f);
                    sndClient.player.playSound(net.minecraft.sounds.SoundEvents.TRIDENT_THUNDER.value(), 8.0f, 1.2f);
                }
            }
            if (tSec >= 1.5f && !soundPhase3Played) {
                soundPhase3Played = true;
                if (sndClient.player != null) {
                    sndClient.player.playSound(net.minecraft.sounds.SoundEvents.END_PORTAL_SPAWN, 20.0f, 0.8f);
                    sndClient.player.playSound(net.minecraft.sounds.SoundEvents.GLASS_BREAK, 15.0f, 0.5f);
                    sndClient.player.playSound(net.minecraft.sounds.SoundEvents.DRAGON_FIREBALL_EXPLODE, 15.0f, 0.4f);
                }
            }
        }

        if (!isTransitioning && !isDebug) return;

        if (isTransitioning && sndClient.player != null) {
            int aX = (int)sndClient.player.getX() + net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.offsetX();
            int aZ = (int)sndClient.player.getZ() + net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.offsetZ();
            int surfaceY = net.nostalgia.client.render.NostalgiaChunkCache.getHighestBlockY(aX, aZ);
            if (surfaceY != lastReportedSurfaceY && sndClient.getConnection() != null) {
                net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.send(new net.nostalgia.network.C2SReportHologramSurfacePayload(surfaceY));
                lastReportedSurfaceY = surfaceY;
            }
        }

        if (isBystander) {
            
            
            
            return;
        }

        float currentRadius = getAlphaRadius();

        if (currentRadius > lastMarkedRadius) {
            if (net.nostalgia.client.render.NostalgiaChunkCache.cacheGenerated) {
                net.sha.api.SHAHologramManager.updateSpatialMap(net.nostalgia.client.render.NostalgiaChunkCache.INSTANCE);
                float expandedRadius = Math.max(0.0f, lastMarkedRadius);
                BlockPos centerP = isDebug ? net.nostalgia.client.render.PortalSkyRenderer.debugCenter : ritualCenter;
                net.sha.api.SHAHologramManager.markRadiusShellDirty(centerP, expandedRadius, currentRadius);

                if (!isDebug && Math.random() < 0.4) {
                    net.minecraft.client.Minecraft client = net.minecraft.client.Minecraft.getInstance();
                    if (client.player != null) {

                        client.player.playSound(net.minecraft.sounds.SoundEvents.CHORUS_FLOWER_GROW, 0.2f, 0.5f + (float)Math.random() * 0.5f);
                    }
                }
            }
            lastMarkedRadius = currentRadius;
        }

        if (inNewDimension) {
            if (waitingForChunks) {
                long timeSinceArrival = getVisualTime() - dimensionChangeTime;

                if (timeSinceArrival < 2000) {
                    return;
                }

                net.minecraft.client.Minecraft client = net.minecraft.client.Minecraft.getInstance();
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

                if (chunksReady || timeSinceArrival > 15000) {

                    waitingForChunks = false;
                    arrivalTime = getVisualTime();
                }
            } else {
                if (getVisualTime() - arrivalTime > 2000) {
                    endTransition();
                }
            }
        }
    }

    public static net.sha.api.entity.VirtualStructureEntity portalMirageEntity = null;

    public static void onCacheGenerated() {
        if (!isTransitioning && !net.nostalgia.client.render.PortalSkyRenderer.isDebugging) return;
        BlockPos centerP = net.nostalgia.client.render.PortalSkyRenderer.isDebugging ? net.nostalgia.client.render.PortalSkyRenderer.debugCenter : ritualCenter;
        if (centerP == null) return;

        net.sha.api.SHAHologramManager.updateSpatialMap(net.nostalgia.client.render.NostalgiaChunkCache.INSTANCE);

        
        

        lastMarkedRadius = -1.0f;
    }

    public static float getWhiteoutAlpha() {
        if (!isTransitioning) return 0.0f;
        if (net.nostalgia.client.NostalgiaConfig.get().ritualType == net.nostalgia.client.NostalgiaConfig.RitualType.SEAMLESS_PORTAL) return 1.0f;
        if (isBystander) {
            long elapsed = getVisualTime() - phase2StartTime;
            
            
            if (elapsed > 1000) {
                return Math.max(0.0f, 1.0f - ((elapsed - 1000) / 500.0f));
            }
            return 1.0f;
        }
        if (inNewDimension && !waitingForChunks) {

            return 1.0f - getFadeProgress();
        }
        return 1.0f;
    }

    public static float getFadeProgress() {
        if (!isTransitioning || !inNewDimension || waitingForChunks) return 0.0f;

        long timeSinceChunks = getVisualTime() - arrivalTime;
        long fadeTime = 2000;

        if (timeSinceChunks > fadeTime) return 1.0f;
        return (float)timeSinceChunks / fadeTime;
    }

    private static double visualTime = -1;
    private static double visualTimeOffset = 0;
    private static long lastFrameTime = 0;
    private static boolean inertiaHooked = false;
    private static long totalInertiaDistance = 0;
    private static long inertiaDecelStart = 0;
    private static double inertiaStartVisualTime = 0;
    public static double cloudVisualTimeOffset = 0;

    public static long getVisualTimeOffset() {
        return (long) visualTimeOffset;
    }

    public static double getVisualTimeOffsetExact() {
        return cloudVisualTimeOffset;
    }

    private static long lastCloudOriginalTime = -1;
    private static double globalCloudOffset = 0.0;

    public static double getDynamicCloudOffset(long originalTime, boolean update) {
        if (update) {
            if (lastCloudOriginalTime == -1) {
                lastCloudOriginalTime = originalTime;
            } else {
                long diff = originalTime - lastCloudOriginalTime;
                if (Math.abs(diff) > 100) {
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
        if (net.nostalgia.client.NostalgiaConfig.get().ritualType == net.nostalgia.client.NostalgiaConfig.RitualType.SEAMLESS_PORTAL) return trueTime;
        long now = getVisualTime();
        if (visualTime == -1) {
            visualTime = trueTime;
            visualTimeOffset = 0;
            cloudVisualTimeOffset = 0;
            lastFrameTime = now;
            return trueTime;
        }

        double dt = (now - lastFrameTime) / 1000.0;
        lastFrameTime = now;
        if (dt > 0.1) dt = 0.1;

        long prevVisualTime = (long) visualTime;

        if (!inNewDimension || waitingForChunks) {

            long elapsed = now - transitionStartTime;
            double seconds = elapsed / 1000.0;

            double baseSeconds = Math.min(seconds, 3.0);
            double velocity = 2.8 * Math.pow(baseSeconds, 1.8) * 1500.0;

            if (seconds > 3.0) {

                double bonus = Math.min(seconds - 3.0, 5.0) / 5.0;
                velocity *= (1.0 + bonus * 1.7);
            }

            double delta = velocity * dt;
            visualTime -= delta;
            visualTimeOffset -= delta;
            cloudVisualTimeOffset -= delta;
        } else {

            if (!inertiaHooked) {
                long targetTimeIn2Sec = trueTime + 40;

                double currentVelocity = 2.8 * Math.pow(3.0, 1.8) * 1500.0;

                long desiredDistance = (long) currentVelocity;

                long diff = ((long)visualTime - targetTimeIn2Sec) % 192000;
                if (diff < 0) diff += 192000;

                long K = Math.round((desiredDistance - diff) / 192000.0);
                totalInertiaDistance = diff + 192000 * K;

                inertiaDecelStart = now;
                inertiaStartVisualTime = visualTime;
                inertiaHooked = true;
            }

            long elapsed = now - inertiaDecelStart;
            if (elapsed >= 2000) {
                visualTimeOffset += (trueTime - visualTime);
                visualTime = trueTime;
                return trueTime;
            }

            double progress = elapsed / 2000.0;

            double easeOut = 1.0 - Math.pow(1.0 - progress, 3.0);

            double newVisual = inertiaStartVisualTime - (totalInertiaDistance * easeOut);
            double delta = newVisual - visualTime;
            visualTimeOffset += delta;
            cloudVisualTimeOffset += delta;
            visualTime = newVisual;
        }

        long prevIndex = Math.floorDiv(prevVisualTime - 6000, 12000L);
        long currIndex = Math.floorDiv((long)visualTime - 6000, 12000L);
        if (prevIndex != currIndex) {
             net.minecraft.client.Minecraft client = net.minecraft.client.Minecraft.getInstance();
             boolean isRD = client.level != null && client.level.dimension().equals(net.nostalgia.world.dimension.ModDimensions.RD_132211_LEVEL_KEY);
             if (!isRD && client.player != null) {
                 float velocityMagnitude = (float) Math.abs(prevVisualTime - visualTime) / (float)(dt > 0 ? dt : 0.05);
                 float pitch = 1.0f + Math.min(velocityMagnitude / 20000.0f, 1.0f);

                 client.player.playSound(net.minecraft.sounds.SoundEvents.AMETHYST_CLUSTER_STEP, 0.4f, pitch);
                 client.player.playSound(net.minecraft.sounds.SoundEvents.ILLUSIONER_MIRROR_MOVE, 0.1f, pitch * 1.5f);
             }
        }

        return (long) visualTime;
    }

    public static float getTransitionTimeSeconds() {
        if (!isTransitioning) return 0.0f;
        long elapsed = getVisualTime() - transitionStartTime;
        return (float) (elapsed / 1000.0);
    }

    public static float getWhiteRadius() {
        if (net.nostalgia.client.NostalgiaConfig.get().ritualType == net.nostalgia.client.NostalgiaConfig.RitualType.SEAMLESS_PORTAL) return 0.0f;
        if (!isTransitioning || inNewDimension || currentPhase < 2) return 0.0f;
        long elapsed = getVisualTime() - phase2StartTime;

        float speedMultiplier = isBystander ? 2.0f : 1.0f;
        return (elapsed / 1000.0f) * 50.0f * speedMultiplier;
    }

    public static float getAlphaRadius() {
        if (net.nostalgia.client.render.PortalSkyRenderer.isDebugging) {
            float progress = Math.min(net.nostalgia.client.render.PortalSkyRenderer.debugTime / 1.5f, 1.0f);
            return progress * 288.0f;
        }
        if (net.nostalgia.client.NostalgiaConfig.get().ritualType == net.nostalgia.client.NostalgiaConfig.RitualType.SEAMLESS_PORTAL) return 0.0f;
        if (!isTransitioning || currentPhase < 3) return 0.0f;
        if (inNewDimension) return 300.0f;

        long elapsed = getVisualTime() - phase3StartTime;
        float progress = Math.min((elapsed / 1000.0f) / 5.0f, 1.0f);
        return progress * 300.0f;
    }
}
