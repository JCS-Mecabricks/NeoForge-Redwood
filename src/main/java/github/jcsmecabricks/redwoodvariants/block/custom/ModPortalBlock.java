package github.jcsmecabricks.redwoodvariants.block.custom;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.MapCodec;
import github.jcsmecabricks.redwoodvariants.block.ModBlocks;
import github.jcsmecabricks.redwoodvariants.worldgen.dimension.ModDimensions;
import github.jcsmecabricks.redwoodvariants.worldgen.dimension.ModTeleporter;
import net.minecraft.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.portal.PortalShape;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class ModPortalBlock extends Block implements Portal {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final MapCodec<ModPortalBlock> CODEC = simpleCodec(ModPortalBlock::new);
    public static final EnumProperty<Direction.Axis> AXIS;
    private static final Map<Direction.Axis, VoxelShape> SHAPES;

    public MapCodec<ModPortalBlock> codec() {
        return CODEC;
    }

    public ModPortalBlock(BlockBehaviour.Properties p_54909_) {
        super(p_54909_);
        this.registerDefaultState(this.stateDefinition.any().setValue(AXIS, Direction.Axis.X));
    }

    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES.get(state.getValue(AXIS));
    }

    protected VoxelShape getEntityInsideCollisionShape(BlockState p_400288_, BlockGetter p_400305_, BlockPos p_400030_, Entity p_399514_) {
        return p_400288_.getShape(p_400305_, p_400030_);
    }

    protected void randomTick(BlockState p_221799_, ServerLevel p_221800_, BlockPos p_221801_, RandomSource p_221802_) {
        if (p_221800_.dimensionType().natural() && p_221800_.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING) && p_221802_.nextInt(2000) < p_221800_.getDifficulty().getId() && p_221800_.anyPlayerCloseEnoughForSpawning(p_221801_)) {
            while(p_221800_.getBlockState(p_221801_).is(this)) {
                p_221801_ = p_221801_.below();
            }

            if (p_221800_.getBlockState(p_221801_).isValidSpawn(p_221800_, p_221801_, EntityType.ZOMBIFIED_PIGLIN)) {
                Entity entity = EntityType.ZOMBIFIED_PIGLIN.spawn(p_221800_, p_221801_.above(), EntitySpawnReason.STRUCTURE);
                if (entity != null) {
                    entity.setPortalCooldown();
                    Entity entity1 = entity.getVehicle();
                    if (entity1 != null) {
                        entity1.setPortalCooldown();
                    }
                }
            }
        }

    }

    protected BlockState updateShape(BlockState p_54928_, LevelReader p_374413_, ScheduledTickAccess p_374339_, BlockPos p_54932_, Direction p_54929_, BlockPos p_54933_, BlockState p_54930_, RandomSource p_374242_) {
        Direction.Axis direction$axis = p_54929_.getAxis();
        Direction.Axis direction$axis1 = p_54928_.getValue(AXIS);
        boolean flag = direction$axis1 != direction$axis && direction$axis.isHorizontal();
        return !flag && !p_54930_.is(this) && !PortalShape.findAnyShape(p_374413_, p_54932_, direction$axis1).isComplete() ? Blocks.AIR.defaultBlockState() : super.updateShape(p_54928_, p_374413_, p_374339_, p_54932_, p_54929_, p_54933_, p_54930_, p_374242_);
    }

    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier p_405383_) {
        if (entity.canUsePortal(false)) {
            entity.setAsInsidePortal(this, pos);
        }

    }

    public int getPortalTransitionTime(ServerLevel p_350689_, Entity p_350280_) {
        int var10000;
        if (p_350280_ instanceof Player player) {
            var10000 = Math.max(0, p_350689_.getGameRules().getInt(player.getAbilities().invulnerable ? GameRules.RULE_PLAYERS_NETHER_PORTAL_CREATIVE_DELAY : GameRules.RULE_PLAYERS_NETHER_PORTAL_DEFAULT_DELAY));
        } else {
            var10000 = 0;
        }

        return var10000;
    }

    @javax.annotation.Nullable
    public TeleportTransition getPortalDestination(ServerLevel p_350444_, Entity p_350334_, BlockPos p_350764_) {
        ResourceKey<Level> resourcekey = p_350444_.dimension() == ModDimensions.REDWOODDIM_LEVEL_KEY ? Level.OVERWORLD : ModDimensions.REDWOODDIM_LEVEL_KEY;
        ServerLevel serverlevel = p_350444_.getServer().getLevel(resourcekey);
        if (serverlevel == null) {
            return null;
        } else {
            boolean flag = serverlevel.dimension() == ModDimensions.REDWOODDIM_LEVEL_KEY;
            WorldBorder worldborder = serverlevel.getWorldBorder();
            double d0 = DimensionType.getTeleportationScale(p_350444_.dimensionType(), serverlevel.dimensionType());
            BlockPos blockpos = worldborder.clampToBounds(p_350334_.getX() * d0, p_350334_.getY(), p_350334_.getZ() * d0);
            return this.getExitPortal(serverlevel, p_350334_, p_350764_, blockpos, flag, worldborder);
        }
    }

    @Nullable
    private TeleportTransition getExitPortal(ServerLevel level, Entity entity, BlockPos pos, BlockPos exitPos, boolean isRedwood, WorldBorder worldBorder) {
        ModTeleporter modTeleporter = new ModTeleporter(level);
        Optional<BlockPos> optional = modTeleporter.findClosestPortalPosition(exitPos, isRedwood, worldBorder);
        BlockUtil.FoundRectangle blockutil$foundrectangle;
        TeleportTransition.PostTeleportTransition teleporttransition$postteleporttransition;
        if (optional.isPresent()) {
            BlockPos blockpos = optional.get();
            BlockState blockstate = level.getBlockState(blockpos);
            blockutil$foundrectangle = BlockUtil.getLargestRectangleAround(blockpos, blockstate.getValue(BlockStateProperties.HORIZONTAL_AXIS), 21, Direction.Axis.Y, 21, (p_351970_) -> level.getBlockState(p_351970_) == blockstate);
            teleporttransition$postteleporttransition = TeleportTransition.PLAY_PORTAL_SOUND.then((p_351967_) -> p_351967_.placePortalTicket(blockpos));
        } else {
            Direction.Axis direction$axis = entity.level().getBlockState(pos).getOptionalValue(AXIS).orElse(Direction.Axis.X);
            Optional<BlockUtil.FoundRectangle> optional1 = modTeleporter.createPortal(exitPos, direction$axis);
            if (optional1.isEmpty()) {
                LOGGER.error("Unable to create a portal, likely target out of worldborder");
                return null;
            }

            blockutil$foundrectangle = optional1.get();
            teleporttransition$postteleporttransition = TeleportTransition.PLAY_PORTAL_SOUND.then(TeleportTransition.PLACE_PORTAL_TICKET);
        }

        return getDimensionTransitionFromExit(entity, pos, blockutil$foundrectangle, level, teleporttransition$postteleporttransition);
    }

    private static TeleportTransition getDimensionTransitionFromExit(Entity entity, BlockPos pos, BlockUtil.FoundRectangle rectangle, ServerLevel level, TeleportTransition.PostTeleportTransition postTeleportTransition) {
        BlockState blockstate = entity.level().getBlockState(pos);
        Direction.Axis direction$axis;
        Vec3 vec3;
        if (blockstate.hasProperty(BlockStateProperties.HORIZONTAL_AXIS)) {
            direction$axis = blockstate.getValue(BlockStateProperties.HORIZONTAL_AXIS);
            BlockUtil.FoundRectangle blockutil$foundrectangle = BlockUtil.getLargestRectangleAround(pos, direction$axis, 21, Direction.Axis.Y, 21, (p_351016_) -> entity.level().getBlockState(p_351016_) == blockstate);
            vec3 = entity.getRelativePortalPosition(direction$axis, blockutil$foundrectangle);
        } else {
            direction$axis = Direction.Axis.X;
            vec3 = new Vec3(0.5F, 0.0F, 0.0F);
        }

        return createDimensionTransition(level, rectangle, direction$axis, vec3, entity, postTeleportTransition);
    }

    private static TeleportTransition createDimensionTransition(ServerLevel level, BlockUtil.FoundRectangle rectangle, Direction.Axis axis, Vec3 offset, Entity entity, TeleportTransition.PostTeleportTransition postTeleportTransition) {
        BlockPos blockpos = rectangle.minCorner;
        BlockState blockstate = level.getBlockState(blockpos);
        Direction.Axis direction$axis = blockstate.getOptionalValue(BlockStateProperties.HORIZONTAL_AXIS).orElse(Direction.Axis.X);
        double d0 = rectangle.axis1Size;
        double d1 = rectangle.axis2Size;
        EntityDimensions entitydimensions = entity.getDimensions(entity.getPose());
        int i = axis == direction$axis ? 0 : 90;
        double d2 = (double)entitydimensions.width() / (double)2.0F + (d0 - (double)entitydimensions.width()) * offset.x();
        double d3 = (d1 - (double)entitydimensions.height()) * offset.y();
        double d4 = (double)0.5F + offset.z();
        boolean flag = direction$axis == Direction.Axis.X;
        Vec3 vec3 = new Vec3((double)blockpos.getX() + (flag ? d2 : d4), (double)blockpos.getY() + d3, (double)blockpos.getZ() + (flag ? d4 : d2));
        Vec3 vec31 = PortalShape.findCollisionFreePosition(vec3, level, entity, entitydimensions);
        return new TeleportTransition(level, vec31, Vec3.ZERO, (float)i, 0.0F, Relative.union(new Set[]{Relative.DELTA, Relative.ROTATION}), postTeleportTransition);
    }

    public Portal.Transition getLocalTransition() {
        return Transition.CONFUSION;
    }

    public void animateTick(BlockState p_221794_, Level p_221795_, BlockPos p_221796_, RandomSource p_221797_) {
        if (p_221797_.nextInt(100) == 0) {
            p_221795_.playLocalSound((double)p_221796_.getX() + (double)0.5F, (double)p_221796_.getY() + (double)0.5F, (double)p_221796_.getZ() + (double)0.5F, SoundEvents.PORTAL_AMBIENT, SoundSource.BLOCKS, 0.5F, p_221797_.nextFloat() * 0.4F + 0.8F, false);
        }

        for(int i = 0; i < 4; ++i) {
            double d0 = (double)p_221796_.getX() + p_221797_.nextDouble();
            double d1 = (double)p_221796_.getY() + p_221797_.nextDouble();
            double d2 = (double)p_221796_.getZ() + p_221797_.nextDouble();
            double d3 = ((double)p_221797_.nextFloat() - (double)0.5F) * (double)0.5F;
            double d4 = ((double)p_221797_.nextFloat() - (double)0.5F) * (double)0.5F;
            double d5 = ((double)p_221797_.nextFloat() - (double)0.5F) * (double)0.5F;
            int j = p_221797_.nextInt(2) * 2 - 1;
            if (!p_221795_.getBlockState(p_221796_.west()).is(this) && !p_221795_.getBlockState(p_221796_.east()).is(this)) {
                d0 = (double)p_221796_.getX() + (double)0.5F + (double)0.25F * (double)j;
                d3 = (double)(p_221797_.nextFloat() * 2.0F * (float)j);
            } else {
                d2 = (double)p_221796_.getZ() + (double)0.5F + (double)0.25F * (double)j;
                d5 = (double)(p_221797_.nextFloat() * 2.0F * (float)j);
            }
        }

    }

    public boolean trySpawnPortal(Level level, BlockPos pos) {
        if (!(level instanceof ServerLevel serverLevel)) return false;
        Optional<RedwoodPortal> optionalPortal = RedwoodPortal.getNewPortal(serverLevel, pos, Direction.Axis.X);
        if (optionalPortal.isPresent()) {
            RedwoodPortal portal = optionalPortal.get();
            if (!portal.wasAlreadyValid()) {
                portal.createPortal(level);
                return true;
            }
        }

        return false;
    }


    protected ItemStack getCloneItemStack(LevelReader p_304402_, BlockPos p_54912_, BlockState p_54913_, boolean p_386478_) {
        return ItemStack.EMPTY;
    }

    protected BlockState rotate(BlockState state, Rotation rot) {
        switch (rot) {
            case COUNTERCLOCKWISE_90:
            case CLOCKWISE_90:
                switch (state.getValue(AXIS)) {
                    case X -> {
                        return state.setValue(AXIS, Direction.Axis.Z);
                    }
                    case Z -> {
                        return state.setValue(AXIS, Direction.Axis.X);
                    }
                    default -> {
                        return state;
                    }
                }
            default:
                return state;
        }
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{AXIS});
    }

    static {
        AXIS = BlockStateProperties.HORIZONTAL_AXIS;
        SHAPES = Shapes.rotateHorizontalAxis(Block.column((double)4.0F, (double)16.0F, (double)0.0F, (double)16.0F));
    }
}