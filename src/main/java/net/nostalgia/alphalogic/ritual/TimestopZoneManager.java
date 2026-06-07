package net.nostalgia.alphalogic.ritual;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.nostalgia.alphalogic.ritual.event.RitualEventRegistry;

public class TimestopZoneManager {
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

    private static int structureCheckTick = 0;

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
                    EchoRitualEventInstance activeInst = RitualEventRegistry.findInstanceByBeacon(bPos);
                    if (activeInst != null && activeInst.state() != EchoRitualManager.State.INACTIVE) {
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

    public static void tickActiveZones(net.minecraft.server.MinecraftServer server) {
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
                            EchoRitualEventInstance activeInst = RitualEventRegistry.findInstanceByBeacon(bPos);
                            if (activeInst != null && activeInst.state() != EchoRitualManager.State.INACTIVE) {
                                EchoRitualManager.handleInterrupt(bPos);
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
}
