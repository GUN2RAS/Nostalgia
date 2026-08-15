package net.nostalgia.item;

import java.util.function.Function;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;

public class ModItems {
  public static final Item DIMENSION_ALPHA = registerItem("dimension_alpha", Item::new);
  public static final Item DIMENSION_RD = registerItem("dimension_rd", Item::new);
  public static final Item ALPHA_CLOCK = registerItem("alpha_clock", p -> new Item(p.stacksTo(1)));
  public static final Item CHARGED_AMETHYST = registerItem("charged_amethyst", ChargedAmethystItem::new);

  public ModItems() {
  }

  public static Item registerItem(String name, Function<Properties, Item> factory) {
    Identifier identifier = Identifier.fromNamespaceAndPath("nostalgia", name);
    ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, identifier);
    Item item = factory.apply(new Properties().setId(key));
    return (Item)Registry.register(BuiltInRegistries.ITEM, key, item);
  }

  public static void registerModItems() {
  }
}
