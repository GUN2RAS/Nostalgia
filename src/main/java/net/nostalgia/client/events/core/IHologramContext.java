package net.nostalgia.client.events.core;

import net.minecraft.core.BlockPos;

public interface IHologramContext {

    boolean isActive();

    boolean contains(int x, int y, int z);

    BlockPos getCenter();

    float getRadius();

    int getOffsetX();

    int getOffsetY();

    int getOffsetZ();

    String getTargetDimension();

    boolean isSkyInverted();

    default boolean isTerrainActive() {
        return true;
    }

    default float getCollisionRadius() {
        return getRadius();
    }
}

