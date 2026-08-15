package net.nostalgia.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator.Pack;
import net.minecraft.core.RegistrySetBuilder;

public class NostalgiaDataGenerator implements DataGeneratorEntrypoint {
  public NostalgiaDataGenerator() {
  }

  public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
    Pack pack = fabricDataGenerator.createPack();
    NostalgiaBlockTagProvider blockTagProvider = (NostalgiaBlockTagProvider)pack.addProvider(NostalgiaBlockTagProvider::new);
    pack.addProvider(NostalgiaItemTagProvider::new);
    pack.addProvider(NostalgiaRecipeProvider::new);
    pack.addProvider(NostalgiaModelProvider::new);
    pack.addProvider(NostalgiaLanguageProvider::new);
    pack.addProvider(NostalgiaRuLanguageProvider::new);
  }

  public void buildRegistry(RegistrySetBuilder registryBuilder) {
  }
}
