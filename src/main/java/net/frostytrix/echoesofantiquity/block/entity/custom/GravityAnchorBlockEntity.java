package net.frostytrix.echoesofantiquity.block.entity.custom;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.frostytrix.echoesofantiquity.block.custom.GravityAnchorBlock;
import net.frostytrix.echoesofantiquity.block.entity.ModBlockEntities;
import net.frostytrix.echoesofantiquity.sound.GravityAnchorSoundInstance;
import net.frostytrix.echoesofantiquity.sound.ModSounds;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GravityAnchorBlockEntity extends BlockEntity {
    private boolean isPlayingSound = false;

    public GravityAnchorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.GRAVITY_ANCHOR_BE, pos, state);
    }

    public boolean isActive() {
        BlockState state = this.getCachedState();
        if (state.contains(GravityAnchorBlock.ACTIVE)) {
            return state.get(GravityAnchorBlock.ACTIVE);
        }
        return false;
    }

    public void tick(World world, BlockPos pos, BlockState state) {
        if (world.isClient && state.get(GravityAnchorBlock.ACTIVE)) {
            playAmbientSound(world, pos);
        } else {
            // Si le bloc s'éteint, on réinitialise la variable
            isPlayingSound = false;
        }
    }

    @Environment(EnvType.CLIENT)
    private void playAmbientSound(World world, BlockPos pos) {
        if (!isPlayingSound) {
            // On lance notre boucle sonore infinie
            MinecraftClient.getInstance().getSoundManager().play(
                    new GravityAnchorSoundInstance(ModSounds.GRAVITY_ANCHOR_ACTIVE, world, pos)
            );
            isPlayingSound = true;
        }
    }
}