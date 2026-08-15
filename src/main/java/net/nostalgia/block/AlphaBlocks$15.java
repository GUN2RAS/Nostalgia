package net.nostalgia.block;

import java.util.function.Supplier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

class AlphaBlocks$15 extends ChestBlock {
  AlphaBlocks$15(Supplier arg0, SoundEvent arg1, SoundEvent arg2, Properties arg3) {
    super(arg0, arg1, arg2, arg3);
  }

  public RenderShape getRenderShape(BlockState state) {
    return RenderShape.MODEL;
  }
}
