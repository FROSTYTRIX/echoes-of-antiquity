package net.frostytrix.echoesofantiquity.item.custom;

import net.frostytrix.echoesofantiquity.component.MagnetMode;
import net.frostytrix.echoesofantiquity.component.ModDataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class MagnetRingItem extends Item {
    public static final int RANGE = 4;
    public static final float SPEED = 0.02f;

    public MagnetRingItem(Settings settings) {
        super(settings);
    }

    private static MagnetMode modeOf(ItemStack stack) {
        return stack.getOrDefault(ModDataComponentTypes.MAGNET_MODE, MagnetMode.OFF);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        MagnetMode next = modeOf(stack).next();
        stack.set(ModDataComponentTypes.MAGNET_MODE, next);

        if (!world.isClient) {
            world.playSound(null, user.getBlockPos(), SoundEvents.BLOCK_LODESTONE_PLACE, SoundCategory.PLAYERS,
                    0.4f, next == MagnetMode.OFF ? 0.8f : 1.4f);
        }
        return TypedActionResult.success(stack, world.isClient());
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (world.isClient || !(entity instanceof PlayerEntity player)) {
            return;
        }

        MagnetMode mode = modeOf(stack);
        if (mode == MagnetMode.OFF) {
            return;
        }

        Box area = player.getBoundingBox().expand(RANGE);
        List<ItemEntity> entities = world.getNonSpectatingEntities(ItemEntity.class, area);

        for (ItemEntity itemEntity : entities) {
            double distanceSq = itemEntity.squaredDistanceTo(player);
            if (distanceSq <= 0.5) {
                continue;
            }

            Vec3d direction = player.getPos().add(0, 0.75, 0).subtract(itemEntity.getPos()).normalize();
            double pullStrength = 0.05 + (RANGE / (distanceSq + 1)) * SPEED;
            if (mode == MagnetMode.REPULSING) {
                pullStrength = -pullStrength;
            }

            itemEntity.setVelocity(itemEntity.getVelocity().multiply(0.95).add(direction.multiply(pullStrength)));
            itemEntity.velocityDirty = true;
            itemEntity.velocityModified = true;
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable("tooltip.echoesofantiquity.magnetic_ring")
                .append(Text.translatable(modeOf(stack).translationKey())));
        super.appendTooltip(stack, context, tooltip, type);
    }
}
