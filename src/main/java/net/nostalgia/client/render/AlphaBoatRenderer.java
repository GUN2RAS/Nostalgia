package net.nostalgia.client.render;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.BoatRenderState;
import net.minecraft.resources.Identifier;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import com.mojang.math.Axis;
import net.nostalgia.client.render.model.AlphaBoatModel;
import net.nostalgia.entity.AlphaBoatEntity;

public class AlphaBoatRenderer extends EntityRenderer<AlphaBoatEntity, BoatRenderState> {
    private static final Identifier BOAT_TEXTURE = Identifier.fromNamespaceAndPath("nostalgia", "textures/entity/boat/alpha_boat.png");
    private final AlphaBoatModel model;

    public AlphaBoatRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new AlphaBoatModel(AlphaBoatModel.createBodyLayer().bakeRoot());
        this.shadowRadius = 0.5F;
    }

    @Override
    public void submit(BoatRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.translate(0.0F, 0.375F, 0.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - state.yRot));
        
        float damageTime = state.damageTime;
        if (damageTime > 0.0F) {
            poseStack.mulPose(Axis.XP.rotationDegrees(Mth.sin(damageTime) * damageTime * state.hurtTime / 10.0F * (float)state.hurtDir));
        }

        if (!state.isUnderWater && !Mth.equal(state.bubbleAngle, 0.0F)) {
            poseStack.mulPose(Axis.ZP.rotationDegrees(state.bubbleAngle));
        }

        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));

        this.model.setupAnim(state);
        submitNodeCollector.submitModel(this.model, state, poseStack, BOAT_TEXTURE, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null);

        poseStack.popPose();
        super.submit(state, poseStack, submitNodeCollector, camera);
    }

    @Override
    public BoatRenderState createRenderState() {
        return new BoatRenderState();
    }

    @Override
    public void extractRenderState(AlphaBoatEntity entity, BoatRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.yRot = entity.getViewYRot(partialTick);
        state.hurtTime = entity.getHurtTime() - partialTick;
        state.damageTime = Math.max(entity.getDamage() - partialTick, 0.0F);
        state.hurtDir = entity.getHurtDir();
        state.bubbleAngle = entity.getBubbleAngle(partialTick);
        state.isUnderWater = entity.isUnderWater();
    }
}
