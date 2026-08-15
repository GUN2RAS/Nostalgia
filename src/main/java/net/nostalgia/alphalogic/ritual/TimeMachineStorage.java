package net.nostalgia.alphalogic.ritual;

import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;

public interface TimeMachineStorage {
  ItemStack nostalgia$getShard();

  void nostalgia$setShard(ItemStack var1);

  int nostalgia$getEnergy();

  void nostalgia$setEnergy(int var1);

  ContainerData nostalgia$getEnergyData();
}
