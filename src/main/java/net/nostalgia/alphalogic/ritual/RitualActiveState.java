package net.nostalgia.alphalogic.ritual;

import net.minecraft.core.BlockPos;

public class RitualActiveState {
    public static boolean isTransitioning = false;
    public static BlockPos ritualCenter = null;
    public static volatile int offsetX = 0;
    public static volatile int yOffset = 0;
    public static volatile int offsetZ = 0;
    public static volatile float currentRadius = 0;
    
    public static final java.util.Set<java.util.UUID> participants = java.util.concurrent.ConcurrentHashMap.newKeySet();

    public static boolean isParticipant(net.minecraft.world.entity.Entity entity) {
        if (!isTransitioning || entity == null) return false;
        return participants.contains(entity.getUUID());
    }
}
