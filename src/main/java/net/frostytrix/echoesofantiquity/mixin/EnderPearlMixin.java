package net.frostytrix.echoesofantiquity.mixin;

import net.frostytrix.echoesofantiquity.block.ModBlocks;
import net.frostytrix.echoesofantiquity.block.custom.VoidAnchorBlock;
import net.frostytrix.echoesofantiquity.block.entity.custom.VoidAnchorBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnderPearlEntity.class)
public abstract class EnderPearlMixin {
    @Inject(method = "onCollision", at = @At("HEAD"), cancellable = true)
    private void stopPearlTP(HitResult hitResult, CallbackInfo ci) {
        EnderPearlEntity pearl = (EnderPearlEntity) (Object) this;

        // Check if the pearl itself is in the anchor radius (has the tag)
        // OR if the player throwing it is currently anchored
        Entity owner = pearl.getOwner();
        boolean ownerAnchored = (owner != null && owner.getCommandTags().contains("void_anchor_suppressed"));
        boolean pearlAnchored = pearl.getCommandTags().contains("void_anchor_suppressed");

        if (pearlAnchored || ownerAnchored) {
            // We "kill" the pearl so it disappears without teleporting the player
            pearl.discard();
            ci.cancel();

            BlockPos anchorPos1 = this.findNearestActiveAnchor(pearl);
            BlockPos anchorPos2 = this.findNearestActiveAnchor(owner);
            if (anchorPos1 != null) {
                ServerWorld serverWorld = (ServerWorld) pearl.getWorld();
                // Spawn "Portal" or "Witch" particles at the block
                serverWorld.spawnParticles(ParticleTypes.PORTAL,
                        anchorPos1.getX() + 0.5, anchorPos1.getY() + 1.2, anchorPos1.getZ() + 0.5,
                        20, 0.2, 0.2, 0.2, 0.1);
            } else if (anchorPos2 != null) {
                ServerWorld serverWorld = (ServerWorld) owner.getWorld();
                // Spawn "Portal" or "Witch" particles at the block
                serverWorld.spawnParticles(ParticleTypes.PORTAL,
                        anchorPos2.getX() + 0.5, anchorPos2.getY() + 1.2, anchorPos2.getZ() + 0.5,
                        20, 0.2, 0.2, 0.2, 0.1);
            }
            pearl.removeCommandTag("void_anchor_suppressed");
            if (owner != null) { owner.removeCommandTag("void_anchor_suppressed");
            }
        }
    }

    private BlockPos findNearestActiveAnchor(Entity entity) {
        BlockPos pos = entity.getBlockPos();
        int r = VoidAnchorBlockEntity.noTPRadius; // Use the same radius as your BlockEntity
        for (BlockPos target : BlockPos.iterate(pos.add(-r, -r, -r), pos.add(r, r, r))) {
            BlockState state = entity.getWorld().getBlockState(target);
            if (state.isOf(ModBlocks.VOID_ANCHOR) && state.get(VoidAnchorBlock.ACTIVE)) {
                return target.toImmutable();
            }
        }
        return null;

    }
}
