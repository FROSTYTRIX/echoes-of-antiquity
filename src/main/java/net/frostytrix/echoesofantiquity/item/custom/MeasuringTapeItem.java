package net.frostytrix.echoesofantiquity.item.custom;

import net.frostytrix.echoesofantiquity.component.MeasuringTapeData;
import net.frostytrix.echoesofantiquity.component.ModDataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.tooltip.TooltipType;
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

    private static MeasuringTapeData dataOf(ItemStack stack) {
        return stack.getOrDefault(ModDataComponentTypes.MEASURING_TAPE, MeasuringTapeData.EMPTY);
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

        MeasuringTapeData data = dataOf(stack);
        stack.set(ModDataComponentTypes.MEASURING_TAPE,
                player.isSneaking() ? data.withSecond(pos) : data.withFirst(pos));

        return ActionResult.SUCCESS;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (world.isClient) {
            return TypedActionResult.success(stack, true);
        }

        MeasuringTapeData data = dataOf(stack);
        stack.set(ModDataComponentTypes.MEASURING_TAPE,
                user.isSneaking() ? data.toggledMode() : data.cleared());

        return TypedActionResult.success(stack);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        MeasuringTapeData data = dataOf(stack);

        data.first().ifPresent(pos -> tooltip.add(Text.translatable("tooltip.echoesofantiquity.measuring_tape.first_pos")
                .append(Text.literal("X: " + pos.getX() + ", Y: " + pos.getY() + ", Z: " + pos.getZ()))));

        data.second().ifPresent(pos -> tooltip.add(Text.translatable("tooltip.echoesofantiquity.measuring_tape.second_pos")
                .append(Text.literal("X: " + pos.getX() + ", Y: " + pos.getY() + ", Z: " + pos.getZ()))));

        if (data.first().isPresent() && data.second().isPresent()) {
            BlockPos first = data.first().get();
            BlockPos second = data.second().get();

            String distance = switch (data.mode()) {
                case VECTOR -> String.format("%.2f", new Vec3d(
                        first.getX() - second.getX(),
                        first.getY() - second.getY(),
                        first.getZ() - second.getZ()).length() + 1);
                case MANHATTAN -> String.valueOf(first.getManhattanDistance(second) + 1);
            };

            tooltip.add(Text.translatable(data.mode().translationKey()).append(distance));
        }
        super.appendTooltip(stack, context, tooltip, type);
    }
}
