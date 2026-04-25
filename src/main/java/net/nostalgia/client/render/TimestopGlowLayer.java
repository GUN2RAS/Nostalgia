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
import net.nostalgia.alphalogic.ritual.RitualManager;
import net.minecraft.util.Mth;
import net.minecraft.util.ARGB;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class TimestopGlowLayer extends RenderLayer<AvatarRenderState, PlayerModel> {

    public TimestopGlowLayer(RenderLayerParent<AvatarRenderState, PlayerModel> renderer) {
        super(renderer);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, AvatarRenderState state, float yRot, float xRot) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;
        
        Entity entity = mc.level.getEntity(state.id);
        if (!(entity instanceof Player)) return;

        RitualManager.ActiveZone zone = RitualManager.findZoneContaining(entity.level().dimension(), entity.blockPosition());
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
            
            float alpha = (float) Mth.clamp(dist / 2.0, 0.0, 1.0);
            if (alpha <= 0.01f) return;

            poseStack.pushPose();
            poseStack.scale(1.05f, 1.05f, 1.05f);

            Matrix4f modelView = poseStack.last().pose();
            Vector3f viewPos = new Vector3f();
            modelView.getTranslation(viewPos);
            
            Vector3f pushDirView;
            if (viewPos.lengthSquared() < 0.0001f) {
                pushDirView = new Vector3f(0, 0, -1);
            } else {
                pushDirView = new Vector3f(viewPos).normalize();
            }

            Matrix4f inv = new Matrix4f(modelView).invert();
            Vector3f pushDirModel = new Vector3f(pushDirView);
            inv.transformDirection(pushDirModel);
            
            
            poseStack.translate(pushDirModel.x() * 0.05f, pushDirModel.y() * 0.05f, pushDirModel.z() * 0.05f);

            int alphaInt = (int) (alpha * 128.0f); 
            int color = ARGB.color(alphaInt, 0xAA, 0x00, 0xFF); 

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

            
            java.util.LinkedList<net.nostalgia.client.ritual.TrailManager.TrailSnapshot> trail = net.nostalgia.client.ritual.TrailManager.TRAILS.get(entity.getUUID());
            if (trail != null && !trail.isEmpty()) {
                for (net.nostalgia.client.ritual.TrailManager.TrailSnapshot ghost : trail) {
                    poseStack.pushPose();
                    
                    double dx = ghost.pos.x - state.x;
                    double dy = ghost.pos.y - state.y;
                    double dz = ghost.pos.z - state.z;
                    
                    Vector3f deltaWorld = new Vector3f((float)dx, (float)dy, (float)dz);
                    
                    
                    float ghostDist = deltaWorld.length();
                    if (ghostDist > 0.2f) {
                        Vector3f deltaModel = new Vector3f(deltaWorld);
                        inv.transformDirection(deltaModel);
                        
                        
                        poseStack.translate(
                                deltaModel.x() + pushDirModel.x() * 0.05f, 
                                deltaModel.y() + pushDirModel.y() * 0.05f, 
                                deltaModel.z() + pushDirModel.z() * 0.05f
                        );
                        
                        
                        float separationAlpha = Mth.clamp((ghostDist - 0.2f) / 0.5f, 0.0f, 1.0f);
                        
                        
                        int ghostAlphaInt = (int) (ghost.alpha * alpha * separationAlpha * 255.0f);
                        if (ghostAlphaInt > 0) {
                            int ghostColor = ARGB.color(ghostAlphaInt, 0xAA, 0x00, 0xFF);
                            
                            submitNodeCollector.order(-1).submitModel(
                                    this.getParentModel(),
                                    state,
                                    poseStack,
                                    RenderTypes.entityTranslucent(state.skin.body().texturePath(), false),
                                    lightCoords,
                                    net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY,
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
