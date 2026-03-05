package net.frostytrix.echoesofantiquity.item.custom;

import net.frostytrix.echoesofantiquity.item.ModItems;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class SoulSiphonItem extends SwordItem {
    public SoulSiphonItem(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, settings);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (target.isDead() && !target.getWorld().isClient) {
            ItemEntity soul = new ItemEntity(target.getWorld(), target.getX(), target.getY(), target.getZ(),
                    new ItemStack(ModItems.SOUL_FRAGMENT));
            target.getWorld().spawnEntity(soul);
        }
        return super.postHit(stack, target, attacker);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!user.getWorld().isClient && user.getInventory().contains(new ItemStack(ModItems.SOUL_FRAGMENT))) {
            int fragmentIndex= user.getInventory().indexOf(new ItemStack(ModItems.SOUL_FRAGMENT));
            user.getInventory().getStack(fragmentIndex).decrement(1);
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 100), null);
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 100, 1), null);
        }
        return super.use(world, user, hand);
    }
}
