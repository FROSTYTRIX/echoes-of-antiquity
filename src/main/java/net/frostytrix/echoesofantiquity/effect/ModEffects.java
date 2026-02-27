package net.frostytrix.echoesofantiquity.effect;

import net.frostytrix.echoesofantiquity.EchoesOfAntiquity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class ModEffects {
    public static final RegistryEntry<StatusEffect> PHASING = registerStatusEffect("phasing",
            new PhasingEffect(StatusEffectCategory.NEUTRAL, 0x93248e));
    public static final RegistryEntry<StatusEffect> REACH = registerStatusEffect("reach",
            new ReachEffect(StatusEffectCategory.BENEFICIAL, 0x41e8c3));


    private static RegistryEntry<StatusEffect> registerStatusEffect(String name, StatusEffect statusEffect) {
        return Registry.registerReference(Registries.STATUS_EFFECT, Identifier.of(EchoesOfAntiquity.MOD_ID, name), statusEffect);
    }

    public static void registerEffects(){
        EchoesOfAntiquity.LOGGER.info("[EchoesOfAntiquity] Loading effects");
    }
}
