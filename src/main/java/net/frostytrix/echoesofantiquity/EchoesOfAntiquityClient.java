package net.frostytrix.echoesofantiquity;

import net.fabricmc.api.ClientModInitializer;
import net.frostytrix.echoesofantiquity.block.entity.ModBlockEntities;
import net.frostytrix.echoesofantiquity.block.entity.renderer.VoidPedestalBERenderer;
import net.frostytrix.echoesofantiquity.screen.ModScreenHandlers;
import net.frostytrix.echoesofantiquity.screen.custom.UncrafterScreen;
import net.frostytrix.echoesofantiquity.util.ModModelPredicates;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class EchoesOfAntiquityClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockEntityRendererFactories.register(ModBlockEntities.VOID_PEDESTAL_BE, VoidPedestalBERenderer::new);

        ModModelPredicates.registerModelPredicate();

        HandledScreens.register(ModScreenHandlers.UNCRAFTER_SCREEN_HANDLER, UncrafterScreen::new);
    }
}
