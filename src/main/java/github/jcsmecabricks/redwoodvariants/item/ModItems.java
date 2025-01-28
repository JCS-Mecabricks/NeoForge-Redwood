package github.jcsmecabricks.redwoodvariants.item;

import github.jcsmecabricks.redwoodvariants.RedwoodVariants;
import github.jcsmecabricks.redwoodvariants.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.HangingSignItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SignItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(RedwoodVariants.MOD_ID);

    public static final DeferredItem<Item> RESOURCE_REALM = ITEMS.register("resource_realm",
            () -> new Item(new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath
                            (RedwoodVariants.MOD_ID, "resource_realm")))));

    public static final DeferredItem<Item> REDWOOD_SIGN = ITEMS.register("redwood_sign",
            () -> new SignItem(ModBlocks.REDWOOD_SIGN.get(), ModBlocks.REDWOOD_WALL_SIGN.get(),
                    new Item.Properties().stacksTo(16)
                            .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath
                                    (RedwoodVariants.MOD_ID, "redwood_sign")))));
    public static final DeferredItem<Item> REDWOOD_HANGING_SIGN = ITEMS.register("redwood_hanging_sign",
            () -> new HangingSignItem(ModBlocks.REDWOOD_HANGING_SIGN.get(), ModBlocks.REDWOOD_WALL_HANGING_SIGN.get(),
                    new Item.Properties().stacksTo(16)
                            .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath
                                    (RedwoodVariants.MOD_ID, "redwood_hanging_sign")))));

    public static DeferredItem<Item> registerItem(String name, Function<Item.Properties, Item> factory, Item.Properties properties) {
        return ITEMS.register(name, () -> factory.apply(properties));
    }


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
