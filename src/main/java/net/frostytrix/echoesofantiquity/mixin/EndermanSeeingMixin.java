package net.frostytrix.echoesofantiquity.mixin;

import net.frostytrix.echoesofantiquity.item.ModItems;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EndermanEntity.class)
public class EndermanSeeingMixin {

    @Inject(method = "isPlayerStaring", at = @At("HEAD"), cancellable = true)
    private void isPlayerStaringObsidianGoggles(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        ItemStack item = player.getInventory().armor.get(3);
        if (item.isOf(ModItems.OBSIDIAN_GOGGLES)) {
            cir.setReturnValue(false);
        }
    }
}
