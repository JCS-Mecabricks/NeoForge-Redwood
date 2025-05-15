package github.jcsmecabricks.redwoodvariants.entity.custom;

import com.mojang.serialization.Codec;
import github.jcsmecabricks.redwoodvariants.entity.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.event.EventHooks;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class GrizzlyBearEntity extends TamableAnimal implements NeutralMob{
    private int warningSoundTicks;
    public final AnimationState sittingTransitionAnimationState = new AnimationState();
    public final AnimationState sitingAnimtaionState = new AnimationState();
    public final AnimationState standingAnimationState = new AnimationState();

    public static final EntityDataAccessor<Long> LAST_POSE_CHANGE_TICK =
            SynchedEntityData.defineId(GrizzlyBearEntity.class, EntityDataSerializers.LONG);
    private static final EntityDataAccessor<Boolean> ATTACKING =
            SynchedEntityData.defineId(GrizzlyBearEntity.class, EntityDataSerializers.BOOLEAN);

    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    public final AnimationState attackAnimationState = new AnimationState();
    public int attackAnimationTimeout = 0;

    public GrizzlyBearEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new GrizzlyBearMeleeAttackGoal());
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.15D));
        this.goalSelector.addGoal(3, new GrizzlyBearHurtByTargetGoal());
        this.goalSelector.addGoal(3, new GrizzlyBearAttackPlayersGoal());
        this.goalSelector.addGoal(4, new TemptGoal(this, 1.25D, p -> p.is(Items.SALMON), false));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(5, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(6, new FollowOwnerGoal(this, 1.0F, 10.0F, 2.0F));
        this.goalSelector.addGoal(6, new FollowParentGoal(this, 1.25));
        this.goalSelector.addGoal(7, new GrizzlyBearWanderGoal());
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));

    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 35.0)
                .add(Attributes.FOLLOW_RANGE, 20.0)
                .add(Attributes.TEMPT_RANGE, 20.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.ATTACK_DAMAGE, 8.0);
    }

    @Override
    public InteractionResult mobInteract(Player p_406380_, InteractionHand p_406261_) {
        ItemStack itemstack = p_406380_.getItemInHand(p_406261_);
        Item item = itemstack.getItem();
        if (this.isTame()) {
            if (this.isFood(itemstack) && this.getHealth() < this.getMaxHealth()) {
                FoodProperties foodproperties = itemstack.get(DataComponents.FOOD);
                float f = foodproperties != null ? (float)foodproperties.nutrition() : 1.0F;
                this.heal(2.0F * f);
                this.usePlayerItem(p_406380_, p_406261_, itemstack);
                this.gameEvent(GameEvent.EAT);
                return InteractionResult.SUCCESS;
            }

            InteractionResult interactionresult = super.mobInteract(p_406380_, p_406261_);
            if (!interactionresult.consumesAction() && this.isOwnedBy(p_406380_)) {
                this.toggleSitting();
                this.jumping = false;
                this.navigation.stop();
                this.setTarget(null);
                return InteractionResult.SUCCESS.withoutItem();
            } else {
                            return interactionresult;
            }
        } else if (!this.level().isClientSide && itemstack.is(Items.HONEY_BOTTLE) && !this.isAngry()) {
            itemstack.consume(1, p_406380_);
            this.tryToTame(p_406380_);
            return InteractionResult.SUCCESS_SERVER;
        } else {
            return super.mobInteract(p_406380_, p_406261_);
        }
    }

    private void tryToTame(Player player) {
        if (!EventHooks.onAnimalTame(this, player)) {
            if (this.random.nextInt(5) == 0) {
                this.tame(player);
                this.navigation.stop();
                this.setTarget(null);
                this.toggleSitting();
                this.level().broadcastEntityEvent(this, (byte)7);
            } else {
                this.level().broadcastEntityEvent(this, (byte)6);
            }
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.isBaby() ? SoundEvents.POLAR_BEAR_AMBIENT_BABY : SoundEvents.POLAR_BEAR_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.POLAR_BEAR_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.POLAR_BEAR_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState block) {
        this.playSound(SoundEvents.POLAR_BEAR_STEP, 0.15F, 1.0F);
    }


    public void setAttacking(boolean attacking) {
        this.entityData.set(ATTACKING, attacking);
    }

    public boolean isAttacking() {
        return this.entityData.get(ATTACKING);
    }

    public boolean isSitting() {
        return this.entityData.get(LAST_POSE_CHANGE_TICK) < 0L;
    }

    public void toggleSitting() {
        if (this.isSitting()) {
            standUp();
        } else {
            sitDown();
        }
    }

    public void sitDown() {
        if (!this.isSitting()) {
            this.makeSound(SoundEvents.CAMEL_SIT);
            this.setPose(Pose.SITTING);
            this.gameEvent(GameEvent.ENTITY_ACTION);
            this.resetLastPoseChangeTick(-this.level().getGameTime());
            setInSittingPose(true);
        }
    }

    public void standUp() {
        if (this.isSitting()) {
            this.makeSound(SoundEvents.CAMEL_STAND);
            this.setPose(Pose.STANDING);
            this.gameEvent(GameEvent.ENTITY_ACTION);
            this.resetLastPoseChangeTick(this.level().getGameTime());
            setInSittingPose(false);
        }
    }


    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ATTACKING, false);
        builder.define(LAST_POSE_CHANGE_TICK, 0L);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putLong("LastPoseTick", this.entityData.get(LAST_POSE_CHANGE_TICK));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);

        long i = tag.read("LastPoseTick", Codec.LONG).orElse(-1L);
        if (i < 0L) {
            this.setPose(Pose.SITTING);
        }
        this.resetLastPoseChangeTick(i);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, EntitySpawnReason pReason,
                                        @Nullable SpawnGroupData pSpawnData) {
        this.resetLastPoseChangeTickToFullStand(pLevel.getLevel().getGameTime());
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData);
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return itemStack.is(Items.SALMON);
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return 0;
    }

    @Override
    public void setRemainingPersistentAngerTime(int i) {

    }

    @Override
    public @Nullable UUID getPersistentAngerTarget() {
        return null;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID uuid) {

    }

    @Override
    public void startPersistentAngerTimer() {

    }

    class GrizzlyBearMeleeAttackGoal extends MeleeAttackGoal {
        public GrizzlyBearMeleeAttackGoal() {
            super(GrizzlyBearEntity.this, 1.25D, true);
        }


        protected double getAttackReachSqr(LivingEntity target) {
            return 4.0 + target.getBbWidth() * target.getBbWidth(); // Reach ~2 blocks + target size
        }

        @Override
        protected void checkAndPerformAttack(LivingEntity target) {
            if (this.canPerformAttack(target)) {
                this.resetAttackCooldown();
                this.mob.doHurtTarget(getServerLevel(this.mob), target);
                GrizzlyBearEntity.this.setAttacking(false);
            } else if (this.mob.distanceToSqr(target) < this.getAttackReachSqr(target) + 2.0) {
                if (this.isTimeToAttack()) {
                    GrizzlyBearEntity.this.setAttacking(false);
                    this.resetAttackCooldown();
                }

                if (this.getTicksUntilNextAttack() <= 10) {
                    GrizzlyBearEntity.this.setAttacking(true);
                    GrizzlyBearEntity.this.playWarningSound();
                }
            } else {
                this.resetAttackCooldown();
                GrizzlyBearEntity.this.setAttacking(false);
            }
        }

        @Override
        public void stop() {
            GrizzlyBearEntity.this.setAttacking(false);
            super.stop();
        }
    }


    protected void playWarningSound() {
        if (this.warningSoundTicks <= 0) {
            this.makeSound(SoundEvents.POLAR_BEAR_WARNING);
            this.warningSoundTicks = 40;
        }

    }

    class GrizzlyBearHurtByTargetGoal extends HurtByTargetGoal {
        public GrizzlyBearHurtByTargetGoal() {
            super(GrizzlyBearEntity.this);
        }
    }

    class GrizzlyBearWanderGoal extends WaterAvoidingRandomStrollGoal {
        public GrizzlyBearWanderGoal() {
            super(GrizzlyBearEntity.this, 1.0D);
        }

        @Override
        public boolean canUse() {
            return !GrizzlyBearEntity.this.isSitting()
                    && (!GrizzlyBearEntity.this.isTame() || GrizzlyBearEntity.this.getOwner() == null)
                    && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return !GrizzlyBearEntity.this.isSitting()
                    && (!GrizzlyBearEntity.this.isTame() || GrizzlyBearEntity.this.getOwner() == null)
                    && super.canContinueToUse();
        }
    }



    class GrizzlyBearAttackPlayersGoal extends NearestAttackableTargetGoal<Player> {
        public GrizzlyBearAttackPlayersGoal() {
            super(GrizzlyBearEntity.this, Player.class, 20, true, true, null);
        }

        @Override
        public boolean canUse() {
            if (GrizzlyBearEntity.this.isBaby()) {
                return false;
            } else {
                if (super.canUse()) {
                    for (GrizzlyBearEntity grizzlyBearEntity : GrizzlyBearEntity.this.level().getEntitiesOfClass(GrizzlyBearEntity.class,
                            GrizzlyBearEntity.this.getBoundingBox().inflate(8.0F, 4.0F, 8.0F))) {
                        if (grizzlyBearEntity.isBaby()) {
                            return true;
                        }
                    }
                }
                return false;
            }
        }

        @Override
        protected double getFollowDistance() {
            return super.getFollowDistance() * 0.5;
        }
    }

    private void setupAnimationStates() {
        if (this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = 40;
            this.idleAnimationState.start(this.tickCount);
        } else {
            --this.idleAnimationTimeout;
        }

        if(this.isAttacking() && attackAnimationTimeout <= 0) {
            attackAnimationTimeout = 40; // Length in ticks of your animation
            attackAnimationState.start(this.tickCount);
        } else {
            --this.attackAnimationTimeout;
        }

        if(!this.isAttacking()) {
            attackAnimationState.stop();
        }
        if (this.isVisuallySitting()) {
            this.standingAnimationState.stop();
            if (this.isVisuallySittingDown()) {
                this.sittingTransitionAnimationState.startIfStopped(this.tickCount);
                this.sitingAnimtaionState.stop();
            } else {
                this.sittingTransitionAnimationState.stop();
                this.sitingAnimtaionState.startIfStopped(this.tickCount);
            }
        } else {
            this.sitingAnimtaionState.stop();
            this.sittingTransitionAnimationState.stop();
            this.standingAnimationState.animateWhen(this.isInPoseTransition() && this.getPoseTime() >= 0L, this.tickCount);
        }
    }

    @Override
    public boolean isImmobile() {
        return this.isSitting() || super.isImmobile();
    }


    public boolean isInPoseTransition() {
        long i = this.getPoseTime();
        return i < (long) (this.isSitting() ? 40 : 52);
    }

    public boolean isVisuallySitting() {
        return this.getPoseTime() < 0L != this.isSitting();
    }

    private boolean isVisuallySittingDown() {
        return this.isSitting() && this.getPoseTime() < 40L && this.getPoseTime() >= 0L;
    }

    public void resetLastPoseChangeTick(long pLastPoseChangeTick) {
        this.entityData.set(LAST_POSE_CHANGE_TICK, pLastPoseChangeTick);
    }

    public long getPoseTime() {
        return this.level().getGameTime() - Math.abs(this.entityData.get(LAST_POSE_CHANGE_TICK));
    }

    private void resetLastPoseChangeTickToFullStand(long pLastPoseChangedTick) {
        this.resetLastPoseChangeTick(Math.max(0L, pLastPoseChangedTick - 52L - 1L));
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide()) {
            this.setupAnimationStates();
        }
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob ageableMob) {
        return ModEntities.GRIZZLY_BEAR.get().create(pLevel, EntitySpawnReason.BREEDING);
    }
}
