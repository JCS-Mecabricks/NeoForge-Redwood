package github.jcsmecabricks.redwoodvariants.worldgen.biome;

import github.jcsmecabricks.redwoodvariants.RedwoodVariants;
import github.jcsmecabricks.redwoodvariants.worldgen.ModPlacedFeatures;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.ARGB;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.attribute.BackgroundMusic;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;
import terrablender.api.Regions;

public class ModBiomes {
    public static boolean redwood;
    public static final ResourceKey<Biome> REDWOOD_BIOME = ResourceKey.create(Registries.BIOME,
            Identifier.fromNamespaceAndPath(RedwoodVariants.MOD_ID, "redwood_biome"));

    public static void bootstrap(BootstrapContext<Biome> context) {
        context.register(REDWOOD_BIOME, redwoodBiome(context, redwood));
    }

    public static void registerBiomes() {
        Regions.register(new ModOverworldRegion(Identifier.fromNamespaceAndPath(RedwoodVariants.MOD_ID, "redwood_biome"), 20));
    }

    public static void globalOverworldGeneration(BiomeGenerationSettings.Builder builder) {
        BiomeDefaultFeatures.addDefaultCarversAndLakes(builder);
        BiomeDefaultFeatures.addDefaultCrystalFormations(builder);
        BiomeDefaultFeatures.addDefaultUndergroundVariety(builder);
        BiomeDefaultFeatures.addDefaultSprings(builder);
        BiomeDefaultFeatures.addSurfaceFreezing(builder);
    }

    public static Biome redwoodBiome(BootstrapContext<Biome> context, boolean redwood) {
        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();

        spawnBuilder.addSpawn(EntityTypes.WOLF, 5, UniformInt.of(4, 4));
        spawnBuilder.addSpawn(EntityTypes.FOX, 20, UniformInt.of(3, 5));

        BiomeDefaultFeatures.farmAnimals(spawnBuilder);

        BiomeGenerationSettings.Builder genBuilder =
                new BiomeGenerationSettings.Builder(
                        context.lookup(Registries.PLACED_FEATURE),
                        context.lookup(Registries.CARVER)
                );

        globalOverworldGeneration(genBuilder);
        BiomeDefaultFeatures.addDefaultOres(genBuilder);
        BiomeDefaultFeatures.addMossyStoneBlock(genBuilder);

        genBuilder.addFeature(
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModPlacedFeatures.REDWOOD_PLACED_KEY
        );

        BiomeDefaultFeatures.addFerns(genBuilder);
        BiomeDefaultFeatures.addForestFlowers(genBuilder);
        BiomeDefaultFeatures.addDefaultExtraVegetation(genBuilder, true);

        return new Biome.BiomeBuilder()
                .hasPrecipitation(false).temperature(1F).downfall(0.7F)
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .waterColor(4159204)
                        .grassColorOverride(9470285)
                        .foliageColorOverride(16262185)
                        .build())
                .mobSpawnSettings(spawnBuilder.build()).generationSettings(genBuilder.build())
                .setAttribute(EnvironmentAttributes.WATER_FOG_COLOR, ARGB.vector3fFromRGB24(12638463))
                .setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, new BackgroundMusic(SoundEvents.AMBIENT_WARPED_FOREST_MOOD))
                // ✅ VISUAL ENVIRONMENT
                .setAttribute(
                        EnvironmentAttributes.SKY_COLOR,
                        ARGB.vector3fFromRGB24(0x78A7FF)
                )
                .generationSettings(genBuilder.build())
                .build();
    }
}