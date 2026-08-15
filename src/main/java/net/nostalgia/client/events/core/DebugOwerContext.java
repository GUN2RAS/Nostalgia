package net.nostalgia.client.events.core;

import net.minecraft.core.BlockPos;

public class DebugOwerContext implements IHologramContext {
  public static final DebugOwerContext INSTANCE = new DebugOwerContext();
  private boolean active = false;
  private BlockPos center = null;

  private DebugOwerContext() {
  }

  public void setActive(boolean active, BlockPos center) {
    this.active = active;
    this.center = center;
  }

  @Override
  public boolean isActive() {
    return this.active;
  }

  @Override
  public boolean contains(int x, int y, int z) {
    if (this.active && this.center != null) {
      float currentRadius = 100.0F;
      double dx = x - this.center.getX();
      double dy = y - this.center.getY();
      double dz = z - this.center.getZ();
      long h = x * 73856093L ^ y * 19349663L ^ z * 83492791L;
      double noise = (h & 16777215L) / 1.6777215E7 * 2.0 - 1.0;
      double distSq = dx * dx + dy * dy + dz * dz;
      double threshold = currentRadius - noise * 2.0;
      return !(threshold < 0.0) && !(distSq > threshold * threshold);
    } else {
      return false;
    }
  }

  @Override
  public BlockPos getCenter() {
    return this.center;
  }

  @Override
  public float getRadius() {
    return 100.0F;
  }

  @Override
  public int getOffsetX() {
    return 0;
  }

  @Override
  public int getOffsetY() {
    return 0;
  }

  @Override
  public int getOffsetZ() {
    return 0;
  }

  @Override
  public String getTargetDimension() {
    return "minecraft:overworld";
  }

  @Override
  public boolean isSkyInverted() {
    return true;
  }
}
