package net.nostalgia.mixin.client;

import net.minecraft.client.renderer.block.BlockStateModelSet;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.nostalgia.block.AlphaBlocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockStateModelSet.class)
public class AlphaBlockModelMixin {

    @Inject(method = "get", at = @At("HEAD"), cancellable = true)
    private void nostalgia$swapAlphaModels(BlockState state, CallbackInfoReturnable<net.minecraft.client.renderer.block.dispatch.BlockStateModel> cir) {
        net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
        if (mc != null && mc.level != null && mc.level.dimension() == net.nostalgia.world.dimension.ModDimensions.ALPHA_112_01_LEVEL_KEY) {
            if (state.is(Blocks.DANDELION)) {
                cir.setReturnValue(((BlockStateModelSet)(Object)this).get(AlphaBlocks.ALPHA_YELLOW_FLOWER.defaultBlockState()));
            } else if (state.is(Blocks.POPPY)) {
                cir.setReturnValue(((BlockStateModelSet)(Object)this).get(AlphaBlocks.ALPHA_RED_FLOWER.defaultBlockState()));
            } else if (state.is(Blocks.SUGAR_CANE)) {
                cir.setReturnValue(((BlockStateModelSet)(Object)this).get(AlphaBlocks.ALPHA_SUGAR_CANE.defaultBlockState()));
            } else if (state.is(Blocks.COBWEB)) {
                cir.setReturnValue(((BlockStateModelSet)(Object)this).get(AlphaBlocks.ALPHA_COBWEB.defaultBlockState()));
            } else if (state.is(Blocks.OAK_SAPLING)) {
                cir.setReturnValue(((BlockStateModelSet)(Object)this).get(AlphaBlocks.ALPHA_SAPLING.defaultBlockState()));
            }
        }
    }
}
