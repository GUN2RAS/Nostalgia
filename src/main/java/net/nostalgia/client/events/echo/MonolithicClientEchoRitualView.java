package net.nostalgia.client.events.echo;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.nostalgia.alphalogic.ritual.event.ClientEchoRitualView;

import java.util.UUID;

@Environment(EnvType.CLIENT)
public final class MonolithicClientEchoRitualView implements ClientEchoRitualView {
    public static final MonolithicClientEchoRitualView INSTANCE = new MonolithicClientEchoRitualView();
    private static final UUID FIXED_ID = UUID.nameUUIDFromBytes("nostalgia.monolithic_client_transition".getBytes());

    private MonolithicClientEchoRitualView() {}

    public static ClientEchoRitualView activeOrNull() {
        return RitualVisualManager.isTransitioning ? INSTANCE : null;
    }

    @Override
    public UUID id() {
        return RitualVisualManager.myInstanceId != null ? RitualVisualManager.myInstanceId : FIXED_ID;
    }

    @Override
    public boolean isTransitioning() { return RitualVisualManager.isTransitioning; }

    @Override
    public boolean isBystander() { return RitualVisualManager.isBystander; }

    @Override
    public int currentPhase() { return RitualVisualManager.currentPhase; }

    @Override
    public BlockPos ritualCenter() { return RitualVisualManager.ritualCenter; }

    @Override
    public String targetDimension() { return RitualVisualManager.targetDimension; }

    @Override
    public int offsetX() { return net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.offsetX(); }

    @Override
    public int yOffset() { return net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.yOffset(); }

    @Override
    public int offsetZ() { return net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.offsetZ(); }

    @Override
    public boolean isInNewDimension() { return RitualVisualManager.isInNewDimension(); }

    @Override
    public boolean waitingForChunks() { return RitualVisualManager.waitingForChunks; }

    @Override
    public int lastReportedSurfaceY() { return RitualVisualManager.lastReportedSurfaceY; }

    @Override
    public long visualTime() { return RitualVisualManager.getVisualTime(); }

    @Override
    public long phase2StartTime() { return RitualVisualManager.phase2StartTime; }

    @Override
    public long phase3StartTime() { return RitualVisualManager.phase3StartTime; }

    @Override
    public long transitionStartTime() { return RitualVisualManager.transitionStartTime; }

    @Override
    public float alphaRadius() { return RitualVisualManager.getTransitionAlphaRadius(); }

    @Override
    public float whiteoutAlpha() { return RitualVisualManager.getWhiteoutAlpha(); }

    @Override
    public float whiteRadius() { return RitualVisualManager.getWhiteRadius(); }

    @Override
    public float fadeProgress() { return RitualVisualManager.getFadeProgress(); }

    @Override
    public float transitionTimeSeconds() { return RitualVisualManager.getTransitionTimeSeconds(); }
}
