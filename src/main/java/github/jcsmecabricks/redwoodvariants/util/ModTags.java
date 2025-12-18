package github.jcsmecabricks.redwoodvariants.util;

import github.jcsmecabricks.redwoodvariants.RedwoodVariants;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static class Blocks {
        public static final TagKey<Block> PORTAL_FRAME_BLOCKS
                = tag("portal_frame_blocks");

        private static TagKey<Block> tag(String name) {
            return BlockTags.create(Identifier.fromNamespaceAndPath(RedwoodVariants.MOD_ID, name));
        }
    }
}
