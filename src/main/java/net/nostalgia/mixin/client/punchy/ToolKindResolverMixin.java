package net.nostalgia.mixin.client.punchy;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import punchy.client.render.ToolKindResolver;
import punchy.client.renderer.layer.PlayerArmModelLayer;
import punchy.client.renderer.layer.VanillaFirstPersonItemLayer;

@Mixin(value = ToolKindResolver.class, remap = false)
public class ToolKindResolverMixin {

    @Inject(method = "resolveItemKindWithoutOverrides", at = @At("HEAD"), cancellable = true)
    private static void nostalgia$amethystIsTrident(ItemStack stack, VanillaFirstPersonItemLayer.ToolKind fallback, CallbackInfoReturnable<VanillaFirstPersonItemLayer.ToolKind> cir) {
        if (stack != null && stack.is(Items.AMETHYST_SHARD)) {
            cir.setReturnValue(VanillaFirstPersonItemLayer.ToolKind.TRIDENT);
        }
    }

    @Inject(method = "resolveArmKind", at = @At("HEAD"), cancellable = true)
    private static void nostalgia$amethystIsTridentArm(ItemStack stack, PlayerArmModelLayer.ToolKind fallback, CallbackInfoReturnable<PlayerArmModelLayer.ToolKind> cir) {
        if (stack != null && stack.is(Items.AMETHYST_SHARD)) {
            cir.setReturnValue(PlayerArmModelLayer.ToolKind.TRIDENT);
        }
    }
}
