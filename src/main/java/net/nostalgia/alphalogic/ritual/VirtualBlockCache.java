package net.nostalgia.alphalogic.ritual;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.nostalgia.alphalogic.ritual.event.RitualEventRegistry;
import net.nostalgia.alphalogic.ritual.event.TransitionEvent;
import net.nostalgia.client.render.cache.DimensionHologramCache;
import net.nostalgia.client.render.cache.DimensionHologramRegistry;

import java.util.Collections;
import java.util.Map;

public class VirtualBlockCache {

    private static DimensionHologramCache active() {
        TransitionEvent t = RitualEventRegistry.activeTransition();
        if (t == null) return null;
        return DimensionHologramRegistry.getByName(t.targetDimensionId());
    }

    public static void put(BlockPos pos, BlockState state) {
        DimensionHologramCache c = active();
        if (c != null) c.setOverride(pos.immutable(), state);
    }

    public static BlockState get(BlockPos pos) {
        DimensionHologramCache c = active();
        return c != null ? c.getOverride(pos) : null;
    }

    public static boolean has(BlockPos pos) {
        DimensionHologramCache c = active();
        return c != null && c.hasOverride(pos);
    }

    public static void clear() {
        DimensionHologramCache c = active();
        if (c != null) c.clearOverrides();
    }

    public static Map<BlockPos, BlockState> getAll() {
        DimensionHologramCache c = active();
        return c != null ? c.getAllOverrides() : Collections.emptyMap();
    }
}
