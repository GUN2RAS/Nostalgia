package net.nostalgia.alphalogic.ritual.event;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.UUID;

public interface RitualEvent {
    enum Kind {
        TRANSITION,
        TIMESTOP_ZONE
    }

    UUID id();
    Kind kind();
    BlockPos beaconPos();
    ResourceKey<Level> dimension();
}
