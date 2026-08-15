package net.nostalgia.alphalogic;

import java.util.ArrayDeque;
import java.util.Deque;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;

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
    if (this.level != null && this.level.isLoaded(pos)) {
      int skyLight = this.level.getBrightness(LightLayer.SKY, pos);
      int blockLight = this.level.getBrightness(LightLayer.BLOCK, pos);
      this.updateQueue.addLast(new long[]{pos.asLong(), skyLight, blockLight});
      this.dirty = true;
      if (this.updateQueue.size() > 512) {
        this.updateQueue.pollFirst();
      }
    }
  }

  public void processUpdates() {
    if (this.dirty && !this.updateQueue.isEmpty()) {
      int processed = 0;

      while (!this.updateQueue.isEmpty() && processed < 512) {
        long[] entry = this.updateQueue.pollFirst();
        BlockPos pos = BlockPos.of(entry[0]);
        int storedSky = (int)entry[1];
        int storedBlock = (int)entry[2];
        int currentSky = this.level.getBrightness(LightLayer.SKY, pos);
        int currentBlock = this.level.getBrightness(LightLayer.BLOCK, pos);
        if (currentSky == storedSky && currentBlock == storedBlock) {
          processed++;
        } else {
          this.propagateToNeighbors(pos, currentSky, currentBlock);
          processed++;
        }
      }

      if (this.updateQueue.isEmpty()) {
        this.dirty = false;
      }
    }
  }

  private void propagateToNeighbors(BlockPos center, int skyLevel, int blockLevel) {
    for (Direction dir : Direction.values()) {
      BlockPos neighbor = center.relative(dir);
      if (this.level.isLoaded(neighbor)) {
        BlockState neighborState = this.level.getBlockState(neighbor);
        if (!neighborState.canOcclude()) {
          int neighborSky = this.level.getBrightness(LightLayer.SKY, neighbor);
          int neighborBlock = this.level.getBrightness(LightLayer.BLOCK, neighbor);
          int expectedSky = Math.max(0, skyLevel - 1);
          int expectedBlock = Math.max(0, blockLevel - 1);
          if ((neighborSky < expectedSky || neighborBlock < expectedBlock) && this.updateQueue.size() < 512) {
            this.updateQueue.addLast(new long[]{neighbor.asLong(), neighborSky, neighborBlock});
          }
        }
      }
    }
  }

  public boolean isDirty() {
    return this.dirty;
  }

  public int getPendingUpdates() {
    return this.updateQueue.size();
  }
}
