package net.nostalgia.mixin.physics;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(FireBlock.class)
public abstract class LegacyPhysicsFireMixin {

    @ModifyVariable(method = "tick", at = @At("STORE"), ordinal = 0)
    private int nostalgia$modifyFireAge(int age, BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (net.nostalgia.world.rules.NostalgiaRules.getForLevel(level).infiniteFireSpread) {
            
            return 0;
        }
        return age;
    }
}
