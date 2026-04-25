package net.nostalgia.mixin.alpha;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public class AlphaMinecraftMixin {
    
    @Inject(method = "useAmbientOcclusion()Z", at = @At("HEAD"), cancellable = true)
    private static void disableAmbientOcclusionInAlpha(CallbackInfoReturnable<Boolean> cir) {
        Minecraft client = Minecraft.getInstance();
        if (client.level != null && client.level.dimension().equals(net.nostalgia.world.dimension.ModDimensions.ALPHA_112_01_LEVEL_KEY)) {
            cir.setReturnValue(false);
        }
    }
}
