package net.nostalgia.mixin.alpha;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.CowRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.CowRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.nostalgia.world.dimension.ModDimensions;
import net.nostalgia.client.model.AlphaCowModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CowRenderer.class)
public abstract class AlphaCowRendererMixin extends MobRenderer<net.minecraft.world.entity.animal.cow.Cow, CowRenderState, net.minecraft.client.model.animal.cow.CowModel> {

  @Unique
  private AlphaCowModel nostalgia$alphaModel;

  public AlphaCowRendererMixin(net.minecraft.client.renderer.entity.EntityRendererProvider.Context context, net.minecraft.client.model.animal.cow.CowModel model, float shadowRadius) {
    super(context, model, shadowRadius);
  }

  @Inject(method = "<init>", at = @At("RETURN"))
  private void nostalgia$onInit(net.minecraft.client.renderer.entity.EntityRendererProvider.Context context, CallbackInfo ci) {
    this.nostalgia$alphaModel = new AlphaCowModel(AlphaCowModel.bakeModelPart());
  }

  @Inject(
    method = "submit(Lnet/minecraft/client/renderer/entity/state/CowRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
    at = @At(
      value = "INVOKE",
      target = "Lnet/minecraft/client/renderer/entity/MobRenderer;submit(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V"
    )
  )
  private void nostalgia$swapModelBeforeRender(CowRenderState state, PoseStack poseStack,
      SubmitNodeCollector collector, CameraRenderState camera, CallbackInfo ci) {
    if (Minecraft.getInstance().level != null && ModDimensions.ALPHA_112_01_LEVEL_KEY.equals(Minecraft.getInstance().level.dimension())) {
      this.model = this.nostalgia$alphaModel;
    }
  }
}
