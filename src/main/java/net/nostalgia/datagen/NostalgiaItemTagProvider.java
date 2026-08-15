package net.nostalgia.datagen;

import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider.ItemTagsProvider;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;

public class NostalgiaItemTagProvider extends ItemTagsProvider {
  public NostalgiaItemTagProvider(FabricPackOutput output, CompletableFuture<Provider> registriesFuture) {
    super(output, registriesFuture);
  }

  protected void addTags(Provider provider) {
    this.builder(ItemTags.LOGS).add(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("nostalgia", "alpha_oak_log")));
    this.builder(ItemTags.PLANKS).add(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("nostalgia", "alpha_oak_planks")));
    this.builder(ItemTags.LEAVES).add(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("nostalgia", "alpha_leaves")));
    this.builder(ItemTags.SAND).add(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("nostalgia", "alpha_sand")));
    this.builder(NostalgiaTags.STICKS)
      .add(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("minecraft", "stick")))
      .add(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("nostalgia", "alpha_stick")));
    this.builder(NostalgiaTags.IRON_INGOTS)
      .add(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("minecraft", "iron_ingot")))
      .add(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("nostalgia", "alpha_iron_ingot")));
    this.builder(NostalgiaTags.GOLD_INGOTS)
      .add(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("minecraft", "gold_ingot")))
      .add(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("nostalgia", "alpha_gold_ingot")));
    this.builder(NostalgiaTags.DIAMONDS)
      .add(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("minecraft", "diamond")))
      .add(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("nostalgia", "alpha_diamond")));
    this.builder(NostalgiaTags.COBBLESTONES)
      .add(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("minecraft", "cobblestone")))
      .add(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("nostalgia", "alpha_cobblestone")));
    this.builder(NostalgiaTags.OAK_PLANKS)
      .add(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("minecraft", "oak_planks")))
      .add(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("nostalgia", "alpha_oak_planks")));
    this.builder(NostalgiaTags.OAK_LOGS)
      .add(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("minecraft", "oak_log")))
      .add(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("nostalgia", "alpha_oak_log")));
    this.builder(NostalgiaTags.STRINGS)
      .add(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("minecraft", "string")))
      .add(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("nostalgia", "alpha_string")));
    this.builder(NostalgiaTags.COAL)
      .add(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("minecraft", "coal")))
      .add(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("nostalgia", "alpha_coal")));
    this.builder(NostalgiaTags.LEATHER)
      .add(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("minecraft", "leather")))
      .add(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("nostalgia", "alpha_leather")));
    this.builder(NostalgiaTags.REDSTONE)
      .add(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("minecraft", "redstone")))
      .add(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("nostalgia", "alpha_redstone")));
    this.builder(NostalgiaTags.FLINT)
      .add(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("minecraft", "flint")))
      .add(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("nostalgia", "alpha_flint")));
    this.builder(NostalgiaTags.FEATHERS)
      .add(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("minecraft", "feather")))
      .add(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("nostalgia", "alpha_feather")));
    this.builder(NostalgiaTags.GUNPOWDER)
      .add(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("minecraft", "gunpowder")))
      .add(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("nostalgia", "alpha_gunpowder")));
    this.builder(NostalgiaTags.WHEAT)
      .add(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("minecraft", "wheat")))
      .add(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("nostalgia", "alpha_wheat")));
    this.builder(NostalgiaTags.PAPER)
      .add(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("minecraft", "paper")))
      .add(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("nostalgia", "alpha_paper")));
    this.builder(NostalgiaTags.BOOKS)
      .add(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("minecraft", "book")))
      .add(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("nostalgia", "alpha_book")));
    this.builder(NostalgiaTags.BRICKS)
      .add(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("minecraft", "brick")))
      .add(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("nostalgia", "alpha_brick")));
    this.builder(NostalgiaTags.CLAY_BALLS)
      .add(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("minecraft", "clay_ball")))
      .add(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("nostalgia", "alpha_clay_ball")));
    this.builder(NostalgiaTags.SNOWBALLS)
      .add(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("minecraft", "snowball")))
      .add(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("nostalgia", "alpha_snowball")));
    this.builder(NostalgiaTags.APPLES)
      .add(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("minecraft", "apple")))
      .add(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("nostalgia", "alpha_apple")));
    this.builder(NostalgiaTags.SAND)
      .add(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("minecraft", "sand")))
      .add(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("nostalgia", "alpha_sand")));
  }
}
