package net.nostalgia.alphalogic.ritual.event;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;

import java.util.UUID;

@Environment(EnvType.CLIENT)
public interface ClientTransitionView {
    UUID id();
    boolean isTransitioning();
    boolean isBystander();
    int currentPhase();
    BlockPos ritualCenter();
    String targetDimension();
    int offsetX();
    int yOffset();
    int offsetZ();
    boolean isInNewDimension();
    boolean waitingForChunks();
    int lastReportedSurfaceY();

    long visualTime();
    long phase2StartTime();
    long phase3StartTime();
    long transitionStartTime();

    float alphaRadius();
    float whiteoutAlpha();
    float whiteRadius();
    float fadeProgress();
    float transitionTimeSeconds();
}
