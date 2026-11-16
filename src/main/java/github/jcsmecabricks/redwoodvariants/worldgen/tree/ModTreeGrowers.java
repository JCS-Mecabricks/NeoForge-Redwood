package github.jcsmecabricks.redwoodvariants.worldgen.tree;

import github.jcsmecabricks.redwoodvariants.RedwoodVariants;
import github.jcsmecabricks.redwoodvariants.worldgen.ModConfiguredFeatures;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.block.grower.TreeGrower;

import java.util.Optional;

public class ModTreeGrowers {
    public static final TreeGrower REDWOOD = new
            TreeGrower(RedwoodVariants.MOD_ID + ":redwood",
            WeightedList.of(ModConfiguredFeatures.REDWOOD_KEY), WeightedList.of(), WeightedList.of(), null);
}