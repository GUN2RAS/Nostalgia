package net.nostalgia.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.entity.state.ItemClusterRenderState;
import net.minecraft.client.renderer.entity.state.ItemEntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.AABB;
import net.nostalgia.world.dimension.ModDimensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntityRenderer.class)
public class ItemEntityRendererAlphaMixin {

    @Inject(method = "submitMultipleFromCount(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/ItemClusterRenderState;Lnet/minecraft/util/RandomSource;Lnet/minecraft/world/phys/AABB;)V", at = @At("HEAD"))
    private static void nostalgia$flattenAlphaDropsSubmit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, ItemClusterRenderState state, RandomSource random, AABB modelBoundingBox, CallbackInfo ci) {
        if (Minecraft.getInstance().level != null && Minecraft.getInstance().level.dimension() == ModDimensions.ALPHA_112_01_LEVEL_KEY) {
            float modelDepth = (float) modelBoundingBox.getZsize();
            if (modelDepth <= 0.1F) {
                poseStack.scale(1.0F, 1.0F, 0.000001F);
                state.count = 1;
            }
        }
    }

    @Inject(method = "renderMultipleFromCount(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/ItemClusterRenderState;Lnet/minecraft/util/RandomSource;)V", at = @At("HEAD"))
    private static void nostalgia$flattenAlphaDropsRender(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, ItemClusterRenderState state, RandomSource random, CallbackInfo ci) {
        if (Minecraft.getInstance().level != null && Minecraft.getInstance().level.dimension() == ModDimensions.ALPHA_112_01_LEVEL_KEY) {
            float modelDepth = (float) state.item.getModelBoundingBox().getZsize();
            if (modelDepth <= 0.1F) {
                poseStack.scale(1.0F, 1.0F, 0.000001F);
                state.count = 1;
            }
        }
    }

    @Redirect(
        method = "submit(Lnet/minecraft/client/renderer/entity/state/ItemEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/item/ItemEntity;getSpin(FF)F")
    )
    private float nostalgia$redirectSpin(float ageInTicks, float bobOffset, ItemEntityRenderState state) {
        float defaultSpin = ItemEntity.getSpin(ageInTicks, bobOffset);
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null && mc.level.dimension() == ModDimensions.ALPHA_112_01_LEVEL_KEY) {
            float modelDepth = (float) state.item.getModelBoundingBox().getZsize();
            if (modelDepth <= 0.1F) {
                float cameraYRot = mc.gameRenderer.getMainCamera().yRot();
                return (180.0F - cameraYRot) * 0.017453292F;
            }
        }
        return defaultSpin;
    }
}
