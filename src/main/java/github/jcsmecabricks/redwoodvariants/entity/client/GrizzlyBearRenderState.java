package github.jcsmecabricks.redwoodvariants.entity.client;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.item.ItemStack;

public class GrizzlyBearRenderState extends LivingEntityRenderState {
    public final AnimationState sittingAnimationState;
    public final AnimationState sittingTransitionAnimationState;
    public final AnimationState standingAnimationState;
    public final AnimationState attackingAnimationState;
    public final AnimationState idleAnimationState;


    public GrizzlyBearRenderState() {
        this.sittingAnimationState = new AnimationState();
        this.sittingTransitionAnimationState = new AnimationState();
        this.standingAnimationState = new AnimationState();
        this.idleAnimationState = new AnimationState();
        this.attackingAnimationState = new AnimationState();
    }
}