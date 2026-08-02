package net.frostytrix.echoesofantiquity.block.entity.custom;

import net.frostytrix.echoesofantiquity.block.custom.GravityAnchorBlock;
import net.frostytrix.echoesofantiquity.block.entity.ModBlockEntities;
import net.frostytrix.echoesofantiquity.sound.client.GravityAnchorSounds;
import net.frostytrix.echoesofantiquity.util.GravityAnchorManager;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GravityAnchorBlockEntity extends BlockEntity {
    private boolean isPlayingSound = false;
    private boolean registered = false;

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
        boolean active = state.get(GravityAnchorBlock.ACTIVE);

        if (world.isClient) {
            if (active) {
                if (!isPlayingSound) {
                    GravityAnchorSounds.startHum(world, pos);
                    isPlayingSound = true;
                }
            } else {
                isPlayingSound = false;
            }
            return;
        }

        // A fresh block entity starts unregistered, so this heals itself after a restart or chunk reload.
        if (active != registered) {
            if (active) {
                GravityAnchorManager.addAnchor(world, pos);
            } else {
                GravityAnchorManager.removeAnchor(world, pos);
            }
            registered = active;
        }
    }

    @Override
    public void markRemoved() {
        if (this.registered && this.world != null && !this.world.isClient) {
            GravityAnchorManager.removeAnchor(this.world, this.pos);
            this.registered = false;
        }
        super.markRemoved();
    }
}
