package net.nostalgia.alphalogic.ritual.geometry;

import net.minecraft.core.BlockPos;

public record OffsetGeometry(int dx, int yOffset, int dz) implements TransitionGeometry {
  @Override
  public BlockPos forward(BlockPos p) {
    return new BlockPos(p.getX() + this.dx, p.getY() - this.yOffset, p.getZ() + this.dz);
  }

  @Override
  public BlockPos inverse(BlockPos p) {
    return new BlockPos(p.getX() - this.dx, p.getY() + this.yOffset, p.getZ() - this.dz);
  }

  @Override
  public long forwardPacked(BlockPos p) {
    return BlockPos.asLong(p.getX() + this.dx, p.getY() - this.yOffset, p.getZ() + this.dz);
  }

  @Override
  public long inversePacked(BlockPos p) {
    return BlockPos.asLong(p.getX() - this.dx, p.getY() + this.yOffset, p.getZ() - this.dz);
  }
}
