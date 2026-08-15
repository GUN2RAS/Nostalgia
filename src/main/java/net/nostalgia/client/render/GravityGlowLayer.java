package net.nostalgia.client.render;

import com.example.api.Gravity;
import com.example.api.GravityChanger;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.player.Player;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class GravityGlowLayer extends RenderLayer<AvatarRenderState, PlayerModel> {
  public GravityGlowLayer(RenderLayerParent<AvatarRenderState, PlayerModel> renderer) {
    super(renderer);
  }

  public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, AvatarRenderState state, float yRot, float xRot) {
    Minecraft mc = Minecraft.getInstance();
    if (mc.level != null) {
      if (mc.level.getEntity(state.id) instanceof Player player) {
        if (player instanceof GravityChanger changer && changer.isInfected()) {
          Gravity gravity = changer.getInfectedGravity();
          float pulse = 0.5F + 0.3F * (float)Math.sin((float)(System.currentTimeMillis() % 100000L) * 0.005F);

          int color = switch (gravity) {
            case UP -> ARGB.color((int)(pulse * 180.0F), 0, 255, 255);
            case DOWN -> ARGB.color((int)(pulse * 180.0F), 210, 0, 255);
            case WEST -> ARGB.color((int)(pulse * 180.0F), 0, 255, 42);
            case EAST -> ARGB.color((int)(pulse * 180.0F), 255, 170, 0);
            case NORTH -> ARGB.color((int)(pulse * 180.0F), 255, 0, 85);
            case SOUTH -> ARGB.color((int)(pulse * 180.0F), 0, 85, 255);
            default -> throw new MatchException(null, null);
          };
          poseStack.pushPose();
          poseStack.scale(1.04F, 1.04F, 1.04F);
          Matrix4f modelView = poseStack.last().pose();
          Vector3f viewPos = new Vector3f();
          modelView.getTranslation(viewPos);
          Vector3f pushDirView = viewPos.lengthSquared() < 1.0E-4F ? new Vector3f(0.0F, 0.0F, -1.0F) : new Vector3f(viewPos).normalize();
          Matrix4f inv = new Matrix4f(modelView).invert();
          Vector3f pushDirModel = new Vector3f(pushDirView);
          inv.transformDirection(pushDirModel);
          poseStack.translate(pushDirModel.x() * 0.04F, pushDirModel.y() * 0.04F, pushDirModel.z() * 0.04F);
          submitNodeCollector.order(-1)
            .submitModel(
              this.getParentModel(),
              state,
              poseStack,
              RenderTypes.entityTranslucent(state.skin.body().texturePath(), false),
              lightCoords,
              OverlayTexture.NO_OVERLAY,
              color,
              null,
              0,
              null
            );
          poseStack.popPose();
        }
      }
    }
  }
}
