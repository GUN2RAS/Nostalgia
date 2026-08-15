package net.nostalgia.alphalogic.ritual.geometry;

import net.minecraft.core.BlockPos;

public record MirrorGeometry(int planeY, int pivotZ) implements TransitionGeometry {
  @Override
  public BlockPos forward(BlockPos p) {
    return new BlockPos(p.getX(), this.planeY - p.getY(), 2 * this.pivotZ - p.getZ());
  }

  @Override
  public BlockPos inverse(BlockPos p) {
    return this.forward(p);
  }

  @Override
  public long forwardPacked(BlockPos p) {
    return BlockPos.asLong(p.getX(), this.planeY - p.getY(), 2 * this.pivotZ - p.getZ());
  }

  @Override
  public long inversePacked(BlockPos p) {
    return this.forwardPacked(p);
  }
}
