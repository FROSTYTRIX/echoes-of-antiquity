package net.frostytrix.echoesofantiquity.mixin;

import net.frostytrix.echoesofantiquity.util.GravityAnchorManager;
import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FallingBlock.class)
public class FallingBlockMixin {

    @Inject(method = "scheduledTick", at = @At("HEAD"), cancellable = true)
    private void preventFall(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        // Si le bloc est à côté d'une ancre, on annule la mise à jour qui le fait tomber.
        if (GravityAnchorManager.isProtected(world, pos)) {
            ci.cancel();
        }
    }
}