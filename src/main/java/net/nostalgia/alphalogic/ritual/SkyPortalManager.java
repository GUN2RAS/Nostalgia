package net.nostalgia.alphalogic.ritual;

import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;

public class SkyPortalManager {

    private static volatile SkyPortalEventInstance active;

    public static SkyPortalEventInstance getActive() {
        return active;
    }

    public static boolean isAnyActive() {
        return active != null;
    }

    public static SkyPortalEventInstance start(BlockPos center, int crackPlaneY, int crackPlaneYTarget, boolean inverted, long seed, String sourceDimension, String targetDimension, int durationTicks, MinecraftServer server) {
        SkyPortalEventInstance inst = new SkyPortalEventInstance(center, crackPlaneY, crackPlaneYTarget, inverted, seed, sourceDimension, targetDimension, durationTicks);
        active = inst;
        if (server != null) {
            net.minecraft.server.level.ServerLevel sourceLevel = server.getLevel(net.minecraft.resources.ResourceKey.create(
                net.minecraft.core.registries.Registries.DIMENSION, net.minecraft.resources.Identifier.tryParse(sourceDimension)));
            if (sourceLevel != null) {
                PortalSavedData.get(sourceLevel).savePortal(center, crackPlaneY, crackPlaneYTarget, inverted, seed, sourceDimension, targetDimension, durationTicks);
            }
        }
        return inst;
    }

    public static void stop(MinecraftServer server, String sourceDimension) {
        active = null;
        if (server != null) {
            net.minecraft.server.level.ServerLevel sourceLevel = server.getLevel(net.minecraft.resources.ResourceKey.create(
                net.minecraft.core.registries.Registries.DIMENSION, net.minecraft.resources.Identifier.tryParse(sourceDimension)));
            if (sourceLevel != null) {
                PortalSavedData.get(sourceLevel).clearPortal();
            }
        }
    }

    public static void loadFromDisk(MinecraftServer server) {
        if (server == null) return;
        for (net.minecraft.server.level.ServerLevel level : server.getAllLevels()) {
            PortalSavedData data = PortalSavedData.get(level);
            if (data.portal.isPresent()) {
                PortalSavedData.PortalEntry entry = data.portal.get();
                active = new SkyPortalEventInstance(entry.center(), entry.crackPlaneY(), entry.crackPlaneYTarget(), entry.inverted(), entry.seed(), entry.sourceDimension(), entry.targetDimension(), entry.timerTicks());
                return;
            }
        }
    }

    public static void sendPortalToPlayer(net.minecraft.server.level.ServerPlayer player, MinecraftServer server) {
        SkyPortalEventInstance inst = active;
        if (inst == null) return;

        net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(player,
                new net.nostalgia.network.S2CSkyPortalPayload(true, inst.crackPlaneY(), inst.crackPlaneYTarget(), inst.inverted(), inst.seed(), inst.center(), inst.sourceDimension(), inst.targetDimension(), true));

        net.minecraft.server.level.ServerLevel targetLevel = server.getLevel(net.minecraft.resources.ResourceKey.create(
                net.minecraft.core.registries.Registries.DIMENSION, net.minecraft.resources.Identifier.tryParse(inst.targetDimension())));
        if (targetLevel != null && DimensionUtil.isClientGenerated(inst.targetDimension())) {
            java.util.Map<BlockPos, net.minecraft.world.level.block.state.BlockState> deltas =
                    HologramWorldData.get(targetLevel).getDeltasInRadius(inst.center(), 300.0);
            DeltaSyncService.sendBulkDeltasToPlayer(player, deltas, inst.targetDimension());
        }
    }

    public static void toggleGlobal(MinecraftServer server, BlockPos center, boolean inverted, long seed, String sourceDimension, String targetDimension) {
        SkyPortalEventInstance existing = active;
        boolean activating = existing == null;

        if (activating) {
            int crackPlaneY = center.getY() + 70;
            net.minecraft.server.level.ServerLevel sourceLevel = server.getLevel(net.minecraft.resources.ResourceKey.create(
                net.minecraft.core.registries.Registries.DIMENSION, net.minecraft.resources.Identifier.tryParse(sourceDimension)));
            if (sourceLevel != null) {
                int highestY = center.getY();
                int r = 320;
                int cxCenter = center.getX() >> 4;
                int czCenter = center.getZ() >> 4;
                for (int cx = -20; cx <= 20; cx++) {
                    for (int cz = -20; cz <= 20; cz++) {
                        if (sourceLevel.hasChunk(cxCenter + cx, czCenter + cz)) {
                            net.minecraft.world.level.chunk.ChunkAccess chunk = sourceLevel.getChunk(cxCenter + cx, czCenter + cz);
                            for (int lx = 0; lx < 16; lx++) {
                                for (int lz = 0; lz < 16; lz++) {
                                    int vx = (cxCenter + cx) * 16 + lx;
                                    int vz = (czCenter + cz) * 16 + lz;
                                    double dx = vx - center.getX();
                                    double dz = vz - center.getZ();
                                    if (dx * dx + dz * dz <= r * r) {
                                        int h = chunk.getHeight(net.minecraft.world.level.levelgen.Heightmap.Types.WORLD_SURFACE, lx, lz);
                                        if (h > highestY) highestY = h;
                                    }
                                }
                            }
                        } else {
                            if (cx % 2 == 0 && cz % 2 == 0) {
                                int vx = (cxCenter + cx) * 16 + 8;
                                int vz = (czCenter + cz) * 16 + 8;
                                double dx = vx - center.getX();
                                double dz = vz - center.getZ();
                                if (dx * dx + dz * dz <= r * r) {
                                    int h = sourceLevel.getChunkSource().getGenerator().getBaseHeight(
                                        vx,
                                        vz,
                                        net.minecraft.world.level.levelgen.Heightmap.Types.WORLD_SURFACE,
                                        sourceLevel,
                                        sourceLevel.getChunkSource().randomState()
                                    );
                                    if (h > highestY) highestY = h;
                                }
                            }
                        }
                    }
                }
                crackPlaneY = Math.max(center.getY() + 70, highestY + 10);
            }

            int crackPlaneYTarget = center.getY() + 70;
            net.minecraft.server.level.ServerLevel targetLevel = server.getLevel(net.minecraft.resources.ResourceKey.create(
                net.minecraft.core.registries.Registries.DIMENSION, net.minecraft.resources.Identifier.tryParse(targetDimension)));
            if (targetLevel != null) {
                int targetSurfaceY;
                int centerCx = center.getX() >> 4;
                int centerCz = center.getZ() >> 4;
                if (targetLevel.hasChunk(centerCx, centerCz)) {
                    targetSurfaceY = targetLevel.getChunk(centerCx, centerCz).getHeight(
                        net.minecraft.world.level.levelgen.Heightmap.Types.WORLD_SURFACE,
                        center.getX() & 15,
                        center.getZ() & 15
                    );
                } else {
                    targetSurfaceY = targetLevel.getChunkSource().getGenerator().getBaseHeight(
                        center.getX(),
                        center.getZ(),
                        net.minecraft.world.level.levelgen.Heightmap.Types.WORLD_SURFACE,
                        targetLevel,
                        targetLevel.getChunkSource().randomState()
                    );
                }

                int highestYTarget = targetSurfaceY;
                int r = 320;
                int cxCenter = center.getX() >> 4;
                int czCenter = center.getZ() >> 4;
                for (int cx = -20; cx <= 20; cx++) {
                    for (int cz = -20; cz <= 20; cz++) {
                        if (targetLevel.hasChunk(cxCenter + cx, czCenter + cz)) {
                            net.minecraft.world.level.chunk.ChunkAccess chunk = targetLevel.getChunk(cxCenter + cx, czCenter + cz);
                            for (int lx = 0; lx < 16; lx++) {
                                for (int lz = 0; lz < 16; lz++) {
                                    int vx = (cxCenter + cx) * 16 + lx;
                                    int vz = (czCenter + cz) * 16 + lz;
                                    double dx = vx - center.getX();
                                    double dz = vz - center.getZ();
                                    if (dx * dx + dz * dz <= r * r) {
                                        int h = chunk.getHeight(net.minecraft.world.level.levelgen.Heightmap.Types.WORLD_SURFACE, lx, lz);
                                        if (h > highestYTarget) highestYTarget = h;
                                    }
                                }
                            }
                        } else {
                            if (cx % 2 == 0 && cz % 2 == 0) {
                                int vx = (cxCenter + cx) * 16 + 8;
                                int vz = (czCenter + cz) * 16 + 8;
                                double dx = vx - center.getX();
                                double dz = vz - center.getZ();
                                if (dx * dx + dz * dz <= r * r) {
                                    int h = targetLevel.getChunkSource().getGenerator().getBaseHeight(
                                        vx,
                                        vz,
                                        net.minecraft.world.level.levelgen.Heightmap.Types.WORLD_SURFACE,
                                        targetLevel,
                                        targetLevel.getChunkSource().randomState()
                                    );
                                    if (h > highestYTarget) highestYTarget = h;
                                }
                            }
                        }
                    }
                }
                crackPlaneYTarget = Math.max(targetSurfaceY + 70, highestYTarget + 10);
            } else {
                crackPlaneYTarget = crackPlaneY;
            }

            start(center, crackPlaneY, crackPlaneYTarget, inverted, seed, sourceDimension, targetDimension, 6000, server);
            if (sourceLevel != null) {
                TimestopZoneManager.removeZone(sourceLevel, center);
            }
        } else {
            targetDimension = existing.targetDimension();
            sourceDimension = existing.sourceDimension();
            stop(server, sourceDimension);
        }

        int crackPlaneY = activating && active != null ? active.crackPlaneY() : 256;
        int crackPlaneYTarget = activating && active != null ? active.crackPlaneYTarget() : 256;
        net.nostalgia.network.S2CSkyPortalPayload payload = new net.nostalgia.network.S2CSkyPortalPayload(activating, crackPlaneY, crackPlaneYTarget, inverted, seed, center, sourceDimension, targetDimension);
        for (net.minecraft.server.level.ServerPlayer p : server.getPlayerList().getPlayers()) {
            net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(p, payload);
        }

        if (activating) {
            net.minecraft.server.level.ServerLevel targetLevel = server.getLevel(net.minecraft.resources.ResourceKey.create(
                net.minecraft.core.registries.Registries.DIMENSION, net.minecraft.resources.Identifier.tryParse(targetDimension)));
            if (targetLevel != null && DimensionUtil.isClientGenerated(targetDimension)) {
                java.util.Map<BlockPos, net.minecraft.world.level.block.state.BlockState> deltas = HologramWorldData.get(targetLevel).getDeltasInRadius(center, 300.0);
                DeltaSyncService.broadcastBulkDeltas(server, deltas, targetDimension, null);
            }
        }
    }

    private static final java.util.concurrent.ConcurrentHashMap<java.util.UUID, Long> lastLandingTime = new java.util.concurrent.ConcurrentHashMap<>();

    public static void handleClientLandingRequest(MinecraftServer server, net.minecraft.server.level.ServerPlayer p,
                                                   double px, double py, double pz, float yRot, float xRot) {
        SkyPortalEventInstance inst = getActive();
        if (inst == null) return;

        long now = System.currentTimeMillis();
        java.util.UUID uuid = p.getUUID();
        Long lastTime = lastLandingTime.get(uuid);
        if (lastTime != null && now - lastTime < 1500L) return;
        lastLandingTime.put(uuid, now);

        String pDim = p.level().dimension().identifier().toString();
        boolean inSource = pDim.equals(inst.sourceDimension());
        boolean inTarget = pDim.equals(inst.targetDimension());
        if (!inSource && !inTarget) return;

        String oppositeDim = inSource ? inst.targetDimension() : inst.sourceDimension();
        net.minecraft.server.level.ServerLevel oppositeLevel = server.getLevel(
            net.minecraft.resources.ResourceKey.create(
                net.minecraft.core.registries.Registries.DIMENSION,
                net.minecraft.resources.Identifier.tryParse(oppositeDim)));
        if (oppositeLevel == null) return;

        int inversionConstant = inst.crackPlaneY() + inst.crackPlaneYTarget();
        int feetY = net.minecraft.util.Mth.floor(py - 0.1);
        int targetY = inversionConstant - feetY;

        double newX = px;
        double newY = targetY + 1.0;
        double newZ = inst.inverted() ? (2 * inst.center().getZ() - pz) : pz;
        float newYaw = inst.inverted() ? (yRot + 180.0F) : yRot;

        newY = Math.max(oppositeLevel.getMinY() + 1.0, Math.min(oppositeLevel.getMaxY() - 2.0, newY));

        if (p instanceof com.example.api.GravityChanger gc) {
            gc.clearInfection();
            gc.setGravityInstant(com.example.api.Gravity.DOWN);
        }

        if (p.containerMenu != null && p.containerMenu != p.inventoryMenu) {
            p.closeContainer();
        }

        net.minecraft.world.phys.Vec3 motion = p.getDeltaMovement();

        p.teleportTo(oppositeLevel, newX, newY, newZ, java.util.Collections.emptySet(), newYaw, xRot, true);

        p.setDeltaMovement(motion);
        p.hurtMarked = true;

        p.level().playSound(null, p.getX(), p.getY(), p.getZ(),
            net.minecraft.sounds.SoundEvents.PORTAL_TRAVEL, net.minecraft.sounds.SoundSource.PLAYERS, 0.5F, 1.2F);

        org.slf4j.LoggerFactory.getLogger("SkyPortalManager").info(
            "[SkyPortal] Player {} landed (client-reported), teleported to {} at ({}, {}, {})",
            p.getName().getString(), oppositeDim, newX, newY, newZ);
    }
}
