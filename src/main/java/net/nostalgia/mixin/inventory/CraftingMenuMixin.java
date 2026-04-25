package net.nostalgia.mixin.inventory;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingMenu;
import net.nostalgia.block.AlphaBlocks;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CraftingMenu.class)
public abstract class CraftingMenuMixin extends net.minecraft.world.inventory.AbstractContainerMenu {

    @Shadow @Final private ContainerLevelAccess access;

    protected CraftingMenuMixin(net.minecraft.world.inventory.MenuType<?> menuType, int i) {
        super(menuType, i);
    }

    @Inject(method = "stillValid", at = @At("HEAD"), cancellable = true)
    private void onStillValid(Player player, CallbackInfoReturnable<Boolean> cir) {
        if (stillValid(this.access, player, AlphaBlocks.ALPHA_CRAFTING_TABLE)) {
            cir.setReturnValue(true);
        }
    }
}
