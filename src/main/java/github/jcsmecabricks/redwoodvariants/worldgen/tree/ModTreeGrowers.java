package github.jcsmecabricks.redwoodvariants.worldgen.tree;

import github.jcsmecabricks.redwoodvariants.RedwoodVariants;
import github.jcsmecabricks.redwoodvariants.worldgen.ModConfiguredFeatures;
import net.minecraft.world.level.block.grower.TreeGrower;

import java.util.Optional;

public class ModTreeGrowers {
    public static final TreeGrower REDWOOD = new TreeGrower(RedwoodVariants.MOD_ID + ":redwood",
            Optional.empty(), Optional.of(ModConfiguredFeatures.REDWOOD_KEY), Optional.empty());
}