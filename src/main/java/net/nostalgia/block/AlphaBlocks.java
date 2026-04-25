package net.nostalgia.block;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import net.minecraft.world.level.block.grower.TreeGrower;

public class AlphaBlocks {
        public static final java.util.List<Item> TAB_ITEMS = new java.util.ArrayList<>();


        public static final net.minecraft.resources.ResourceKey<CreativeModeTab> ALPHA_TAB_KEY = net.minecraft.resources.ResourceKey
                        .create(net.minecraft.core.registries.Registries.CREATIVE_MODE_TAB,
                                        Identifier.fromNamespaceAndPath("nostalgia", "alpha_blocks"));

        public static final net.minecraft.resources.ResourceKey<Block> ALPHA_STONE_KEY = net.minecraft.resources.ResourceKey
                        .create(net.minecraft.core.registries.Registries.BLOCK,
                                        Identifier.fromNamespaceAndPath("nostalgia", "alpha_stone"));
        public static final Block ALPHA_STONE = registerBlock("alpha_stone",
                        new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).setId(ALPHA_STONE_KEY)
                                        .sound(net.nostalgia.sound.AlphaSounds.ALPHA_STONE_SOUND)),
                        ALPHA_STONE_KEY);

        public static final net.minecraft.resources.ResourceKey<Block> ALPHA_COBBLESTONE_KEY = net.minecraft.resources.ResourceKey
                        .create(net.minecraft.core.registries.Registries.BLOCK,
                                        Identifier.fromNamespaceAndPath("nostalgia", "alpha_cobblestone"));
        public static final Block ALPHA_COBBLESTONE = registerBlock("alpha_cobblestone",
                        new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLESTONE)
                                        .setId(ALPHA_COBBLESTONE_KEY)
                                        .sound(net.nostalgia.sound.AlphaSounds.ALPHA_STONE_SOUND)),
                        ALPHA_COBBLESTONE_KEY);

        public static final net.minecraft.resources.ResourceKey<Block> ALPHA_MOSSY_COBBLESTONE_KEY = net.minecraft.resources.ResourceKey
                        .create(net.minecraft.core.registries.Registries.BLOCK,
                                        Identifier.fromNamespaceAndPath("nostalgia", "alpha_mossy_cobblestone"));
        public static final Block ALPHA_MOSSY_COBBLESTONE = registerBlock("alpha_mossy_cobblestone",
                        new Block(
                                        BlockBehaviour.Properties.ofFullCopy(Blocks.MOSSY_COBBLESTONE)
                                                        .setId(ALPHA_MOSSY_COBBLESTONE_KEY)
                                                        .sound(net.nostalgia.sound.AlphaSounds.ALPHA_STONE_SOUND)),
                        ALPHA_MOSSY_COBBLESTONE_KEY);

        public static final net.minecraft.resources.ResourceKey<Block> ALPHA_DIRT_KEY = net.minecraft.resources.ResourceKey
                        .create(net.minecraft.core.registries.Registries.BLOCK,
                                        Identifier.fromNamespaceAndPath("nostalgia", "alpha_dirt"));
        public static final Block ALPHA_DIRT = registerBlock("alpha_dirt",
                        new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT).setId(ALPHA_DIRT_KEY)
                                        .sound(net.nostalgia.sound.AlphaSounds.ALPHA_GRAVEL_SOUND)),
                        ALPHA_DIRT_KEY);

        public static final net.minecraft.resources.ResourceKey<Block> ALPHA_GRASS_BLOCK_KEY = net.minecraft.resources.ResourceKey
                        .create(net.minecraft.core.registries.Registries.BLOCK,
                                        Identifier.fromNamespaceAndPath("nostalgia", "alpha_grass_block"));
        public static final Block ALPHA_GRASS_BLOCK = registerBlock("alpha_grass_block",
                        new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.GRASS_BLOCK)
                                        .setId(ALPHA_GRASS_BLOCK_KEY)
                                        .sound(net.nostalgia.sound.AlphaSounds.ALPHA_GRASS_SOUND)),
                        ALPHA_GRASS_BLOCK_KEY);

        public static final net.minecraft.resources.ResourceKey<Block> ALPHA_GRASS_BLOCK_FLIPPED_KEY = net.minecraft.resources.ResourceKey
                        .create(net.minecraft.core.registries.Registries.BLOCK,
                                        Identifier.fromNamespaceAndPath("nostalgia", "alpha_grass_block_flipped"));
        public static final Block ALPHA_GRASS_BLOCK_FLIPPED = registerBlockNoItem("alpha_grass_block_flipped",
                        new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.GRASS_BLOCK)
                                        .setId(ALPHA_GRASS_BLOCK_FLIPPED_KEY)
                                        .sound(net.nostalgia.sound.AlphaSounds.ALPHA_GRASS_SOUND)),
                        ALPHA_GRASS_BLOCK_FLIPPED_KEY);

        public static final net.minecraft.resources.ResourceKey<Block> ALPHA_SAND_KEY = net.minecraft.resources.ResourceKey
                        .create(net.minecraft.core.registries.Registries.BLOCK,
                                        Identifier.fromNamespaceAndPath("nostalgia", "alpha_sand"));
        public static final Block ALPHA_SAND = registerBlock("alpha_sand",
                        new net.minecraft.world.level.block.ColoredFallingBlock(new net.minecraft.util.ColorRGBA(14406560), BlockBehaviour.Properties.ofFullCopy(Blocks.SAND).setId(ALPHA_SAND_KEY)
                                        .sound(net.nostalgia.sound.AlphaSounds.ALPHA_SAND_SOUND)),
                        ALPHA_SAND_KEY);

        public static final net.minecraft.resources.ResourceKey<Block> ALPHA_GRAVEL_KEY = net.minecraft.resources.ResourceKey
                        .create(net.minecraft.core.registries.Registries.BLOCK,
                                        Identifier.fromNamespaceAndPath("nostalgia", "alpha_gravel"));
        public static final Block ALPHA_GRAVEL = registerBlock("alpha_gravel",
                        new net.minecraft.world.level.block.ColoredFallingBlock(new net.minecraft.util.ColorRGBA(8421504), BlockBehaviour.Properties.ofFullCopy(Blocks.GRAVEL).setId(ALPHA_GRAVEL_KEY)
                                        .sound(net.nostalgia.sound.AlphaSounds.ALPHA_GRAVEL_SOUND)),
                        ALPHA_GRAVEL_KEY);

        public static final net.minecraft.resources.ResourceKey<Block> ALPHA_BEDROCK_KEY = net.minecraft.resources.ResourceKey
                        .create(net.minecraft.core.registries.Registries.BLOCK,
                                        Identifier.fromNamespaceAndPath("nostalgia", "alpha_bedrock"));
        public static final Block ALPHA_BEDROCK = registerBlock("alpha_bedrock",
                        new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.BEDROCK).setId(ALPHA_BEDROCK_KEY)),
                        ALPHA_BEDROCK_KEY);

        public static final net.minecraft.resources.ResourceKey<Block> ALPHA_SPONGE_KEY = net.minecraft.resources.ResourceKey
                        .create(net.minecraft.core.registries.Registries.BLOCK,
                                        Identifier.fromNamespaceAndPath("nostalgia", "alpha_sponge"));
        public static final Block ALPHA_SPONGE = registerBlock("alpha_sponge",
                        new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.SPONGE).setId(ALPHA_SPONGE_KEY)),
                        ALPHA_SPONGE_KEY);

        public static final net.minecraft.resources.ResourceKey<Block> ALPHA_OBSIDIAN_KEY = net.minecraft.resources.ResourceKey
                        .create(net.minecraft.core.registries.Registries.BLOCK,
                                        Identifier.fromNamespaceAndPath("nostalgia", "alpha_obsidian"));
        public static final Block ALPHA_OBSIDIAN = registerBlock("alpha_obsidian",
                        new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OBSIDIAN).setId(ALPHA_OBSIDIAN_KEY)),
                        ALPHA_OBSIDIAN_KEY);

        public static final net.minecraft.resources.ResourceKey<Block> ALPHA_CLAY_KEY = net.minecraft.resources.ResourceKey
                        .create(net.minecraft.core.registries.Registries.BLOCK,
                                        Identifier.fromNamespaceAndPath("nostalgia", "alpha_clay"));
        public static final Block ALPHA_CLAY = registerBlock("alpha_clay",
                        new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.CLAY).setId(ALPHA_CLAY_KEY)),
                        ALPHA_CLAY_KEY);

        public static final net.minecraft.resources.ResourceKey<Block> ALPHA_SNOW_BLOCK_KEY = net.minecraft.resources.ResourceKey
                        .create(net.minecraft.core.registries.Registries.BLOCK,
                                        Identifier.fromNamespaceAndPath("nostalgia", "alpha_snow_block"));
        public static final Block ALPHA_SNOW_BLOCK = registerBlock("alpha_snow_block",
                        new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.SNOW_BLOCK).setId(ALPHA_SNOW_BLOCK_KEY)),
                        ALPHA_SNOW_BLOCK_KEY);

        public static final net.minecraft.resources.ResourceKey<Block> ALPHA_COAL_ORE_KEY = net.minecraft.resources.ResourceKey
                        .create(net.minecraft.core.registries.Registries.BLOCK,
                                        Identifier.fromNamespaceAndPath("nostalgia", "alpha_coal_ore"));
        public static final Block ALPHA_COAL_ORE = registerBlock("alpha_coal_ore",
                        new net.minecraft.world.level.block.DropExperienceBlock(net.minecraft.util.valueproviders.UniformInt.of(0, 2), BlockBehaviour.Properties.ofFullCopy(Blocks.COAL_ORE).setId(ALPHA_COAL_ORE_KEY)),
                        ALPHA_COAL_ORE_KEY);

        public static final net.minecraft.resources.ResourceKey<Block> ALPHA_IRON_ORE_KEY = net.minecraft.resources.ResourceKey
                        .create(net.minecraft.core.registries.Registries.BLOCK,
                                        Identifier.fromNamespaceAndPath("nostalgia", "alpha_iron_ore"));
        public static final Block ALPHA_IRON_ORE = registerBlock("alpha_iron_ore",
                        new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_ORE).setId(ALPHA_IRON_ORE_KEY)),
                        ALPHA_IRON_ORE_KEY);
                        
        public static final net.minecraft.resources.ResourceKey<Block> ALPHA_REDSTONE_ORE_KEY = net.minecraft.resources.ResourceKey
                        .create(net.minecraft.core.registries.Registries.BLOCK, Identifier.fromNamespaceAndPath("nostalgia", "alpha_redstone_ore"));
        public static final Block ALPHA_REDSTONE_ORE = registerBlock("alpha_redstone_ore",
                        new net.minecraft.world.level.block.RedStoneOreBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.REDSTONE_ORE).setId(ALPHA_REDSTONE_ORE_KEY)), ALPHA_REDSTONE_ORE_KEY);
                        

        public static final net.minecraft.resources.ResourceKey<Block> ALPHA_GOLD_ORE_KEY = net.minecraft.resources.ResourceKey
                        .create(net.minecraft.core.registries.Registries.BLOCK,
                                        Identifier.fromNamespaceAndPath("nostalgia", "alpha_gold_ore"));
        public static final Block ALPHA_GOLD_ORE = registerBlock("alpha_gold_ore",
                        new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.GOLD_ORE).setId(ALPHA_GOLD_ORE_KEY)),
                        ALPHA_GOLD_ORE_KEY);

        public static final net.minecraft.resources.ResourceKey<Block> ALPHA_DIAMOND_ORE_KEY = net.minecraft.resources.ResourceKey
                        .create(net.minecraft.core.registries.Registries.BLOCK,
                                        Identifier.fromNamespaceAndPath("nostalgia", "alpha_diamond_ore"));
        public static final Block ALPHA_DIAMOND_ORE = registerBlock("alpha_diamond_ore",
                        new net.minecraft.world.level.block.DropExperienceBlock(net.minecraft.util.valueproviders.UniformInt.of(3, 7), BlockBehaviour.Properties.ofFullCopy(Blocks.DIAMOND_ORE)
                                        .setId(ALPHA_DIAMOND_ORE_KEY)),
                        ALPHA_DIAMOND_ORE_KEY);



        public static final net.minecraft.resources.ResourceKey<Block> ALPHA_IRON_BLOCK_KEY = net.minecraft.resources.ResourceKey
                        .create(net.minecraft.core.registries.Registries.BLOCK,
                                        Identifier.fromNamespaceAndPath("nostalgia", "alpha_iron_block"));
        public static final Block ALPHA_IRON_BLOCK = registerBlock("alpha_iron_block",
                        new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).setId(ALPHA_IRON_BLOCK_KEY)),
                        ALPHA_IRON_BLOCK_KEY);

        public static final net.minecraft.resources.ResourceKey<Block> ALPHA_GOLD_BLOCK_KEY = net.minecraft.resources.ResourceKey
                        .create(net.minecraft.core.registries.Registries.BLOCK,
                                        Identifier.fromNamespaceAndPath("nostalgia", "alpha_gold_block"));
        public static final Block ALPHA_GOLD_BLOCK = registerBlock("alpha_gold_block",
                        new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.GOLD_BLOCK).setId(ALPHA_GOLD_BLOCK_KEY)),
                        ALPHA_GOLD_BLOCK_KEY);

        public static final net.minecraft.resources.ResourceKey<Block> ALPHA_DIAMOND_BLOCK_KEY = net.minecraft.resources.ResourceKey
                        .create(net.minecraft.core.registries.Registries.BLOCK,
                                        Identifier.fromNamespaceAndPath("nostalgia", "alpha_diamond_block"));
        public static final Block ALPHA_DIAMOND_BLOCK = registerBlock("alpha_diamond_block",
                        new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DIAMOND_BLOCK)
                                        .setId(ALPHA_DIAMOND_BLOCK_KEY)),
                        ALPHA_DIAMOND_BLOCK_KEY);

        public static final net.minecraft.resources.ResourceKey<Block> ALPHA_OAK_LOG_KEY = net.minecraft.resources.ResourceKey
                        .create(net.minecraft.core.registries.Registries.BLOCK,
                                        Identifier.fromNamespaceAndPath("nostalgia", "alpha_oak_log"));
        public static final Block ALPHA_OAK_LOG = registerBlock("alpha_oak_log",
                        new RotatedPillarBlock(
                                        BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG).setId(ALPHA_OAK_LOG_KEY)),
                        ALPHA_OAK_LOG_KEY);

        public static final net.minecraft.resources.ResourceKey<Block> ALPHA_OAK_PLANKS_KEY = net.minecraft.resources.ResourceKey
                        .create(net.minecraft.core.registries.Registries.BLOCK,
                                        Identifier.fromNamespaceAndPath("nostalgia", "alpha_oak_planks"));
        public static final Block ALPHA_OAK_PLANKS = registerBlock("alpha_oak_planks",
                        new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).setId(ALPHA_OAK_PLANKS_KEY)
                                        .sound(net.nostalgia.sound.AlphaSounds.ALPHA_WOOD_SOUND)),
                        ALPHA_OAK_PLANKS_KEY);

        public static final net.minecraft.resources.ResourceKey<Block> ALPHA_BOOKSHELF_KEY = net.minecraft.resources.ResourceKey
                        .create(net.minecraft.core.registries.Registries.BLOCK,
                                        Identifier.fromNamespaceAndPath("nostalgia", "alpha_bookshelf"));
        public static final Block ALPHA_BOOKSHELF = registerBlock("alpha_bookshelf",
                        new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.BOOKSHELF).setId(ALPHA_BOOKSHELF_KEY)
                                        .sound(net.nostalgia.sound.AlphaSounds.ALPHA_WOOD_SOUND)),
                        ALPHA_BOOKSHELF_KEY);

        public static final net.minecraft.resources.ResourceKey<Block> ALPHA_BRICKS_KEY = net.minecraft.resources.ResourceKey
                        .create(net.minecraft.core.registries.Registries.BLOCK,
                                        Identifier.fromNamespaceAndPath("nostalgia", "alpha_bricks"));
        public static final Block ALPHA_BRICKS = registerBlock("alpha_bricks",
                        new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.BRICKS).setId(ALPHA_BRICKS_KEY)),
                        ALPHA_BRICKS_KEY);

        public static final net.minecraft.resources.ResourceKey<Block> ALPHA_TNT_KEY = net.minecraft.resources.ResourceKey
                        .create(net.minecraft.core.registries.Registries.BLOCK,
                                        Identifier.fromNamespaceAndPath("nostalgia", "alpha_tnt"));
        public static final Block ALPHA_TNT = registerBlock("alpha_tnt",
                        new net.minecraft.world.level.block.TntBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.TNT).setId(ALPHA_TNT_KEY)),
                        ALPHA_TNT_KEY);



        public static final net.minecraft.resources.ResourceKey<Block> ALPHA_LEAVES_KEY = net.minecraft.resources.ResourceKey
                        .create(net.minecraft.core.registries.Registries.BLOCK,
                                        Identifier.fromNamespaceAndPath("nostalgia", "alpha_leaves"));

        public static class AlphaLeavesBlock extends net.minecraft.world.level.block.LeavesBlock {
                public static final com.mojang.serialization.MapCodec<AlphaLeavesBlock> CODEC = simpleCodec(AlphaLeavesBlock::new);

                public AlphaLeavesBlock(BlockBehaviour.Properties properties) {
                        super(0.00F, properties);
                }

                @Override
                public boolean isRandomlyTicking(net.minecraft.world.level.block.state.BlockState state) {
                        return false;
                }

                @Override
                public void spawnFallingLeavesParticle(net.minecraft.world.level.Level level, net.minecraft.core.BlockPos pos, net.minecraft.util.RandomSource random) {
                }

                @Override
                public com.mojang.serialization.MapCodec<? extends net.minecraft.world.level.block.LeavesBlock> codec() {
                        return CODEC;
                }
        }

        public static final Block ALPHA_LEAVES = registerBlock("alpha_leaves",
                        new AlphaLeavesBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES).noOcclusion()
                                        .setId(ALPHA_LEAVES_KEY)),
                        ALPHA_LEAVES_KEY);

        public static final net.minecraft.resources.ResourceKey<Block> ALPHA_GLASS_KEY = net.minecraft.resources.ResourceKey
                        .create(net.minecraft.core.registries.Registries.BLOCK,
                                        Identifier.fromNamespaceAndPath("nostalgia", "alpha_glass"));
        public static final Block ALPHA_GLASS = registerBlock("alpha_glass",
                        new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).noOcclusion()
                                        .setId(ALPHA_GLASS_KEY)),
                        ALPHA_GLASS_KEY);

        public static final net.minecraft.resources.ResourceKey<Block> ALPHA_ICE_KEY = net.minecraft.resources.ResourceKey
                        .create(net.minecraft.core.registries.Registries.BLOCK,
                                        Identifier.fromNamespaceAndPath("nostalgia", "alpha_ice"));
        public static final Block ALPHA_ICE = registerBlock("alpha_ice",
                        new net.minecraft.world.level.block.IceBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.ICE).noOcclusion().setId(ALPHA_ICE_KEY)),
                        ALPHA_ICE_KEY);

        public static final net.minecraft.resources.ResourceKey<Block> ALPHA_WOOL_KEY = net.minecraft.resources.ResourceKey
                        .create(net.minecraft.core.registries.Registries.BLOCK,
                                        Identifier.fromNamespaceAndPath("nostalgia", "alpha_wool"));
        public static final Block ALPHA_WOOL = registerBlock("alpha_wool",
                        new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_WOOL).setId(ALPHA_WOOL_KEY)),
                        ALPHA_WOOL_KEY);

        public static final net.minecraft.resources.ResourceKey<Block> ALPHA_SAPLING_KEY = net.minecraft.resources.ResourceKey
                        .create(net.minecraft.core.registries.Registries.BLOCK,
                                        Identifier.fromNamespaceAndPath("nostalgia", "alpha_sapling"));
        public static final Block ALPHA_SAPLING = registerBlock("alpha_sapling",
                        new SaplingBlock(TreeGrower.OAK,
                                        BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SAPLING).noOcclusion().noCollision()
                                                        .setId(ALPHA_SAPLING_KEY)) {
                                @Override
                                public net.minecraft.world.phys.shapes.VoxelShape getCollisionShape(net.minecraft.world.level.block.state.BlockState state, net.minecraft.world.level.BlockGetter level, net.minecraft.core.BlockPos pos, net.minecraft.world.phys.shapes.CollisionContext context) {
                                        return net.minecraft.world.phys.shapes.Shapes.empty();
                                }
                        },
                        ALPHA_SAPLING_KEY);

        public static final net.minecraft.resources.ResourceKey<Block> ALPHA_RED_FLOWER_KEY = net.minecraft.resources.ResourceKey
                        .create(net.minecraft.core.registries.Registries.BLOCK,
                                        Identifier.fromNamespaceAndPath("nostalgia", "alpha_red_flower"));
        public static final Block ALPHA_RED_FLOWER = registerBlock("alpha_red_flower",
                        new FlowerBlock(SuspiciousStewEffects.EMPTY,
                                        BlockBehaviour.Properties.ofFullCopy(Blocks.POPPY).noOcclusion().noCollision()
                                                        .setId(ALPHA_RED_FLOWER_KEY)) {
                                @Override
                                public net.minecraft.world.phys.shapes.VoxelShape getCollisionShape(net.minecraft.world.level.block.state.BlockState state, net.minecraft.world.level.BlockGetter level, net.minecraft.core.BlockPos pos, net.minecraft.world.phys.shapes.CollisionContext context) {
                                        return net.minecraft.world.phys.shapes.Shapes.empty();
                                }
                        },
                        ALPHA_RED_FLOWER_KEY);

        public static final net.minecraft.resources.ResourceKey<Block> ALPHA_RED_FLOWER_FLIPPED_KEY = net.minecraft.resources.ResourceKey
                        .create(net.minecraft.core.registries.Registries.BLOCK,
                                        Identifier.fromNamespaceAndPath("nostalgia", "alpha_red_flower_flipped"));
        public static final Block ALPHA_RED_FLOWER_FLIPPED = registerBlockNoItem("alpha_red_flower_flipped",
                        new FlowerBlock(SuspiciousStewEffects.EMPTY,
                                        BlockBehaviour.Properties.ofFullCopy(Blocks.POPPY).noOcclusion().noCollision()
                                                        .setId(ALPHA_RED_FLOWER_FLIPPED_KEY)) {
                                @Override
                                public net.minecraft.world.phys.shapes.VoxelShape getCollisionShape(net.minecraft.world.level.block.state.BlockState state, net.minecraft.world.level.BlockGetter level, net.minecraft.core.BlockPos pos, net.minecraft.world.phys.shapes.CollisionContext context) {
                                        return net.minecraft.world.phys.shapes.Shapes.empty();
                                }
                        },
                        ALPHA_RED_FLOWER_FLIPPED_KEY);

        public static final net.minecraft.resources.ResourceKey<Block> ALPHA_YELLOW_FLOWER_KEY = net.minecraft.resources.ResourceKey
                        .create(net.minecraft.core.registries.Registries.BLOCK,
                                        Identifier.fromNamespaceAndPath("nostalgia", "alpha_yellow_flower"));
        public static final Block ALPHA_YELLOW_FLOWER = registerBlock("alpha_yellow_flower",
                        new FlowerBlock(SuspiciousStewEffects.EMPTY,
                                        BlockBehaviour.Properties.ofFullCopy(Blocks.DANDELION)
                                                        .noOcclusion().noCollision().setId(ALPHA_YELLOW_FLOWER_KEY)) {
                                @Override
                                public net.minecraft.world.phys.shapes.VoxelShape getCollisionShape(net.minecraft.world.level.block.state.BlockState state, net.minecraft.world.level.BlockGetter level, net.minecraft.core.BlockPos pos, net.minecraft.world.phys.shapes.CollisionContext context) {
                                        return net.minecraft.world.phys.shapes.Shapes.empty();
                                }
                        },
                        ALPHA_YELLOW_FLOWER_KEY);

        public static final net.minecraft.resources.ResourceKey<Block> ALPHA_YELLOW_FLOWER_FLIPPED_KEY = net.minecraft.resources.ResourceKey
                        .create(net.minecraft.core.registries.Registries.BLOCK,
                                        Identifier.fromNamespaceAndPath("nostalgia", "alpha_yellow_flower_flipped"));
        public static final Block ALPHA_YELLOW_FLOWER_FLIPPED = registerBlockNoItem("alpha_yellow_flower_flipped",
                        new FlowerBlock(SuspiciousStewEffects.EMPTY,
                                        BlockBehaviour.Properties.ofFullCopy(Blocks.DANDELION).noOcclusion().noCollision()
                                                        .setId(ALPHA_YELLOW_FLOWER_FLIPPED_KEY)) {
                                @Override
                                public net.minecraft.world.phys.shapes.VoxelShape getCollisionShape(net.minecraft.world.level.block.state.BlockState state, net.minecraft.world.level.BlockGetter level, net.minecraft.core.BlockPos pos, net.minecraft.world.phys.shapes.CollisionContext context) {
                                        return net.minecraft.world.phys.shapes.Shapes.empty();
                                }
                        },
                        ALPHA_YELLOW_FLOWER_FLIPPED_KEY);

        public static final net.minecraft.resources.ResourceKey<Block> ALPHA_RED_MUSHROOM_KEY = net.minecraft.resources.ResourceKey
                        .create(net.minecraft.core.registries.Registries.BLOCK,
                                        Identifier.fromNamespaceAndPath("nostalgia", "alpha_red_mushroom"));
        public static final Block ALPHA_RED_MUSHROOM = registerBlock("alpha_red_mushroom",
                        new FlowerBlock(SuspiciousStewEffects.EMPTY,
                                        BlockBehaviour.Properties.ofFullCopy(Blocks.RED_MUSHROOM)
                                                        .noOcclusion().noCollision().setId(ALPHA_RED_MUSHROOM_KEY)) {
                                @Override
                                public net.minecraft.world.phys.shapes.VoxelShape getCollisionShape(net.minecraft.world.level.block.state.BlockState state, net.minecraft.world.level.BlockGetter level, net.minecraft.core.BlockPos pos, net.minecraft.world.phys.shapes.CollisionContext context) {
                                        return net.minecraft.world.phys.shapes.Shapes.empty();
                                }
                        },
                        ALPHA_RED_MUSHROOM_KEY);

        public static final net.minecraft.resources.ResourceKey<Block> ALPHA_BROWN_MUSHROOM_KEY = net.minecraft.resources.ResourceKey
                        .create(net.minecraft.core.registries.Registries.BLOCK,
                                        Identifier.fromNamespaceAndPath("nostalgia", "alpha_brown_mushroom"));
        public static final Block ALPHA_BROWN_MUSHROOM = registerBlock("alpha_brown_mushroom",
                        new FlowerBlock(SuspiciousStewEffects.EMPTY,
                                        BlockBehaviour.Properties.ofFullCopy(Blocks.BROWN_MUSHROOM)
                                                        .noOcclusion().noCollision().setId(ALPHA_BROWN_MUSHROOM_KEY)) {
                                @Override
                                public net.minecraft.world.phys.shapes.VoxelShape getCollisionShape(net.minecraft.world.level.block.state.BlockState state, net.minecraft.world.level.BlockGetter level, net.minecraft.core.BlockPos pos, net.minecraft.world.phys.shapes.CollisionContext context) {
                                        return net.minecraft.world.phys.shapes.Shapes.empty();
                                }
                        },
                        ALPHA_BROWN_MUSHROOM_KEY);

        public static final net.minecraft.resources.ResourceKey<Block> ALPHA_SUGAR_CANE_KEY = net.minecraft.resources.ResourceKey
                        .create(net.minecraft.core.registries.Registries.BLOCK,
                                        Identifier.fromNamespaceAndPath("nostalgia", "alpha_sugar_cane"));
        public static final Block ALPHA_SUGAR_CANE = registerBlock("alpha_sugar_cane",
                        new SugarCaneBlock(
                                        BlockBehaviour.Properties.ofFullCopy(Blocks.SUGAR_CANE).noOcclusion().noCollision()
                                                        .setId(ALPHA_SUGAR_CANE_KEY)) {
                                @Override
                                public net.minecraft.world.phys.shapes.VoxelShape getCollisionShape(net.minecraft.world.level.block.state.BlockState state, net.minecraft.world.level.BlockGetter level, net.minecraft.core.BlockPos pos, net.minecraft.world.phys.shapes.CollisionContext context) {
                                        return net.minecraft.world.phys.shapes.Shapes.empty();
                                }
                        },
                        ALPHA_SUGAR_CANE_KEY);

        public static final net.minecraft.resources.ResourceKey<Block> ALPHA_SUGAR_CANE_FLIPPED_KEY = net.minecraft.resources.ResourceKey
                        .create(net.minecraft.core.registries.Registries.BLOCK,
                                        Identifier.fromNamespaceAndPath("nostalgia", "alpha_sugar_cane_flipped"));
        public static final Block ALPHA_SUGAR_CANE_FLIPPED = registerBlockNoItem("alpha_sugar_cane_flipped",
                        new SugarCaneBlock(
                                        BlockBehaviour.Properties.ofFullCopy(Blocks.SUGAR_CANE).noOcclusion().noCollision()
                                                        .setId(ALPHA_SUGAR_CANE_FLIPPED_KEY)) {
                                @Override
                                public net.minecraft.world.phys.shapes.VoxelShape getCollisionShape(net.minecraft.world.level.block.state.BlockState state, net.minecraft.world.level.BlockGetter level, net.minecraft.core.BlockPos pos, net.minecraft.world.phys.shapes.CollisionContext context) {
                                        return net.minecraft.world.phys.shapes.Shapes.empty();
                                }
                        },
                        ALPHA_SUGAR_CANE_FLIPPED_KEY);

        public static final net.minecraft.resources.ResourceKey<Block> ALPHA_COBWEB_KEY = net.minecraft.resources.ResourceKey
                        .create(net.minecraft.core.registries.Registries.BLOCK,
                                        Identifier.fromNamespaceAndPath("nostalgia", "alpha_cobweb"));
        public static final Block ALPHA_COBWEB = registerBlock("alpha_cobweb",
                        new WebBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COBWEB).noOcclusion().noCollision()
                                        .setId(ALPHA_COBWEB_KEY)) {
                                @Override
                                public net.minecraft.world.phys.shapes.VoxelShape getCollisionShape(net.minecraft.world.level.block.state.BlockState state, net.minecraft.world.level.BlockGetter level, net.minecraft.core.BlockPos pos, net.minecraft.world.phys.shapes.CollisionContext context) {
                                        return net.minecraft.world.phys.shapes.Shapes.empty();
                                }
                        },
                        ALPHA_COBWEB_KEY);



        public static final net.minecraft.resources.ResourceKey<Block> ALPHA_LADDER_KEY = net.minecraft.resources.ResourceKey
                        .create(net.minecraft.core.registries.Registries.BLOCK,
                                        Identifier.fromNamespaceAndPath("nostalgia", "alpha_ladder"));
        public static final Block ALPHA_LADDER = registerBlock("alpha_ladder",
                        new LadderBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.LADDER).noOcclusion().noCollision()
                                        .setId(ALPHA_LADDER_KEY)) {
                                @Override
                                public net.minecraft.world.phys.shapes.VoxelShape getCollisionShape(net.minecraft.world.level.block.state.BlockState state, net.minecraft.world.level.BlockGetter level, net.minecraft.core.BlockPos pos, net.minecraft.world.phys.shapes.CollisionContext context) {
                                        return net.minecraft.world.phys.shapes.Shapes.empty();
                                }
                        },
                        ALPHA_LADDER_KEY);







        public static final net.minecraft.resources.ResourceKey<Block> ALPHA_LEVER_KEY = net.minecraft.resources.ResourceKey
                        .create(net.minecraft.core.registries.Registries.BLOCK,
                                        Identifier.fromNamespaceAndPath("nostalgia", "alpha_lever"));
        public static final Block ALPHA_LEVER = registerBlock("alpha_lever",
                        new net.minecraft.world.level.block.LeverBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.LEVER).noOcclusion()
                                        .setId(ALPHA_LEVER_KEY)),
                        ALPHA_LEVER_KEY);

        public static final net.minecraft.resources.ResourceKey<Block> ALPHA_WOODEN_DOOR_KEY = net.minecraft.resources.ResourceKey
                        .create(net.minecraft.core.registries.Registries.BLOCK, Identifier.fromNamespaceAndPath("nostalgia", "alpha_wooden_door"));
        public static final Block ALPHA_WOODEN_DOOR = registerBlockNoItem("alpha_wooden_door",
                        new net.minecraft.world.level.block.DoorBlock(BlockSetType.OAK, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_DOOR).noOcclusion().setId(ALPHA_WOODEN_DOOR_KEY)),
                        ALPHA_WOODEN_DOOR_KEY);

        public static final net.minecraft.resources.ResourceKey<Block> ALPHA_IRON_DOOR_KEY = net.minecraft.resources.ResourceKey
                        .create(net.minecraft.core.registries.Registries.BLOCK, Identifier.fromNamespaceAndPath("nostalgia", "alpha_iron_door"));
        public static final Block ALPHA_IRON_DOOR = registerBlockNoItem("alpha_iron_door",
                        new net.minecraft.world.level.block.DoorBlock(BlockSetType.IRON, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_DOOR).noOcclusion().setId(ALPHA_IRON_DOOR_KEY)),
                        ALPHA_IRON_DOOR_KEY);

        public static final net.minecraft.resources.ResourceKey<Block> ALPHA_CACTUS_KEY = net.minecraft.resources.ResourceKey
                        .create(net.minecraft.core.registries.Registries.BLOCK,
                                        Identifier.fromNamespaceAndPath("nostalgia", "alpha_cactus"));
        public static final Block ALPHA_CACTUS = registerBlock("alpha_cactus",
                        new net.minecraft.world.level.block.CactusBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CACTUS).noOcclusion()
                                        .setId(ALPHA_CACTUS_KEY)) {},
                        ALPHA_CACTUS_KEY);

        public static final net.minecraft.resources.ResourceKey<Block> ALPHA_CACTUS_FLIPPED_KEY = net.minecraft.resources.ResourceKey
                        .create(net.minecraft.core.registries.Registries.BLOCK,
                                        Identifier.fromNamespaceAndPath("nostalgia", "alpha_cactus_flipped"));
        public static final Block ALPHA_CACTUS_FLIPPED = registerBlockNoItem("alpha_cactus_flipped",
                        new net.minecraft.world.level.block.CactusBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CACTUS).noOcclusion()
                                        .setId(ALPHA_CACTUS_FLIPPED_KEY)) {},
                        ALPHA_CACTUS_FLIPPED_KEY);

        public static final net.minecraft.resources.ResourceKey<Block> ALPHA_FARMLAND_KEY = net.minecraft.resources.ResourceKey
                        .create(net.minecraft.core.registries.Registries.BLOCK,
                                        Identifier.fromNamespaceAndPath("nostalgia", "alpha_farmland"));
        public static final Block ALPHA_FARMLAND = registerBlock("alpha_farmland",
                        new net.minecraft.world.level.block.FarmlandBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.FARMLAND).setId(ALPHA_FARMLAND_KEY)) {},
                        ALPHA_FARMLAND_KEY);

        public static final net.minecraft.resources.ResourceKey<Block> ALPHA_WHEAT_CROP_KEY = net.minecraft.resources.ResourceKey
                        .create(net.minecraft.core.registries.Registries.BLOCK, Identifier.fromNamespaceAndPath("nostalgia", "alpha_wheat_crop"));
        public static final Block ALPHA_WHEAT_CROP = registerBlockNoItem("alpha_wheat_crop",
                        new net.minecraft.world.level.block.CropBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.WHEAT).setId(ALPHA_WHEAT_CROP_KEY)),
                        ALPHA_WHEAT_CROP_KEY);

        public static final net.minecraft.resources.ResourceKey<Block> ALPHA_CRAFTING_TABLE_KEY = net.minecraft.resources.ResourceKey
                        .create(net.minecraft.core.registries.Registries.BLOCK, Identifier.fromNamespaceAndPath("nostalgia", "alpha_crafting_table"));
        public static final Block ALPHA_CRAFTING_TABLE = registerBlock("alpha_crafting_table",
                        new CraftingTableBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CRAFTING_TABLE).setId(ALPHA_CRAFTING_TABLE_KEY)),
                        ALPHA_CRAFTING_TABLE_KEY);

        public static final net.minecraft.resources.ResourceKey<Block> ALPHA_CHEST_KEY = net.minecraft.resources.ResourceKey
                        .create(net.minecraft.core.registries.Registries.BLOCK, Identifier.fromNamespaceAndPath("nostalgia", "alpha_chest"));
        public static final Block ALPHA_CHEST = registerBlock("alpha_chest",
                        new net.minecraft.world.level.block.ChestBlock(() -> net.minecraft.world.level.block.entity.BlockEntityType.CHEST, net.minecraft.sounds.SoundEvents.EMPTY, net.minecraft.sounds.SoundEvents.EMPTY, BlockBehaviour.Properties.ofFullCopy(Blocks.CHEST).setId(ALPHA_CHEST_KEY)) {
                                @Override
                                public net.minecraft.world.level.block.RenderShape getRenderShape(net.minecraft.world.level.block.state.BlockState state) {
                                        return net.minecraft.world.level.block.RenderShape.MODEL;
                                }
                        },
                        ALPHA_CHEST_KEY);

        public static final net.minecraft.resources.ResourceKey<Block> ALPHA_FURNACE_KEY = net.minecraft.resources.ResourceKey
                        .create(net.minecraft.core.registries.Registries.BLOCK, Identifier.fromNamespaceAndPath("nostalgia", "alpha_furnace"));
        public static final Block ALPHA_FURNACE = registerBlock("alpha_furnace",
                        new net.minecraft.world.level.block.FurnaceBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.FURNACE).setId(ALPHA_FURNACE_KEY)) {},
                        ALPHA_FURNACE_KEY);

        public static final CreativeModeTab ALPHA_TAB = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
                        ALPHA_TAB_KEY,
                        net.minecraft.world.item.CreativeModeTab.builder(net.minecraft.world.item.CreativeModeTab.Row.TOP, 0)
                                        .icon(() -> new net.minecraft.world.item.ItemStack(ALPHA_GRASS_BLOCK))
                                        .title(net.minecraft.network.chat.Component.literal("Alpha Blocks"))
                                        .displayItems((parameters, output) -> {
                                                for (Item item : TAB_ITEMS) {
                                                        output.accept(item);
                                                }
                                        })
                                        .build());

        public static Block registerBlock(String name, Block block, net.minecraft.resources.ResourceKey<Block> key) {
                registerBlockItem(name, block);
                return Registry.register(BuiltInRegistries.BLOCK, key, block);
        }

        public static Block registerBlockNoItem(String name, Block block, net.minecraft.resources.ResourceKey<Block> key) {
                return Registry.register(BuiltInRegistries.BLOCK, key, block);
        }

        private static Item registerBlockItem(String name, Block block) {
                Identifier identifier = Identifier.fromNamespaceAndPath("nostalgia", name);
                net.minecraft.resources.ResourceKey<Item> key = net.minecraft.resources.ResourceKey.create(
                                net.minecraft.core.registries.Registries.ITEM, identifier);
                Item item = Registry.register(BuiltInRegistries.ITEM, key,
                                new BlockItem(block, new Item.Properties().setId(key).useBlockDescriptionPrefix()));
                TAB_ITEMS.add(item);
                return item;
        }

        public static void register() {
        }
}
