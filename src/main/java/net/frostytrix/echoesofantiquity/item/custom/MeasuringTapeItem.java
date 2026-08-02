package net.frostytrix.echoesofantiquity.item.custom;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class MeasuringTapeItem extends Item {

    public MeasuringTapeItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        ItemStack stack = context.getStack();
        BlockPos pos = context.getBlockPos();
        PlayerEntity player = context.getPlayer();

        // Server-side write; the component syncs to the client for the tooltip.
        if (player == null || context.getWorld().isClient) {
            return ActionResult.SUCCESS;
        }

        NbtComponent customData = stack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT);
        NbtCompound nbt = customData.copyNbt();

        if (!player.isSneaking()) {
            nbt.putInt("FirstX", pos.getX());
            nbt.putInt("FirstY", pos.getY());
            nbt.putInt("FirstZ", pos.getZ());
            nbt.putBoolean("HasFirst", true);
        } else {
            nbt.putInt("SecondX", pos.getX());
            nbt.putInt("SecondY", pos.getY());
            nbt.putInt("SecondZ", pos.getZ());
            nbt.putBoolean("HasSecond", true);
        }

        stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(nbt));

        return ActionResult.SUCCESS;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (world.isClient) {
            return TypedActionResult.success(stack, true);
        }

        NbtComponent customData = stack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT);
        NbtCompound nbt = customData.copyNbt();

        if (user.isSneaking()) {
            String mode = nbt.getString("Mode");
            if (mode.isEmpty() || mode.equals("vector_distance")) {
                nbt.putString("Mode", "manhattan_distance");
            } else {
                nbt.putString("Mode", "vector_distance");
            }
        } else {
            nbt.remove("HasFirst");
            nbt.remove("HasSecond");
        }

        stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(nbt));
        return TypedActionResult.success(stack);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        NbtComponent customData = stack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT);
        NbtCompound nbt = customData.copyNbt();

        boolean hasFirst = nbt.getBoolean("HasFirst");
        boolean hasSecond = nbt.getBoolean("HasSecond");
        String mode = nbt.getString("Mode");
        if (mode.isEmpty()) mode = "vector_distance";

        BlockPos firstPos = null;
        BlockPos secondPos = null;

        if (hasFirst) {
            firstPos = new BlockPos(nbt.getInt("FirstX"), nbt.getInt("FirstY"), nbt.getInt("FirstZ"));
            tooltip.add(Text.translatable("tooltip.echoesofantiquity.measuring_tape.first_pos")
                    .append(Text.literal("X: " + firstPos.getX() + ", Y: " + firstPos.getY() + ", Z: " + firstPos.getZ())));
        }

        if (hasSecond) {
            secondPos = new BlockPos(nbt.getInt("SecondX"), nbt.getInt("SecondY"), nbt.getInt("SecondZ"));
            tooltip.add(Text.translatable("tooltip.echoesofantiquity.measuring_tape.second_pos")
                    .append(Text.literal("X: " + secondPos.getX() + ", Y: " + secondPos.getY() + ", Z: " + secondPos.getZ())));
        }

        if (hasFirst && hasSecond) {
            Vec3d newVec = new Vec3d(firstPos.getX() - secondPos.getX(), firstPos.getY() - secondPos.getY(), firstPos.getZ() - secondPos.getZ());
            if (mode.equals("vector_distance")) {
                double distance = newVec.length();
                String rounded = String.format("%.2f", distance + 1);
                tooltip.add(Text.translatable("tooltip.echoesofantiquity.measuring_tape.distance").append(rounded));
            } else if (mode.equals("manhattan_distance")) {
                int distance = firstPos.getManhattanDistance(secondPos) + 1;
                tooltip.add(Text.translatable("tooltip.echoesofantiquity.measuring_tape.manhattan_distance").append(String.valueOf(distance)));
            }
        }
        super.appendTooltip(stack, context, tooltip, type);
    }
}