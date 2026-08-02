package net.frostytrix.echoesofantiquity.mixin;

import net.frostytrix.echoesofantiquity.util.GravityAnchorManager;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FallingBlockEntity.class)
public abstract class FallingBlockEntityMixin extends Entity {

    public FallingBlockEntityMixin(EntityType<?> type, World world) {
        super(type, world);

    }

    @Shadow public abstract BlockState getBlockState();

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void freezeInAnchorZone(CallbackInfo ci) {
        World world = this.getWorld();

        if (world.isClient) return;

        BlockPos currentPos = this.getBlockPos();

        if (GravityAnchorManager.isProtected(world, currentPos)) {
            BlockState stateToPlace = this.getBlockState();

            // Don't overwrite a solid block.
            if (world.getBlockState(currentPos).isReplaceable()) {
                world.setBlockState(currentPos, stateToPlace, 3);

                this.discard();

                ci.cancel();
            }
        }
    }
}