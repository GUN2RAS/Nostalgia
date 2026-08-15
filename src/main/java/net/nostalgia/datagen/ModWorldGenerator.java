package net.nostalgia.datagen;

import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider.Entries;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.Registries;

public class ModWorldGenerator extends FabricDynamicRegistryProvider {
  public ModWorldGenerator(FabricPackOutput output, CompletableFuture<Provider> registriesFuture) {
    super(output, registriesFuture);
  }

  protected void configure(Provider registries, Entries entries) {
    entries.addAll(registries.lookupOrThrow(Registries.DIMENSION_TYPE));
    entries.addAll(registries.lookupOrThrow(Registries.LEVEL_STEM));
  }

  public String getName() {
    return "Nostalgia World Gen";
  }
}
