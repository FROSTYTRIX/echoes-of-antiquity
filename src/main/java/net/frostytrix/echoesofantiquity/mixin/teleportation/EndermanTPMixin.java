package net.frostytrix.echoesofantiquity.mixin.teleportation;

import net.frostytrix.echoesofantiquity.block.ModBlocks;
import net.frostytrix.echoesofantiquity.block.custom.VoidPedestalBlock;
import net.frostytrix.echoesofantiquity.block.entity.custom.VoidPedestalBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EndermanEntity.class)
public class EndermanTPMixin {


    @Inject(method = "teleportTo(DDD)Z", at = @At("HEAD"), cancellable = true)
// Removed double x, double y, double z
    private void stopTeleportIfSuppressed(CallbackInfoReturnable<Boolean> cir) {
        // 'this' in a Mixin refers to the class being mixed into
        EndermanEntity enderman = (EndermanEntity) (Object) this;

        // Check for your custom tag
        if (enderman.getCommandTags().contains("void_pedestal_suppressed")) {
            BlockPos anchorPos = this.findNearestActiveAnchor(enderman);
            if (anchorPos != null) {
                // Ensure we are on a ServerWorld before casting
                if (enderman.getWorld() instanceof ServerWorld serverWorld) {
                    // Spawn "Portal" or "Witch" particles at the block
                    serverWorld.spawnParticles(ParticleTypes.PORTAL,
                            anchorPos.getX() + 0.5, anchorPos.getY() + 1.2, anchorPos.getZ() + 0.5,
                            20, 0.2, 0.2, 0.2, 0.1);
                }
            }
            enderman.removeCommandTag("void_pedestal_suppressed");

            // Cancel the teleport and return false (teleport failed)
            cir.setReturnValue(false);
        }
    }

    @Unique
    private BlockPos findNearestActiveAnchor(Entity entity) {
        BlockPos pos = entity.getBlockPos();
        int r = VoidPedestalBlockEntity.noTPRadius; // Use the same radius as your BlockEntity
        for (BlockPos target : BlockPos.iterate(pos.add(-r, -r, -r), pos.add(r, r, r))) {
            BlockState state = entity.getWorld().getBlockState(target);
            if (state.isOf(ModBlocks.VOID_PEDESTAL) && state.get(VoidPedestalBlock.ACTIVE)) {
                return target.toImmutable();
            }
        }
        return null;

    }
}
