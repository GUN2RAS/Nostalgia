package net.nostalgia.alphalogic.ritual;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

public record FreezeRegion(ResourceKey<Level> dimension, BlockPos beaconPos, int chunkRadius) {
  public boolean containsChunk(ResourceKey<Level> dim, long chunkKey) {
    if (!this.dimension.equals(dim)) {
      return false;
    } else {
      int cx = ChunkPos.getX(chunkKey);
      int cz = ChunkPos.getZ(chunkKey);
      int bx = this.beaconPos.getX() >> 4;
      int bz = this.beaconPos.getZ() >> 4;
      return Math.max(Math.abs(cx - bx), Math.abs(cz - bz)) <= this.chunkRadius;
    }
  }

  public boolean containsChunk(ResourceKey<Level> dim, ChunkPos chunkPos) {
    if (!this.dimension.equals(dim)) {
      return false;
    } else {
      int bx = this.beaconPos.getX() >> 4;
      int bz = this.beaconPos.getZ() >> 4;
      return Math.max(Math.abs(chunkPos.x() - bx), Math.abs(chunkPos.z() - bz)) <= this.chunkRadius;
    }
  }

  public boolean containsBlock(ResourceKey<Level> dim, BlockPos pos) {
    if (!this.dimension.equals(dim)) {
      return false;
    } else {
      int bx = this.beaconPos.getX() >> 4;
      int bz = this.beaconPos.getZ() >> 4;
      int cx = pos.getX() >> 4;
      int cz = pos.getZ() >> 4;
      return Math.max(Math.abs(cx - bx), Math.abs(cz - bz)) <= this.chunkRadius;
    }
  }
}
