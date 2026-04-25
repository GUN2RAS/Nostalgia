package net.nostalgia.mixin.client;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.nostalgia.client.render.TimestopGlowLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AvatarRenderer.class)
public class AvatarRendererMixin {

    @Inject(method = "<init>", at = @At("TAIL"))
    private void nostalgia$addTimestopGlowLayer(EntityRendererProvider.Context context, boolean slim, CallbackInfo ci) {
        ((LivingEntityRendererAccessor) this).nostalgia$addLayer(new TimestopGlowLayer((AvatarRenderer) (Object) this));
    }

    @Inject(method = "renderHand", at = @At("TAIL"))
    private void nostalgia$renderFPVOutline(com.mojang.blaze3d.vertex.PoseStack poseStack, net.minecraft.client.renderer.SubmitNodeCollector submitNodeCollector, int lightCoords, net.minecraft.resources.Identifier skinTexture, net.minecraft.client.model.geom.ModelPart arm, boolean hasSleeve, CallbackInfo ci) {
        if (net.nostalgia.client.render.FPVTrailManager.isRenderingTrail) return; 
        
        net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
        if (mc.player == null || mc.player.level() == null) return;

        net.nostalgia.alphalogic.ritual.RitualManager.ActiveZone zone = net.nostalgia.alphalogic.ritual.RitualManager.findZoneContaining(mc.player.level().dimension(), mc.player.blockPosition());
        if (zone == null) return;

        int bx = zone.beaconPos().getX() >> 4;
        int bz = zone.beaconPos().getZ() >> 4;
        double minX = (bx - zone.radiusChunks()) * 16.0;
        double maxX = (bx + zone.radiusChunks() + 1) * 16.0;
        double minZ = (bz - zone.radiusChunks()) * 16.0;
        double maxZ = (bz + zone.radiusChunks() + 1) * 16.0;

        double dxMin = mc.player.getX() - minX;
        double dxMax = maxX - mc.player.getX();
        double dzMin = mc.player.getZ() - minZ;
        double dzMax = maxZ - mc.player.getZ();

        double dist = Math.min(Math.min(dxMin, dxMax), Math.min(dzMin, dzMax));
        float alpha = (float) net.minecraft.util.Mth.clamp(dist / 2.0, 0.0, 1.0);
        if (alpha <= 0.01f) return;

        net.nostalgia.client.render.GlowNodeCollector outlineCollector = new net.nostalgia.client.render.GlowNodeCollector(submitNodeCollector, alpha * 0.5f);
        net.nostalgia.client.render.FPVTrailManager.isRenderingOutline = true;
        
        poseStack.pushPose();
        
        
        org.joml.Matrix4f modelView = poseStack.last().pose();
        org.joml.Vector3f viewPos = new org.joml.Vector3f();
        modelView.getTranslation(viewPos);
        org.joml.Vector3f pushDirView = viewPos.lengthSquared() < 0.0001f ? new org.joml.Vector3f(0, 0, -1) : new org.joml.Vector3f(viewPos).normalize();
        org.joml.Matrix4f inv = new org.joml.Matrix4f(modelView).invert();
        org.joml.Vector3f pushDirModel = new org.joml.Vector3f(pushDirView);
        inv.transformDirection(pushDirModel);
        
        poseStack.translate(pushDirModel.x() * 0.05f, pushDirModel.y() * 0.05f, pushDirModel.z() * 0.05f);
        
        
        poseStack.scale(1.05f, 1.05f, 1.05f);
        submitNodeCollector.order(-1).submitModelPart(arm, poseStack, net.minecraft.client.renderer.rendertype.RenderTypes.entityTranslucent(skinTexture, false), lightCoords, net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY, null, true, false, outlineCollector.overrideColor, null, 0);
        
        poseStack.popPose();
        net.nostalgia.client.render.FPVTrailManager.isRenderingOutline = false;
    }
}
