package net.frostytrix.echoesofantiquity.screen;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.frostytrix.echoesofantiquity.EchoesOfAntiquity;
import net.frostytrix.echoesofantiquity.screen.custom.UncrafterScreenHandler;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class ModScreenHandlers {
    public static final ScreenHandlerType<UncrafterScreenHandler> UNCRAFTER_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, Identifier.of(EchoesOfAntiquity.MOD_ID, "uncrafter_screen_handler"),
                    new ExtendedScreenHandlerType<>(UncrafterScreenHandler::new, BlockPos.PACKET_CODEC));

    public static void registerScreenHandlers() {
        EchoesOfAntiquity.LOGGER.info("Registering Screen Handlers for " + EchoesOfAntiquity.MOD_ID);
    }
}
