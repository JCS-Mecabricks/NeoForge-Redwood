package github.jcsmecabricks.redwoodvariants.entity;

import github.jcsmecabricks.redwoodvariants.RedwoodVariants;
import github.jcsmecabricks.redwoodvariants.entity.custom.GrizzlyBearEntity;
import github.jcsmecabricks.redwoodvariants.entity.custom.ModBoatEntity;
import github.jcsmecabricks.redwoodvariants.entity.custom.ModChestBoatEntity;
import github.jcsmecabricks.redwoodvariants.item.ModItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, RedwoodVariants.MOD_ID);

    public static final Supplier<EntityType<GrizzlyBearEntity>> GRIZZLY_BEAR =
            ENTITY_TYPES.register("grizzly_bear", () -> EntityType.Builder.of(GrizzlyBearEntity::new, MobCategory.CREATURE)
                    .sized(1.6f, 1.6f).build(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath
                            (RedwoodVariants.MOD_ID, "grizzly_bear"))));

    public static final Supplier<EntityType<ModBoatEntity>> REDWOOD_BOAT =
            ENTITY_TYPES.register("redwood_boat", () -> EntityType.Builder.of(boatFactory(() -> ModItems.REDWOOD_BOAT.get().asItem()),
                            MobCategory.CREATURE)
                    .sized(1.375F, 0.5625F)
                    .eyeHeight(0.5625F)
                    .clientTrackingRange(10)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            ResourceLocation.fromNamespaceAndPath
                            (RedwoodVariants.MOD_ID, "redwood_boat"))));

    public static final Supplier<EntityType<ModChestBoatEntity>> REDWOOD_CHEST_BOAT =
            ENTITY_TYPES.register("redwood_chest_boat", () -> EntityType.Builder.of(chestBoatFactory(() -> ModItems.REDWOOD_CHEST_BOAT.get().asItem()),
                    MobCategory.CREATURE)
                    .sized(1.375F, 0.5625F)
                    .eyeHeight(0.5625F)
                    .clientTrackingRange(10)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            ResourceLocation.fromNamespaceAndPath
                            (RedwoodVariants.MOD_ID, "redwood_chest_boat"))));

    private static EntityType.EntityFactory<ModBoatEntity> boatFactory(Supplier<Item> boatItemGetter) {
        return (p_375558_, p_375559_) -> new ModBoatEntity(p_375558_, p_375559_, boatItemGetter);
    }

    private static EntityType.EntityFactory<ModChestBoatEntity> chestBoatFactory(Supplier<Item> boatItemGetter) {
        return (p_375555_, p_375556_) -> new ModChestBoatEntity(p_375555_, p_375556_, boatItemGetter);
    }

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
