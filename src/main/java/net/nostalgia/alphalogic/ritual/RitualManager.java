package net.nostalgia.alphalogic.ritual;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

public class RitualManager {
    public enum State {
        INACTIVE,
        TIME_STOPPING,
        FROZEN,
        REVERSING_TIME,
        TIME_RESUMING_DELAY,
        TIME_RESUMING
    }

    private static State currentState = State.INACTIVE;
    private static int ticksActive = 0;
    private static long timeStopStartTime = 0;
    static ServerLevel targetLevel;
    static BlockPos targetBeaconPos;

    private static net.minecraft.server.level.ServerPlayer transitioningPlayer = null;
    static java.util.List<net.minecraft.world.entity.Entity> transitioningEntities = new java.util.ArrayList<>();
    static ServerLevel transitionTarget = null;
    static String transitionDimensionId = "";
    static BlockPos transitionTargetPos = null;
    private static double transitionTargetY = 0;

    public static int currentSyncPhase = 0;
    public static long phaseStartTime = 0;
    public static long activeRitualMillis = 0;
    private static long lastServerTickMillis = 0;
    static final java.util.Set<java.util.UUID> readyClients = java.util.concurrent.ConcurrentHashMap.newKeySet();
    public static final java.util.Set<java.util.UUID> clientsReadyForNextPhase = java.util.concurrent.ConcurrentHashMap.newKeySet();
    public static final java.util.concurrent.ConcurrentHashMap<java.util.UUID, Integer> clientHologramSurfaces = new java.util.concurrent.ConcurrentHashMap<>();

    public static void markClientReady(java.util.UUID uuid) {
        readyClients.add(uuid);
    }

    public static State getClientState() { return currentState; }
    public static void setClientState(State state) { currentState = state; }
    public static boolean isServerTransitioning() { return currentState == State.REVERSING_TIME; }
    public static boolean isServerActive() { return currentState != State.INACTIVE; }
    public static BlockPos getTargetBeaconPos() { return targetBeaconPos; }
    public static void setTargetBeaconPos(BlockPos pos) { targetBeaconPos = pos; }
    
    private static boolean isUnsafeBlock(net.minecraft.server.level.ServerLevel level, net.minecraft.core.BlockPos pos) {
        net.minecraft.world.level.block.state.BlockState state = level.getBlockState(pos);
        if (state.isAir()) return false;
        if (state.is(net.nostalgia.block.AlphaBlocks.ALPHA_LEAVES) || state.is(net.nostalgia.block.AlphaBlocks.ALPHA_OAK_LOG)) return true;
        if (state.is(net.minecraft.world.level.block.Blocks.WATER) || state.is(net.minecraft.world.level.block.Blocks.LAVA)) return true;
        try {
            return !state.getCollisionShape(level, pos).isEmpty();
        } catch (Exception e) {
            return true;
        }
    }
    
    private static int structureCheckTick = 0;

    public static void eagerlyRemoveBrokenZones(ServerLevel level) {
        if (level == null) return;
        boolean removedAny = false;
        java.util.Iterator<ActiveZone> it = activeZones.iterator();
        while (it.hasNext()) {
            ActiveZone zone = it.next();
            if (zone.dimension() != level.dimension()) continue;
            BlockPos bPos = zone.beaconPos();
            if (level.isLoaded(bPos)) {
                net.minecraft.world.level.block.state.BlockState bState = level.getBlockState(bPos);
                net.minecraft.world.level.block.state.BlockState aState = level.getBlockState(bPos.below());
                boolean isValid = bState.is(net.minecraft.world.level.block.Blocks.BEACON) && 
                                  aState.is(net.minecraft.world.level.block.Blocks.RESPAWN_ANCHOR) &&
                                  aState.hasProperty(net.minecraft.world.level.block.RespawnAnchorBlock.CHARGE) &&
                                  aState.getValue(net.minecraft.world.level.block.RespawnAnchorBlock.CHARGE) == 4;
                if (!isValid) {
                    if (targetBeaconPos != null && targetBeaconPos.equals(bPos) && currentState != State.INACTIVE) {
                        continue;
                    }
                    if (level.tickRateManager() instanceof net.nostalgia.alphalogic.ritual.TickRateManagerAccess access) {
                        access.nostalgia$removeRegionAt(level.dimension(), bPos);
                    }
                    activeZones.remove(zone);
                    removedAny = true;
                    net.nostalgia.network.S2CTimestopZoneEndPayload payload = new net.nostalgia.network.S2CTimestopZoneEndPayload(bPos);
                    for (net.minecraft.server.level.ServerPlayer sp : level.getServer().getPlayerList().getPlayers()) {
                        net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(sp, payload);
                    }
                    net.minecraft.world.level.block.state.BlockState bs = level.getBlockState(bPos);
                    level.sendBlockUpdated(bPos, bs, bs, 3);
                    net.minecraft.world.entity.item.ItemEntity crystal = new net.minecraft.world.entity.item.ItemEntity(
                            level, bPos.getX() + 0.5, bPos.getY() + 1.5, bPos.getZ() + 0.5,
                            new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.ECHO_SHARD)
                    );
                    crystal.setDefaultPickUpDelay();
                    level.addFreshEntity(crystal);
                }
            }
        }
        if (removedAny) persistZones(level);
    }

    public static boolean checkZoneStability(ServerLevel level, BlockPos pos) {
        eagerlyRemoveBrokenZones(level);
        int cx = pos.getX() >> 4;
        int cz = pos.getZ() >> 4;
        for (ActiveZone zone : activeZones) {
            if (!zone.dimension().equals(level.dimension())) continue;
            if (zone.beaconPos().equals(pos)) continue;
            int zcx = zone.beaconPos().getX() >> 4;
            int zcz = zone.beaconPos().getZ() >> 4;
            if (Math.abs(cx - zcx) <= 10 && Math.abs(cz - zcz) <= 10) {
                return false;
            }
        }
        return true;
    }

    private static void tickActiveZones(net.minecraft.server.MinecraftServer server) {
        structureCheckTick++;
        if (structureCheckTick % 20 == 0) {
            for (ActiveZone zone : new java.util.ArrayList<>(activeZones)) {
                net.minecraft.server.level.ServerLevel zoneLevel = server.getLevel(zone.dimension());
                if (zoneLevel != null) {
                    BlockPos bPos = zone.beaconPos();
                    if (zoneLevel.isLoaded(bPos)) {
                        net.minecraft.world.level.block.state.BlockState bState = zoneLevel.getBlockState(bPos);
                        net.minecraft.world.level.block.state.BlockState aState = zoneLevel.getBlockState(bPos.below());
                        boolean isValid = bState.is(net.minecraft.world.level.block.Blocks.BEACON) && 
                                          aState.is(net.minecraft.world.level.block.Blocks.RESPAWN_ANCHOR) &&
                                          aState.hasProperty(net.minecraft.world.level.block.RespawnAnchorBlock.CHARGE) &&
                                          aState.getValue(net.minecraft.world.level.block.RespawnAnchorBlock.CHARGE) == 4;
                        if (!isValid) {
                            if (targetBeaconPos != null && targetBeaconPos.equals(bPos) && currentState != State.INACTIVE) {
                                handleInterrupt();
                            } else {
                                removeZone(zoneLevel, bPos);
                                net.minecraft.world.entity.item.ItemEntity crystal = new net.minecraft.world.entity.item.ItemEntity(
                                        zoneLevel,
                                        bPos.getX() + 0.5,
                                        bPos.getY() + 1.5,
                                        bPos.getZ() + 0.5,
                                        new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.ECHO_SHARD)
                                );
                                crystal.setDefaultPickUpDelay();
                                zoneLevel.addFreshEntity(crystal);
                            }
                        }
                    }
                }
            }
        }
    }

    public static void init() {
        if (net.fabricmc.loader.api.FabricLoader.getInstance().isModLoaded("sha")) {
            net.sha.api.SHAHologramManager.ignorePredicate = (java.util.function.Predicate<net.minecraft.world.entity.Entity>) (entity -> {
                if (entity instanceof net.minecraft.world.entity.item.ItemEntity) return false;
                return !net.nostalgia.alphalogic.ritual.RitualActiveState.isParticipant(entity);
            });
        }
        
        ServerTickEvents.START_SERVER_TICK.register(server -> {
            tickActiveZones(server);
            long nowMs = System.currentTimeMillis();
            long dt = lastServerTickMillis == 0 ? 50 : (nowMs - lastServerTickMillis);
            lastServerTickMillis = nowMs;
            if (currentState != State.INACTIVE) {
                activeRitualMillis += dt;
            }

            if (currentSyncPhase > 0 && transitionTarget != null && transitionTargetPos != null) {
                if (currentSyncPhase == 1) {
                    boolean allReady = true;
                    for (net.minecraft.world.entity.Entity e : transitioningEntities) {
                        if (e instanceof net.minecraft.server.level.ServerPlayer sp && !readyClients.contains(sp.getUUID())) {
                            allReady = false; break;
                        }
                    }
                    if (allReady || activeRitualMillis - phaseStartTime > 15000) {
                        currentSyncPhase = 2;
                        phaseStartTime = activeRitualMillis;
                        for (net.minecraft.world.entity.Entity e : transitioningEntities) {
                             if (e instanceof net.minecraft.server.level.ServerPlayer sp) {
                                 net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(sp, new net.nostalgia.network.S2CRitualPhasePayload(2));
                             }
                        }
                    }
                } else if (currentSyncPhase == 2) {
                    if (activeRitualMillis - phaseStartTime > 6500) {
                        currentSyncPhase = 3;
                        phaseStartTime = activeRitualMillis;
                        if (net.fabricmc.loader.api.FabricLoader.getInstance().isModLoaded("sha")) {
                            net.sha.api.SHAHologramManager.updateSpatialMap(net.nostalgia.alphalogic.ritual.NostalgiaServerCollisionBypassProvider.INSTANCE);
                        }
                        for (net.minecraft.world.entity.Entity e : transitioningEntities) {
                             if (e instanceof net.minecraft.server.level.ServerPlayer sp) {
                                 net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(sp, new net.nostalgia.network.S2CRitualPhasePayload(3));
                             }
                        }
                    }
                } else if (currentSyncPhase == 3) {
                    long requiredDelay = 6500;
                    if ("overworld".equals(transitionDimensionId)) {
                        requiredDelay = 2500;
                    }
                    if (activeRitualMillis - phaseStartTime > requiredDelay) {
                        for (java.util.Map.Entry<BlockPos, net.minecraft.world.level.block.state.BlockState> entry : VirtualBlockCache.getAll().entrySet()) {
                            BlockPos vPos = entry.getKey();
                            BlockPos localizedPos = new BlockPos(
                                vPos.getX() + net.nostalgia.alphalogic.ritual.RitualActiveState.offsetX,
                                vPos.getY() - net.nostalgia.alphalogic.ritual.RitualActiveState.yOffset,
                                vPos.getZ() + net.nostalgia.alphalogic.ritual.RitualActiveState.offsetZ
                            );
                            
                            transitionTarget.getChunk(localizedPos.getX() >> 4, localizedPos.getZ() >> 4, net.minecraft.world.level.chunk.status.ChunkStatus.FULL, true);
                            transitionTarget.setBlock(localizedPos, entry.getValue(), 3);
                            
                            if (transitionTarget.dimension() == net.nostalgia.world.dimension.ModDimensions.ALPHA_112_01_LEVEL_KEY) {
                                net.nostalgia.alphalogic.ritual.AlphaWorldData.get(transitionTarget).addDelta(localizedPos, entry.getValue());
                            }
                        }
                        VirtualBlockCache.clear();

                        for (net.minecraft.world.entity.Entity entity : transitioningEntities) {
                            if (!entity.isAlive()) continue;
                            
                            net.minecraft.world.phys.Vec3 motion = entity.getDeltaMovement();
                            double newX = entity.getX() + net.nostalgia.alphalogic.ritual.RitualActiveState.offsetX;
                            double newZ = entity.getZ() + net.nostalgia.alphalogic.ritual.RitualActiveState.offsetZ;
                            double newY;
                            
                            if (entity instanceof net.minecraft.server.level.ServerPlayer sp) {
                                if (sp.level().dimension() == net.nostalgia.world.dimension.ModDimensions.ALPHA_112_01_LEVEL_KEY) {
                                    newY = entity.getY() - net.nostalgia.alphalogic.ritual.RitualActiveState.yOffset;
                                } else {
                                    int reportedClientAlphaY = clientHologramSurfaces.getOrDefault(sp.getUUID(), -1);
                                    if (reportedClientAlphaY != -1) {
                                        int expectedHologramY = reportedClientAlphaY + net.nostalgia.alphalogic.ritual.RitualActiveState.yOffset;
                                        double dy = entity.getY() - expectedHologramY;
                                        if (dy < 0) dy = 0;
                                        newY = reportedClientAlphaY + dy;
                                    } else {
                                        newY = entity.getY() - net.nostalgia.alphalogic.ritual.RitualActiveState.yOffset;
                                    }
                                }
                                if (sp.level().dimension() != net.nostalgia.world.dimension.ModDimensions.ALPHA_112_01_LEVEL_KEY) {
                                    if (sp.containerMenu != null && sp.containerMenu != sp.inventoryMenu) {
                                        sp.closeContainer();
                                    }
                                }
                                sp.teleportTo(transitionTarget, newX, newY, newZ, java.util.Collections.emptySet(), sp.getYRot(), sp.getXRot(), true);
                                if (sp.isCreative() && !sp.getAbilities().mayfly) {
                                    sp.getAbilities().mayfly = true;
                                    sp.onUpdateAbilities();
                                }
                            } else {
                                newY = entity.getY();
                                entity.teleportTo(transitionTarget, newX, newY, newZ, java.util.Collections.emptySet(), entity.getYRot(), entity.getXRot(), true);
                            }
                            
                            entity.setDeltaMovement(motion);
                            if (entity instanceof net.minecraft.world.entity.player.Player p) p.hurtMarked = true;
                        }
                        
                        net.nostalgia.alphalogic.ritual.RitualActiveState.isTransitioning = false;

                        for (net.minecraft.world.entity.Entity entity : transitioningEntities) {
                            if (entity instanceof net.minecraft.server.level.ServerPlayer sp) {
                                net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(sp, new net.nostalgia.network.S2CEndTransitionVisualsPayload());
                            }
                        }

                        java.util.Set<java.util.UUID> participantSet = new java.util.HashSet<>();
                        for (net.minecraft.world.entity.Entity entity : transitioningEntities) {
                            participantSet.add(entity.getUUID());
                        }
                        if (targetLevel != null) {
                            for (net.minecraft.world.entity.player.Player lp : targetLevel.players()) {
                                if (lp instanceof net.minecraft.server.level.ServerPlayer sp && !participantSet.contains(sp.getUUID())) {
                                    net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(sp, new net.nostalgia.network.S2CEndTransitionVisualsPayload());
                                }
                            }
                        }
                        

                        endRitual();
                        transitioningEntities.clear();
                        clientHologramSurfaces.clear();
                        for (java.util.UUID u : clientsReadyForNextPhase) { readyClients.remove(u); }
                        clientsReadyForNextPhase.clear();
                        transitionTarget = null;
                        transitionDimensionId = "";
                        transitionTargetPos = null;
                        currentSyncPhase = 0;
                    }
                }
            }

            if (currentState == State.INACTIVE) return;

            if (currentState == State.REVERSING_TIME) {

            }

            if (currentState == State.TIME_RESUMING_DELAY) {
                long elapsed = activeRitualMillis - timeStopStartTime;
                if (elapsed >= 5000) {
                    if (targetLevel != null && targetBeaconPos != null) {
                        removeZone(targetLevel, targetBeaconPos);
                    }
                    triggerTimeResume();
                }
            }

            if (currentState == State.TIME_STOPPING) {
                long elapsed = activeRitualMillis - timeStopStartTime;

                if (elapsed < 2000) {
                    float progress = (float) elapsed / 2000.0f;
                    float newTps = 20.0f - (19.0f * progress);
                    targetLevel.getServer().tickRateManager().setTickRate(newTps);
                } else {
                    transitionToFrozen();
                }
            } else if (currentState == State.TIME_RESUMING) {
                long elapsed = activeRitualMillis - timeStopStartTime;

                if (elapsed < 2000) {
                    float progress = (float) elapsed / 2000.0f;
                    float newTps = 1.0f + (19.0f * progress);
                    targetLevel.getServer().tickRateManager().setTickRate(newTps);
                } else {
                    endRitual();
                }
            }
        });
    }

    public static final int ZONE_RADIUS_CHUNKS = 5;

    public record ActiveZone(
            net.minecraft.resources.ResourceKey<net.minecraft.world.level.Level> dimension,
            BlockPos beaconPos,
            int radiusChunks,
            long snapGameTime,
            long snapClockTicks,
            float snapRain,
            float snapThunder
    ) { }

    public static final java.util.concurrent.CopyOnWriteArrayList<ActiveZone> activeZones = new java.util.concurrent.CopyOnWriteArrayList<>();

    public static boolean hasAnyRainingZone(net.minecraft.resources.ResourceKey<net.minecraft.world.level.Level> dimension) {
        for (ActiveZone zone : activeZones) {
            if (zone.dimension() == dimension && zone.snapRain() > 0.0F) {
                return true;
            }
        }
        return false;
    }

    
    public static float getLocalRainLevel(net.minecraft.resources.ResourceKey<net.minecraft.world.level.Level> dimension, BlockPos pos) {
        ActiveZone zone = getZoneAt(dimension, pos);
        if (zone != null) {
            return zone.snapRain();
        }
        return -1.0F;
    }

    public static ActiveZone getZoneAt(net.minecraft.resources.ResourceKey<net.minecraft.world.level.Level> dimension, BlockPos pos) {
        for (ActiveZone zone : activeZones) {
            if (zone.dimension() == dimension) {
                int cx = pos.getX() >> 4;
                int cz = pos.getZ() >> 4;
                int zcx = zone.beaconPos().getX() >> 4;
                int zcz = zone.beaconPos().getZ() >> 4;
                if (Math.abs(cx - zcx) <= zone.radiusChunks() && Math.abs(cz - zcz) <= zone.radiusChunks()) {
                    return zone;
                }
            }
        }
        return null;
    }
    public static java.util.List<ActiveZone> getActiveZones() {
        return java.util.Collections.unmodifiableList(activeZones);
    }

    public static boolean hasActiveZones() {
        return !activeZones.isEmpty();
    }

    public static ActiveZone findZoneByBeacon(BlockPos beaconPos) {
        if (beaconPos == null) return null;
        for (ActiveZone z : activeZones) {
            if (z.beaconPos.equals(beaconPos)) return z;
        }
        return null;
    }

    public static ActiveZone findZoneContaining(net.minecraft.resources.ResourceKey<net.minecraft.world.level.Level> dim, BlockPos pos) {
        if (dim == null || pos == null) return null;
        int cx = pos.getX() >> 4;
        int cz = pos.getZ() >> 4;
        for (ActiveZone z : activeZones) {
            if (!z.dimension.equals(dim)) continue;
            int bx = z.beaconPos.getX() >> 4;
            int bz = z.beaconPos.getZ() >> 4;
            if (Math.max(Math.abs(cx - bx), Math.abs(cz - bz)) <= z.radiusChunks) return z;
        }
        return null;
    }

    public static boolean hasActiveZone() {
        return hasActiveZones();
    }

    public static void startRitual(ServerLevel level, BlockPos beaconPos) {
        if (currentState != State.INACTIVE) return;
        if (findZoneByBeacon(beaconPos) != null) return;
        targetLevel = level;
        targetBeaconPos = beaconPos;
        targetLevel.getServer().setWeatherParameters(6000, 0, false, false);
        transitionToTimeStop();

        addZone(level, beaconPos, false);

        long gameTime = level.getGameTime();
        long clockTicks = level.getDefaultClockTime();
        float rain = level.getRainLevel(1.0f);
        float thunder = level.getThunderLevel(1.0f);

        net.nostalgia.network.S2CTimestopZoneStartPayload payload = new net.nostalgia.network.S2CTimestopZoneStartPayload(
                beaconPos, ZONE_RADIUS_CHUNKS, level.dimension().identifier().toString(),
                false, gameTime, clockTicks, rain, thunder);
        for (net.minecraft.server.level.ServerPlayer sp : level.getServer().getPlayerList().getPlayers()) {
            if (sp.level() == level) {
                net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(sp, payload);
            }
        }
        net.minecraft.world.level.block.state.BlockState bs = level.getBlockState(beaconPos);
        level.sendBlockUpdated(beaconPos, bs, bs, 3);
    }

    public static void addZone(ServerLevel level, BlockPos beaconPos, boolean applyPhysicsFreeze) {
        if (level == null || beaconPos == null) return;
        if (findZoneByBeacon(beaconPos) != null) return;
        if (applyPhysicsFreeze && level.tickRateManager() instanceof net.nostalgia.alphalogic.ritual.TickRateManagerAccess access) {
            access.nostalgia$addRegion(new net.nostalgia.alphalogic.ritual.FreezeRegion(level.dimension(), beaconPos, ZONE_RADIUS_CHUNKS));
        }
        ActiveZone zone = new ActiveZone(
                level.dimension(),
                beaconPos.immutable(),
                ZONE_RADIUS_CHUNKS,
                level.getGameTime(),
                level.getDefaultClockTime(),
                level.getRainLevel(1.0f),
                level.getThunderLevel(1.0f)
        );
        activeZones.add(zone);
        persistZones(level);
    }

    public static void persistZones(ServerLevel level) {
        if (level == null) return;
        java.util.List<ZoneSavedData.ZoneEntry> entries = new java.util.ArrayList<>();
        for (ActiveZone z : activeZones) {
            entries.add(new ZoneSavedData.ZoneEntry(
                    z.beaconPos, z.radiusChunks, z.dimension.identifier().toString(),
                    z.snapGameTime, z.snapClockTicks, z.snapRain, z.snapThunder));
        }
        ZoneSavedData data = ZoneSavedData.get(level);
        data.updateZones(entries);
    }

    public static void loadZones(net.minecraft.server.MinecraftServer server) {
        if (server == null) return;
        ServerLevel overworld = server.getLevel(net.minecraft.world.level.Level.OVERWORLD);
        if (overworld == null) return;
        ZoneSavedData data = ZoneSavedData.get(overworld);
        if (data == null || data.zones.isEmpty()) return;
        activeZones.clear();
        for (ZoneSavedData.ZoneEntry e : data.zones) {
            net.minecraft.resources.ResourceKey<net.minecraft.world.level.Level> dim =
                    net.minecraft.resources.ResourceKey.create(net.minecraft.core.registries.Registries.DIMENSION,
                            net.minecraft.resources.Identifier.tryParse(e.dimensionId()));
            ActiveZone zone = new ActiveZone(dim, e.beaconPos(), e.radiusChunks(),
                    e.snapGameTime(), e.snapClockTicks(), e.snapRain(), e.snapThunder());
            activeZones.add(zone);
            ServerLevel target = server.getLevel(dim);
            if (target != null && target.tickRateManager() instanceof net.nostalgia.alphalogic.ritual.TickRateManagerAccess access) {
                access.nostalgia$addRegion(new net.nostalgia.alphalogic.ritual.FreezeRegion(dim, e.beaconPos(), e.radiusChunks()));
            }
        }
    }

    public static void sendZonesToPlayer(net.minecraft.server.level.ServerPlayer player) {
        if (player == null || activeZones.isEmpty()) return;
        net.minecraft.resources.ResourceKey<net.minecraft.world.level.Level> dim = player.level().dimension();
        ActiveZone containing = findZoneContaining(dim, player.blockPosition());
        for (ActiveZone z : activeZones) {
            if (!z.dimension.equals(dim)) continue;
            boolean instant = (z == containing);
            net.nostalgia.network.S2CTimestopZoneStartPayload payload = new net.nostalgia.network.S2CTimestopZoneStartPayload(
                    z.beaconPos, z.radiusChunks, z.dimension.identifier().toString(),
                    instant, z.snapGameTime, z.snapClockTicks, z.snapRain, z.snapThunder);
            net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(player, payload);
        }
    }

    public static void sendZoneToPlayer(net.minecraft.server.level.ServerPlayer player) {
        sendZonesToPlayer(player);
    }

    public static void removeZone(ServerLevel level, BlockPos beaconPos) {
        if (level == null || beaconPos == null) return;
        if (level.tickRateManager() instanceof net.nostalgia.alphalogic.ritual.TickRateManagerAccess access) {
            access.nostalgia$removeRegionAt(level.dimension(), beaconPos);
        }
        activeZones.removeIf(z -> z.beaconPos.equals(beaconPos));
        persistZones(level);
        net.nostalgia.network.S2CTimestopZoneEndPayload payload = new net.nostalgia.network.S2CTimestopZoneEndPayload(beaconPos);
        for (net.minecraft.server.level.ServerPlayer sp : level.getServer().getPlayerList().getPlayers()) {
            net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(sp, payload);
        }
        net.minecraft.world.level.block.state.BlockState bs = level.getBlockState(beaconPos);
        level.sendBlockUpdated(beaconPos, bs, bs, 3);
    }

    public static void triggerTimeStop(ServerLevel level) {
        if (currentState != State.INACTIVE) return;
        targetLevel = level;
        targetLevel.getServer().tickRateManager().setFrozen(false);
        targetLevel.getServer().setWeatherParameters(6000, 0, false, false);
        currentState = State.TIME_STOPPING;
        timeStopStartTime = activeRitualMillis;
    }

    public static void triggerTimeResume() {
        if (currentState != State.FROZEN && currentState != State.TIME_STOPPING && currentState != State.TIME_RESUMING_DELAY) return;
        currentState = State.TIME_RESUMING;
        timeStopStartTime = activeRitualMillis;
        if (targetLevel != null) {
            targetLevel.getServer().tickRateManager().setFrozen(false);
            targetLevel.getServer().tickRateManager().setTickRate(1.0f);
        }
    }

    private static void transitionToTimeStop() {
        currentState = State.TIME_STOPPING;
        timeStopStartTime = activeRitualMillis;
    }

    private static void transitionToFrozen() {
        if (targetLevel != null) {
            targetLevel.getServer().tickRateManager().setTickRate(20.0f);
            if (targetBeaconPos != null) {
                if (targetLevel.tickRateManager() instanceof net.nostalgia.alphalogic.ritual.TickRateManagerAccess access) {
                    access.nostalgia$addRegion(new net.nostalgia.alphalogic.ritual.FreezeRegion(targetLevel.dimension(), targetBeaconPos, ZONE_RADIUS_CHUNKS));
                }
                if (findZoneByBeacon(targetBeaconPos) == null) {
                    addZone(targetLevel, targetBeaconPos, false);
                }
            }
        }
        currentState = State.INACTIVE;
        targetLevel = null;
        targetBeaconPos = null;
    }

    public static void endRitual() {
        if (net.fabricmc.loader.api.FabricLoader.getInstance().isModLoaded("sha")) {
            net.sha.api.SHAHologramManager.removeProvider(net.nostalgia.alphalogic.ritual.NostalgiaServerCollisionBypassProvider.INSTANCE);
        }
        if (targetLevel != null && targetBeaconPos != null) {
            removeZone(targetLevel, targetBeaconPos);
        }
        currentState = State.INACTIVE;
        ticksActive = 0;
        timeStopStartTime = 0;
        if (targetLevel != null) {
            targetLevel.getServer().tickRateManager().setTickRate(20.0f);
            targetLevel.getServer().tickRateManager().setFrozen(false);
        }
        net.nostalgia.alphalogic.ritual.RitualActiveState.offsetX = 0;
        net.nostalgia.alphalogic.ritual.RitualActiveState.offsetZ = 0;
        net.nostalgia.alphalogic.ritual.RitualActiveState.yOffset = 0;
        net.nostalgia.alphalogic.ritual.RitualActiveState.ritualCenter = null;
        net.nostalgia.alphalogic.ritual.RitualActiveState.isTransitioning = false;
    }

    public static void handlePlayerDisconnect(net.minecraft.server.level.ServerPlayer player) {
        if (readyClients.contains(player.getUUID())) {
            readyClients.remove(player.getUUID());
        }
        if (transitioningEntities.contains(player)) {
            transitioningEntities.remove(player);
        }
        if (net.nostalgia.alphalogic.ritual.RitualActiveState.participants.contains(player.getUUID())) {
            net.nostalgia.alphalogic.ritual.RitualActiveState.participants.remove(player.getUUID());
            java.util.List<java.util.UUID> participantUuids = new java.util.ArrayList<>(net.nostalgia.alphalogic.ritual.RitualActiveState.participants);
            net.nostalgia.network.S2CSyncParticipantsPayload payload = new net.nostalgia.network.S2CSyncParticipantsPayload(participantUuids);
            if (targetLevel != null && targetLevel.getServer() != null) {
                for (net.minecraft.server.level.ServerPlayer sp : targetLevel.getServer().getPlayerList().getPlayers()) {
                    net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(sp, payload);
                }
            }
        }
    }

    public static void removeParticipant(java.util.UUID uuid, net.minecraft.server.MinecraftServer server) {
        boolean changed = false;
        if (net.nostalgia.alphalogic.ritual.RitualActiveState.participants.contains(uuid)) {
            net.nostalgia.alphalogic.ritual.RitualActiveState.participants.remove(uuid);
            changed = true;
        }
        java.util.Iterator<net.minecraft.world.entity.Entity> it = transitioningEntities.iterator();
        while (it.hasNext()) {
            net.minecraft.world.entity.Entity e = it.next();
            if (e.getUUID().equals(uuid)) {
                it.remove();
            }
        }
        readyClients.remove(uuid);
        if (changed && server != null) {
            net.minecraft.server.level.ServerPlayer removed = server.getPlayerList().getPlayer(uuid);
            if (removed != null) {
                net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(removed, new net.nostalgia.network.S2CEndTransitionVisualsPayload());
            }
            java.util.List<java.util.UUID> participantUuids = new java.util.ArrayList<>(net.nostalgia.alphalogic.ritual.RitualActiveState.participants);
            net.nostalgia.network.S2CSyncParticipantsPayload payload = new net.nostalgia.network.S2CSyncParticipantsPayload(participantUuids);
            for (java.util.UUID pid : net.nostalgia.alphalogic.ritual.RitualActiveState.participants) {
                net.minecraft.server.level.ServerPlayer sp = server.getPlayerList().getPlayer(pid);
                if (sp != null) {
                    net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(sp, payload);
                }
            }
        }
    }

    public static int getCurrentSyncPhase() { return currentSyncPhase; }
    public static String getTransitionDimensionId() { return transitionDimensionId; }
    public static net.minecraft.core.BlockPos getTransitionBeaconPos() { return targetBeaconPos; }
    public static net.minecraft.core.BlockPos getTransitionTargetPos() { return transitionTargetPos; }

    public static void clearStateOnServerStop() {
        if (net.fabricmc.loader.api.FabricLoader.getInstance().isModLoaded("sha")) {
            net.sha.api.SHAHologramManager.removeProvider(net.nostalgia.alphalogic.ritual.NostalgiaServerCollisionBypassProvider.INSTANCE);
        }
        currentState = State.INACTIVE;
        ticksActive = 0;
        timeStopStartTime = 0;
        activeRitualMillis = 0;
        lastServerTickMillis = 0;
        targetLevel = null;
        targetBeaconPos = null;
        transitioningEntities.clear();
        transitionTarget = null;
        transitionDimensionId = "";
        transitionTargetPos = null;
        currentSyncPhase = 0;
        phaseStartTime = 0;
        readyClients.clear();
        activeZones.clear();
        net.nostalgia.alphalogic.ritual.RitualActiveState.participants.clear();
        net.nostalgia.alphalogic.ritual.RitualActiveState.ritualCenter = null;
        net.nostalgia.alphalogic.ritual.RitualActiveState.isTransitioning = false;
    }

    public static void handleInterrupt() {
        if (currentState != State.INACTIVE) {
            if (targetLevel != null && targetBeaconPos != null) {
                net.minecraft.world.entity.item.ItemEntity crystal = new net.minecraft.world.entity.item.ItemEntity(
                        targetLevel, 
                        targetBeaconPos.getX() + 0.5, 
                        targetBeaconPos.getY() + 1.5, 
                        targetBeaconPos.getZ() + 0.5, 
                        new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.ECHO_SHARD)
                );
                crystal.setDefaultPickUpDelay();
                targetLevel.addFreshEntity(crystal);
            }
            if (currentState == State.FROZEN || currentState == State.TIME_STOPPING) {
                currentState = State.TIME_RESUMING_DELAY;
                timeStopStartTime = activeRitualMillis;
            } else if (currentState != State.TIME_RESUMING_DELAY && currentState != State.TIME_RESUMING) {
                endRitual();
            }
            if (net.fabricmc.loader.api.FabricLoader.getInstance().isModLoaded("sha")) {
                net.sha.api.SHAHologramManager.removeProvider(net.nostalgia.alphalogic.ritual.NostalgiaServerCollisionBypassProvider.INSTANCE);
            }
            transitioningEntities.clear();
            transitionTarget = null;
            transitionDimensionId = "";
            transitionTargetPos = null;
            currentSyncPhase = 0;
        }
    }

    public static void startTeleportTransition(net.minecraft.server.level.ServerPlayer player, ServerLevel level, String dimensionId) {
        transitioningPlayer = player;
        transitionTarget = level;
        transitionDimensionId = dimensionId;
        currentSyncPhase = 1;
        phaseStartTime = activeRitualMillis;
        readyClients.clear();

        BlockPos safePos = player.blockPosition();
        if (level != null) {
            safePos = net.nostalgia.command.TeleportCommand.findSafeSpawn(level, player.getBlockX(), player.getBlockZ());
        }
        
        transitionTargetPos = safePos;
        
        net.nostalgia.alphalogic.ritual.RitualActiveState.ritualCenter = targetBeaconPos;
        net.nostalgia.alphalogic.ritual.RitualActiveState.offsetX = safePos.getX() - targetBeaconPos.getX();
        net.nostalgia.alphalogic.ritual.RitualActiveState.yOffset = targetBeaconPos.getY() - safePos.getY() - 1;
        net.nostalgia.alphalogic.ritual.RitualActiveState.offsetZ = safePos.getZ() - targetBeaconPos.getZ();
        net.nostalgia.alphalogic.ritual.RitualActiveState.isTransitioning = true;
        
        net.minecraft.world.phys.AABB searchBox = new net.minecraft.world.phys.AABB(targetBeaconPos).inflate(10.0);
        java.util.List<net.minecraft.world.entity.Entity> tempPlayers = new java.util.ArrayList<>();
        java.util.List<net.minecraft.world.entity.Entity> collected = new java.util.ArrayList<>();

        for (net.minecraft.world.entity.player.Player p : player.level().players()) {
            if (p.getBoundingBox().intersects(searchBox)) {
                tempPlayers.add(p);
                collected.add(p);
            }
        }

        for (net.minecraft.world.entity.Entity e : player.level().getEntities((net.minecraft.world.entity.Entity)null, searchBox, e -> !(e instanceof net.minecraft.world.entity.player.Player))) {
            if (e instanceof net.minecraft.world.entity.TamableAnimal tamable) {
                net.minecraft.world.entity.LivingEntity owner = tamable.getOwner();
                if (owner != null && tempPlayers.contains(owner)) {
                    collected.add(e);
                }
            } else if (e instanceof net.minecraft.world.entity.Mob mob) {
                net.minecraft.world.entity.Entity leashHolder = mob.getLeashHolder();
                if (leashHolder != null && tempPlayers.contains(leashHolder)) {
                    collected.add(e);
                }
            }
        }

        java.util.List<net.minecraft.world.entity.Entity> linked = new java.util.ArrayList<>();
        for (net.minecraft.world.entity.Entity e : collected) {
            net.minecraft.world.entity.Entity vehicle = e.getVehicle();
            if (vehicle != null && !collected.contains(vehicle) && !linked.contains(vehicle)) linked.add(vehicle);
            for (net.minecraft.world.entity.Entity pass : e.getPassengers()) {
                if (!collected.contains(pass) && !linked.contains(pass)) linked.add(pass);
            }
        }
        collected.addAll(linked);
        
        transitioningEntities.clear();
        transitioningEntities.addAll(collected);

        net.nostalgia.alphalogic.ritual.RitualActiveState.participants.clear();
        java.util.List<java.util.UUID> participantUuids = new java.util.ArrayList<>();
        for (net.minecraft.world.entity.Entity e : collected) {
            net.nostalgia.alphalogic.ritual.RitualActiveState.participants.add(e.getUUID());
            participantUuids.add(e.getUUID());
        }

        net.nostalgia.network.S2CSyncParticipantsPayload payload = new net.nostalgia.network.S2CSyncParticipantsPayload(participantUuids);
        net.nostalgia.network.S2CBystanderVisualsPayload bystanderPayload = new net.nostalgia.network.S2CBystanderVisualsPayload(
                targetBeaconPos,
                net.nostalgia.alphalogic.ritual.RitualActiveState.offsetX,
                net.nostalgia.alphalogic.ritual.RitualActiveState.yOffset,
                net.nostalgia.alphalogic.ritual.RitualActiveState.offsetZ,
                transitionDimensionId != null ? transitionDimensionId : "",
                currentSyncPhase
        );
        for (net.minecraft.server.level.ServerPlayer sp : ((net.minecraft.server.level.ServerLevel) player.level()).getServer().getPlayerList().getPlayers()) {
            if (sp.level() == player.level()) {
                net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(sp, payload);
                if (!transitioningEntities.contains(sp)) {
                    net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(sp, bystanderPayload);
                }
            }
        }
        currentState = State.REVERSING_TIME;
        
        if (targetBeaconPos != null) {
            ActiveZone activeZone = findZoneByBeacon(targetBeaconPos);
            if (activeZone != null) {
                net.minecraft.server.level.ServerLevel zoneLevel = level.getServer().getLevel(activeZone.dimension);
                if (zoneLevel != null) {
                    removeZone(zoneLevel, activeZone.beaconPos);
                }
            }
        }

        net.minecraft.server.MinecraftServer server = level.getServer();
        if (server != null) {
            server.tickRateManager().setTickRate(20.0f);
            server.tickRateManager().setFrozen(false);
            
            if (transitionTarget != null && transitionTargetPos != null) {
                int vd = server.getPlayerList().getViewDistance();
                transitionTarget.getChunkSource().addTicketWithRadius(
                    net.minecraft.server.level.TicketType.PORTAL,
                    new net.minecraft.world.level.ChunkPos(transitionTargetPos.getX() >> 4, transitionTargetPos.getZ() >> 4),
                    vd
                );
                
                final net.minecraft.server.level.ServerLevel preloadTarget = transitionTarget;
                final net.minecraft.core.BlockPos preloadPos = transitionTargetPos;
                java.util.concurrent.CompletableFuture.runAsync(() -> {
                    int cX = preloadPos.getX() >> 4;
                    int cZ = preloadPos.getZ() >> 4;
                    for (int x = -4; x <= 4; x++) {
                        for (int z = -4; z <= 4; z++) {
                            preloadTarget.getChunk(cX + x, cZ + z, net.minecraft.world.level.chunk.status.ChunkStatus.FULL, true);
                        }
                    }
                });
            }
        }
        
        if (level.dimension() == net.nostalgia.world.dimension.ModDimensions.ALPHA_112_01_LEVEL_KEY) {
            java.util.Map<BlockPos, net.minecraft.world.level.block.state.BlockState> deltas = net.nostalgia.alphalogic.ritual.AlphaWorldData.get(level).getDeltasInRadius(safePos, 300.0);
            long[] positions = new long[deltas.size()];
            int[] states = new int[deltas.size()];
            int idx = 0;
            for (java.util.Map.Entry<BlockPos, net.minecraft.world.level.block.state.BlockState> entry : deltas.entrySet()) {
                BlockPos aPos = entry.getKey();
                BlockPos owPos = new BlockPos(
                    aPos.getX() - net.nostalgia.alphalogic.ritual.RitualActiveState.offsetX,
                    aPos.getY() + net.nostalgia.alphalogic.ritual.RitualActiveState.yOffset,
                    aPos.getZ() - net.nostalgia.alphalogic.ritual.RitualActiveState.offsetZ
                );
                positions[idx] = owPos.asLong();
                states[idx] = net.minecraft.world.level.block.Block.getId(entry.getValue());
                idx++;
            }
            net.nostalgia.network.S2CSyncAlphaDeltasPayload deltaPayloadAlpha = new net.nostalgia.network.S2CSyncAlphaDeltasPayload(positions, states);
            for (net.minecraft.world.entity.Entity e : transitioningEntities) {
                if (e instanceof net.minecraft.server.level.ServerPlayer sp) {
                    net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(sp, deltaPayloadAlpha);
                }
            }
        } else if (level.dimension() == net.minecraft.world.level.Level.OVERWORLD) {
            final net.minecraft.core.BlockPos fSafePos = safePos;
            java.util.concurrent.CompletableFuture.runAsync(() -> {
                int radius = 300;
                java.util.List<net.nostalgia.network.S2COverworldSectionsPayload.SectionData> sectionList = new java.util.ArrayList<>();
                
                int centerCX = fSafePos.getX() >> 4;
                int centerCZ = fSafePos.getZ() >> 4;
                int chunkRadius = (radius >> 4) + 1;
                
                for (int cx = -chunkRadius; cx <= chunkRadius; cx++) {
                    for (int cz = -chunkRadius; cz <= chunkRadius; cz++) {
                        net.minecraft.world.level.chunk.ChunkAccess chunk = level.getChunk(centerCX + cx, centerCZ + cz, net.minecraft.world.level.chunk.status.ChunkStatus.FULL, true);
                        if (chunk != null) {
                            int minSec = chunk.getMinSectionY();
                            int maxSec = chunk.getMaxSectionY();
                            for (int sy = minSec; sy <= maxSec; sy++) {
                                it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap paletteMap = new it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap();
                                it.unimi.dsi.fastutil.ints.IntArrayList paletteList = new it.unimi.dsi.fastutil.ints.IntArrayList();
                                
                                int airId = net.minecraft.world.level.block.Block.getId(net.minecraft.world.level.block.Blocks.AIR.defaultBlockState());
                                paletteMap.put(airId, 0);
                                paletteList.add(airId);
                                
                                byte[] indices = new byte[4096];
                                boolean hasNonAir = false;
                                
                                int sectionMinY = sy << 4;
                                for (int lx = 0; lx < 16; lx++) {
                                    for (int lz = 0; lz < 16; lz++) {
                                        int worldX = chunk.getPos().getMinBlockX() + lx;
                                        int worldZ = chunk.getPos().getMinBlockZ() + lz;
                                        double distSq = Math.pow(worldX - fSafePos.getX(), 2) + Math.pow(worldZ - fSafePos.getZ(), 2);
                                        if (distSq > radius * radius) continue;
                                        
                                        net.minecraft.core.BlockPos.MutableBlockPos samplePos = new net.minecraft.core.BlockPos.MutableBlockPos();
                                        for (int ly = 0; ly < 16; ly++) {
                                            int py = sectionMinY + ly;
                                            
                                            samplePos.set(worldX, py, worldZ);
                                            net.minecraft.world.level.block.state.BlockState state = chunk.getBlockState(samplePos);
                                            if (!state.isAir()) {
                                                int stateId = net.minecraft.world.level.block.Block.getId(state);
                                                int palIdx = paletteMap.getOrDefault(stateId, -1);
                                                if (palIdx == -1) {
                                                    palIdx = paletteList.size();
                                                    if (palIdx < 256) {
                                                        paletteMap.put(stateId, palIdx);
                                                        paletteList.add(stateId);
                                                    } else {
                                                        palIdx = 0;
                                                    }
                                                }
                                                indices[(ly << 8) | (lz << 4) | lx] = (byte) palIdx;
                                                hasNonAir = true;
                                            }
                                        }
                                    }
                                }
                                
                                if (hasNonAir) {
                                    int originalCx = centerCX + cx;
                                    int originalSy = sy;
                                    int originalCz = centerCZ + cz;
                                    
                                    sectionList.add(new net.nostalgia.network.S2COverworldSectionsPayload.SectionData(
                                        originalCx, originalSy, originalCz, paletteList.toIntArray(), indices
                                    ));
                                }
                            }
                            
                            if (!sectionList.isEmpty()) {
                                net.nostalgia.network.S2COverworldSectionsPayload payloadOw = new net.nostalgia.network.S2COverworldSectionsPayload(new java.util.ArrayList<>(sectionList));
                                for (net.minecraft.world.entity.Entity e : transitioningEntities) {
                                    if (e instanceof net.minecraft.server.level.ServerPlayer sp) {
                                        net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(sp, payloadOw);
                                    }
                                }
                                sectionList.clear();
                            }
                        }
                    }
                }
            });
        }
        net.minecraft.server.level.ServerLevel tl = level.getServer().getLevel(net.minecraft.resources.ResourceKey.create(
            net.minecraft.core.registries.Registries.DIMENSION, net.minecraft.resources.Identifier.tryParse(dimensionId)));
        long currentSeed = tl != null ? tl.getSeed() : level.getSeed();
        
        net.nostalgia.network.S2CStartTransitionVisualsPayload startPayload = new net.nostalgia.network.S2CStartTransitionVisualsPayload(dimensionId, targetBeaconPos, safePos, currentSeed);
        for (net.minecraft.world.entity.Entity e : transitioningEntities) {
            if (e instanceof net.minecraft.server.level.ServerPlayer sp) {
                net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(sp, startPayload);
            }
        }
    }

    public static boolean isVisualReversing() {
        if (net.fabricmc.loader.api.FabricLoader.getInstance().getEnvironmentType() == net.fabricmc.api.EnvType.CLIENT) {
            return getClientVisualReversing();
        }
        return currentState == State.REVERSING_TIME;
    }

    @net.fabricmc.api.Environment(net.fabricmc.api.EnvType.CLIENT)
    private static boolean getClientVisualReversing() {
        net.nostalgia.alphalogic.ritual.event.ClientTransitionView t = net.nostalgia.client.ritual.ClientRitualEventRegistry.activeTransition();
        return t != null && !t.isBystander();
    }

    public static net.minecraft.core.BlockPos getVisualCenter() {
        if (net.fabricmc.loader.api.FabricLoader.getInstance().getEnvironmentType() == net.fabricmc.api.EnvType.CLIENT) {
            return getClientVisualCenter();
        }
        return targetBeaconPos;
    }

    @net.fabricmc.api.Environment(net.fabricmc.api.EnvType.CLIENT)
    private static net.minecraft.core.BlockPos getClientVisualCenter() {
        net.nostalgia.alphalogic.ritual.event.ClientTransitionView t = net.nostalgia.client.ritual.ClientRitualEventRegistry.activeTransition();
        return t != null ? t.ritualCenter() : null;
    }
}
