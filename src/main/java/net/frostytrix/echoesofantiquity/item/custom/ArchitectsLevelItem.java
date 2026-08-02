package net.frostytrix.echoesofantiquity.item.custom;

import net.frostytrix.echoesofantiquity.util.ModTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ArchitectsLevelItem extends Item {
    public ArchitectsLevelItem(Settings settings) {
        super(settings);
    }

    private boolean hasRequiredBlock(PlayerEntity player, Item item) {
        return player.getInventory().contains(new ItemStack(item));
    }

    private void consumeItem(PlayerEntity player, Item item) {
        for (int i = 0; i < player.getInventory().size(); i++) {
            ItemStack stack = player.getInventory().getStack(i);
            if (!stack.isEmpty() && stack.isOf(item)) {
                stack.decrement(1);
                break; // Stop after removing one
            }
        }
    }

    /** A bump to shave: natural block or fluid. */
    private boolean isLevelable(World world, BlockPos pos) {
        return world.getBlockState(pos).isIn(ModTags.Blocks.NATURAL_BLOCKS_LEVEL)
                || world.getFluidState(pos).isIn(FluidTags.WATER)
                || world.getFluidState(pos).isIn(FluidTags.LAVA);
    }

    /** A hole to fill: air, fluid or replaceable block. Anything else is left alone. */
    private boolean isHole(World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        return state.isAir() || state.isReplaceable() || !world.getFluidState(pos).isEmpty();
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos centerPos = context.getBlockPos(); // Le bloc cliqué (niveau de référence)
        PlayerEntity player = context.getPlayer();

        // Server-side, real player only.
        if (!(world instanceof ServerWorld serverWorld) || !(player instanceof ServerPlayerEntity serverPlayer)) {
            return ActionResult.SUCCESS;
        }

        int radius = 1;

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {

                BlockPos targetFloor = centerPos.add(x, 0, z);

                BlockPos targetAbove = targetFloor.up();
                BlockPos targetAbove2 = targetAbove.up();

                if (isLevelable(world, targetAbove)) {
                    world.breakBlock(targetAbove, true, player);
                    damageLevel(context.getStack(), serverWorld, serverPlayer);
                }

                if (isLevelable(world, targetAbove2)) {
                    world.breakBlock(targetAbove2, true, player);
                    damageLevel(context.getStack(), serverWorld, serverPlayer);
                }

                boolean canPlace = player.isCreative() || hasRequiredBlock(player, Blocks.DIRT.asItem());

                if (canPlace && isHole(world, targetFloor)) {
                    // Clears tall grass / water before placing
                    world.breakBlock(targetFloor, true, player);
                    world.setBlockState(targetFloor, Blocks.DIRT.getDefaultState());

                    if (!player.isCreative()) {
                        consumeItem(player, Blocks.DIRT.asItem());
                    }
                    damageLevel(context.getStack(), serverWorld, serverPlayer);
                }
            }
        }

        player.getItemCooldownManager().set(this, 5);

        return ActionResult.SUCCESS;
    }

    private void damageLevel(ItemStack stack, ServerWorld world, ServerPlayerEntity player) {
        stack.damage(1, world, player, item -> player.sendEquipmentBreakStatus(item, EquipmentSlot.MAINHAND));
    }
}
