package github.jcsmecabricks.redwoodvariants.worldgen.dimension;

import github.jcsmecabricks.redwoodvariants.block.ModBlocks;
import github.jcsmecabricks.redwoodvariants.block.custom.ModPortalBlock;
import github.jcsmecabricks.redwoodvariants.util.ModPoiTypes;
import net.minecraft.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.levelgen.Heightmap;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

public class ModTeleporter {
    public static final int TICKET_RADIUS = 3;
    private static final int REDWOOD_PORTAL_RADIUS = 16;
    private static final int OVERWORLD_PORTAL_RADIUS = 128;
    private static final int FRAME_HEIGHT = 5;
    private static final int FRAME_WIDTH = 4;
    private static final int FRAME_BOX = 3;
    private static final int FRAME_HEIGHT_START = -1;
    private static final int FRAME_HEIGHT_END = 4;
    private static final int FRAME_WIDTH_START = -1;
    private static final int FRAME_WIDTH_END = 3;
    private static final int FRAME_BOX_START = -1;
    private static final int FRAME_BOX_END = 2;
    private static final int NOTHING_FOUND = -1;
    protected final ServerLevel level;

    public ModTeleporter(ServerLevel level) {
        this.level = level;
    }

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


    public Optional<BlockUtil.FoundRectangle> createPortal(BlockPos pos, Direction.Axis axis) {
        Direction direction = Direction.get(Direction.AxisDirection.POSITIVE, axis);
        double d0 = -1.0F;
        BlockPos blockpos = null;
        double d1 = (double)-1.0F;
        BlockPos blockpos1 = null;
        WorldBorder worldborder = this.level.getWorldBorder();
        int i = Math.min(this.level.getMaxY(), this.level.getMinY() + this.level.getLogicalHeight() - 1);
        int j = 1;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = pos.mutable();

        for(BlockPos.MutableBlockPos blockpos$mutableblockpos1 : BlockPos.spiralAround(pos, 16, Direction.EAST, Direction.SOUTH)) {
            int k = Math.min(i, this.level.getHeight(Heightmap.Types.MOTION_BLOCKING, blockpos$mutableblockpos1.getX(), blockpos$mutableblockpos1.getZ()));
            if (worldborder.isWithinBounds(blockpos$mutableblockpos1) && worldborder.isWithinBounds(blockpos$mutableblockpos1.move(direction, 1))) {
                blockpos$mutableblockpos1.move(direction.getOpposite(), 1);

                for(int l = k; l >= this.level.getMinY(); --l) {
                    blockpos$mutableblockpos1.setY(l);
                    if (this.canPortalReplaceBlock(blockpos$mutableblockpos1)) {
                        int i1;
                        for(i1 = l; l > this.level.getMinY() && this.canPortalReplaceBlock(blockpos$mutableblockpos1.move(Direction.DOWN)); --l) {
                        }

                        if (l + 4 <= i) {
                            int j1 = i1 - l;
                            if (j1 <= 0 || j1 >= 3) {
                                blockpos$mutableblockpos1.setY(l);
                                if (this.canHostFrame(blockpos$mutableblockpos1, blockpos$mutableblockpos, direction, 0)) {
                                    double d2 = pos.distSqr(blockpos$mutableblockpos1);
                                    if (this.canHostFrame(blockpos$mutableblockpos1, blockpos$mutableblockpos, direction, -1) && this.canHostFrame(blockpos$mutableblockpos1, blockpos$mutableblockpos, direction, 1) && (d0 == (double)-1.0F || d0 > d2)) {
                                        d0 = d2;
                                        blockpos = blockpos$mutableblockpos1.immutable();
                                    }

                                    if (d0 == (double)-1.0F && (d1 == (double)-1.0F || d1 > d2)) {
                                        d1 = d2;
                                        blockpos1 = blockpos$mutableblockpos1.immutable();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (d0 == (double)-1.0F && d1 != (double)-1.0F) {
            blockpos = blockpos1;
            d0 = d1;
        }

        if (d0 == (double)-1.0F) {
            int k1 = Math.max(this.level.getMinY() - -1, 70);
            int i2 = i - 9;
            if (i2 < k1) {
                return Optional.empty();
            }

            blockpos = (new BlockPos(pos.getX() - direction.getStepX() * 1, Mth.clamp(pos.getY(), k1, i2), pos.getZ() - direction.getStepZ() * 1)).immutable();
            blockpos = worldborder.clampToBounds(blockpos);
            Direction direction1 = direction.getClockWise();

            for(int i3 = -1; i3 < 2; ++i3) {
                for(int j3 = 0; j3 < 2; ++j3) {
                    for(int k3 = -1; k3 < 3; ++k3) {
                        BlockState blockstate1 = k3 < 0 ? ModBlocks.REDWOOD_PLANKS.get().defaultBlockState() : Blocks.AIR.defaultBlockState();
                        blockpos$mutableblockpos.setWithOffset(blockpos, j3 * direction.getStepX() + i3 * direction1.getStepX(), k3, j3 * direction.getStepZ() + i3 * direction1.getStepZ());
                        this.level.setBlockAndUpdate(blockpos$mutableblockpos, blockstate1);
                    }
                }
            }
        }

        for(int l1 = -1; l1 < 3; ++l1) {
            for(int j2 = -1; j2 < 4; ++j2) {
                if (l1 == -1 || l1 == 2 || j2 == -1 || j2 == 3) {
                    blockpos$mutableblockpos.setWithOffset(blockpos, l1 * direction.getStepX(), j2, l1 * direction.getStepZ());
                    this.level.setBlock(blockpos$mutableblockpos, ModBlocks.REDWOOD_PLANKS.get().defaultBlockState(), 3);
                }
            }
        }

        BlockState blockstate = ModBlocks.REDWOOD_PORTAL.get().defaultBlockState().setValue(ModPortalBlock.AXIS, axis);

        for(int k2 = 0; k2 < 2; ++k2) {
            for(int l2 = 0; l2 < 3; ++l2) {
                blockpos$mutableblockpos.setWithOffset(blockpos, k2 * direction.getStepX(), l2, k2 * direction.getStepZ());
                this.level.setBlock(blockpos$mutableblockpos, blockstate, 18);
            }
        }

        return Optional.of(new BlockUtil.FoundRectangle(blockpos.immutable(), 2, 3));
    }

    private boolean canPortalReplaceBlock(BlockPos.MutableBlockPos pos) {
        BlockState blockstate = this.level.getBlockState(pos);
        return blockstate.canBeReplaced() && blockstate.getFluidState().isEmpty();
    }

    private boolean canHostFrame(BlockPos originalPos, BlockPos.MutableBlockPos offsetPos, Direction p_direction, int offsetScale) {
        Direction direction = p_direction.getClockWise();

        for(int i = -1; i < 3; ++i) {
            for(int j = -1; j < 4; ++j) {
                offsetPos.setWithOffset(originalPos, p_direction.getStepX() * i + direction.getStepX() * offsetScale, j, p_direction.getStepZ() * i + direction.getStepZ() * offsetScale);
                if (j < 0 && !this.level.getBlockState(offsetPos).isSolid()) {
                    return false;
                }

                if (j >= 0 && !this.canPortalReplaceBlock(offsetPos)) {
                    return false;
                }
            }
        }

        return true;
    }
}
