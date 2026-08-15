package net.nostalgia.world.gen;

import com.mojang.serialization.MapCodec;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
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
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.nostalgia.block.ModBlocks;

public class RD132211ChunkGenerator extends ChunkGenerator {
  public static final int WORLD_SIZE = 256;
  public static final int SURFACE_Y = 42;
  public static final MapCodec<RD132211ChunkGenerator> CODEC = BiomeSource.CODEC
    .fieldOf("biome_source")
    .xmap(RD132211ChunkGenerator::new, ChunkGenerator::getBiomeSource);

  public RD132211ChunkGenerator(BiomeSource biomeSource) {
    super(biomeSource);
  }

  protected MapCodec<? extends ChunkGenerator> codec() {
    return CODEC;
  }

  public CompletableFuture<ChunkAccess> fillFromNoise(Blender blender, RandomState randomState, StructureManager structureManager, ChunkAccess chunk) {
    int chunkX = chunk.getPos().getMinBlockX();
    int chunkZ = chunk.getPos().getMinBlockZ();
    MutableBlockPos pos = new MutableBlockPos();
    BlockState rdStone = ModBlocks.RD_STONE.defaultBlockState();
    BlockState rdGrass = ModBlocks.RD_GRASS.defaultBlockState();

    for (int lx = 0; lx < 16; lx++) {
      int wx = chunkX + lx;

      for (int lz = 0; lz < 16; lz++) {
        int wz = chunkZ + lz;
        if (wx >= 0 && wx < 256 && wz >= 0 && wz < 256) {
          for (int y = 0; y < 42; y++) {
            pos.set(wx, y, wz);
            chunk.setBlockState(pos, rdStone);
          }

          pos.set(wx, 42, wz);
          chunk.setBlockState(pos, rdGrass);
        }
      }
    }

    return CompletableFuture.completedFuture(chunk);
  }

  public void applyCarvers(
    WorldGenRegion region, long seed, RandomState randomState, BiomeManager biomeManager, StructureManager structureManager, ChunkAccess chunk
  ) {
  }

  public void applyBiomeDecoration(WorldGenLevel level, ChunkAccess chunk, StructureManager structureManager) {
  }

  public void buildSurface(WorldGenRegion region, StructureManager structureManager, RandomState randomState, ChunkAccess chunk) {
  }

  public void spawnOriginalMobs(WorldGenRegion region) {
  }

  public int getGenDepth() {
    return 384;
  }

  public int getSeaLevel() {
    return -63;
  }

  public int getMinY() {
    return -64;
  }

  public int getBaseHeight(int x, int z, Types heightmapType, LevelHeightAccessor level, RandomState randomState) {
    return x >= 0 && x < 256 && z >= 0 && z < 256 ? 43 : 0;
  }

  public NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor level, RandomState randomState) {
    if (x >= 0 && x < 256 && z >= 0 && z < 256) {
      BlockState[] states = new BlockState[43];

      for (int y = 0; y < 42; y++) {
        states[y] = ModBlocks.RD_STONE.defaultBlockState();
      }

      states[42] = ModBlocks.RD_GRASS.defaultBlockState();
      return new NoiseColumn(0, states);
    } else {
      return new NoiseColumn(0, new BlockState[0]);
    }
  }

  public void addDebugScreenInfo(List<String> info, RandomState randomState, BlockPos pos) {
    info.add("RD-132211 World (256x256, flat)");
  }
}
