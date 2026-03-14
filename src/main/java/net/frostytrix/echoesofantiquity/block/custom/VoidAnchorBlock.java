package net.frostytrix.echoesofantiquity.block.custom;

import net.frostytrix.echoesofantiquity.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class VoidAnchorBlock extends Block {
    public VoidAnchorBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(PURPLE, false));
    }

    public static final BooleanProperty PURPLE = BooleanProperty.of("purple");

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(PURPLE);
    }

    @Override
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {

        if (stack.isOf(Items.MAGENTA_DYE) && !state.get(PURPLE)) {
            if (!world.isClient) {
                world.setBlockState(pos, state.with(PURPLE, true));
                world.playSound(null, pos, SoundEvents.ITEM_DYE_USE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                if (!player.getAbilities().creativeMode) {
                    stack.decrement(1);
                }
            }
            return ItemActionResult.SUCCESS;
        }


        if (stack.isOf(Items.WATER_BUCKET) && state.get(PURPLE)) {
            if (!world.isClient) {
                world.setBlockState(pos, state.with(PURPLE, false));
                world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);

                if (!player.getAbilities().creativeMode) {
                    player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Items.BUCKET)));
                }
            }
            return ItemActionResult.SUCCESS;
        }

        if (stack.isOf(ModItems.INFINITE_WATER_BUCKET) && state.get(PURPLE)) {
            if (!world.isClient) {
                world.setBlockState(pos, state.with(PURPLE, false));
                world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }
            return ItemActionResult.SUCCESS;
        }

        return ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }
}
