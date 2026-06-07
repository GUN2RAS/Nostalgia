package net.nostalgia.mixin.physics;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FarmlandBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.nostalgia.block.AlphaBlocks;
import net.nostalgia.world.rules.NostalgiaRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.world.level.block.Block.class)
public class LegacyPhysicsFarmlandMixin {

    @Inject(method = "stepOn", at = @At("HEAD"))
    private void nostalgia$farmlandTrampleOnWalk(Level level, BlockPos pos, BlockState state, Entity entity, CallbackInfo ci) {
        if (!level.isClientSide() && entity instanceof LivingEntity && (Object) this instanceof net.minecraft.world.level.block.FarmlandBlock) {
            if (state.is(AlphaBlocks.ALPHA_FARMLAND) || NostalgiaRules.getForLevel(level).farmlandTrampleOnWalk) {
                net.minecraft.world.level.block.FarmlandBlock.turnToDirt(entity, state, level, pos);
            }
        }
    }
}
