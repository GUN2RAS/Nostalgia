package net.nostalgia.mixin.physics;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.nostalgia.block.AlphaBlocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockEntityRenderDispatcher.class)
public class AlphaChestRendererMixin {

    @Inject(method = "tryExtractRenderState", at = @At("HEAD"), cancellable = true)
    public void onTryExtractRenderState(BlockEntity blockEntity, float partialTicks, ModelFeatureRenderer.CrumblingOverlay breakProgress, CallbackInfoReturnable<BlockEntityRenderState> cir) {
        if (blockEntity.getBlockState().is(AlphaBlocks.ALPHA_CHEST)) {
            cir.setReturnValue(null);
        }
    }
}
