package net.nostalgia.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack.Pose;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.nostalgia.alphalogic.ritual.SkyPortalEventInstance;
import net.nostalgia.alphalogic.ritual.SkyPortalManager;
import net.nostalgia.entity.SkyPortalBeamEntity;

public class SkyPortalBeamRenderer extends EntityRenderer<SkyPortalBeamEntity, SkyPortalBeamRenderer.BeamRenderState> {
  public static final Identifier BEAM_LOCATION = Identifier.withDefaultNamespace("textures/entity/beacon/beacon_beam.png");

  public SkyPortalBeamRenderer(Context context) {
    super(context);
  }

  public SkyPortalBeamRenderer.BeamRenderState createRenderState() {
    return new SkyPortalBeamRenderer.BeamRenderState();
  }

  public void extractRenderState(SkyPortalBeamEntity entity, SkyPortalBeamRenderer.BeamRenderState state, float partialTick) {
    super.extractRenderState(entity, state, partialTick);
    state.gameTime = entity.level().getGameTime();
    state.partialTick = partialTick;
    int color = 11141290;
    SkyPortalEventInstance active = SkyPortalManager.findNearest(entity.blockPosition(), entity.level().dimension().identifier().toString());
    if (active != null) {
      String target = active.targetDimension();
      if (target != null) {
        if (target.contains("alpha")) {
          color = 54998;
        } else if (target.contains("rd")) {
          color = 13395711;
        } else if (target.contains("overworld")) {
          color = 8978176;
        }
      }
    }

    state.color = color;
  }

  public void submit(SkyPortalBeamRenderer.BeamRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
  }

  private static void renderPart(
    Pose pose,
    VertexConsumer builder,
    int color,
    int beamStart,
    int beamEnd,
    float wnx,
    float wnz,
    float enx,
    float enz,
    float wsx,
    float wsz,
    float esx,
    float esz,
    float uu1,
    float uu2,
    float vv1,
    float vv2
  ) {
    renderQuad(pose, builder, color, beamStart, beamEnd, wnx, wnz, enx, enz, uu1, uu2, vv1, vv2);
    renderQuad(pose, builder, color, beamStart, beamEnd, esx, esz, wsx, wsz, uu1, uu2, vv1, vv2);
    renderQuad(pose, builder, color, beamStart, beamEnd, enx, enz, esx, esz, uu1, uu2, vv1, vv2);
    renderQuad(pose, builder, color, beamStart, beamEnd, wsx, wsz, wnx, wnz, uu1, uu2, vv1, vv2);
  }

  private static void renderQuad(
    Pose pose,
    VertexConsumer builder,
    int color,
    int beamStart,
    int beamEnd,
    float wnx,
    float wnz,
    float enx,
    float enz,
    float uu1,
    float uu2,
    float vv1,
    float vv2
  ) {
    addVertex(pose, builder, color, beamEnd, wnx, wnz, uu2, vv1);
    addVertex(pose, builder, color, beamStart, wnx, wnz, uu2, vv2);
    addVertex(pose, builder, color, beamStart, enx, enz, uu1, vv2);
    addVertex(pose, builder, color, beamEnd, enx, enz, uu1, vv1);
  }

  private static void addVertex(Pose pose, VertexConsumer builder, int color, int y, float x, float z, float u, float v) {
    builder.addVertex(pose, x, y, z).setColor(color).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(15728880).setNormal(pose, 0.0F, 1.0F, 0.0F);
  }

  public static class BeamRenderState extends EntityRenderState {
    public long gameTime;
    public float partialTick;
    public int color;

    public BeamRenderState() {
    }
  }
}
