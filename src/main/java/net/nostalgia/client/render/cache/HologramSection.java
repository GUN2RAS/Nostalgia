package net.nostalgia.client.render.cache;

import net.minecraft.world.level.block.state.BlockState;

public class HologramSection {
    public final BlockState[] palette;
    public final byte[] indices;

    public HologramSection(BlockState[] palette, byte[] indices) {
        this.palette = palette;
        this.indices = indices;
    }

    public BlockState getBlockState(int localX, int localY, int localZ) {
        if (palette.length == 0) return net.minecraft.world.level.block.Blocks.AIR.defaultBlockState();
        if (palette.length == 1 || indices == null) return palette[0];
        
        int index = (localY << 8) | (localZ << 4) | localX;
        if (index < 0 || index >= 4096) return net.minecraft.world.level.block.Blocks.AIR.defaultBlockState();
        
        int palIndex = indices[index] & 0xFF;
        if (palIndex >= palette.length) return net.minecraft.world.level.block.Blocks.AIR.defaultBlockState();
        return palette[palIndex];
    }
}
