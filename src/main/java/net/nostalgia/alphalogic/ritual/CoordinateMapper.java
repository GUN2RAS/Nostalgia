package net.nostalgia.alphalogic.ritual;

import net.minecraft.core.BlockPos;
import net.nostalgia.alphalogic.ritual.geometry.TransitionGeometry;

public final class CoordinateMapper {
  private CoordinateMapper() {
  }

  public static BlockPos forward(TransitionGeometry geo, BlockPos pos) {
    return geo.forward(pos);
  }

  public static BlockPos inverse(TransitionGeometry geo, BlockPos pos) {
    return geo.inverse(pos);
  }

  public static long forwardPacked(TransitionGeometry geo, BlockPos pos) {
    return geo.forwardPacked(pos);
  }

  public static long inversePacked(TransitionGeometry geo, BlockPos pos) {
    return geo.inversePacked(pos);
  }
}
