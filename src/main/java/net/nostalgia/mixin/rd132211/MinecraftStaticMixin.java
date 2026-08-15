package net.nostalgia.mixin.rd132211;

import net.minecraft.client.Minecraft;
import net.nostalgia.world.dimension.ModDimensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public class MinecraftStaticMixin {

    @Inject(method = "useAmbientOcclusion", at = @At("HEAD"), cancellable = true)
    private static void onUseAmbientOcclusion(CallbackInfoReturnable<Boolean> cir) {
        Minecraft client = Minecraft.getInstance();
        if (client.player != null && client.player.level().dimension().equals(ModDimensions.RD_132211_LEVEL_KEY)) {
            cir.setReturnValue(false); 
        }
    }
}
