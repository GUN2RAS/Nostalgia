package net.nostalgia.alphalogic.ritual;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.phys.Vec3;
import net.nostalgia.alphalogic.ritual.event.EchoRitualEvent;
import net.nostalgia.alphalogic.ritual.geometry.OffsetGeometry;
import net.nostalgia.alphalogic.ritual.geometry.TransitionGeometry;
import net.nostalgia.command.TeleportCommand;
import net.nostalgia.network.S2CEndTransitionVisualsPayload;
import net.nostalgia.network.S2CRitualPhasePayload;
import net.nostalgia.world.dimension.ModDimensions;
import net.sha.api.SHAHologramManager;

public final class EchoRitualEventInstance implements EchoRitualEvent {
  private final UUID id;
  private BlockPos beaconPos;
  private BlockPos originalBeaconPos;
  private BlockPos targetPos;
  private String targetDimensionId = null;
  private ServerLevel sourceLevel;
  private ServerLevel targetServerLevel;
  private int offsetX;
  private int yOffset;
  private int offsetZ;
  private TransitionGeometry geometry;
  private RitualPhase ritualPhase = RitualPhase.IDLE;
  private long phaseStartTime;
  private boolean transitioning;
  private long timeStopStartTime;
  private EchoRitualManager.State state = EchoRitualManager.State.INACTIVE;
  private CompoundTag beaconNbt;
  private long activeMs = 0L;
  private final Set<UUID> participants = ConcurrentHashMap.newKeySet();
  private final Set<UUID> readyClients = ConcurrentHashMap.newKeySet();
  private final Set<UUID> clientsReadyForNextPhase = ConcurrentHashMap.newKeySet();
  private final Map<UUID, Integer> clientHologramSurfaces = new ConcurrentHashMap<>();
  private final List<Entity> entities = new ArrayList<>();
  public final EventDeltaCache deltaCache = new EventDeltaCache();

  public CompoundTag beaconNbt() {
    return this.beaconNbt;
  }

  public void setBeaconNbt(CompoundTag nbt) {
    this.beaconNbt = nbt;
  }

  public long activeMs() {
    return this.activeMs;
  }

  public void addActiveMs(long dt) {
    this.activeMs += dt;
  }

  public EchoRitualEventInstance(UUID id, BlockPos beaconPos, ServerLevel sourceLevel) {
    this.id = id;
    this.beaconPos = beaconPos;
    this.originalBeaconPos = beaconPos;
    this.sourceLevel = sourceLevel;
  }

  @Override
  public UUID id() {
    return this.id;
  }

  @Override
  public EchoRitualManager.State state() {
    return this.state;
  }

  public void setState(EchoRitualManager.State newState) {
    this.state = newState;
  }

  @Override
  public BlockPos beaconPos() {
    return this.beaconPos;
  }

  public void setBeaconPos(BlockPos pos) {
    this.beaconPos = pos;
  }

  public boolean containsOverworldPos(BlockPos pos) {
    return this.beaconPos == null ? false : pos.closerThan(this.beaconPos, 288.0);
  }

  @Override
  public ResourceKey<Level> dimension() {
    return this.sourceLevel != null ? this.sourceLevel.dimension() : null;
  }

  @Override
  public BlockPos targetPos() {
    return this.targetPos;
  }

  public void setTargetPos(BlockPos pos) {
    this.targetPos = pos;
  }

  @Override
  public String targetDimensionId() {
    return this.targetDimensionId;
  }

  public void setTargetDimensionId(String id) {
    this.targetDimensionId = id;
  }

  @Override
  public ServerLevel sourceLevel() {
    return this.sourceLevel;
  }

  public void setSourceLevel(ServerLevel level) {
    this.sourceLevel = level;
  }

  @Override
  public ServerLevel targetServerLevel() {
    return this.targetServerLevel;
  }

  public void setTargetServerLevel(ServerLevel level) {
    this.targetServerLevel = level;
  }

  @Override
  public int offsetX() {
    return this.offsetX;
  }

  @Override
  public int yOffset() {
    return this.yOffset;
  }

  @Override
  public int offsetZ() {
    return this.offsetZ;
  }

  @Override
  public void setOffsets(int dx, int dy, int dz) {
    this.offsetX = dx;
    this.yOffset = dy;
    this.offsetZ = dz;
    this.geometry = new OffsetGeometry(dx, dy, dz);
  }

  public TransitionGeometry geometry() {
    return this.geometry;
  }

  public void setGeometry(TransitionGeometry g) {
    this.geometry = g;
  }

  @Override
  public int phase() {
    return this.ritualPhase.id();
  }

  @Override
  public void setPhase(int phase) {
    this.ritualPhase = RitualPhase.fromId(phase);
  }

  public RitualPhase ritualPhase() {
    return this.ritualPhase;
  }

  public void setRitualPhase(RitualPhase phase) {
    this.ritualPhase = phase;
  }

  @Override
  public long phaseStartTime() {
    return this.phaseStartTime;
  }

  @Override
  public void setPhaseStartTime(long t) {
    this.phaseStartTime = t;
  }

  public long timeStopStartTime() {
    return this.timeStopStartTime;
  }

  public void setTimeStopStartTime(long t) {
    this.timeStopStartTime = t;
  }

  @Override
  public boolean isTransitioning() {
    return this.transitioning;
  }

  @Override
  public void setTransitioning(boolean v) {
    this.transitioning = v;
  }

  @Override
  public Set<UUID> participants() {
    return this.participants;
  }

  @Override
  public Set<UUID> readyClients() {
    return this.readyClients;
  }

  @Override
  public Set<UUID> clientsReadyForNextPhase() {
    return this.clientsReadyForNextPhase;
  }

  @Override
  public Map<UUID, Integer> clientHologramSurfaces() {
    return this.clientHologramSurfaces;
  }

  @Override
  public List<Entity> entities() {
    return this.entities;
  }

  @Override
  public void cachePut(BlockPos pos, BlockState state) {
    this.deltaCache.put(pos, state);
  }

  @Override
  public BlockState cacheGet(BlockPos pos) {
    return this.deltaCache.get(pos);
  }

  @Override
  public boolean cacheHas(BlockPos pos) {
    return this.deltaCache.has(pos);
  }

  @Override
  public void cacheClear() {
    this.deltaCache.clear();
  }

  @Override
  public Map<BlockPos, BlockState> cacheEntries() {
    return this.deltaCache.getAll();
  }

  public void tick(long dt, MinecraftServer server) {
    if (this.state != EchoRitualManager.State.INACTIVE) {
      this.activeMs += dt;
    }

    if (this.ritualPhase != RitualPhase.IDLE && this.targetServerLevel != null && this.targetPos != null) {
      if (this.ritualPhase == RitualPhase.CACHE_GEN) {
        boolean allReady = true;

        for (Entity e : this.entities) {
          if (e instanceof ServerPlayer sp && !this.readyClients.contains(sp.getUUID())) {
            allReady = false;
            break;
          }
        }

        if (allReady || this.activeMs - this.phaseStartTime > 15000L) {
          this.ritualPhase = RitualPhase.HOLOGRAM_DISPLAY;
          this.phaseStartTime = this.activeMs;

          for (Entity ex : this.entities) {
            if (ex instanceof ServerPlayer sp) {
              ServerPlayNetworking.send(sp, new S2CRitualPhasePayload(this.id, 2));
            }
          }
        }
      } else if (this.ritualPhase == RitualPhase.HOLOGRAM_DISPLAY) {
        if (this.activeMs - this.phaseStartTime > 4500L) {
          this.ritualPhase = RitualPhase.TELEPORT;
          this.phaseStartTime = this.activeMs;
          if (FabricLoader.getInstance().isModLoaded("sha")) {
            SHAHologramManager.updateSpatialMap(NostalgiaServerCollisionBypassProvider.INSTANCE);
          }

          BlockPos targetBeaconPos = this.beaconPos != null ? CoordinateMapper.forward(this.geometry, this.beaconPos) : null;
          boolean isHeadingToRD = DimensionUtil.isRD(this.targetDimensionId);
          boolean isEscapingRD = this.sourceLevel != null && this.sourceLevel.dimension() == ModDimensions.RD_132211_LEVEL_KEY;
          if (!isHeadingToRD && !isEscapingRD && targetBeaconPos != null && this.targetServerLevel != null) {
            int bx = targetBeaconPos.getX();
            int bz = targetBeaconPos.getZ();
            int baseY = targetBeaconPos.getY();

            this.targetServerLevel.getChunk(bx >> 4, bz >> 4, ChunkStatus.FULL, true);
            boolean needsPlatform = false;
            for (int checkY = baseY; checkY > baseY - 20 && checkY > this.targetServerLevel.getMinY(); checkY--) {
              BlockState checkState = this.targetServerLevel.getBlockState(new BlockPos(bx, checkY, bz));
              if (!checkState.isAir() && !checkState.is(Blocks.BEACON) && !checkState.is(Blocks.RESPAWN_ANCHOR)) {
                needsPlatform = !checkState.getFluidState().isEmpty();
                break;
              }
            }

            if (needsPlatform) {
              int radius = 10;
              for (int dx = -radius; dx <= radius; dx++) {
                for (int dz = -radius; dz <= radius; dz++) {
                  if (dx * dx + dz * dz <= radius * radius) {
                    int wx = bx + dx;
                    int wz = bz + dz;
                    this.targetServerLevel.getChunk(wx >> 4, wz >> 4, ChunkStatus.FULL, true);

                    for (int scanY = baseY; scanY > baseY - 20 && scanY > this.targetServerLevel.getMinY(); scanY--) {
                      BlockPos checkPos = new BlockPos(wx, scanY, wz);
                      BlockState st = this.targetServerLevel.getBlockState(checkPos);
                      if (!st.isAir() && !st.is(Blocks.BEACON) && !st.is(Blocks.RESPAWN_ANCHOR)) {
                        if (!st.getFluidState().isEmpty()) {
                          boolean isLava = st.getFluidState().is(FluidTags.LAVA);
                          BlockState platformBlock = isLava ? Blocks.OBSIDIAN.defaultBlockState() : Blocks.ICE.defaultBlockState();
                          this.targetServerLevel.setBlock(checkPos, platformBlock, 3);
                        }
                        break;
                      }
                    }
                  }
                }
              }
            }
          }

          for (Entity exx : this.entities) {
            if (exx instanceof ServerPlayer sp) {
              ServerPlayNetworking.send(sp, new S2CRitualPhasePayload(this.id, 3));
            }
          }
        }
      } else if (this.ritualPhase == RitualPhase.TELEPORT) {
        long requiredDelay = 4500L;
        if (this.activeMs - this.phaseStartTime > requiredDelay) {
          BlockPos targetBeaconPos = this.beaconPos != null ? CoordinateMapper.forward(this.geometry, this.beaconPos) : null;
          List<BlockPos> positionsToSync = new ArrayList<>();

          for (Entry<BlockPos, BlockState> entry : this.deltaCache.getAll().entrySet()) {
            BlockPos vPos = entry.getKey();
            BlockPos localizedPos = CoordinateMapper.forward(this.geometry, vPos);
            if (!DimensionUtil.isRD(this.targetDimensionId)) {
              this.targetServerLevel.getChunk(localizedPos.getX() >> 4, localizedPos.getZ() >> 4, ChunkStatus.FULL, true);
              this.targetServerLevel.setBlock(localizedPos, entry.getValue(), 3);
              positionsToSync.add(localizedPos);
              if (this.beaconPos != null && vPos.equals(this.beaconPos) && this.beaconNbt != null) {
                this.beaconNbt.putInt("x", localizedPos.getX());
                this.beaconNbt.putInt("y", localizedPos.getY());
                this.beaconNbt.putInt("z", localizedPos.getZ());
                BlockEntity targetBe = BlockEntity.loadStatic(localizedPos, entry.getValue(), this.beaconNbt, this.targetServerLevel.registryAccess());
                if (targetBe != null) {
                  this.targetServerLevel.setBlockEntity(targetBe);
                }
              }
            }
          }

          this.deltaCache.clear();
          if (targetBeaconPos != null) {
            this.beaconPos = targetBeaconPos;
            EchoRitualManager.clearSelection(null);
          }

          for (Entity entity : this.entities) {
            if (entity.isAlive()) {
              Vec3 motion = entity.getDeltaMovement();
              double newX = entity.getX() + this.offsetX;
              double newZ = entity.getZ() + this.offsetZ;
              double newY = this.resolveLandingY(entity, newX, newZ);

              if (entity instanceof ServerPlayer sp) {
                if (sp.containerMenu != null && sp.containerMenu != sp.inventoryMenu) {
                  sp.closeContainer();
                }

                sp.teleportTo(this.targetServerLevel, newX, newY, newZ, Collections.emptySet(), sp.getYRot(), sp.getXRot(), true);
                if (sp.isCreative() && !sp.getAbilities().mayfly) {
                  sp.getAbilities().mayfly = true;
                  sp.onUpdateAbilities();
                }
              } else {
                entity.teleportTo(this.targetServerLevel, newX, newY, newZ, Collections.emptySet(), entity.getYRot(), entity.getXRot(), true);
              }

              entity.setDeltaMovement(motion);
              if (entity instanceof Player p) {
                p.hurtMarked = true;
              }
            }
          }

          this.transitioning = false;

          for (Entity entityx : this.entities) {
            if (entityx instanceof ServerPlayer sp) {
              ServerPlayNetworking.send(sp, new S2CEndTransitionVisualsPayload(this.id));
              SkyPortalManager.sendPortalToPlayer(sp, this.targetServerLevel.getServer());
            }
          }

          Set<UUID> participantSet = new HashSet<>();

          for (Entity entityxx : this.entities) {
            participantSet.add(entityxx.getUUID());
          }

          if (this.sourceLevel != null) {
            for (Player lp : this.sourceLevel.players()) {
              if (lp instanceof ServerPlayer sp && !participantSet.contains(sp.getUUID())) {
                ServerPlayNetworking.send(sp, new S2CEndTransitionVisualsPayload(this.id));
              }
            }
          }

          EchoRitualManager.endRitualForInstance(this, this.originalBeaconPos);
        }
      }
    }

    if (this.state != EchoRitualManager.State.INACTIVE) {
      if (this.state == EchoRitualManager.State.TIME_RESUMING_DELAY) {
        long elapsed = this.activeMs - this.timeStopStartTime;
        if (elapsed >= 5000L) {
          if (this.sourceLevel != null && this.originalBeaconPos != null) {
            TimestopZoneManager.removeZone(this.sourceLevel, this.originalBeaconPos);
          }

          EchoRitualManager.triggerTimeResumeForInstance(this);
        }
      }

      if (this.state == EchoRitualManager.State.TIME_STOPPING) {
        long elapsed = this.activeMs - this.timeStopStartTime;
        if (elapsed == 0L && this.sourceLevel != null) {
          this.sourceLevel.getServer().tickRateManager().setTickRate(1.0F);
        }

        if (elapsed >= 2000L) {
          EchoRitualManager.transitionToFrozenForInstance(this);
        }
      } else if (this.state == EchoRitualManager.State.TIME_RESUMING) {
        long elapsedx = this.activeMs - this.timeStopStartTime;
        if (elapsedx >= 2000L) {
          EchoRitualManager.endRitualForInstance(this);
        }
      }
    }
  }

  private double resolveLandingY(Entity entity, double newX, double newZ) {
    if (entity instanceof ServerPlayer sp) {
      Integer clientSurface = this.clientHologramSurfaces.get(sp.getUUID());
      if (clientSurface != null && clientSurface != -1) {
        return clientSurface.doubleValue() + 1.0;
      }
    }

    if (this.targetServerLevel != null) {
      this.targetServerLevel.getChunk((int)Math.floor(newX) >> 4, (int)Math.floor(newZ) >> 4, ChunkStatus.FULL, true);
      int surfaceY = TeleportCommand.getSurfaceY(this.targetServerLevel, (int)Math.floor(newX), (int)Math.floor(newZ), true);
      if (surfaceY > this.targetServerLevel.getMinY()) {
        return surfaceY;
      }
    }

    double fallback = entity.getY() - this.yOffset;
    return EchoRitualManager.calculateSafeYAndApplyEffects(entity, this.targetServerLevel, newX, fallback, newZ);
  }
}
