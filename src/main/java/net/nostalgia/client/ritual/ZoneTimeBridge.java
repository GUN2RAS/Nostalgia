package net.nostalgia.client.ritual;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ZoneTimeBridge {
    public static volatile long lastRealClockTicks = 0L;
    public static volatile boolean hasClockReal = false;
}
