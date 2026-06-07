package net.nostalgia.client.render;

import net.minecraft.world.entity.Entity;
import net.nostalgia.alphalogic.ritual.EchoRitualManager;

public class TimestopOutlineHelper {
    public static boolean shouldGlow(Entity entity) {
        if (entity.level() == null) return false;
        return net.nostalgia.alphalogic.ritual.TimestopZoneManager.findZoneContaining(entity.level().dimension(), entity.blockPosition()) != null;
    }
}
