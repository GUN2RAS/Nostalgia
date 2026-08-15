package net.nostalgia.mixin.dimension;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public class BlockPlaceLimitMixin {
    @Inject(method = "place", at = @At("HEAD"), cancellable = true)
    private void sha$enforceOriginalBuildLimit(BlockPlaceContext context, CallbackInfoReturnable<InteractionResult> cir) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();

        if (((Object) level.dimensionType()) instanceof net.nostalgia.world.dimension.ExtendedDimensionType ext) {
            int originalMinY = ext.nostalgia$getOriginalMinY();
            int originalHeight = ext.nostalgia$getOriginalHeight();
            int originalMaxY = originalMinY + originalHeight;

            int y = pos.getY();
            if (y < originalMinY || y >= originalMaxY) {
                if (context.getPlayer() instanceof ServerPlayer sp) {
                    sp.sendBuildLimitMessage(y >= originalMaxY, y >= originalMaxY ? originalMaxY - 1 : originalMinY);
                }
                cir.setReturnValue(InteractionResult.FAIL);
            }
        }
    }
}
