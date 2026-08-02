package net.frostytrix.echoesofantiquity.mixin;

import net.frostytrix.echoesofantiquity.item.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {

    @Inject(method = "occludeVibrationSignals", at = @At("HEAD"), cancellable = true)
    private void stayQuietInEnderBoots(CallbackInfoReturnable<Boolean> cir) {
        Entity entity = (Entity) (Object) this;

        if (entity instanceof LivingEntity livingEntity) {
            ItemStack boots = livingEntity.getEquippedStack(EquipmentSlot.FEET);

            if (boots.isOf(ModItems.ENDER_BOOTS)) {
                cir.setReturnValue(true);
            }
        }
    }
}
