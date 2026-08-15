package net.nostalgia.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import net.minecraft.client.gui.Font.DisplayMode;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.OrderedSubmitNodeCollector;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.SubmitNodeCollector.CustomGeometryRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector.ParticleGroupRenderer;
import net.minecraft.client.renderer.block.MovingBlockRenderState;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.EntityRenderState.LeashState;
import net.minecraft.client.renderer.entity.state.EntityRenderState.ShadowPiece;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer.CrumblingOverlay;
import net.minecraft.client.renderer.item.ItemStackRenderState.FoilType;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.jspecify.annotations.Nullable;

public class GlowNodeCollector implements SubmitNodeCollector {
  private final SubmitNodeCollector parent;
  public final int overrideColor;

  public GlowNodeCollector(SubmitNodeCollector parent, float alpha) {
    this.parent = parent;
    int alphaInt = (int)(alpha * 255.0F);
    this.overrideColor = ARGB.color(alphaInt, 170, 0, 255);
  }

  public OrderedSubmitNodeCollector order(int order) {
    return new GlowNodeCollector.GlowOrderedCollector(this.parent.order(order), this.overrideColor);
  }

  public void submitShadow(PoseStack poseStack, float radius, List<ShadowPiece> pieces) {
    this.parent.submitShadow(poseStack, radius, pieces);
  }

  public void submitNameTag(
    PoseStack poseStack,
    @Nullable Vec3 nameTagAttachment,
    int offset,
    Component name,
    boolean seeThrough,
    int lightCoords,
    double distanceToCameraSq,
    CameraRenderState camera
  ) {
    this.parent.submitNameTag(poseStack, nameTagAttachment, offset, name, seeThrough, lightCoords, distanceToCameraSq, camera);
  }

  public void submitText(
    PoseStack poseStack,
    float x,
    float y,
    FormattedCharSequence string,
    boolean dropShadow,
    DisplayMode displayMode,
    int lightCoords,
    int color,
    int backgroundColor,
    int outlineColor
  ) {
    this.parent.submitText(poseStack, x, y, string, dropShadow, displayMode, lightCoords, color, backgroundColor, outlineColor);
  }

  public void submitFlame(PoseStack poseStack, EntityRenderState renderState, Quaternionf rotation) {
    this.parent.submitFlame(poseStack, renderState, rotation);
  }

  public void submitLeash(PoseStack poseStack, LeashState leashState) {
    this.parent.submitLeash(poseStack, leashState);
  }

  public <S> void submitModel(
    Model<? super S> model,
    S state,
    PoseStack poseStack,
    RenderType renderType,
    int lightCoords,
    int overlayCoords,
    int tintedColor,
    @Nullable TextureAtlasSprite sprite,
    int outlineColor,
    CrumblingOverlay crumblingOverlay
  ) {
    RenderType overriddenType = FPVTrailManager.isRenderingOutline ? renderType : renderType;
    this.parent.submitModel(model, state, poseStack, overriddenType, lightCoords, overlayCoords, this.overrideColor, sprite, outlineColor, crumblingOverlay);
  }

  public void submitModelPart(
    ModelPart modelPart,
    PoseStack poseStack,
    RenderType renderType,
    int lightCoords,
    int overlayCoords,
    @Nullable TextureAtlasSprite sprite,
    boolean sheeted,
    boolean hasFoil,
    int tintedColor,
    CrumblingOverlay crumblingOverlay,
    int outlineColor
  ) {
    this.parent
      .submitModelPart(
        modelPart, poseStack, renderType, lightCoords, overlayCoords, sprite, sheeted, hasFoil, this.overrideColor, crumblingOverlay, outlineColor
      );
  }

  public void submitMovingBlock(PoseStack poseStack, MovingBlockRenderState movingBlockRenderState) {
    this.parent.submitMovingBlock(poseStack, movingBlockRenderState);
  }

  public void submitBlockModel(
    PoseStack poseStack, RenderType renderType, List<BlockStateModelPart> parts, int[] tintLayers, int lightCoords, int overlayCoords, int outlineColor
  ) {
    this.parent.submitBlockModel(poseStack, renderType, parts, tintLayers, lightCoords, overlayCoords, outlineColor);
  }

  public void submitBreakingBlockModel(PoseStack poseStack, BlockStateModel model, long seed, int progress) {
    this.parent.submitBreakingBlockModel(poseStack, model, seed, progress);
  }

  public void submitItem(
    PoseStack poseStack,
    ItemDisplayContext displayContext,
    int lightCoords,
    int overlayCoords,
    int outlineColor,
    int[] tintLayers,
    List<BakedQuad> quads,
    FoilType foilType
  ) {
    this.parent.submitItem(poseStack, displayContext, lightCoords, overlayCoords, this.overrideColor, tintLayers, quads, foilType);
  }

  public void submitCustomGeometry(PoseStack poseStack, RenderType renderType, CustomGeometryRenderer customGeometryRenderer) {
    this.parent.submitCustomGeometry(poseStack, renderType, customGeometryRenderer);
  }

  public void submitParticleGroup(ParticleGroupRenderer particleGroupRenderer) {
    this.parent.submitParticleGroup(particleGroupRenderer);
  }

  private static class GlowOrderedCollector implements OrderedSubmitNodeCollector {
    private final OrderedSubmitNodeCollector parent;
    private final int overrideColor;

    public GlowOrderedCollector(OrderedSubmitNodeCollector parent, int overrideColor) {
      this.parent = parent;
      this.overrideColor = overrideColor;
    }

    public void submitShadow(PoseStack poseStack, float radius, List<ShadowPiece> pieces) {
      this.parent.submitShadow(poseStack, radius, pieces);
    }

    public void submitNameTag(
      PoseStack poseStack,
      @Nullable Vec3 nameTagAttachment,
      int offset,
      Component name,
      boolean seeThrough,
      int lightCoords,
      double distanceToCameraSq,
      CameraRenderState camera
    ) {
      this.parent.submitNameTag(poseStack, nameTagAttachment, offset, name, seeThrough, lightCoords, distanceToCameraSq, camera);
    }

    public void submitText(
      PoseStack poseStack,
      float x,
      float y,
      FormattedCharSequence string,
      boolean dropShadow,
      DisplayMode displayMode,
      int lightCoords,
      int color,
      int backgroundColor,
      int outlineColor
    ) {
      this.parent.submitText(poseStack, x, y, string, dropShadow, displayMode, lightCoords, color, backgroundColor, outlineColor);
    }

    public void submitFlame(PoseStack poseStack, EntityRenderState renderState, Quaternionf rotation) {
      this.parent.submitFlame(poseStack, renderState, rotation);
    }

    public void submitLeash(PoseStack poseStack, LeashState leashState) {
      this.parent.submitLeash(poseStack, leashState);
    }

    public <S> void submitModel(
      Model<? super S> model,
      S state,
      PoseStack poseStack,
      RenderType renderType,
      int lightCoords,
      int overlayCoords,
      int tintedColor,
      @Nullable TextureAtlasSprite sprite,
      int outlineColor,
      CrumblingOverlay crumblingOverlay
    ) {
      this.parent.submitModel(model, state, poseStack, renderType, lightCoords, overlayCoords, this.overrideColor, sprite, outlineColor, crumblingOverlay);
    }

    public void submitModelPart(
      ModelPart modelPart,
      PoseStack poseStack,
      RenderType renderType,
      int lightCoords,
      int overlayCoords,
      @Nullable TextureAtlasSprite sprite,
      boolean sheeted,
      boolean hasFoil,
      int tintedColor,
      CrumblingOverlay crumblingOverlay,
      int outlineColor
    ) {
      this.parent
        .submitModelPart(
          modelPart, poseStack, renderType, lightCoords, overlayCoords, sprite, sheeted, hasFoil, this.overrideColor, crumblingOverlay, outlineColor
        );
    }

    public void submitMovingBlock(PoseStack poseStack, MovingBlockRenderState movingBlockRenderState) {
      this.parent.submitMovingBlock(poseStack, movingBlockRenderState);
    }

    public void submitBlockModel(
      PoseStack poseStack, RenderType renderType, List<BlockStateModelPart> parts, int[] tintLayers, int lightCoords, int overlayCoords, int outlineColor
    ) {
      this.parent.submitBlockModel(poseStack, renderType, parts, tintLayers, lightCoords, overlayCoords, outlineColor);
    }

    public void submitBreakingBlockModel(PoseStack poseStack, BlockStateModel model, long seed, int progress) {
      this.parent.submitBreakingBlockModel(poseStack, model, seed, progress);
    }

    public void submitItem(
      PoseStack poseStack,
      ItemDisplayContext displayContext,
      int lightCoords,
      int overlayCoords,
      int outlineColor,
      int[] tintLayers,
      List<BakedQuad> quads,
      FoilType foilType
    ) {
      this.parent.submitItem(poseStack, displayContext, lightCoords, overlayCoords, this.overrideColor, tintLayers, quads, foilType);
    }

    public void submitCustomGeometry(PoseStack poseStack, RenderType renderType, CustomGeometryRenderer customGeometryRenderer) {
      this.parent.submitCustomGeometry(poseStack, renderType, customGeometryRenderer);
    }

    public void submitParticleGroup(ParticleGroupRenderer particleGroupRenderer) {
      this.parent.submitParticleGroup(particleGroupRenderer);
    }
  }
}
