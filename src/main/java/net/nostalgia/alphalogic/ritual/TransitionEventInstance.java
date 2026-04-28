package net.nostalgia.alphalogic.ritual;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.nostalgia.alphalogic.ritual.event.TransitionEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class TransitionEventInstance implements TransitionEvent {
    private final UUID id;
    private BlockPos beaconPos;
    private BlockPos targetPos;
    private String targetDimensionId = "";
    private ServerLevel sourceLevel;
    private ServerLevel targetServerLevel;

    private int offsetX;
    private int yOffset;
    private int offsetZ;

    private int phase;
    private long phaseStartTime;
    private boolean transitioning;
    private long timeStopStartTime;
    private RitualManager.State state = RitualManager.State.INACTIVE;

    private long activeMs = 0;

    public long activeMs() { return activeMs; }
    public void addActiveMs(long dt) { this.activeMs += dt; }

    private final Set<UUID> participants = ConcurrentHashMap.newKeySet();
    private final Set<UUID> readyClients = ConcurrentHashMap.newKeySet();
    private final Set<UUID> clientsReadyForNextPhase = ConcurrentHashMap.newKeySet();
    private final Map<UUID, Integer> clientHologramSurfaces = new ConcurrentHashMap<>();
    private final List<Entity> entities = new ArrayList<>();

    public TransitionEventInstance(UUID id, BlockPos beaconPos, ServerLevel sourceLevel) {
        this.id = id;
        this.beaconPos = beaconPos;
        this.sourceLevel = sourceLevel;
    }

    @Override
    public UUID id() { return id; }

    @Override
    public RitualManager.State state() { return state; }

    public void setState(RitualManager.State newState) { this.state = newState; }

    @Override
    public BlockPos beaconPos() { return beaconPos; }

    public void setBeaconPos(BlockPos pos) { this.beaconPos = pos; }

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

    @Override
    public void cachePut(BlockPos pos, BlockState state) { VirtualBlockCache.put(pos, state); }

    @Override
    public BlockState cacheGet(BlockPos pos) { return VirtualBlockCache.get(pos); }

    @Override
    public boolean cacheHas(BlockPos pos) { return VirtualBlockCache.has(pos); }

    @Override
    public void cacheClear() { VirtualBlockCache.clear(); }

    @Override
    public Map<BlockPos, BlockState> cacheEntries() { return VirtualBlockCache.getAll(); }

    public void tick(long dt, net.minecraft.server.MinecraftServer server) {
        if (state != RitualManager.State.INACTIVE) {
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
                            net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(sp, new net.nostalgia.network.S2CRitualPhasePayload(2));
                        }
                    }
                }
            } else if (phase == 2) {
                if (activeMs - phaseStartTime > 6500) {
                    phase = 3;
                    phaseStartTime = activeMs;
                    if (net.fabricmc.loader.api.FabricLoader.getInstance().isModLoaded("sha")) {
                        net.sha.api.SHAHologramManager.updateSpatialMap(NostalgiaServerCollisionBypassProvider.INSTANCE);
                    }
                    for (Entity e : entities) {
                        if (e instanceof net.minecraft.server.level.ServerPlayer sp) {
                            net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(sp, new net.nostalgia.network.S2CRitualPhasePayload(3));
                        }
                    }
                }
            } else if (phase == 3) {
                long requiredDelay = 6500;
                if ("overworld".equals(targetDimensionId)) {
                    requiredDelay = 2500;
                }
                if (activeMs - phaseStartTime > requiredDelay) {
                    for (Map.Entry<BlockPos, BlockState> entry : VirtualBlockCache.getAll().entrySet()) {
                        BlockPos vPos = entry.getKey();
                        BlockPos localizedPos = new BlockPos(
                            vPos.getX() + offsetX,
                            vPos.getY() - yOffset,
                            vPos.getZ() + offsetZ
                        );
                        targetServerLevel.getChunk(localizedPos.getX() >> 4, localizedPos.getZ() >> 4, net.minecraft.world.level.chunk.status.ChunkStatus.FULL, true);
                        targetServerLevel.setBlock(localizedPos, entry.getValue(), 3);
                        if (targetServerLevel.dimension() == net.nostalgia.world.dimension.ModDimensions.ALPHA_112_01_LEVEL_KEY) {
                            AlphaWorldData.get(targetServerLevel).addDelta(localizedPos, entry.getValue());
                        }
                    }
                    VirtualBlockCache.clear();

                    for (Entity entity : entities) {
                        if (!entity.isAlive()) continue;
                        net.minecraft.world.phys.Vec3 motion = entity.getDeltaMovement();
                        double newX = entity.getX() + offsetX;
                        double newZ = entity.getZ() + offsetZ;
                        double newY;
                        if (entity instanceof net.minecraft.server.level.ServerPlayer sp) {
                            if (sp.level().dimension() == net.nostalgia.world.dimension.ModDimensions.ALPHA_112_01_LEVEL_KEY) {
                                newY = entity.getY() - yOffset;
                            } else {
                                int reportedClientAlphaY = clientHologramSurfaces.getOrDefault(sp.getUUID(), -1);
                                if (reportedClientAlphaY != -1) {
                                    int expectedHologramY = reportedClientAlphaY + yOffset;
                                    double dy = entity.getY() - expectedHologramY;
                                    if (dy < 0) dy = 0;
                                    newY = reportedClientAlphaY + dy;
                                } else {
                                    newY = entity.getY() - yOffset;
                                }
                            }
                            if (sp.level().dimension() != net.nostalgia.world.dimension.ModDimensions.ALPHA_112_01_LEVEL_KEY) {
                                if (sp.containerMenu != null && sp.containerMenu != sp.inventoryMenu) {
                                    sp.closeContainer();
                                }
                            }
                            sp.teleportTo(targetServerLevel, newX, newY, newZ, java.util.Collections.emptySet(), sp.getYRot(), sp.getXRot(), true);
                            if (sp.isCreative() && !sp.getAbilities().mayfly) {
                                sp.getAbilities().mayfly = true;
                                sp.onUpdateAbilities();
                            }
                        } else {
                            newY = entity.getY();
                            entity.teleportTo(targetServerLevel, newX, newY, newZ, java.util.Collections.emptySet(), entity.getYRot(), entity.getXRot(), true);
                        }
                        entity.setDeltaMovement(motion);
                        if (entity instanceof net.minecraft.world.entity.player.Player p) p.hurtMarked = true;
                    }

                    transitioning = false;

                    for (Entity entity : entities) {
                        if (entity instanceof net.minecraft.server.level.ServerPlayer sp) {
                            net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(sp, new net.nostalgia.network.S2CEndTransitionVisualsPayload());
                        }
                    }

                    java.util.Set<UUID> participantSet = new java.util.HashSet<>();
                    for (Entity entity : entities) {
                        participantSet.add(entity.getUUID());
                    }
                    if (sourceLevel != null) {
                        for (net.minecraft.world.entity.player.Player lp : sourceLevel.players()) {
                            if (lp instanceof net.minecraft.server.level.ServerPlayer sp && !participantSet.contains(sp.getUUID())) {
                                net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(sp, new net.nostalgia.network.S2CEndTransitionVisualsPayload());
                            }
                        }
                    }

                    RitualManager.endRitualForInstance(this);
                }
            }
        }

        if (state == RitualManager.State.INACTIVE) return;

        if (state == RitualManager.State.TIME_RESUMING_DELAY) {
            long elapsed = activeMs - timeStopStartTime;
            if (elapsed >= 5000) {
                if (sourceLevel != null && beaconPos != null) {
                    RitualManager.removeZone(sourceLevel, beaconPos);
                }
                RitualManager.triggerTimeResumeForInstance(this);
            }
        }

        if (state == RitualManager.State.TIME_STOPPING) {
            long elapsed = activeMs - timeStopStartTime;
            if (elapsed == 0 && sourceLevel != null) {
                sourceLevel.getServer().tickRateManager().setTickRate(1.0f);
            }
            if (elapsed >= 2000) {
                RitualManager.transitionToFrozenForInstance(this);
            }
        } else if (state == RitualManager.State.TIME_RESUMING) {
            long elapsed = activeMs - timeStopStartTime;
            if (elapsed >= 2000) {
                RitualManager.endRitualForInstance(this);
            }
        }
    }
}
