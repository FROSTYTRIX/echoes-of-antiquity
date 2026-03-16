package net.frostytrix.echoesofantiquity.item.custom;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
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

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        NbtComponent customData = user.getStackInHand(hand).getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT);
        NbtCompound nbt = customData.copyNbt();

        if (nbt.getBoolean("attracting")){nbt.putBoolean("attracting", false);}
        else {nbt.putBoolean("attracting", true);}

        user.getStackInHand(hand).set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(nbt));

        return TypedActionResult.success(user.getStackInHand(hand));
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isClient && entity instanceof PlayerEntity player) {
            Box area = player.getBoundingBox().expand(RANGE);
            List<ItemEntity> entities = world.getNonSpectatingEntities(ItemEntity.class, area);
            NbtComponent customData = stack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT);
            NbtCompound nbt = customData.copyNbt();

            for (ItemEntity itemEntity : entities) {
                double distanceSq = itemEntity.squaredDistanceTo(player);

                if (distanceSq > 0.5 && nbt.getBoolean("attracting")) {
                    Vec3d direction = player.getPos().add(0, 0.75, 0).subtract(itemEntity.getPos()).normalize();

                    double pullStrength = 0.05 + (RANGE / (distanceSq + 1)) * 0.02;

                    Vec3d newVelocity = itemEntity.getVelocity().multiply(0.95).add(direction.multiply(pullStrength));
                    itemEntity.setVelocity(newVelocity);

                    itemEntity.velocityDirty = true;
                    itemEntity.velocityModified = true;
                } else {
                    Vec3d direction = player.getPos().add(0, 0.75, 0).subtract(itemEntity.getPos()).normalize();

                    double pullStrength = - 0.05 - (RANGE / (distanceSq + 1)) * 0.02;

                    Vec3d newVelocity = itemEntity.getVelocity().multiply(0.95).add(direction.multiply(pullStrength));
                    itemEntity.setVelocity(newVelocity);

                    itemEntity.velocityDirty = true;
                    itemEntity.velocityModified = true;
                }
            }
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        NbtComponent customData = stack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT);
        NbtCompound nbt = customData.copyNbt();

        boolean attracting = nbt.getBoolean("attracting");

        if (attracting) {tooltip.add((Text.translatable("tooltip.echoesofantiquity.magnetic_ring").append(Text.translatable("tooltip.echoesofantiquity.magnetic_ring.attracting"))));}
        else {tooltip.add((Text.translatable("tooltip.echoesofantiquity.magnetic_ring").append(Text.translatable("tooltip.echoesofantiquity.magnetic_ring.repulsing"))));}
        super.appendTooltip(stack, context, tooltip, type);
    }
}
