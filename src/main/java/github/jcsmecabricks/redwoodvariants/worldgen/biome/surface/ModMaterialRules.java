package github.jcsmecabricks.redwoodvariants.worldgen.biome.surface;

import github.jcsmecabricks.redwoodvariants.worldgen.biome.ModBiomes;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.material.MaterialRules;
import net.minecraft.world.level.levelgen.material.condition.MaterialCondition;
import net.minecraft.world.level.levelgen.material.rule.MaterialRule;
import net.minecraft.world.level.levelgen.placement.CaveSurface;

public class ModMaterialRules {

    private static final MaterialRule DIRT =
            makeStateRule(Blocks.DIRT);

    private static final MaterialRule GRASS_BLOCK =
            makeStateRule(Blocks.GRASS_BLOCK);

    private static final MaterialCondition ON_FLOOR = MaterialRules.stoneDepthCheck(0, false, CaveSurface.FLOOR);
    private static final MaterialCondition UNDER_FLOOR = MaterialRules.stoneDepthCheck(0, true, CaveSurface.FLOOR);
    private static final MaterialCondition DEEP_UNDER_FLOOR = MaterialRules.stoneDepthCheck(0, true, 6, CaveSurface.FLOOR);
    private static final MaterialCondition UNDER_CEILING = MaterialRules.stoneDepthCheck(0, true, CaveSurface.CEILING);

    public static MaterialRule makeRules(RegistryAccess registryAccess) {
        return MaterialRules.sequence(
                MaterialRules.ifTrue(MaterialRules.isBiome(registryAccess.lookupOrThrow(Registries.BIOME), ModBiomes.REDWOOD_BIOME),
                        MaterialRules.sequence(MaterialRules.ifTrue(UNDER_FLOOR, GRASS_BLOCK))),
                // Default to green terracotta
                MaterialRules.ifTrue(ON_FLOOR, GRASS_BLOCK)
        );
    }

    private static MaterialRule makeStateRule(Block block) {
        return MaterialRules.state(
                block.defaultBlockState()
        );
    }
}