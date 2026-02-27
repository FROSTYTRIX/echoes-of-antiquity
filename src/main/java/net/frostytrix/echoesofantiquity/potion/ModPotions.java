package net.frostytrix.echoesofantiquity.potion;

import net.frostytrix.echoesofantiquity.EchoesOfAntiquity;
import net.frostytrix.echoesofantiquity.effect.ModEffects;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class ModPotions {
    public static final RegistryEntry<Potion> PHASING_POTION = registerPotion("phasing_potion",
            new Potion(new StatusEffectInstance(ModEffects.PHASING, 600, 0)));
    public static final RegistryEntry<Potion> REACH_POTION = registerPotion("reach_potion",
            new Potion(new StatusEffectInstance(ModEffects.REACH, 900, 0)));

    private static RegistryEntry<Potion> registerPotion(String name, Potion potion){
        return Registry.registerReference(Registries.POTION, Identifier.of(EchoesOfAntiquity.MOD_ID, name), potion);
    };

    public static void registerPotions() {
        EchoesOfAntiquity.LOGGER.info("Registering Potions");
    }
}
