package net.nostalgia.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TimeMachineBlockEntity extends BlockEntity {

    public TimeMachineBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TIME_MACHINE_BE, pos, state);
    }

}
