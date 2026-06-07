package net.nostalgia.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.ThrownItemRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.nostalgia.entity.ThrownAmethystEntity;

public class ThrownAmethystRenderer extends EntityRenderer<ThrownAmethystEntity, ThrownAmethystRenderer.AmethystRenderState> {
    private final ItemModelResolver itemModelResolver;

    public ThrownAmethystRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemModelResolver = context.getItemModelResolver();
    }

    @Override
    public AmethystRenderState createRenderState() {
        return new AmethystRenderState();
    }

    @Override
    public void extractRenderState(ThrownAmethystEntity entity, AmethystRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        this.itemModelResolver.updateForNonLiving(state.item, entity.getItem(), ItemDisplayContext.GROUND, entity);
        state.xRot = entity.getXRot(partialTicks);
        state.yRot = entity.getYRot(partialTicks);
    }

    @Override
    public void submit(AmethystRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.pushPose();
        
        // Вращаем аметист как стрелу (по направлению полета)
        poseStack.mulPose(Axis.YP.rotationDegrees(state.yRot - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(state.xRot));
        
        // Немного сдвинем, чтобы центр вращения был правильным, и масштабируем если надо
        poseStack.translate(0.0F, -0.1F, 0.0F);

        state.item.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
        
        poseStack.popPose();
        super.submit(state, poseStack, submitNodeCollector, camera);
    }

    public static class AmethystRenderState extends ThrownItemRenderState {
        public float xRot;
        public float yRot;
    }
}
