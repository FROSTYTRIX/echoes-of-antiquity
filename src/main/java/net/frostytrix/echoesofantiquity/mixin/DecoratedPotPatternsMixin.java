package net.frostytrix.echoesofantiquity.mixin;

import net.frostytrix.echoesofantiquity.item.ModItems;
import net.minecraft.block.DecoratedPotPattern;
import net.minecraft.block.DecoratedPotPatterns;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DecoratedPotPatterns.class)
public class DecoratedPotPatternsMixin {

    // On intercepte la demande de texture du Pot
    @Inject(method = "fromSherd", at = @At("HEAD"), cancellable = true)
    private static void getCustomSherdPattern(Item sherd, CallbackInfoReturnable<RegistryKey<DecoratedPotPattern>> cir) {
        if (ModItems.SHERD_TO_PATTERN.containsKey(sherd)) {
            cir.setReturnValue(ModItems.SHERD_TO_PATTERN.get(sherd));
        }
    }
}