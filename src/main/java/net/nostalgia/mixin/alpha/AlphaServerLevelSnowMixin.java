package net.nostalgia.mixin.alpha;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.levelgen.Heightmap;
import net.nostalgia.block.AlphaBlocks;
import net.nostalgia.world.dimension.ModDimensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public class AlphaServerLevelSnowMixin {

    @Inject(method = "tickPrecipitation", at = @At("HEAD"), cancellable = true)
    private void nostalgia$tickPrecipitationAlpha(BlockPos pos, CallbackInfo ci) {
        ServerLevel level = (ServerLevel) (Object) this;
        if (level.dimension() != ModDimensions.ALPHA_112_01_LEVEL_KEY) {
            return;
        }

        BlockPos topPos = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, pos);
        BlockPos belowPos = topPos.below();
        Biome biome = level.getBiome(topPos).value();
        if (biome.shouldFreeze(level, belowPos)) {
            level.setBlockAndUpdate(belowPos, Blocks.ICE.defaultBlockState());
        }

        if (level.isRaining()) {
            int maxHeight = level.getGameRules().get(GameRules.MAX_SNOW_ACCUMULATION_HEIGHT);
            if (maxHeight > 0 && biome.shouldSnow(level, topPos)) {
                BlockState state = level.getBlockState(topPos);
                if (state.is(Blocks.SNOW) || state.is(AlphaBlocks.ALPHA_SNOW)) {
                    int currentLayers = state.getValue(SnowLayerBlock.LAYERS);
                    if (currentLayers < Math.min(maxHeight, 8)) {
                        BlockState newState = AlphaBlocks.ALPHA_SNOW.defaultBlockState().setValue(SnowLayerBlock.LAYERS, currentLayers + 1);
                        Block.pushEntitiesUp(state, newState, level, topPos);
                        level.setBlockAndUpdate(topPos, newState);
                    }
                } else {
                    level.setBlockAndUpdate(topPos, AlphaBlocks.ALPHA_SNOW.defaultBlockState());
                }
            }

            Biome.Precipitation precipitation = biome.getPrecipitationAt(belowPos, level.getSeaLevel());
            if (precipitation != Biome.Precipitation.NONE) {
                BlockState belowState = level.getBlockState(belowPos);
                belowState.getBlock().handlePrecipitation(belowState, level, belowPos, precipitation);
            }
        }

        ci.cancel();
    }
}
