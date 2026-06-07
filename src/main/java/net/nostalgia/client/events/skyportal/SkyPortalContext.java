package net.nostalgia.client.events.skyportal;

import net.minecraft.core.BlockPos;
import net.nostalgia.alphalogic.ritual.event.SkyPortalEvent;
import net.nostalgia.client.events.core.IHologramContext;
import net.nostalgia.client.events.skyportal.MonolithicSkyPortalEvent;
import net.nostalgia.client.events.echo.RitualVisualManager;

public class SkyPortalContext implements IHologramContext {

    public static final SkyPortalContext INSTANCE = new SkyPortalContext();

    private SkyPortalContext() {}

    @Override
    public boolean isActive() {
        if (net.nostalgia.client.render.PortalSkyRenderer.skyPortalTransitioning) {
            return true;
        }
        SkyPortalEvent skyPortal = MonolithicSkyPortalEvent.activeOrNull();
        return skyPortal != null && skyPortal.islandVisible();
    }

    @Override
    public boolean contains(int x, int y, int z) {
        net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
        boolean isTarget = mc.level != null && mc.level.dimension().identifier().toString().equals(net.nostalgia.client.render.PortalSkyRenderer.originalTargetDimension);
        int currentCrackPlaneY = isTarget ? net.nostalgia.client.render.PortalSkyRenderer.crackPlaneYTarget : net.nostalgia.client.render.PortalSkyRenderer.crackPlaneY;
        if (y <= currentCrackPlaneY) return false;

        SkyPortalEvent skyPortal = MonolithicSkyPortalEvent.activeOrNull();
        if (skyPortal == null || !skyPortal.islandVisible()) return false;

        BlockPos center = skyPortal.center();
        if (center == null) return false;

        double dx = x - center.getX();
        double dz = z - center.getZ();

        long h = (x * 73856093L) ^ (y * 19349663L) ^ (z * 83492791L);
        double noise = ((h & 0xFFFFFF) / (double) 0xFFFFFF) * 2.0 - 1.0;

        double distSq = dx * dx + dz * dz;
        double r = RitualVisualManager.getPortalAlphaRadius();
        double threshold = r - (noise * 2.0);

        if (threshold < 0 || distSq > threshold * threshold) {
            return false;
        }

        return true;
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
        if (skyPortal == null) return null;

        if (net.minecraft.client.Minecraft.getInstance().level != null) {
            String currentDim = net.minecraft.client.Minecraft.getInstance().level.dimension().identifier().toString();
            String tDim = net.nostalgia.alphalogic.ritual.DimensionUtil.normalize(skyPortal.targetDimension());
            String sDim = net.nostalgia.alphalogic.ritual.DimensionUtil.normalize(skyPortal.sourceDimension());

            if (currentDim.equals(tDim)) {
                return sDim;
            } else {
                return tDim;
            }
        } else {
            return net.nostalgia.alphalogic.ritual.DimensionUtil.normalize(skyPortal.targetDimension());
        }
    }

    @Override
    public boolean isSkyInverted() {
        SkyPortalEvent skyPortal = MonolithicSkyPortalEvent.activeOrNull();
        return skyPortal != null && skyPortal.isInverted();
    }
}
