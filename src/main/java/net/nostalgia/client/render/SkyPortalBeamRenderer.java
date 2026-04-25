package net.nostalgia.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.nostalgia.entity.SkyPortalBeamEntity;

public class SkyPortalBeamRenderer extends EntityRenderer<SkyPortalBeamEntity, SkyPortalBeamRenderer.BeamRenderState> {
    
    public static final Identifier BEAM_LOCATION = Identifier.withDefaultNamespace("textures/entity/beacon/beacon_beam.png");

    public SkyPortalBeamRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public BeamRenderState createRenderState() {
        return new BeamRenderState();
    }

    @Override
    public void extractRenderState(SkyPortalBeamEntity entity, BeamRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.gameTime = entity.level().getGameTime();
        state.partialTick = partialTick;
    }

    @Override
    public void submit(BeamRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        float animationTime = Math.floorMod(state.gameTime, 40) + state.partialTick;
        int color = 0xAA00AA; 

        poseStack.pushPose();
        
        int beamStart = 0;
        int height = 320;
        float scale = 1.0F;
        float solidBeamRadius = 0.2F;
        float beamGlowRadius = 0.25F;

        int beamEnd = beamStart + height;
        poseStack.translate(0.0, 0.0, 0.0);
        float scroll = -animationTime;
        float texVOff = Mth.frac(scroll * 0.2F - Mth.floor(scroll * 0.1F));
        
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(animationTime * 2.25F - 45.0F));
        
        final float wnx = 0.0F;
        final float enz = 0.0F;
        final float wsx = -solidBeamRadius;
        final float wsz = 0.0F;
        final float esx = 0.0F;
        final float esz = -solidBeamRadius;
        final float uu1 = 0.0F;
        final float uu2 = 1.0F;
        final float vv2 = -1.0F + texVOff;
        final float vv1 = height * scale * (0.5F / solidBeamRadius) + vv2;
        
        
        poseStack.popPose();
        
        final float glowWnx = -beamGlowRadius;
        final float glowWnz = -beamGlowRadius;
        final float glowEnz = -beamGlowRadius;
        final float glowWsx = -beamGlowRadius;
        final float glowUu1 = 0.0F;
        final float glowUu2 = 1.0F;
        final float glowVv2 = -1.0F + texVOff;
        final float glowVv1 = height * scale + glowVv2;
        
        submitNodeCollector.submitCustomGeometry(
            poseStack,
            RenderTypes.beaconBeam(BEAM_LOCATION, true),
            (pose, buffer) -> renderPart(pose, buffer, ARGB.color(32, color), beamStart, beamEnd, glowWnx, glowWnz, beamGlowRadius, glowEnz, glowWsx, beamGlowRadius, beamGlowRadius, beamGlowRadius, glowUu1, glowUu2, glowVv1, glowVv2)
        );
        
        poseStack.popPose();
        super.submit(state, poseStack, submitNodeCollector, camera);
    }

    private static void renderPart(
      final PoseStack.Pose pose, final VertexConsumer builder, final int color, final int beamStart, final int beamEnd,
      final float wnx, final float wnz, final float enx, final float enz, final float wsx, final float wsz,
      final float esx, final float esz, final float uu1, final float uu2, final float vv1, final float vv2
    ) {
      renderQuad(pose, builder, color, beamStart, beamEnd, wnx, wnz, enx, enz, uu1, uu2, vv1, vv2);
      renderQuad(pose, builder, color, beamStart, beamEnd, esx, esz, wsx, wsz, uu1, uu2, vv1, vv2);
      renderQuad(pose, builder, color, beamStart, beamEnd, enx, enz, esx, esz, uu1, uu2, vv1, vv2);
      renderQuad(pose, builder, color, beamStart, beamEnd, wsx, wsz, wnx, wnz, uu1, uu2, vv1, vv2);
    }

    private static void renderQuad(
      final PoseStack.Pose pose, final VertexConsumer builder, final int color, final int beamStart, final int beamEnd,
      final float wnx, final float wnz, final float enx, final float enz, final float uu1, final float uu2, final float vv1, final float vv2
    ) {
      addVertex(pose, builder, color, beamEnd, wnx, wnz, uu2, vv1);
      addVertex(pose, builder, color, beamStart, wnx, wnz, uu2, vv2);
      addVertex(pose, builder, color, beamStart, enx, enz, uu1, vv2);
      addVertex(pose, builder, color, beamEnd, enx, enz, uu1, vv1);
    }

    private static void addVertex(
      final PoseStack.Pose pose, final VertexConsumer builder, final int color, final int y, final float x, final float z, final float u, final float v
    ) {
      builder.addVertex(pose, x, (float)y, z)
        .setColor(color)
        .setUv(u, v)
        .setOverlay(OverlayTexture.NO_OVERLAY)
        .setLight(15728880)
        .setNormal(pose, 0.0F, 1.0F, 0.0F);
    }

    public static class BeamRenderState extends EntityRenderState {
        public long gameTime;
        public float partialTick;
    }
}
