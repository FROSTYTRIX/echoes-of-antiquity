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
        Block clickedBlock = world.getBlockState(context.getBlockPos()).getBlock();
        BlockPos hasCoords = context.getStack().get(ModDataComponentTypes.COORDINATES);

        if (!world.isClient()) {
            if (clickedBlock == ModBlocks.VOID_ANCHOR ) {
                if (hasCoords == null) {
                    world.playSound(null, context.getBlockPos(), SoundEvents.ITEM_LODESTONE_COMPASS_LOCK, SoundCategory.BLOCKS);

                    context.getStack().set(ModDataComponentTypes.COORDINATES, context.getBlockPos());
                    return ActionResult.SUCCESS;
                }
                assert player != null;
                if (player.isSneaking()) {
                    world.playSound(null, context.getBlockPos(), SoundEvents.ITEM_LODESTONE_COMPASS_LOCK, SoundCategory.BLOCKS);

                    context.getStack().set(ModDataComponentTypes.COORDINATES, context.getBlockPos());
                    return ActionResult.SUCCESS;
                }
                return ActionResult.PASS;
            }

            ServerWorld worldServer = (ServerWorld) world;

            if (hasCoords != null){

                if (world.getBlockState(hasCoords).getBlock() == ModBlocks.VOID_ANCHOR && world.getBlockState(hasCoords.up()).isAir() && world.getBlockState(hasCoords.up(2)).isAir()) {

                    assert player != null;
                    player.teleport(hasCoords.getX() + 0.5, hasCoords.up().getY(), hasCoords.getZ() + 0.5, ParticleTypes.PORTAL.shouldAlwaysSpawn());

                    world.playSound(null, hasCoords.up(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0f, 1.0f);
                    worldServer.spawnParticles(ParticleTypes.PORTAL, player.getX(), player.getY() + 1, player.getZ(), 20, 0.5, 0.5, 0.5, 0.1);

                    context.getStack().damage(1, (ServerWorld) world, (ServerPlayerEntity) player,
                            item -> player.sendEquipmentBreakStatus(item, EquipmentSlot.MAINHAND));
                    player.getItemCooldownManager().set(this, 20);
                }
            }
            return ActionResult.SUCCESS;
        }
        return ActionResult.FAIL;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {

        ItemStack stack = player.getStackInHand(hand);
        BlockPos hasCoords = stack.get(ModDataComponentTypes.COORDINATES);

        if (!world.isClient()) {

            ServerWorld worldServer = (ServerWorld) world;

            if (hasCoords != null){

                if (world.getBlockState(hasCoords).getBlock() == ModBlocks.VOID_ANCHOR && world.getBlockState(hasCoords.up()).isAir() && world.getBlockState(hasCoords.up(2)).isAir()) {

                player.teleport(hasCoords.getX() + 0.5, hasCoords.up().getY(), hasCoords.getZ() + 0.5, ParticleTypes.PORTAL.shouldAlwaysSpawn());

                world.playSound(null, hasCoords.up(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0f, 1.0f);
                worldServer.spawnParticles(ParticleTypes.PORTAL, player.getX(), player.getY() + 1, player.getZ(), 20, 0.5, 0.5, 0.5, 0.1);

                stack.damage(1, (ServerWorld) world, (ServerPlayerEntity) player,
                        item -> player.sendEquipmentBreakStatus(item, EquipmentSlot.MAINHAND));
                    player.getItemCooldownManager().set(this, 20);
                    return TypedActionResult.success(stack);
                }
            }
        }
        return TypedActionResult.fail(stack);
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
