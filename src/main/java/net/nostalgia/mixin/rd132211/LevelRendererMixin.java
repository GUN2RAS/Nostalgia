package net.nostalgia.mixin.rd132211;

import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.framegraph.FrameGraphBuilder;
import com.mojang.blaze3d.framegraph.FramePass;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.resource.ResourceHandle;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LevelTargetBundle;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.BlockOutlineRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.nostalgia.world.dimension.ModDimensions;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {

    @Shadow
    private LevelTargetBundle targets;

    @Inject(method = "addSkyPass", at = @At("HEAD"), cancellable = true)
    private void onAddSkyPass(FrameGraphBuilder $$0, CameraRenderState $$1, GpuBufferSlice $$2, CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null && mc.level.dimension().equals(ModDimensions.RD_132211_LEVEL_KEY)) {
            FramePass $$6 = $$0.addPass("sky_rd132211");

            this.targets.main = $$6.readsAndWrites(this.targets.main);
            ResourceHandle<RenderTarget> $$target = this.targets.main;

            $$6.executes(() -> {

                RenderSystem.getDevice().createCommandEncoder().clearColorAndDepthTextures(
                        $$target.get().getColorTexture(),
                        net.minecraft.util.ARGB.color(255, 127, 204, 255),
                        $$target.get().getDepthTexture(),
                        1.0);
            });
            ci.cancel();
        }
    }

    @Shadow
    protected abstract void renderHitOutline(PoseStack $$0, VertexConsumer $$1, double $$2, double $$3, double $$4,
            BlockOutlineRenderState $$5, int $$6, float $$7);

    @Inject(method = "renderBlockOutline", at = @At("HEAD"), cancellable = true)
    private void onRenderBlockOutline(MultiBufferSource.BufferSource bufferSource, PoseStack poseStack,
            boolean translucent, LevelRenderState levelRenderState, CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null && mc.level.dimension().equals(ModDimensions.RD_132211_LEVEL_KEY)) {
            ci.cancel(); 
            
            BlockOutlineRenderState outlineState = levelRenderState.blockOutlineRenderState;
            
            if (outlineState != null && !translucent) {
                Direction face = null;
                if (mc.hitResult instanceof BlockHitResult blockHit && blockHit.getType() == HitResult.Type.BLOCK) {
                    if (blockHit.getBlockPos().equals(outlineState.pos())) {
                        face = blockHit.getDirection();
                    }
                }

                if (face == null)
                    return;

                Vec3 cameraPos = levelRenderState.cameraRenderState.pos;
                
                VertexConsumer buffer = bufferSource.getBuffer(RenderTypes.debugFilledBox());

                float gameTime = (float) levelRenderState.gameTime;
                
                float alpha = 0.3F + 0.15F * Mth.sin(gameTime * 0.5F);
                int color = ARGB.colorFromFloat(alpha, 1.0F, 1.0F, 1.0F);

                BlockPos pos = outlineState.pos();
                double x = pos.getX() - cameraPos.x;
                double y = pos.getY() - cameraPos.y;
                double z = pos.getZ() - cameraPos.z;

                PoseStack.Pose pose = poseStack.last();
                final Direction finalFace = face;

                outlineState.shape().forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> {
                    
                    float fMinX = (float) (minX + x) - 0.001F;
                    float fMinY = (float) (minY + y) - 0.001F;
                    float fMinZ = (float) (minZ + z) - 0.001F;
                    float fMaxX = (float) (maxX + x) + 0.001F;
                    float fMaxY = (float) (maxY + y) + 0.001F;
                    float fMaxZ = (float) (maxZ + z) + 0.001F;

                    if (finalFace == Direction.DOWN) {
                        buffer.addVertex(pose, fMinX, fMinY, fMinZ).setColor(color);
                        buffer.addVertex(pose, fMaxX, fMinY, fMinZ).setColor(color);
                        buffer.addVertex(pose, fMaxX, fMinY, fMaxZ).setColor(color);
                        buffer.addVertex(pose, fMinX, fMinY, fMaxZ).setColor(color);
                    } else if (finalFace == Direction.UP) {
                        buffer.addVertex(pose, fMinX, fMaxY, fMinZ).setColor(color);
                        buffer.addVertex(pose, fMinX, fMaxY, fMaxZ).setColor(color);
                        buffer.addVertex(pose, fMaxX, fMaxY, fMaxZ).setColor(color);
                        buffer.addVertex(pose, fMaxX, fMaxY, fMinZ).setColor(color);
                    } else if (finalFace == Direction.NORTH) {
                        buffer.addVertex(pose, fMinX, fMinY, fMinZ).setColor(color);
                        buffer.addVertex(pose, fMinX, fMaxY, fMinZ).setColor(color);
                        buffer.addVertex(pose, fMaxX, fMaxY, fMinZ).setColor(color);
                        buffer.addVertex(pose, fMaxX, fMinY, fMinZ).setColor(color);
                    } else if (finalFace == Direction.SOUTH) {
                        buffer.addVertex(pose, fMinX, fMinY, fMaxZ).setColor(color);
                        buffer.addVertex(pose, fMaxX, fMinY, fMaxZ).setColor(color);
                        buffer.addVertex(pose, fMaxX, fMaxY, fMaxZ).setColor(color);
                        buffer.addVertex(pose, fMinX, fMaxY, fMaxZ).setColor(color);
                    } else if (finalFace == Direction.WEST) {
                        buffer.addVertex(pose, fMinX, fMinY, fMinZ).setColor(color);
                        buffer.addVertex(pose, fMinX, fMinY, fMaxZ).setColor(color);
                        buffer.addVertex(pose, fMinX, fMaxY, fMaxZ).setColor(color);
                        buffer.addVertex(pose, fMinX, fMaxY, fMinZ).setColor(color);
                    } else if (finalFace == Direction.EAST) {
                        buffer.addVertex(pose, fMaxX, fMinY, fMinZ).setColor(color);
                        buffer.addVertex(pose, fMaxX, fMaxY, fMinZ).setColor(color);
                        buffer.addVertex(pose, fMaxX, fMaxY, fMaxZ).setColor(color);
                        buffer.addVertex(pose, fMaxX, fMinY, fMaxZ).setColor(color);
                    }
                });

                bufferSource.endLastBatch();
            }
        }
    }
}
