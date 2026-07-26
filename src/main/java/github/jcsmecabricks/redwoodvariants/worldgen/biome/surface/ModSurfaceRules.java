package github.jcsmecabricks.redwoodvariants.worldgen.biome.surface;

import github.jcsmecabricks.redwoodvariants.worldgen.biome.ModBiomes;
import net.minecraft.core.HolderGetter;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.SurfaceRules;

public class ModSurfaceRules {
    private static final SurfaceRules.RuleSource DIRT = makeStateRule(Blocks.DIRT);
    private static final SurfaceRules.RuleSource GRASS_BLOCK = makeStateRule(Blocks.GRASS_BLOCK);


    public static SurfaceRules.RuleSource makeRules(
            HolderGetter<Biome> biomes
    ) {
        SurfaceRules.ConditionSource isAtOrAboveWaterLevel =
                SurfaceRules.waterBlockCheck(-1, 0);

        SurfaceRules.RuleSource grassSurface =
                SurfaceRules.sequence(
                        SurfaceRules.ifTrue(
                                isAtOrAboveWaterLevel,
                                GRASS_BLOCK
                        ),
                        DIRT
                );

        return SurfaceRules.sequence(

                // Redwood biome surface rules
                SurfaceRules.ifTrue(
                        SurfaceRules.isBiome(
                                biomes,
                                ModBiomes.REDWOOD_BIOME
                        ),
                        SurfaceRules.sequence(
                                SurfaceRules.ifTrue(
                                        SurfaceRules.ON_FLOOR,
                                        GRASS_BLOCK
                                ),
                                SurfaceRules.ifTrue(
                                        SurfaceRules.ON_CEILING,
                                        GRASS_BLOCK
                                )
                        )
                ),

                // Default grass and dirt surface
                SurfaceRules.ifTrue(
                        SurfaceRules.ON_FLOOR,
                        grassSurface
                )
        );
    }

    private static SurfaceRules.RuleSource makeStateRule(Block block) {
        return SurfaceRules.state(block.defaultBlockState());
    }
}