package net.nostalgia.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.nostalgia.block.AlphaBlocks;
import net.nostalgia.item.AlphaItems;

import java.util.concurrent.CompletableFuture;

public class NostalgiaLanguageProvider extends FabricLanguageProvider {
    
    protected NostalgiaLanguageProvider(FabricPackOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, "en_us", registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.Provider registryLookup, TranslationBuilder translationBuilder) {
        
        try {
            for (java.lang.reflect.Field field : AlphaItems.class.getDeclaredFields()) {
                if (Item.class.isAssignableFrom(field.getType())) {
                    Item item = (Item) field.get(null);
                    String name = BuiltInRegistries.ITEM.getKey(item).getPath();
                    if(name.equals("air")) continue;
                    String formattedName = formatName(name.replace("alpha_", ""));
                    translationBuilder.add(item, formattedName);
                }
            }
        } catch (Exception e) {}

        
        try {
            for (java.lang.reflect.Field field : AlphaBlocks.class.getDeclaredFields()) {
                if (Block.class.isAssignableFrom(field.getType())) {
                    Block block = (Block) field.get(null);
                    String name = BuiltInRegistries.BLOCK.getKey(block).getPath();
                    if(name.equals("air")) continue;
                    String formattedName = formatName(name.replace("alpha_", ""));
                    translationBuilder.add(block, formattedName);
                    translationBuilder.add("item.nostalgia." + name, formattedName);
                }
            }
        } catch (Exception e) {}

        translationBuilder.add("itemGroup.nostalgia.alpha_tab", "Alpha Blocks");
        translationBuilder.add("itemGroup.nostalgia.alpha_items", "Alpha Items");

        translationBuilder.add("gui.nostalgia.time_machine.launch", "START");
        translationBuilder.add("gui.nostalgia.time_machine.launch_tooltip", "INITIALIZE");
        translationBuilder.add("gui.nostalgia.time_machine.charge", "CHARGE");

        translationBuilder.add("gui.nostalgia.warning.title", "ALPHA PROTOCOL");
        translationBuilder.add("gui.nostalgia.warning.text", "THIS MOD CONTAINS FLASHING LIGHTS! PEOPLE WITH PHOTOSENSITIVE EPILEPSY SHOULD NOT PLAY!");
        translationBuilder.add("gui.nostalgia.warning.continue", "CONTINUE");
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
