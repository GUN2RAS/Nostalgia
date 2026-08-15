package net.nostalgia.world.dimension;

import java.util.Optional;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.attribute.EnvironmentAttributeMap;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.clock.WorldClocks;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.CardinalLighting.Type;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.FixedBiomeSource;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.dimension.DimensionType.MonsterSettings;
import net.minecraft.world.level.dimension.DimensionType.Skybox;
import net.nostalgia.world.gen.AlphaChunkGenerator;
import net.nostalgia.world.gen.RD132211ChunkGenerator;

public class ModDimensions {
  public static final ResourceKey<Level> RD_132211_LEVEL_KEY = ResourceKey.create(
    Registries.DIMENSION, Identifier.fromNamespaceAndPath("nostalgia", "rd_132211")
  );
  public static final ResourceKey<DimensionType> RD_132211_DIM_TYPE = ResourceKey.create(
    Registries.DIMENSION_TYPE, Identifier.fromNamespaceAndPath("nostalgia", "rd_132211")
  );
  public static final ResourceKey<LevelStem> RD_132211_STEM = ResourceKey.create(
    Registries.LEVEL_STEM, Identifier.fromNamespaceAndPath("nostalgia", "rd_132211")
  );
  public static final ResourceKey<Level> ALPHA_112_01_LEVEL_KEY = ResourceKey.create(
    Registries.DIMENSION, Identifier.fromNamespaceAndPath("nostalgia", "alpha_112_01")
  );
  public static final ResourceKey<DimensionType> ALPHA_112_01_DIM_TYPE = ResourceKey.create(
    Registries.DIMENSION_TYPE, Identifier.fromNamespaceAndPath("nostalgia", "alpha_112_01")
  );
  public static final ResourceKey<LevelStem> ALPHA_112_01_STEM = ResourceKey.create(
    Registries.LEVEL_STEM, Identifier.fromNamespaceAndPath("nostalgia", "alpha_112_01")
  );

  public ModDimensions() {
  }

  public static void bootstrapType(BootstrapContext<DimensionType> context) {
    context.register(
      RD_132211_DIM_TYPE,
      new DimensionType(
        false,
        true,
        false,
        false,
        1.0,
        0,
        256,
        256,
        BlockTags.INFINIBURN_OVERWORLD,
        1.0F,
        new MonsterSettings(ConstantInt.of(0), 0),
        Skybox.OVERWORLD,
        Type.DEFAULT,
        EnvironmentAttributeMap.EMPTY,
        HolderSet.empty(),
        Optional.empty()
      )
    );
    context.register(
      ALPHA_112_01_DIM_TYPE,
      new DimensionType(
        false,
        true,
        false,
        false,
        1.0,
        0,
        128,
        128,
        BlockTags.INFINIBURN_OVERWORLD,
        0.0F,
        new MonsterSettings(ConstantInt.of(0), 15),
        Skybox.OVERWORLD,
        Type.DEFAULT,
        EnvironmentAttributeMap.builder()
          .set(EnvironmentAttributes.SKY_COLOR, -7816193)
          .set(EnvironmentAttributes.FOG_COLOR, -1315841)
          .set(EnvironmentAttributes.CLOUD_HEIGHT, 108.0F)
          .set(EnvironmentAttributes.CLOUD_COLOR, -1)
          .build(),
        HolderSet.empty(),
        Optional.of(context.lookup(Registries.WORLD_CLOCK).getOrThrow(WorldClocks.OVERWORLD))
      )
    );
  }

  public static void bootstrapStem(BootstrapContext<LevelStem> context) {
    HolderGetter<Biome> biomeRegistry = context.lookup(Registries.BIOME);
    HolderGetter<DimensionType> dimTypes = context.lookup(Registries.DIMENSION_TYPE);
    context.register(
      RD_132211_STEM,
      new LevelStem(
        dimTypes.getOrThrow(RD_132211_DIM_TYPE),
        new RD132211ChunkGenerator(
          new FixedBiomeSource(biomeRegistry.getOrThrow(ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath("nostalgia", "rd_132211"))))
        )
      )
    );
    context.register(
      ALPHA_112_01_STEM,
      new LevelStem(
        dimTypes.getOrThrow(ALPHA_112_01_DIM_TYPE),
        new AlphaChunkGenerator(
          new FixedBiomeSource(biomeRegistry.getOrThrow(ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath("nostalgia", "alpha"))))
        )
      )
    );
  }
}
