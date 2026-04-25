package net.nostalgia.mixin.physics;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FoodProperties.class)
public class LegacyPhysicsFoodPropertiesMixin {
    
    @Inject(method = "onConsume(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/component/Consumable;)V", at = @At("HEAD"), cancellable = true)
    private void alphaHeal(Level level, LivingEntity entity, ItemStack itemStack, Consumable consumable, CallbackInfo ci) {
        if (entity instanceof Player player && level.dimension().equals(net.nostalgia.world.dimension.ModDimensions.ALPHA_112_01_LEVEL_KEY)) {
            FoodProperties food = (FoodProperties) (Object) this;

            player.heal((float) food.nutrition());

            ci.cancel();
        }
    }
}
