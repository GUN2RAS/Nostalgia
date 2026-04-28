package net.nostalgia.alphalogic.ritual.event;

public interface TimestopZoneEvent extends RitualEvent {
    int radiusChunks();
    long snapGameTime();
    long snapClockTicks();
    float snapRain();
    float snapThunder();

    @Override
    default Kind kind() { return Kind.TIMESTOP_ZONE; }
}
