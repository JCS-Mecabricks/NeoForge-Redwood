package github.jcsmecabricks.redwoodvariants.datagen;

import github.jcsmecabricks.redwoodvariants.worldgen.ModBiomeModifiers;
import github.jcsmecabricks.redwoodvariants.worldgen.ModConfiguredFeatures;
import github.jcsmecabricks.redwoodvariants.worldgen.ModPlacedFeatures;
import github.jcsmecabricks.redwoodvariants.worldgen.biome.ModBiomes;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.List;
import java.util.Set;

public class ModDatapackProvider {

    public static final RegistrySetBuilder WORLD_BUILDER = new RegistrySetBuilder()
            .add(Registries.FEATURE, ModConfiguredFeatures::bootstrap)
            .add(Registries.PLACED_FEATURE, ModPlacedFeatures::bootstrap)
            .add(Registries.BIOME, ModBiomes::bootstrap)
            .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ModBiomeModifiers::bootstrap);

    public static final RegistrySetBuilder RELOADABLE_BUILDER = new RegistrySetBuilder()
            .add(Registries.LOOT_TABLE, new LootTableProvider(Set.of(), List.of(
                    new LootTableProvider.SubProviderEntry(ModBlockLootTableProvider::new, LootContextParamSets.BLOCK),
                    new LootTableProvider.SubProviderEntry(ModExtraLootProvider::new, LootContextParamSets.ALL_PARAMS))));
}
