package net.nostalgia.world.dimension;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.attribute.EnvironmentAttributeMap;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.core.HolderSet;
import net.nostalgia.NostalgiaMod;

public class ModDimensions {
        public static final ResourceKey<Level> RD_132211_LEVEL_KEY = ResourceKey.create(Registries.DIMENSION,
                        Identifier.fromNamespaceAndPath(NostalgiaMod.MOD_ID, "rd_132211"));
        public static final ResourceKey<DimensionType> RD_132211_DIM_TYPE = ResourceKey.create(
                        Registries.DIMENSION_TYPE,
                        Identifier.fromNamespaceAndPath(NostalgiaMod.MOD_ID, "rd_132211"));
        public static final ResourceKey<LevelStem> RD_132211_STEM = ResourceKey.create(Registries.LEVEL_STEM,
                        Identifier.fromNamespaceAndPath(NostalgiaMod.MOD_ID, "rd_132211"));

        public static final ResourceKey<Level> ALPHA_112_01_LEVEL_KEY = ResourceKey.create(Registries.DIMENSION,
                        Identifier.fromNamespaceAndPath(NostalgiaMod.MOD_ID, "alpha_112_01"));
        public static final ResourceKey<DimensionType> ALPHA_112_01_DIM_TYPE = ResourceKey.create(
                        Registries.DIMENSION_TYPE,
                        Identifier.fromNamespaceAndPath(NostalgiaMod.MOD_ID, "alpha_112_01"));
        public static final ResourceKey<LevelStem> ALPHA_112_01_STEM = ResourceKey.create(Registries.LEVEL_STEM,
                        Identifier.fromNamespaceAndPath(NostalgiaMod.MOD_ID, "alpha_112_01"));

        public static void bootstrapType(BootstrapContext<DimensionType> context) {
                
                context.register(RD_132211_DIM_TYPE, new DimensionType(
                                false, 
                                true, 
                                false, 
                                false, 
                                1.0, 
                                0, 
                                256, 
                                256, 
                                BlockTags.INFINIBURN_OVERWORLD, 
                                1.0f, 
                                new DimensionType.MonsterSettings(ConstantInt.of(0), 0), 
                                DimensionType.Skybox.OVERWORLD, 
                                net.minecraft.world.level.CardinalLighting.Type.DEFAULT, 
                                net.minecraft.world.attribute.EnvironmentAttributeMap.EMPTY, 
                                net.minecraft.core.HolderSet.empty(), java.util.Optional.<net.minecraft.core.Holder<net.minecraft.world.clock.WorldClock>>empty()
                ));

                context.register(ALPHA_112_01_DIM_TYPE, new DimensionType(
                                false, 
                                true, 
                                false, 
                                false, 
                                1.0, 
                                0, 
                                128, 
                                128, 
                                BlockTags.INFINIBURN_OVERWORLD, 
                                0.0f, 
                                new DimensionType.MonsterSettings(ConstantInt.of(0), 15), 
                                DimensionType.Skybox.OVERWORLD, 
                                net.minecraft.world.level.CardinalLighting.Type.DEFAULT, 
                                EnvironmentAttributeMap.builder()
                                                .set(EnvironmentAttributes.SKY_COLOR, 0xFF88BBFF) 
                                                .set(EnvironmentAttributes.FOG_COLOR, 0xFFEBEBFF) 
                                .set(EnvironmentAttributes.CLOUD_HEIGHT, 108.0f)
                                .set(EnvironmentAttributes.CLOUD_COLOR, 0xFFFFFFFF)
                                .build(), 
                HolderSet.empty(), java.util.Optional.of(context.lookup(Registries.WORLD_CLOCK).getOrThrow(net.minecraft.world.clock.WorldClocks.OVERWORLD))
        ));
    }

        public static void bootstrapStem(BootstrapContext<LevelStem> context) {
                var biomeRegistry = context.lookup(Registries.BIOME);
                var dimTypes = context.lookup(Registries.DIMENSION_TYPE);

                context.register(RD_132211_STEM, new LevelStem(
                                dimTypes.getOrThrow(RD_132211_DIM_TYPE),
                                new net.nostalgia.world.gen.RD132211ChunkGenerator(
                                                new net.minecraft.world.level.biome.FixedBiomeSource(
                                                                biomeRegistry.getOrThrow(
                                                                                net.minecraft.world.level.biome.Biomes.PLAINS)))));

                context.register(ALPHA_112_01_STEM, new LevelStem(
                                dimTypes.getOrThrow(ALPHA_112_01_DIM_TYPE),
                                new net.nostalgia.world.gen.AlphaChunkGenerator(
                                                new net.minecraft.world.level.biome.FixedBiomeSource(
                                                                biomeRegistry.getOrThrow(
                                                                                ResourceKey.create(Registries.BIOME,
                                                                                                Identifier.fromNamespaceAndPath(
                                                                                                                "nostalgia",
                                                                                                                "alpha")))))));
        }
}
