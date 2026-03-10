package net.frostytrix.echoesofantiquity.item.custom;

import net.minecraft.block.BlockState;
import net.minecraft.block.FluidDrainable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public class InfiniteWaterBucketItem extends BucketItem {


    public InfiniteWaterBucketItem(Settings settings) {
        super(Fluids.WATER, settings);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stackToKeep = user.getStackInHand(hand);

        if (user.isSneaking()) {
            // 1. Shoot a raycast looking ONLY for fluids
            BlockHitResult hitResult = raycast(world, user, RaycastContext.FluidHandling.SOURCE_ONLY);

            if (hitResult.getType() == HitResult.Type.BLOCK) {
                BlockPos pos = hitResult.getBlockPos();
                BlockState state = world.getBlockState(pos);

                // 2. Check if the block is a fluid that can be drained
                if (state.getBlock() instanceof FluidDrainable drainable) {
                    // 3. Drain it!
                    ItemStack drained = drainable.tryDrainFluid(user, world, pos, state);

                    if (!drained.isEmpty()) {
                        // Play the slurp sound manually
                        world.playSound(user, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0f, 1.0f);
                        return TypedActionResult.success(stackToKeep, world.isClient());
                    }
                }
            }
            return TypedActionResult.pass(stackToKeep);
        }

        // Normal behavior (Not sneaking) - place water
        TypedActionResult<ItemStack> placeResult = super.use(world, user, hand);
        if (placeResult.getResult().isAccepted()) {
            return TypedActionResult.success(stackToKeep, world.isClient());
        }

        return placeResult;
    }
}
