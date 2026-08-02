package net.frostytrix.echoesofantiquity.item.custom;

import net.frostytrix.echoesofantiquity.component.ModDataComponentTypes;
import net.frostytrix.echoesofantiquity.config.ModConfig;
import net.frostytrix.echoesofantiquity.item.ModArmorMaterials;
import net.frostytrix.echoesofantiquity.util.TeleportUtils;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class VoidChainmailChestplateItem extends ModArmorItem {

    public VoidChainmailChestplateItem(RegistryEntry<ArmorMaterial> material, Type type, Settings settings) {
        super(material, type, settings);
    }

    /** Whether anything can be stood on right below. */
    private boolean hasGroundBelow(World world, BlockPos pos) {
        BlockPos below = pos.down();
        return !world.getBlockState(below).getCollisionShape(world, below).isEmpty();
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);

        if (!world.isClient() && entity instanceof PlayerEntity player) {

            // 0 = Boots, 1 = Leggings, 2 = Chestplate, 3 = Helmet
            if (player.getInventory().getArmorStack(2) != stack) {
                return;
            }

            if (hasFullSuitOfArmorOn(player) && hasCorrectArmorOn(ModArmorMaterials.VOID_CHAINMAIL_ARMOR_MATERIAL, player)) {

                Vec3d lastSafe = stack.get(ModDataComponentTypes.LAST_SAFE_POS);

                if (player.isOnGround()) {

                    // Centered to prevent edge-slipping.
                    Vec3d safe = new Vec3d(
                            Math.floor(player.getX()) + 0.5,
                            player.getY(),
                            Math.floor(player.getZ()) + 0.5);

                    // Walking off a ledge keeps isOnGround() true while the centered column is already void.
                    if (hasGroundBelow(world, BlockPos.ofFloored(safe))) {

                        boolean shouldUpdate = lastSafe == null
                                || Math.abs(lastSafe.x - safe.x) >= 0.1
                                || Math.abs(lastSafe.y - safe.y) >= 0.5
                                || Math.abs(lastSafe.z - safe.z) >= 0.1;

                        if (shouldUpdate) {
                            stack.set(ModDataComponentTypes.LAST_SAFE_POS, safe);
                            lastSafe = safe;
                        }
                    }
                }

                if (player.getY() < -64) {
                    if (lastSafe != null) {
                        Vec3d destination = lastSafe;

                        // Stored ground may have been mined since; otherwise the player falls again in a loop.
                        BlockPos rescuePos = BlockPos.ofFloored(destination);
                        if (!hasGroundBelow(world, rescuePos)) {
                            destination = TeleportUtils.findSafeTeleportSpot(world, rescuePos).orElse(destination);
                        }

                        player.teleport(destination.x, destination.y + 0.2, destination.z, ParticleTypes.PORTAL.shouldAlwaysSpawn());

                        player.setVelocity(0, 0, 0);
                        player.velocityModified = true;
                        player.fallDistance = 0.0f;

                        world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0f, 1.0f);

                        for (int i = 0; i < 4; i++) {
                            ItemStack armorPiece = player.getInventory().getArmorStack(i);

                            if (!armorPiece.isEmpty()) {
                                int maxDamage = armorPiece.getMaxDamage();
                                int damageAmount = maxDamage / ModConfig.get().voidRescueDurabilityDivisor;
                                int currentDamage = armorPiece.getOrDefault(DataComponentTypes.DAMAGE, 0);

                                if (currentDamage + damageAmount >= maxDamage) {
                                    armorPiece.decrement(1);
                                    world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.PLAYERS, 1.0f, 1.0f);
                                } else {
                                    armorPiece.set(DataComponentTypes.DAMAGE, currentDamage + damageAmount);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}