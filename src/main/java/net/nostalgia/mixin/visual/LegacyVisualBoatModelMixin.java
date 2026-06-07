package net.nostalgia.mixin.visual;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.object.boat.AbstractBoatModel;
import net.minecraft.client.renderer.entity.state.BoatRenderState;
import net.nostalgia.duck.LegacyBoatRenderStateDuck;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractBoatModel.class)
public abstract class LegacyVisualBoatModelMixin {

    @Shadow @Final private ModelPart leftPaddle;
    @Shadow @Final private ModelPart rightPaddle;

    @Inject(method = "setupAnim", at = @At("TAIL"))
    private void nostalgia$hidePaddles(BoatRenderState state, CallbackInfo ci) {
        if (((LegacyBoatRenderStateDuck) state).nostalgia$isLegacy()) {
            this.leftPaddle.visible = false;
            this.rightPaddle.visible = false;
        } else {
            this.leftPaddle.visible = true;
            this.rightPaddle.visible = true;
        }
    }
}
