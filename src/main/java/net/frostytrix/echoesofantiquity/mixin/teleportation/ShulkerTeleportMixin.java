package net.frostytrix.echoesofantiquity.mixin.teleportation;

import net.frostytrix.echoesofantiquity.util.VoidPedestalManager;
import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShulkerEntity.class)
public class ShulkerTeleportMixin {
    @Inject(method = "tryTeleport", at = @At("HEAD"), cancellable = true)
    private void stopShulkerTP(CallbackInfoReturnable<Boolean> cir) {
        ShulkerEntity shulker = (ShulkerEntity) (Object) this;

        BlockPos pedestalPos = VoidPedestalManager.findNearestActivePedestal(shulker.getWorld(), shulker.getBlockPos());
        if (pedestalPos == null) {
            return;
        }

        if (shulker.getWorld() instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles(ParticleTypes.PORTAL,
                    pedestalPos.getX() + 0.5, pedestalPos.getY() + 1.2, pedestalPos.getZ() + 0.5,
                    20, 0.2, 0.2, 0.2, 0.1);
        }

        cir.setReturnValue(false);
    }
}
