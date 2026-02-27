package net.frostytrix.echoesofantiquity;

import net.fabricmc.api.ClientModInitializer;
import net.frostytrix.echoesofantiquity.block.entity.ModBlockEntities;
import net.frostytrix.echoesofantiquity.block.entity.renderer.VoidAnchorBERenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class EchoesOfAntiquityClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockEntityRendererFactories.register(ModBlockEntities.VOID_ANCHOR_BE, VoidAnchorBERenderer::new);
    }
}
