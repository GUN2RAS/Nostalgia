package net.nostalgia.client.render;

import net.minecraft.world.entity.Entity;
import net.nostalgia.alphalogic.ritual.TimestopZoneManager;

public class TimestopOutlineHelper {
  public TimestopOutlineHelper() {
  }

  public static boolean shouldGlow(Entity entity) {
    return entity.level() == null ? false : TimestopZoneManager.findZoneContaining(entity.level().dimension(), entity.blockPosition()) != null;
  }
}
