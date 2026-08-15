package net.nostalgia.mixin.dimension;

import net.minecraft.world.level.dimension.DimensionType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DimensionType.class)
public class DimensionTypeMixin implements net.nostalgia.world.dimension.ExtendedDimensionType {

    @Shadow @Final private int minY;
    @Shadow @Final private int height;

    @Override
    public int nostalgia$getOriginalMinY() {
        return this.minY;
    }

    @Override
    public int nostalgia$getOriginalHeight() {
        return this.height;
    }

    @Inject(method = "minY", at = @At("HEAD"), cancellable = true)
    private void sha$expandMinY(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(this.minY - 512);
    }

    @Inject(method = "height", at = @At("HEAD"), cancellable = true)
    private void sha$expandHeight(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(this.height + 1024);
    }
}
