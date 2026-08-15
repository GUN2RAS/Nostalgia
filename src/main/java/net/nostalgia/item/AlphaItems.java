package net.nostalgia.item;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BoatItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.CompassItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DoubleHighBlockItem;
import net.minecraft.world.item.EggItem;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MinecartItem;
import net.minecraft.world.item.SnowballItem;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.CreativeModeTab.Row;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.equipment.ArmorMaterials;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.nostalgia.block.AlphaBlocks;
import net.nostalgia.entity.AlphaEntities;

public class AlphaItems {
  public static final List<Item> TAB_ITEMS = new ArrayList<>();
  public static final Item ALPHA_WOODEN_SWORD = registerItem("alpha_wooden_sword", new Properties().sword(ToolMaterial.WOOD, 1.0F, -2.8F));
  public static final Item ALPHA_WOODEN_AXE = registerItem("alpha_wooden_axe", new Properties().axe(ToolMaterial.WOOD, 1.0F, -2.8F));
  public static final Item ALPHA_STONE_SWORD = registerItem("alpha_stone_sword", new Properties().sword(ToolMaterial.STONE, 1.0F, -2.8F));
  public static final Item ALPHA_STONE_AXE = registerItem("alpha_stone_axe", new Properties().axe(ToolMaterial.STONE, 1.0F, -2.8F));
  public static final Item ALPHA_IRON_SWORD = registerItem("alpha_iron_sword", new Properties().sword(ToolMaterial.IRON, 1.0F, -2.8F));
  public static final Item ALPHA_IRON_AXE = registerItem("alpha_iron_axe", new Properties().axe(ToolMaterial.IRON, 1.0F, -2.8F));
  public static final Item ALPHA_DIAMOND_SWORD = registerItem("alpha_diamond_sword", new Properties().sword(ToolMaterial.DIAMOND, 1.0F, -2.8F));
  public static final Item ALPHA_DIAMOND_AXE = registerItem("alpha_diamond_axe", new Properties().axe(ToolMaterial.DIAMOND, 1.0F, -2.8F));
  public static final Item ALPHA_GOLDEN_SWORD = registerItem("alpha_golden_sword", new Properties().sword(ToolMaterial.GOLD, 1.0F, -2.8F));
  public static final Item ALPHA_GOLDEN_AXE = registerItem("alpha_golden_axe", new Properties().axe(ToolMaterial.GOLD, 1.0F, -2.8F));
  public static final Item ALPHA_LEATHER_HELMET = registerItem("alpha_leather_helmet", new Properties().humanoidArmor(ArmorMaterials.LEATHER, ArmorType.HELMET));
  public static final Item ALPHA_LEATHER_CHESTPLATE = registerItem(
    "alpha_leather_chestplate", new Properties().humanoidArmor(ArmorMaterials.LEATHER, ArmorType.CHESTPLATE)
  );
  public static final Item ALPHA_LEATHER_LEGGINGS = registerItem(
    "alpha_leather_leggings", new Properties().humanoidArmor(ArmorMaterials.LEATHER, ArmorType.LEGGINGS)
  );
  public static final Item ALPHA_LEATHER_BOOTS = registerItem("alpha_leather_boots", new Properties().humanoidArmor(ArmorMaterials.LEATHER, ArmorType.BOOTS));
  public static final Item ALPHA_CHAINMAIL_HELMET = registerItem(
    "alpha_chainmail_helmet", new Properties().humanoidArmor(ArmorMaterials.CHAINMAIL, ArmorType.HELMET)
  );
  public static final Item ALPHA_CHAINMAIL_CHESTPLATE = registerItem(
    "alpha_chainmail_chestplate", new Properties().humanoidArmor(ArmorMaterials.CHAINMAIL, ArmorType.CHESTPLATE)
  );
  public static final Item ALPHA_CHAINMAIL_LEGGINGS = registerItem(
    "alpha_chainmail_leggings", new Properties().humanoidArmor(ArmorMaterials.CHAINMAIL, ArmorType.LEGGINGS)
  );
  public static final Item ALPHA_CHAINMAIL_BOOTS = registerItem(
    "alpha_chainmail_boots", new Properties().humanoidArmor(ArmorMaterials.CHAINMAIL, ArmorType.BOOTS)
  );
  public static final Item ALPHA_IRON_HELMET = registerItem("alpha_iron_helmet", new Properties().humanoidArmor(ArmorMaterials.IRON, ArmorType.HELMET));
  public static final Item ALPHA_IRON_CHESTPLATE = registerItem(
    "alpha_iron_chestplate", new Properties().humanoidArmor(ArmorMaterials.IRON, ArmorType.CHESTPLATE)
  );
  public static final Item ALPHA_IRON_LEGGINGS = registerItem("alpha_iron_leggings", new Properties().humanoidArmor(ArmorMaterials.IRON, ArmorType.LEGGINGS));
  public static final Item ALPHA_IRON_BOOTS = registerItem("alpha_iron_boots", new Properties().humanoidArmor(ArmorMaterials.IRON, ArmorType.BOOTS));
  public static final Item ALPHA_DIAMOND_HELMET = registerItem("alpha_diamond_helmet", new Properties().humanoidArmor(ArmorMaterials.DIAMOND, ArmorType.HELMET));
  public static final Item ALPHA_DIAMOND_CHESTPLATE = registerItem(
    "alpha_diamond_chestplate", new Properties().humanoidArmor(ArmorMaterials.DIAMOND, ArmorType.CHESTPLATE)
  );
  public static final Item ALPHA_DIAMOND_LEGGINGS = registerItem(
    "alpha_diamond_leggings", new Properties().humanoidArmor(ArmorMaterials.DIAMOND, ArmorType.LEGGINGS)
  );
  public static final Item ALPHA_DIAMOND_BOOTS = registerItem("alpha_diamond_boots", new Properties().humanoidArmor(ArmorMaterials.DIAMOND, ArmorType.BOOTS));
  public static final Item ALPHA_GOLDEN_HELMET = registerItem("alpha_golden_helmet", new Properties().humanoidArmor(ArmorMaterials.GOLD, ArmorType.HELMET));
  public static final Item ALPHA_GOLDEN_CHESTPLATE = registerItem(
    "alpha_golden_chestplate", new Properties().humanoidArmor(ArmorMaterials.GOLD, ArmorType.CHESTPLATE)
  );
  public static final Item ALPHA_GOLDEN_LEGGINGS = registerItem(
    "alpha_golden_leggings", new Properties().humanoidArmor(ArmorMaterials.GOLD, ArmorType.LEGGINGS)
  );
  public static final Item ALPHA_GOLDEN_BOOTS = registerItem("alpha_golden_boots", new Properties().humanoidArmor(ArmorMaterials.GOLD, ArmorType.BOOTS));
  public static final Item ALPHA_APPLE = registerItem("alpha_apple", new Properties().food(Foods.APPLE));
  public static final Item ALPHA_GOLDEN_APPLE = registerItem("alpha_golden_apple", new Properties().food(Foods.GOLDEN_APPLE));
  public static final Item ALPHA_PORKCHOP = registerItem("alpha_porkchop", new Properties().food(Foods.PORKCHOP));
  public static final Item ALPHA_COOKED_PORKCHOP = registerItem("alpha_cooked_porkchop", new Properties().food(Foods.COOKED_PORKCHOP));
  public static final Item ALPHA_BREAD = registerItem("alpha_bread", new Properties().food(Foods.BREAD));
  public static final Item ALPHA_MUSHROOM_STEW = registerItem("alpha_mushroom_stew", new Properties().food(Foods.MUSHROOM_STEW));
  public static final Item ALPHA_STICK = registerItem("alpha_stick", new Properties());
  public static final Item ALPHA_BOWL = registerItem("alpha_bowl", new Properties());
  public static final Item ALPHA_STRING = registerItem("alpha_string", new Properties());
  public static final Item ALPHA_FEATHER = registerItem("alpha_feather", new Properties());
  public static final Item ALPHA_GUNPOWDER = registerItem("alpha_gunpowder", new Properties());
  public static final Item ALPHA_WHEAT_SEEDS = registerItem(
    "alpha_wheat_seeds", p -> new BlockItem(AlphaBlocks.ALPHA_WHEAT_CROP, p.useItemDescriptionPrefix()), new Properties()
  );
  public static final Item ALPHA_WHEAT = registerItem("alpha_wheat", new Properties());
  public static final Item ALPHA_FLINT = registerItem("alpha_flint", new Properties());
  public static final Item ALPHA_PAINTING = registerItem("alpha_painting", AlphaPaintingItem::new, new Properties());
  public static final Item ALPHA_SIGN = registerItem("alpha_sign", new Properties());
  public static final Item ALPHA_WOODEN_DOOR = registerItem(
    "alpha_wooden_door", p -> new DoubleHighBlockItem(AlphaBlocks.ALPHA_WOODEN_DOOR, p), new Properties()
  );
  public static final Item ALPHA_IRON_DOOR = registerItem("alpha_iron_door", p -> new DoubleHighBlockItem(AlphaBlocks.ALPHA_IRON_DOOR, p), new Properties());
  public static final Item ALPHA_BUCKET = registerItem("alpha_bucket", p -> new BucketItem(Fluids.EMPTY, p), new Properties().stacksTo(16));
  public static final Item ALPHA_WATER_BUCKET = registerItem("alpha_water_bucket", p -> new BucketItem(Fluids.WATER, p), new Properties().stacksTo(1));
  public static final Item ALPHA_LAVA_BUCKET = registerItem("alpha_lava_bucket", p -> new BucketItem(Fluids.LAVA, p), new Properties().stacksTo(1));
  public static final Item ALPHA_MILK_BUCKET = registerItem("alpha_milk_bucket", Item::new, new Properties().stacksTo(1));
  public static final Item ALPHA_MINECART = registerItem("alpha_minecart", p -> new MinecartItem(EntityType.MINECART, p), new Properties().stacksTo(1));
  public static final Item ALPHA_CHEST_MINECART = registerItem(
    "alpha_chest_minecart", p -> new MinecartItem(EntityType.CHEST_MINECART, p), new Properties().stacksTo(1)
  );
  public static final Item ALPHA_FURNACE_MINECART = registerItem(
    "alpha_furnace_minecart", p -> new MinecartItem(EntityType.FURNACE_MINECART, p), new Properties().stacksTo(1)
  );
  public static final Item ALPHA_REDSTONE = registerItem("alpha_redstone", p -> new BlockItem(Blocks.REDSTONE_WIRE, p), new Properties());
  public static final Item ALPHA_SNOWBALL = registerItem("alpha_snowball", SnowballItem::new, new Properties().stacksTo(16));
  public static final Item ALPHA_BOAT = registerItem("alpha_boat", p -> new BoatItem(AlphaEntities.ALPHA_BOAT, p), new Properties().stacksTo(1));
  public static final Item ALPHA_LEATHER = registerItem("alpha_leather", new Properties());
  public static final Item ALPHA_BRICK = registerItem("alpha_brick", new Properties());
  public static final Item ALPHA_CLAY_BALL = registerItem("alpha_clay_ball", new Properties());
  public static final Item ALPHA_PAPER = registerItem("alpha_paper", new Properties());
  public static final Item ALPHA_BOOK = registerItem("alpha_book", new Properties());
  public static final Item ALPHA_SLIMEBALL = registerItem("alpha_slimeball", new Properties());
  public static final Item ALPHA_EGG = registerItem("alpha_egg", EggItem::new, new Properties().stacksTo(16));
  public static final Item ALPHA_COMPASS = registerItem("alpha_compass", CompassItem::new, new Properties());
  public static final Item ALPHA_FLINT_AND_STEEL = registerItem("alpha_flint_and_steel", FlintAndSteelItem::new, new Properties().durability(64));
  public static final Item ALPHA_BOW = registerItem("alpha_bow", BowItem::new, new Properties().durability(384));
  public static final Item ALPHA_ARROW = registerItem("alpha_arrow", ArrowItem::new, new Properties());
  public static final Item ALPHA_DIAMOND = registerItem("alpha_diamond", new Properties());
  public static final Item ALPHA_IRON_INGOT = registerItem("alpha_iron_ingot", new Properties());
  public static final Item ALPHA_GOLD_INGOT = registerItem("alpha_gold_ingot", new Properties());
  public static final Item ALPHA_COAL = registerItem("alpha_coal", new Properties());
  public static final ResourceKey<CreativeModeTab> ALPHA_ITEMS_TAB_KEY = ResourceKey.create(
    Registries.CREATIVE_MODE_TAB, Identifier.fromNamespaceAndPath("nostalgia", "alpha_items")
  );
  public static final CreativeModeTab ALPHA_ITEMS_TAB = (CreativeModeTab)Registry.register(
    BuiltInRegistries.CREATIVE_MODE_TAB,
    ALPHA_ITEMS_TAB_KEY,
    CreativeModeTab.builder(Row.TOP, 1)
      .icon(() -> new ItemStack(ALPHA_DIAMOND_SWORD))
      .title(Component.literal("Alpha Items"))
      .displayItems((parameters, output) -> {
        for (Item item : TAB_ITEMS) {
          output.accept(item);
        }
      })
      .build()
  );

  public AlphaItems() {
  }

  public static <T extends Item> T registerItem(String name, Function<Properties, T> factory, Properties properties) {
    ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("nostalgia", name));
    properties.setId(key);
    T item = (T)factory.apply(properties);
    T registeredItem = (T)Registry.register(BuiltInRegistries.ITEM, key, item);
    TAB_ITEMS.add(registeredItem);
    return registeredItem;
  }

  public static Item registerItem(String name, Properties properties) {
    return registerItem(name, Item::new, properties);
  }

  public static void register() {
  }
}
