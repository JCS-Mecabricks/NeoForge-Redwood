package github.jcsmecabricks.redwoodvariants.worldgen.biome;

import github.jcsmecabricks.redwoodvariants.RedwoodVariants;
import net.minecraft.resources.ResourceLocation;
import terrablender.api.Regions;

public class ModTerrablender {
    public static void registerBiomes() {
        Regions.register(new ModOverworldRegion(ResourceLocation.fromNamespaceAndPath(RedwoodVariants.MOD_ID, "overworld"), 5));
    }
}