package net.nostalgia.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.util.ARGB;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import com.example.api.GravityChanger;
import com.example.api.Gravity;

public class GravityGlowLayer extends RenderLayer<AvatarRenderState, PlayerModel> {

    public GravityGlowLayer(RenderLayerParent<AvatarRenderState, PlayerModel> renderer) {
        super(renderer);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, AvatarRenderState state, float yRot, float xRot) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;
        
        Entity entity = mc.level.getEntity(state.id);
        if (!(entity instanceof Player player)) return;

        if (player instanceof GravityChanger changer && changer.isInfected()) {
            Gravity gravity = changer.getInfectedGravity();
            float pulse = 0.5f + 0.3f * (float) Math.sin((System.currentTimeMillis() % 100000L) * 0.005f);
            
            int color = switch (gravity) {
                case UP -> ARGB.color((int) (pulse * 180.0f), 0, 255, 255);
                case DOWN -> ARGB.color((int) (pulse * 180.0f), 210, 0, 255);
                case WEST -> ARGB.color((int) (pulse * 180.0f), 0, 255, 42);
                case EAST -> ARGB.color((int) (pulse * 180.0f), 255, 170, 0);
                case NORTH -> ARGB.color((int) (pulse * 180.0f), 255, 0, 85);
                case SOUTH -> ARGB.color((int) (pulse * 180.0f), 0, 85, 255);
            };

            poseStack.pushPose();
            poseStack.scale(1.04f, 1.04f, 1.04f);

            Matrix4f modelView = poseStack.last().pose();
            Vector3f viewPos = new Vector3f();
            modelView.getTranslation(viewPos);
            
            Vector3f pushDirView = viewPos.lengthSquared() < 0.0001f ? new Vector3f(0, 0, -1) : new Vector3f(viewPos).normalize();
            Matrix4f inv = new Matrix4f(modelView).invert();
            Vector3f pushDirModel = new Vector3f(pushDirView);
            inv.transformDirection(pushDirModel);
            
            poseStack.translate(pushDirModel.x() * 0.04f, pushDirModel.y() * 0.04f, pushDirModel.z() * 0.04f);

            submitNodeCollector.order(-1).submitModel(
                    this.getParentModel(),
                    state,
                    poseStack,
                    RenderTypes.entityTranslucent(state.skin.body().texturePath(), false),
                    lightCoords,
                    net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY,
                    color,
                    null,
                    0,
                    null
            );

            poseStack.popPose();
        }
    }
}
