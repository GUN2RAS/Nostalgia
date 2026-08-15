package net.nostalgia.alphalogic.ritual.event;

import java.util.UUID;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;

@Environment(EnvType.CLIENT)
public interface SkyPortalEvent {
  UUID id();

  BlockPos center();

  String targetDimension();

  String sourceDimension();

  long seed();

  float time();

  boolean isAnimatingOut();

  boolean isInverted();

  boolean islandVisible();
}
