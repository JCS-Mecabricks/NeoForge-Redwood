package github.jcsmecabricks.redwoodvariants;

import github.jcsmecabricks.redwoodvariants.datagen.*;
import github.jcsmecabricks.redwoodvariants.worldgen.ModBiomeModifiers;
import github.jcsmecabricks.redwoodvariants.worldgen.ModConfiguredFeatures;
import github.jcsmecabricks.redwoodvariants.worldgen.ModPlacedFeatures;
import github.jcsmecabricks.redwoodvariants.worldgen.biome.ModBiomes;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataRegistryEntriesEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.List;
import java.util.Set;

@EventBusSubscriber(modid = RedwoodVariants.MOD_ID)
public class RedwoodVariantsDataGen {

    @SubscribeEvent
    public static void gatherDataRegistryEntries(GatherDataRegistryEntriesEvent event) {

        event.gatherFor(RedwoodVariants.MOD_ID, Identifier.DEFAULT_NAMESPACE)

                // World generation
                .add(Registries.BIOME, ModBiomes::bootstrap)
                .add(Registries.FEATURE, ModConfiguredFeatures::bootstrap)
                .add(Registries.PLACED_FEATURE, ModPlacedFeatures::bootstrap)
                .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ModBiomeModifiers::bootstrap)

                // Loot tables
                .add(Registries.LOOT_TABLE, new LootTableProvider(Set.of(), List.of(
                        new LootTableProvider.SubProviderEntry(
                                ModBlockLootTableProvider::new,
                                LootContextParamSets.BLOCK
                        ),
                        new LootTableProvider.SubProviderEntry(
                                ModExtraLootProvider::new,
                                LootContextParamSets.ALL_PARAMS
                        )
                )));
    }
}