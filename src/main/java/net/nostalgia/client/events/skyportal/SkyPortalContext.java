package net.nostalgia.client.events.skyportal;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.nostalgia.alphalogic.ritual.DimensionUtil;
import net.nostalgia.alphalogic.ritual.event.SkyPortalEvent;
import net.nostalgia.client.events.core.IHologramContext;
import net.nostalgia.client.events.echo.EchoTransitionContext;
import net.nostalgia.client.events.echo.RitualVisualManager;
import net.nostalgia.client.render.PortalSkyRenderer;

public class SkyPortalContext implements IHologramContext {
  public static final SkyPortalContext INSTANCE = new SkyPortalContext();

  private SkyPortalContext() {
  }

  @Override
  public boolean isActive() {
    if (EchoTransitionContext.INSTANCE.isActive()) {
      return false;
    }
    if (PortalSkyRenderer.skyPortalTransitioning) {
      return true;
    } else {
      SkyPortalEvent skyPortal = MonolithicSkyPortalEvent.activeOrNull();
      return skyPortal != null && skyPortal.islandVisible();
    }
  }

  @Override
  public boolean contains(int x, int y, int z) {
    Minecraft mc = Minecraft.getInstance();
    var level = mc.level;
    if (level == null) return false;
    String currentDim = level.dimension().identifier().toString();
    if (PortalSkyRenderer.active && PortalSkyRenderer.portalCenter != null) {
      String src = PortalSkyRenderer.originalSourceDimension;
      String tgt = PortalSkyRenderer.originalTargetDimension;
      if (src != null && tgt != null && (currentDim.equals(src) || currentDim.equals(tgt))) {
        boolean isTarget = currentDim.equals(tgt);
        int currentCrackPlaneY = isTarget ? PortalSkyRenderer.crackPlaneYTarget : PortalSkyRenderer.crackPlaneY;
        if (y > currentCrackPlaneY) {
          BlockPos center = PortalSkyRenderer.portalCenter;
          double dx = x - center.getX();
          double dz = z - center.getZ();
          long h = x * 73856093L ^ y * 19349663L ^ z * 83492791L;
          double noise = (h & 16777215L) / 1.6777215E7 * 2.0 - 1.0;
          double distSq = dx * dx + dz * dz;
          double r = RitualVisualManager.getPortalAlphaRadius();
          double threshold = r - noise * 2.0;
          if (threshold >= 0.0 && distSq <= threshold * threshold) {
            return true;
          }
        }
      }
    }
    for (ClientSkyPortalData portal : net.nostalgia.network.NostalgiaClientNetworking.clientPortals.values()) {
      if (currentDim.equals(portal.sourceDimension()) || currentDim.equals(portal.targetDimension())) {
        boolean isTarget = currentDim.equals(portal.targetDimension());
        int currentCrackPlaneY = isTarget ? portal.crackPlaneYTarget() : portal.crackPlaneY();
        if (y > currentCrackPlaneY) {
          BlockPos center = portal.center();
          if (center != null) {
            double dx = x - center.getX();
            double dz = z - center.getZ();
            long h = x * 73856093L ^ y * 19349663L ^ z * 83492791L;
            double noise = (h & 16777215L) / 1.6777215E7 * 2.0 - 1.0;
            double distSq = dx * dx + dz * dz;
            double r = RitualVisualManager.getPortalAlphaRadius();
            double threshold = r - noise * 2.0;
            if (threshold >= 0.0 && distSq <= threshold * threshold) {
              return true;
            }
          }
        }
      }
    }
    return false;
  }

  @Override
  public BlockPos getCenter() {
    SkyPortalEvent skyPortal = MonolithicSkyPortalEvent.activeOrNull();
    return skyPortal != null ? skyPortal.center() : null;
  }

  @Override
  public float getRadius() {
    return RitualVisualManager.getPortalAlphaRadius();
  }

  @Override
  public int getOffsetX() {
    return 0;
  }

  @Override
  public int getOffsetY() {
    return 0;
  }

  @Override
  public int getOffsetZ() {
    return 0;
  }

  @Override
  public String getTargetDimension() {
    SkyPortalEvent skyPortal = MonolithicSkyPortalEvent.activeOrNull();
    if (skyPortal == null) {
      return null;
    } else if (Minecraft.getInstance().level != null) {
      String currentDim = Minecraft.getInstance().level.dimension().identifier().toString();
      String tDim = DimensionUtil.normalize(skyPortal.targetDimension());
      String sDim = DimensionUtil.normalize(skyPortal.sourceDimension());
      return currentDim.equals(tDim) ? sDim : tDim;
    } else {
      return DimensionUtil.normalize(skyPortal.targetDimension());
    }
  }

  @Override
  public boolean isSkyInverted() {
    SkyPortalEvent skyPortal = MonolithicSkyPortalEvent.activeOrNull();
    return skyPortal != null && skyPortal.isInverted();
  }
}
