package net.nostalgia.client.render.cache;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class OverworldHologramCache {

    public static void putSection(int chunkX, int sectionY, int chunkZ, HologramSection section) {
        DimensionHologramRegistry.get(Level.OVERWORLD).putSection(chunkX, sectionY, chunkZ, section);
    }

    public static BlockState getBlockState(int x, int y, int z) {
        return DimensionHologramRegistry.get(Level.OVERWORLD).getSectionBlock(x, y, z);
    }

    public static void clear() {
        DimensionHologramRegistry.get(Level.OVERWORLD).clearSections();
    }
}
