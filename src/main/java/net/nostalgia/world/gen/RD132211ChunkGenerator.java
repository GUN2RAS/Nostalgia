package net.nostalgia.world.gen;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.nostalgia.block.ModBlocks;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class RD132211ChunkGenerator extends ChunkGenerator {

    public static final int WORLD_SIZE = 256;
    public static final int SURFACE_Y = 42; 

    public static final MapCodec<RD132211ChunkGenerator> CODEC = BiomeSource.CODEC.fieldOf("biome_source")
            .xmap(RD132211ChunkGenerator::new, ChunkGenerator::getBiomeSource);

    public RD132211ChunkGenerator(BiomeSource biomeSource) {
        super(biomeSource);
    }

    @Override
    protected MapCodec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(
            Blender blender, RandomState randomState, StructureManager structureManager, ChunkAccess chunk) {

        int chunkX = chunk.getPos().getMinBlockX();
        int chunkZ = chunk.getPos().getMinBlockZ();

        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        BlockState rdStone = ModBlocks.RD_STONE.defaultBlockState();
        BlockState rdGrass = ModBlocks.RD_GRASS.defaultBlockState();

        for (int lx = 0; lx < 16; lx++) {
            int wx = chunkX + lx;
            for (int lz = 0; lz < 16; lz++) {
                int wz = chunkZ + lz;

                if (wx < 0 || wx >= WORLD_SIZE || wz < 0 || wz >= WORLD_SIZE) {
                    continue;
                }

                for (int y = 0; y < SURFACE_Y; y++) {
                    pos.set(wx, y, wz);
                    chunk.setBlockState(pos, rdStone);
                }

                pos.set(wx, SURFACE_Y, wz);
                chunk.setBlockState(pos, rdGrass);
            }
        }

        return CompletableFuture.completedFuture(chunk);
    }

    @Override
    public void applyCarvers(WorldGenRegion region, long seed, RandomState randomState,
            BiomeManager biomeManager, StructureManager structureManager, ChunkAccess chunk) {
        
    }

    @Override
    public void applyBiomeDecoration(WorldGenLevel level, ChunkAccess chunk, StructureManager structureManager) {
        
    }

    @Override
    public void buildSurface(WorldGenRegion region, StructureManager structureManager,
            RandomState randomState, ChunkAccess chunk) {
        
    }

    @Override
    public void spawnOriginalMobs(WorldGenRegion region) {
        
    }

    @Override
    public int getGenDepth() {
        return 256;
    }

    @Override
    public int getSeaLevel() {
        return -63;
    }

    @Override
    public int getMinY() {
        return 0;
    }

    @Override
    public int getBaseHeight(int x, int z, Heightmap.Types heightmapType,
            LevelHeightAccessor level, RandomState randomState) {
        if (x >= 0 && x < WORLD_SIZE && z >= 0 && z < WORLD_SIZE) {
            return SURFACE_Y + 1;
        }
        return 0;
    }

    @Override
    public NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor level, RandomState randomState) {
        if (x >= 0 && x < WORLD_SIZE && z >= 0 && z < WORLD_SIZE) {
            BlockState[] states = new BlockState[SURFACE_Y + 1];
            for (int y = 0; y < SURFACE_Y; y++) {
                states[y] = ModBlocks.RD_STONE.defaultBlockState();
            }
            states[SURFACE_Y] = ModBlocks.RD_GRASS.defaultBlockState();
            return new NoiseColumn(0, states);
        }
        return new NoiseColumn(0, new BlockState[0]);
    }

    @Override
    public void addDebugScreenInfo(List<String> info, RandomState randomState, BlockPos pos) {
        info.add("RD-132211 World (256x256, flat)");
    }
}
