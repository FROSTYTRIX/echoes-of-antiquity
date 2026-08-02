package net.frostytrix.echoesofantiquity.item.custom;

import net.frostytrix.echoesofantiquity.block.ModBlocks;
import net.frostytrix.echoesofantiquity.component.ModDataComponentTypes;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class StaticPearlItem extends Item {
    public StaticPearlItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        PlayerEntity player = context.getPlayer();
        ItemStack stack = context.getStack();
        Block clickedBlock = world.getBlockState(context.getBlockPos()).getBlock();
        BlockPos hasCoords = stack.get(ModDataComponentTypes.COORDINATES);

        // Server-side, real player only.
        if (!(world instanceof ServerWorld serverWorld) || !(player instanceof ServerPlayerEntity serverPlayer)) {
            return ActionResult.SUCCESS;
        }

        if (clickedBlock == ModBlocks.VOID_ANCHOR) {
            // Bind, or re-bind while sneaking
            if (hasCoords == null || player.isSneaking()) {
                world.playSound(null, context.getBlockPos(), SoundEvents.ITEM_LODESTONE_COMPASS_LOCK, SoundCategory.BLOCKS);
                stack.set(ModDataComponentTypes.COORDINATES, context.getBlockPos());
                return ActionResult.SUCCESS;
            }
            return ActionResult.PASS;
        }

        tryTeleport(serverWorld, serverPlayer, stack, hasCoords);
        return ActionResult.SUCCESS;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        BlockPos hasCoords = stack.get(ModDataComponentTypes.COORDINATES);

        if (!(world instanceof ServerWorld serverWorld) || !(player instanceof ServerPlayerEntity serverPlayer)) {
            return TypedActionResult.pass(stack);
        }

        if (tryTeleport(serverWorld, serverPlayer, stack, hasCoords)) {
            return TypedActionResult.success(stack);
        }
        return TypedActionResult.fail(stack);
    }

    /** @return true if the teleport happened. */
    private boolean tryTeleport(ServerWorld world, ServerPlayerEntity player, ItemStack stack, BlockPos anchorPos) {
        if (anchorPos == null) {
            return false;
        }

        boolean anchorIntact = world.getBlockState(anchorPos).getBlock() == ModBlocks.VOID_ANCHOR;
        boolean spaceIsFree = world.getBlockState(anchorPos.up()).isAir() && world.getBlockState(anchorPos.up(2)).isAir();

        if (!anchorIntact || !spaceIsFree) {
            return false;
        }

        player.teleport(anchorPos.getX() + 0.5, anchorPos.up().getY(), anchorPos.getZ() + 0.5, ParticleTypes.PORTAL.shouldAlwaysSpawn());

        world.playSound(null, anchorPos.up(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0f, 1.0f);
        world.spawnParticles(ParticleTypes.PORTAL, player.getX(), player.getY() + 1, player.getZ(), 20, 0.5, 0.5, 0.5, 0.1);

        stack.damage(1, world, player, item -> player.sendEquipmentBreakStatus(item, EquipmentSlot.MAINHAND));
        player.getItemCooldownManager().set(this, 20);
        return true;
    }

    @Override
    public boolean hasGlint(ItemStack stack) {return stack.get(ModDataComponentTypes.COORDINATES) != null;}

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        BlockPos hasCoords = stack.get(ModDataComponentTypes.COORDINATES);
        if (!world.isClient() && hasCoords != null) {
            if (world.getBlockState(hasCoords).getBlock() != ModBlocks.VOID_ANCHOR) {
                stack.set(ModDataComponentTypes.COORDINATES, null);
            }
        }
    }
}
