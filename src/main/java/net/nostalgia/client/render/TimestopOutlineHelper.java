package net.nostalgia.client.render;

import net.minecraft.world.entity.Entity;
import net.nostalgia.alphalogic.ritual.RitualManager;

public class TimestopOutlineHelper {
    public static boolean shouldGlow(Entity entity) {
        if (entity.level() == null) return false;
        return RitualManager.findZoneContaining(entity.level().dimension(), entity.blockPosition()) != null;
    }
}
