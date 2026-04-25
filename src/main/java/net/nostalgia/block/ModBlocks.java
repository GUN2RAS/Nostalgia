package net.nostalgia.block;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.nostalgia.NostalgiaMod;

public class ModBlocks {

        public static final ResourceKey<Block> RD_STONE_KEY = ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(NostalgiaMod.MOD_ID, "rd_stone"));
        public static final Block RD_STONE = registerBlock("rd_stone", new Block(BlockBehaviour.Properties.of().setId(RD_STONE_KEY).strength(0.0f)), RD_STONE_KEY);

        public static final ResourceKey<Block> RD_GRASS_KEY = ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(NostalgiaMod.MOD_ID, "rd_grass"));
        public static final Block RD_GRASS = registerBlock("rd_grass", new Block(BlockBehaviour.Properties.of().setId(RD_GRASS_KEY).strength(0.0f)), RD_GRASS_KEY);

        public static final ResourceKey<Block> TIME_MACHINE_KEY = ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(NostalgiaMod.MOD_ID, "time_machine"));
        public static final Block TIME_MACHINE = registerBlock("time_machine", new TimeMachineBlock(BlockBehaviour.Properties.of().setId(TIME_MACHINE_KEY).strength(4f).requiresCorrectToolForDrops().noOcclusion()), TIME_MACHINE_KEY);

        public static Block registerBlock(String name, Block block, ResourceKey<Block> key) {
                registerBlockItem(name, block);
                return Registry.register(BuiltInRegistries.BLOCK, key, block);
        }

        private static Item registerBlockItem(String name, Block block) {
                Identifier identifier = Identifier.fromNamespaceAndPath(NostalgiaMod.MOD_ID, name);
                ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, identifier);
                Item item = Registry.register(BuiltInRegistries.ITEM, key,
                                new BlockItem(block, new Item.Properties().setId(key)));
                return item;
        }

        public static void registerModBlocks() {
        }
}
