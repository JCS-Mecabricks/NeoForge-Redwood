package github.jcsmecabricks.redwoodvariants.worldgen.dimension;

import com.mojang.datafixers.util.Pair;
import github.jcsmecabricks.redwoodvariants.RedwoodVariants;
import github.jcsmecabricks.redwoodvariants.worldgen.biome.ModBiomes;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ARGB;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.attribute.EnvironmentAttributeMap;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.level.CardinalLighting;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.timeline.Timeline;

import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;

public class ModDimensions {
    public static final ResourceKey<LevelStem> REDWOODDIM_KEY = ResourceKey.create(Registries.LEVEL_STEM,
            Identifier.fromNamespaceAndPath(RedwoodVariants.MOD_ID, "redwooddim"));
    public static final ResourceKey<Level> REDWOODDIM_LEVEL_KEY = ResourceKey.create(Registries.DIMENSION,
            Identifier.fromNamespaceAndPath(RedwoodVariants.MOD_ID, "redwooddim"));
    public static final ResourceKey<DimensionType> REDWOOD_DIM_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE,
            Identifier.fromNamespaceAndPath(RedwoodVariants.MOD_ID, "redwooddim_type"));

    static EnvironmentAttributeMap attributes =
            EnvironmentAttributeMap.builder()
                    .set(EnvironmentAttributes.SKY_COLOR, ARGB.vector3fFromRGB24(7907327))
                    .set(EnvironmentAttributes.FOG_COLOR, ARGB.vector3fFromRGB24(12638463))
                    .set(EnvironmentAttributes.WATER_FOG_COLOR, ARGB.vector3fFromRGB24(4159204))
                    .build();

    public static void bootstrapType(BootstrapContext<DimensionType> context) {
        HolderGetter<DimensionType> dimTypes = context.lookup(Registries.DIMENSION_TYPE);

        HolderSet<Timeline> overworldTimelines =
                dimTypes.getOrThrow(BuiltinDimensionTypes.OVERWORLD)
                        .value()
                        .timelines();

        HolderSet<Block> infiniburn =
                context.lookup(Registries.BLOCK)
                        .getOrThrow(BlockTags.INFINIBURN_OVERWORLD);


        context.register(REDWOOD_DIM_TYPE, new DimensionType(
                false,   // fixed time
                true,// hasSkyLight
                false,
                false,
                1.0,
                -64,
                256,
                256,
                infiniburn,
                0.0f,

                new DimensionType.MonsterSettings(
                        UniformInt.of(15, 15),
                        0
                ),

                DimensionType.Skybox.OVERWORLD,
                CardinalLighting.Type.DEFAULT,
                attributes,
                HolderSet.empty(),
                Optional.empty() // ✅ THIS replaces RegistryEntryList.empty()
        ));
    }

    public static void bootstrapStem(BootstrapContext<LevelStem> context) {
        HolderGetter<Biome> biomeRegistry = context.lookup(Registries.BIOME);
        HolderGetter<DimensionType> dimTypes = context.lookup(Registries.DIMENSION_TYPE);
        HolderGetter<NoiseGeneratorSettings> noiseGenSettings = context.lookup(Registries.NOISE_SETTINGS);

        NoiseBasedChunkGenerator wrappedChunkGenerator = new NoiseBasedChunkGenerator(
                new FixedBiomeSource(biomeRegistry.getOrThrow(ModBiomes.REDWOOD_BIOME)),
                noiseGenSettings.getOrThrow(NoiseGeneratorSettings.AMPLIFIED));

        NoiseBasedChunkGenerator noiseBasedChunkGenerator = new NoiseBasedChunkGenerator(
                MultiNoiseBiomeSource.createFromList(
                        new Climate.ParameterList<>(List.of(Pair.of(
                                        Climate.parameters(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F), biomeRegistry.getOrThrow(ModBiomes.REDWOOD_BIOME)),
                                Pair.of(
                                        Climate.parameters(0.1F, 0.2F, 0.0F, 0.2F, 0.0F, 0.0F, 0.0F), biomeRegistry.getOrThrow(Biomes.OLD_GROWTH_SPRUCE_TAIGA)),
                                Pair.of(
                                        Climate.parameters(0.3F, 0.6F, 0.1F, 0.1F, 0.0F, 0.0F, 0.0F), biomeRegistry.getOrThrow(Biomes.TAIGA)),
                                Pair.of(
                                        Climate.parameters(0.4F, 0.3F, 0.2F, 0.1F, 0.0F, 0.0F, 0.0F), biomeRegistry.getOrThrow(Biomes.OLD_GROWTH_PINE_TAIGA))

                        ))),
                noiseGenSettings.getOrThrow(NoiseGeneratorSettings.AMPLIFIED));

        LevelStem stem = new LevelStem(dimTypes.getOrThrow(ModDimensions.REDWOOD_DIM_TYPE), noiseBasedChunkGenerator);

        context.register(REDWOODDIM_KEY, stem);
    }
}