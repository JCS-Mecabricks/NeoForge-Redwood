package github.jcsmecabricks.redwoodvariants.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import github.jcsmecabricks.redwoodvariants.RedwoodVariants;
import github.jcsmecabricks.redwoodvariants.entity.custom.GrizzlyBearEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class GrizzlyBearRenderer extends MobRenderer<GrizzlyBearEntity, GrizzlyBearRenderState, GrizzlyBearModel> {
    public GrizzlyBearRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new GrizzlyBearModel(pContext.bakeLayer(GrizzlyBearModel.GRIZZLY_BEAR)), 0.9f);
    }

    @Override
    public ResourceLocation getTextureLocation(GrizzlyBearRenderState grizzlyBearRenderState) {
        return ResourceLocation.fromNamespaceAndPath(RedwoodVariants.MOD_ID, "textures/entity/grizzly/grizzly_bear.png");
    }

    @Override
    public GrizzlyBearRenderState createRenderState() {
        return new GrizzlyBearRenderState();
    }

    @Override
    protected void scale(GrizzlyBearRenderState renderState, PoseStack poseStack) {
        float scale = renderState.isBaby ? 0.8F : 1.3F;
        poseStack.scale(scale, scale, scale);
    }

    @Override
    public void extractRenderState(GrizzlyBearEntity grizzlyBearEntity, GrizzlyBearRenderState grizzlyBearRenderState, float p_361157_) {
        super.extractRenderState(grizzlyBearEntity, grizzlyBearRenderState, p_361157_);
        grizzlyBearRenderState.idleAnimationState.copyFrom(grizzlyBearEntity.idleAnimationState);
        grizzlyBearRenderState.sittingAnimationState.copyFrom(grizzlyBearEntity.sitingAnimtaionState);
        grizzlyBearRenderState.standingAnimationState.copyFrom(grizzlyBearEntity.standingAnimationState);
        grizzlyBearRenderState.sittingTransitionAnimationState.copyFrom(grizzlyBearEntity.sittingTransitionAnimationState);
        grizzlyBearRenderState.attackingAnimationState.copyFrom(grizzlyBearEntity.attackAnimationState);
    }
}
