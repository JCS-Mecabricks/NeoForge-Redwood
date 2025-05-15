package github.jcsmecabricks.redwoodvariants.item.custom;

import github.jcsmecabricks.redwoodvariants.entity.custom.ModChestBoatEntity;
import github.jcsmecabricks.redwoodvariants.entity.custom.ModChestBoatEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;

public class ModChestBoatItem extends Item {
    private final EntityType<? extends ModChestBoatEntity> entityType;

    public ModChestBoatItem(EntityType<? extends ModChestBoatEntity> entityType, Item.Properties properties) {
        super(properties);
        this.entityType = entityType;
    }

    public InteractionResult use(Level p_40622_, Player p_40623_, InteractionHand p_40624_) {
        ItemStack itemstack = p_40623_.getItemInHand(p_40624_);
        HitResult hitresult = getPlayerPOVHitResult(p_40622_, p_40623_, ClipContext.Fluid.ANY);
        if (hitresult.getType() == HitResult.Type.MISS) {
            return InteractionResult.PASS;
        } else {
            Vec3 vec3 = p_40623_.getViewVector(1.0F);
            double d0 = (double)5.0F;
            List<Entity> list = p_40622_.getEntities(p_40623_, p_40623_.getBoundingBox().expandTowards(vec3.scale((double)5.0F)).inflate((double)1.0F), EntitySelector.CAN_BE_PICKED);
            if (!list.isEmpty()) {
                Vec3 vec31 = p_40623_.getEyePosition();

                for(Entity entity : list) {
                    AABB aabb = entity.getBoundingBox().inflate((double)entity.getPickRadius());
                    if (aabb.contains(vec31)) {
                        return InteractionResult.PASS;
                    }
                }
            }

            if (hitresult.getType() == HitResult.Type.BLOCK) {
                ModChestBoatEntity modChestBoatEntity = this.getBoat(p_40622_, hitresult, itemstack, p_40623_);
                if (modChestBoatEntity == null) {
                    return InteractionResult.FAIL;
                } else {
                    modChestBoatEntity.setYRot(p_40623_.getYRot());
                    if (!p_40622_.noCollision(modChestBoatEntity, modChestBoatEntity.getBoundingBox())) {
                        return InteractionResult.FAIL;
                    } else {
                        if (!p_40622_.isClientSide) {
                            p_40622_.addFreshEntity(modChestBoatEntity);
                            p_40622_.gameEvent(p_40623_, GameEvent.ENTITY_PLACE, hitresult.getLocation());
                            itemstack.consume(1, p_40623_);
                        }

                        p_40623_.awardStat(Stats.ITEM_USED.get(this));
                        return InteractionResult.SUCCESS;
                    }
                }
            } else {
                return InteractionResult.PASS;
            }
        }
    }

    @Nullable
    private ModChestBoatEntity getBoat(Level level, HitResult hitResult, ItemStack stack, Player player) {
        ModChestBoatEntity modChestBoatEntity = this.entityType.create(level, EntitySpawnReason.SPAWN_ITEM_USE);
        if (modChestBoatEntity != null) {
            Vec3 vec3 = hitResult.getLocation();
            modChestBoatEntity.setInitialPos(vec3.x, vec3.y, vec3.z);
            if (level instanceof ServerLevel) {
                ServerLevel serverlevel = (ServerLevel)level;
                EntityType.createDefaultStackConfig(serverlevel, stack, player).accept(modChestBoatEntity);
            }
        }

        return modChestBoatEntity;
    }
}
