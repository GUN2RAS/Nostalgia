package net.nostalgia.datagen;

import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.nostalgia.block.AlphaBlocks;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.Registries;

public class NostalgiaItemTagProvider extends FabricTagsProvider.ItemTagsProvider {

    public NostalgiaItemTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        builder(ItemTags.LOGS)
            .add(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("nostalgia", "alpha_oak_log")));

        builder(ItemTags.PLANKS)
            .add(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("nostalgia", "alpha_oak_planks")));

        builder(ItemTags.LEAVES)
            .add(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("nostalgia", "alpha_leaves")));

        builder(ItemTags.SAND)
            .add(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("nostalgia", "alpha_sand")));
    }
}
