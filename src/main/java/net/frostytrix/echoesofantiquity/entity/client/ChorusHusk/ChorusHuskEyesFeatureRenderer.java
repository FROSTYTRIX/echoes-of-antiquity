package net.frostytrix.echoesofantiquity.entity.client.ChorusHusk;

import net.frostytrix.echoesofantiquity.EchoesOfAntiquity;
import net.frostytrix.echoesofantiquity.entity.custom.ChorusHuskEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.feature.EyesFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.util.Identifier;

public class ChorusHuskEyesFeatureRenderer extends EyesFeatureRenderer<ChorusHuskEntity, ChorusHuskModel<ChorusHuskEntity>> {

    // Points to your new transparent texture with just the eyes
    private static final RenderLayer SKIN = RenderLayer.getEyes(Identifier.of(EchoesOfAntiquity.MOD_ID, "textures/entity/chorus_husk/chorus_husk_eyes.png"));

    public ChorusHuskEyesFeatureRenderer(FeatureRendererContext<ChorusHuskEntity, ChorusHuskModel<ChorusHuskEntity>> featureRendererContext) {
        super(featureRendererContext);
    }

    @Override
    public RenderLayer getEyesTexture() {
        return SKIN;
    }
}
