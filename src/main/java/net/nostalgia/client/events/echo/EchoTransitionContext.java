package net.nostalgia.client.events.echo;

import net.minecraft.core.BlockPos;
import net.nostalgia.alphalogic.ritual.DimensionUtil;
import net.nostalgia.alphalogic.ritual.event.ClientEchoRitualView;
import net.nostalgia.alphalogic.ritual.event.RitualEventRegistry;
import net.nostalgia.client.events.caches.UniversalHologramCache;
import net.nostalgia.client.events.core.ClientRitualEventRegistry;
import net.nostalgia.client.events.core.IHologramContext;

public class EchoTransitionContext implements IHologramContext {
  public static final EchoTransitionContext INSTANCE = new EchoTransitionContext();

  private EchoTransitionContext() {
  }

  @Override
  public boolean isActive() {
    ClientEchoRitualView transition = ClientRitualEventRegistry.activeTransition();
    if (transition == null) {
      return false;
    } else {
      boolean inNew = transition.isInNewDimension();
      return inNew ? false : transition.isBystander() || RitualVisualManager.currentPhase >= 1;
    }
  }

  @Override
  public boolean contains(int x, int y, int z) {
    ClientEchoRitualView transition = ClientRitualEventRegistry.activeTransition();
    if (transition == null) {
      return false;
    } else {
      BlockPos center = transition.ritualCenter();
      if (center == null) {
        return false;
      } else {
        float currentRadius = this.getCollisionRadius();
        if (currentRadius <= 0.01F) {
          return false;
        } else {
          double dx = x - center.getX();
          double dz = z - center.getZ();
          long h = x * 73856093L ^ y * 19349663L ^ z * 83492791L;
          double noise = (h & 16777215L) / 1.6777215E7 * 2.0 - 1.0;
          double distSqXZ = dx * dx + dz * dz;
          double threshold = currentRadius - noise * 2.0;
          return threshold < 0.0 || distSqXZ > threshold * threshold ? false : y >= -64 && y <= 320;
        }
      }
    }
  }

  @Override
  public BlockPos getCenter() {
    ClientEchoRitualView transition = ClientRitualEventRegistry.activeTransition();
    return transition != null ? transition.ritualCenter() : null;
  }

  @Override
  public float getRadius() {
    ClientEchoRitualView transition = ClientRitualEventRegistry.activeTransition();
    return transition != null ? transition.alphaRadius() : 0.0F;
  }

  @Override
  public int getOffsetX() {
    return RitualEventRegistry.offsetX();
  }

  @Override
  public int getOffsetY() {
    return RitualEventRegistry.yOffset();
  }

  @Override
  public int getOffsetZ() {
    return RitualEventRegistry.offsetZ();
  }

  @Override
  public String getTargetDimension() {
    ClientEchoRitualView transition = ClientRitualEventRegistry.activeTransition();
    return transition == null ? null : DimensionUtil.normalize(transition.targetDimension());
  }

  @Override
  public boolean isSkyInverted() {
    return false;
  }

  @Override
  public boolean isTerrainActive() {
    ClientEchoRitualView transition = ClientRitualEventRegistry.activeTransition();
    return transition != null && transition.currentPhase() >= 3;
  }

  @Override
  public float getCollisionRadius() {
    return UniversalHologramCache.decoupledCollision ? UniversalHologramCache.customCollisionRadius : this.getRadius();
  }
}
