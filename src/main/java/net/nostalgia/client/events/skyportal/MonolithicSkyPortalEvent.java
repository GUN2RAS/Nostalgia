package net.nostalgia.client.events.skyportal;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.nostalgia.alphalogic.ritual.event.SkyPortalEvent;
import net.nostalgia.client.render.PortalSkyRenderer;

import java.util.UUID;

@Environment(EnvType.CLIENT)
public final class MonolithicSkyPortalEvent implements SkyPortalEvent {
    public static final MonolithicSkyPortalEvent INSTANCE = new MonolithicSkyPortalEvent();
    private static final UUID FIXED_ID = UUID.nameUUIDFromBytes("nostalgia.monolithic_sky_portal".getBytes());

    private MonolithicSkyPortalEvent() {}

    public static SkyPortalEvent activeOrNull() {
        return PortalSkyRenderer.active ? INSTANCE : null;
    }

    @Override
    public UUID id() { return FIXED_ID; }

    @Override
    public BlockPos center() { return PortalSkyRenderer.portalCenter; }

    @Override
    public String targetDimension() { return PortalSkyRenderer.targetDimension; }

    @Override
    public String sourceDimension() { return PortalSkyRenderer.sourceDimension; }

    @Override
    public long seed() { return net.nostalgia.alphalogic.bridge.AlphaEngineManager.getWorldSeed(); }

    @Override
    public float time() { return PortalSkyRenderer.portalTime; }

    @Override
    public boolean isAnimatingOut() { return PortalSkyRenderer.isAnimatingOut; }

    @Override
    public boolean isInverted() { return PortalSkyRenderer.inverted; }

    @Override
    public boolean islandVisible() { return PortalSkyRenderer.islandVisible; }
}
