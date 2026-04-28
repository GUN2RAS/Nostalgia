package net.nostalgia.alphalogic.ritual;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.nostalgia.alphalogic.ritual.event.RitualEventRegistry;
import net.nostalgia.alphalogic.ritual.event.TransitionEvent;
import net.sha.api.HologramBounds;
import net.sha.api.HologramProvider;

public class NostalgiaServerCollisionBypassProvider implements HologramProvider {
    public static final NostalgiaServerCollisionBypassProvider INSTANCE = new NostalgiaServerCollisionBypassProvider();

    private static final int BOUNDS_RADIUS = 300;

    private static boolean isServerThread() {
        return "Server thread".equals(Thread.currentThread().getName());
    }

    @Override
    public boolean isActive() {
        if (!isServerThread()) return false;
        TransitionEvent t = RitualEventRegistry.activeTransition();
        if (t == null) return false;
        if (t.beaconPos() == null) return false;
        return t.phase() >= 3;
    }

    @Override
    public boolean providesCollision() {
        return true;
    }

    @Override
    public HologramBounds getBounds() {
        TransitionEvent t = RitualEventRegistry.activeTransition();
        if (t == null) return null;
        BlockPos center = t.beaconPos();
        if (center == null) return null;
        int minX = center.getX() - BOUNDS_RADIUS;
        int maxX = center.getX() + BOUNDS_RADIUS;
        int minY = Math.max(-64, center.getY() - BOUNDS_RADIUS);
        int maxY = Math.min(320, center.getY() + BOUNDS_RADIUS);
        int minZ = center.getZ() - BOUNDS_RADIUS;
        int maxZ = center.getZ() + BOUNDS_RADIUS;
        return new HologramBounds(minX, minY, minZ, maxX, maxY, maxZ);
    }

    @Override
    public BlockState getSpoofedBlock(int worldX, int y, int worldZ) {
        if (!isServerThread()) return null;
        return Blocks.AIR.defaultBlockState();
    }
}
