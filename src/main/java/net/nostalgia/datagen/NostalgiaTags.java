package net.nostalgia.datagen;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class NostalgiaTags {
  public static final TagKey<Item> STICKS = item("sticks");
  public static final TagKey<Item> IRON_INGOTS = item("iron_ingots");
  public static final TagKey<Item> GOLD_INGOTS = item("gold_ingots");
  public static final TagKey<Item> DIAMONDS = item("diamonds");
  public static final TagKey<Item> COBBLESTONES = item("cobblestones");
  public static final TagKey<Item> OAK_PLANKS = item("oak_planks");
  public static final TagKey<Item> OAK_LOGS = item("oak_logs");
  public static final TagKey<Item> STRINGS = item("strings");
  public static final TagKey<Item> COAL = item("coal");
  public static final TagKey<Item> LEATHER = item("leather");
  public static final TagKey<Item> REDSTONE = item("redstone");
  public static final TagKey<Item> FLINT = item("flint");
  public static final TagKey<Item> FEATHERS = item("feathers");
  public static final TagKey<Item> GUNPOWDER = item("gunpowder");
  public static final TagKey<Item> WHEAT = item("wheat");
  public static final TagKey<Item> PAPER = item("paper");
  public static final TagKey<Item> BOOKS = item("books");
  public static final TagKey<Item> BRICKS = item("bricks");
  public static final TagKey<Item> CLAY_BALLS = item("clay_balls");
  public static final TagKey<Item> SNOWBALLS = item("snowballs");
  public static final TagKey<Item> APPLES = item("apples");
  public static final TagKey<Item> SAND = item("sand");

  public NostalgiaTags() {
  }

  private static TagKey<Item> item(String name) {
    return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("nostalgia", name));
  }
}
