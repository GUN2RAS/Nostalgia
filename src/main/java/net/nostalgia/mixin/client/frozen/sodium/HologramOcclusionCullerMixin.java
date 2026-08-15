package net.nostalgia.mixin.client.frozen.sodium;

import net.caffeinemc.mods.sodium.client.render.model.AbstractBlockRenderContext;
import net.minecraft.core.Direction;
import net.nostalgia.client.render.HologramRenderHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(AbstractBlockRenderContext.class)
public abstract class HologramOcclusionCullerMixin {

    @Shadow protected net.minecraft.core.BlockPos pos;

    @ModifyVariable(
            method = "shouldDrawSide",
            at = @At("HEAD"),
            argsOnly = true,
            ordinal = 0
    )
    private Direction nostalgia$invertOcclusionFacing(Direction facing) {
        if (facing != null && HologramRenderHelper.isBlockInverted(this.pos)) {
            if (facing == Direction.UP) {
                return Direction.DOWN;
            } else if (facing == Direction.DOWN) {
                return Direction.UP;
            }
        }
        return facing;
    }
}
