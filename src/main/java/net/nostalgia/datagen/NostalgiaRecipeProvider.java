package net.nostalgia.datagen;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.HolderLookup.RegistryLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.nostalgia.block.AlphaBlocks;
import net.nostalgia.item.AlphaItems;

public class NostalgiaRecipeProvider extends FabricRecipeProvider {
  public NostalgiaRecipeProvider(FabricPackOutput output, CompletableFuture<Provider> registriesFuture) {
    super(output, registriesFuture);
  }

  public String getName() {
    return "Nostalgia Recipes";
  }

  protected RecipeProvider createRecipeProvider(final Provider registryLookup, final RecipeOutput exporter) {
    return new RecipeProvider(registryLookup, exporter) {
      {
        Objects.requireNonNull(NostalgiaRecipeProvider.this);
      }

      public void buildRecipes() {
        RegistryLookup<Item> itemLookup = registryLookup.lookupOrThrow(Registries.ITEM);
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.TOOLS, AlphaItems.ALPHA_WOODEN_SWORD)
          .pattern("X")
          .pattern("X")
          .pattern("#")
          .define('X', NostalgiaTags.OAK_PLANKS)
          .define('#', NostalgiaTags.STICKS)
          .unlockedBy("has_material", this.has(AlphaBlocks.ALPHA_OAK_PLANKS))
          .save(exporter);
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.TOOLS, AlphaItems.ALPHA_STONE_SWORD)
          .pattern("X")
          .pattern("X")
          .pattern("#")
          .define('X', NostalgiaTags.COBBLESTONES)
          .define('#', NostalgiaTags.STICKS)
          .unlockedBy("has_material", this.has(AlphaBlocks.ALPHA_COBBLESTONE))
          .save(exporter);
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.TOOLS, AlphaItems.ALPHA_IRON_SWORD)
          .pattern("X")
          .pattern("X")
          .pattern("#")
          .define('X', NostalgiaTags.IRON_INGOTS)
          .define('#', NostalgiaTags.STICKS)
          .unlockedBy("has_material", this.has(AlphaItems.ALPHA_IRON_INGOT))
          .save(exporter);
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.TOOLS, AlphaItems.ALPHA_DIAMOND_SWORD)
          .pattern("X")
          .pattern("X")
          .pattern("#")
          .define('X', NostalgiaTags.DIAMONDS)
          .define('#', NostalgiaTags.STICKS)
          .unlockedBy("has_material", this.has(AlphaItems.ALPHA_DIAMOND))
          .save(exporter);
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.TOOLS, AlphaItems.ALPHA_WOODEN_AXE)
          .pattern("XX")
          .pattern("X#")
          .pattern(" #")
          .define('X', NostalgiaTags.OAK_PLANKS)
          .define('#', NostalgiaTags.STICKS)
          .unlockedBy("has_material", this.has(AlphaBlocks.ALPHA_OAK_PLANKS))
          .save(exporter);
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.TOOLS, AlphaItems.ALPHA_STONE_AXE)
          .pattern("XX")
          .pattern("X#")
          .pattern(" #")
          .define('X', NostalgiaTags.COBBLESTONES)
          .define('#', NostalgiaTags.STICKS)
          .unlockedBy("has_material", this.has(AlphaBlocks.ALPHA_COBBLESTONE))
          .save(exporter);
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.TOOLS, AlphaItems.ALPHA_IRON_AXE)
          .pattern("XX")
          .pattern("X#")
          .pattern(" #")
          .define('X', NostalgiaTags.IRON_INGOTS)
          .define('#', NostalgiaTags.STICKS)
          .unlockedBy("has_material", this.has(AlphaItems.ALPHA_IRON_INGOT))
          .save(exporter);
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.TOOLS, AlphaItems.ALPHA_DIAMOND_AXE)
          .pattern("XX")
          .pattern("X#")
          .pattern(" #")
          .define('X', NostalgiaTags.DIAMONDS)
          .define('#', NostalgiaTags.STICKS)
          .unlockedBy("has_material", this.has(AlphaItems.ALPHA_DIAMOND))
          .save(exporter);
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.TOOLS, AlphaItems.ALPHA_GOLDEN_SWORD)
          .pattern("X")
          .pattern("X")
          .pattern("#")
          .define('X', NostalgiaTags.GOLD_INGOTS)
          .define('#', NostalgiaTags.STICKS)
          .unlockedBy("has_material", this.has(AlphaItems.ALPHA_GOLD_INGOT))
          .save(exporter);
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.TOOLS, AlphaItems.ALPHA_GOLDEN_AXE)
          .pattern("XX")
          .pattern("X#")
          .pattern(" #")
          .define('X', NostalgiaTags.GOLD_INGOTS)
          .define('#', NostalgiaTags.STICKS)
          .unlockedBy("has_material", this.has(AlphaItems.ALPHA_GOLD_INGOT))
          .save(exporter);
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.REDSTONE, AlphaItems.ALPHA_WOODEN_DOOR, 1)
          .pattern("XX")
          .pattern("XX")
          .pattern("XX")
          .define('X', NostalgiaTags.OAK_PLANKS)
          .unlockedBy("has_material", this.has(AlphaBlocks.ALPHA_OAK_PLANKS))
          .save(exporter);
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.REDSTONE, AlphaItems.ALPHA_IRON_DOOR, 1)
          .pattern("XX")
          .pattern("XX")
          .pattern("XX")
          .define('X', NostalgiaTags.IRON_INGOTS)
          .unlockedBy("has_material", this.has(AlphaItems.ALPHA_IRON_INGOT))
          .save(exporter);
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.COMBAT, AlphaItems.ALPHA_LEATHER_HELMET)
          .pattern("XXX")
          .pattern("X X")
          .define('X', NostalgiaTags.LEATHER)
          .unlockedBy("has_material", this.has(AlphaItems.ALPHA_LEATHER))
          .save(exporter);
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.COMBAT, AlphaItems.ALPHA_LEATHER_CHESTPLATE)
          .pattern("X X")
          .pattern("XXX")
          .pattern("XXX")
          .define('X', NostalgiaTags.LEATHER)
          .unlockedBy("has_material", this.has(AlphaItems.ALPHA_LEATHER))
          .save(exporter);
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.COMBAT, AlphaItems.ALPHA_LEATHER_LEGGINGS)
          .pattern("XXX")
          .pattern("X X")
          .pattern("X X")
          .define('X', NostalgiaTags.LEATHER)
          .unlockedBy("has_material", this.has(AlphaItems.ALPHA_LEATHER))
          .save(exporter);
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.COMBAT, AlphaItems.ALPHA_LEATHER_BOOTS)
          .pattern("X X")
          .pattern("X X")
          .define('X', NostalgiaTags.LEATHER)
          .unlockedBy("has_material", this.has(AlphaItems.ALPHA_LEATHER))
          .save(exporter);
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.COMBAT, AlphaItems.ALPHA_IRON_HELMET)
          .pattern("XXX")
          .pattern("X X")
          .define('X', NostalgiaTags.IRON_INGOTS)
          .unlockedBy("has_material", this.has(AlphaItems.ALPHA_IRON_INGOT))
          .save(exporter);
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.COMBAT, AlphaItems.ALPHA_IRON_CHESTPLATE)
          .pattern("X X")
          .pattern("XXX")
          .pattern("XXX")
          .define('X', NostalgiaTags.IRON_INGOTS)
          .unlockedBy("has_material", this.has(AlphaItems.ALPHA_IRON_INGOT))
          .save(exporter);
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.COMBAT, AlphaItems.ALPHA_IRON_LEGGINGS)
          .pattern("XXX")
          .pattern("X X")
          .pattern("X X")
          .define('X', NostalgiaTags.IRON_INGOTS)
          .unlockedBy("has_material", this.has(AlphaItems.ALPHA_IRON_INGOT))
          .save(exporter);
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.COMBAT, AlphaItems.ALPHA_IRON_BOOTS)
          .pattern("X X")
          .pattern("X X")
          .define('X', NostalgiaTags.IRON_INGOTS)
          .unlockedBy("has_material", this.has(AlphaItems.ALPHA_IRON_INGOT))
          .save(exporter);
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.COMBAT, AlphaItems.ALPHA_DIAMOND_HELMET)
          .pattern("XXX")
          .pattern("X X")
          .define('X', NostalgiaTags.DIAMONDS)
          .unlockedBy("has_material", this.has(AlphaItems.ALPHA_DIAMOND))
          .save(exporter);
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.COMBAT, AlphaItems.ALPHA_DIAMOND_CHESTPLATE)
          .pattern("X X")
          .pattern("XXX")
          .pattern("XXX")
          .define('X', NostalgiaTags.DIAMONDS)
          .unlockedBy("has_material", this.has(AlphaItems.ALPHA_DIAMOND))
          .save(exporter);
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.COMBAT, AlphaItems.ALPHA_DIAMOND_LEGGINGS)
          .pattern("XXX")
          .pattern("X X")
          .pattern("X X")
          .define('X', NostalgiaTags.DIAMONDS)
          .unlockedBy("has_material", this.has(AlphaItems.ALPHA_DIAMOND))
          .save(exporter);
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.COMBAT, AlphaItems.ALPHA_DIAMOND_BOOTS)
          .pattern("X X")
          .pattern("X X")
          .define('X', NostalgiaTags.DIAMONDS)
          .unlockedBy("has_material", this.has(AlphaItems.ALPHA_DIAMOND))
          .save(exporter);
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.BUILDING_BLOCKS, AlphaBlocks.ALPHA_OAK_PLANKS, 4)
          .pattern("X")
          .define('X', NostalgiaTags.OAK_LOGS)
          .unlockedBy("has_material", this.has(AlphaBlocks.ALPHA_OAK_LOG))
          .save(exporter);
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.BUILDING_BLOCKS, AlphaBlocks.ALPHA_CRAFTING_TABLE, 1)
          .pattern("XX")
          .pattern("XX")
          .define('X', NostalgiaTags.OAK_PLANKS)
          .unlockedBy("has_material", this.has(AlphaBlocks.ALPHA_OAK_PLANKS))
          .save(exporter);
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.BUILDING_BLOCKS, AlphaBlocks.ALPHA_CHEST, 1)
          .pattern("XXX")
          .pattern("X X")
          .pattern("XXX")
          .define('X', NostalgiaTags.OAK_PLANKS)
          .unlockedBy("has_material", this.has(AlphaBlocks.ALPHA_OAK_PLANKS))
          .save(exporter);
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.BUILDING_BLOCKS, AlphaBlocks.ALPHA_FURNACE, 1)
          .pattern("XXX")
          .pattern("X X")
          .pattern("XXX")
          .define('X', NostalgiaTags.COBBLESTONES)
          .unlockedBy("has_material", this.has(AlphaBlocks.ALPHA_COBBLESTONE))
          .save(exporter);
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.MISC, AlphaItems.ALPHA_STICK, 4)
          .pattern("X")
          .pattern("X")
          .define('X', NostalgiaTags.OAK_PLANKS)
          .unlockedBy("has_material", this.has(AlphaBlocks.ALPHA_OAK_PLANKS))
          .save(exporter);
        SimpleCookingRecipeBuilder.smelting(
            Ingredient.of(AlphaBlocks.ALPHA_IRON_ORE), RecipeCategory.MISC, CookingBookCategory.MISC, AlphaItems.ALPHA_IRON_INGOT, 0.7F, 200
          )
          .unlockedBy("has_iron_ore", this.has(AlphaBlocks.ALPHA_IRON_ORE))
          .save(exporter, "alpha_iron_ingot_from_smelting");
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.DECORATIONS, Items.TORCH, 4)
          .pattern("X")
          .pattern("#")
          .define('X', NostalgiaTags.COAL)
          .define('#', NostalgiaTags.STICKS)
          .unlockedBy("has_material", this.has(AlphaItems.ALPHA_COAL))
          .save(exporter, "vanilla_torch_from_alpha_coal");
        SimpleCookingRecipeBuilder.smelting(
            Ingredient.of(AlphaBlocks.ALPHA_GOLD_ORE), RecipeCategory.MISC, CookingBookCategory.MISC, AlphaItems.ALPHA_GOLD_INGOT, 1.0F, 200
          )
          .unlockedBy("has_gold_ore", this.has(AlphaBlocks.ALPHA_GOLD_ORE))
          .save(exporter, "alpha_gold_ingot_from_smelting");
        SimpleCookingRecipeBuilder.smelting(
            Ingredient.of(itemLookup.getOrThrow(NostalgiaTags.COBBLESTONES)),
            RecipeCategory.BUILDING_BLOCKS,
            CookingBookCategory.BLOCKS,
            AlphaBlocks.ALPHA_STONE,
            0.1F,
            200
          )
          .unlockedBy("has_cobblestone", this.has(AlphaBlocks.ALPHA_COBBLESTONE))
          .save(exporter, "alpha_stone_from_smelting");
        SimpleCookingRecipeBuilder.smelting(
            Ingredient.of(AlphaItems.ALPHA_PORKCHOP), RecipeCategory.FOOD, CookingBookCategory.FOOD, AlphaItems.ALPHA_COOKED_PORKCHOP, 0.35F, 200
          )
          .unlockedBy("has_porkchop", this.has(AlphaItems.ALPHA_PORKCHOP))
          .save(exporter, "alpha_cooked_porkchop_from_smelting");
        SimpleCookingRecipeBuilder.smelting(
            Ingredient.of(itemLookup.getOrThrow(NostalgiaTags.CLAY_BALLS)), RecipeCategory.MISC, CookingBookCategory.MISC, AlphaItems.ALPHA_BRICK, 0.3F, 200
          )
          .unlockedBy("has_clay_ball", this.has(AlphaItems.ALPHA_CLAY_BALL))
          .save(exporter, "alpha_brick_from_smelting");
        SimpleCookingRecipeBuilder.smelting(
            Ingredient.of(itemLookup.getOrThrow(NostalgiaTags.SAND)),
            RecipeCategory.BUILDING_BLOCKS,
            CookingBookCategory.BLOCKS,
            AlphaBlocks.ALPHA_GLASS,
            0.1F,
            200
          )
          .unlockedBy("has_sand", this.has(AlphaBlocks.ALPHA_SAND))
          .save(exporter, "alpha_glass_from_smelting");
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.COMBAT, AlphaItems.ALPHA_GOLDEN_HELMET)
          .pattern("XXX")
          .pattern("X X")
          .define('X', NostalgiaTags.GOLD_INGOTS)
          .unlockedBy("has_material", this.has(AlphaItems.ALPHA_GOLD_INGOT))
          .save(exporter);
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.COMBAT, AlphaItems.ALPHA_GOLDEN_CHESTPLATE)
          .pattern("X X")
          .pattern("XXX")
          .pattern("XXX")
          .define('X', NostalgiaTags.GOLD_INGOTS)
          .unlockedBy("has_material", this.has(AlphaItems.ALPHA_GOLD_INGOT))
          .save(exporter);
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.COMBAT, AlphaItems.ALPHA_GOLDEN_LEGGINGS)
          .pattern("XXX")
          .pattern("X X")
          .pattern("X X")
          .define('X', NostalgiaTags.GOLD_INGOTS)
          .unlockedBy("has_material", this.has(AlphaItems.ALPHA_GOLD_INGOT))
          .save(exporter);
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.COMBAT, AlphaItems.ALPHA_GOLDEN_BOOTS)
          .pattern("X X")
          .pattern("X X")
          .define('X', NostalgiaTags.GOLD_INGOTS)
          .unlockedBy("has_material", this.has(AlphaItems.ALPHA_GOLD_INGOT))
          .save(exporter);
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.COMBAT, AlphaItems.ALPHA_BOW)
          .pattern(" #X")
          .pattern("# X")
          .pattern(" #X")
          .define('#', NostalgiaTags.STICKS)
          .define('X', NostalgiaTags.STRINGS)
          .unlockedBy("has_material", this.has(AlphaItems.ALPHA_STRING))
          .save(exporter);
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.COMBAT, AlphaItems.ALPHA_ARROW, 4)
          .pattern("X")
          .pattern("#")
          .pattern("Y")
          .define('X', NostalgiaTags.FLINT)
          .define('#', NostalgiaTags.STICKS)
          .define('Y', NostalgiaTags.FEATHERS)
          .unlockedBy("has_material", this.has(AlphaItems.ALPHA_FLINT))
          .save(exporter);
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.BUILDING_BLOCKS, AlphaBlocks.ALPHA_IRON_BLOCK, 1)
          .pattern("XXX")
          .pattern("XXX")
          .pattern("XXX")
          .define('X', NostalgiaTags.IRON_INGOTS)
          .unlockedBy("has_material", this.has(AlphaItems.ALPHA_IRON_INGOT))
          .save(exporter);
        ShapelessRecipeBuilder.shapeless(itemLookup, RecipeCategory.MISC, AlphaItems.ALPHA_IRON_INGOT, 9)
          .requires(AlphaBlocks.ALPHA_IRON_BLOCK)
          .unlockedBy("has_material", this.has(AlphaBlocks.ALPHA_IRON_BLOCK))
          .save(exporter, "alpha_iron_ingot_from_block");
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.BUILDING_BLOCKS, AlphaBlocks.ALPHA_GOLD_BLOCK, 1)
          .pattern("XXX")
          .pattern("XXX")
          .pattern("XXX")
          .define('X', NostalgiaTags.GOLD_INGOTS)
          .unlockedBy("has_material", this.has(AlphaItems.ALPHA_GOLD_INGOT))
          .save(exporter);
        ShapelessRecipeBuilder.shapeless(itemLookup, RecipeCategory.MISC, AlphaItems.ALPHA_GOLD_INGOT, 9)
          .requires(AlphaBlocks.ALPHA_GOLD_BLOCK)
          .unlockedBy("has_material", this.has(AlphaBlocks.ALPHA_GOLD_BLOCK))
          .save(exporter, "alpha_gold_ingot_from_block");
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.BUILDING_BLOCKS, AlphaBlocks.ALPHA_DIAMOND_BLOCK, 1)
          .pattern("XXX")
          .pattern("XXX")
          .pattern("XXX")
          .define('X', NostalgiaTags.DIAMONDS)
          .unlockedBy("has_material", this.has(AlphaItems.ALPHA_DIAMOND))
          .save(exporter);
        ShapelessRecipeBuilder.shapeless(itemLookup, RecipeCategory.MISC, AlphaItems.ALPHA_DIAMOND, 9)
          .requires(AlphaBlocks.ALPHA_DIAMOND_BLOCK)
          .unlockedBy("has_material", this.has(AlphaBlocks.ALPHA_DIAMOND_BLOCK))
          .save(exporter, "alpha_diamond_from_block");
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.FOOD, AlphaItems.ALPHA_BREAD)
          .pattern("XXX")
          .define('X', NostalgiaTags.WHEAT)
          .unlockedBy("has_material", this.has(AlphaItems.ALPHA_WHEAT))
          .save(exporter);
        ShapelessRecipeBuilder.shapeless(itemLookup, RecipeCategory.FOOD, AlphaItems.ALPHA_MUSHROOM_STEW, 1)
          .requires(AlphaItems.ALPHA_BOWL)
          .requires(AlphaBlocks.ALPHA_RED_MUSHROOM)
          .requires(AlphaBlocks.ALPHA_BROWN_MUSHROOM)
          .unlockedBy("has_material", this.has(AlphaItems.ALPHA_BOWL))
          .save(exporter, "alpha_mushroom_stew");
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.MISC, AlphaItems.ALPHA_BOWL, 4)
          .pattern("X X")
          .pattern(" X ")
          .define('X', NostalgiaTags.OAK_PLANKS)
          .unlockedBy("has_material", this.has(AlphaBlocks.ALPHA_OAK_PLANKS))
          .save(exporter);
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.MISC, AlphaItems.ALPHA_PAPER, 3)
          .pattern("XXX")
          .define('X', AlphaBlocks.ALPHA_SUGAR_CANE)
          .unlockedBy("has_material", this.has(AlphaBlocks.ALPHA_SUGAR_CANE))
          .save(exporter);
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.MISC, AlphaItems.ALPHA_BOOK)
          .pattern("X")
          .pattern("X")
          .pattern("X")
          .define('X', NostalgiaTags.PAPER)
          .unlockedBy("has_material", this.has(AlphaItems.ALPHA_PAPER))
          .save(exporter);
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.MISC, AlphaItems.ALPHA_BUCKET)
          .pattern("X X")
          .pattern(" X ")
          .define('X', NostalgiaTags.IRON_INGOTS)
          .unlockedBy("has_material", this.has(AlphaItems.ALPHA_IRON_INGOT))
          .save(exporter);
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.TOOLS, AlphaItems.ALPHA_FLINT_AND_STEEL)
          .pattern("X ")
          .pattern(" Y")
          .define('X', NostalgiaTags.IRON_INGOTS)
          .define('Y', NostalgiaTags.FLINT)
          .unlockedBy("has_material", this.has(AlphaItems.ALPHA_IRON_INGOT))
          .save(exporter);
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.TOOLS, AlphaItems.ALPHA_COMPASS)
          .pattern(" X ")
          .pattern("X#X")
          .pattern(" X ")
          .define('X', NostalgiaTags.IRON_INGOTS)
          .define('#', NostalgiaTags.REDSTONE)
          .unlockedBy("has_material", this.has(AlphaItems.ALPHA_REDSTONE))
          .save(exporter);
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.TOOLS, Items.FISHING_ROD)
          .pattern("  X")
          .pattern(" X#")
          .pattern("X #")
          .define('X', NostalgiaTags.STICKS)
          .define('#', NostalgiaTags.STRINGS)
          .unlockedBy("has_material", this.has(AlphaItems.ALPHA_STRING))
          .save(exporter, "vanilla_fishing_rod_from_alpha");
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.DECORATIONS, AlphaItems.ALPHA_PAINTING)
          .pattern("XXX")
          .pattern("X#X")
          .pattern("XXX")
          .define('X', NostalgiaTags.STICKS)
          .define('#', AlphaBlocks.ALPHA_WOOL)
          .unlockedBy("has_material", this.has(AlphaBlocks.ALPHA_WOOL))
          .save(exporter);
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.FOOD, AlphaItems.ALPHA_GOLDEN_APPLE)
          .pattern("XXX")
          .pattern("X#X")
          .pattern("XXX")
          .define('X', AlphaBlocks.ALPHA_GOLD_BLOCK)
          .define('#', NostalgiaTags.APPLES)
          .unlockedBy("has_material", this.has(AlphaBlocks.ALPHA_GOLD_BLOCK))
          .save(exporter);
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.DECORATIONS, AlphaItems.ALPHA_SIGN, 1)
          .pattern("XXX")
          .pattern("XXX")
          .pattern(" # ")
          .define('X', NostalgiaTags.OAK_PLANKS)
          .define('#', NostalgiaTags.STICKS)
          .unlockedBy("has_material", this.has(AlphaBlocks.ALPHA_OAK_PLANKS))
          .save(exporter);
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.TRANSPORTATION, AlphaItems.ALPHA_MINECART)
          .pattern("X X")
          .pattern("XXX")
          .define('X', NostalgiaTags.IRON_INGOTS)
          .unlockedBy("has_material", this.has(AlphaItems.ALPHA_IRON_INGOT))
          .save(exporter);
        ShapelessRecipeBuilder.shapeless(itemLookup, RecipeCategory.TRANSPORTATION, AlphaItems.ALPHA_CHEST_MINECART, 1)
          .requires(AlphaBlocks.ALPHA_CHEST)
          .requires(AlphaItems.ALPHA_MINECART)
          .unlockedBy("has_material", this.has(AlphaItems.ALPHA_MINECART))
          .save(exporter, "alpha_chest_minecart");
        ShapelessRecipeBuilder.shapeless(itemLookup, RecipeCategory.TRANSPORTATION, AlphaItems.ALPHA_FURNACE_MINECART, 1)
          .requires(AlphaBlocks.ALPHA_FURNACE)
          .requires(AlphaItems.ALPHA_MINECART)
          .unlockedBy("has_material", this.has(AlphaItems.ALPHA_MINECART))
          .save(exporter, "alpha_furnace_minecart");
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.TRANSPORTATION, AlphaItems.ALPHA_BOAT)
          .pattern("X X")
          .pattern("XXX")
          .define('X', NostalgiaTags.OAK_PLANKS)
          .unlockedBy("has_material", this.has(AlphaBlocks.ALPHA_OAK_PLANKS))
          .save(exporter);
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.BUILDING_BLOCKS, AlphaBlocks.ALPHA_BRICKS, 1)
          .pattern("XX")
          .pattern("XX")
          .define('X', NostalgiaTags.BRICKS)
          .unlockedBy("has_material", this.has(AlphaItems.ALPHA_BRICK))
          .save(exporter);
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.BUILDING_BLOCKS, AlphaBlocks.ALPHA_TNT, 1)
          .pattern("X#X")
          .pattern("#X#")
          .pattern("X#X")
          .define('X', NostalgiaTags.GUNPOWDER)
          .define('#', NostalgiaTags.SAND)
          .unlockedBy("has_material", this.has(AlphaItems.ALPHA_GUNPOWDER))
          .save(exporter);
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.BUILDING_BLOCKS, AlphaBlocks.ALPHA_BOOKSHELF, 1)
          .pattern("XXX")
          .pattern("###")
          .pattern("XXX")
          .define('X', NostalgiaTags.OAK_PLANKS)
          .define('#', NostalgiaTags.BOOKS)
          .unlockedBy("has_material", this.has(AlphaItems.ALPHA_BOOK))
          .save(exporter);
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.BUILDING_BLOCKS, AlphaBlocks.ALPHA_WOOL, 1)
          .pattern("XXX")
          .pattern("XXX")
          .pattern("XXX")
          .define('X', NostalgiaTags.STRINGS)
          .unlockedBy("has_material", this.has(AlphaItems.ALPHA_STRING))
          .save(exporter);
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.BUILDING_BLOCKS, AlphaBlocks.ALPHA_SNOW_BLOCK, 1)
          .pattern("XX")
          .pattern("XX")
          .define('X', NostalgiaTags.SNOWBALLS)
          .unlockedBy("has_material", this.has(AlphaItems.ALPHA_SNOWBALL))
          .save(exporter);
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.BUILDING_BLOCKS, AlphaBlocks.ALPHA_CLAY, 1)
          .pattern("XX")
          .pattern("XX")
          .define('X', NostalgiaTags.CLAY_BALLS)
          .unlockedBy("has_material", this.has(AlphaItems.ALPHA_CLAY_BALL))
          .save(exporter);
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.DECORATIONS, AlphaBlocks.ALPHA_LADDER, 1)
          .pattern("X X")
          .pattern("XXX")
          .pattern("X X")
          .define('X', NostalgiaTags.STICKS)
          .unlockedBy("has_material", this.has(AlphaItems.ALPHA_STICK))
          .save(exporter);
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.REDSTONE, AlphaBlocks.ALPHA_LEVER, 1)
          .pattern("X")
          .pattern("#")
          .define('X', NostalgiaTags.STICKS)
          .define('#', NostalgiaTags.COBBLESTONES)
          .unlockedBy("has_material", this.has(AlphaBlocks.ALPHA_COBBLESTONE))
          .save(exporter);
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.REDSTONE, AlphaBlocks.ALPHA_REDSTONE_TORCH_ITEM, 1)
          .pattern("X")
          .pattern("#")
          .define('X', NostalgiaTags.REDSTONE)
          .define('#', NostalgiaTags.STICKS)
          .unlockedBy("has_material", this.has(AlphaItems.ALPHA_REDSTONE))
          .save(exporter, "alpha_redstone_torch");
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.TOOLS, Items.WOODEN_PICKAXE)
          .pattern("XXX")
          .pattern(" # ")
          .pattern(" # ")
          .define('X', NostalgiaTags.OAK_PLANKS)
          .define('#', NostalgiaTags.STICKS)
          .unlockedBy("has_material", this.has(AlphaBlocks.ALPHA_OAK_PLANKS))
          .save(exporter, "vanilla_wooden_pickaxe_from_alpha");
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.TOOLS, Items.STONE_PICKAXE)
          .pattern("XXX")
          .pattern(" # ")
          .pattern(" # ")
          .define('X', NostalgiaTags.COBBLESTONES)
          .define('#', NostalgiaTags.STICKS)
          .unlockedBy("has_material", this.has(AlphaBlocks.ALPHA_COBBLESTONE))
          .save(exporter, "vanilla_stone_pickaxe_from_alpha");
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.TOOLS, Items.IRON_PICKAXE)
          .pattern("XXX")
          .pattern(" # ")
          .pattern(" # ")
          .define('X', NostalgiaTags.IRON_INGOTS)
          .define('#', NostalgiaTags.STICKS)
          .unlockedBy("has_material", this.has(AlphaItems.ALPHA_IRON_INGOT))
          .save(exporter, "vanilla_iron_pickaxe_from_alpha");
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.TOOLS, Items.GOLDEN_PICKAXE)
          .pattern("XXX")
          .pattern(" # ")
          .pattern(" # ")
          .define('X', NostalgiaTags.GOLD_INGOTS)
          .define('#', NostalgiaTags.STICKS)
          .unlockedBy("has_material", this.has(AlphaItems.ALPHA_GOLD_INGOT))
          .save(exporter, "vanilla_golden_pickaxe_from_alpha");
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.TOOLS, Items.DIAMOND_PICKAXE)
          .pattern("XXX")
          .pattern(" # ")
          .pattern(" # ")
          .define('X', NostalgiaTags.DIAMONDS)
          .define('#', NostalgiaTags.STICKS)
          .unlockedBy("has_material", this.has(AlphaItems.ALPHA_DIAMOND))
          .save(exporter, "vanilla_diamond_pickaxe_from_alpha");
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.TOOLS, Items.WOODEN_SHOVEL)
          .pattern("X")
          .pattern("#")
          .pattern("#")
          .define('X', NostalgiaTags.OAK_PLANKS)
          .define('#', NostalgiaTags.STICKS)
          .unlockedBy("has_material", this.has(AlphaBlocks.ALPHA_OAK_PLANKS))
          .save(exporter, "vanilla_wooden_shovel_from_alpha");
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.TOOLS, Items.STONE_SHOVEL)
          .pattern("X")
          .pattern("#")
          .pattern("#")
          .define('X', NostalgiaTags.COBBLESTONES)
          .define('#', NostalgiaTags.STICKS)
          .unlockedBy("has_material", this.has(AlphaBlocks.ALPHA_COBBLESTONE))
          .save(exporter, "vanilla_stone_shovel_from_alpha");
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.TOOLS, Items.IRON_SHOVEL)
          .pattern("X")
          .pattern("#")
          .pattern("#")
          .define('X', NostalgiaTags.IRON_INGOTS)
          .define('#', NostalgiaTags.STICKS)
          .unlockedBy("has_material", this.has(AlphaItems.ALPHA_IRON_INGOT))
          .save(exporter, "vanilla_iron_shovel_from_alpha");
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.TOOLS, Items.GOLDEN_SHOVEL)
          .pattern("X")
          .pattern("#")
          .pattern("#")
          .define('X', NostalgiaTags.GOLD_INGOTS)
          .define('#', NostalgiaTags.STICKS)
          .unlockedBy("has_material", this.has(AlphaItems.ALPHA_GOLD_INGOT))
          .save(exporter, "vanilla_golden_shovel_from_alpha");
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.TOOLS, Items.DIAMOND_SHOVEL)
          .pattern("X")
          .pattern("#")
          .pattern("#")
          .define('X', NostalgiaTags.DIAMONDS)
          .define('#', NostalgiaTags.STICKS)
          .unlockedBy("has_material", this.has(AlphaItems.ALPHA_DIAMOND))
          .save(exporter, "vanilla_diamond_shovel_from_alpha");
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.TOOLS, Items.WOODEN_HOE)
          .pattern("XX")
          .pattern(" #")
          .pattern(" #")
          .define('X', NostalgiaTags.OAK_PLANKS)
          .define('#', NostalgiaTags.STICKS)
          .unlockedBy("has_material", this.has(AlphaBlocks.ALPHA_OAK_PLANKS))
          .save(exporter, "vanilla_wooden_hoe_from_alpha");
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.TOOLS, Items.STONE_HOE)
          .pattern("XX")
          .pattern(" #")
          .pattern(" #")
          .define('X', NostalgiaTags.COBBLESTONES)
          .define('#', NostalgiaTags.STICKS)
          .unlockedBy("has_material", this.has(AlphaBlocks.ALPHA_COBBLESTONE))
          .save(exporter, "vanilla_stone_hoe_from_alpha");
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.TOOLS, Items.IRON_HOE)
          .pattern("XX")
          .pattern(" #")
          .pattern(" #")
          .define('X', NostalgiaTags.IRON_INGOTS)
          .define('#', NostalgiaTags.STICKS)
          .unlockedBy("has_material", this.has(AlphaItems.ALPHA_IRON_INGOT))
          .save(exporter, "vanilla_iron_hoe_from_alpha");
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.TOOLS, Items.GOLDEN_HOE)
          .pattern("XX")
          .pattern(" #")
          .pattern(" #")
          .define('X', NostalgiaTags.GOLD_INGOTS)
          .define('#', NostalgiaTags.STICKS)
          .unlockedBy("has_material", this.has(AlphaItems.ALPHA_GOLD_INGOT))
          .save(exporter, "vanilla_golden_hoe_from_alpha");
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.TOOLS, Items.DIAMOND_HOE)
          .pattern("XX")
          .pattern(" #")
          .pattern(" #")
          .define('X', NostalgiaTags.DIAMONDS)
          .define('#', NostalgiaTags.STICKS)
          .unlockedBy("has_material", this.has(AlphaItems.ALPHA_DIAMOND))
          .save(exporter, "vanilla_diamond_hoe_from_alpha");
      }
    };
  }
}
