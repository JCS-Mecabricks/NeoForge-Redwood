package github.jcsmecabricks.redwoodvariants.worldgen.dimension;

import github.jcsmecabricks.redwoodvariants.block.ModBlocks;
import github.jcsmecabricks.redwoodvariants.block.custom.ModPortalBlock;
import net.minecraft.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.portal.PortalForcer;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Function;

public class ModTeleporter extends PortalForcer {
        public static BlockPos thisPos = BlockPos.ZERO;
        public static boolean insideDimension = true;

    public ModTeleporter(ServerLevel level) {
        super(level);
    }

    @Override
    public @NotNull Optional<BlockPos> findClosestPortalPosition(BlockPos exitPos, boolean isRedwood, WorldBorder worldBorder) {
        int searchRadius = 16;

        for (BlockPos pos : BlockPos.betweenClosed(exitPos.offset(-searchRadius, -searchRadius, -searchRadius),
                exitPos.offset(searchRadius, searchRadius, searchRadius))) {
            BlockState state = level.getBlockState(pos);

            if (state.getBlock() == ModBlocks.REDWOOD_PORTAL.get()) {
                return Optional.of(pos.immutable());
            }
        }

        return Optional.empty();
    }

    private boolean canHostFrame(BlockPos originalPos, BlockPos.MutableBlockPos offsetPos, Direction p_direction, int offsetScale) {
        Direction direction = p_direction.getClockWise();

        for(int i = -1; i < 3; ++i) {
            for(int j = -1; j < 4; ++j) {
                offsetPos.setWithOffset(originalPos, p_direction.getStepX() * i + direction.getStepX() * offsetScale, j, p_direction.getStepZ() * i + direction.getStepZ() * offsetScale);
                if (j < 0 && !this.level.getBlockState(offsetPos).canOcclude()) {
                    return false;
                }

                if (j >= 0 && !this.canPortalReplaceBlock(offsetPos)) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean canPortalReplaceBlock(BlockPos.MutableBlockPos pos) {
        BlockState blockstate = this.level.getBlockState(pos);
        return blockstate.canBeReplaced() && blockstate.getFluidState().isEmpty();
    }

    @Override
    public @NotNull Optional<BlockUtil.FoundRectangle> createPortal(BlockPos pos, Direction.Axis axis) {
        int groundY = this.level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, pos.getX(), pos.getZ());
        BlockPos groundPos = new BlockPos(pos.getX(), groundY, pos.getZ());

        BlockPos adjustedPos = groundPos.below();
        if (!this.level.getBlockState(adjustedPos).canOcclude()) {
            while (!this.level.getBlockState(adjustedPos).canOcclude() && adjustedPos.getY() > this.level.getMinY()) {
                adjustedPos = adjustedPos.below();
            }
        }

        BlockState blockstate = ModBlocks.REDWOOD_PORTAL.get().defaultBlockState().setValue(ModPortalBlock.AXIS, axis);
        this.level.setBlock(adjustedPos.above(), blockstate, 18);

        return Optional.of(new BlockUtil.FoundRectangle(adjustedPos.above().immutable(), 1, 1));
    }



    public Entity placeEntity(Entity entity, ServerLevel currentWorld, ServerLevel destinationWorld,
                              float yaw, Function<Boolean, Entity> repositionEntity) {
        entity = repositionEntity.apply(false);
        int y = 61;

        if (!insideDimension) {
            y = thisPos.getY();
        }

        BlockPos destinationPos = new BlockPos(thisPos.getX(), y, thisPos.getZ());

        int tries = 0;
        while ((destinationWorld.getBlockState(destinationPos).getBlock() != Blocks.AIR) &&
                !destinationWorld.getBlockState(destinationPos).canBeReplaced(Fluids.WATER) &&
                (destinationWorld.getBlockState(destinationPos.above()).getBlock()  != Blocks.AIR) &&
                !destinationWorld.getBlockState(destinationPos.above()).canBeReplaced(Fluids.WATER) && (tries < 25)) {
            destinationPos = destinationPos.above(2);
            tries++;
        }

        entity.setPos(destinationPos.getX(), destinationPos.getY(), destinationPos.getZ());

        if (insideDimension) {
            boolean doSetBlock = true;
            for (BlockPos checkPos : BlockPos.betweenClosed(destinationPos.below(10).west(10),
                    destinationPos.above(10).east(10))) {
                if (destinationWorld.getBlockState(checkPos).getBlock() instanceof ModPortalBlock) {
                    doSetBlock = false;
                    break;
                }
            }
            if (doSetBlock) {
                destinationWorld.setBlock(destinationPos, ModBlocks.REDWOOD_PORTAL.get().defaultBlockState(), 3);
            }
        }

        return entity;
    }
    }
