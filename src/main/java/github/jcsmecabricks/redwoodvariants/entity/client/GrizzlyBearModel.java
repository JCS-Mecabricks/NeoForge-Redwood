package github.jcsmecabricks.redwoodvariants.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import github.jcsmecabricks.redwoodvariants.RedwoodVariants;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class GrizzlyBearModel extends EntityModel<GrizzlyBearRenderState> {
    public static final ModelLayerLocation GRIZZLY_BEAR = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(RedwoodVariants.MOD_ID, "grizzly_bear"), "main");
    private final ModelPart root;
    private final ModelPart grizzly_bear;
    private final ModelPart body;
    private final ModelPart neck;
    private final ModelPart head;
    private final ModelPart mouth;
    private final ModelPart right_leg;
    private final ModelPart right_front;
    private final ModelPart right_back;
    private final ModelPart left_leg;
    private final ModelPart left_back;
    private final ModelPart left_front;

    public GrizzlyBearModel(ModelPart root) {
        super(root);
        this.root = root.getChild("root");
        this.grizzly_bear = this.root.getChild("grizzly_bear");
        this.body = this.grizzly_bear.getChild("body");
        this.neck = this.grizzly_bear.getChild("neck");
        this.head = this.neck.getChild("head");
        this.mouth = this.head.getChild("mouth");
        this.right_leg = this.grizzly_bear.getChild("right_leg");
        this.right_front = this.right_leg.getChild("right_front");
        this.right_back = this.right_leg.getChild("right_back");
        this.left_leg = this.grizzly_bear.getChild("left_leg");
        this.left_back = this.left_leg.getChild("left_back");
        this.left_front = this.left_leg.getChild("left_front");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition grizzly_bear = root.addOrReplaceChild("grizzly_bear", CubeListBuilder.create(), PartPose.offset(1.0F, 0.0F, 5.0F));

        PartDefinition body = grizzly_bear.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -19.0F, -7.0F, 14.0F, 11.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition neck = grizzly_bear.addOrReplaceChild("neck", CubeListBuilder.create().texOffs(0, 25).addBox(-7.0F, -9.0F, -6.0F, 12.0F, 10.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -9.0F, -13.0F));

        PartDefinition head = neck.addOrReplaceChild("head", CubeListBuilder.create().texOffs(47, 24).addBox(-6.5F, -7.0F, -5.0F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(56, 6).addBox(-7.5F, -7.0F, -4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(56, 12).addBox(-0.5F, -8.0F, -4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(56, 10).addBox(0.5F, -7.0F, -4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(56, 8).addBox(-7.5F, -8.0F, -4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(56, 14).addBox(0.5F, -8.0F, -4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(56, 16).addBox(-6.5F, -8.0F, -4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 0.0F, -7.0F));

        PartDefinition mouth = head.addOrReplaceChild("mouth", CubeListBuilder.create().texOffs(56, 0).addBox(-3.5F, -12.0F, -28.0F, 5.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 9.0F, 20.0F));

        PartDefinition right_leg = grizzly_bear.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition right_front = right_leg.addOrReplaceChild("right_front", CubeListBuilder.create().texOffs(48, 38).addBox(-6.5F, -8.0F, -16.0F, 4.0F, 8.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition right_back = right_leg.addOrReplaceChild("right_back", CubeListBuilder.create().texOffs(0, 47).addBox(-7.5F, -8.0F, -2.0F, 4.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition left_leg = grizzly_bear.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition left_back = left_leg.addOrReplaceChild("left_back", CubeListBuilder.create().texOffs(24, 47).addBox(1.5F, -8.0F, -2.0F, 4.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition left_front = left_leg.addOrReplaceChild("left_front", CubeListBuilder.create().texOffs(48, 52).addBox(0.5F, -8.0F, -16.0F, 4.0F, 8.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(GrizzlyBearRenderState renderState) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.applyHeadRotation(renderState, renderState.yRot, renderState.xRot);
        this.animateWalk(GrizzlyBearAnimations.WALK, renderState.walkAnimationPos, renderState.walkAnimationSpeed, 2f, 2.5f);
        this.animate(renderState.idleAnimationState, GrizzlyBearAnimations.IDLE, renderState.ageInTicks, 1f);
        this.animate(renderState.sittingAnimationState, GrizzlyBearAnimations.SIT, renderState.ageInTicks, 1f);
        this.animate(renderState.standingAnimationState, GrizzlyBearAnimations.STAND, renderState.ageInTicks, 1f);
        this.animate(renderState.sittingTransitionAnimationState, GrizzlyBearAnimations.SIT_TRANSITION, renderState.ageInTicks, 1f);
        this.animate(renderState.attackingAnimationState, GrizzlyBearAnimations.ATTACK, renderState.ageInTicks, 1f);
    }

    private void applyHeadRotation(GrizzlyBearRenderState renderState, float headYaw, float headPitch) {
        headYaw = Mth.clamp(headYaw, -30f, 30f);
        headPitch = Mth.clamp(headPitch, -25f, 45);

        this.head.yRot = headYaw * ((float)Math.PI / 180f);
        this.head.xRot = headPitch *  ((float)Math.PI / 180f);
    }

    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color, float red, float green, float blue, float alpha) {
        root.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }
}