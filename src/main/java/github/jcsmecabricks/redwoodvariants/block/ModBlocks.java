package github.jcsmecabricks.redwoodvariants.block;

import github.jcsmecabricks.redwoodvariants.RedwoodVariants;
import github.jcsmecabricks.redwoodvariants.block.custom.*;
import github.jcsmecabricks.redwoodvariants.item.ModItems;
import github.jcsmecabricks.redwoodvariants.util.ModWoodTypes;
import github.jcsmecabricks.redwoodvariants.worldgen.tree.ModTreeGrowers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(RedwoodVariants.MOD_ID);

    public static final DeferredBlock<Block> REDWOOD_PORTAL = registerBlock("redwood_portal",
            () -> new ModPortalBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.NETHER_PORTAL)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath
                            (RedwoodVariants.MOD_ID, "redwood_portal")))
                    .noCollission()
                    .noLootTable()
                    .strength(10f, 50.0f)
                    .requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> REDWOOD_PLANKS = registerBlock("redwood_planks",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.PALE_OAK_PLANKS)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath
                            (RedwoodVariants.MOD_ID, "redwood_planks")))){
                @Override
                public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return true;
                }

                @Override
                public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 20;
                }

                @Override
                public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 5;
                }
            });

    public static final DeferredBlock<Block> REDWOOD_LOG = registerBlock("redwood_log",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath
                            (RedwoodVariants.MOD_ID, "redwood_log")))));
    public static final DeferredBlock<Block> REDWOOD_WOOD = registerBlock("redwood_wood",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath
                            (RedwoodVariants.MOD_ID, "redwood_wood")))));
    public static final DeferredBlock<Block> STRIPPED_REDWOOD_LOG = registerBlock("stripped_redwood_log",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_DARK_OAK_LOG)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath
                            (RedwoodVariants.MOD_ID, "stripped_redwood_log")))));
    public static final DeferredBlock<Block> STRIPPED_REDWOOD_WOOD = registerBlock("stripped_redwood_wood",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_DARK_OAK_WOOD)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath
                            (RedwoodVariants.MOD_ID, "stripped_redwood_wood")))));

    public static final DeferredBlock<StairBlock> REDWOOD_STAIRS = registerBlock("redwood_stairs",
            () -> new StairBlock(ModBlocks.REDWOOD_PLANKS.get().defaultBlockState(),
                    BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_STAIRS)
                            .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath
                                    (RedwoodVariants.MOD_ID, "redwood_stairs")))));
    public static final DeferredBlock<SlabBlock> REDWOOD_SLAB = registerBlock("redwood_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_SLAB)
                            .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath
                                    (RedwoodVariants.MOD_ID, "redwood_slab")))));
    public static final DeferredBlock<PressurePlateBlock> REDWOOD_PRESSURE_PLATE = registerBlock("redwood_pressure_plate",
            () -> new PressurePlateBlock(BlockSetType.OAK,
                    BlockBehaviour.Properties.ofFullCopy(Blocks.PALE_OAK_PRESSURE_PLATE)
                            .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath
                                    (RedwoodVariants.MOD_ID, "redwood_pressure_plate")))));
    public static final DeferredBlock<ButtonBlock> REDWOOD_BUTTON = registerBlock("redwood_button",
            () -> new ButtonBlock(BlockSetType.OAK,
                    20,
                    BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_BUTTON)
                            .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath
                                    (RedwoodVariants.MOD_ID, "redwood_button")))));

    public static final DeferredBlock<FenceBlock> REDWOOD_FENCE = registerBlock("redwood_fence",
            () -> new FenceBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DARK_OAK_FENCE)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath
                            (RedwoodVariants.MOD_ID, "redwood_fence")))));
    public static final DeferredBlock<FenceGateBlock> REDWOOD_FENCE_GATE = registerBlock("redwood_fence_gate",
            () -> new FenceGateBlock(WoodType.OAK, BlockBehaviour.Properties.ofFullCopy(Blocks.JUNGLE_FENCE_GATE)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath
                            (RedwoodVariants.MOD_ID, "redwood_fence_gate")))));

    public static final DeferredBlock<DoorBlock> REDWOOD_DOOR = registerBlock("redwood_door",
            () -> new DoorBlock(BlockSetType.OAK, BlockBehaviour.Properties.ofFullCopy(Blocks.DARK_OAK_DOOR)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath
                    (RedwoodVariants.MOD_ID, "redwood_door")))));
    public static final DeferredBlock<TrapDoorBlock> REDWOOD_TRAPDOOR = registerBlock("redwood_trapdoor",
            () -> new TrapDoorBlock(BlockSetType.OAK, BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_TRAPDOOR)                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath
                    (RedwoodVariants.MOD_ID, "redwood_trapdoor")))));

    public static final DeferredBlock<Block> REDWOOD_LEAVES = registerBlock("redwood_leaves",
            () -> new TintedParticleLeavesBlock(0, BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_LEAVES)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath
                            (RedwoodVariants.MOD_ID, "redwood_leaves")))){
                @Override
                public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return true;
                }

                @Override
                public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 20;
                }

                @Override
                public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 5;
                }
            });
    public static final DeferredBlock<Block> REDWOOD_SAPLING = registerBlock("redwood_sapling",
            () -> new SaplingBlock(ModTreeGrowers.REDWOOD,BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SAPLING)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath
                            (RedwoodVariants.MOD_ID, "redwood_sapling")))));

    public static final DeferredBlock<Block> REDWOOD_SIGN = BLOCKS.register("redwood_sign",
            () -> new ModStandingSignBlock(ModWoodTypes.REDWOOD, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SIGN)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath
                            (RedwoodVariants.MOD_ID, "redwood_sign")))));
    public static final DeferredBlock<Block> REDWOOD_WALL_SIGN = BLOCKS.register("redwood_wall_sign",
            () -> new ModWallSignBlock(ModWoodTypes.REDWOOD, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SIGN)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath
                            (RedwoodVariants.MOD_ID, "redwood_wall_sign")))));

    public static final DeferredBlock<Block> REDWOOD_HANGING_SIGN = BLOCKS.register("redwood_hanging_sign",
            () -> new ModHangingSignBlock(ModWoodTypes.REDWOOD, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SIGN)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath
                            (RedwoodVariants.MOD_ID, "redwood_hanging_sign")))));
    public static final DeferredBlock<Block> REDWOOD_WALL_HANGING_SIGN = BLOCKS.register("redwood_wall_hanging_sign",
            () -> new ModWallHangingSignBlock(ModWoodTypes.REDWOOD, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SIGN)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath
                            (RedwoodVariants.MOD_ID, "redwood_wall_hanging_sign")))));

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().useBlockDescriptionPrefix()
                .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(RedwoodVariants.MOD_ID, name)))));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
