package net.nostalgia.datagen;

import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.nostalgia.block.AlphaBlocks;

public class NostalgiaBlockTagProvider extends FabricTagsProvider.BlockTagsProvider {

    public NostalgiaBlockTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        builder(BlockTags.MINEABLE_WITH_PICKAXE)
            .add(AlphaBlocks.ALPHA_STONE_KEY)
            .add(AlphaBlocks.ALPHA_COBBLESTONE_KEY)
            .add(AlphaBlocks.ALPHA_MOSSY_COBBLESTONE_KEY)
            .add(AlphaBlocks.ALPHA_COAL_ORE_KEY)
            .add(AlphaBlocks.ALPHA_IRON_ORE_KEY)
            .add(AlphaBlocks.ALPHA_GOLD_ORE_KEY)

            .add(AlphaBlocks.ALPHA_REDSTONE_ORE_KEY)
            .add(AlphaBlocks.ALPHA_DIAMOND_ORE_KEY)
            .add(AlphaBlocks.ALPHA_OBSIDIAN_KEY)
            .add(AlphaBlocks.ALPHA_FURNACE_KEY);

        builder(BlockTags.MINEABLE_WITH_AXE)
            .add(AlphaBlocks.ALPHA_OAK_LOG_KEY)
            .add(AlphaBlocks.ALPHA_OAK_PLANKS_KEY)
            .add(AlphaBlocks.ALPHA_BOOKSHELF_KEY)
            .add(AlphaBlocks.ALPHA_CHEST_KEY)
            .add(AlphaBlocks.ALPHA_CRAFTING_TABLE_KEY);

        builder(BlockTags.MINEABLE_WITH_SHOVEL)
            .add(AlphaBlocks.ALPHA_DIRT_KEY)
            .add(AlphaBlocks.ALPHA_GRASS_BLOCK_KEY)
            .add(AlphaBlocks.ALPHA_FARMLAND_KEY)
            .add(AlphaBlocks.ALPHA_SAND_KEY)
            .add(AlphaBlocks.ALPHA_GRAVEL_KEY)
            .add(AlphaBlocks.ALPHA_CLAY_KEY)
            .add(AlphaBlocks.ALPHA_SNOW_BLOCK_KEY);

        
        builder(BlockTags.NEEDS_STONE_TOOL)
            .add(AlphaBlocks.ALPHA_IRON_ORE_KEY);

        builder(BlockTags.NEEDS_IRON_TOOL)
            .add(AlphaBlocks.ALPHA_REDSTONE_ORE_KEY)
            .add(AlphaBlocks.ALPHA_DIAMOND_ORE_KEY);

        builder(BlockTags.NEEDS_DIAMOND_TOOL)
            .add(AlphaBlocks.ALPHA_OBSIDIAN_KEY);
            
        
        builder(BlockTags.CLIMBABLE)
            .add(AlphaBlocks.ALPHA_LADDER_KEY);
            
        builder(BlockTags.LOGS)
            .add(AlphaBlocks.ALPHA_OAK_LOG_KEY);
            
        builder(BlockTags.PLANKS)
            .add(AlphaBlocks.ALPHA_OAK_PLANKS_KEY);

        builder(BlockTags.LEAVES)
            .add(AlphaBlocks.ALPHA_LEAVES_KEY);

        builder(BlockTags.SAND)
            .add(AlphaBlocks.ALPHA_SAND_KEY);

        builder(BlockTags.SUPPORTS_CROPS)
            .add(AlphaBlocks.ALPHA_FARMLAND_KEY);
    }
}
