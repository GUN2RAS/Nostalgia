package net.nostalgia.alphalogic.ritual.event;

import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public interface RitualEvent {
  UUID id();

  RitualEvent.Kind kind();

  BlockPos beaconPos();

  ResourceKey<Level> dimension();

  public static enum Kind {
    TRANSITION,
    TIMESTOP_ZONE;

    private Kind() {
    }
  }
}
