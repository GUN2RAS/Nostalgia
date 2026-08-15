package net.nostalgia.client.events.core;

import net.minecraft.core.BlockPos;

public interface IHologramContext {
  boolean isActive();

  boolean contains(int var1, int var2, int var3);

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
    return this.getRadius();
  }
}
