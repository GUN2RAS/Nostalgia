package net.nostalgia.alphalogic.ritual;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.nostalgia.alphalogic.ritual.event.RitualEventRegistry;
import net.nostalgia.network.S2CTimestopZoneEndPayload;
import net.nostalgia.network.S2CTimestopZoneStartPayload;

public class TimestopZoneManager {
  public static final int ZONE_RADIUS_CHUNKS = 5;
  public static final CopyOnWriteArrayList<TimestopZoneManager.ActiveZone> activeZones = new CopyOnWriteArrayList<>();
  private static int structureCheckTick = 0;

  public TimestopZoneManager() {
  }

  public static boolean hasAnyRainingZone(ResourceKey<Level> dimension) {
    for (TimestopZoneManager.ActiveZone zone : activeZones) {
      if (zone.dimension() == dimension && zone.snapRain() > 0.0F) {
        return true;
      }
    }

    return false;
  }

  public static float getLocalRainLevel(ResourceKey<Level> dimension, BlockPos pos) {
    TimestopZoneManager.ActiveZone zone = getZoneAt(dimension, pos);
    return zone != null ? zone.snapRain() : -1.0F;
  }

  public static TimestopZoneManager.ActiveZone getZoneAt(ResourceKey<Level> dimension, BlockPos pos) {
    for (TimestopZoneManager.ActiveZone zone : activeZones) {
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

  public static List<TimestopZoneManager.ActiveZone> getActiveZones() {
    return Collections.unmodifiableList(activeZones);
  }

  public static boolean hasActiveZones() {
    return !activeZones.isEmpty();
  }

  public static TimestopZoneManager.ActiveZone findZoneByBeacon(BlockPos beaconPos) {
    if (beaconPos == null) {
      return null;
    } else {
      for (TimestopZoneManager.ActiveZone z : activeZones) {
        if (z.beaconPos.equals(beaconPos)) {
          return z;
        }
      }

      return null;
    }
  }

  public static TimestopZoneManager.ActiveZone findZoneContaining(ResourceKey<Level> dim, BlockPos pos) {
    if (dim != null && pos != null) {
      int cx = pos.getX() >> 4;
      int cz = pos.getZ() >> 4;

      for (TimestopZoneManager.ActiveZone z : activeZones) {
        if (z.dimension.equals(dim)) {
          int bx = z.beaconPos.getX() >> 4;
          int bz = z.beaconPos.getZ() >> 4;
          if (Math.max(Math.abs(cx - bx), Math.abs(cz - bz)) <= z.radiusChunks) {
            return z;
          }
        }
      }

      return null;
    } else {
      return null;
    }
  }

  public static boolean hasActiveZone() {
    return hasActiveZones();
  }

  public static void addZone(ServerLevel level, BlockPos beaconPos, boolean applyPhysicsFreeze) {
    if (level != null && beaconPos != null) {
      if (findZoneByBeacon(beaconPos) == null) {
        if (applyPhysicsFreeze && level.tickRateManager() instanceof TickRateManagerAccess access) {
          access.nostalgia$addRegion(new FreezeRegion(level.dimension(), beaconPos, 5));
        }

        TimestopZoneManager.ActiveZone zone = new TimestopZoneManager.ActiveZone(
          level.dimension(), beaconPos.immutable(), 5, level.getGameTime(), level.getDefaultClockTime(), level.getRainLevel(1.0F), level.getThunderLevel(1.0F)
        );
        activeZones.add(zone);
        persistZones(level);
      }
    }
  }

  public static void persistZones(ServerLevel level) {
    if (level != null) {
      List<ZoneSavedData.ZoneEntry> entries = new ArrayList<>();

      for (TimestopZoneManager.ActiveZone z : activeZones) {
        entries.add(
          new ZoneSavedData.ZoneEntry(
            z.beaconPos, z.radiusChunks, z.dimension.identifier().toString(), z.snapGameTime, z.snapClockTicks, z.snapRain, z.snapThunder
          )
        );
      }

      ZoneSavedData data = ZoneSavedData.get(level);
      data.updateZones(entries);
    }
  }

  public static void loadZones(MinecraftServer server) {
    if (server != null) {
      ServerLevel overworld = server.getLevel(Level.OVERWORLD);
      if (overworld != null) {
        ZoneSavedData data = ZoneSavedData.get(overworld);
        if (data != null && !data.zones.isEmpty()) {
          activeZones.clear();

          for (ZoneSavedData.ZoneEntry e : data.zones) {
            ResourceKey<Level> dim = ResourceKey.create(Registries.DIMENSION, Identifier.tryParse(e.dimensionId()));
            TimestopZoneManager.ActiveZone zone = new TimestopZoneManager.ActiveZone(
              dim, e.beaconPos(), e.radiusChunks(), e.snapGameTime(), e.snapClockTicks(), e.snapRain(), e.snapThunder()
            );
            activeZones.add(zone);
            ServerLevel target = server.getLevel(dim);
            if (target != null && target.tickRateManager() instanceof TickRateManagerAccess access) {
              access.nostalgia$addRegion(new FreezeRegion(dim, e.beaconPos(), e.radiusChunks()));
            }
          }
        }
      }
    }
  }

  public static void sendZonesToPlayer(ServerPlayer player) {
    if (player != null && !activeZones.isEmpty()) {
      ResourceKey<Level> dim = player.level().dimension();
      TimestopZoneManager.ActiveZone containing = findZoneContaining(dim, player.blockPosition());

      for (TimestopZoneManager.ActiveZone z : activeZones) {
        if (z.dimension.equals(dim)) {
          boolean instant = z == containing;
          S2CTimestopZoneStartPayload payload = new S2CTimestopZoneStartPayload(
            z.beaconPos, z.radiusChunks, z.dimension.identifier().toString(), instant, z.snapGameTime, z.snapClockTicks, z.snapRain, z.snapThunder
          );
          ServerPlayNetworking.send(player, payload);
        }
      }
    }
  }

  public static void sendZoneToPlayer(ServerPlayer player) {
    sendZonesToPlayer(player);
  }

  public static void removeZone(ServerLevel level, BlockPos beaconPos) {
    if (level != null && beaconPos != null) {
      if (level.tickRateManager() instanceof TickRateManagerAccess access) {
        access.nostalgia$removeRegionAt(level.dimension(), beaconPos);
      }

      activeZones.removeIf(z -> z.beaconPos.equals(beaconPos));
      persistZones(level);
      S2CTimestopZoneEndPayload payload = new S2CTimestopZoneEndPayload(beaconPos);

      for (ServerPlayer sp : level.getServer().getPlayerList().getPlayers()) {
        ServerPlayNetworking.send(sp, payload);
      }

      BlockState bs = level.getBlockState(beaconPos);
      level.sendBlockUpdated(beaconPos, bs, bs, 3);
    }
  }

  public static void eagerlyRemoveBrokenZones(ServerLevel level) {
    if (level != null) {
      boolean removedAny = false;

      for (TimestopZoneManager.ActiveZone zone : activeZones) {
        if (zone.dimension() == level.dimension()) {
          BlockPos bPos = zone.beaconPos();
          if (level.isLoaded(bPos)) {
            BlockState bState = level.getBlockState(bPos);
            BlockState aState = level.getBlockState(bPos.below());
            boolean isValid = bState.is(Blocks.BEACON)
              && aState.is(Blocks.RESPAWN_ANCHOR)
              && aState.hasProperty(RespawnAnchorBlock.CHARGE)
              && (Integer)aState.getValue(RespawnAnchorBlock.CHARGE) == 4;
            if (!isValid) {
              EchoRitualEventInstance activeInst = RitualEventRegistry.findInstanceByBeacon(bPos);
              if (activeInst == null || activeInst.state() == EchoRitualManager.State.INACTIVE) {
                if (level.tickRateManager() instanceof TickRateManagerAccess access) {
                  access.nostalgia$removeRegionAt(level.dimension(), bPos);
                }

                activeZones.remove(zone);
                removedAny = true;
                S2CTimestopZoneEndPayload payload = new S2CTimestopZoneEndPayload(bPos);

                for (ServerPlayer sp : level.getServer().getPlayerList().getPlayers()) {
                  ServerPlayNetworking.send(sp, payload);
                }

                BlockState bs = level.getBlockState(bPos);
                level.sendBlockUpdated(bPos, bs, bs, 3);
                ItemEntity crystal = new ItemEntity(level, bPos.getX() + 0.5, bPos.getY() + 1.5, bPos.getZ() + 0.5, new ItemStack(Items.ECHO_SHARD));
                crystal.setDefaultPickUpDelay();
                level.addFreshEntity(crystal);
              }
            }
          }
        }
      }

      if (removedAny) {
        persistZones(level);
      }
    }
  }

  public static boolean checkZoneStability(ServerLevel level, BlockPos pos) {
    eagerlyRemoveBrokenZones(level);
    int cx = pos.getX() >> 4;
    int cz = pos.getZ() >> 4;

    for (TimestopZoneManager.ActiveZone zone : activeZones) {
      if (zone.dimension().equals(level.dimension()) && !zone.beaconPos().equals(pos)) {
        int zcx = zone.beaconPos().getX() >> 4;
        int zcz = zone.beaconPos().getZ() >> 4;
        if (Math.abs(cx - zcx) <= 10 && Math.abs(cz - zcz) <= 10) {
          return false;
        }
      }
    }

    return true;
  }

  public static void tickActiveZones(MinecraftServer server) {
    structureCheckTick++;
    if (structureCheckTick % 20 == 0) {
      for (TimestopZoneManager.ActiveZone zone : new ArrayList<>(activeZones)) {
        ServerLevel zoneLevel = server.getLevel(zone.dimension());
        if (zoneLevel != null) {
          BlockPos bPos = zone.beaconPos();
          if (zoneLevel.isLoaded(bPos)) {
            BlockState bState = zoneLevel.getBlockState(bPos);
            BlockState aState = zoneLevel.getBlockState(bPos.below());
            boolean isValid = bState.is(Blocks.BEACON)
              && aState.is(Blocks.RESPAWN_ANCHOR)
              && aState.hasProperty(RespawnAnchorBlock.CHARGE)
              && (Integer)aState.getValue(RespawnAnchorBlock.CHARGE) == 4;
            if (!isValid) {
              EchoRitualEventInstance activeInst = RitualEventRegistry.findInstanceByBeacon(bPos);
              if (activeInst != null && activeInst.state() != EchoRitualManager.State.INACTIVE) {
                EchoRitualManager.handleInterrupt(bPos);
              } else {
                removeZone(zoneLevel, bPos);
                ItemEntity crystal = new ItemEntity(zoneLevel, bPos.getX() + 0.5, bPos.getY() + 1.5, bPos.getZ() + 0.5, new ItemStack(Items.ECHO_SHARD));
                crystal.setDefaultPickUpDelay();
                zoneLevel.addFreshEntity(crystal);
              }
            }
          }
        }
      }
    }
  }

  public record ActiveZone(
    ResourceKey<Level> dimension, BlockPos beaconPos, int radiusChunks, long snapGameTime, long snapClockTicks, float snapRain, float snapThunder
  ) {
  }
}
