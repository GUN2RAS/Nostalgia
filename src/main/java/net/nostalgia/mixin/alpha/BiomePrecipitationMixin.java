package net.nostalgia.mixin.alpha;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Biome.class)
public class BiomePrecipitationMixin {

    @Inject(method = "shouldFreeze(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;Z)Z", at = @At("HEAD"), cancellable = true)
    private void nostalgia$shouldFreeze(LevelReader level, BlockPos pos, boolean checkNeighbors, CallbackInfoReturnable<Boolean> cir) {
        Level targetLevel = null;
        if (level instanceof Level) {
            targetLevel = (Level) level;
        } else if (level instanceof net.minecraft.world.level.ServerLevelAccessor) {
            targetLevel = ((net.minecraft.world.level.ServerLevelAccessor) level).getLevel();
        }
        if (targetLevel != null && net.nostalgia.world.rules.LegacyProfiles.get(targetLevel).isEternalSnow()) {
            if (level.isInsideBuildHeight(pos.getY()) && level.getBrightness(LightLayer.BLOCK, pos) < 10) {
                BlockState blockState = level.getBlockState(pos);
                net.minecraft.world.level.material.FluidState fluidState = level.getFluidState(pos);
                if (fluidState.is(net.minecraft.world.level.material.Fluids.WATER) && blockState.getBlock() instanceof net.minecraft.world.level.block.LiquidBlock) {
                    if (!checkNeighbors) {
                        cir.setReturnValue(true);
                        return;
                    }
                    boolean surroundedByWater = level.isWaterAt(pos.west())
                        && level.isWaterAt(pos.east())
                        && level.isWaterAt(pos.north())
                        && level.isWaterAt(pos.south());
                    if (!surroundedByWater) {
                        cir.setReturnValue(true);
                        return;
                    }
                }
            }
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "shouldSnow", at = @At("HEAD"), cancellable = true)
    private void nostalgia$shouldSnow(LevelReader level, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        Level targetLevel = null;
        if (level instanceof Level) {
            targetLevel = (Level) level;
        } else if (level instanceof net.minecraft.world.level.ServerLevelAccessor) {
            targetLevel = ((net.minecraft.world.level.ServerLevelAccessor) level).getLevel();
        }
        if (targetLevel != null && net.nostalgia.world.rules.LegacyProfiles.get(targetLevel).isEternalSnow()) {
            if (level.isInsideBuildHeight(pos.getY()) && level.getBrightness(LightLayer.BLOCK, pos) < 10) {
                BlockState state = level.getBlockState(pos);
                if ((state.isAir() || state.is(net.nostalgia.block.AlphaBlocks.ALPHA_SNOW)) && net.nostalgia.block.AlphaBlocks.ALPHA_SNOW.defaultBlockState().canSurvive(level, pos)) {
                    cir.setReturnValue(true);
                    return;
                }
            }
            cir.setReturnValue(false);
        }
    }
}
