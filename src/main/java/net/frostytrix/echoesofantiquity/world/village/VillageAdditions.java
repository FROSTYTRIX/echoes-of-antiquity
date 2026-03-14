package net.frostytrix.echoesofantiquity.world.village;

import net.frostytrix.echoesofantiquity.EchoesOfAntiquity;
import net.frostytrix.echoesofantiquity.structures.impl.FabricStructurePoolRegistry;
import net.minecraft.util.Identifier;

public class VillageAdditions {

    // Using https://github.com/fzzyhmstrs/structurized-reborn/blob/master/src/main/resources/structurized.mixins.json
    public static void registerNewVillageStructures() {
        FabricStructurePoolRegistry.registerSimple(
                Identifier.of("minecraft:village/plains/terminators"),
                Identifier.of(EchoesOfAntiquity.MOD_ID, "waystone"),
                1
        );
    }
}
