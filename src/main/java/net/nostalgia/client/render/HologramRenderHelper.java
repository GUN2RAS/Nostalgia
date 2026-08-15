package net.nostalgia.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.nostalgia.alphalogic.ritual.event.ClientEchoRitualView;
import net.nostalgia.client.events.core.ClientRitualEventRegistry;

public class HologramRenderHelper {
  public HologramRenderHelper() {
  }

  public static boolean isBlockInverted(BlockPos pos) {
    if (!PortalSkyRenderer.inverted) {
      return false;
    } else {
      if (PortalSkyRenderer.active && PortalSkyRenderer.portalCenter != null && pos.distSqr(PortalSkyRenderer.portalCenter) <= 204800.0) {
        Minecraft mc = Minecraft.getInstance();
        boolean isTarget = mc.level != null && mc.level.dimension().identifier().toString().equals(PortalSkyRenderer.originalTargetDimension);
        int currentCrackPlaneY = isTarget ? PortalSkyRenderer.crackPlaneYTarget : PortalSkyRenderer.crackPlaneY;
        if (pos.getY() > currentCrackPlaneY) {
          return true;
        }
      }

      ClientEchoRitualView transition = ClientRitualEventRegistry.activeTransition();
      return transition != null && !transition.isBystander() && pos.getY() > 150;
    }
  }
}
