package net.nostalgia.alphalogic.ritual;

import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import java.util.UUID;

public class SkyPortalEventInstance {


    private final UUID id;
    private final BlockPos center;
    private final int crackPlaneY;
    private final int crackPlaneYTarget;
    private final boolean inverted;
    private final long seed;
    private final String sourceDimension;
    private final String targetDimension;
    private int timerTicks;

    public SkyPortalEventInstance(BlockPos center, int crackPlaneY, int crackPlaneYTarget, boolean inverted, long seed, String sourceDimension, String targetDimension, int durationTicks) {
        this.id = UUID.randomUUID();
        this.center = center;
        this.crackPlaneY = crackPlaneY;
        this.crackPlaneYTarget = crackPlaneYTarget;
        this.inverted = inverted;
        this.seed = seed;
        this.sourceDimension = sourceDimension;
        this.targetDimension = targetDimension;
        this.timerTicks = durationTicks;
    }

    public UUID id() { return id; }
    public BlockPos center() { return center; }
    public int crackPlaneY() { return crackPlaneY; }
    public int crackPlaneYTarget() { return crackPlaneYTarget; }
    public boolean inverted() { return inverted; }
    public long seed() { return seed; }
    public String sourceDimension() { return sourceDimension; }
    public String targetDimension() { return targetDimension; }
    public int timerTicks() { return timerTicks; }
    public boolean isActive() { return timerTicks > 0; }

    public boolean containsOverworldPos(BlockPos pos) {
        return containsOverworldPos(pos, null);
    }

    public boolean containsOverworldPos(BlockPos pos, String currentDim) {
        if (!isActive()) return false;
        boolean isTarget = currentDim != null && currentDim.equals(targetDimension);
        int currentCrackPlaneY = isTarget ? crackPlaneYTarget : crackPlaneY;
        if (inverted) {
            if (pos.getY() <= currentCrackPlaneY) return false;
        } else {
            // Если портал не инвертирован (например, портал на земле), логика высоты может быть другой, но пока ограничим сверху.
            // В рамках мода обычный портал - это Transition, а SkyPortal - это небесный.
        }
        double dx = pos.getX() - center.getX();
        double dz = pos.getZ() - center.getZ();
        double dist = Math.sqrt(dx * dx + dz * dz);
        return dist <= 288.0;
    }
    public void tick() {
        if (timerTicks > 0) {
            timerTicks--;
        }
    }


}
