package net.frostytrix.echoesofantiquity.mixin;

import net.frostytrix.echoesofantiquity.block.ModBlocks;
import net.frostytrix.echoesofantiquity.block.custom.VoidPedestalBlock;
import net.frostytrix.echoesofantiquity.block.entity.custom.VoidAnchorBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
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
        if (shulker.getCommandTags().contains("void_pedestal_suppressed")) {
            BlockPos anchorPos = this.findNearestActiveAnchor(shulker);
            if (anchorPos != null) {
                ServerWorld serverWorld = (ServerWorld) shulker.getWorld();
                // Spawn "Portal" or "Witch" particles at the block
                serverWorld.spawnParticles(ParticleTypes.PORTAL,
                        anchorPos.getX() + 0.5, anchorPos.getY() + 1.2, anchorPos.getZ() + 0.5,
                        20, 0.2, 0.2, 0.2, 0.1);
            }

            cir.setReturnValue(false); // Cancel the teleport
            shulker.removeCommandTag("void_pedestal_suppressed");
        }
    }

    private BlockPos findNearestActiveAnchor(Entity entity) {
        BlockPos pos = entity.getBlockPos();
        int r = VoidAnchorBlockEntity.noTPRadius; // Use the same radius as your BlockEntity
        for (BlockPos target : BlockPos.iterate(pos.add(-r, -r, -r), pos.add(r, r, r))) {
            BlockState state = entity.getWorld().getBlockState(target);
            if (state.isOf(ModBlocks.VOID_PEDESTAL) && state.get(VoidPedestalBlock.ACTIVE)) {
                return target.toImmutable();
            }
        }
        return null;

    }
}
