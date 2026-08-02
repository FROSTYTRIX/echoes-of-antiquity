package net.frostytrix.echoesofantiquity.mixin.teleportation;

import net.frostytrix.echoesofantiquity.util.VoidPedestalManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnderPearlEntity.class)
public abstract class EnderPearlMixin {
    @Inject(method = "onCollision", at = @At("HEAD"), cancellable = true)
    private void stopPearlTP(HitResult hitResult, CallbackInfo ci) {
        EnderPearlEntity pearl = (EnderPearlEntity) (Object) this;

        // Blocked if the pearl lands near an active pedestal, or if the thrower is near one.
        Entity owner = pearl.getOwner();
        BlockPos pedestalPos = VoidPedestalManager.findNearestActivePedestal(pearl.getWorld(), pearl.getBlockPos());
        World particleWorld = pearl.getWorld();

        if (pedestalPos == null && owner != null) {
            pedestalPos = VoidPedestalManager.findNearestActivePedestal(owner.getWorld(), owner.getBlockPos());
            particleWorld = owner.getWorld();
        }

        if (pedestalPos == null) {
            return;
        }

        pearl.discard();
        ci.cancel();

        if (particleWorld instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles(ParticleTypes.PORTAL,
                    pedestalPos.getX() + 0.5, pedestalPos.getY() + 1.2, pedestalPos.getZ() + 0.5,
                    20, 0.2, 0.2, 0.2, 0.1);
        }
    }
}
