package github.jcsmecabricks.redwoodvariants.item;

import github.jcsmecabricks.redwoodvariants.RedwoodVariants;
import github.jcsmecabricks.redwoodvariants.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, RedwoodVariants.MOD_ID);

    public static final Supplier<CreativeModeTab> REDWOOD_TAB = CREATIVE_MODE_TAB.register("redwood_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModBlocks.REDWOOD_LOG))
                    .title(Component.translatable("itemGroup.redwoodvariants.redwood_group"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModBlocks.STRIPPED_REDWOOD_WOOD.get());
                        output.accept(ModBlocks.REDWOOD_WOOD.get());
                        output.accept(ModBlocks.STRIPPED_REDWOOD_LOG.get());
                        output.accept(ModBlocks.REDWOOD_LOG.get());
                        output.accept(ModBlocks.REDWOOD_LEAVES.get());
                        output.accept(ModBlocks.REDWOOD_PORTAL.get());
                        output.accept(ModBlocks.REDWOOD_PLANKS.get());
                        output.accept(ModBlocks.REDWOOD_SAPLING.get());
                        output.accept(ModBlocks.REDWOOD_STAIRS.get());
                        output.accept(ModBlocks.REDWOOD_SLAB.get());
                        output.accept(ModBlocks.REDWOOD_PRESSURE_PLATE.get());
                        output.accept(ModBlocks.REDWOOD_BUTTON.get());
                        output.accept(ModBlocks.REDWOOD_FENCE.get());
                        output.accept(ModBlocks.REDWOOD_FENCE_GATE.get());
                        output.accept(ModBlocks.REDWOOD_DOOR.get());
                        output.accept(ModItems.REDWOOD_HANGING_SIGN.get());
                        output.accept(ModItems.RESOURCE_REALM.get());
                        output.accept(ModItems.REDWOOD_SIGN.get());
                        output.accept(ModBlocks.REDWOOD_TRAPDOOR.get());
                    }).build());



    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
