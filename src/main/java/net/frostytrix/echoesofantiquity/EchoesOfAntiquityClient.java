package net.frostytrix.echoesofantiquity;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.frostytrix.echoesofantiquity.block.ModBlocks;
import net.frostytrix.echoesofantiquity.block.entity.ModBlockEntities;
import net.frostytrix.echoesofantiquity.block.entity.renderer.SieveBERenderer;
import net.frostytrix.echoesofantiquity.block.entity.renderer.VoidPedestalBERenderer;
import net.frostytrix.echoesofantiquity.block.entity.renderer.WaystoneBERenderer;
import net.frostytrix.echoesofantiquity.screen.ModScreenHandlers;
import net.frostytrix.echoesofantiquity.screen.custom.SieveScreen;
import net.frostytrix.echoesofantiquity.screen.custom.UncrafterScreen;
import net.frostytrix.echoesofantiquity.util.ModModelPredicates;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class EchoesOfAntiquityClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockEntityRendererFactories.register(ModBlockEntities.VOID_PEDESTAL_BE, VoidPedestalBERenderer::new);

        BlockEntityRendererFactories.register(ModBlockEntities.WAYSTONE_BE, WaystoneBERenderer::new);
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WAYSTONE, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.GRAVITY_ANCHOR, RenderLayer.getCutout());

        BlockEntityRendererFactories.register(ModBlockEntities.SIEVE_BE, SieveBERenderer::new);

        ModModelPredicates.registerModelPredicate();

        HandledScreens.register(ModScreenHandlers.UNCRAFTER_SCREEN_HANDLER, UncrafterScreen::new);
        HandledScreens.register(ModScreenHandlers.SIEVE_SCREEN_HANDLER, SieveScreen::new);
    }
}
