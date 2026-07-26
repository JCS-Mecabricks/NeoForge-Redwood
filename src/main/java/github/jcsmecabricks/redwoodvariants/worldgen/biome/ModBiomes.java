package github.jcsmecabricks.redwoodvariants.worldgen.biome;

import github.jcsmecabricks.redwoodvariants.RedwoodVariants;
import github.jcsmecabricks.redwoodvariants.worldgen.ModPlacedFeatures;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.attribute.BackgroundMusic;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;

import static net.minecraft.data.worldgen.biome.OverworldBiomes.calculateSkyColor;

public class ModBiomes {
    public static final ResourceKey<Biome> REDWOOD_BIOME = ResourceKey.create(Registries.BIOME,
            Identifier.fromNamespaceAndPath(RedwoodVariants.MOD_ID, "redwood_biome"));

    public static void boostrap(BootstrapContext<Biome> context) {
        context.register(REDWOOD_BIOME, redwoodBiome(context));
    }

    private static Biome.BiomeBuilder baseBiome(float p_457942_, float p_458031_) {
        return new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .temperature(p_457942_)
                .downfall(p_458031_)
                .setAttribute(EnvironmentAttributes.SKY_COLOR, calculateSkyColor(p_457942_))
                .specialEffects(new BiomeSpecialEffects.Builder().waterColor(4159204).build());
    }

    public static void globalOverworldGeneration(BiomeGenerationSettings.Builder builder) {
        BiomeDefaultFeatures.addDefaultCarversAndLakes(builder);
        BiomeDefaultFeatures.addDefaultCrystalFormations(builder);
        BiomeDefaultFeatures.addDefaultUndergroundVariety(builder);
        BiomeDefaultFeatures.addDefaultSprings(builder);
        BiomeDefaultFeatures.addSurfaceFreezing(builder);
    }

    public static Biome redwoodBiome(BootstrapContext<Biome> context) {
        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();

        spawnBuilder.addSpawn(MobCategory.CREATURE, 5, new MobSpawnSettings.SpawnerData(EntityTypes.WOLF, 5, 4));
        spawnBuilder.addSpawn(MobCategory.CREATURE, 6, new MobSpawnSettings.SpawnerData(EntityTypes.FOX, 3, 5));
        BiomeDefaultFeatures.farmAnimals(spawnBuilder);
        BiomeDefaultFeatures.commonSpawns(spawnBuilder);

        BiomeGenerationSettings.Builder biomeBuilder =
                new BiomeGenerationSettings.Builder(context.lookup(Registries.PLACED_FEATURE), context.lookup(Registries.CONFIGURED_CARVER));
        globalOverworldGeneration(biomeBuilder);
        BiomeDefaultFeatures.addMossyStoneBlock(biomeBuilder);
        BiomeDefaultFeatures.addMeadowVegetation(biomeBuilder);
        BiomeDefaultFeatures.addDefaultOres(biomeBuilder);
        BiomeDefaultFeatures.addExtraGold(biomeBuilder);

        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.REDWOOD_PLACED_KEY);
        BiomeDefaultFeatures.addDefaultExtraVegetation(biomeBuilder, true);

        return baseBiome(0.4F, 0.7F)
                .setAttribute(
                        EnvironmentAttributes.SKY_COLOR,
                        calculateSkyColor(0.4F)
                )
                .setAttribute(
                        EnvironmentAttributes.WATER_FOG_COLOR,
                        12638463
                )
                .setAttribute(
                        EnvironmentAttributes.BACKGROUND_MUSIC,
                        new BackgroundMusic(SoundEvents.AMBIENT_WARPED_FOREST_MOOD)
                )
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .waterColor(4159204)
                        .grassColorOverride(9470285)
                        .foliageColorOverride(16262185)
                        .build())
                .mobSpawnSettings(spawnBuilder.build())
                .generationSettings(biomeBuilder.build())
                .build();
    }
}