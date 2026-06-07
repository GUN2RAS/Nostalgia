package net.nostalgia.client.render;

import net.minecraft.core.BlockPos;

public class HologramRenderHelper {
    public static boolean isBlockInverted(BlockPos pos) {
        if (!PortalSkyRenderer.inverted) return false;
        
        if (PortalSkyRenderer.active && PortalSkyRenderer.portalCenter != null) {
            if (pos.distSqr(PortalSkyRenderer.portalCenter) <= 320 * 320 * 2) {
                net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
                boolean isTarget = mc.level != null && mc.level.dimension().identifier().toString().equals(PortalSkyRenderer.originalTargetDimension);
                int currentCrackPlaneY = isTarget ? PortalSkyRenderer.crackPlaneYTarget : PortalSkyRenderer.crackPlaneY;
                if (pos.getY() > currentCrackPlaneY) {
                    return true;
                }
            }
        }
        
        net.nostalgia.alphalogic.ritual.event.ClientEchoRitualView transition = net.nostalgia.client.events.core.ClientRitualEventRegistry.activeTransition();
        if (transition != null && !transition.isBystander()) {
            if (pos.getY() > 150) {
                return true;
            }
        }
        
        return false;
    }
}
