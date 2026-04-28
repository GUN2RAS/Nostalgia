package net.nostalgia.alphalogic.ritual;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.nostalgia.alphalogic.ritual.event.TimestopZoneEvent;

import java.util.UUID;

public final class ActiveZoneEvent implements TimestopZoneEvent {
    private final RitualManager.ActiveZone zone;
    private final UUID id;

    public ActiveZoneEvent(RitualManager.ActiveZone zone) {
        this.zone = zone;
        String key = zone.beaconPos().asLong() + ":" + zone.dimension().identifier();
        this.id = UUID.nameUUIDFromBytes(key.getBytes());
    }

    @Override
    public UUID id() { return id; }

    @Override
    public BlockPos beaconPos() { return zone.beaconPos(); }

    @Override
    public ResourceKey<Level> dimension() { return zone.dimension(); }

    @Override
    public int radiusChunks() { return zone.radiusChunks(); }

    @Override
    public long snapGameTime() { return zone.snapGameTime(); }

    @Override
    public long snapClockTicks() { return zone.snapClockTicks(); }

    @Override
    public float snapRain() { return zone.snapRain(); }

    @Override
    public float snapThunder() { return zone.snapThunder(); }
}
