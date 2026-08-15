package net.nostalgia.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.LinkedList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.nostalgia.alphalogic.ritual.TimestopZoneManager;
import net.nostalgia.client.events.echo.TrailManager;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class TimestopGlowLayer extends RenderLayer<AvatarRenderState, PlayerModel> {
  public TimestopGlowLayer(RenderLayerParent<AvatarRenderState, PlayerModel> renderer) {
    super(renderer);
  }

  public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, AvatarRenderState state, float yRot, float xRot) {
    Minecraft mc = Minecraft.getInstance();
    if (mc.level != null) {
      Entity entity = mc.level.getEntity(state.id);
      if (entity instanceof Player) {
        TimestopZoneManager.ActiveZone zone = TimestopZoneManager.findZoneContaining(entity.level().dimension(), entity.blockPosition());
        if (zone != null) {
          int bx = zone.beaconPos().getX() >> 4;
          int bz = zone.beaconPos().getZ() >> 4;
          double minX = (bx - zone.radiusChunks()) * 16.0;
          double maxX = (bx + zone.radiusChunks() + 1) * 16.0;
          double minZ = (bz - zone.radiusChunks()) * 16.0;
          double maxZ = (bz + zone.radiusChunks() + 1) * 16.0;
          double dxMin = entity.getX() - minX;
          double dxMax = maxX - entity.getX();
          double dzMin = entity.getZ() - minZ;
          double dzMax = maxZ - entity.getZ();
          double dist = Math.min(Math.min(dxMin, dxMax), Math.min(dzMin, dzMax));
          float alpha = (float)Mth.clamp(dist / 2.0, 0.0, 1.0);
          if (alpha <= 0.01F) {
            return;
          }

          poseStack.pushPose();
          poseStack.scale(1.05F, 1.05F, 1.05F);
          Matrix4f modelView = poseStack.last().pose();
          Vector3f viewPos = new Vector3f();
          modelView.getTranslation(viewPos);
          Vector3f pushDirView;
          if (viewPos.lengthSquared() < 1.0E-4F) {
            pushDirView = new Vector3f(0.0F, 0.0F, -1.0F);
          } else {
            pushDirView = new Vector3f(viewPos).normalize();
          }

          Matrix4f inv = new Matrix4f(modelView).invert();
          Vector3f pushDirModel = new Vector3f(pushDirView);
          inv.transformDirection(pushDirModel);
          poseStack.translate(pushDirModel.x() * 0.05F, pushDirModel.y() * 0.05F, pushDirModel.z() * 0.05F);
          int alphaInt = (int)(alpha * 128.0F);
          int color = ARGB.color(alphaInt, 170, 0, 255);
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
          LinkedList<TrailManager.TrailSnapshot> trail = TrailManager.TRAILS.get(entity.getUUID());
          if (trail != null && !trail.isEmpty()) {
            for (TrailManager.TrailSnapshot ghost : trail) {
              poseStack.pushPose();
              double dx = ghost.pos.x - state.x;
              double dy = ghost.pos.y - state.y;
              double dz = ghost.pos.z - state.z;
              Vector3f deltaWorld = new Vector3f((float)dx, (float)dy, (float)dz);
              float ghostDist = deltaWorld.length();
              if (ghostDist > 0.2F) {
                Vector3f deltaModel = new Vector3f(deltaWorld);
                inv.transformDirection(deltaModel);
                poseStack.translate(
                  deltaModel.x() + pushDirModel.x() * 0.05F, deltaModel.y() + pushDirModel.y() * 0.05F, deltaModel.z() + pushDirModel.z() * 0.05F
                );
                float separationAlpha = Mth.clamp((ghostDist - 0.2F) / 0.5F, 0.0F, 1.0F);
                int ghostAlphaInt = (int)(ghost.alpha * alpha * separationAlpha * 255.0F);
                if (ghostAlphaInt > 0) {
                  int ghostColor = ARGB.color(ghostAlphaInt, 170, 0, 255);
                  submitNodeCollector.order(-1)
                    .submitModel(
                      this.getParentModel(),
                      state,
                      poseStack,
                      RenderTypes.entityTranslucent(state.skin.body().texturePath(), false),
                      lightCoords,
                      OverlayTexture.NO_OVERLAY,
                      ghostColor,
                      null,
                      0,
                      null
                    );
                }
              }

              poseStack.popPose();
            }
          }
        }
      }
    }
  }
}
