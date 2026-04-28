package net.nostalgia.client.ritual;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.nostalgia.alphalogic.ritual.event.ClientTransitionView;

import java.util.UUID;

@Environment(EnvType.CLIENT)
public final class MonolithicClientTransitionView implements ClientTransitionView {
    public static final MonolithicClientTransitionView INSTANCE = new MonolithicClientTransitionView();
    private static final UUID FIXED_ID = UUID.nameUUIDFromBytes("nostalgia.monolithic_client_transition".getBytes());

    private MonolithicClientTransitionView() {}

    public static ClientTransitionView activeOrNull() {
        return RitualVisualManager.isTransitioning ? INSTANCE : null;
    }

    @Override
    public UUID id() { return FIXED_ID; }

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
    public int offsetX() { return RitualVisualManager.offsetX; }

    @Override
    public int yOffset() { return RitualVisualManager.yOffset; }

    @Override
    public int offsetZ() { return RitualVisualManager.offsetZ; }

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
    public float alphaRadius() { return RitualVisualManager.getAlphaRadius(); }

    @Override
    public float whiteoutAlpha() { return RitualVisualManager.getWhiteoutAlpha(); }

    @Override
    public float whiteRadius() { return RitualVisualManager.getWhiteRadius(); }

    @Override
    public float fadeProgress() { return RitualVisualManager.getFadeProgress(); }

    @Override
    public float transitionTimeSeconds() { return RitualVisualManager.getTransitionTimeSeconds(); }
}
