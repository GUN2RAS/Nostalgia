package net.nostalgia.client.ritual;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.nostalgia.alphalogic.ritual.event.ClientTransitionView;
import net.nostalgia.alphalogic.ritual.event.SkyPortalEvent;
import net.nostalgia.client.render.cache.DimensionHologramCache;
import net.nostalgia.client.render.cache.DimensionHologramRegistry;

public class ClientVirtualBlockCache {

    private static DimensionHologramCache active() {
        ClientTransitionView t = ClientRitualEventRegistry.activeTransition();
        if (t != null && t.targetDimension() != null) {
            return DimensionHologramRegistry.getByName(t.targetDimension());
        }
        SkyPortalEvent sp = ClientRitualEventRegistry.activeSkyPortal();
        if (sp != null && sp.targetDimension() != null) {
            return DimensionHologramRegistry.getByName(sp.targetDimension());
        }
        return null;
    }

    public static void put(BlockPos pos, BlockState state) {
        DimensionHologramCache c = active();
        if (c != null) c.setOverride(pos.immutable(), state);
    }

    public static void remove(BlockPos pos) {
        DimensionHologramCache c = active();
        if (c != null) c.removeOverride(pos);
    }

    public static BlockState get(long posAsLong) {
        DimensionHologramCache c = active();
        return c != null ? c.getOverrideRaw(posAsLong) : null;
    }

    public static boolean has(long posAsLong) {
        DimensionHologramCache c = active();
        return c != null && c.hasOverrideRaw(posAsLong);
    }

    public static void syncDeltas(long[] positions, int[] states) {
        DimensionHologramCache c = active();
        if (c == null) return;
        for (int i = 0; i < positions.length; i++) {
            c.setOverrideRaw(positions[i], net.minecraft.world.level.block.Block.stateById(states[i]));
        }
    }

    public static void clear() {
        DimensionHologramCache c = active();
        if (c != null) c.clearOverrides();
    }
}
