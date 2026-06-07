package net.nostalgia.alphalogic;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayDeque;
import java.util.Deque;

public class LegacyLightingEngine {

    private static final int MAX_LIGHT = 15;
    private static final int SPREAD_LIMIT = 512;

    private final Level level;
    private final Deque<long[]> updateQueue;
    private boolean dirty;

    public LegacyLightingEngine(Level level) {
        this.level = level;
        this.updateQueue = new ArrayDeque<>();
        this.dirty = false;
    }

    public void scheduleUpdate(BlockPos pos) {
        if (level == null || !level.isLoaded(pos)) {
            return;
        }

        int skyLight = level.getBrightness(LightLayer.SKY, pos);
        int blockLight = level.getBrightness(LightLayer.BLOCK, pos);

        updateQueue.addLast(new long[]{ pos.asLong(), skyLight, blockLight });
        dirty = true;

        if (updateQueue.size() > SPREAD_LIMIT) {
            updateQueue.pollFirst();
        }
    }

    public void processUpdates() {
        if (!dirty || updateQueue.isEmpty()) {
            return;
        }

        int processed = 0;

        while (!updateQueue.isEmpty() && processed < SPREAD_LIMIT) {
            long[] entry = updateQueue.pollFirst();
            BlockPos pos = BlockPos.of(entry[0]);
            int storedSky = (int) entry[1];
            int storedBlock = (int) entry[2];

            int currentSky = level.getBrightness(LightLayer.SKY, pos);
            int currentBlock = level.getBrightness(LightLayer.BLOCK, pos);

            if (currentSky == storedSky && currentBlock == storedBlock) {
                processed++;
                continue;
            }

            propagateToNeighbors(pos, currentSky, currentBlock);
            processed++;
        }

        if (updateQueue.isEmpty()) {
            dirty = false;
        }
    }

    private void propagateToNeighbors(BlockPos center, int skyLevel, int blockLevel) {
        for (Direction dir : Direction.values()) {
            BlockPos neighbor = center.relative(dir);

            if (!level.isLoaded(neighbor)) {
                continue;
            }

            BlockState neighborState = level.getBlockState(neighbor);

            if (neighborState.canOcclude()) {
                continue;
            }

            int neighborSky = level.getBrightness(LightLayer.SKY, neighbor);
            int neighborBlock = level.getBrightness(LightLayer.BLOCK, neighbor);

            int expectedSky = Math.max(0, skyLevel - 1);
            int expectedBlock = Math.max(0, blockLevel - 1);

            if (neighborSky < expectedSky || neighborBlock < expectedBlock) {
                if (updateQueue.size() < SPREAD_LIMIT) {
                    updateQueue.addLast(new long[]{ neighbor.asLong(), neighborSky, neighborBlock });
                }
            }
        }
    }

    public boolean isDirty() {
        return dirty;
    }

    public int getPendingUpdates() {
        return updateQueue.size();
    }
}
