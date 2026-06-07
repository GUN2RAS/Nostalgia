package net.nostalgia.alphalogic.ritual;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.nostalgia.alphalogic.ritual.event.EchoRitualEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class EchoRitualEventInstance implements EchoRitualEvent {
    private final UUID id;
    private BlockPos beaconPos;
    private BlockPos targetPos;
    private String targetDimensionId = null;
    private ServerLevel sourceLevel;
    private ServerLevel targetServerLevel;

    private int offsetX;
    private int yOffset;
    private int offsetZ;

    private int phase;
    private long phaseStartTime;
    private boolean transitioning;
    private long timeStopStartTime;
    private EchoRitualManager.State state = EchoRitualManager.State.INACTIVE;
    private net.minecraft.nbt.CompoundTag beaconNbt;

    public net.minecraft.nbt.CompoundTag beaconNbt() { return beaconNbt; }
    public void setBeaconNbt(net.minecraft.nbt.CompoundTag nbt) { this.beaconNbt = nbt; }

    private long activeMs = 0;

    public long activeMs() { return activeMs; }
    public void addActiveMs(long dt) { this.activeMs += dt; }

    private final Set<UUID> participants = ConcurrentHashMap.newKeySet();
    private final Set<UUID> readyClients = ConcurrentHashMap.newKeySet();
    private final Set<UUID> clientsReadyForNextPhase = ConcurrentHashMap.newKeySet();
    private final Map<UUID, Integer> clientHologramSurfaces = new ConcurrentHashMap<>();
    private final List<Entity> entities = new ArrayList<>();

    public EchoRitualEventInstance(UUID id, BlockPos beaconPos, ServerLevel sourceLevel) {
        this.id = id;
        this.beaconPos = beaconPos;
        this.sourceLevel = sourceLevel;
    }

    @Override
    public UUID id() { return id; }

    @Override
    public EchoRitualManager.State state() { return state; }

    public void setState(EchoRitualManager.State newState) { this.state = newState; }

    @Override
    public BlockPos beaconPos() { return beaconPos; }

    public void setBeaconPos(BlockPos pos) { this.beaconPos = pos; }

    public boolean containsOverworldPos(BlockPos pos) {
        if (beaconPos == null) return false;
        return pos.closerThan(beaconPos, 288.0);
    }

    @Override
    public ResourceKey<Level> dimension() {
        return sourceLevel != null ? sourceLevel.dimension() : null;
    }

    @Override
    public BlockPos targetPos() { return targetPos; }

    public void setTargetPos(BlockPos pos) { this.targetPos = pos; }

    @Override
    public String targetDimensionId() { return targetDimensionId; }

    public void setTargetDimensionId(String id) { this.targetDimensionId = id; }

    @Override
    public ServerLevel sourceLevel() { return sourceLevel; }

    public void setSourceLevel(ServerLevel level) { this.sourceLevel = level; }

    @Override
    public ServerLevel targetServerLevel() { return targetServerLevel; }

    public void setTargetServerLevel(ServerLevel level) { this.targetServerLevel = level; }

    @Override
    public int offsetX() { return offsetX; }

    @Override
    public int yOffset() { return yOffset; }

    @Override
    public int offsetZ() { return offsetZ; }

    @Override
    public void setOffsets(int dx, int dy, int dz) {
        this.offsetX = dx;
        this.yOffset = dy;
        this.offsetZ = dz;
    }

    @Override
    public int phase() { return phase; }

    @Override
    public void setPhase(int phase) { this.phase = phase; }

    @Override
    public long phaseStartTime() { return phaseStartTime; }

    @Override
    public void setPhaseStartTime(long t) { this.phaseStartTime = t; }

    public long timeStopStartTime() { return timeStopStartTime; }

    public void setTimeStopStartTime(long t) { this.timeStopStartTime = t; }

    @Override
    public boolean isTransitioning() { return transitioning; }

    @Override
    public void setTransitioning(boolean v) { this.transitioning = v; }

    @Override
    public Set<UUID> participants() { return participants; }

    @Override
    public Set<UUID> readyClients() { return readyClients; }

    @Override
    public Set<UUID> clientsReadyForNextPhase() { return clientsReadyForNextPhase; }

    @Override
    public Map<UUID, Integer> clientHologramSurfaces() { return clientHologramSurfaces; }

    @Override
    public List<Entity> entities() { return entities; }

    public final EventDeltaCache deltaCache = new EventDeltaCache();

    public void cachePut(BlockPos pos, BlockState state) { deltaCache.put(pos, state); }
    public BlockState cacheGet(BlockPos pos) { return deltaCache.get(pos); }
    public boolean cacheHas(BlockPos pos) { return deltaCache.has(pos); }
    public void cacheClear() { deltaCache.clear(); }
    public Map<BlockPos, BlockState> cacheEntries() { return deltaCache.getAll(); }

    public void tick(long dt, net.minecraft.server.MinecraftServer server) {
        if (state != EchoRitualManager.State.INACTIVE) {
            activeMs += dt;
        }

        if (phase > 0 && targetServerLevel != null && targetPos != null) {
            if (phase == 1) {
                boolean allReady = true;
                for (Entity e : entities) {
                    if (e instanceof net.minecraft.server.level.ServerPlayer sp && !readyClients.contains(sp.getUUID())) {
                        allReady = false; break;
                    }
                }
                if (allReady || activeMs - phaseStartTime > 15000) {
                    phase = 2;
                    phaseStartTime = activeMs;
                    for (Entity e : entities) {
                        if (e instanceof net.minecraft.server.level.ServerPlayer sp) {
                            net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(sp, new net.nostalgia.network.S2CRitualPhasePayload(id, 2));
                        }
                    }
                }
            } else if (phase == 2) {
                if (activeMs - phaseStartTime > 4500) {
                    phase = 3;
                    phaseStartTime = activeMs;
                    if (net.fabricmc.loader.api.FabricLoader.getInstance().isModLoaded("sha")) {
                        net.sha.api.SHAHologramManager.updateSpatialMap(NostalgiaServerCollisionBypassProvider.INSTANCE);
                    }
                    for (Entity e : entities) {
                        if (e instanceof net.minecraft.server.level.ServerPlayer sp) {
                            net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(sp, new net.nostalgia.network.S2CRitualPhasePayload(id, 3));
                        }
                    }
                }
            } else if (phase == 3) {
                long requiredDelay = 4500;
                if (activeMs - phaseStartTime > requiredDelay) {
                    int maxY = targetServerLevel != null ? targetServerLevel.getMaxY() : 320;
                    BlockPos targetBeaconPos = beaconPos != null ? net.nostalgia.alphalogic.ritual.CoordinateMapper.sourceToTarget(beaconPos, offsetX, yOffset, offsetZ, false, maxY) : null;
                    boolean isHeadingToRD = DimensionUtil.isRD(targetDimensionId);

                    if (!isHeadingToRD && targetBeaconPos != null && targetServerLevel != null) {
                        net.minecraft.core.BlockPos surfacePos = new BlockPos(targetBeaconPos.getX(), targetBeaconPos.getY() - 1, targetBeaconPos.getZ());
                        targetServerLevel.getChunk(surfacePos.getX() >> 4, surfacePos.getZ() >> 4, net.minecraft.world.level.chunk.status.ChunkStatus.FULL, true);
                        net.minecraft.world.level.block.state.BlockState surfaceState = targetServerLevel.getBlockState(surfacePos);
                        if (!surfaceState.getFluidState().isEmpty()) {
                            int radius = 5;
                            boolean isLava = surfaceState.getFluidState().is(net.minecraft.tags.FluidTags.LAVA);
                            net.minecraft.world.level.block.state.BlockState platformBlock = isLava ? net.minecraft.world.level.block.Blocks.OBSIDIAN.defaultBlockState() : net.minecraft.world.level.block.Blocks.ICE.defaultBlockState();

                            for (int dx = -radius; dx <= radius; dx++) {
                                for (int dz = -radius; dz <= radius; dz++) {
                                    if (dx * dx + dz * dz <= radius * radius) {
                                        BlockPos icePos = surfacePos.offset(dx, 0, dz);
                                        targetServerLevel.getChunk(icePos.getX() >> 4, icePos.getZ() >> 4, net.minecraft.world.level.chunk.status.ChunkStatus.FULL, true);
                                        net.minecraft.world.level.block.state.BlockState curState = targetServerLevel.getBlockState(icePos);
                                        if (!curState.getFluidState().isEmpty() || curState.isAir()) {
                                            targetServerLevel.setBlock(icePos, platformBlock, 3);
                                            if (DimensionUtil.isClientGenerated(targetServerLevel.dimension().identifier().toString())) {
                                                HologramWorldData.get(targetServerLevel).addDelta(icePos, platformBlock);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    List<BlockPos> positionsToSync = new java.util.ArrayList<>();
                    for (Map.Entry<BlockPos, BlockState> entry : deltaCache.getAll().entrySet()) {
                        BlockPos vPos = entry.getKey();
                        BlockPos localizedPos = net.nostalgia.alphalogic.ritual.CoordinateMapper.sourceToTarget(vPos, offsetX, yOffset, offsetZ, false, maxY);
                        if (!DimensionUtil.isRD(targetDimensionId)) {
                            targetServerLevel.getChunk(localizedPos.getX() >> 4, localizedPos.getZ() >> 4, net.minecraft.world.level.chunk.status.ChunkStatus.FULL, true);
                            targetServerLevel.setBlock(localizedPos, entry.getValue(), 3);
                            positionsToSync.add(localizedPos);

                            if (beaconPos != null && vPos.equals(beaconPos) && beaconNbt != null) {
                                beaconNbt.putInt("x", localizedPos.getX());
                                beaconNbt.putInt("y", localizedPos.getY());
                                beaconNbt.putInt("z", localizedPos.getZ());
                                net.minecraft.world.level.block.entity.BlockEntity targetBe = net.minecraft.world.level.block.entity.BlockEntity.loadStatic(localizedPos, entry.getValue(), beaconNbt, targetServerLevel.registryAccess());
                                if (targetBe != null) {
                                    targetServerLevel.setBlockEntity(targetBe);
                                }
                            }
                        }
                    }
                    deltaCache.clear();


                    if (targetBeaconPos != null) {
                        this.beaconPos = targetBeaconPos;
                        EchoRitualManager.clearSelection(null);
                    }

                    for (Entity entity : entities) {
                        if (!entity.isAlive()) continue;
                        net.minecraft.world.phys.Vec3 motion = entity.getDeltaMovement();
                        double newX = entity.getX() + offsetX;
                        double newZ = entity.getZ() + offsetZ;
                        double newY;
                        if (entity instanceof net.minecraft.server.level.ServerPlayer sp) {
                            double calculatedY;
                            calculatedY = entity.getY() - yOffset;
                            newY = EchoRitualManager.calculateSafeYAndApplyEffects(entity, targetServerLevel, newX, calculatedY, newZ);
                            if (sp.containerMenu != null && sp.containerMenu != sp.inventoryMenu) {
                                sp.closeContainer();
                            }
                            sp.teleportTo(targetServerLevel, newX, newY, newZ, java.util.Collections.emptySet(), sp.getYRot(), sp.getXRot(), true);
                            if (sp.isCreative() && !sp.getAbilities().mayfly) {
                                sp.getAbilities().mayfly = true;
                                sp.onUpdateAbilities();
                            }
                        } else {
                            newY = EchoRitualManager.calculateSafeYAndApplyEffects(entity, targetServerLevel, newX, entity.getY() - yOffset, newZ);
                            entity.teleportTo(targetServerLevel, newX, newY, newZ, java.util.Collections.emptySet(), entity.getYRot(), entity.getXRot(), true);
                        }
                        entity.setDeltaMovement(motion);
                        if (entity instanceof net.minecraft.world.entity.player.Player p) p.hurtMarked = true;
                    }

                    transitioning = false;

                    for (Entity entity : entities) {
                        if (entity instanceof net.minecraft.server.level.ServerPlayer sp) {
                            net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(sp, new net.nostalgia.network.S2CEndTransitionVisualsPayload(id));
                        }
                    }

                    java.util.Set<UUID> participantSet = new java.util.HashSet<>();
                    for (Entity entity : entities) {
                        participantSet.add(entity.getUUID());
                    }
                    if (sourceLevel != null) {
                        for (net.minecraft.world.entity.player.Player lp : sourceLevel.players()) {
                            if (lp instanceof net.minecraft.server.level.ServerPlayer sp && !participantSet.contains(sp.getUUID())) {
                                net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(sp, new net.nostalgia.network.S2CEndTransitionVisualsPayload(id));
                            }
                        }
                    }

                    EchoRitualManager.endRitualForInstance(this);
                }
            }
        }

        if (state == EchoRitualManager.State.INACTIVE) return;

        if (state == EchoRitualManager.State.TIME_RESUMING_DELAY) {
            long elapsed = activeMs - timeStopStartTime;
            if (elapsed >= 5000) {
                if (sourceLevel != null && beaconPos != null) {
                    TimestopZoneManager.removeZone(sourceLevel, beaconPos);
                }
                EchoRitualManager.triggerTimeResumeForInstance(this);
            }
        }

        if (state == EchoRitualManager.State.TIME_STOPPING) {
            long elapsed = activeMs - timeStopStartTime;
            if (elapsed == 0 && sourceLevel != null) {
                sourceLevel.getServer().tickRateManager().setTickRate(1.0f);
            }
            if (elapsed >= 2000) {
                EchoRitualManager.transitionToFrozenForInstance(this);
            }
        } else if (state == EchoRitualManager.State.TIME_RESUMING) {
            long elapsed = activeMs - timeStopStartTime;
            if (elapsed >= 2000) {
                EchoRitualManager.endRitualForInstance(this);
            }
        }
    }
}
