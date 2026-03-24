package net.frostytrix.echoesofantiquity.entity.client.ChorusHusk;

import net.frostytrix.echoesofantiquity.EchoesOfAntiquity;
import net.frostytrix.echoesofantiquity.entity.custom.ChorusHuskEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class ChorusHuskModel<T extends ChorusHuskEntity> extends SinglePartEntityModel<T> {
    public static final EntityModelLayer CHORUS_HUSK = new EntityModelLayer(Identifier.of(EchoesOfAntiquity.MOD_ID, "chorus_husk"), "main");


    private final ModelPart main;
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart right_arm;
    private final ModelPart left_arm;
    private final ModelPart right_leg;
    private final ModelPart left_leg;

    public ChorusHuskModel(ModelPart root) {
        this.main = root.getChild("main");
        this.body = this.main.getChild("body");
        this.head = this.main.getChild("head");
        this.right_arm = this.main.getChild("right_arm");
        this.left_arm = this.main.getChild("left_arm");
        this.right_leg = this.main.getChild("right_leg");
        this.left_leg = this.main.getChild("left_leg");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData main = modelPartData.addChild("main", ModelPartBuilder.create(), ModelTransform.pivot(-1.0833F, -10.3333F, 0.25F));

        ModelPartData body = main.addChild("body", ModelPartBuilder.create().uv(16, 9).cuboid(-8.0F, 0.0F, -2.0F, 11.0F, 9.0F, 4.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-8.0F, 9.0F, -2.0F, 16.0F, 5.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(1.0833F, -4.6667F, -0.25F));

        ModelPartData head = main.addChild("head", ModelPartBuilder.create().uv(16, 22).cuboid(-3.0F, -10.0F, -3.0F, 6.0F, 10.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(-1.4167F, -4.6667F, -0.25F));

        ModelPartData right_arm = main.addChild("right_arm", ModelPartBuilder.create().uv(0, 9).cuboid(-3.0F, -2.0F, -2.5F, 4.0F, 25.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(-7.9167F, -2.6667F, 0.25F));

        ModelPartData left_arm = main.addChild("left_arm", ModelPartBuilder.create().uv(40, 0).cuboid(-2.0F, -2.0F, -2.0F, 9.0F, 4.0F, 4.0F, new Dilation(0.0F))
                .uv(18, 38).cuboid(3.0F, 2.0F, -2.0F, 4.0F, 17.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(6.0833F, -2.6667F, -0.25F));

        ModelPartData right_leg = main.addChild("right_leg", ModelPartBuilder.create().uv(40, 22).cuboid(-3.0F, 2.0F, -2.0F, 10.0F, 5.0F, 3.0F, new Dilation(0.0F))
                .uv(34, 38).cuboid(-3.0F, 7.0F, -2.0F, 3.0F, 20.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(-3.9167F, 7.3333F, 0.25F));

        ModelPartData left_leg = main.addChild("left_leg", ModelPartBuilder.create().uv(0, 38).cuboid(-3.0F, 2.0F, -2.0F, 6.0F, 18.0F, 3.0F, new Dilation(0.0F))
                .uv(46, 8).cuboid(0.0F, 20.0F, -2.0F, 3.0F, 7.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(6.0833F, 7.3333F, 0.25F));
        return TexturedModelData.of(modelData, 80, 80);
    }

    @Override
    public void setAngles(ChorusHuskEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);
        this.setHeadAngles(netHeadYaw, headPitch);

        this.animateMovement(ChorusHuskAnimations.ANIM_CHORUS_HUSK_WALK, limbSwing, limbSwingAmount, 2, 2.5f);
        this.updateAnimation(entity.idleAnimationState, ChorusHuskAnimations.ANIM_CHORUS_HUSK_IDLE, ageInTicks, 1f);
    }

    private void setHeadAngles(float headYaw, float headPitch){
        headYaw = MathHelper.clamp(headYaw, -30f, 30f);
        headPitch = MathHelper.clamp(headPitch, -25f, 45f);

        this.head.yaw = (float) (headYaw * 0.017453292);
        this.head.pitch = (float) (headPitch * 0.017453292);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, int color) {
        main.render(matrices, vertexConsumer, light, overlay, color);
    }

    @Override
    public ModelPart getPart() {
        return main;
    }
}
