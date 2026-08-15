package net.nostalgia.alphalogic.ritual;

import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.nostalgia.alphalogic.ritual.geometry.MirrorGeometry;
import net.nostalgia.alphalogic.ritual.geometry.TransitionGeometry;

public class SkyPortalEventInstance {
  private final UUID id;
  private final BlockPos center;
  private final int crackPlaneY;
  private final int crackPlaneYTarget;
  private final boolean inverted;
  private final long seed;
  private final String sourceDimension;
  private final String targetDimension;
  private int timerTicks;

  public SkyPortalEventInstance(
    BlockPos center, int crackPlaneY, int crackPlaneYTarget, boolean inverted, long seed, String sourceDimension, String targetDimension, int durationTicks
  ) {
    this(UUID.randomUUID(), center, crackPlaneY, crackPlaneYTarget, inverted, seed, sourceDimension, targetDimension, durationTicks);
  }

  public SkyPortalEventInstance(
    UUID id, BlockPos center, int crackPlaneY, int crackPlaneYTarget, boolean inverted, long seed, String sourceDimension, String targetDimension, int durationTicks
  ) {
    this.id = id;
    this.center = center;
    this.crackPlaneY = crackPlaneY;
    this.crackPlaneYTarget = crackPlaneYTarget;
    this.inverted = inverted;
    this.seed = seed;
    this.sourceDimension = sourceDimension;
    this.targetDimension = targetDimension;
    this.timerTicks = durationTicks;
  }

  public UUID id() {
    return this.id;
  }

  public BlockPos center() {
    return this.center;
  }

  public int crackPlaneY() {
    return this.crackPlaneY;
  }

  public int crackPlaneYTarget() {
    return this.crackPlaneYTarget;
  }

  public boolean inverted() {
    return this.inverted;
  }

  public long seed() {
    return this.seed;
  }

  public String sourceDimension() {
    return this.sourceDimension;
  }

  public String targetDimension() {
    return this.targetDimension;
  }

  public int timerTicks() {
    return this.timerTicks;
  }

  public boolean isActive() {
    return this.timerTicks > 0;
  }

  public TransitionGeometry geometry() {
    return new MirrorGeometry(this.crackPlaneY + this.crackPlaneYTarget, this.center.getZ());
  }

  public boolean containsOverworldPos(BlockPos pos) {
    return this.containsOverworldPos(pos, null);
  }

  public boolean containsOverworldPos(BlockPos pos, String currentDim) {
    if (!this.isActive()) {
      return false;
    } else {
      boolean isTarget = currentDim != null && currentDim.equals(this.targetDimension);
      int currentCrackPlaneY = isTarget ? this.crackPlaneYTarget : this.crackPlaneY;
      if (this.inverted && pos.getY() <= currentCrackPlaneY) {
        return false;
      } else {
        double dx = pos.getX() - this.center.getX();
        double dz = pos.getZ() - this.center.getZ();
        double dist = Math.sqrt(dx * dx + dz * dz);
        return dist <= 288.0;
      }
    }
  }

  public void tick() {
    if (this.timerTicks > 0) {
      this.timerTicks--;
    }
  }
}
