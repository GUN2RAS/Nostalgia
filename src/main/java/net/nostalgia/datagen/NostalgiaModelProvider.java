package net.nostalgia.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.world.level.block.Block;
import net.nostalgia.block.AlphaBlocks;
import net.nostalgia.item.AlphaItems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import com.google.gson.JsonParser;

public class NostalgiaModelProvider implements DataProvider {
    private final FabricPackOutput output;

    public NostalgiaModelProvider(FabricPackOutput output) {
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput writer) {
        List<CompletableFuture<?>> futures = new ArrayList<>();
        try {
            for (java.lang.reflect.Field field : AlphaItems.class.getDeclaredFields()) {
                if (net.minecraft.world.item.Item.class.isAssignableFrom(field.getType())) {
                    net.minecraft.world.item.Item item = (net.minecraft.world.item.Item) field.get(null);
                    String name = BuiltInRegistries.ITEM.getKey(item).getPath();
                    if(name.equals("air")) continue; 

                    if (name.equals("alpha_compass")) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("{\n  \"model\": {\n    \"type\": \"minecraft:range_dispatch\",\n    \"property\": \"minecraft:compass\",\n    \"scale\": 32.0,\n    \"target\": \"spawn\",\n    \"entries\": [\n");
                        sb.append("      { \"threshold\": 0.0, \"model\": { \"type\": \"minecraft:model\", \"model\": \"nostalgia:item/alpha_compass_16\" } },\n");
                        double[] thresholds = {0.5, 1.5, 2.5, 3.5, 4.5, 5.5, 6.5, 7.5, 8.5, 9.5, 10.5, 11.5, 12.5, 13.5, 14.5, 15.5, 16.5, 17.5, 18.5, 19.5, 20.5, 21.5, 22.5, 23.5, 24.5, 25.5, 26.5, 27.5, 28.5, 29.5, 30.5, 31.5};
                        int[] frames = {17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
                        for (int i = 0; i < 32; i++) {
                            sb.append("      { \"threshold\": ").append(thresholds[i]).append(", \"model\": { \"type\": \"minecraft:model\", \"model\": \"nostalgia:item/alpha_compass_").append(String.format("%02d", frames[i])).append("\" } }");
                            if (i < 31) sb.append(",\n"); else sb.append("\n");
                        }
                        sb.append("    ]\n  }\n}");
                        
                        try {
                            java.nio.file.Path srcDir = java.nio.file.Paths.get("E:/mods/nostalgia/Minecraft_Programmer_Art/assets/minecraft/textures/item");
                            java.nio.file.Path destDir = java.nio.file.Paths.get("E:/mods/nostalgia/src/main/resources/assets/nostalgia/textures/item");
                            if (java.nio.file.Files.exists(srcDir)) {
                                for (int i = 0; i < 32; i++) {
                                    String srcName = String.format("compass_%02d.png", i);
                                    String destName = String.format("alpha_compass_%02d.png", i);
                                    java.nio.file.Files.copy(srcDir.resolve(srcName), destDir.resolve(destName), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                                    
                                    String frameModel = "{\n  \"parent\": \"minecraft:item/generated\",\n  \"textures\": {\n    \"layer0\": \"nostalgia:item/alpha_compass_" + String.format("%02d", i) + "\"\n  }\n}";
                                    Path framePath = output.getOutputFolder().resolve("assets/nostalgia/models/item/alpha_compass_" + String.format("%02d", i) + ".json");
                                    futures.add(DataProvider.saveStable(writer, JsonParser.parseString(frameModel), framePath));
                                }
                            }
                        } catch(Exception e) { e.printStackTrace(); }
                        Path iPath = output.getOutputFolder().resolve("assets/nostalgia/items/alpha_compass.json");
                        futures.add(DataProvider.saveStable(writer, JsonParser.parseString(sb.toString()), iPath));
                        continue;
                    }

                    String modelJson = "{\n  \"parent\": \"minecraft:item/" + (isTool(name) ? "handheld" : "generated") + "\",\n  \"textures\": {\n    \"layer0\": \"nostalgia:item/" + name + "\"\n  }\n}";
                    Path modelPath = output.getOutputFolder().resolve("assets/nostalgia/models/item/" + name + ".json");
                    futures.add(DataProvider.saveStable(writer, JsonParser.parseString(modelJson), modelPath));

                    String itemDefJson = "{\n  \"model\": {\n    \"type\": \"minecraft:model\",\n    \"model\": \"nostalgia:item/" + name + "\"\n  }\n}";
                    Path itemPath = output.getOutputFolder().resolve("assets/nostalgia/items/" + name + ".json");
                    futures.add(DataProvider.saveStable(writer, JsonParser.parseString(itemDefJson), itemPath));
                }
            }

            
            String fireworkModelJson = "{\n  \"parent\": \"minecraft:item/generated\",\n  \"textures\": {\n    \"layer0\": \"nostalgia:item/ritual_firework\"\n  }\n}";
            Path fireworkModelPath = output.getOutputFolder().resolve("assets/nostalgia/models/item/ritual_firework.json");
            futures.add(DataProvider.saveStable(writer, JsonParser.parseString(fireworkModelJson), fireworkModelPath));

            String fireworkDefJson = "{\n  \"model\": {\n    \"type\": \"minecraft:model\",\n    \"model\": \"nostalgia:item/ritual_firework\"\n  }\n}";
            Path fireworkDefPath = output.getOutputFolder().resolve("assets/nostalgia/items/ritual_firework.json");
            futures.add(DataProvider.saveStable(writer, JsonParser.parseString(fireworkDefJson), fireworkDefPath));

            for (java.lang.reflect.Field field : AlphaBlocks.class.getDeclaredFields()) {
                if (Block.class.isAssignableFrom(field.getType())) {
                    Block block = (Block) field.get(null);
                    String name = BuiltInRegistries.BLOCK.getKey(block).getPath();
                    if(name.equals("air") || name.equals("alpha_redstone_wire") || name.equals("alpha_redstone_wall_torch")) continue;

                    String modelJson;
                    boolean is2DIcon = name.equals("alpha_lever") || name.equals("alpha_torch") || 
                                       name.equals("alpha_redstone_torch") || 
                                       name.equals("alpha_yellow_flower") || name.equals("alpha_red_flower") || 
                                       name.equals("alpha_brown_mushroom") || name.equals("alpha_red_mushroom") || 
                                       name.equals("alpha_cobweb") || name.equals("alpha_sapling") || 
                                       name.equals("alpha_ladder");
                    
                    if (name.equals("alpha_furnace")) {
                        modelJson = "{\n  \"parent\": \"minecraft:block/furnace\"\n}";
                    } else if (is2DIcon) {
                        String tex = name.equals("alpha_redstone_torch") ? "alpha_redstone_torch_on" : name;
                        modelJson = "{\n  \"parent\": \"minecraft:item/generated\",\n  \"textures\": {\n    \"layer0\": \"nostalgia:block/" + tex + "\"\n  }\n}";
                    } else if (name.equals("alpha_wooden_door") || name.equals("alpha_iron_door") || name.equals("alpha_sugar_cane") || name.equals("alpha_wheat")) {
                        modelJson = "{\n  \"parent\": \"minecraft:item/generated\",\n  \"textures\": {\n    \"layer0\": \"nostalgia:item/" + name + "\"\n  }\n}";
                    } else {
                        modelJson = "{\n  \"parent\": \"nostalgia:block/" + name + "\"\n}";
                    }

                    Path modelPath = output.getOutputFolder().resolve("assets/nostalgia/models/item/" + name + ".json");
                    futures.add(DataProvider.saveStable(writer, JsonParser.parseString(modelJson), modelPath));

                    String itemDefJson;
                    itemDefJson = "{\n  \"model\": {\n    \"type\": \"minecraft:model\",\n    \"model\": \"nostalgia:item/" + name + "\"\n  }\n}";
                    Path itemPath = output.getOutputFolder().resolve("assets/nostalgia/items/" + name + ".json");
                    futures.add(DataProvider.saveStable(writer, JsonParser.parseString(itemDefJson), itemPath));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }

    private boolean isTool(String name) {
        return name.contains("sword") || name.contains("shovel") || name.contains("pickaxe") || name.contains("axe") || name.contains("hoe") || name.contains("bow") || name.contains("fishing_rod") || name.contains("flint_and_steel");
    }

    @Override
    public String getName() {
        return "Nostalgia Custom Models";
    }
}
