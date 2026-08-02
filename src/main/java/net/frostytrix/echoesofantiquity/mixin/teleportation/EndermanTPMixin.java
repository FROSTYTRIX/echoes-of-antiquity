package net.frostytrix.echoesofantiquity.mixin.teleportation;

import net.frostytrix.echoesofantiquity.util.VoidPedestalManager;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EndermanEntity.class)
public class EndermanTPMixin {

    @Inject(method = "teleportTo(DDD)Z", at = @At("HEAD"), cancellable = true)
    private void stopTeleportIfSuppressed(CallbackInfoReturnable<Boolean> cir) {
        EndermanEntity enderman = (EndermanEntity) (Object) this;

        BlockPos pedestalPos = VoidPedestalManager.findNearestActivePedestal(enderman.getWorld(), enderman.getBlockPos());
        if (pedestalPos == null) {
            return;
        }

        if (enderman.getWorld() instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles(ParticleTypes.PORTAL,
                    pedestalPos.getX() + 0.5, pedestalPos.getY() + 1.2, pedestalPos.getZ() + 0.5,
                    20, 0.2, 0.2, 0.2, 0.1);
        }

        cir.setReturnValue(false);
    }
}
