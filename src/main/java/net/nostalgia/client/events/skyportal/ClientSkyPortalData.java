package net.nostalgia.client.events.skyportal;

import java.util.UUID;
import net.minecraft.core.BlockPos;

public record ClientSkyPortalData(
  UUID id,
  BlockPos center,
  int crackPlaneY,
  int crackPlaneYTarget,
  boolean inverted,
  long seed,
  String sourceDimension,
  String targetDimension
) {}
