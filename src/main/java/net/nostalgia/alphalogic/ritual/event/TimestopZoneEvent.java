package net.nostalgia.alphalogic.ritual.event;

public interface TimestopZoneEvent extends RitualEvent {
  int radiusChunks();

  long snapGameTime();

  long snapClockTicks();

  float snapRain();

  float snapThunder();

  @Override
  default RitualEvent.Kind kind() {
    return RitualEvent.Kind.TIMESTOP_ZONE;
  }
}
