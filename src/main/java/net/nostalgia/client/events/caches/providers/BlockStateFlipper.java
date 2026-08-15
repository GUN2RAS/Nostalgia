package net.nostalgia.client.events.caches.providers;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.*;

public final class BlockStateFlipper {

    private BlockStateFlipper() {}

    public static BlockState flipVertical(BlockState state) {
        if (state == null || state.isAir()) return state;

        for (net.minecraft.world.level.block.state.properties.Property<?> prop : state.getProperties()) {
            state = flipProperty(state, prop);
        }

        return state;
    }

    @SuppressWarnings("unchecked")
    private static BlockState flipProperty(BlockState state, Property<?> prop) {
        String name = prop.getName();

        if (prop == BlockStateProperties.FACING || prop == BlockStateProperties.FACING_HOPPER) {
            Direction dir = state.getValue((EnumProperty<Direction>) prop);
            if (dir == Direction.UP && ((EnumProperty<Direction>) prop).getPossibleValues().contains(Direction.DOWN)) {
                return state.setValue((EnumProperty<Direction>) prop, Direction.DOWN);
            } else if (dir == Direction.DOWN && ((EnumProperty<Direction>) prop).getPossibleValues().contains(Direction.UP)) {
                return state.setValue((EnumProperty<Direction>) prop, Direction.UP);
            }
        }

        if (prop == BlockStateProperties.HALF) {
            Half half = state.getValue(BlockStateProperties.HALF);
            return state.setValue(BlockStateProperties.HALF, half == Half.TOP ? Half.BOTTOM : Half.TOP);
        }

        if (prop == BlockStateProperties.DOUBLE_BLOCK_HALF) {
            DoubleBlockHalf half = state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF);
            return state.setValue(BlockStateProperties.DOUBLE_BLOCK_HALF,
                    half == DoubleBlockHalf.UPPER ? DoubleBlockHalf.LOWER : DoubleBlockHalf.UPPER);
        }

        if (prop == BlockStateProperties.ATTACH_FACE) {
            AttachFace face = state.getValue(BlockStateProperties.ATTACH_FACE);
            if (face == AttachFace.FLOOR) return state.setValue(BlockStateProperties.ATTACH_FACE, AttachFace.CEILING);
            if (face == AttachFace.CEILING) return state.setValue(BlockStateProperties.ATTACH_FACE, AttachFace.FLOOR);
        }

        if (name.equals("up") && prop instanceof BooleanProperty && state.getProperties().contains(BlockStateProperties.DOWN)) {
            boolean up = state.getValue(BlockStateProperties.UP);
            boolean down = state.getValue(BlockStateProperties.DOWN);
            state = state.setValue(BlockStateProperties.UP, down);
            state = state.setValue(BlockStateProperties.DOWN, up);
            return state;
        }

        if (name.equals("down") && prop instanceof BooleanProperty && state.getProperties().contains(BlockStateProperties.UP)) {
            return state;
        }

        return state;
    }
}
