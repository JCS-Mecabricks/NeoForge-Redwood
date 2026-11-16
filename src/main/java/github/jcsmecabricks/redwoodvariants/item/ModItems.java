package github.jcsmecabricks.redwoodvariants.item;

import github.jcsmecabricks.redwoodvariants.RedwoodVariants;
import github.jcsmecabricks.redwoodvariants.block.ModBlocks;
import github.jcsmecabricks.redwoodvariants.entity.ModEntities;
import github.jcsmecabricks.redwoodvariants.item.custom.ModBoatItem;
import github.jcsmecabricks.redwoodvariants.item.custom.ModChestBoatItem;
import github.jcsmecabricks.redwoodvariants.item.custom.ResourceRealmItem;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.TypedEntityData;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(RedwoodVariants.MOD_ID);

    public static final DeferredItem<Item> RESOURCE_REALM = ITEMS.register("resource_realm",
            () -> new ResourceRealmItem(new Item.Properties()
                    .stacksTo(1)
                    .setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath
                            (RedwoodVariants.MOD_ID, "resource_realm")))));

    public static final DeferredItem<Item> REDWOOD_SIGN = ITEMS.registerItem("redwood_sign",
            properties -> new StandingAndWallBlockItem(ModBlocks.REDWOOD_SIGN.get(), ModBlocks.REDWOOD_WALL_SIGN.get(), Direction.DOWN, properties.stacksTo(1)));

    public static final DeferredItem<Item> REDWOOD_HANGING_SIGN = ITEMS.registerItem("redwood_hanging_sign",
            properties -> new HangingSignItem(ModBlocks.REDWOOD_HANGING_SIGN.get(), ModBlocks.REDWOOD_WALL_HANGING_SIGN.get(),
                    properties.stacksTo(16)));

    public static final DeferredItem<Item> GRIZZLY_SPAWN_EGG = ITEMS.register("grizzly_spawn_egg", () ->
            new SpawnEggItem(new Item.Properties()
                    .component(
                            DataComponents.ENTITY_DATA,
                            TypedEntityData.of(ModEntities.GRIZZLY_BEAR.get(), new CompoundTag())
                    )
                    .setId(ResourceKey.create(
                            Registries.ITEM,
                            Identifier.fromNamespaceAndPath(RedwoodVariants.MOD_ID, "grizzly_spawn_egg")
                    ))
            )
    );


    public static final DeferredItem<Item> REDWOOD_CHEST_BOAT = ITEMS.register("redwood_chest_boat",
            () -> new ModChestBoatItem(ModEntities.REDWOOD_CHEST_BOAT.get(),
                    new Item.Properties()
                            .setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath
                                    (RedwoodVariants.MOD_ID, "redwood_chest_boat")))));

    public static final DeferredItem<Item> REDWOOD_BOAT = ITEMS.register("redwood_boat",
            () -> new ModBoatItem(ModEntities.REDWOOD_BOAT.get(),
                    new Item.Properties()
                            .setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath
                                    (RedwoodVariants.MOD_ID, "redwood_boat")))));

    public static DeferredItem<Item> registerItem(String name, Function<Item.Properties, Item> factory, Item.Properties properties) {
        return ITEMS.register(name, () -> factory.apply(properties));
    }


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
