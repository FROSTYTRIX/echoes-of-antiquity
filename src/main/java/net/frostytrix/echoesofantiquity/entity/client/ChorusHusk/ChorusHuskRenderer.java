package net.frostytrix.echoesofantiquity.entity.client.ChorusHusk;

import net.frostytrix.echoesofantiquity.EchoesOfAntiquity;
import net.frostytrix.echoesofantiquity.entity.custom.ChorusHuskEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class ChorusHuskRenderer extends MobEntityRenderer<ChorusHuskEntity, ChorusHuskModel<ChorusHuskEntity>> {
    public ChorusHuskRenderer(EntityRendererFactory.Context context) {
        super(context, new ChorusHuskModel<>(context.getPart(ChorusHuskModel.CHORUS_HUSK)), 0.75f);
        this.addFeature(new ChorusHuskEyesFeatureRenderer(this));
    }

    @Override
    public Identifier getTexture(ChorusHuskEntity entity) {
        return Identifier.of(EchoesOfAntiquity.MOD_ID, "textures/entity/chorus_husk/chorus_husk.png");
    }

    @Override
    public void render(ChorusHuskEntity livingEntity, float f, float g, MatrixStack matrixStack,
                       VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.scale(1f,1f,1f);

        super.render(livingEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }
}
