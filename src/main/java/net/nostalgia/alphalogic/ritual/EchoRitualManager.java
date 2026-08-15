package net.nostalgia.alphalogic.ritual;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents.StartTick;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.TicketType;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.phys.AABB;
import net.nostalgia.alphalogic.ritual.event.ClientEchoRitualView;
import net.nostalgia.alphalogic.ritual.event.RitualEventRegistry;
import net.nostalgia.block.AlphaBlocks;
import net.nostalgia.block.ModBlocks;
import net.nostalgia.client.events.core.ClientRitualEventRegistry;
import net.nostalgia.command.TeleportCommand;
import net.nostalgia.network.S2CBystanderVisualsPayload;
import net.nostalgia.network.S2CEndTransitionVisualsPayload;
import net.nostalgia.network.S2CSkyPortalPayload;
import net.nostalgia.network.S2CStartTransitionVisualsPayload;
import net.nostalgia.network.S2CSyncParticipantsPayload;
import net.nostalgia.network.S2CTimestopZoneStartPayload;
import net.nostalgia.world.dimension.ModDimensions;
import net.sha.api.SHAHologramManager;

public class EchoRitualManager {
  public static long activeRitualMillis = 0L;
  private static long lastServerTickMillis = 0L;
  private static final ConcurrentHashMap<UUID, BlockPos> selectedBeacons = new ConcurrentHashMap<>();
  public static final ConcurrentHashMap<UUID, String> playerReturnDimensions = new ConcurrentHashMap<>();

  public EchoRitualManager() {
  }

  public static void selectBeacon(UUID playerUuid, BlockPos pos) {
    if (playerUuid != null && pos != null) {
      selectedBeacons.put(playerUuid, pos.immutable());
    }
  }

  public static BlockPos getSelectedBeacon(UUID playerUuid) {
    return playerUuid != null ? selectedBeacons.get(playerUuid) : null;
  }

  public static void clearSelection(UUID playerUuid) {
    if (playerUuid != null) {
      selectedBeacons.remove(playerUuid);
    }
  }

  public static void markClientReady(UUID uuid) {
    RitualEventRegistry.markClientReady(uuid);
  }

  public static EchoRitualManager.State getClientState() {
    return RitualEventRegistry.state();
  }

  public static void setClientState(EchoRitualManager.State state) {
    RitualEventRegistry.setState(state);
  }

  public static boolean isServerTransitioning() {
    return getClientState() == EchoRitualManager.State.REVERSING_TIME;
  }

  public static boolean isServerActive() {
    return getClientState() != EchoRitualManager.State.INACTIVE;
  }

  private static boolean isUnsafeBlock(ServerLevel level, BlockPos pos) {
    BlockState state = level.getBlockState(pos);
    if (state.isAir()) {
      return false;
    } else if (state.is(AlphaBlocks.ALPHA_LEAVES) || state.is(AlphaBlocks.ALPHA_OAK_LOG)) {
      return true;
    } else if (!state.is(Blocks.WATER) && !state.is(Blocks.LAVA)) {
      try {
        return !state.getCollisionShape(level, pos).isEmpty();
      } catch (Exception var4) {
        return true;
      }
    } else {
      return true;
    }
  }

  public static double calculateSafeYAndApplyEffects(Entity entity, ServerLevel targetLevel, double targetX, double targetY, double targetZ) {
    MutableBlockPos pos = new MutableBlockPos(targetX, targetY + 0.1, targetZ);
    boolean inBlock = false;

    while (pos.getY() < targetLevel.getMaxY()) {
      BlockState feetState = targetLevel.getBlockState(pos);
      BlockState headState = targetLevel.getBlockState(pos.above());
      boolean feetSolid = !feetState.getCollisionShape(targetLevel, pos).isEmpty();
      boolean headSolid = !headState.getCollisionShape(targetLevel, pos.above()).isEmpty();
      if (!feetSolid && !headSolid) {
        break;
      }

      inBlock = true;
      pos.move(Direction.UP);
    }

    if (inBlock) {
      entity.setDeltaMovement(entity.getDeltaMovement().add(0.0, 0.6, 0.0));
      return pos.getY();
    } else {
      MutableBlockPos downPos = new MutableBlockPos(targetX, targetY - 1.0, targetZ);
      boolean foundGround = false;

      for (int i = 0; i < 4; i++) {
        BlockState state = targetLevel.getBlockState(downPos);
        if (!state.getCollisionShape(targetLevel, downPos).isEmpty() || state.is(Blocks.WATER)) {
          foundGround = true;
          break;
        }

        downPos.move(Direction.DOWN);
      }

      if (!foundGround && entity instanceof LivingEntity living) {
        living.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 100, 0, false, false));
        living.addEffect(new MobEffectInstance(MobEffects.RESISTANCE, 100, 4, false, false));
      }

      return targetY;
    }
  }

  private static boolean anyInstanceSlowingTime(EchoRitualEventInstance exclude) {
    for (EchoRitualEventInstance i : RitualEventRegistry.allInstances()) {
      if (i != exclude) {
        EchoRitualManager.State s = i.state();
        if (s == EchoRitualManager.State.TIME_STOPPING
          || s == EchoRitualManager.State.FROZEN
          || s == EchoRitualManager.State.TIME_RESUMING_DELAY
          || s == EchoRitualManager.State.TIME_RESUMING) {
          return true;
        }
      }
    }

    return false;
  }

  public static void transitionToFrozenForInstance(EchoRitualEventInstance inst) {
    ServerLevel src = inst.sourceLevel();
    BlockPos beacon = inst.beaconPos();
    if (src != null && beacon != null) {
      if (src.tickRateManager() instanceof TickRateManagerAccess access) {
        access.nostalgia$addRegion(new FreezeRegion(src.dimension(), beacon, 5));
      }

      if (TimestopZoneManager.findZoneByBeacon(beacon) == null) {
        TimestopZoneManager.addZone(src, beacon, false);
      }
    }

    inst.setState(EchoRitualManager.State.FROZEN);
  }

  public static void triggerTimeResumeForInstance(EchoRitualEventInstance inst) {
    EchoRitualManager.State s = inst.state();
    if (s == EchoRitualManager.State.FROZEN || s == EchoRitualManager.State.TIME_STOPPING || s == EchoRitualManager.State.TIME_RESUMING_DELAY) {
      inst.setState(EchoRitualManager.State.TIME_RESUMING);
      inst.setTimeStopStartTime(inst.activeMs());
      ServerLevel src = inst.sourceLevel();
      if (src != null) {
        src.getServer().tickRateManager().setFrozen(false);
      }
    }
  }

  public static void endRitualForInstance(EchoRitualEventInstance inst) {
    endRitualForInstance(inst, inst.beaconPos());
  }

  public static void endRitualForInstance(EchoRitualEventInstance inst, BlockPos originalBeaconPos) {
    if (FabricLoader.getInstance().isModLoaded("sha")) {
      SHAHologramManager.removeProvider(NostalgiaServerCollisionBypassProvider.INSTANCE);
    }

    ServerLevel src = inst.sourceLevel();
    if (src != null && originalBeaconPos != null) {
      TimestopZoneManager.removeZone(src, originalBeaconPos);
    }

    if (src != null && !anyInstanceSlowingTime(inst)) {
      src.getServer().tickRateManager().setTickRate(20.0F);
      src.getServer().tickRateManager().setFrozen(false);
    }

    RitualEventRegistry.endEvent(inst.id());
  }

  public static void init() {
    if (FabricLoader.getInstance().isModLoaded("sha")) {
      SHAHologramManager.ignorePredicate = entity -> entity instanceof ItemEntity ? false : !RitualEventRegistry.isParticipant(entity);
    }

    ServerTickEvents.START_SERVER_TICK.register((StartTick)server -> {
      TimestopZoneManager.tickActiveZones(server);
      long nowMs = System.currentTimeMillis();
      long dt = lastServerTickMillis == 0L ? 50L : nowMs - lastServerTickMillis;
      lastServerTickMillis = nowMs;

      for (EchoRitualEventInstance inst : new ArrayList<>(RitualEventRegistry.allInstances())) {
        inst.tick(dt, server);
      }
    });
  }

  public static void startRitual(ServerLevel level, BlockPos beaconPos) {
    if (RitualEventRegistry.findInstanceByBeacon(beaconPos) == null) {
      if (TimestopZoneManager.findZoneByBeacon(beaconPos) == null) {
        level.getServer().setWeatherParameters(6000, 0, false, false);
        EchoRitualEventInstance newInst = RitualEventRegistry.startEvent(beaconPos, level);
        newInst.setState(EchoRitualManager.State.TIME_STOPPING);
        newInst.setTimeStopStartTime(newInst.activeMs());
        TimestopZoneManager.addZone(level, beaconPos, false);
        long gameTime = level.getGameTime();
        long clockTicks = level.getDefaultClockTime();
        float rain = level.getRainLevel(1.0F);
        float thunder = level.getThunderLevel(1.0F);
        S2CTimestopZoneStartPayload payload = new S2CTimestopZoneStartPayload(
          beaconPos, 5, level.dimension().identifier().toString(), false, gameTime, clockTicks, rain, thunder
        );

        for (ServerPlayer sp : level.getServer().getPlayerList().getPlayers()) {
          if (sp.level() == level) {
            ServerPlayNetworking.send(sp, payload);
          }
        }

        BlockState bs = level.getBlockState(beaconPos);
        level.sendBlockUpdated(beaconPos, bs, bs, 3);
      }
    }
  }

  public static void triggerTimeStop(ServerLevel level, BlockPos beaconPos) {
    if (beaconPos != null) {
      EchoRitualEventInstance inst = RitualEventRegistry.findInstanceByBeacon(beaconPos);
      if (inst == null) {
        inst = RitualEventRegistry.startEvent(beaconPos, level);
      }

      if (inst.state() == EchoRitualManager.State.INACTIVE) {
        level.getServer().tickRateManager().setFrozen(false);
        level.getServer().setWeatherParameters(6000, 0, false, false);
        inst.setState(EchoRitualManager.State.TIME_STOPPING);
        inst.setTimeStopStartTime(inst.activeMs());
      }
    }
  }

  public static void triggerTimeResume(BlockPos beaconPos) {
    if (beaconPos != null) {
      EchoRitualEventInstance inst = RitualEventRegistry.findInstanceByBeacon(beaconPos);
      if (inst != null) {
        EchoRitualManager.State s = inst.state();
        if (s == EchoRitualManager.State.FROZEN || s == EchoRitualManager.State.TIME_STOPPING || s == EchoRitualManager.State.TIME_RESUMING_DELAY) {
          inst.setState(EchoRitualManager.State.TIME_RESUMING);
          inst.setTimeStopStartTime(inst.activeMs());
          ServerLevel src = inst.sourceLevel();
          if (src != null) {
            src.getServer().tickRateManager().setFrozen(false);
            src.getServer().tickRateManager().setTickRate(1.0F);
          }
        }
      }
    }
  }

  public static void handlePlayerDisconnect(ServerPlayer player) {
    UUID uuid = player.getUUID();
    clearSelection(uuid);
    EchoRitualEventInstance inst = RitualEventRegistry.findInstanceForParticipant(uuid);
    if (inst != null) {
      inst.readyClients().remove(uuid);
      inst.entities().remove(player);
      inst.participants().remove(uuid);
      List<UUID> participantUuids = new ArrayList<>(inst.participants());
      S2CSyncParticipantsPayload payload = new S2CSyncParticipantsPayload(participantUuids);
      ServerLevel src = inst.sourceLevel();
      if (src != null && src.getServer() != null) {
        for (ServerPlayer sp : src.getServer().getPlayerList().getPlayers()) {
          ServerPlayNetworking.send(sp, payload);
        }
      }
    }
  }

  public static void removeParticipant(UUID uuid, MinecraftServer server) {
    EchoRitualEventInstance inst = RitualEventRegistry.findInstanceForParticipant(uuid);
    if (inst != null) {
      boolean changed = inst.participants().remove(uuid);
      Iterator<Entity> it = inst.entities().iterator();

      while (it.hasNext()) {
        Entity e = it.next();
        if (e.getUUID().equals(uuid)) {
          it.remove();
        }
      }

      inst.readyClients().remove(uuid);
      if (changed && server != null) {
        ServerPlayer removed = server.getPlayerList().getPlayer(uuid);
        if (removed != null) {
          ServerPlayNetworking.send(removed, new S2CEndTransitionVisualsPayload(inst.id()));
        }

        List<UUID> participantUuids = new ArrayList<>(inst.participants());
        S2CSyncParticipantsPayload payload = new S2CSyncParticipantsPayload(participantUuids);

        for (UUID pid : inst.participants()) {
          ServerPlayer sp = server.getPlayerList().getPlayer(pid);
          if (sp != null) {
            ServerPlayNetworking.send(sp, payload);
          }
        }
      }
    }
  }

  public static int getCurrentSyncPhase() {
    return RitualEventRegistry.currentSyncPhase();
  }

  public static String getTransitionDimensionId() {
    return RitualEventRegistry.transitionDimensionId();
  }

  public static BlockPos getTransitionTargetPos() {
    return RitualEventRegistry.transitionTargetPos();
  }

  public static void clearStateOnServerStop() {
    if (FabricLoader.getInstance().isModLoaded("sha")) {
      SHAHologramManager.removeProvider(NostalgiaServerCollisionBypassProvider.INSTANCE);
    }

    activeRitualMillis = 0L;
    lastServerTickMillis = 0L;
    selectedBeacons.clear();
    TimestopZoneManager.activeZones.clear();
    RitualEventRegistry.endAllEvents();
  }

  public static void handleInterrupt(BlockPos beaconPos) {
    if (beaconPos != null) {
      EchoRitualEventInstance inst = RitualEventRegistry.findInstanceByBeacon(beaconPos);
      if (inst != null) {
        EchoRitualManager.State state = inst.state();
        if (state != EchoRitualManager.State.INACTIVE) {
          ServerLevel src = inst.sourceLevel();
          if (src != null) {
            ItemEntity crystal = new ItemEntity(src, beaconPos.getX() + 0.5, beaconPos.getY() + 1.5, beaconPos.getZ() + 0.5, new ItemStack(Items.ECHO_SHARD));
            crystal.setDefaultPickUpDelay();
            src.addFreshEntity(crystal);
          }

          if (state == EchoRitualManager.State.FROZEN || state == EchoRitualManager.State.TIME_STOPPING) {
            inst.setState(EchoRitualManager.State.TIME_RESUMING_DELAY);
            inst.setTimeStopStartTime(inst.activeMs());
          } else if (state != EchoRitualManager.State.TIME_RESUMING_DELAY && state != EchoRitualManager.State.TIME_RESUMING) {
            endRitualForInstance(inst);
          }

          if (FabricLoader.getInstance().isModLoaded("sha")) {
            SHAHologramManager.removeProvider(NostalgiaServerCollisionBypassProvider.INSTANCE);
          }

          inst.entities().clear();
          inst.setPhase(0);
        }
      }
    }
  }

  public static void startTeleportTransition(ServerPlayer player, ServerLevel level, String dimensionId, BlockPos beaconPos) {
    startTeleportTransition(player, level, dimensionId, beaconPos, null);
  }

  public static void startTeleportTransition(ServerPlayer player, ServerLevel level, String dimensionId, BlockPos beaconPos, BlockPos landingOverride) {
    if (beaconPos != null) {
      BlockPos originalBeaconPos = beaconPos;
      EchoRitualEventInstance inst = RitualEventRegistry.findInstanceByBeacon(beaconPos);
      if (inst == null) {
        inst = RitualEventRegistry.startEvent(beaconPos, player.level());
      }

      inst.setTargetServerLevel(level);
      inst.setTargetDimensionId(dimensionId);
      if (inst.state() == EchoRitualManager.State.INACTIVE) {
        inst.setState(EchoRitualManager.State.FROZEN);
      }

      inst.setPhase(1);
      inst.setPhaseStartTime(inst.activeMs());
      inst.readyClients().clear();
      int targetSurfaceY = beaconPos.getY();
      if (level != null) {
        level.getChunk(beaconPos.getX() >> 4, beaconPos.getZ() >> 4, ChunkStatus.FULL, true);
        targetSurfaceY = TeleportCommand.getSurfaceY(level, beaconPos.getX(), beaconPos.getZ(), true);
      }

      int offsetX = 0;
      int offsetZ = 0;
      int offsetY = beaconPos.getY() - targetSurfaceY;
      BlockPos playerSafePos = player.blockPosition();
      boolean isEscapingRD = player.level().dimension() == ModDimensions.RD_132211_LEVEL_KEY;
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
      } else if (landingOverride != null && level != null) {
        level.getChunk(landingOverride.getX() >> 4, landingOverride.getZ() >> 4, ChunkStatus.FULL, true);
        int landY = TeleportCommand.getSurfaceY(level, landingOverride.getX(), landingOverride.getZ(), true) + 1;
        playerSafePos = new BlockPos(landingOverride.getX(), landY, landingOverride.getZ());
        offsetX = landingOverride.getX() - beaconPos.getX();
        offsetZ = landingOverride.getZ() - beaconPos.getZ();
        offsetY = beaconPos.getY() - landY;
      } else if (level != null) {
        level.getChunk(player.getBlockX() >> 4, player.getBlockZ() >> 4, ChunkStatus.FULL, true);
        int pY = TeleportCommand.getSurfaceY(level, player.getBlockX(), player.getBlockZ());
        playerSafePos = new BlockPos(player.getBlockX(), pY, player.getBlockZ());
      }

      inst.setTargetPos(playerSafePos);
      inst.setBeaconPos(beaconPos);
      inst.setOffsets(offsetX, offsetY, offsetZ);
      inst.setTransitioning(true);
      ServerLevel sourceLevel = player.level();
      BlockPos srcAnchor = beaconPos.below();
      BlockState bState = player.level().getBlockState(beaconPos);
      BlockState aState = player.level().getBlockState(beaconPos.below());
      BlockEntity be = sourceLevel.getBlockEntity(beaconPos);
      if (be != null) {
        inst.setBeaconNbt(be.saveWithFullMetadata(sourceLevel.registryAccess()));
      }

      TimestopZoneManager.ActiveZone activeZone = TimestopZoneManager.findZoneByBeacon(originalBeaconPos);
      if (activeZone != null) {
        ServerLevel zoneLevel = sourceLevel.getServer().getLevel(activeZone.dimension());
        if (zoneLevel != null) {
          TimestopZoneManager.removeZone(zoneLevel, activeZone.beaconPos());
        }
      }

      if (!isEscapingRD && !isHeadingToRD) {
        inst.cachePut(beaconPos, bState);
        inst.cachePut(beaconPos.below(), aState);
      }

      AABB searchBox = new AABB(beaconPos).inflate(10.0);
      List<Entity> tempPlayers = new ArrayList<>();
      List<Entity> collected = new ArrayList<>();
      if (isEscapingRD) {
        for (Player p : player.level().players()) {
          tempPlayers.add(p);
          collected.add(p);
        }
      } else {
        for (ServerPlayer p : sourceLevel.players()) {
          if (p.getBoundingBox().intersects(searchBox)) {
            tempPlayers.add(p);
            collected.add(p);
          }
        }
      }

      for (Entity e : player.level().getEntities((Entity)null, searchBox, ex -> !(ex instanceof Player))) {
        if (e instanceof TamableAnimal tamable) {
          LivingEntity owner = tamable.getOwner();
          if (owner != null && tempPlayers.contains(owner)) {
            collected.add(e);
          }
        } else if (e instanceof Mob mob) {
          Entity leashHolder = mob.getLeashHolder();
          if (leashHolder != null && tempPlayers.contains(leashHolder)) {
            collected.add(e);
          }
        }
      }

      List<Entity> linked = new ArrayList<>();

      for (Entity ex : collected) {
        Entity vehicle = ex.getVehicle();
        if (vehicle != null && !collected.contains(vehicle) && !linked.contains(vehicle)) {
          linked.add(vehicle);
        }

        for (Entity pass : ex.getPassengers()) {
          if (!collected.contains(pass) && !linked.contains(pass)) {
            linked.add(pass);
          }
        }
      }

      collected.addAll(linked);
      inst.entities().clear();
      inst.entities().addAll(collected);
      inst.participants().clear();
      List<UUID> participantUuids = new ArrayList<>();

      for (Entity ex : collected) {
        inst.participants().add(ex.getUUID());
        participantUuids.add(ex.getUUID());
      }

      MinecraftServer srv = player.level().getServer();
      if (srv != null) {
        for (SkyPortalEventInstance portalInst : new java.util.ArrayList<>(SkyPortalManager.allPortals())) {
          S2CSkyPortalPayload closePayload = new S2CSkyPortalPayload(
            portalInst.id(), false, 256, 256, false, 0L, portalInst.center(), portalInst.sourceDimension(), portalInst.targetDimension()
          );
          if (portalInst.center().equals(beaconPos)) {
            org.slf4j.LoggerFactory.getLogger("SkyPortalEvent").info("[SKYPORTAL-EVENT] Ritual on SAME beacon -> Stopping portal {} on server", portalInst.id());
            SkyPortalManager.stopPortal(srv, portalInst.id());
            for (ServerPlayer p : srv.getPlayerList().getPlayers()) {
              ServerPlayNetworking.send(p, closePayload);
            }
          } else {
            org.slf4j.LoggerFactory.getLogger("SkyPortalEvent").info("[SKYPORTAL-EVENT] Ritual on DIFFERENT beacon -> Keeping portal {} ALIVE on server, sending closePayload to {} participants", portalInst.id(), inst.entities().size());
            for (Entity e : inst.entities()) {
              if (e instanceof ServerPlayer sp) {
                ServerPlayNetworking.send(sp, closePayload);
              }
            }
          }
        }
      }

      ServerLevel tlEarly = level.getServer().getLevel(ResourceKey.create(Registries.DIMENSION, Identifier.tryParse(dimensionId)));
      long currentSeedEarly = tlEarly != null ? tlEarly.getSeed() : level.getSeed();
      int targetSkyColor = -1;
      int targetFogColor = -1;
      if (tlEarly != null && playerSafePos != null) {
        targetSkyColor = (Integer)tlEarly.environmentAttributes().getValue(EnvironmentAttributes.SKY_COLOR, playerSafePos.getCenter(), null);
        targetFogColor = (Integer)tlEarly.environmentAttributes().getValue(EnvironmentAttributes.FOG_COLOR, playerSafePos.getCenter(), null);
      }

      S2CStartTransitionVisualsPayload startPayloadEarly = new S2CStartTransitionVisualsPayload(
        inst.id(),
        dimensionId,
        beaconPos,
        playerSafePos,
        inst.offsetX(),
        inst.yOffset(),
        inst.offsetZ(),
        currentSeedEarly,
        targetSkyColor,
        targetFogColor,
        Block.getId(bState),
        Block.getId(aState)
      );

      for (Entity ex : inst.entities()) {
        if (ex instanceof ServerPlayer sp) {
          ServerPlayNetworking.send(sp, startPayloadEarly);
        }
      }

      if (!isEscapingRD && !isHeadingToRD) {
        String targetDimId = inst.targetDimensionId() != null
          ? inst.targetDimensionId()
          : (inst.targetServerLevel() != null ? inst.targetServerLevel().dimension().identifier().toString() : level.dimension().identifier().toString());
        BlockPos targetBeaconPos = CoordinateMapper.forward(inst.geometry(), beaconPos);
        DeltaSyncService.broadcastSingleDelta(level.getServer(), targetBeaconPos, bState, targetDimId, null);
        DeltaSyncService.broadcastSingleDelta(level.getServer(), targetBeaconPos.below(), aState, targetDimId, null);
      }

      if (!isEscapingRD && !isHeadingToRD) {
        Set<UUID> participantSet = inst.participants();
        if (bState.is(Blocks.BEACON)) {
          sourceLevel.setBlock(beaconPos, Blocks.AIR.defaultBlockState(), 1);
          sourceLevel.setBlock(srcAnchor, Blocks.AIR.defaultBlockState(), 1);

          for (ServerPlayer sp : level.getServer().getPlayerList().getPlayers()) {
            if (!participantSet.contains(sp.getUUID())) {
              sp.connection.send(new ClientboundBlockUpdatePacket(beaconPos, Blocks.AIR.defaultBlockState()));
              sp.connection.send(new ClientboundBlockUpdatePacket(srcAnchor, Blocks.AIR.defaultBlockState()));
            }
          }
        } else if (bState.is(ModBlocks.TIME_MACHINE)) {
          sourceLevel.setBlock(beaconPos, Blocks.AIR.defaultBlockState(), 1);

          for (ServerPlayer spx : level.getServer().getPlayerList().getPlayers()) {
            if (!participantSet.contains(spx.getUUID())) {
              spx.connection.send(new ClientboundBlockUpdatePacket(beaconPos, Blocks.AIR.defaultBlockState()));
            }
          }
        }

        HologramWorldData alphaDataSrc = HologramWorldData.get(sourceLevel);
        alphaDataSrc.addDelta(srcAnchor, Blocks.AIR.defaultBlockState());
        alphaDataSrc.addDelta(beaconPos, Blocks.AIR.defaultBlockState());
      }

      S2CSyncParticipantsPayload payload = new S2CSyncParticipantsPayload(participantUuids);
      S2CBystanderVisualsPayload bystanderPayload = new S2CBystanderVisualsPayload(
        inst.id(),
        beaconPos,
        inst.offsetX(),
        inst.yOffset(),
        inst.offsetZ(),
        inst.targetDimensionId() != null ? inst.targetDimensionId() : level.dimension().identifier().toString(),
        inst.phase()
      );

      for (ServerPlayer spxx : player.level().getServer().getPlayerList().getPlayers()) {
        if (spxx.level() == player.level()) {
          if (!inst.entities().contains(spxx)) {
            ServerPlayNetworking.send(spxx, bystanderPayload);
          }

          ServerPlayNetworking.send(spxx, payload);
        }
      }

      inst.setState(EchoRitualManager.State.REVERSING_TIME);
      MinecraftServer server = level.getServer();
      if (server != null) {
        server.tickRateManager().setTickRate(20.0F);
        server.tickRateManager().setFrozen(false);
        ServerLevel transitionTarget = inst.targetServerLevel();
        BlockPos transitionTargetPos = inst.targetPos();
        if (transitionTarget != null && transitionTargetPos != null) {
          int vd = server.getPlayerList().getViewDistance();
          transitionTarget.getChunkSource()
            .addTicketWithRadius(TicketType.PORTAL, new ChunkPos(transitionTargetPos.getX() >> 4, transitionTargetPos.getZ() >> 4), vd);
          CompletableFuture.runAsync(() -> {
            int cX = transitionTargetPos.getX() >> 4;
            int cZ = transitionTargetPos.getZ() >> 4;

            for (int x = -4; x <= 4; x++) {
              for (int z = -4; z <= 4; z++) {
                transitionTarget.getChunk(cX + x, cZ + z, ChunkStatus.FULL, true);
              }
            }
          });
        }
      }

      String targetDimId = inst.targetDimensionId() != null
        ? inst.targetDimensionId()
        : (inst.targetServerLevel() != null ? inst.targetServerLevel().dimension().identifier().toString() : level.dimension().identifier().toString());
      BlockPos targetBeaconFiltered = CoordinateMapper.forward(inst.geometry(), beaconPos);
      BlockPos targetAnchorFiltered = targetBeaconFiltered.below();
      Map<BlockPos, BlockState> deltas = HologramWorldData.get(level).getDeltasInRadius(playerSafePos, 300.0);
      deltas.remove(targetBeaconFiltered);
      deltas.remove(targetAnchorFiltered);
      DeltaSyncService.broadcastBulkDeltas(level.getServer(), deltas, targetDimId, inst.participants());
      if (inst.targetServerLevel() != null && !DimensionUtil.isClientGenerated(targetDimId)) {
        BlockPos targetPosForChunks = new BlockPos(
          playerSafePos.getX() + inst.offsetX(), playerSafePos.getY() - inst.yOffset(), playerSafePos.getZ() + inst.offsetZ()
        );
        List<ServerPlayer> players = new ArrayList<>();

        for (Entity exx : inst.entities()) {
          if (exx instanceof ServerPlayer spxxx) {
            players.add(spxxx);
          }
        }

        if (!players.isEmpty()) {
          HologramChunkLoader.startLoading(
            players, inst.targetServerLevel(), targetPosForChunks, 300, HologramChunkLoader.getAllChunksInRadius(targetPosForChunks, 300)
          );
        }
      }
    }
  }

  public static boolean isVisualReversing() {
    return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT
      ? getClientVisualReversing()
      : getClientState() == EchoRitualManager.State.REVERSING_TIME;
  }

  @Environment(EnvType.CLIENT)
  private static boolean getClientVisualReversing() {
    ClientEchoRitualView t = ClientRitualEventRegistry.activeTransition();
    return t != null && !t.isBystander();
  }

  public static BlockPos getVisualCenter() {
    if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
      return getClientVisualCenter();
    } else {
      EchoRitualEventInstance any = RitualEventRegistry.activeInstance();
      return any != null ? any.beaconPos() : null;
    }
  }

  @Environment(EnvType.CLIENT)
  private static BlockPos getClientVisualCenter() {
    ClientEchoRitualView t = ClientRitualEventRegistry.activeTransition();
    return t != null ? t.ritualCenter() : null;
  }

  public static enum State {
    INACTIVE,
    TIME_STOPPING,
    FROZEN,
    REVERSING_TIME,
    TIME_RESUMING_DELAY,
    TIME_RESUMING;

    private State() {
    }
  }
}
