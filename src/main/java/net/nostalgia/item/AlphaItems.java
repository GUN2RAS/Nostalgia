package net.nostalgia.item;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.*;
import net.minecraft.world.item.equipment.ArmorMaterials;
import net.minecraft.world.item.equipment.ArmorType;
import net.nostalgia.NostalgiaMod;

import net.minecraft.world.item.CreativeModeTab;

public class AlphaItems {
    public static final java.util.List<Item> TAB_ITEMS = new java.util.ArrayList<>();

    public static <T extends Item> T registerItem(String name, java.util.function.Function<Item.Properties, T> factory, Item.Properties properties) {
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(NostalgiaMod.MOD_ID, name));
        properties.setId(key);
        T item = factory.apply(properties);
        T registeredItem = Registry.register(BuiltInRegistries.ITEM, key, item);
        TAB_ITEMS.add(registeredItem);
        return registeredItem;
    }

    public static Item registerItem(String name, Item.Properties properties) {
        return registerItem(name, Item::new, properties);
    }

        public static final Item ALPHA_WOODEN_SWORD = registerItem("alpha_wooden_sword", new Item.Properties().sword(ToolMaterial.WOOD, 1.0F, -2.8F));
        public static final Item ALPHA_WOODEN_AXE = registerItem("alpha_wooden_axe", new Item.Properties().axe(ToolMaterial.WOOD, 1.0F, -2.8F));
        public static final Item ALPHA_STONE_SWORD = registerItem("alpha_stone_sword", new Item.Properties().sword(ToolMaterial.STONE, 1.0F, -2.8F));
        public static final Item ALPHA_STONE_AXE = registerItem("alpha_stone_axe", new Item.Properties().axe(ToolMaterial.STONE, 1.0F, -2.8F));
        public static final Item ALPHA_IRON_SWORD = registerItem("alpha_iron_sword", new Item.Properties().sword(ToolMaterial.IRON, 1.0F, -2.8F));
        public static final Item ALPHA_IRON_AXE = registerItem("alpha_iron_axe", new Item.Properties().axe(ToolMaterial.IRON, 1.0F, -2.8F));
        public static final Item ALPHA_DIAMOND_SWORD = registerItem("alpha_diamond_sword", new Item.Properties().sword(ToolMaterial.DIAMOND, 1.0F, -2.8F));
        public static final Item ALPHA_DIAMOND_AXE = registerItem("alpha_diamond_axe", new Item.Properties().axe(ToolMaterial.DIAMOND, 1.0F, -2.8F));
        public static final Item ALPHA_GOLDEN_SWORD = registerItem("alpha_golden_sword", new Item.Properties().sword(ToolMaterial.GOLD, 1.0F, -2.8F));
        public static final Item ALPHA_GOLDEN_AXE = registerItem("alpha_golden_axe", new Item.Properties().axe(ToolMaterial.GOLD, 1.0F, -2.8F));
        public static final Item ALPHA_LEATHER_HELMET = registerItem("alpha_leather_helmet", new Item.Properties().humanoidArmor(ArmorMaterials.LEATHER, ArmorType.HELMET));
        public static final Item ALPHA_LEATHER_CHESTPLATE = registerItem("alpha_leather_chestplate", new Item.Properties().humanoidArmor(ArmorMaterials.LEATHER, ArmorType.CHESTPLATE));
        public static final Item ALPHA_LEATHER_LEGGINGS = registerItem("alpha_leather_leggings", new Item.Properties().humanoidArmor(ArmorMaterials.LEATHER, ArmorType.LEGGINGS));
        public static final Item ALPHA_LEATHER_BOOTS = registerItem("alpha_leather_boots", new Item.Properties().humanoidArmor(ArmorMaterials.LEATHER, ArmorType.BOOTS));
        public static final Item ALPHA_CHAINMAIL_HELMET = registerItem("alpha_chainmail_helmet", new Item.Properties().humanoidArmor(ArmorMaterials.CHAINMAIL, ArmorType.HELMET));
        public static final Item ALPHA_CHAINMAIL_CHESTPLATE = registerItem("alpha_chainmail_chestplate", new Item.Properties().humanoidArmor(ArmorMaterials.CHAINMAIL, ArmorType.CHESTPLATE));
        public static final Item ALPHA_CHAINMAIL_LEGGINGS = registerItem("alpha_chainmail_leggings", new Item.Properties().humanoidArmor(ArmorMaterials.CHAINMAIL, ArmorType.LEGGINGS));
        public static final Item ALPHA_CHAINMAIL_BOOTS = registerItem("alpha_chainmail_boots", new Item.Properties().humanoidArmor(ArmorMaterials.CHAINMAIL, ArmorType.BOOTS));
        public static final Item ALPHA_IRON_HELMET = registerItem("alpha_iron_helmet", new Item.Properties().humanoidArmor(ArmorMaterials.IRON, ArmorType.HELMET));
        public static final Item ALPHA_IRON_CHESTPLATE = registerItem("alpha_iron_chestplate", new Item.Properties().humanoidArmor(ArmorMaterials.IRON, ArmorType.CHESTPLATE));
        public static final Item ALPHA_IRON_LEGGINGS = registerItem("alpha_iron_leggings", new Item.Properties().humanoidArmor(ArmorMaterials.IRON, ArmorType.LEGGINGS));
        public static final Item ALPHA_IRON_BOOTS = registerItem("alpha_iron_boots", new Item.Properties().humanoidArmor(ArmorMaterials.IRON, ArmorType.BOOTS));
        public static final Item ALPHA_DIAMOND_HELMET = registerItem("alpha_diamond_helmet", new Item.Properties().humanoidArmor(ArmorMaterials.DIAMOND, ArmorType.HELMET));
        public static final Item ALPHA_DIAMOND_CHESTPLATE = registerItem("alpha_diamond_chestplate", new Item.Properties().humanoidArmor(ArmorMaterials.DIAMOND, ArmorType.CHESTPLATE));
        public static final Item ALPHA_DIAMOND_LEGGINGS = registerItem("alpha_diamond_leggings", new Item.Properties().humanoidArmor(ArmorMaterials.DIAMOND, ArmorType.LEGGINGS));
        public static final Item ALPHA_DIAMOND_BOOTS = registerItem("alpha_diamond_boots", new Item.Properties().humanoidArmor(ArmorMaterials.DIAMOND, ArmorType.BOOTS));
        public static final Item ALPHA_GOLDEN_HELMET = registerItem("alpha_golden_helmet", new Item.Properties().humanoidArmor(ArmorMaterials.GOLD, ArmorType.HELMET));
        public static final Item ALPHA_GOLDEN_CHESTPLATE = registerItem("alpha_golden_chestplate", new Item.Properties().humanoidArmor(ArmorMaterials.GOLD, ArmorType.CHESTPLATE));
        public static final Item ALPHA_GOLDEN_LEGGINGS = registerItem("alpha_golden_leggings", new Item.Properties().humanoidArmor(ArmorMaterials.GOLD, ArmorType.LEGGINGS));
        public static final Item ALPHA_GOLDEN_BOOTS = registerItem("alpha_golden_boots", new Item.Properties().humanoidArmor(ArmorMaterials.GOLD, ArmorType.BOOTS));
        public static final Item ALPHA_APPLE = registerItem("alpha_apple", new Item.Properties().food(Foods.APPLE));
        public static final Item ALPHA_GOLDEN_APPLE = registerItem("alpha_golden_apple", new Item.Properties().food(Foods.GOLDEN_APPLE));
        public static final Item ALPHA_PORKCHOP = registerItem("alpha_porkchop", new Item.Properties().food(Foods.PORKCHOP));
        public static final Item ALPHA_COOKED_PORKCHOP = registerItem("alpha_cooked_porkchop", new Item.Properties().food(Foods.COOKED_PORKCHOP));
        public static final Item ALPHA_BREAD = registerItem("alpha_bread", new Item.Properties().food(Foods.BREAD));
        public static final Item ALPHA_MUSHROOM_STEW = registerItem("alpha_mushroom_stew", new Item.Properties().food(Foods.MUSHROOM_STEW));
        public static final Item ALPHA_STICK = registerItem("alpha_stick", new Item.Properties());
        public static final Item ALPHA_BOWL = registerItem("alpha_bowl", new Item.Properties());
        public static final Item ALPHA_STRING = registerItem("alpha_string", new Item.Properties());
        public static final Item ALPHA_FEATHER = registerItem("alpha_feather", new Item.Properties());
        public static final Item ALPHA_GUNPOWDER = registerItem("alpha_gunpowder", new Item.Properties());
        public static final Item ALPHA_WHEAT_SEEDS = registerItem("alpha_wheat_seeds", p -> new net.minecraft.world.item.BlockItem(net.nostalgia.block.AlphaBlocks.ALPHA_WHEAT_CROP, p.useItemDescriptionPrefix()), new Item.Properties());
        public static final Item ALPHA_WHEAT = registerItem("alpha_wheat", new Item.Properties());
        public static final Item ALPHA_FLINT = registerItem("alpha_flint", new Item.Properties());
        public static final Item ALPHA_PAINTING = registerItem("alpha_painting", new Item.Properties());
        public static final Item ALPHA_SIGN = registerItem("alpha_sign", new Item.Properties());
        public static final Item ALPHA_WOODEN_DOOR = registerItem("alpha_wooden_door", p -> new net.minecraft.world.item.DoubleHighBlockItem(net.nostalgia.block.AlphaBlocks.ALPHA_WOODEN_DOOR, p), new Item.Properties());
        public static final Item ALPHA_IRON_DOOR = registerItem("alpha_iron_door", p -> new net.minecraft.world.item.DoubleHighBlockItem(net.nostalgia.block.AlphaBlocks.ALPHA_IRON_DOOR, p), new Item.Properties());
        public static final Item ALPHA_BUCKET = registerItem("alpha_bucket", p -> new net.minecraft.world.item.BucketItem(net.minecraft.world.level.material.Fluids.EMPTY, p), new Item.Properties().stacksTo(16));
        public static final Item ALPHA_WATER_BUCKET = registerItem("alpha_water_bucket", p -> new net.minecraft.world.item.BucketItem(net.minecraft.world.level.material.Fluids.WATER, p), new Item.Properties().stacksTo(1));
        public static final Item ALPHA_LAVA_BUCKET = registerItem("alpha_lava_bucket", p -> new net.minecraft.world.item.BucketItem(net.minecraft.world.level.material.Fluids.LAVA, p), new Item.Properties().stacksTo(1));
        public static final Item ALPHA_MILK_BUCKET = registerItem("alpha_milk_bucket", Item::new, new Item.Properties().stacksTo(1));
        public static final Item ALPHA_MINECART = registerItem("alpha_minecart", p -> new net.minecraft.world.item.MinecartItem(net.minecraft.world.entity.EntityType.MINECART, p), new Item.Properties().stacksTo(1));
        public static final Item ALPHA_CHEST_MINECART = registerItem("alpha_chest_minecart", p -> new net.minecraft.world.item.MinecartItem(net.minecraft.world.entity.EntityType.CHEST_MINECART, p), new Item.Properties().stacksTo(1));
        public static final Item ALPHA_FURNACE_MINECART = registerItem("alpha_furnace_minecart", p -> new net.minecraft.world.item.MinecartItem(net.minecraft.world.entity.EntityType.FURNACE_MINECART, p), new Item.Properties().stacksTo(1));
        public static final Item ALPHA_REDSTONE = registerItem("alpha_redstone", p -> new net.minecraft.world.item.BlockItem(net.minecraft.world.level.block.Blocks.REDSTONE_WIRE, p), new Item.Properties());
        public static final Item ALPHA_SNOWBALL = registerItem("alpha_snowball", net.minecraft.world.item.SnowballItem::new, new Item.Properties().stacksTo(16));
        public static final Item ALPHA_BOAT = registerItem("alpha_boat", p -> new net.minecraft.world.item.BoatItem(net.nostalgia.entity.AlphaEntities.ALPHA_BOAT, p), new Item.Properties().stacksTo(1));
        public static final Item ALPHA_LEATHER = registerItem("alpha_leather", new Item.Properties());
        public static final Item ALPHA_BRICK = registerItem("alpha_brick", new Item.Properties());
        public static final Item ALPHA_CLAY_BALL = registerItem("alpha_clay_ball", new Item.Properties());

        public static final Item ALPHA_PAPER = registerItem("alpha_paper", new Item.Properties());
        public static final Item ALPHA_BOOK = registerItem("alpha_book", new Item.Properties());
        public static final Item ALPHA_SLIMEBALL = registerItem("alpha_slimeball", new Item.Properties());
        public static final Item ALPHA_EGG = registerItem("alpha_egg", net.minecraft.world.item.EggItem::new, new Item.Properties().stacksTo(16));
        public static final Item ALPHA_COMPASS = registerItem("alpha_compass", net.minecraft.world.item.CompassItem::new, new Item.Properties());
        public static final Item ALPHA_FISHING_ROD = registerItem("alpha_fishing_rod", net.minecraft.world.item.FishingRodItem::new, new Item.Properties().durability(64));
        public static final Item ALPHA_FLINT_AND_STEEL = registerItem("alpha_flint_and_steel", net.minecraft.world.item.FlintAndSteelItem::new, new Item.Properties().durability(64));
        public static final Item ALPHA_BOW = registerItem("alpha_bow", net.minecraft.world.item.BowItem::new, new Item.Properties().durability(384));
        public static final Item ALPHA_ARROW = registerItem("alpha_arrow", net.minecraft.world.item.ArrowItem::new, new Item.Properties());
        public static final Item ALPHA_DIAMOND = registerItem("alpha_diamond", new Item.Properties());
        public static final Item ALPHA_IRON_INGOT = registerItem("alpha_iron_ingot", new Item.Properties());
        public static final Item ALPHA_GOLD_INGOT = registerItem("alpha_gold_ingot", new Item.Properties());
        public static final Item ALPHA_COAL = registerItem("alpha_coal", new Item.Properties());
        public static final net.minecraft.resources.ResourceKey<CreativeModeTab> ALPHA_ITEMS_TAB_KEY = net.minecraft.resources.ResourceKey
                        .create(net.minecraft.core.registries.Registries.CREATIVE_MODE_TAB,
                                        Identifier.fromNamespaceAndPath("nostalgia", "alpha_items"));

        public static final CreativeModeTab ALPHA_ITEMS_TAB = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
                        ALPHA_ITEMS_TAB_KEY,
                        net.minecraft.world.item.CreativeModeTab.builder(net.minecraft.world.item.CreativeModeTab.Row.TOP, 1)
                                        .icon(() -> new net.minecraft.world.item.ItemStack(ALPHA_DIAMOND_SWORD))
                                        .title(net.minecraft.network.chat.Component.literal("Alpha Items"))
                                        .displayItems((parameters, output) -> {

                                                for (Item item : TAB_ITEMS) {
                                                        output.accept(item);
                                                }
                                        })
                                        .build());

    public static void register() {
    }
}
