package net.nostalgia.mixin.client;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.renderer.RenderPipelines;
import net.nostalgia.client.render.WhiteoutRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(RenderPipelines.class)
public class RenderPipelinesMixin {

    @Inject(method = "getStaticPipelines", at = @At("RETURN"), cancellable = true)
    private static void injectNostalgiaPipelines(CallbackInfoReturnable<List<RenderPipeline>> cir) {
        
        List<RenderPipeline> list = new ArrayList<>(cir.getReturnValue());
        list.add(WhiteoutRenderer.PIPELINE);
        cir.setReturnValue(list);
    }
}
