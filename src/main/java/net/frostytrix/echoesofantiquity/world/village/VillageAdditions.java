package net.frostytrix.echoesofantiquity.world.village;

import net.frostytrix.echoesofantiquity.EchoesOfAntiquity;
import net.frostytrix.echoesofantiquity.structures.impl.FabricStructurePoolRegistry;
import net.minecraft.util.Identifier;

public class VillageAdditions {

    // Plains only until there is a waystone variant that suits each village style.
    private static final String[] VILLAGE_TYPES = {"plains"};

    // Using https://github.com/fzzyhmstrs/structurized-reborn/blob/master/src/main/resources/structurized.mixins.json
    public static void registerNewVillageStructures() {
        for (String type : VILLAGE_TYPES) {
            FabricStructurePoolRegistry.registerSimple(
                    Identifier.of("minecraft:village/" + type + "/terminators"),
                    Identifier.of(EchoesOfAntiquity.MOD_ID, "waystone"),
                    1
            );
        }
    }
}
