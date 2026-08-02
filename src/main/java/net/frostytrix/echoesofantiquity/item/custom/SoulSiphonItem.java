package net.frostytrix.echoesofantiquity.item.custom;

import net.frostytrix.echoesofantiquity.item.ModItems;
import net.frostytrix.echoesofantiquity.util.ModTags;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class SoulSiphonItem extends SwordItem {
    public SoulSiphonItem(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, settings);
    }

    private static final int BUFF_COOLDOWN = 200;

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        // isDead() is only set after onDeath(), which has not run yet here.
        if ((target.isDead() || target.getHealth() <= 0.0F) && !target.getWorld().isClient) {
            if (target.getType().isIn(ModTags.Entities.SOULLESS)) {
                return super.postHit(stack, target, attacker);
            }

            float maxHealth = target.getMaxHealth();
            int fragmentsToDrop = 1 + (int)(maxHealth / 20.0f);

            ItemEntity soulDrop = new ItemEntity(
                    target.getWorld(), target.getX(), target.getY(), target.getZ(),
                    new ItemStack(ModItems.SOUL_FRAGMENT, fragmentsToDrop)
            );
            target.getWorld().spawnEntity(soulDrop);
        }
        return super.postHit(stack, target, attacker);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (user.getItemCooldownManager().isCoolingDown(this)) {
            return TypedActionResult.fail(stack);
        }

        if (!world.isClient) {
            if (!user.getInventory().contains(new ItemStack(ModItems.SOUL_FRAGMENT))) {
                return TypedActionResult.fail(stack);
            }

            int fragmentIndex = user.getInventory().indexOf(new ItemStack(ModItems.SOUL_FRAGMENT));
            user.getInventory().getStack(fragmentIndex).decrement(1);
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 100), null);
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 100, 1), null);

            world.playSound(null, user.getBlockPos(), SoundEvents.PARTICLE_SOUL_ESCAPE.value(),
                    SoundCategory.PLAYERS, 1.0f, 1.0f);
            user.getItemCooldownManager().set(this, BUFF_COOLDOWN);
        }

        return TypedActionResult.success(stack, world.isClient());
    }
}
