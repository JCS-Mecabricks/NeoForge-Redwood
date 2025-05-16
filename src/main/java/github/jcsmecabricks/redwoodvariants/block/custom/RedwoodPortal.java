package github.jcsmecabricks.redwoodvariants.block.custom;

import github.jcsmecabricks.redwoodvariants.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.tags.BlockTags;
import org.apache.commons.lang3.mutable.MutableInt;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Predicate;

public class RedwoodPortal {
    private static final int MIN_WIDTH = 2;
    public static final int MAX_WIDTH = 21;
    private static final int MIN_HEIGHT = 3;
    public static final int MAX_HEIGHT = 21;

    // Predicate to test if a block is a valid frame block (e.g., Redwood Planks)
    private static final Predicate<BlockState> IS_VALID_FRAME_BLOCK = state -> state.is(ModBlocks.REDWOOD_PLANKS.get());

    private final Direction.Axis axis;
    private final Direction negativeDir;
    private final int foundPortalBlocks;
    private final BlockPos lowerCorner;
    private final int height;
    private final int width;

    private RedwoodPortal(Direction.Axis axis, int foundPortalBlocks, Direction negativeDir, BlockPos lowerCorner, int width, int height) {
        this.axis = axis;
        this.foundPortalBlocks = foundPortalBlocks;
        this.negativeDir = negativeDir;
        this.lowerCorner = lowerCorner;
        this.width = width;
        this.height = height;
    }

    public static Optional<RedwoodPortal> getNewPortal(LevelReader world, BlockPos pos, Direction.Axis firstCheckedAxis) {
        return getOrEmpty(world, pos, p -> p.isValid() && p.foundPortalBlocks == 0, firstCheckedAxis);
    }

    public static Optional<RedwoodPortal> getOrEmpty(LevelReader world, BlockPos pos, Predicate<RedwoodPortal> validator, Direction.Axis firstCheckedAxis) {
        Optional<RedwoodPortal> optional = Optional.of(getOnAxis(world, pos, firstCheckedAxis)).filter(validator);
        if (optional.isPresent()) {
            return optional;
        } else {
            Direction.Axis axis = firstCheckedAxis == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X;
            return Optional.of(getOnAxis(world, pos, axis)).filter(validator);
        }
    }

    public static RedwoodPortal getOnAxis(BlockGetter world, BlockPos pos, Direction.Axis axis) {
        Direction direction = axis == Direction.Axis.X ? Direction.WEST : Direction.SOUTH;
        BlockPos blockPos = getLowerCorner(world, direction, pos);
        if (blockPos == null) {
            return new RedwoodPortal(axis, 0, direction, pos, 0, 0);
        } else {
            int width = getValidatedWidth(world, blockPos, direction);
            if (width == 0) {
                return new RedwoodPortal(axis, 0, direction, blockPos, 0, 0);
            } else {
                MutableInt foundPortalBlocks = new MutableInt();
                int height = getHeight(world, blockPos, direction, width, foundPortalBlocks);
                return new RedwoodPortal(axis, foundPortalBlocks.getValue(), direction, blockPos, width, height);
            }
        }
    }

    @Nullable
    private static BlockPos getLowerCorner(BlockGetter world, Direction direction, BlockPos pos) {
        int minY = Math.max(world.getMinY(), pos.getY() - MAX_HEIGHT);
        BlockPos.MutableBlockPos mutablePos = pos.mutable();

        while (mutablePos.getY() > minY && validStateInsidePortal(world.getBlockState(mutablePos.below()))) {
            mutablePos.move(Direction.DOWN);
        }
        Direction opposite = direction.getOpposite();
        int width = getWidth(world, mutablePos, opposite) - 1;
        if (width < 0) return null;

        BlockPos offsetPos = mutablePos.relative(opposite, width);
        return offsetPos;

    }

    private static int getValidatedWidth(BlockGetter world, BlockPos lowerCorner, Direction negativeDir) {
        int width = getWidth(world, lowerCorner, negativeDir);
        return (width >= MIN_WIDTH && width <= MAX_WIDTH) ? width : 0;
    }

    private static int getWidth(BlockGetter world, BlockPos start, Direction dir) {
        BlockPos.MutableBlockPos mutable = start.mutable();

        for (int i = 0; i <= MAX_WIDTH; i++) {
            mutable.set(start).move(dir, i);
            BlockState state = world.getBlockState(mutable);
            if (!validStateInsidePortal(state)) {
                if (IS_VALID_FRAME_BLOCK.test(state)) {
                    return i;
                }
                break;
            }

            BlockState belowState = world.getBlockState(mutable.below());
            if (!IS_VALID_FRAME_BLOCK.test(belowState)) {
                break;
            }
        }

        return 0;
    }

    private static int getHeight(BlockGetter world, BlockPos lowerCorner, Direction negativeDir, int width, MutableInt foundPortalBlocks) {
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        int height = getPotentialHeight(world, lowerCorner, negativeDir, mutable, width, foundPortalBlocks);
        return (height >= MIN_HEIGHT && height <= MAX_HEIGHT && isHorizontalFrameValid(world, lowerCorner, negativeDir, mutable, width, height)) ? height : 0;
    }

    private static int getPotentialHeight(BlockGetter world, BlockPos lowerCorner, Direction negativeDir, BlockPos.MutableBlockPos mutable, int width, MutableInt foundPortalBlocks) {
        for (int y = 0; y < MAX_HEIGHT; y++) {
            mutable.set(lowerCorner).move(Direction.UP, y).move(negativeDir, -1);
            if (!IS_VALID_FRAME_BLOCK.test(world.getBlockState(mutable))) {
                return y;
            }

            mutable.set(lowerCorner).move(Direction.UP, y).move(negativeDir, width);
            if (!IS_VALID_FRAME_BLOCK.test(world.getBlockState(mutable))) {
                return y;
            }

            for (int x = 0; x < width; x++) {
                mutable.set(lowerCorner).move(Direction.UP, y).move(negativeDir, x);
                BlockState state = world.getBlockState(mutable);
                if (!validStateInsidePortal(state)) {
                    return y;
                }

                if (state.is(ModBlocks.REDWOOD_PORTAL.get())) {
                    foundPortalBlocks.increment();
                }
            }
        }
        return MAX_HEIGHT;
    }

    private static boolean isHorizontalFrameValid(BlockGetter world, BlockPos lowerCorner, Direction dir, BlockPos.MutableBlockPos pos, int width, int height) {
        for (int i = 0; i < width; i++) {
            pos.set(lowerCorner).move(Direction.UP, height).move(dir, i);
            if (!IS_VALID_FRAME_BLOCK.test(world.getBlockState(pos))) {
                return false;
            }
        }
        return true;
    }

    private static boolean validStateInsidePortal(BlockState state) {
        return state.isAir() || state.is(BlockTags.FIRE) || state.is(ModBlocks.REDWOOD_PORTAL.get());
    }

    public boolean isValid() {
        return this.width >= MIN_WIDTH && this.width <= MAX_WIDTH && this.height >= MIN_HEIGHT && this.height <= MAX_HEIGHT;
    }

    private static BlockPos offsetMultiple(BlockPos pos, Direction dir1, int dist1, Direction dir2, int dist2) {
        int dx = dir1.getStepX() * dist1 + dir2.getStepX() * dist2;
        int dy = dir1.getStepY() * dist1 + dir2.getStepY() * dist2;
        int dz = dir1.getStepZ() * dist1 + dir2.getStepZ() * dist2;
        return pos.offset(dx, dy, dz);
    }


    public void createPortal(LevelAccessor world) {
        BlockState portalState = ModBlocks.REDWOOD_PORTAL.get().defaultBlockState().setValue(ModPortalBlock.AXIS, this.axis);
        BlockPos.betweenClosed(
                        this.lowerCorner,
                        offsetMultiple(this.lowerCorner, Direction.UP, this.height - 1, this.negativeDir, this.width - 1)
                )
                .forEach(pos -> world.setBlock(pos, portalState, 18));

    }
    public boolean wasAlreadyValid() {
        return this.isValid() && this.foundPortalBlocks == this.width * this.height;
    }
}
