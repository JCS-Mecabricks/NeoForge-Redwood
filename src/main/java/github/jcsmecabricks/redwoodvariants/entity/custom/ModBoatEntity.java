package github.jcsmecabricks.redwoodvariants.entity.custom;

import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.AbstractBoat;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

public class ModBoatEntity extends Boat {
    public ModBoatEntity(EntityType<? extends Boat> entityType, Level level, Supplier<Item> dropItem) {
        super(entityType, level, dropItem);
    }
}
