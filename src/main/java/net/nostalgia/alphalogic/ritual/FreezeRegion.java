package net.nostalgia.alphalogic.ritual;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

public record FreezeRegion(ResourceKey<Level> dimension, BlockPos beaconPos, int chunkRadius) {

    public boolean containsChunk(ResourceKey<Level> dim, long chunkKey) {
        if (!dimension.equals(dim)) return false;
        int cx = ChunkPos.getX(chunkKey);
        int cz = ChunkPos.getZ(chunkKey);
        int bx = beaconPos.getX() >> 4;
        int bz = beaconPos.getZ() >> 4;
        return Math.max(Math.abs(cx - bx), Math.abs(cz - bz)) <= chunkRadius;
    }

    public boolean containsChunk(ResourceKey<Level> dim, ChunkPos chunkPos) {
        if (!dimension.equals(dim)) return false;
        int bx = beaconPos.getX() >> 4;
        int bz = beaconPos.getZ() >> 4;
        return Math.max(Math.abs(chunkPos.x() - bx), Math.abs(chunkPos.z() - bz)) <= chunkRadius;
    }

    public boolean containsBlock(ResourceKey<Level> dim, BlockPos pos) {
        if (!dimension.equals(dim)) return false;
        int bx = beaconPos.getX() >> 4;
        int bz = beaconPos.getZ() >> 4;
        int cx = pos.getX() >> 4;
        int cz = pos.getZ() >> 4;
        return Math.max(Math.abs(cx - bx), Math.abs(cz - bz)) <= chunkRadius;
    }
}
