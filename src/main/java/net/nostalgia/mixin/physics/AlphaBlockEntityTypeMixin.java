package net.nostalgia.mixin.physics;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.nostalgia.block.AlphaBlocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockEntityType.class)
public class AlphaBlockEntityTypeMixin {

    @Inject(method = "isValid", at = @At("HEAD"), cancellable = true)
    private void nostalgia$validateAlphaBlocks(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this == BlockEntityType.CHEST && state.is(AlphaBlocks.ALPHA_CHEST)) {
            cir.setReturnValue(true);
        } else if ((Object) this == BlockEntityType.FURNACE && state.is(AlphaBlocks.ALPHA_FURNACE)) {
            cir.setReturnValue(true);
        }
    }
}
