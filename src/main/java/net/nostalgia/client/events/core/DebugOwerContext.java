package net.nostalgia.client.events.core;

import net.minecraft.core.BlockPos;

public class DebugOwerContext implements IHologramContext {

    public static final DebugOwerContext INSTANCE = new DebugOwerContext();

    private boolean active = false;
    private BlockPos center = null;

    private DebugOwerContext() {}

    public void setActive(boolean active, BlockPos center) {
        this.active = active;
        this.center = center;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public boolean contains(int x, int y, int z) {
        if (!active || center == null) return false;

        float currentRadius = 100.0f; // Дебаг радиус

        double dx = x - center.getX();
        double dy = y - center.getY();
        double dz = z - center.getZ();

        long h = (x * 73856093L) ^ (y * 19349663L) ^ (z * 83492791L);
        double noise = ((h & 0xFFFFFF) / (double) 0xFFFFFF) * 2.0 - 1.0;

        double distSq = dx * dx + dy * dy + dz * dz;
        double threshold = currentRadius - (noise * 2.0);

        if (threshold < 0 || distSq > threshold * threshold) {
            return false;
        }

        return true;
    }

    @Override
    public BlockPos getCenter() {
        return center;
    }

    @Override
    public float getRadius() {
        return 100.0f;
    }

    @Override
    public int getOffsetX() {
        return 0;
    }

    @Override
    public int getOffsetY() {
        return 0;
    }

    @Override
    public int getOffsetZ() {
        return 0;
    }

    @Override
    public String getTargetDimension() {
        return "minecraft:overworld";
    }

    @Override
    public boolean isSkyInverted() {
        return true;
    }
}
