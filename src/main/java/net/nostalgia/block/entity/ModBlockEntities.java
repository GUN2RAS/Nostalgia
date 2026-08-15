package net.nostalgia.block.entity;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.nostalgia.block.ModBlocks;

public class ModBlockEntities {
  public static BlockEntityType<TimeMachineBlockEntity> TIME_MACHINE_BE;

  public ModBlockEntities() {
  }

  public static void registerBlockEntities() {
    TIME_MACHINE_BE = (BlockEntityType<TimeMachineBlockEntity>)Registry.register(
      BuiltInRegistries.BLOCK_ENTITY_TYPE,
      Identifier.fromNamespaceAndPath("nostalgia", "time_machine_be"),
      FabricBlockEntityTypeBuilder.create(TimeMachineBlockEntity::new, new Block[]{ModBlocks.TIME_MACHINE}).build()
    );
  }
}
