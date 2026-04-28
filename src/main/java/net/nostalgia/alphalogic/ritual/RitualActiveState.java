package net.nostalgia.alphalogic.ritual;

import net.minecraft.core.BlockPos;

public class RitualActiveState {
    public static boolean isTransitioning = false;
    public static BlockPos ritualCenter = null;
    public static volatile float currentRadius = 0;
}
