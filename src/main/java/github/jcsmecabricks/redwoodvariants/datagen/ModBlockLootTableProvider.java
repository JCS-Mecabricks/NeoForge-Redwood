package github.jcsmecabricks.redwoodvariants.datagen;

import github.jcsmecabricks.redwoodvariants.block.ModBlocks;
import github.jcsmecabricks.redwoodvariants.item.ModItems;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.Set;

public class ModBlockLootTableProvider extends BlockLootSubProvider {
    public ModBlockLootTableProvider(LootTableSubProvider.Context context) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), context);
    }

    @Override
    protected void generate() {
        dropSelf(ModBlocks.REDWOOD_STAIRS.get());
        dropSelf(ModBlocks.REDWOOD_PRESSURE_PLATE.get());
        dropSelf(ModBlocks.REDWOOD_SAPLING.get());
        dropSelf(ModBlocks.REDWOOD_BUTTON.get());
        dropSelf(ModBlocks.REDWOOD_TRAPDOOR.get());
        dropSelf(ModBlocks.REDWOOD_FENCE.get());
        add(ModBlocks.REDWOOD_LEAVES.get(), createLeavesDrops(ModBlocks.REDWOOD_LEAVES.get(), ModBlocks.REDWOOD_SAPLING.get(), NORMAL_LEAVES_SAPLING_CHANCES));
        dropSelf(ModBlocks.REDWOOD_FENCE_GATE.get());
        dropSelf(ModBlocks.REDWOOD_PLANKS.get());
        dropSelf(ModBlocks.REDWOOD_LOG.get());
        dropSelf(ModBlocks.REDWOOD_WOOD.get());
        dropSelf(ModBlocks.STRIPPED_REDWOOD_WOOD.get());
        dropSelf(ModBlocks.STRIPPED_REDWOOD_LOG.get());
        add(ModBlocks.REDWOOD_SLAB.get(), createSlabItemTable(ModBlocks.REDWOOD_SLAB.get()));
        add(ModBlocks.REDWOOD_DOOR.get(), createDoorTable(ModBlocks.REDWOOD_DOOR.get()));

        // Regular sign
        add(
                ModBlocks.REDWOOD_SIGN.get(),
                createSingleItemTable(ModItems.REDWOOD_SIGN.get())
        );

        add(
                ModBlocks.REDWOOD_WALL_SIGN.get(),
                createSingleItemTable(ModItems.REDWOOD_SIGN.get())
        );

        // Hanging sign
        add(
                ModBlocks.REDWOOD_HANGING_SIGN.get(),
                createSingleItemTable(ModItems.REDWOOD_HANGING_SIGN.get())
        );

        add(
                ModBlocks.REDWOOD_WALL_HANGING_SIGN.get(),
                createSingleItemTable(ModItems.REDWOOD_HANGING_SIGN.get())
        );
}

@Override
protected Iterable<Block> getKnownBlocks() {
    return ModBlocks.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }
}