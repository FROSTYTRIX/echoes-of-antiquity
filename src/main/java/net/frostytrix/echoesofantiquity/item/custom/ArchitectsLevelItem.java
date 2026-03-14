package net.frostytrix.echoesofantiquity.item.custom;

import net.frostytrix.echoesofantiquity.util.ModTags;
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

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos centerPos = context.getBlockPos(); // Le bloc cliqué (niveau de référence)
        PlayerEntity player = context.getPlayer();

        if (!world.isClient && player != null) {
            // On définit le rayon (1 bloc autour du centre = 3x3)
            int radius = 1;

            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {

                    boolean canPlace = player.isCreative() || hasRequiredBlock(player, Blocks.DIRT.asItem());

                    // 1. Définir la position cible au sol
                    BlockPos targetFloor = centerPos.add(x, 0, z);

                    // 2. Définir la position juste au-dessus (la "bosse" à raser)
                    BlockPos targetAbove = targetFloor.up();
                    BlockPos targetAbove2 = targetAbove.up();

                    // --- LOGIQUE DE NETTOYAGE (Rasage) ---
                    if (world.getBlockState(targetAbove).isIn(ModTags.Blocks.NATURAL_BLOCKS_LEVEL)
                            || world.getFluidState(targetAbove).isIn(FluidTags.WATER) || world.getFluidState(targetAbove).isIn(FluidTags.LAVA)) {
                        world.breakBlock(targetAbove, true, player);
                        context.getStack().damage(1,  (ServerWorld) world, (ServerPlayerEntity) player,
                                item -> player.sendEquipmentBreakStatus(item, EquipmentSlot.MAINHAND));
                    }

                    if (world.getBlockState(targetAbove2).isIn(ModTags.Blocks.NATURAL_BLOCKS_LEVEL)
                            || world.getFluidState(targetAbove2).isIn(FluidTags.WATER) || world.getFluidState(targetAbove2).isIn(FluidTags.LAVA)) {
                        world.breakBlock(targetAbove2, true, player);
                        context.getStack().damage(1,  (ServerWorld) world, (ServerPlayerEntity) player,
                                item -> player.sendEquipmentBreakStatus(item, EquipmentSlot.MAINHAND));
                    }

                    // --- LOGIQUE DE REMPLISSAGE (Trou) ---
                    if (world.getBlockState(targetFloor).isIn(ModTags.Blocks.NATURAL_BLOCKS_LEVEL)
                    || world.getFluidState(targetFloor).isIn(FluidTags.WATER) || world.getFluidState(targetFloor).isIn(FluidTags.LAVA)
                    || !world.getBlockState(targetFloor).isOf(Blocks.AIR) || !world.getBlockState(targetFloor).isOf(Blocks.CAVE_AIR)
                            || !world.getBlockState(targetFloor).isOf(Blocks.VOID_AIR)) {

                        // Ici on place de la terre par défaut, ou le bloc souhaité
                        if (!world.getBlockState(targetFloor).isOf(Blocks.DIRT) && canPlace) {
                            world.breakBlock(targetFloor, true, player);
                            world.setBlockState(targetFloor, Blocks.DIRT.getDefaultState());
                            if (!player.isCreative()) {
                                consumeItem(player, Blocks.DIRT.asItem());
                            }
                            context.getStack().damage(1,  (ServerWorld) world, (ServerPlayerEntity) player,
                                    item -> player.sendEquipmentBreakStatus(item, EquipmentSlot.MAINHAND));
                        }
                    }
                }
            }

            // Ajouter un petit cooldown pour le "Game Feel"
            player.getItemCooldownManager().set(this, 5); // 0.5 seconde
        }

        return ActionResult.SUCCESS;
    }
}
