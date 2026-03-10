package net.frostytrix.echoesofantiquity.item.custom;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
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
        // 1. Create our backup copy to prevent the item from deleting itself
        ItemStack stackToKeep = user.getStackInHand(hand).copy();

        // 2. Check if the player is holding Shift (sneaking)
        if (user.isSneaking()) {

            // Delegate the action to the vanilla EMPTY bucket.
            // This handles the raycasting, removing the water block, and playing the slurp sound!
            TypedActionResult<ItemStack> pickupResult = Items.BUCKET.use(world, user, hand);

            // If it successfully picked up water...
            if (pickupResult.getResult().isAccepted()) {
                // Return our infinite bucket so the game doesn't hand the player a normal water bucket
                return TypedActionResult.success(stackToKeep, world.isClient());
            }

            // If they shift-clicked the air, just pass
            return TypedActionResult.pass(stackToKeep);
        }

        // 3. Normal behavior (Not sneaking) - place water
        TypedActionResult<ItemStack> placeResult = super.use(world, user, hand);

        if (placeResult.getResult().isAccepted()) {
            return TypedActionResult.success(stackToKeep, world.isClient());
        }

        return placeResult;
    }
}
