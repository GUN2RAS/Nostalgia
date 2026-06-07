package net.nostalgia.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.item.ItemEntity;
import net.nostalgia.world.dimension.ModDimensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemEntity.class)
public class ItemEntityAlphaSpinMixin {

    @Inject(method = "getSpin", at = @At("HEAD"), cancellable = true)
    private static void nostalgia$billboardSpin(float ageInTicks, float bobOffset, CallbackInfoReturnable<Float> cir) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null && mc.level.dimension() == ModDimensions.ALPHA_112_01_LEVEL_KEY) {
            float cameraYRot = mc.gameRenderer.getMainCamera().yRot();
            cir.setReturnValue((180.0F - cameraYRot) * 0.017453292F);
        }
    }
}
