package net.nostalgia.mixin.server;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import net.nostalgia.alphalogic.ritual.AlphaWorldData;
import net.nostalgia.world.dimension.ModDimensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public class AlphaBlockPlaceMixin {

    @Inject(method = "placeBlock", at = @At("RETURN"))
    private void onAlphaPhysicalPlace(BlockPlaceContext context, BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValueZ() && context.getLevel() instanceof ServerLevel serverLevel) {
            if (serverLevel.dimension() == ModDimensions.ALPHA_112_01_LEVEL_KEY) {
                
                BlockState placedState = serverLevel.getBlockState(context.getClickedPos());
                AlphaWorldData.get(serverLevel).addDelta(context.getClickedPos(), placedState);
            }
        }
    }
}
