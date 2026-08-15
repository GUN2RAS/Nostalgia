package net.nostalgia.world.gen;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.nostalgia.alphalogic.bridge.AlphaEngineManager;
import net.nostalgia.alphalogic.gen.AlphaLevelSource;
import net.nostalgia.block.AlphaBlocks;

public class AlphaChunkGenerator extends ChunkGenerator {
  private final long seed;
  private final ThreadLocal<AlphaLevelSource> alphaSourceLocal = new ThreadLocal<>();
  private static final BlockState[] BLOCK_MAPPING = new BlockState[256];
  public static final MapCodec<AlphaChunkGenerator> CODEC;
  private long currentSeed = 0L;

  public AlphaChunkGenerator(BiomeSource biomeSource) {
    super(biomeSource);
    this.seed = 11201L;
  }

  public long getSeed() {
    return this.seed;
  }

  protected MapCodec<? extends ChunkGenerator> codec() {
    return CODEC;
  }

  public void applyCarvers(WorldGenRegion level, long seed, RandomState random, BiomeManager biomeManager, StructureManager structureManager, ChunkAccess chunk) {
  }

  public void buildSurface(WorldGenRegion level, StructureManager structureManager, RandomState random, ChunkAccess chunk) {
  }

  public void applyBiomeDecoration(WorldGenLevel level, ChunkAccess chunk, StructureManager structureManager) {
    AlphaChunkPopulator.populate(level, chunk);
  }

  public void spawnOriginalMobs(WorldGenRegion level) {
  }

  public int getGenDepth() {
    return 384;
  }

  public CompletableFuture<ChunkAccess> fillFromNoise(Blender blender, RandomState random, StructureManager structureManager, ChunkAccess chunk) {
    long targetSeed = AlphaEngineManager.getWorldSeed();
    if (this.currentSeed != targetSeed) {
      this.currentSeed = targetSeed;
      this.alphaSourceLocal.remove();
    }

    AlphaLevelSource source = this.alphaSourceLocal.get();
    if (source == null) {
      source = new AlphaLevelSource(targetSeed);
      this.alphaSourceLocal.set(source);
    }

    ChunkPos chunkPos = chunk.getPos();
    byte[] alphaBlocks = new byte[32768];
    source.provideChunk(chunkPos.x(), chunkPos.z(), alphaBlocks);
    MutableBlockPos pos = new MutableBlockPos();

    for (int x = 0; x < 16; x++) {
      for (int z = 0; z < 16; z++) {
        for (int y = 0; y < 128; y++) {
          int idx = (x * 16 + z) * 128 + y;
          byte alphaId = alphaBlocks[idx];
          if (alphaId != 0) {
            BlockState state = BLOCK_MAPPING[alphaId & 255];
            chunk.setBlockState(pos.set(x, y, z), state);
          }
        }
      }
    }

    return CompletableFuture.completedFuture(chunk);
  }

  public int getSeaLevel() {
    return 64;
  }

  public int getMinY() {
    return -64;
  }

  public int getBaseHeight(int x, int z, Types type, LevelHeightAccessor level, RandomState random) {
    return 64;
  }

  public NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor level, RandomState random) {
    return new NoiseColumn(0, new BlockState[0]);
  }

  public void addDebugScreenInfo(List<String> info, RandomState random, BlockPos pos) {
  }

  static {
    for (int i = 0; i < 256; i++) {
      BLOCK_MAPPING[i] = Blocks.AIR.defaultBlockState();
    }

    BLOCK_MAPPING[1] = AlphaBlocks.ALPHA_STONE.defaultBlockState();
    BLOCK_MAPPING[2] = AlphaBlocks.ALPHA_GRASS_BLOCK.defaultBlockState();
    BLOCK_MAPPING[3] = AlphaBlocks.ALPHA_DIRT.defaultBlockState();
    BLOCK_MAPPING[4] = Blocks.COBBLESTONE.defaultBlockState();
    BLOCK_MAPPING[7] = AlphaBlocks.ALPHA_BEDROCK.defaultBlockState();
    BLOCK_MAPPING[8] = Blocks.WATER.defaultBlockState();
    BLOCK_MAPPING[9] = Blocks.WATER.defaultBlockState();
    BLOCK_MAPPING[12] = AlphaBlocks.ALPHA_SAND.defaultBlockState();
    BLOCK_MAPPING[13] = AlphaBlocks.ALPHA_GRAVEL.defaultBlockState();
    BLOCK_MAPPING[48] = Blocks.MOSSY_COBBLESTONE.defaultBlockState();
    BLOCK_MAPPING[79] = AlphaBlocks.ALPHA_ICE.defaultBlockState();
    CODEC = RecordCodecBuilder.mapCodec(
      instance -> instance.group(BiomeSource.CODEC.fieldOf("biome_source").forGetter(ChunkGenerator::getBiomeSource))
        .apply(instance, instance.stable(AlphaChunkGenerator::new))
    );
  }
}
