package github.jcsmecabricks.redwoodvariants.item.custom;

import github.jcsmecabricks.redwoodvariants.block.ModBlocks;
import github.jcsmecabricks.redwoodvariants.block.custom.ModPortalBlock;
import github.jcsmecabricks.redwoodvariants.worldgen.dimension.ModDimensions;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class ResourceRealmItem extends Item {
    public ResourceRealmItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();

        if (!level.isClientSide && context.getPlayer() != null) {
            if (level.dimension().equals(ModDimensions.REDWOODDIM_LEVEL_KEY) || level.dimension().equals(Level.OVERWORLD)) {
                ModPortalBlock portalBlock = (ModPortalBlock) ModBlocks.REDWOOD_PORTAL.get();
                BlockPos[] frameChecks = {
                        pos.offset(-1, 0, 0),
                        pos.offset(1, 0, 0),
                        pos.offset(0, 1, 0),
                        pos.offset(0, 2, 0),
                        pos.offset(0, -1, 0)
                };
                for (BlockPos framePos : frameChecks) {
                    if (portalBlock.trySpawnPortal(level, framePos)) {
                        level.playSound(null, framePos, SoundEvents.PORTAL_TRIGGER, SoundSource.BLOCKS, 1.0F, 1.0F);
                        return InteractionResult.SUCCESS;
                    }
                }
            }
        }

        return InteractionResult.FAIL;
    }
}
