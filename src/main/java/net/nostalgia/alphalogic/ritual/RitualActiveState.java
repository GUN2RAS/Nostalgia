package net.nostalgia.alphalogic.ritual;

import net.minecraft.core.BlockPos;

public class RitualActiveState {
    public static boolean isTransitioning = false;
    public static BlockPos ritualCenter = null;
    public static volatile int offsetX = 0;
    public static volatile int yOffset = 0;
    public static volatile int offsetZ = 0;
    public static volatile float currentRadius = 0;
}
