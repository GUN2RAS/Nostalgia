package net.nostalgia.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.nostalgia.world.dimension.ModDimensions;

public class NostalgiaDataGenerator implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        
        NostalgiaBlockTagProvider blockTagProvider = pack.addProvider(NostalgiaBlockTagProvider::new);
        pack.addProvider(NostalgiaItemTagProvider::new);

        pack.addProvider(NostalgiaRecipeProvider::new);
        pack.addProvider(NostalgiaModelProvider::new);
        pack.addProvider(NostalgiaLanguageProvider::new);
        pack.addProvider(NostalgiaRuLanguageProvider::new);
    }

    @Override
    public void buildRegistry(RegistrySetBuilder registryBuilder) {

    }
}
