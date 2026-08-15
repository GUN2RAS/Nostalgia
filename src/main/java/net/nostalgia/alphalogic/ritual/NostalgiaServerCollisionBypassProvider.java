package net.nostalgia.alphalogic.ritual;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.nostalgia.alphalogic.ritual.event.EchoRitualEvent;
import net.nostalgia.alphalogic.ritual.event.RitualEventRegistry;
import net.sha.api.HologramBounds;
import net.sha.api.HologramProvider;

public class NostalgiaServerCollisionBypassProvider implements HologramProvider {
  public static final NostalgiaServerCollisionBypassProvider INSTANCE = new NostalgiaServerCollisionBypassProvider();
  public static final ThreadLocal<Boolean> IS_OVERWORLD = ThreadLocal.withInitial(() -> false);
  private static final int BOUNDS_RADIUS = 300;

  public NostalgiaServerCollisionBypassProvider() {
  }

  private static boolean isServerThread() {
    return "Server thread".equals(Thread.currentThread().getName());
  }

  public boolean isActive() {
    if (!isServerThread()) {
      return false;
    } else if (!IS_OVERWORLD.get()) {
      return false;
    } else {
      EchoRitualEvent t = RitualEventRegistry.activeTransition();
      if (t == null) {
        return false;
      } else {
        return t.beaconPos() == null ? false : t.phase() >= 3;
      }
    }
  }

  public boolean providesCollision() {
    return true;
  }

  public HologramBounds getBounds() {
    EchoRitualEvent t = RitualEventRegistry.activeTransition();
    if (t == null) {
      return null;
    } else {
      BlockPos center = t.beaconPos();
      if (center == null) {
        return null;
      } else {
        int minX = center.getX() - 300;
        int maxX = center.getX() + 300;
        int minY = Math.max(-64, center.getY() - 300);
        int maxY = Math.min(320, center.getY() + 300);
        int minZ = center.getZ() - 300;
        int maxZ = center.getZ() + 300;
        return new HologramBounds(minX, minY, minZ, maxX, maxY, maxZ);
      }
    }
  }

  public BlockState getSpoofedBlock(int worldX, int y, int worldZ) {
    if (!isServerThread()) {
      return null;
    }
    BlockPos pos = new BlockPos(worldX, y, worldZ);
    for (net.nostalgia.alphalogic.ritual.SkyPortalEventInstance portal : net.nostalgia.alphalogic.ritual.SkyPortalManager.allPortals()) {
      if (portal != null && portal.isActive()) {
        if (pos.equals(portal.center()) || pos.equals(portal.center().below())) {
          return null;
        }
      }
    }
    return Blocks.AIR.defaultBlockState();
  }
}
