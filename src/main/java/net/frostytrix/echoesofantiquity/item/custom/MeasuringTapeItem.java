package net.frostytrix.echoesofantiquity.item.custom;

import net.frostytrix.echoesofantiquity.component.ModDataComponentTypes;
import net.minecraft.client.gui.screen.Screen;
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
import java.util.Map;

public class MeasuringTapeItem extends Item {
    BlockPos firstPos;
    BlockPos secondPos;

    private static final Map<String, String> DISTANCE_STYLE_MAP =
            Map.of(
                    "vector_distance", "manhattan_distance",
                    "manhattan_distance", "vector_distance"
            );

    String usedDistance = "vector_distance";

    public MeasuringTapeItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (!context.getWorld().isClient()){
            if (!context.getPlayer().isSneaking()) {
                firstPos = context.getBlockPos();
            }  else {
                secondPos = context.getBlockPos();
            }
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if  (!world.isClient()) {
            if (user.isSneaking()) {
                usedDistance = DISTANCE_STYLE_MAP.get(usedDistance);
            } else {
                firstPos = null;
                secondPos = null;
            }
        }
        return TypedActionResult.success(user.getStackInHand(hand));
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {


        if (firstPos != null) {
            tooltip.add(Text.translatable("tooltip.echoesofantiquity.measuring_tape.first_pos")
                    .append(Text.literal("X: " +  firstPos.getX() + ", Y: " +  firstPos.getY() + ", Z: " + firstPos.getZ())));
        }
        if (secondPos != null) {
            tooltip.add(Text.translatable("tooltip.echoesofantiquity.measuring_tape.second_pos")
                    .append(Text.literal("X: " +  secondPos.getX() + ", Y: " +  secondPos.getY() + ", Z: " + secondPos.getZ())));
        }
        if (firstPos != null && secondPos != null) {
            Vec3d newVec =  new Vec3d(firstPos.getX() - secondPos.getX(), firstPos.getY() - secondPos.getY(), firstPos.getZ() - secondPos.getZ());
            if (usedDistance.equals("vector_distance")) {
                double distance = newVec.length();
                String rounded = String.format("%.2f", distance);

                tooltip.add(Text.translatable("tooltip.echoesofantiquity.measuring_tape.distance").append(rounded));
            } else if  (usedDistance.equals("manhattan_distance")) {
                int distance = firstPos.getManhattanDistance(secondPos) + 1;

                tooltip.add(Text.translatable("tooltip.echoesofantiquity.measuring_tape.manhattan_distance").append(String.valueOf(distance)));
            }
        }
        super.appendTooltip(stack, context, tooltip, type);
    }
}
