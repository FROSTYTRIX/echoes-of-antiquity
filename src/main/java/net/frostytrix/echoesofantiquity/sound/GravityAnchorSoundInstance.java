package net.frostytrix.echoesofantiquity.sound;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.frostytrix.echoesofantiquity.block.ModBlocks;
import net.frostytrix.echoesofantiquity.block.custom.GravityAnchorBlock;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
public class GravityAnchorSoundInstance extends MovingSoundInstance {
    private final World world;
    private final BlockPos pos;

    public GravityAnchorSoundInstance(SoundEvent sound, World world, BlockPos pos) {
        super(sound, SoundCategory.BLOCKS, SoundInstance.createRandom());
        this.world = world;
        this.pos = pos;

        // Pin the sound to the exact center of the block
        this.x = (double) pos.getX() + 0.5;
        this.y = (double) pos.getY() + 0.5;
        this.z = (double) pos.getZ() + 0.5;

        // This makes the hum loop infinitely
        this.repeat = true;
        this.repeatDelay = 0;
        this.volume = 1.4f;
    }

    @Override
    public void tick() {
        // If the block is broken OR turned off, kill the audio loop
        if (!world.getBlockState(pos).isOf(ModBlocks.GRAVITY_ANCHOR) || !world.getBlockState(pos).get(GravityAnchorBlock.ACTIVE)) {
            this.setDone();
        }
    }
}