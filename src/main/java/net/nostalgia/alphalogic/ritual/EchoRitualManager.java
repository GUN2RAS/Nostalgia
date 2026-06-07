package net.nostalgia.alphalogic.ritual;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.nostalgia.alphalogic.ritual.event.RitualEventRegistry;

public class EchoRitualManager {
    public enum State {
        INACTIVE,
        TIME_STOPPING,
        FROZEN,
        REVERSING_TIME,
        TIME_RESUMING_DELAY,
        TIME_RESUMING
    }

    public static long activeRitualMillis = 0;
    private static long lastServerTickMillis = 0;

    private static final java.util.concurrent.ConcurrentHashMap<java.util.UUID, BlockPos> selectedBeacons = new java.util.concurrent.ConcurrentHashMap<>();
    public static final java.util.concurrent.ConcurrentHashMap<java.util.UUID, String> playerReturnDimensions = new java.util.concurrent.ConcurrentHashMap<>();

    public static void selectBeacon(java.util.UUID playerUuid, BlockPos pos) {
        if (playerUuid == null || pos == null) return;
        selectedBeacons.put(playerUuid, pos.immutable());
    }

    public static BlockPos getSelectedBeacon(java.util.UUID playerUuid) {
        return playerUuid != null ? selectedBeacons.get(playerUuid) : null;
    }

    public static void clearSelection(java.util.UUID playerUuid) {
        if (playerUuid != null) selectedBeacons.remove(playerUuid);
    }

    public static void markClientReady(java.util.UUID uuid) {
        RitualEventRegistry.markClientReady(uuid);
    }

    public static State getClientState() { return RitualEventRegistry.state(); }
    public static void setClientState(State state) { RitualEventRegistry.setState(state); }
    public static boolean isServerTransitioning() { return getClientState() == State.REVERSING_TIME; }
    public static boolean isServerActive() { return getClientState() != State.INACTIVE; }

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

    public static double calculateSafeYAndApplyEffects(net.minecraft.world.entity.Entity entity, ServerLevel targetLevel, double targetX, double targetY, double targetZ) {
        net.minecraft.core.BlockPos.MutableBlockPos pos = new net.minecraft.core.BlockPos.MutableBlockPos(targetX, targetY + 0.1, targetZ);
        
        boolean inBlock = false;
        while (pos.getY() < targetLevel.getMaxY()) {
            net.minecraft.world.level.block.state.BlockState feetState = targetLevel.getBlockState(pos);
            net.minecraft.world.level.block.state.BlockState headState = targetLevel.getBlockState(pos.above());
            
            boolean feetSolid = !feetState.getCollisionShape(targetLevel, pos).isEmpty() || !feetState.getFluidState().isEmpty();
            boolean headSolid = !headState.getCollisionShape(targetLevel, pos.above()).isEmpty() || !headState.getFluidState().isEmpty();

            if (feetSolid || headSolid) {
                inBlock = true;
                pos.move(net.minecraft.core.Direction.UP);
            } else {
                break;
            }
        }
        
        if (inBlock) {
            entity.setDeltaMovement(entity.getDeltaMovement().add(0, 0.6, 0));
            return pos.getY();
        }
        
        net.minecraft.core.BlockPos.MutableBlockPos downPos = new net.minecraft.core.BlockPos.MutableBlockPos(targetX, targetY - 1, targetZ);
        boolean foundGround = false;
        for (int i = 0; i < 4; i++) {
            net.minecraft.world.level.block.state.BlockState state = targetLevel.getBlockState(downPos);
            if (!state.getCollisionShape(targetLevel, downPos).isEmpty() || state.is(net.minecraft.world.level.block.Blocks.WATER)) {
                foundGround = true;
                break;
            }
            downPos.move(net.minecraft.core.Direction.DOWN);
        }
        
        if (!foundGround) {
            if (entity instanceof net.minecraft.world.entity.LivingEntity living) {
                living.addEffect(new net.minecraft.world.effect.MobEffectInstance(net.minecraft.world.effect.MobEffects.SLOW_FALLING, 100, 0, false, false));
                living.addEffect(new net.minecraft.world.effect.MobEffectInstance(net.minecraft.world.effect.MobEffects.RESISTANCE, 100, 4, false, false));
            }
        }
        
        return targetY;
    }

    private static boolean anyInstanceSlowingTime(EchoRitualEventInstance exclude) {
        for (EchoRitualEventInstance i : RitualEventRegistry.allInstances()) {
            if (i == exclude) continue;
            State s = i.state();
            if (s == State.TIME_STOPPING || s == State.FROZEN || s == State.TIME_RESUMING_DELAY || s == State.TIME_RESUMING) {
                return true;
            }
        }
        return false;
    }

    public static void transitionToFrozenForInstance(EchoRitualEventInstance inst) {
        ServerLevel src = inst.sourceLevel();
        BlockPos beacon = inst.beaconPos();
        if (src != null && beacon != null) {
            if (src.tickRateManager() instanceof net.nostalgia.alphalogic.ritual.TickRateManagerAccess access) {
                access.nostalgia$addRegion(new net.nostalgia.alphalogic.ritual.FreezeRegion(src.dimension(), beacon, TimestopZoneManager.ZONE_RADIUS_CHUNKS));
            }
            if (TimestopZoneManager.findZoneByBeacon(beacon) == null) {
                TimestopZoneManager.addZone(src, beacon, false);
            }
        }
        inst.setState(State.FROZEN);
    }

    public static void triggerTimeResumeForInstance(EchoRitualEventInstance inst) {
        State s = inst.state();
        if (s != State.FROZEN && s != State.TIME_STOPPING && s != State.TIME_RESUMING_DELAY) return;
        inst.setState(State.TIME_RESUMING);
        inst.setTimeStopStartTime(inst.activeMs());
        ServerLevel src = inst.sourceLevel();
        if (src != null) {
            src.getServer().tickRateManager().setFrozen(false);
        }
    }

    public static void endRitualForInstance(EchoRitualEventInstance inst) {
        if (net.fabricmc.loader.api.FabricLoader.getInstance().isModLoaded("sha")) {
            net.sha.api.SHAHologramManager.removeProvider(net.nostalgia.alphalogic.ritual.NostalgiaServerCollisionBypassProvider.INSTANCE);
        }
        ServerLevel src = inst.sourceLevel();
        BlockPos beacon = inst.beaconPos();
        if (src != null && beacon != null) {
            TimestopZoneManager.removeZone(src, beacon);
        }
        if (src != null && !anyInstanceSlowingTime(inst)) {
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
            TimestopZoneManager.tickActiveZones(server);
            long nowMs = System.currentTimeMillis();
            long dt = lastServerTickMillis == 0 ? 50 : (nowMs - lastServerTickMillis);
            lastServerTickMillis = nowMs;

            for (EchoRitualEventInstance inst : new java.util.ArrayList<>(RitualEventRegistry.allInstances())) {
                inst.tick(dt, server);
            }
        });
    }



    public static void startRitual(ServerLevel level, BlockPos beaconPos) {
        if (RitualEventRegistry.findInstanceByBeacon(beaconPos) != null) return;
        if (TimestopZoneManager.findZoneByBeacon(beaconPos) != null) return;
        level.getServer().setWeatherParameters(6000, 0, false, false);
        EchoRitualEventInstance newInst = RitualEventRegistry.startEvent(beaconPos, level);
        newInst.setState(State.TIME_STOPPING);
        newInst.setTimeStopStartTime(newInst.activeMs());

        TimestopZoneManager.addZone(level, beaconPos, false);

        long gameTime = level.getGameTime();
        long clockTicks = level.getDefaultClockTime();
        float rain = level.getRainLevel(1.0f);
        float thunder = level.getThunderLevel(1.0f);

        net.nostalgia.network.S2CTimestopZoneStartPayload payload = new net.nostalgia.network.S2CTimestopZoneStartPayload(
                beaconPos, TimestopZoneManager.ZONE_RADIUS_CHUNKS, level.dimension().identifier().toString(),
                false, gameTime, clockTicks, rain, thunder);
        for (net.minecraft.server.level.ServerPlayer sp : level.getServer().getPlayerList().getPlayers()) {
            if (sp.level() == level) {
                net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(sp, payload);
            }
        }
        net.minecraft.world.level.block.state.BlockState bs = level.getBlockState(beaconPos);
        level.sendBlockUpdated(beaconPos, bs, bs, 3);
    }



    public static void triggerTimeStop(ServerLevel level, BlockPos beaconPos) {
        if (beaconPos == null) return;
        EchoRitualEventInstance inst = RitualEventRegistry.findInstanceByBeacon(beaconPos);
        if (inst == null) {
            inst = RitualEventRegistry.startEvent(beaconPos, level);
        }
        if (inst.state() != State.INACTIVE) return;
        level.getServer().tickRateManager().setFrozen(false);
        level.getServer().setWeatherParameters(6000, 0, false, false);
        inst.setState(State.TIME_STOPPING);
        inst.setTimeStopStartTime(inst.activeMs());
    }

    public static void triggerTimeResume(BlockPos beaconPos) {
        if (beaconPos == null) return;
        EchoRitualEventInstance inst = RitualEventRegistry.findInstanceByBeacon(beaconPos);
        if (inst == null) return;
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

    public static void handlePlayerDisconnect(net.minecraft.server.level.ServerPlayer player) {
        java.util.UUID uuid = player.getUUID();
        clearSelection(uuid);
        EchoRitualEventInstance inst = RitualEventRegistry.findInstanceForParticipant(uuid);
        if (inst == null) return;
        inst.readyClients().remove(uuid);
        inst.entities().remove(player);
        inst.participants().remove(uuid);
        java.util.List<java.util.UUID> participantUuids = new java.util.ArrayList<>(inst.participants());
        net.nostalgia.network.S2CSyncParticipantsPayload payload = new net.nostalgia.network.S2CSyncParticipantsPayload(participantUuids);
        ServerLevel src = inst.sourceLevel();
        if (src != null && src.getServer() != null) {
            for (net.minecraft.server.level.ServerPlayer sp : src.getServer().getPlayerList().getPlayers()) {
                net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(sp, payload);
            }
        }
    }

    public static void removeParticipant(java.util.UUID uuid, net.minecraft.server.MinecraftServer server) {
        EchoRitualEventInstance inst = RitualEventRegistry.findInstanceForParticipant(uuid);
        if (inst == null) return;
        boolean changed = inst.participants().remove(uuid);
        java.util.Iterator<net.minecraft.world.entity.Entity> it = inst.entities().iterator();
        while (it.hasNext()) {
            net.minecraft.world.entity.Entity e = it.next();
            if (e.getUUID().equals(uuid)) {
                it.remove();
            }
        }
        inst.readyClients().remove(uuid);
        if (changed && server != null) {
            net.minecraft.server.level.ServerPlayer removed = server.getPlayerList().getPlayer(uuid);
            if (removed != null) {
                net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(removed, new net.nostalgia.network.S2CEndTransitionVisualsPayload(inst.id()));
            }
            java.util.List<java.util.UUID> participantUuids = new java.util.ArrayList<>(inst.participants());
            net.nostalgia.network.S2CSyncParticipantsPayload payload = new net.nostalgia.network.S2CSyncParticipantsPayload(participantUuids);
            for (java.util.UUID pid : inst.participants()) {
                net.minecraft.server.level.ServerPlayer sp = server.getPlayerList().getPlayer(pid);
                if (sp != null) {
                    net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(sp, payload);
                }
            }
        }
    }

    public static int getCurrentSyncPhase() { return RitualEventRegistry.currentSyncPhase(); }
    public static String getTransitionDimensionId() { return RitualEventRegistry.transitionDimensionId(); }
    public static net.minecraft.core.BlockPos getTransitionTargetPos() { return RitualEventRegistry.transitionTargetPos(); }

    public static void clearStateOnServerStop() {
        if (net.fabricmc.loader.api.FabricLoader.getInstance().isModLoaded("sha")) {
            net.sha.api.SHAHologramManager.removeProvider(net.nostalgia.alphalogic.ritual.NostalgiaServerCollisionBypassProvider.INSTANCE);
        }
        activeRitualMillis = 0;
        lastServerTickMillis = 0;
        selectedBeacons.clear();
        TimestopZoneManager.activeZones.clear();
        RitualEventRegistry.endAllEvents();
    }

    public static void handleInterrupt(BlockPos beaconPos) {
        if (beaconPos == null) return;
        EchoRitualEventInstance inst = RitualEventRegistry.findInstanceByBeacon(beaconPos);
        if (inst == null) return;
        State state = inst.state();
        if (state == State.INACTIVE) return;

        ServerLevel src = inst.sourceLevel();
        if (src != null) {
            net.minecraft.world.entity.item.ItemEntity crystal = new net.minecraft.world.entity.item.ItemEntity(
                    src,
                    beaconPos.getX() + 0.5,
                    beaconPos.getY() + 1.5,
                    beaconPos.getZ() + 0.5,
                    new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.ECHO_SHARD)
            );
            crystal.setDefaultPickUpDelay();
            src.addFreshEntity(crystal);
        }
        if (state == State.FROZEN || state == State.TIME_STOPPING) {
            inst.setState(State.TIME_RESUMING_DELAY);
            inst.setTimeStopStartTime(inst.activeMs());
        } else if (state != State.TIME_RESUMING_DELAY && state != State.TIME_RESUMING) {
            endRitualForInstance(inst);
        }
        if (net.fabricmc.loader.api.FabricLoader.getInstance().isModLoaded("sha")) {
            net.sha.api.SHAHologramManager.removeProvider(net.nostalgia.alphalogic.ritual.NostalgiaServerCollisionBypassProvider.INSTANCE);
        }
        inst.entities().clear();
        inst.setPhase(0);
    }

    public static void startTeleportTransition(net.minecraft.server.level.ServerPlayer player, ServerLevel level, String dimensionId, BlockPos beaconPos) {
        if (beaconPos == null) return;

        // Закрываем активный скайпортал если он открыт — иначе он пролезет в RD
        SkyPortalEventInstance portalInst = SkyPortalManager.getActive();
        if (portalInst != null) {
            net.minecraft.server.MinecraftServer server = ((ServerLevel) player.level()).getServer();
            SkyPortalManager.stop(server, portalInst.sourceDimension());
            net.nostalgia.network.S2CSkyPortalPayload closePayload = new net.nostalgia.network.S2CSkyPortalPayload(
                false, 256, 256, false, 0L, portalInst.center(),
                portalInst.sourceDimension(), portalInst.targetDimension());
            for (net.minecraft.server.level.ServerPlayer p : server.getPlayerList().getPlayers()) {
                net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(p, closePayload);
            }
        }

        final BlockPos originalBeaconPos = beaconPos;
        EchoRitualEventInstance inst = RitualEventRegistry.findInstanceByBeacon(beaconPos);
        if (inst == null) {
            inst = RitualEventRegistry.startEvent(beaconPos, (ServerLevel) player.level());
        }
        inst.setTargetServerLevel(level);
        inst.setTargetDimensionId(dimensionId);
        if (inst.state() == State.INACTIVE) {
            inst.setState(State.FROZEN);
        }
        inst.setPhase(1);
        inst.setPhaseStartTime(inst.activeMs());
        inst.readyClients().clear();

        int targetSurfaceY = beaconPos.getY();
        if (level != null) {
            level.getChunk(beaconPos.getX() >> 4, beaconPos.getZ() >> 4, net.minecraft.world.level.chunk.status.ChunkStatus.FULL, true);
            targetSurfaceY = net.nostalgia.command.TeleportCommand.getSurfaceY(level, beaconPos.getX(), beaconPos.getZ(), true) + 1;
        }

        int offsetX = 0;
        int offsetZ = 0;
        int offsetY = beaconPos.getY() - targetSurfaceY;

        BlockPos playerSafePos = player.blockPosition();
        boolean isEscapingRD = player.level().dimension() == net.nostalgia.world.dimension.ModDimensions.RD_132211_LEVEL_KEY;
        if (isEscapingRD) {
            BlockPos rdOrigBeaconPos = beaconPos;
            beaconPos = playerSafePos;
            offsetX = rdOrigBeaconPos.getX() - playerSafePos.getX();
            offsetZ = rdOrigBeaconPos.getZ() - playerSafePos.getZ();
            offsetY = playerSafePos.getY() - targetSurfaceY;
        }

        boolean isHeadingToRD = DimensionUtil.isRD(dimensionId);
        if (isHeadingToRD) {
            playerReturnDimensions.put(player.getUUID(), player.level().dimension().identifier().toString());
            playerSafePos = new BlockPos(128, 43, 128);
            offsetX = 128 - beaconPos.getX();
            offsetZ = 128 - beaconPos.getZ();
            offsetY = beaconPos.getY() - 43;
        } else if (level != null) {
            level.getChunk(player.getBlockX() >> 4, player.getBlockZ() >> 4, net.minecraft.world.level.chunk.status.ChunkStatus.FULL, true);
            int pY = net.nostalgia.command.TeleportCommand.getSurfaceY(level, player.getBlockX(), player.getBlockZ());
            playerSafePos = new BlockPos(player.getBlockX(), pY, player.getBlockZ());
        }

        inst.setTargetPos(playerSafePos);
        inst.setBeaconPos(beaconPos);
        inst.setOffsets(
            offsetX,
            offsetY,
            offsetZ
        );
        inst.setTransitioning(true);

        ServerLevel sourceLevel = (ServerLevel) player.level();
        BlockPos srcAnchor = beaconPos.below();
        net.minecraft.world.level.block.state.BlockState bState = player.level().getBlockState(beaconPos);
        net.minecraft.world.level.block.state.BlockState aState = player.level().getBlockState(beaconPos.below());

        net.minecraft.world.level.block.entity.BlockEntity be = sourceLevel.getBlockEntity(beaconPos);
        if (be != null) {
            inst.setBeaconNbt(be.saveWithFullMetadata(sourceLevel.registryAccess()));
        }

        TimestopZoneManager.ActiveZone activeZone = TimestopZoneManager.findZoneByBeacon(originalBeaconPos);
        if (activeZone != null) {
            net.minecraft.server.level.ServerLevel zoneLevel = sourceLevel.getServer().getLevel(activeZone.dimension());
            if (zoneLevel != null) {
                TimestopZoneManager.removeZone(zoneLevel, activeZone.beaconPos());
            }
        }

        if (!isEscapingRD && !isHeadingToRD) {
            inst.cachePut(beaconPos, bState);
            inst.cachePut(beaconPos.below(), aState);
        }

        net.minecraft.world.phys.AABB searchBox = new net.minecraft.world.phys.AABB(beaconPos).inflate(10.0);
        java.util.List<net.minecraft.world.entity.Entity> tempPlayers = new java.util.ArrayList<>();
        java.util.List<net.minecraft.world.entity.Entity> collected = new java.util.ArrayList<>();
        if (isEscapingRD) {
            for (net.minecraft.world.entity.player.Player p : player.level().players()) {
                tempPlayers.add(p);
                collected.add(p);
            }
        } else {
            for (net.minecraft.server.level.ServerPlayer p : sourceLevel.players()) {
                if (p.getBoundingBox().intersects(searchBox)) {
                    tempPlayers.add(p);
                    collected.add(p);
                }
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
        
        int targetSkyColor = -1;
        int targetFogColor = -1;
        if (tlEarly != null && playerSafePos != null) {
            targetSkyColor = tlEarly.environmentAttributes().getValue(net.minecraft.world.attribute.EnvironmentAttributes.SKY_COLOR, playerSafePos.getCenter(), null);
            targetFogColor = tlEarly.environmentAttributes().getValue(net.minecraft.world.attribute.EnvironmentAttributes.FOG_COLOR, playerSafePos.getCenter(), null);
        }

        net.nostalgia.network.S2CStartTransitionVisualsPayload startPayloadEarly = new net.nostalgia.network.S2CStartTransitionVisualsPayload(inst.id(), dimensionId, beaconPos, playerSafePos, inst.offsetX(), inst.yOffset(), inst.offsetZ(), currentSeedEarly, targetSkyColor, targetFogColor, net.minecraft.world.level.block.Block.getId(bState), net.minecraft.world.level.block.Block.getId(aState));
        for (net.minecraft.world.entity.Entity e : inst.entities()) {
            if (e instanceof net.minecraft.server.level.ServerPlayer sp) {
                net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(sp, startPayloadEarly);
            }
        }

        if (!isEscapingRD && !isHeadingToRD) {
            String targetDimId = inst.targetDimensionId() != null ? inst.targetDimensionId() : inst.targetServerLevel() != null ? inst.targetServerLevel().dimension().identifier().toString() : level.dimension().identifier().toString();
            BlockPos targetBeaconPos = CoordinateMapper.sourceToTarget(beaconPos, inst.offsetX(), inst.yOffset(), inst.offsetZ(), false, 320);
            net.nostalgia.alphalogic.ritual.DeltaSyncService.broadcastSingleDelta(level.getServer(), targetBeaconPos, bState, targetDimId, null);
            net.nostalgia.alphalogic.ritual.DeltaSyncService.broadcastSingleDelta(level.getServer(), targetBeaconPos.below(), aState, targetDimId, null);
        }

        if (!isEscapingRD && !isHeadingToRD) {
            java.util.Set<java.util.UUID> participantSet = inst.participants();
            if (bState.is(net.minecraft.world.level.block.Blocks.BEACON)) {
                sourceLevel.setBlock(beaconPos, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), 1);
                sourceLevel.setBlock(srcAnchor, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), 1);
                for (net.minecraft.server.level.ServerPlayer sp : level.getServer().getPlayerList().getPlayers()) {
                    if (!participantSet.contains(sp.getUUID())) {
                        sp.connection.send(new net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket(beaconPos, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState()));
                        sp.connection.send(new net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket(srcAnchor, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState()));
                    }
                }
            } else if (bState.is(net.nostalgia.block.ModBlocks.TIME_MACHINE)) {
                sourceLevel.setBlock(beaconPos, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), 1);
                for (net.minecraft.server.level.ServerPlayer sp : level.getServer().getPlayerList().getPlayers()) {
                    if (!participantSet.contains(sp.getUUID())) {
                        sp.connection.send(new net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket(beaconPos, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState()));
                    }
                }
            }

            if (DimensionUtil.isClientGenerated(sourceLevel.dimension().identifier().toString())) {
                HologramWorldData alphaDataSrc = HologramWorldData.get(sourceLevel);
                alphaDataSrc.addDelta(srcAnchor, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState());
                alphaDataSrc.addDelta(beaconPos, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState());
            }
        }

        net.nostalgia.network.S2CSyncParticipantsPayload payload = new net.nostalgia.network.S2CSyncParticipantsPayload(participantUuids);
        net.nostalgia.network.S2CBystanderVisualsPayload bystanderPayload = new net.nostalgia.network.S2CBystanderVisualsPayload(
                inst.id(),
                beaconPos,
                inst.offsetX(),
                inst.yOffset(),
                inst.offsetZ(),
                inst.targetDimensionId() != null ? inst.targetDimensionId() : level.dimension().identifier().toString(),
                inst.phase()
        );
        for (net.minecraft.server.level.ServerPlayer sp : ((net.minecraft.server.level.ServerLevel) player.level()).getServer().getPlayerList().getPlayers()) {
            if (sp.level() == player.level()) {
                if (!inst.entities().contains(sp)) {
                    net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(sp, bystanderPayload);
                }
                net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(sp, payload);
            }
        }
        inst.setState(State.REVERSING_TIME);


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

        String targetDimId = inst.targetDimensionId() != null ? inst.targetDimensionId() : inst.targetServerLevel() != null ? inst.targetServerLevel().dimension().identifier().toString() : level.dimension().identifier().toString();

        if (net.nostalgia.alphalogic.ritual.DimensionUtil.isClientGenerated(level.dimension().identifier().toString())) {
            BlockPos targetBeaconFiltered = CoordinateMapper.sourceToTarget(beaconPos, inst.offsetX(), inst.yOffset(), inst.offsetZ(), false, 320);
            BlockPos targetAnchorFiltered = targetBeaconFiltered.below();
            java.util.Map<BlockPos, net.minecraft.world.level.block.state.BlockState> deltas = net.nostalgia.alphalogic.ritual.HologramWorldData.get(level).getDeltasInRadius(playerSafePos, 300.0);
            deltas.remove(targetBeaconFiltered);
            deltas.remove(targetAnchorFiltered);
            DeltaSyncService.broadcastBulkDeltas(level.getServer(), deltas, targetDimId, inst.participants());
        }

        if (inst.targetServerLevel() != null && !DimensionUtil.isClientGenerated(targetDimId)) {
            final net.minecraft.core.BlockPos targetPosForChunks = new net.minecraft.core.BlockPos(
                    playerSafePos.getX() + inst.offsetX(),
                    playerSafePos.getY() - inst.yOffset(),
                    playerSafePos.getZ() + inst.offsetZ()
            );
            java.util.List<net.minecraft.server.level.ServerPlayer> players = new java.util.ArrayList<>();
            for (net.minecraft.world.entity.Entity e : inst.entities()) {
                if (e instanceof net.minecraft.server.level.ServerPlayer sp) {
                    players.add(sp);
                }
            }
            if (!players.isEmpty()) {
                net.nostalgia.alphalogic.ritual.HologramChunkLoader.startLoading(players, inst.targetServerLevel(), targetPosForChunks, 300, net.nostalgia.alphalogic.ritual.HologramChunkLoader.getAllChunksInRadius(targetPosForChunks, 300));
            }
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
        net.nostalgia.alphalogic.ritual.event.ClientEchoRitualView t = net.nostalgia.client.events.core.ClientRitualEventRegistry.activeTransition();
        return t != null && !t.isBystander();
    }

    public static net.minecraft.core.BlockPos getVisualCenter() {
        if (net.fabricmc.loader.api.FabricLoader.getInstance().getEnvironmentType() == net.fabricmc.api.EnvType.CLIENT) {
            return getClientVisualCenter();
        }
        EchoRitualEventInstance any = RitualEventRegistry.activeInstance();
        return any != null ? any.beaconPos() : null;
    }

    @net.fabricmc.api.Environment(net.fabricmc.api.EnvType.CLIENT)
    private static net.minecraft.core.BlockPos getClientVisualCenter() {
        net.nostalgia.alphalogic.ritual.event.ClientEchoRitualView t = net.nostalgia.client.events.core.ClientRitualEventRegistry.activeTransition();
        return t != null ? t.ritualCenter() : null;
    }
}
