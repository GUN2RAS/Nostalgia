package net.nostalgia.alphalogic.ritual.geometry;

import net.minecraft.core.BlockPos;

public interface TransitionGeometry {
  BlockPos forward(BlockPos var1);

  BlockPos inverse(BlockPos var1);

  long forwardPacked(BlockPos var1);

  long inversePacked(BlockPos var1);
}
