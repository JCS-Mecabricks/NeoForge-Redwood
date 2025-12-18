package github.jcsmecabricks.redwoodvariants.worldgen.biome;

import github.jcsmecabricks.redwoodvariants.RedwoodVariants;
import net.minecraft.resources.Identifier;
import terrablender.api.Regions;

public class ModTerrablender {
    public static void registerBiomes() {
        Regions.register(new ModOverworldRegion(Identifier.fromNamespaceAndPath(RedwoodVariants.MOD_ID, "overworld"), 5));
    }
}