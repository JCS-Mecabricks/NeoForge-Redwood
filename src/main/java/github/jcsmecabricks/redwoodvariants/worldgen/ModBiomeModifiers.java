package github.jcsmecabricks.redwoodvariants.worldgen;

import github.jcsmecabricks.redwoodvariants.RedwoodVariants;
import github.jcsmecabricks.redwoodvariants.entity.ModEntities;
import github.jcsmecabricks.redwoodvariants.worldgen.biome.ModBiomes;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.random.Weighted;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ModBiomeModifiers {

    public static final ResourceKey<BiomeModifier> SPAWN_GRIZZLY_BEAR = registerKey("spawn_grizzly_bear");

    public static void bootstrap(BootstrapContext<BiomeModifier> context) {
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        var biomes = context.lookup(Registries.BIOME);

        context.register(SPAWN_GRIZZLY_BEAR, BiomeModifiers.AddSpawnsBiomeModifier.singleSpawn(
                HolderSet.direct(biomes.getOrThrow(Biomes.FOREST), biomes.getOrThrow(ModBiomes.REDWOOD_BIOME)),
                new Weighted<>(new MobSpawnSettings.SpawnerData(ModEntities.GRIZZLY_BEAR.get(), UniformInt.of(2, 3)), 25)));
    }

            private static ResourceKey<BiomeModifier> registerKey(String name) {
        return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, Identifier.fromNamespaceAndPath(RedwoodVariants.MOD_ID, name));
            }
}
