package net.nostalgia.block;

import com.mojang.serialization.MapCodec;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.ColorRGBA;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.item.CreativeModeTab.Row;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CactusBlock;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.ColoredFallingBlock;
import net.minecraft.world.level.block.CraftingTableBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.FarmlandBlock;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraft.world.level.block.IceBlock;
import net.minecraft.world.level.block.JukeboxBlock;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.RedStoneOreBlock;
import net.minecraft.world.level.block.RedstoneTorchBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SugarCaneBlock;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.RedstoneWallTorchBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.WebBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.nostalgia.sound.AlphaSounds;

public class AlphaBlocks {
  public static final List<Item> TAB_ITEMS = new ArrayList<>();
  public static final ResourceKey<CreativeModeTab> ALPHA_TAB_KEY = ResourceKey.create(
    Registries.CREATIVE_MODE_TAB, Identifier.fromNamespaceAndPath("nostalgia", "alpha_blocks")
  );
  public static final ResourceKey<Block> ALPHA_STONE_KEY = ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("nostalgia", "alpha_stone"));
  public static final Block ALPHA_STONE = registerBlock(
    "alpha_stone", new Block(Properties.ofFullCopy(Blocks.STONE).setId(ALPHA_STONE_KEY).sound(AlphaSounds.ALPHA_STONE_SOUND)), ALPHA_STONE_KEY
  );
  public static final ResourceKey<Block> ALPHA_COBBLESTONE_KEY = ResourceKey.create(
    Registries.BLOCK, Identifier.fromNamespaceAndPath("nostalgia", "alpha_cobblestone")
  );
  public static final Block ALPHA_COBBLESTONE = registerBlock(
    "alpha_cobblestone",
    new Block(Properties.ofFullCopy(Blocks.COBBLESTONE).setId(ALPHA_COBBLESTONE_KEY).sound(AlphaSounds.ALPHA_STONE_SOUND)),
    ALPHA_COBBLESTONE_KEY
  );
  public static final ResourceKey<Block> ALPHA_MOSSY_COBBLESTONE_KEY = ResourceKey.create(
    Registries.BLOCK, Identifier.fromNamespaceAndPath("nostalgia", "alpha_mossy_cobblestone")
  );
  public static final Block ALPHA_MOSSY_COBBLESTONE = registerBlock(
    "alpha_mossy_cobblestone",
    new Block(Properties.ofFullCopy(Blocks.MOSSY_COBBLESTONE).setId(ALPHA_MOSSY_COBBLESTONE_KEY).sound(AlphaSounds.ALPHA_STONE_SOUND)),
    ALPHA_MOSSY_COBBLESTONE_KEY
  );
  public static final ResourceKey<Block> ALPHA_DIRT_KEY = ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("nostalgia", "alpha_dirt"));
  public static final Block ALPHA_DIRT = registerBlock(
    "alpha_dirt", new Block(Properties.ofFullCopy(Blocks.DIRT).setId(ALPHA_DIRT_KEY).sound(AlphaSounds.ALPHA_GRAVEL_SOUND)), ALPHA_DIRT_KEY
  );
  public static final ResourceKey<Block> ALPHA_GRASS_BLOCK_KEY = ResourceKey.create(
    Registries.BLOCK, Identifier.fromNamespaceAndPath("nostalgia", "alpha_grass_block")
  );
  public static final Block ALPHA_GRASS_BLOCK = registerBlock(
    "alpha_grass_block",
    new Block(Properties.ofFullCopy(Blocks.GRASS_BLOCK).setId(ALPHA_GRASS_BLOCK_KEY).sound(AlphaSounds.ALPHA_GRASS_SOUND)),
    ALPHA_GRASS_BLOCK_KEY
  );
  public static final ResourceKey<Block> ALPHA_SAND_KEY = ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("nostalgia", "alpha_sand"));
  public static final Block ALPHA_SAND = registerBlock(
    "alpha_sand",
    new ColoredFallingBlock(new ColorRGBA(14406560), Properties.ofFullCopy(Blocks.SAND).setId(ALPHA_SAND_KEY).sound(AlphaSounds.ALPHA_SAND_SOUND)),
    ALPHA_SAND_KEY
  );
  public static final ResourceKey<Block> ALPHA_GRAVEL_KEY = ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("nostalgia", "alpha_gravel"));
  public static final Block ALPHA_GRAVEL = registerBlock(
    "alpha_gravel",
    new ColoredFallingBlock(new ColorRGBA(8421504), Properties.ofFullCopy(Blocks.GRAVEL).setId(ALPHA_GRAVEL_KEY).sound(AlphaSounds.ALPHA_GRAVEL_SOUND)),
    ALPHA_GRAVEL_KEY
  );
  public static final ResourceKey<Block> ALPHA_BEDROCK_KEY = ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("nostalgia", "alpha_bedrock"));
  public static final Block ALPHA_BEDROCK = registerBlock(
    "alpha_bedrock", new Block(Properties.ofFullCopy(Blocks.BEDROCK).setId(ALPHA_BEDROCK_KEY)), ALPHA_BEDROCK_KEY
  );
  public static final ResourceKey<Block> ALPHA_SPONGE_KEY = ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("nostalgia", "alpha_sponge"));
  public static final Block ALPHA_SPONGE = registerBlock(
    "alpha_sponge", new Block(Properties.ofFullCopy(Blocks.SPONGE).setId(ALPHA_SPONGE_KEY)), ALPHA_SPONGE_KEY
  );
  public static final ResourceKey<Block> ALPHA_OBSIDIAN_KEY = ResourceKey.create(
    Registries.BLOCK, Identifier.fromNamespaceAndPath("nostalgia", "alpha_obsidian")
  );
  public static final Block ALPHA_OBSIDIAN = registerBlock(
    "alpha_obsidian", new Block(Properties.ofFullCopy(Blocks.OBSIDIAN).setId(ALPHA_OBSIDIAN_KEY)), ALPHA_OBSIDIAN_KEY
  );
  public static final ResourceKey<Block> ALPHA_CLAY_KEY = ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("nostalgia", "alpha_clay"));
  public static final Block ALPHA_CLAY = registerBlock("alpha_clay", new Block(Properties.ofFullCopy(Blocks.CLAY).setId(ALPHA_CLAY_KEY)), ALPHA_CLAY_KEY);
  public static final ResourceKey<Block> ALPHA_SNOW_BLOCK_KEY = ResourceKey.create(
    Registries.BLOCK, Identifier.fromNamespaceAndPath("nostalgia", "alpha_snow_block")
  );
  public static final Block ALPHA_SNOW_BLOCK = registerBlock(
    "alpha_snow_block", new Block(Properties.ofFullCopy(Blocks.SNOW_BLOCK).setId(ALPHA_SNOW_BLOCK_KEY)), ALPHA_SNOW_BLOCK_KEY
  );
  public static final ResourceKey<Block> ALPHA_COAL_ORE_KEY = ResourceKey.create(
    Registries.BLOCK, Identifier.fromNamespaceAndPath("nostalgia", "alpha_coal_ore")
  );
  public static final Block ALPHA_COAL_ORE = registerBlock(
    "alpha_coal_ore", new DropExperienceBlock(UniformInt.of(0, 2), Properties.ofFullCopy(Blocks.COAL_ORE).setId(ALPHA_COAL_ORE_KEY)), ALPHA_COAL_ORE_KEY
  );
  public static final ResourceKey<Block> ALPHA_IRON_ORE_KEY = ResourceKey.create(
    Registries.BLOCK, Identifier.fromNamespaceAndPath("nostalgia", "alpha_iron_ore")
  );
  public static final Block ALPHA_IRON_ORE = registerBlock(
    "alpha_iron_ore", new Block(Properties.ofFullCopy(Blocks.IRON_ORE).setId(ALPHA_IRON_ORE_KEY)), ALPHA_IRON_ORE_KEY
  );
  public static final ResourceKey<Block> ALPHA_REDSTONE_ORE_KEY = ResourceKey.create(
    Registries.BLOCK, Identifier.fromNamespaceAndPath("nostalgia", "alpha_redstone_ore")
  );
  public static final Block ALPHA_REDSTONE_ORE = registerBlock(
    "alpha_redstone_ore", new RedStoneOreBlock(Properties.ofFullCopy(Blocks.REDSTONE_ORE).setId(ALPHA_REDSTONE_ORE_KEY)), ALPHA_REDSTONE_ORE_KEY
  );
  public static final ResourceKey<Block> ALPHA_GOLD_ORE_KEY = ResourceKey.create(
    Registries.BLOCK, Identifier.fromNamespaceAndPath("nostalgia", "alpha_gold_ore")
  );
  public static final Block ALPHA_GOLD_ORE = registerBlock(
    "alpha_gold_ore", new Block(Properties.ofFullCopy(Blocks.GOLD_ORE).setId(ALPHA_GOLD_ORE_KEY)), ALPHA_GOLD_ORE_KEY
  );
  public static final ResourceKey<Block> ALPHA_DIAMOND_ORE_KEY = ResourceKey.create(
    Registries.BLOCK, Identifier.fromNamespaceAndPath("nostalgia", "alpha_diamond_ore")
  );
  public static final Block ALPHA_DIAMOND_ORE = registerBlock(
    "alpha_diamond_ore",
    new DropExperienceBlock(UniformInt.of(3, 7), Properties.ofFullCopy(Blocks.DIAMOND_ORE).setId(ALPHA_DIAMOND_ORE_KEY)),
    ALPHA_DIAMOND_ORE_KEY
  );
  public static final ResourceKey<Block> ALPHA_IRON_BLOCK_KEY = ResourceKey.create(
    Registries.BLOCK, Identifier.fromNamespaceAndPath("nostalgia", "alpha_iron_block")
  );
  public static final Block ALPHA_IRON_BLOCK = registerBlock(
    "alpha_iron_block", new Block(Properties.ofFullCopy(Blocks.IRON_BLOCK).setId(ALPHA_IRON_BLOCK_KEY)), ALPHA_IRON_BLOCK_KEY
  );
  public static final ResourceKey<Block> ALPHA_GOLD_BLOCK_KEY = ResourceKey.create(
    Registries.BLOCK, Identifier.fromNamespaceAndPath("nostalgia", "alpha_gold_block")
  );
  public static final Block ALPHA_GOLD_BLOCK = registerBlock(
    "alpha_gold_block", new Block(Properties.ofFullCopy(Blocks.GOLD_BLOCK).setId(ALPHA_GOLD_BLOCK_KEY)), ALPHA_GOLD_BLOCK_KEY
  );
  public static final ResourceKey<Block> ALPHA_DIAMOND_BLOCK_KEY = ResourceKey.create(
    Registries.BLOCK, Identifier.fromNamespaceAndPath("nostalgia", "alpha_diamond_block")
  );
  public static final Block ALPHA_DIAMOND_BLOCK = registerBlock(
    "alpha_diamond_block", new Block(Properties.ofFullCopy(Blocks.DIAMOND_BLOCK).setId(ALPHA_DIAMOND_BLOCK_KEY)), ALPHA_DIAMOND_BLOCK_KEY
  );
  public static final ResourceKey<Block> ALPHA_OAK_LOG_KEY = ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("nostalgia", "alpha_oak_log"));
  public static final Block ALPHA_OAK_LOG = registerBlock(
    "alpha_oak_log", new RotatedPillarBlock(Properties.ofFullCopy(Blocks.OAK_LOG).setId(ALPHA_OAK_LOG_KEY)), ALPHA_OAK_LOG_KEY
  );
  public static final ResourceKey<Block> ALPHA_OAK_PLANKS_KEY = ResourceKey.create(
    Registries.BLOCK, Identifier.fromNamespaceAndPath("nostalgia", "alpha_oak_planks")
  );
  public static final Block ALPHA_OAK_PLANKS = registerBlock(
    "alpha_oak_planks",
    new Block(Properties.ofFullCopy(Blocks.OAK_PLANKS).setId(ALPHA_OAK_PLANKS_KEY).sound(AlphaSounds.ALPHA_WOOD_SOUND)),
    ALPHA_OAK_PLANKS_KEY
  );
  public static final ResourceKey<Block> ALPHA_BOOKSHELF_KEY = ResourceKey.create(
    Registries.BLOCK, Identifier.fromNamespaceAndPath("nostalgia", "alpha_bookshelf")
  );
  public static final Block ALPHA_BOOKSHELF = registerBlock(
    "alpha_bookshelf", new Block(Properties.ofFullCopy(Blocks.BOOKSHELF).setId(ALPHA_BOOKSHELF_KEY).sound(AlphaSounds.ALPHA_WOOD_SOUND)), ALPHA_BOOKSHELF_KEY
  );
  public static final ResourceKey<Block> ALPHA_BRICKS_KEY = ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("nostalgia", "alpha_bricks"));
  public static final Block ALPHA_BRICKS = registerBlock(
    "alpha_bricks", new Block(Properties.ofFullCopy(Blocks.BRICKS).setId(ALPHA_BRICKS_KEY)), ALPHA_BRICKS_KEY
  );
  public static final ResourceKey<Block> ALPHA_TNT_KEY = ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("nostalgia", "alpha_tnt"));
  public static final Block ALPHA_TNT = registerBlock("alpha_tnt", new TntBlock(Properties.ofFullCopy(Blocks.TNT).setId(ALPHA_TNT_KEY)), ALPHA_TNT_KEY);
  public static final ResourceKey<Block> ALPHA_LEAVES_KEY = ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("nostalgia", "alpha_leaves"));
  public static final Block ALPHA_LEAVES = registerBlock(
    "alpha_leaves", new AlphaBlocks.AlphaLeavesBlock(Properties.ofFullCopy(Blocks.OAK_LEAVES).noOcclusion().setId(ALPHA_LEAVES_KEY)), ALPHA_LEAVES_KEY
  );
  public static final ResourceKey<Block> ALPHA_GLASS_KEY = ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("nostalgia", "alpha_glass"));
  public static final Block ALPHA_GLASS = registerBlock(
    "alpha_glass", new Block(Properties.ofFullCopy(Blocks.GLASS).noOcclusion().setId(ALPHA_GLASS_KEY)), ALPHA_GLASS_KEY
  );
  public static final ResourceKey<Block> ALPHA_ICE_KEY = ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("nostalgia", "alpha_ice"));
  public static final Block ALPHA_ICE = registerBlock(
    "alpha_ice", new IceBlock(Properties.ofFullCopy(Blocks.ICE).noOcclusion().setId(ALPHA_ICE_KEY)), ALPHA_ICE_KEY
  );
  public static final ResourceKey<Block> ALPHA_WOOL_KEY = ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("nostalgia", "alpha_wool"));
  public static final Block ALPHA_WOOL = registerBlock("alpha_wool", new Block(Properties.ofFullCopy(Blocks.WHITE_WOOL).setId(ALPHA_WOOL_KEY)), ALPHA_WOOL_KEY);
  public static final ResourceKey<Block> ALPHA_SAPLING_KEY = ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("nostalgia", "alpha_sapling"));
  public static final Block ALPHA_SAPLING = registerBlock(
    "alpha_sapling", new SaplingBlock(TreeGrower.OAK, Properties.ofFullCopy(Blocks.OAK_SAPLING).noOcclusion().noCollision().setId(ALPHA_SAPLING_KEY)) {
      public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
      }
    }, ALPHA_SAPLING_KEY
  );
  public static final ResourceKey<Block> ALPHA_RED_FLOWER_KEY = ResourceKey.create(
    Registries.BLOCK, Identifier.fromNamespaceAndPath("nostalgia", "alpha_red_flower")
  );
  public static final Block ALPHA_RED_FLOWER = registerBlock(
    "alpha_red_flower",
    new FlowerBlock(SuspiciousStewEffects.EMPTY, Properties.ofFullCopy(Blocks.POPPY).noOcclusion().noCollision().setId(ALPHA_RED_FLOWER_KEY)) {
      public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
      }
    },
    ALPHA_RED_FLOWER_KEY
  );
  public static final ResourceKey<Block> ALPHA_YELLOW_FLOWER_KEY = ResourceKey.create(
    Registries.BLOCK, Identifier.fromNamespaceAndPath("nostalgia", "alpha_yellow_flower")
  );
  public static final Block ALPHA_YELLOW_FLOWER = registerBlock(
    "alpha_yellow_flower",
    new FlowerBlock(SuspiciousStewEffects.EMPTY, Properties.ofFullCopy(Blocks.DANDELION).noOcclusion().noCollision().setId(ALPHA_YELLOW_FLOWER_KEY)) {
      public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
      }
    },
    ALPHA_YELLOW_FLOWER_KEY
  );
  public static final ResourceKey<Block> ALPHA_RED_MUSHROOM_KEY = ResourceKey.create(
    Registries.BLOCK, Identifier.fromNamespaceAndPath("nostalgia", "alpha_red_mushroom")
  );
  public static final Block ALPHA_RED_MUSHROOM = registerBlock(
    "alpha_red_mushroom",
    new FlowerBlock(SuspiciousStewEffects.EMPTY, Properties.ofFullCopy(Blocks.RED_MUSHROOM).noOcclusion().noCollision().setId(ALPHA_RED_MUSHROOM_KEY)) {
      public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
      }
    },
    ALPHA_RED_MUSHROOM_KEY
  );
  public static final ResourceKey<Block> ALPHA_BROWN_MUSHROOM_KEY = ResourceKey.create(
    Registries.BLOCK, Identifier.fromNamespaceAndPath("nostalgia", "alpha_brown_mushroom")
  );
  public static final Block ALPHA_BROWN_MUSHROOM = registerBlock(
    "alpha_brown_mushroom",
    new FlowerBlock(SuspiciousStewEffects.EMPTY, Properties.ofFullCopy(Blocks.BROWN_MUSHROOM).noOcclusion().noCollision().setId(ALPHA_BROWN_MUSHROOM_KEY)) {
      public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
      }
    },
    ALPHA_BROWN_MUSHROOM_KEY
  );
  public static final ResourceKey<Block> ALPHA_SUGAR_CANE_KEY = ResourceKey.create(
    Registries.BLOCK, Identifier.fromNamespaceAndPath("nostalgia", "alpha_sugar_cane")
  );
  public static final Block ALPHA_SUGAR_CANE = registerBlock(
    "alpha_sugar_cane", new SugarCaneBlock(Properties.ofFullCopy(Blocks.SUGAR_CANE).noOcclusion().noCollision().setId(ALPHA_SUGAR_CANE_KEY)) {
      public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
      }
    }, ALPHA_SUGAR_CANE_KEY
  );
  public static final ResourceKey<Block> ALPHA_COBWEB_KEY = ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("nostalgia", "alpha_cobweb"));
  public static final Block ALPHA_COBWEB = registerBlock(
    "alpha_cobweb", new WebBlock(Properties.ofFullCopy(Blocks.COBWEB).noOcclusion().noCollision().setId(ALPHA_COBWEB_KEY)) {
      public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
      }
    }, ALPHA_COBWEB_KEY
  );
  public static final ResourceKey<Block> ALPHA_LADDER_KEY = ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("nostalgia", "alpha_ladder"));
  public static final Block ALPHA_LADDER = registerBlock(
    "alpha_ladder", new LadderBlock(Properties.ofFullCopy(Blocks.LADDER).noOcclusion().noCollision().setId(ALPHA_LADDER_KEY)) {
      public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
      }
    }, ALPHA_LADDER_KEY
  );
  public static final ResourceKey<Block> ALPHA_LEVER_KEY = ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("nostalgia", "alpha_lever"));
  public static final Block ALPHA_LEVER = registerBlock(
    "alpha_lever", new LeverBlock(Properties.ofFullCopy(Blocks.LEVER).noOcclusion().setId(ALPHA_LEVER_KEY)), ALPHA_LEVER_KEY
  );
  public static final ResourceKey<Block> ALPHA_WOODEN_DOOR_KEY = ResourceKey.create(
    Registries.BLOCK, Identifier.fromNamespaceAndPath("nostalgia", "alpha_wooden_door")
  );
  public static final Block ALPHA_WOODEN_DOOR = registerBlockNoItem(
    "alpha_wooden_door",
    new DoorBlock(BlockSetType.OAK, Properties.ofFullCopy(Blocks.OAK_DOOR).noOcclusion().setId(ALPHA_WOODEN_DOOR_KEY)),
    ALPHA_WOODEN_DOOR_KEY
  );
  public static final ResourceKey<Block> ALPHA_IRON_DOOR_KEY = ResourceKey.create(
    Registries.BLOCK, Identifier.fromNamespaceAndPath("nostalgia", "alpha_iron_door")
  );
  public static final Block ALPHA_IRON_DOOR = registerBlockNoItem(
    "alpha_iron_door", new DoorBlock(BlockSetType.IRON, Properties.ofFullCopy(Blocks.IRON_DOOR).noOcclusion().setId(ALPHA_IRON_DOOR_KEY)), ALPHA_IRON_DOOR_KEY
  );
  public static final ResourceKey<Block> ALPHA_CACTUS_KEY = ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("nostalgia", "alpha_cactus"));
  public static final Block ALPHA_CACTUS = registerBlock(
    "alpha_cactus", new CactusBlock(Properties.ofFullCopy(Blocks.CACTUS).noOcclusion().setId(ALPHA_CACTUS_KEY)) {}, ALPHA_CACTUS_KEY
  );
  public static final ResourceKey<Block> ALPHA_FARMLAND_KEY = ResourceKey.create(
    Registries.BLOCK, Identifier.fromNamespaceAndPath("nostalgia", "alpha_farmland")
  );
  public static final Block ALPHA_FARMLAND = registerBlock(
    "alpha_farmland", new FarmlandBlock(Properties.ofFullCopy(Blocks.FARMLAND).setId(ALPHA_FARMLAND_KEY)) {}, ALPHA_FARMLAND_KEY
  );
  public static final ResourceKey<Block> ALPHA_WHEAT_CROP_KEY = ResourceKey.create(
    Registries.BLOCK, Identifier.fromNamespaceAndPath("nostalgia", "alpha_wheat_crop")
  );
  public static final Block ALPHA_WHEAT_CROP = registerBlockNoItem(
    "alpha_wheat_crop", new CropBlock(Properties.ofFullCopy(Blocks.WHEAT).setId(ALPHA_WHEAT_CROP_KEY)), ALPHA_WHEAT_CROP_KEY
  );
  public static final ResourceKey<Block> ALPHA_CRAFTING_TABLE_KEY = ResourceKey.create(
    Registries.BLOCK, Identifier.fromNamespaceAndPath("nostalgia", "alpha_crafting_table")
  );
  public static final Block ALPHA_CRAFTING_TABLE = registerBlock(
    "alpha_crafting_table", new CraftingTableBlock(Properties.ofFullCopy(Blocks.CRAFTING_TABLE).setId(ALPHA_CRAFTING_TABLE_KEY)), ALPHA_CRAFTING_TABLE_KEY
  );
  public static final ResourceKey<Block> ALPHA_CHEST_KEY = ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("nostalgia", "alpha_chest"));
  public static final Block ALPHA_CHEST = registerBlock(
    "alpha_chest",
    new ChestBlock(() -> BlockEntityType.CHEST, SoundEvents.EMPTY, SoundEvents.EMPTY, Properties.ofFullCopy(Blocks.CHEST).setId(ALPHA_CHEST_KEY)) {
      public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
      }
    },
    ALPHA_CHEST_KEY
  );
  public static final ResourceKey<Block> ALPHA_FURNACE_KEY = ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("nostalgia", "alpha_furnace"));
  public static final Block ALPHA_FURNACE = registerBlock(
    "alpha_furnace", new FurnaceBlock(Properties.ofFullCopy(Blocks.FURNACE).setId(ALPHA_FURNACE_KEY)) {}, ALPHA_FURNACE_KEY
  );
  public static final ResourceKey<Block> ALPHA_JUKEBOX_KEY = ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("nostalgia", "alpha_jukebox"));
  public static final Block ALPHA_JUKEBOX = registerBlock(
    "alpha_jukebox", new JukeboxBlock(Properties.ofFullCopy(Blocks.JUKEBOX).setId(ALPHA_JUKEBOX_KEY)), ALPHA_JUKEBOX_KEY
  );
  public static final ResourceKey<Block> ALPHA_REDSTONE_TORCH_KEY = ResourceKey.create(
    Registries.BLOCK, Identifier.fromNamespaceAndPath("nostalgia", "alpha_redstone_torch")
  );
  public static final Block ALPHA_REDSTONE_TORCH = registerBlockNoItem(
    "alpha_redstone_torch", new RedstoneTorchBlock(Properties.ofFullCopy(Blocks.REDSTONE_TORCH).setId(ALPHA_REDSTONE_TORCH_KEY)), ALPHA_REDSTONE_TORCH_KEY
  );
  public static final ResourceKey<Block> ALPHA_REDSTONE_WALL_TORCH_KEY = ResourceKey.create(
    Registries.BLOCK, Identifier.fromNamespaceAndPath("nostalgia", "alpha_redstone_wall_torch")
  );
  public static final Block ALPHA_REDSTONE_WALL_TORCH = registerBlockNoItem(
    "alpha_redstone_wall_torch", new RedstoneWallTorchBlock(Properties.ofFullCopy(Blocks.REDSTONE_WALL_TORCH).setId(ALPHA_REDSTONE_WALL_TORCH_KEY)), ALPHA_REDSTONE_WALL_TORCH_KEY
  );
  private static final ResourceKey<Item> ALPHA_REDSTONE_TORCH_ITEM_KEY = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("nostalgia", "alpha_redstone_torch"));
  public static final Item ALPHA_REDSTONE_TORCH_ITEM = registerTorchItem(ALPHA_REDSTONE_TORCH, ALPHA_REDSTONE_WALL_TORCH, ALPHA_REDSTONE_TORCH_ITEM_KEY);
  public static final ResourceKey<Block> ALPHA_TORCH_KEY = ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("nostalgia", "alpha_torch"));
  public static final Block ALPHA_TORCH = registerBlockNoItem(
    "alpha_torch", new TorchBlock(ParticleTypes.FLAME, Properties.ofFullCopy(Blocks.TORCH).setId(ALPHA_TORCH_KEY)), ALPHA_TORCH_KEY
  );
  public static final ResourceKey<Block> ALPHA_WALL_TORCH_KEY = ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("nostalgia", "alpha_wall_torch"));
  public static final Block ALPHA_WALL_TORCH = registerBlockNoItem(
    "alpha_wall_torch", new WallTorchBlock(ParticleTypes.FLAME, Properties.ofFullCopy(Blocks.WALL_TORCH).setId(ALPHA_WALL_TORCH_KEY)), ALPHA_WALL_TORCH_KEY
  );
  private static final ResourceKey<Item> ALPHA_TORCH_ITEM_KEY = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("nostalgia", "alpha_torch"));
  public static final Item ALPHA_TORCH_ITEM = registerTorchItem(ALPHA_TORCH, ALPHA_WALL_TORCH, ALPHA_TORCH_ITEM_KEY);
  public static final ResourceKey<Block> ALPHA_SNOW_KEY = ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("nostalgia", "alpha_snow"));
  public static final Block ALPHA_SNOW = registerBlock(
    "alpha_snow", new SnowLayerBlock(Properties.ofFullCopy(Blocks.SNOW).setId(ALPHA_SNOW_KEY)), ALPHA_SNOW_KEY
  );
  public static final CreativeModeTab ALPHA_TAB = (CreativeModeTab)Registry.register(
    BuiltInRegistries.CREATIVE_MODE_TAB,
    ALPHA_TAB_KEY,
    CreativeModeTab.builder(Row.TOP, 0)
      .icon(() -> new ItemStack(ALPHA_GRASS_BLOCK))
      .title(Component.literal("Alpha Blocks"))
      .displayItems((parameters, output) -> {
        for (Item item : TAB_ITEMS) {
          output.accept(item);
        }
      })
      .build()
  );

  public AlphaBlocks() {
  }

  public static Block registerBlock(String name, Block block, ResourceKey<Block> key) {
    registerBlockItem(name, block);
    return (Block)Registry.register(BuiltInRegistries.BLOCK, key, block);
  }

  public static Block registerBlockNoItem(String name, Block block, ResourceKey<Block> key) {
    return (Block)Registry.register(BuiltInRegistries.BLOCK, key, block);
  }

  private static Item registerTorchItem(Block standing, Block wall, ResourceKey<Item> key) {
    Item item = Registry.register(
      BuiltInRegistries.ITEM, key, new StandingAndWallBlockItem(standing, wall, Direction.DOWN, new Item.Properties().setId(key).useBlockDescriptionPrefix())
    );
    TAB_ITEMS.add(item);
    return item;
  }

  private static Item registerBlockItem(String name, Block block) {
    Identifier identifier = Identifier.fromNamespaceAndPath("nostalgia", name);
    ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, identifier);
    Item item = (Item)Registry.register(
      BuiltInRegistries.ITEM, key, new BlockItem(block, new net.minecraft.world.item.Item.Properties().setId(key).useBlockDescriptionPrefix())
    );
    TAB_ITEMS.add(item);
    return item;
  }

  public static void register() {
  }

  public static class AlphaLeavesBlock extends LeavesBlock {
    public static final MapCodec<AlphaBlocks.AlphaLeavesBlock> CODEC = simpleCodec(AlphaBlocks.AlphaLeavesBlock::new);

    public AlphaLeavesBlock(Properties properties) {
      super(0.0F, properties);
    }

    public boolean isRandomlyTicking(BlockState state) {
      return false;
    }

    public void spawnFallingLeavesParticle(Level level, BlockPos pos, RandomSource random) {
    }

    public MapCodec<? extends LeavesBlock> codec() {
      return CODEC;
    }
  }
}
