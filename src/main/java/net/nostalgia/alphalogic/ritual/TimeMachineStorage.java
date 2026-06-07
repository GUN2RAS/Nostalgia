package net.nostalgia.alphalogic.ritual;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.ContainerData;

public interface TimeMachineStorage {
    ItemStack nostalgia$getShard();
    void nostalgia$setShard(ItemStack shard);
    int nostalgia$getEnergy();
    void nostalgia$setEnergy(int energy);
    ContainerData nostalgia$getEnergyData();
}

