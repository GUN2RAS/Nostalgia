package net.nostalgia.mixin.item;

import net.minecraft.world.level.block.entity.FuelValues;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Item;
import it.unimi.dsi.fastutil.objects.Object2IntSortedMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.nostalgia.item.AlphaItems;
import net.nostalgia.block.AlphaBlocks;

@Mixin(FuelValues.class)
public class FuelValuesMixin {

    @Shadow
    private Object2IntSortedMap<Item> values;

    @Inject(method = "vanillaBurnTimes(Lnet/minecraft/core/HolderLookup$Provider;Lnet/minecraft/world/flag/FeatureFlagSet;I)Lnet/minecraft/world/level/block/entity/FuelValues;", at = @At("RETURN"))
    private static void nostalgia$addAlphaFuels(HolderLookup.Provider provider, FeatureFlagSet featureFlagSet, int baseUnit, CallbackInfoReturnable<FuelValues> cir) {
        Object2IntSortedMap<Item> map = ((FuelValuesMixin)(Object) cir.getReturnValue()).values;

        map.put(AlphaItems.ALPHA_COAL, baseUnit * 8);
        map.put(AlphaBlocks.ALPHA_OAK_LOG.asItem(), baseUnit * 3 / 2);
        map.put(AlphaBlocks.ALPHA_OAK_PLANKS.asItem(), baseUnit * 3 / 2);
        map.put(AlphaItems.ALPHA_STICK, baseUnit / 2);
        map.put(AlphaBlocks.ALPHA_CHEST.asItem(), baseUnit * 3 / 2);
        map.put(AlphaBlocks.ALPHA_CRAFTING_TABLE.asItem(), baseUnit * 3 / 2);
        map.put(AlphaBlocks.ALPHA_BOOKSHELF.asItem(), baseUnit * 3 / 2);
        map.put(AlphaItems.ALPHA_LAVA_BUCKET, baseUnit * 100);
    }
}
