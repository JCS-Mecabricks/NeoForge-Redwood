package github.jcsmecabricks.redwoodvariants.entity.custom;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

public class ModChestBoatEntity extends ChestBoat {
    public ModChestBoatEntity(EntityType<? extends ChestBoat> p_376154_, Level p_219872_, Supplier<Item> p_376136_) {
        super(p_376154_, p_219872_, p_376136_);
    }
}
