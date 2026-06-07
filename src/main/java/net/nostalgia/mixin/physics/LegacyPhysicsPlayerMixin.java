package net.nostalgia.mixin.physics;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FoodData.class)
public class LegacyPhysicsPlayerMixin {

    @Inject(method = "tick(Lnet/minecraft/server/level/ServerPlayer;)V", at = @At("HEAD"), cancellable = true)
    private void disableHungerTicking(net.minecraft.server.level.ServerPlayer player, CallbackInfo ci) {
        if (net.nostalgia.world.rules.NostalgiaRules.getForLevel(player.level()).disableHunger) {
            
            FoodData food = (FoodData) (Object) this;
            food.setFoodLevel(20);
            food.setSaturation(5.0F); 

            ci.cancel();
        }
    }
}
