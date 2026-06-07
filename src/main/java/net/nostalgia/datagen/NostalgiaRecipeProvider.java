package net.nostalgia.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.nostalgia.block.AlphaBlocks;
import net.nostalgia.item.AlphaItems;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.CookingBookCategory;
import java.util.concurrent.CompletableFuture;

public class NostalgiaRecipeProvider extends FabricRecipeProvider {
    public NostalgiaRecipeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public String getName() {
        return "Nostalgia Recipes";
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider registryLookup, RecipeOutput exporter) {
        return new RecipeProvider(registryLookup, exporter) {
            @Override
            public void buildRecipes() {
        ShapedRecipeBuilder.shaped(registryLookup.lookupOrThrow(net.minecraft.core.registries.Registries.ITEM), RecipeCategory.TOOLS, AlphaItems.ALPHA_WOODEN_SWORD)
            .pattern("X")
            .pattern("X")
            .pattern("#")
            .define('X', AlphaBlocks.ALPHA_OAK_PLANKS)
            .define('#', AlphaItems.ALPHA_STICK)
            .unlockedBy("has_material", has(AlphaBlocks.ALPHA_OAK_PLANKS))
            .save(exporter);

        ShapedRecipeBuilder.shaped(registryLookup.lookupOrThrow(net.minecraft.core.registries.Registries.ITEM), RecipeCategory.TOOLS, AlphaItems.ALPHA_STONE_SWORD)
            .pattern("X")
            .pattern("X")
            .pattern("#")
            .define('X', AlphaBlocks.ALPHA_COBBLESTONE)
            .define('#', AlphaItems.ALPHA_STICK)
            .unlockedBy("has_material", has(AlphaBlocks.ALPHA_COBBLESTONE))
            .save(exporter);

        ShapedRecipeBuilder.shaped(registryLookup.lookupOrThrow(net.minecraft.core.registries.Registries.ITEM), RecipeCategory.TOOLS, AlphaItems.ALPHA_IRON_SWORD)
            .pattern("X")
            .pattern("X")
            .pattern("#")
            .define('X', AlphaItems.ALPHA_IRON_INGOT)
            .define('#', AlphaItems.ALPHA_STICK)
            .unlockedBy("has_material", has(AlphaItems.ALPHA_IRON_INGOT))
            .save(exporter);

        ShapedRecipeBuilder.shaped(registryLookup.lookupOrThrow(net.minecraft.core.registries.Registries.ITEM), RecipeCategory.TOOLS, AlphaItems.ALPHA_DIAMOND_SWORD)
            .pattern("X")
            .pattern("X")
            .pattern("#")
            .define('X', AlphaItems.ALPHA_DIAMOND)
            .define('#', AlphaItems.ALPHA_STICK)
            .unlockedBy("has_material", has(AlphaItems.ALPHA_DIAMOND))
            .save(exporter);

        ShapedRecipeBuilder.shaped(registryLookup.lookupOrThrow(net.minecraft.core.registries.Registries.ITEM), RecipeCategory.COMBAT, AlphaItems.ALPHA_LEATHER_HELMET)
            .pattern("XXX")
            .pattern("X X")
            .define('X', AlphaItems.ALPHA_LEATHER)
            .unlockedBy("has_material", has(AlphaItems.ALPHA_LEATHER))
            .save(exporter);
        ShapedRecipeBuilder.shaped(registryLookup.lookupOrThrow(net.minecraft.core.registries.Registries.ITEM), RecipeCategory.COMBAT, AlphaItems.ALPHA_LEATHER_CHESTPLATE)
            .pattern("X X")
            .pattern("XXX")
            .pattern("XXX")
            .define('X', AlphaItems.ALPHA_LEATHER)
            .unlockedBy("has_material", has(AlphaItems.ALPHA_LEATHER))
            .save(exporter);
        ShapedRecipeBuilder.shaped(registryLookup.lookupOrThrow(net.minecraft.core.registries.Registries.ITEM), RecipeCategory.COMBAT, AlphaItems.ALPHA_LEATHER_LEGGINGS)
            .pattern("XXX")
            .pattern("X X")
            .pattern("X X")
            .define('X', AlphaItems.ALPHA_LEATHER)
            .unlockedBy("has_material", has(AlphaItems.ALPHA_LEATHER))
            .save(exporter);
        ShapedRecipeBuilder.shaped(registryLookup.lookupOrThrow(net.minecraft.core.registries.Registries.ITEM), RecipeCategory.COMBAT, AlphaItems.ALPHA_LEATHER_BOOTS)
            .pattern("X X")
            .pattern("X X")
            .define('X', AlphaItems.ALPHA_LEATHER)
            .unlockedBy("has_material", has(AlphaItems.ALPHA_LEATHER))
            .save(exporter);
        ShapedRecipeBuilder.shaped(registryLookup.lookupOrThrow(net.minecraft.core.registries.Registries.ITEM), RecipeCategory.COMBAT, AlphaItems.ALPHA_CHAINMAIL_HELMET)
            .pattern("XXX")
            .pattern("X X")
            .define('X', AlphaItems.ALPHA_IRON_INGOT)
            .unlockedBy("has_material", has(AlphaItems.ALPHA_IRON_INGOT))
            .save(exporter);
        ShapedRecipeBuilder.shaped(registryLookup.lookupOrThrow(net.minecraft.core.registries.Registries.ITEM), RecipeCategory.COMBAT, AlphaItems.ALPHA_CHAINMAIL_CHESTPLATE)
            .pattern("X X")
            .pattern("XXX")
            .pattern("XXX")
            .define('X', AlphaItems.ALPHA_IRON_INGOT)
            .unlockedBy("has_material", has(AlphaItems.ALPHA_IRON_INGOT))
            .save(exporter);
        ShapedRecipeBuilder.shaped(registryLookup.lookupOrThrow(net.minecraft.core.registries.Registries.ITEM), RecipeCategory.COMBAT, AlphaItems.ALPHA_CHAINMAIL_LEGGINGS)
            .pattern("XXX")
            .pattern("X X")
            .pattern("X X")
            .define('X', AlphaItems.ALPHA_IRON_INGOT)
            .unlockedBy("has_material", has(AlphaItems.ALPHA_IRON_INGOT))
            .save(exporter);
        ShapedRecipeBuilder.shaped(registryLookup.lookupOrThrow(net.minecraft.core.registries.Registries.ITEM), RecipeCategory.COMBAT, AlphaItems.ALPHA_CHAINMAIL_BOOTS)
            .pattern("X X")
            .pattern("X X")
            .define('X', AlphaItems.ALPHA_IRON_INGOT)
            .unlockedBy("has_material", has(AlphaItems.ALPHA_IRON_INGOT))
            .save(exporter);
        ShapedRecipeBuilder.shaped(registryLookup.lookupOrThrow(net.minecraft.core.registries.Registries.ITEM), RecipeCategory.COMBAT, AlphaItems.ALPHA_IRON_HELMET)
            .pattern("XXX")
            .pattern("X X")
            .define('X', AlphaItems.ALPHA_IRON_INGOT)
            .unlockedBy("has_material", has(AlphaItems.ALPHA_IRON_INGOT))
            .save(exporter);
        ShapedRecipeBuilder.shaped(registryLookup.lookupOrThrow(net.minecraft.core.registries.Registries.ITEM), RecipeCategory.COMBAT, AlphaItems.ALPHA_IRON_CHESTPLATE)
            .pattern("X X")
            .pattern("XXX")
            .pattern("XXX")
            .define('X', AlphaItems.ALPHA_IRON_INGOT)
            .unlockedBy("has_material", has(AlphaItems.ALPHA_IRON_INGOT))
            .save(exporter);
        ShapedRecipeBuilder.shaped(registryLookup.lookupOrThrow(net.minecraft.core.registries.Registries.ITEM), RecipeCategory.COMBAT, AlphaItems.ALPHA_IRON_LEGGINGS)
            .pattern("XXX")
            .pattern("X X")
            .pattern("X X")
            .define('X', AlphaItems.ALPHA_IRON_INGOT)
            .unlockedBy("has_material", has(AlphaItems.ALPHA_IRON_INGOT))
            .save(exporter);
        ShapedRecipeBuilder.shaped(registryLookup.lookupOrThrow(net.minecraft.core.registries.Registries.ITEM), RecipeCategory.COMBAT, AlphaItems.ALPHA_IRON_BOOTS)
            .pattern("X X")
            .pattern("X X")
            .define('X', AlphaItems.ALPHA_IRON_INGOT)
            .unlockedBy("has_material", has(AlphaItems.ALPHA_IRON_INGOT))
            .save(exporter);
        ShapedRecipeBuilder.shaped(registryLookup.lookupOrThrow(net.minecraft.core.registries.Registries.ITEM), RecipeCategory.COMBAT, AlphaItems.ALPHA_DIAMOND_HELMET)
            .pattern("XXX")
            .pattern("X X")
            .define('X', AlphaItems.ALPHA_DIAMOND)
            .unlockedBy("has_material", has(AlphaItems.ALPHA_DIAMOND))
            .save(exporter);
        ShapedRecipeBuilder.shaped(registryLookup.lookupOrThrow(net.minecraft.core.registries.Registries.ITEM), RecipeCategory.COMBAT, AlphaItems.ALPHA_DIAMOND_CHESTPLATE)
            .pattern("X X")
            .pattern("XXX")
            .pattern("XXX")
            .define('X', AlphaItems.ALPHA_DIAMOND)
            .unlockedBy("has_material", has(AlphaItems.ALPHA_DIAMOND))
            .save(exporter);
        ShapedRecipeBuilder.shaped(registryLookup.lookupOrThrow(net.minecraft.core.registries.Registries.ITEM), RecipeCategory.COMBAT, AlphaItems.ALPHA_DIAMOND_LEGGINGS)
            .pattern("XXX")
            .pattern("X X")
            .pattern("X X")
            .define('X', AlphaItems.ALPHA_DIAMOND)
            .unlockedBy("has_material", has(AlphaItems.ALPHA_DIAMOND))
            .save(exporter);
        ShapedRecipeBuilder.shaped(registryLookup.lookupOrThrow(net.minecraft.core.registries.Registries.ITEM), RecipeCategory.COMBAT, AlphaItems.ALPHA_DIAMOND_BOOTS)
            .pattern("X X")
            .pattern("X X")
            .define('X', AlphaItems.ALPHA_DIAMOND)
            .unlockedBy("has_material", has(AlphaItems.ALPHA_DIAMOND))
            .save(exporter);
        ShapedRecipeBuilder.shaped(registryLookup.lookupOrThrow(net.minecraft.core.registries.Registries.ITEM), RecipeCategory.BUILDING_BLOCKS, AlphaBlocks.ALPHA_OAK_PLANKS, 4)
            .pattern("X")
            .define('X', AlphaBlocks.ALPHA_OAK_LOG)
            .unlockedBy("has_material", has(AlphaBlocks.ALPHA_OAK_LOG))
            .save(exporter);
        ShapedRecipeBuilder.shaped(registryLookup.lookupOrThrow(net.minecraft.core.registries.Registries.ITEM), RecipeCategory.BUILDING_BLOCKS, AlphaBlocks.ALPHA_CRAFTING_TABLE, 1)
            .pattern("XX")
            .pattern("XX")
            .define('X', AlphaBlocks.ALPHA_OAK_PLANKS)
            .unlockedBy("has_material", has(AlphaBlocks.ALPHA_OAK_PLANKS))
            .save(exporter);
        ShapedRecipeBuilder.shaped(registryLookup.lookupOrThrow(net.minecraft.core.registries.Registries.ITEM), RecipeCategory.BUILDING_BLOCKS, AlphaBlocks.ALPHA_CHEST, 1)
            .pattern("XXX")
            .pattern("X X")
            .pattern("XXX")
            .define('X', AlphaBlocks.ALPHA_OAK_PLANKS)
            .unlockedBy("has_material", has(AlphaBlocks.ALPHA_OAK_PLANKS))
            .save(exporter);
        ShapedRecipeBuilder.shaped(registryLookup.lookupOrThrow(net.minecraft.core.registries.Registries.ITEM), RecipeCategory.BUILDING_BLOCKS, AlphaBlocks.ALPHA_FURNACE, 1)
            .pattern("XXX")
            .pattern("X X")
            .pattern("XXX")
            .define('X', AlphaBlocks.ALPHA_COBBLESTONE)
            .unlockedBy("has_material", has(AlphaBlocks.ALPHA_COBBLESTONE))
            .save(exporter);
        ShapedRecipeBuilder.shaped(registryLookup.lookupOrThrow(net.minecraft.core.registries.Registries.ITEM), RecipeCategory.MISC, AlphaItems.ALPHA_STICK, 4)
            .pattern("X")
            .pattern("X")
            .define('X', AlphaBlocks.ALPHA_OAK_PLANKS)
            .unlockedBy("has_material", has(AlphaBlocks.ALPHA_OAK_PLANKS))
            .save(exporter);

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(AlphaBlocks.ALPHA_IRON_ORE), RecipeCategory.MISC, CookingBookCategory.MISC, AlphaItems.ALPHA_IRON_INGOT, 0.7F, 200)
            .unlockedBy("has_iron_ore", has(AlphaBlocks.ALPHA_IRON_ORE))
            .save(exporter, "alpha_iron_ingot_from_smelting");

        ShapedRecipeBuilder.shaped(registryLookup.lookupOrThrow(net.minecraft.core.registries.Registries.ITEM), RecipeCategory.DECORATIONS, Items.TORCH, 4)
            .pattern("X")
            .pattern("#")
            .define('X', AlphaItems.ALPHA_COAL)
            .define('#', AlphaItems.ALPHA_STICK)
            .unlockedBy("has_material", has(AlphaItems.ALPHA_COAL))
            .save(exporter, "vanilla_torch_from_alpha_coal");

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(AlphaBlocks.ALPHA_GOLD_ORE), RecipeCategory.MISC, CookingBookCategory.MISC, AlphaItems.ALPHA_GOLD_INGOT, 1.0F, 200)
            .unlockedBy("has_gold_ore", has(AlphaBlocks.ALPHA_GOLD_ORE))
            .save(exporter, "alpha_gold_ingot_from_smelting");

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(AlphaBlocks.ALPHA_DIAMOND_ORE), RecipeCategory.MISC, CookingBookCategory.MISC, AlphaItems.ALPHA_DIAMOND, 1.0F, 200)
            .unlockedBy("has_diamond_ore", has(AlphaBlocks.ALPHA_DIAMOND_ORE))
            .save(exporter, "alpha_diamond_from_smelting");



        SimpleCookingRecipeBuilder.smelting(Ingredient.of(AlphaBlocks.ALPHA_COAL_ORE), RecipeCategory.MISC, CookingBookCategory.MISC, AlphaItems.ALPHA_COAL, 0.1F, 200)
            .unlockedBy("has_coal_ore", has(AlphaBlocks.ALPHA_COAL_ORE))
            .save(exporter, "alpha_coal_from_smelting");

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(AlphaBlocks.ALPHA_COBBLESTONE), RecipeCategory.BUILDING_BLOCKS, CookingBookCategory.BLOCKS, AlphaBlocks.ALPHA_STONE, 0.1F, 200)
            .unlockedBy("has_cobblestone", has(AlphaBlocks.ALPHA_COBBLESTONE))
            .save(exporter, "alpha_stone_from_smelting");

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(AlphaItems.ALPHA_PORKCHOP), RecipeCategory.FOOD, CookingBookCategory.FOOD, AlphaItems.ALPHA_COOKED_PORKCHOP, 0.35F, 200)
            .unlockedBy("has_porkchop", has(AlphaItems.ALPHA_PORKCHOP))
            .save(exporter, "alpha_cooked_porkchop_from_smelting");

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(AlphaItems.ALPHA_CLAY_BALL), RecipeCategory.MISC, CookingBookCategory.MISC, AlphaItems.ALPHA_BRICK, 0.3F, 200)
            .unlockedBy("has_clay_ball", has(AlphaItems.ALPHA_CLAY_BALL))
            .save(exporter, "alpha_brick_from_smelting");

            }
        };
    }
}
