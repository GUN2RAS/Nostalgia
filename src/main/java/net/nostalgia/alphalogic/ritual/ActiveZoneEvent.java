package net.nostalgia.alphalogic.ritual;

import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.nostalgia.alphalogic.ritual.event.TimestopZoneEvent;

public final class ActiveZoneEvent implements TimestopZoneEvent {
  private final TimestopZoneManager.ActiveZone zone;
  private final UUID id;

  public ActiveZoneEvent(TimestopZoneManager.ActiveZone zone) {
    this.zone = zone;
    String key = zone.beaconPos().asLong() + ":" + zone.dimension().identifier();
    this.id = UUID.nameUUIDFromBytes(key.getBytes());
  }

  @Override
  public UUID id() {
    return this.id;
  }

  @Override
  public BlockPos beaconPos() {
    return this.zone.beaconPos();
  }

  @Override
  public ResourceKey<Level> dimension() {
    return this.zone.dimension();
  }

  @Override
  public int radiusChunks() {
    return this.zone.radiusChunks();
  }

  @Override
  public long snapGameTime() {
    return this.zone.snapGameTime();
  }

  @Override
  public long snapClockTicks() {
    return this.zone.snapClockTicks();
  }

  @Override
  public float snapRain() {
    return this.zone.snapRain();
  }

  @Override
  public float snapThunder() {
    return this.zone.snapThunder();
  }
}
