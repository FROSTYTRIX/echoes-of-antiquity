package net.frostytrix.echoesofantiquity.item;

import net.frostytrix.echoesofantiquity.EchoesOfAntiquity;
import net.minecraft.item.Item;
import net.minecraft.item.trim.ArmorTrimMaterial;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.Map;

public class ModTrimMaterials {
    public static final RegistryKey<ArmorTrimMaterial> END_STEEL = of("end_steel");

    /**
     * The trim_type predicate is registered as a ClampedModelPredicateProvider, so the value is clamped to
     * [0, 1]: anything above 1.0 becomes exactly 1.0, which is amethyst. Vanilla fills 0.1 to 1.0 in steps
     * of 0.1, so a modded material has to sit between two of those steps. The odd digits keep us from
     * landing on the same value as another mod.
     */
    public static final float END_STEEL_MODEL_INDEX = 0.1612200701F;

    public static void bootstrap(Registerable<ArmorTrimMaterial> context) {
        register(context, END_STEEL, ModItems.END_STEEL_INGOT, Formatting.AQUA, END_STEEL_MODEL_INDEX);
    }

    private static RegistryKey<ArmorTrimMaterial> of(String name) {
        return RegistryKey.of(RegistryKeys.TRIM_MATERIAL, Identifier.of(EchoesOfAntiquity.MOD_ID, name));
    }

    private static void register(Registerable<ArmorTrimMaterial> context, RegistryKey<ArmorTrimMaterial> key,
                                 Item ingredient, Formatting color, float itemModelIndex) {
        context.register(key, new ArmorTrimMaterial(
                key.getValue().getPath(),
                Registries.ITEM.getEntry(ingredient),
                itemModelIndex,
                Map.of(),
                Text.translatable(Util.getTrimMaterialTranslationKey(key)).formatted(color)
        ));
    }

    private static class Util {
        static String getTrimMaterialTranslationKey(RegistryKey<ArmorTrimMaterial> key) {
            return "trim_material." + key.getValue().getNamespace() + "." + key.getValue().getPath();
        }
    }
}
