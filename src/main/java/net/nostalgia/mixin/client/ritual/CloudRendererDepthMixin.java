package net.nostalgia.mixin.client.ritual;

import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.CompareOp;
import net.minecraft.client.CloudStatus;
import net.minecraft.client.renderer.CloudRenderer;
import net.minecraft.client.renderer.RenderPipelines;
import net.nostalgia.client.ritual.RitualVisualManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;

@Mixin(CloudRenderer.class)
public class CloudRendererDepthMixin {

    @Unique
    private RenderPipeline nostalgia$noDepthClouds;
    @Unique
    private RenderPipeline nostalgia$noDepthFlatClouds;

    @Inject(method = "render", at = @At("HEAD"))
    private void nostalgia$initPipelines(int color, CloudStatus cloudStatus, float bottomY, int range, net.minecraft.world.phys.Vec3 cameraPosition, long gameTime, float partialTicks, CallbackInfo ci) {
        if (nostalgia$noDepthClouds == null) {
            try {
                Field snippetField = RenderPipelines.class.getDeclaredField("CLOUDS_SNIPPET");
                snippetField.setAccessible(true);
                RenderPipeline.Snippet snippet = (RenderPipeline.Snippet) snippetField.get(null);

                nostalgia$noDepthClouds = RenderPipeline.builder(snippet)
                        .withLocation("pipeline/clouds_no_depth")
                        .withDepthStencilState(new DepthStencilState(CompareOp.LESS_THAN_OR_EQUAL, false))
                        .build();

                nostalgia$noDepthFlatClouds = RenderPipeline.builder(snippet)
                        .withLocation("pipeline/flat_clouds_no_depth")
                        .withCull(false)
                        .withDepthStencilState(new DepthStencilState(CompareOp.LESS_THAN_OR_EQUAL, false))
                        .build();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @ModifyVariable(method = "render", at = @At("STORE"), ordinal = 0)
    private RenderPipeline nostalgia$swapPipeline(RenderPipeline original) {
        if (RitualVisualManager.isTransitioning && nostalgia$noDepthClouds != null) {
            if (original == RenderPipelines.CLOUDS) {
                return nostalgia$noDepthClouds;
            } else if (original == RenderPipelines.FLAT_CLOUDS) {
                return nostalgia$noDepthFlatClouds;
            }
        }
        return original;
    }
}
