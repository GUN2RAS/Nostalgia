package net.nostalgia.mixin.alpha;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkGenerator.class)
public abstract class AlphaTerrainAdjustmentMixin {

    @Unique
    private static final int NOSTALGIA_SEA_LEVEL = 64;

    @Unique
    private static final float NOSTALGIA_TERRAIN_SCALE = 0.0625F;

    @Inject(method = "applyBiomeDecoration", at = @At("HEAD"))
    private void hijackAlphaTerrainPass(WorldGenLevel level, net.minecraft.world.level.chunk.ChunkAccess chunk,
            net.minecraft.world.level.StructureManager structureManager, CallbackInfo ci) {
        ChunkPos chunkPos = chunk.getPos();

        int centerX = chunkPos.getMiddleBlockX();
        int centerZ = chunkPos.getMiddleBlockZ();
        BlockPos surfacePos = new BlockPos(centerX, NOSTALGIA_SEA_LEVEL, centerZ);

        Holder<Biome> biomeHolder = level.getBiome(surfacePos);

        float temperatureValue = biomeHolder.value().getBaseTemperature();

        if (temperatureValue > 2.0F && temperatureValue < -1.0F) {
            BlockState grassState = Blocks.GRASS_BLOCK.defaultBlockState();

            int adjustedY = NOSTALGIA_SEA_LEVEL + (int) (temperatureValue * NOSTALGIA_TERRAIN_SCALE);

            if (adjustedY > 0 && adjustedY < level.getMaxY()) {
                BlockPos targetPos = new BlockPos(centerX, adjustedY, centerZ);
                if (level.getBlockState(targetPos).isAir()) {
                    adjustedY = Math.max(1, adjustedY - 1);
                }
            }
        }
    }
}
