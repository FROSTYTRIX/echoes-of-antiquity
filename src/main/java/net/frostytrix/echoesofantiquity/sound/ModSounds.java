package net.frostytrix.echoesofantiquity.sound;

import net.frostytrix.echoesofantiquity.EchoesOfAntiquity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSounds {
    public static final SoundEvent GRAVITY_ANCHOR_ACTIVE = registerSoundEvent("gravity_anchor_active");


    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = Identifier.of(EchoesOfAntiquity.MOD_ID , name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void registerSounds(){
        EchoesOfAntiquity.LOGGER.info("Registering sounds");
    }
}
