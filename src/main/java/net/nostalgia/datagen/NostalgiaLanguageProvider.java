package net.nostalgia.datagen;

import java.lang.reflect.Field;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider.TranslationBuilder;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.nostalgia.block.AlphaBlocks;
import net.nostalgia.item.AlphaItems;

public class NostalgiaLanguageProvider extends FabricLanguageProvider {
  protected NostalgiaLanguageProvider(FabricPackOutput dataOutput, CompletableFuture<Provider> registryLookup) {
    super(dataOutput, "en_us", registryLookup);
  }

  public void generateTranslations(Provider registryLookup, TranslationBuilder translationBuilder) {
    for (Field field : AlphaItems.class.getDeclaredFields()) {
      try {
        if (Item.class.isAssignableFrom(field.getType())) {
          Item item = (Item)field.get(null);
          String name = BuiltInRegistries.ITEM.getKey(item).getPath();
          if (!name.equals("air")) {
            String formattedName = "Alpha " + this.formatName(name.replace("alpha_", ""));
            translationBuilder.add(item, formattedName);
          }
        }
      } catch (Exception ignored) {
      }
    }

    for (Field fieldx : AlphaBlocks.class.getDeclaredFields()) {
      try {
        if (Block.class.isAssignableFrom(fieldx.getType())) {
          Block block = (Block)fieldx.get(null);
          String name = BuiltInRegistries.BLOCK.getKey(block).getPath();
          if (!name.equals("air")) {
            String formattedName = name.startsWith("alpha_") ? "Alpha " + this.formatName(name.replace("alpha_", "")) : this.formatName(name);
            translationBuilder.add(block, formattedName);
            translationBuilder.add("item.nostalgia." + name, formattedName);
          }
        }
      } catch (Exception ignored) {
      }
    }

    translationBuilder.add("itemGroup.nostalgia.alpha_tab", "Alpha Blocks");
    translationBuilder.add("itemGroup.nostalgia.alpha_items", "Alpha Items");
    translationBuilder.add("gui.nostalgia.time_machine.launch", "START");
    translationBuilder.add("gui.nostalgia.time_machine.launch_tooltip", "INITIALIZE");
    translationBuilder.add("gui.nostalgia.time_machine.charge", "CHARGE");
    translationBuilder.add("gui.nostalgia.time_machine.scanning", "SCANNING");
    translationBuilder.add("gui.nostalgia.time_machine.scan_cooldown", "RELOAD");
    translationBuilder.add("gui.nostalgia.warning.title", "ALPHA PROTOCOL");
    translationBuilder.add("gui.nostalgia.warning.text", "THIS MOD CONTAINS FLASHING LIGHTS! PEOPLE WITH PHOTOSENSITIVE EPILEPSY SHOULD NOT PLAY!");
    translationBuilder.add("gui.nostalgia.warning.continue", "CONTINUE");
    translationBuilder.add("item.nostalgia.charged_amethyst", "Charged Amethyst");
    translationBuilder.add("item.nostalgia.charged_amethyst.up", "\u00a7bCharged Amethyst \u16cf");
    translationBuilder.add("item.nostalgia.charged_amethyst.down", "\u00a75Charged Amethyst \u16e6");
    translationBuilder.add("item.nostalgia.charged_amethyst.left", "\u00a7aCharged Amethyst \u16b2");
    translationBuilder.add("item.nostalgia.charged_amethyst.right", "\u00a76Charged Amethyst \u16a6");
  }

  private String formatName(String input) {
    String[] words = input.split("_");
    StringBuilder sb = new StringBuilder();

    for (String word : words) {
      if (word.length() > 0) {
        sb.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1)).append(" ");
      }
    }

    return sb.toString().trim();
  }
}
