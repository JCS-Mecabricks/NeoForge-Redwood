package github.jcsmecabricks.redwoodvariants.util;

import github.jcsmecabricks.redwoodvariants.RedwoodVariants;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;

public class ModWoodTypes {
        public static final WoodType REDWOOD = WoodType.register(new WoodType(RedwoodVariants.MOD_ID + ":redwood", BlockSetType.OAK));
    }
