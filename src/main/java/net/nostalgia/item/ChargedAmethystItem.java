package net.nostalgia.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

public class ChargedAmethystItem extends Item {
    public ChargedAmethystItem(Properties properties) {
        super(properties.stacksTo(64));
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }

    @Override
    public Component getName(ItemStack stack) {
        String direction = getDirection(stack);
        if (!"none".equals(direction)) {
            return Component.translatable(this.getDescriptionId() + "." + direction);
        }
        return Component.translatable(this.getDescriptionId());
    }

    public static String getDirection(ItemStack stack) {
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        if (customData != null) {
            CompoundTag nbt = customData.copyTag();
            if (nbt.contains("direction")) {
                return nbt.getString("direction").orElse("none").toLowerCase();
            }
        }
        return "none";
    }

    public static void setDirection(ItemStack stack, String direction) {
        CompoundTag nbt = new CompoundTag();
        nbt.putString("direction", direction.toLowerCase());
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(nbt));
    }
}
