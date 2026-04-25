package net.nostalgia.item;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.nostalgia.NostalgiaMod;

public class ModItems {

    public static Item registerItem(String name, java.util.function.Function<Item.Properties, Item> factory) {
        Identifier identifier = Identifier.fromNamespaceAndPath(NostalgiaMod.MOD_ID, name);
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, identifier);
        Item item = factory.apply(new Item.Properties().setId(key));
        Item registered = Registry.register(BuiltInRegistries.ITEM, key, item);

        return registered;
    }

    public static final Item DIMENSION_ALPHA = registerItem("dimension_alpha", Item::new);
    public static final Item DIMENSION_RD = registerItem("dimension_rd", Item::new);
    
    public static final Item ALPHA_CLOCK = registerItem("alpha_clock", p -> new Item(p.stacksTo(1)));

    public static void registerModItems() {
    }
}
