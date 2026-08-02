package net.frostytrix.echoesofantiquity.sound.client;

import net.frostytrix.echoesofantiquity.sound.GravityAnchorSoundInstance;
import net.frostytrix.echoesofantiquity.sound.ModSounds;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Client-only entry point for the anchor hum. Keeping the MinecraftClient reference in its own class means
 * a dedicated server never loads it, instead of relying on an @Environment annotation inside a common class.
 */
public class GravityAnchorSounds {

    public static void startHum(World world, BlockPos pos) {
        MinecraftClient.getInstance().getSoundManager().play(
                new GravityAnchorSoundInstance(ModSounds.GRAVITY_ANCHOR_ACTIVE, world, pos)
        );
    }
}
