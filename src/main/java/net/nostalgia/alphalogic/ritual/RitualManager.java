package net.nostalgia.alphalogic.ritual;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.nostalgia.alphalogic.ritual.event.RitualEventRegistry;

public class RitualManager {
    public enum State {
        INACTIVE,
        TIME_STOPPING,
        FROZEN,
        REVERSING_TIME,
        TIME_RESUMING_DELAY,
        TIME_RESUMING
    }

    static ServerLevel targetLevel;
    static BlockPos targetBeaconPos;

    public static long activeRitualMillis = 0;
    private static long lastServerTickMillis = 0;

    public static void markClientReady(java.util.UUID uuid) {
        RitualEventRegistry.markClientReady(uuid);
    }

    public static State getClientState() { return RitualEventRegistry.state(); }
    public static void setClientState(State state) { RitualEventRegistry.setState(state); }
    public static boolean isServerTransitioning() { return getClientState() == State.REVERSING_TIME; }
    public static boolean isServerActive() { return getClientState() != State.INACTIVE; }
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
                    if (targetBeaconPos != null && targetBeaconPos.equals(bPos) && getClientState() != State.INACTIVE) {
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
                            if (targetBeaconPos != null && targetBeaconPos.equals(bPos) && getClientState() != State.INACTIVE) {
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

    public static void transitionToFrozenForInstance(TransitionEventInstance inst) {
        ServerLevel src = inst.sourceLevel();
        BlockPos beacon = inst.beaconPos();
        if (src != null) {
            src.getServer().tickRateManager().setTickRate(20.0f);
            if (beacon != null) {
                if (src.tickRateManager() instanceof net.nostalgia.alphalogic.ritual.TickRateManagerAccess access) {
                    access.nostalgia$addRegion(new net.nostalgia.alphalogic.ritual.FreezeRegion(src.dimension(), beacon, ZONE_RADIUS_CHUNKS));
                }
                if (findZoneByBeacon(beacon) == null) {
                    addZone(src, beacon, false);
                }
            }
        }
        inst.setState(State.INACTIVE);
        targetLevel = null;
        targetBeaconPos = null;
    }

    public static void triggerTimeResumeForInstance(TransitionEventInstance inst) {
        State s = inst.state();
        if (s != State.FROZEN && s != State.TIME_STOPPING && s != State.TIME_RESUMING_DELAY) return;
        inst.setState(State.TIME_RESUMING);
        inst.setTimeStopStartTime(inst.activeMs());
        ServerLevel src = inst.sourceLevel();
        if (src != null) {
            src.getServer().tickRateManager().setFrozen(false);
            src.getServer().tickRateManager().setTickRate(1.0f);
        }
    }

    public static void endRitualForInstance(TransitionEventInstance inst) {
        if (net.fabricmc.loader.api.FabricLoader.getInstance().isModLoaded("sha")) {
            net.sha.api.SHAHologramManager.removeProvider(net.nostalgia.alphalogic.ritual.NostalgiaServerCollisionBypassProvider.INSTANCE);
        }
        ServerLevel src = inst.sourceLevel();
        BlockPos beacon = inst.beaconPos();
        if (src != null && beacon != null) {
            removeZone(src, beacon);
        }
        if (src != null) {
            src.getServer().tickRateManager().setTickRate(20.0f);
            src.getServer().tickRateManager().setFrozen(false);
        }
        RitualEventRegistry.endEvent(inst.id());
    }

    public static void init() {
        if (net.fabricmc.loader.api.FabricLoader.getInstance().isModLoaded("sha")) {
            net.sha.api.SHAHologramManager.ignorePredicate = (java.util.function.Predicate<net.minecraft.world.entity.Entity>) (entity -> {
                if (entity instanceof net.minecraft.world.entity.item.ItemEntity) return false;
                return !RitualEventRegistry.isParticipant(entity);
            });
        }

        ServerTickEvents.START_SERVER_TICK.register(server -> {
            tickActiveZones(server);
            long nowMs = System.currentTimeMillis();
            long dt = lastServerTickMillis == 0 ? 50 : (nowMs - lastServerTickMillis);
            lastServerTickMillis = nowMs;

            for (TransitionEventInstance inst : new java.util.ArrayList<>(RitualEventRegistry.allInstances())) {
                inst.tick(dt, server);
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
        if (getClientState() != State.INACTIVE) return;
        if (findZoneByBeacon(beaconPos) != null) return;
        targetLevel = level;
        targetBeaconPos = beaconPos;
        targetLevel.getServer().setWeatherParameters(6000, 0, false, false);
        RitualEventRegistry.startEvent(beaconPos, level);
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
        if (getClientState() != State.INACTIVE) return;
        targetLevel = level;
        if (RitualEventRegistry.activeInstance() == null) {
            RitualEventRegistry.startEvent(targetBeaconPos, level);
        }
        targetLevel.getServer().tickRateManager().setFrozen(false);
        targetLevel.getServer().setWeatherParameters(6000, 0, false, false);
        RitualEventRegistry.setState(State.TIME_STOPPING);
        RitualEventRegistry.setTimeStopStartTime(activeRitualMillis);
    }

    public static void triggerTimeResume() {
        State s = getClientState();
        if (s != State.FROZEN && s != State.TIME_STOPPING && s != State.TIME_RESUMING_DELAY) return;
        RitualEventRegistry.setState(State.TIME_RESUMING);
        RitualEventRegistry.setTimeStopStartTime(activeRitualMillis);
        if (targetLevel != null) {
            targetLevel.getServer().tickRateManager().setFrozen(false);
            targetLevel.getServer().tickRateManager().setTickRate(1.0f);
        }
    }

    private static void transitionToTimeStop() {
        RitualEventRegistry.setState(State.TIME_STOPPING);
        RitualEventRegistry.setTimeStopStartTime(activeRitualMillis);
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
        RitualEventRegistry.setState(State.INACTIVE);
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
        if (targetLevel != null) {
            targetLevel.getServer().tickRateManager().setTickRate(20.0f);
            targetLevel.getServer().tickRateManager().setFrozen(false);
        }
        RitualEventRegistry.endEvent();
    }

    public static void handlePlayerDisconnect(net.minecraft.server.level.ServerPlayer player) {
        RitualEventRegistry.readyClients().remove(player.getUUID());
        RitualEventRegistry.entities().remove(player);
        if (RitualEventRegistry.participants().contains(player.getUUID())) {
            RitualEventRegistry.removeParticipantUuid(player.getUUID());
            java.util.List<java.util.UUID> participantUuids = new java.util.ArrayList<>(RitualEventRegistry.participants());
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
        if (RitualEventRegistry.participants().contains(uuid)) {
            RitualEventRegistry.removeParticipantUuid(uuid);
            changed = true;
        }
        java.util.Iterator<net.minecraft.world.entity.Entity> it = RitualEventRegistry.entities().iterator();
        while (it.hasNext()) {
            net.minecraft.world.entity.Entity e = it.next();
            if (e.getUUID().equals(uuid)) {
                it.remove();
            }
        }
        RitualEventRegistry.readyClients().remove(uuid);
        if (changed && server != null) {
            net.minecraft.server.level.ServerPlayer removed = server.getPlayerList().getPlayer(uuid);
            if (removed != null) {
                net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(removed, new net.nostalgia.network.S2CEndTransitionVisualsPayload());
            }
            java.util.List<java.util.UUID> participantUuids = new java.util.ArrayList<>(RitualEventRegistry.participants());
            net.nostalgia.network.S2CSyncParticipantsPayload payload = new net.nostalgia.network.S2CSyncParticipantsPayload(participantUuids);
            for (java.util.UUID pid : RitualEventRegistry.participants()) {
                net.minecraft.server.level.ServerPlayer sp = server.getPlayerList().getPlayer(pid);
                if (sp != null) {
                    net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(sp, payload);
                }
            }
        }
    }

    public static int getCurrentSyncPhase() { return RitualEventRegistry.currentSyncPhase(); }
    public static String getTransitionDimensionId() { return RitualEventRegistry.transitionDimensionId(); }
    public static net.minecraft.core.BlockPos getTransitionBeaconPos() { return targetBeaconPos; }
    public static net.minecraft.core.BlockPos getTransitionTargetPos() { return RitualEventRegistry.transitionTargetPos(); }

    public static void clearStateOnServerStop() {
        if (net.fabricmc.loader.api.FabricLoader.getInstance().isModLoaded("sha")) {
            net.sha.api.SHAHologramManager.removeProvider(net.nostalgia.alphalogic.ritual.NostalgiaServerCollisionBypassProvider.INSTANCE);
        }
        activeRitualMillis = 0;
        lastServerTickMillis = 0;
        targetLevel = null;
        targetBeaconPos = null;
        activeZones.clear();
        RitualEventRegistry.endAllEvents();
    }

    public static void handleInterrupt() {
        State state = getClientState();
        if (state != State.INACTIVE) {
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
            if (state == State.FROZEN || state == State.TIME_STOPPING) {
                RitualEventRegistry.setState(State.TIME_RESUMING_DELAY);
                RitualEventRegistry.setTimeStopStartTime(activeRitualMillis);
            } else if (state != State.TIME_RESUMING_DELAY && state != State.TIME_RESUMING) {
                endRitual();
            }
            if (net.fabricmc.loader.api.FabricLoader.getInstance().isModLoaded("sha")) {
                net.sha.api.SHAHologramManager.removeProvider(net.nostalgia.alphalogic.ritual.NostalgiaServerCollisionBypassProvider.INSTANCE);
            }
            RitualEventRegistry.entities().clear();
            RitualEventRegistry.setCurrentSyncPhase(0);
        }
    }

    public static void startTeleportTransition(net.minecraft.server.level.ServerPlayer player, ServerLevel level, String dimensionId) {
        TransitionEventInstance inst = RitualEventRegistry.findInstanceByBeacon(targetBeaconPos);
        if (inst == null) {
            inst = RitualEventRegistry.startEvent(targetBeaconPos, (ServerLevel) player.level());
        }
        inst.setTargetServerLevel(level);
        inst.setTargetDimensionId(dimensionId);
        inst.setPhase(1);
        inst.setPhaseStartTime(inst.activeMs());
        inst.readyClients().clear();

        BlockPos safePos = player.blockPosition();
        if (level != null) {
            safePos = net.nostalgia.command.TeleportCommand.findSafeSpawn(level, player.getBlockX(), player.getBlockZ());
        }

        inst.setTargetPos(safePos);

        inst.setBeaconPos(targetBeaconPos);
        inst.setOffsets(
            safePos.getX() - targetBeaconPos.getX(),
            targetBeaconPos.getY() - safePos.getY() - 1,
            safePos.getZ() - targetBeaconPos.getZ()
        );
        inst.setTransitioning(true);

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

        inst.entities().clear();
        inst.entities().addAll(collected);

        inst.participants().clear();
        java.util.List<java.util.UUID> participantUuids = new java.util.ArrayList<>();
        for (net.minecraft.world.entity.Entity e : collected) {
            inst.participants().add(e.getUUID());
            participantUuids.add(e.getUUID());
        }

        net.minecraft.server.level.ServerLevel tlEarly = level.getServer().getLevel(net.minecraft.resources.ResourceKey.create(
            net.minecraft.core.registries.Registries.DIMENSION, net.minecraft.resources.Identifier.tryParse(dimensionId)));
        long currentSeedEarly = tlEarly != null ? tlEarly.getSeed() : level.getSeed();
        net.nostalgia.network.S2CStartTransitionVisualsPayload startPayloadEarly = new net.nostalgia.network.S2CStartTransitionVisualsPayload(dimensionId, targetBeaconPos, safePos, currentSeedEarly);
        for (net.minecraft.world.entity.Entity e : inst.entities()) {
            if (e instanceof net.minecraft.server.level.ServerPlayer sp) {
                net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(sp, startPayloadEarly);
            }
        }

        net.nostalgia.network.S2CSyncParticipantsPayload payload = new net.nostalgia.network.S2CSyncParticipantsPayload(participantUuids);
        net.nostalgia.network.S2CBystanderVisualsPayload bystanderPayload = new net.nostalgia.network.S2CBystanderVisualsPayload(
                targetBeaconPos,
                inst.offsetX(),
                inst.yOffset(),
                inst.offsetZ(),
                inst.targetDimensionId() != null ? inst.targetDimensionId() : "",
                inst.phase()
        );
        for (net.minecraft.server.level.ServerPlayer sp : ((net.minecraft.server.level.ServerLevel) player.level()).getServer().getPlayerList().getPlayers()) {
            if (sp.level() == player.level()) {
                net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(sp, payload);
                if (!inst.entities().contains(sp)) {
                    net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(sp, bystanderPayload);
                }
            }
        }
        inst.setState(State.REVERSING_TIME);

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

            net.minecraft.server.level.ServerLevel transitionTarget = inst.targetServerLevel();
            BlockPos transitionTargetPos = inst.targetPos();
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
                    aPos.getX() - inst.offsetX(),
                    aPos.getY() + inst.yOffset(),
                    aPos.getZ() - inst.offsetZ()
                );
                positions[idx] = owPos.asLong();
                states[idx] = net.minecraft.world.level.block.Block.getId(entry.getValue());
                idx++;
            }
            net.nostalgia.network.S2CSyncAlphaDeltasPayload deltaPayloadAlpha = new net.nostalgia.network.S2CSyncAlphaDeltasPayload(positions, states);
            for (net.minecraft.world.entity.Entity e : inst.entities()) {
                if (e instanceof net.minecraft.server.level.ServerPlayer sp) {
                    net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(sp, deltaPayloadAlpha);
                }
            }
        } else if (level.dimension() == net.minecraft.world.level.Level.OVERWORLD) {
            final net.minecraft.core.BlockPos fSafePos = safePos;
            final TransitionEventInstance owInst = inst;
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
                                for (net.minecraft.world.entity.Entity e : owInst.entities()) {
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
    }

    public static boolean isVisualReversing() {
        if (net.fabricmc.loader.api.FabricLoader.getInstance().getEnvironmentType() == net.fabricmc.api.EnvType.CLIENT) {
            return getClientVisualReversing();
        }
        return getClientState() == State.REVERSING_TIME;
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
