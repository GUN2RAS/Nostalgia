package net.nostalgia.mixin.client.punchy;

import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import punchy.client.state.TridentStateMachine;

@Mixin(value = TridentStateMachine.class, remap = false)
public class TridentStateMachineMixin {

    @ModifyVariable(method = "tick", at = @At(value = "STORE", ordinal = 0), name = "isTrident")
    private boolean nostalgia$amethystIsTrident(boolean isTrident, Minecraft client) {
        if (client != null && client.player != null) {
            ItemStack usingStack = client.player.getUseItem();
            if (usingStack != null && usingStack.is(Items.AMETHYST_SHARD)) {
                return true;
            }
        }
        return isTrident;
    }
}
