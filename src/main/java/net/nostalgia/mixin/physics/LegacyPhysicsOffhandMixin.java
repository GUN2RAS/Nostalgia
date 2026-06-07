package net.nostalgia.mixin.physics;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionHand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.nostalgia.world.dimension.ModDimensions;

@Mixin(Player.class)
public class LegacyPhysicsOffhandMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    private void clearOffhand(CallbackInfo ci) {
        Player player = (Player)(Object)this;
        if (player.level().dimension().equals(ModDimensions.ALPHA_112_01_LEVEL_KEY)) {
            ItemStack offhand = player.getItemInHand(InteractionHand.OFF_HAND);
            if (!offhand.isEmpty()) {
                
                if (player.level() instanceof net.minecraft.server.level.ServerLevel) {
                    player.drop(offhand.copy(), true);
                }
                offhand.setCount(0);
            }
        }
    }
}
