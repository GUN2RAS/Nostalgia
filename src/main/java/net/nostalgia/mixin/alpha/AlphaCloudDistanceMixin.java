package net.nostalgia.mixin.alpha;

import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.nostalgia.world.dimension.ModDimensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(OptionInstance.class)
public abstract class AlphaCloudDistanceMixin {
    @Inject(method = "get", at = @At("HEAD"), cancellable = true)
    private void hijackCloudRange(CallbackInfoReturnable<Object> cir) {
        Minecraft mc = Minecraft.getInstance();
        if (mc != null && mc.level != null && mc.options != null && mc.level.dimension() == ModDimensions.ALPHA_112_01_LEVEL_KEY) {
            
            if ((Object) this == mc.options.cloudRange()) {
                
                cir.setReturnValue(mc.options.renderDistance().get());
            }
        }
    }
}
