package net.nostalgia.alphalogic.ritual.event;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;

import java.util.UUID;

@Environment(EnvType.CLIENT)
public interface SkyPortalEvent {
    UUID id();
    BlockPos center();
    String targetDimension();
    long seed();
    float time();
    boolean isAnimatingOut();
    boolean isInverted();
    boolean islandVisible();
}
