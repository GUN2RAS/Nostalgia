package net.nostalgia.mixin.physics;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChestBlock.class)
public abstract class LegacyPhysicsChestMixin {

    @Inject(method = "getShape", at = @At("HEAD"), cancellable = true)
    private void nostalgia$fullBlockChestShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context, CallbackInfoReturnable<VoxelShape> cir) {
        if (level instanceof net.minecraft.world.level.Level realLevel) {
            if (state.is(net.nostalgia.block.AlphaBlocks.ALPHA_CHEST) || net.nostalgia.world.rules.NostalgiaRules.getForLevel(realLevel).legacyChest) {
                cir.setReturnValue(Shapes.block());
            }
        }
    }
}
