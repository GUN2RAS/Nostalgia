package net.nostalgia.client.events.core;

import java.util.concurrent.ConcurrentHashMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.nostalgia.alphalogic.ritual.FreezeRegion;
import net.nostalgia.alphalogic.ritual.TickRateManagerAccess;

@Environment(EnvType.CLIENT)
public class ClientFreezeRegions {
  public static final ConcurrentHashMap<BlockPos, ClientFreezeRegions.ZoneSnapshot> snapshots = new ConcurrentHashMap<>();

  public ClientFreezeRegions() {
  }

  public static TickRateManagerAccess access() {
    Minecraft mc = Minecraft.getInstance();
    ClientLevel level = mc.level;
    if (level == null) {
      return null;
    } else {
      return level.tickRateManager() instanceof TickRateManagerAccess a ? a : null;
    }
  }

  public static void clearAll() {
    snapshots.clear();
    TickRateManagerAccess a = access();
    if (a != null) {
      a.nostalgia$clearRegions();
    }
  }

  public static boolean hasRegions() {
    TickRateManagerAccess a = access();
    return a != null && a.nostalgia$hasRegions();
  }

  public static boolean isLocalPlayerInZone() {
    Minecraft mc = Minecraft.getInstance();
    TickRateManagerAccess a = access();
    return a != null && mc.player != null && mc.level != null ? a.nostalgia$isChunkFrozen(mc.level.dimension(), mc.player.chunkPosition()) : false;
  }

  public static boolean isRitualBeacon(BlockPos pos) {
    if (pos == null) {
      return false;
    } else {
      Minecraft mc = Minecraft.getInstance();
      TickRateManagerAccess a = access();
      if (a != null && mc.level != null) {
        ResourceKey<Level> dim = mc.level.dimension();

        for (FreezeRegion r : a.nostalgia$regions()) {
          if (r.dimension().equals(dim) && r.beaconPos().equals(pos)) {
            return true;
          }
        }

        return false;
      } else {
        return false;
      }
    }
  }

  public record ZoneSnapshot(long gameTime, long clockTicks, float rain, float thunder) {
  }
}
