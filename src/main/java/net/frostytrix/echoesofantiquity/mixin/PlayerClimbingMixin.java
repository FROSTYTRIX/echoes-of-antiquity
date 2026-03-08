package net.frostytrix.echoesofantiquity.mixin;

import net.frostytrix.echoesofantiquity.item.ModItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerClimbingMixin {

    @Inject(at = @At("HEAD"), method = "tick")
    public void Climb(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        if (player.horizontalCollision && player.isHolding(ModItems.CLIMBING_SPIDER_LEG)) {
            Vec3d initialVec = player.getVelocity();
            Vec3d climbVec = new  Vec3d(initialVec.x, 0.2d , initialVec.z);
            player.setVelocity(climbVec.multiply(0.96d));
        }
    }
}
