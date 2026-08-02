package net.frostytrix.echoesofantiquity.item.custom;

import net.frostytrix.echoesofantiquity.item.ModArmorMaterials;
import net.frostytrix.echoesofantiquity.util.TeleportUtils;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Optional;

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

                NbtComponent nbtComponent = stack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT);
                NbtCompound nbt = nbtComponent.copyNbt();

                if (player.isOnGround()) {

                    // Centered to prevent edge-slipping.
                    double safeX = Math.floor(player.getX()) + 0.5;
                    double safeY = player.getY();
                    double safeZ = Math.floor(player.getZ()) + 0.5;

                    // Walking off a ledge keeps isOnGround() true while the centered column is already void.
                    if (hasGroundBelow(world, BlockPos.ofFloored(safeX, safeY, safeZ))) {

                        boolean shouldUpdate = true;
                        if (nbt.contains("last_safe_x")) {
                            double lastX = nbt.getDouble("last_safe_x");
                            double lastY = nbt.getDouble("last_safe_y");
                            double lastZ = nbt.getDouble("last_safe_z");

                            if (Math.abs(lastX - safeX) < 0.1 && Math.abs(lastY - safeY) < 0.5 && Math.abs(lastZ - safeZ) < 0.1) {
                                shouldUpdate = false;
                            }
                        }

                        if (shouldUpdate) {
                            nbt.putDouble("last_safe_x", safeX);
                            nbt.putDouble("last_safe_y", safeY);
                            nbt.putDouble("last_safe_z", safeZ);
                            stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(nbt));
                        }
                    }
                }

                if (player.getY() < -64) {
                    if (nbt.contains("last_safe_x")) {
                        double safeX = nbt.getDouble("last_safe_x");
                        double safeY = nbt.getDouble("last_safe_y");
                        double safeZ = nbt.getDouble("last_safe_z");

                        // Stored ground may have been mined since; otherwise the player falls again in a loop.
                        BlockPos rescuePos = BlockPos.ofFloored(safeX, safeY, safeZ);
                        if (!hasGroundBelow(world, rescuePos)) {
                            Optional<Vec3d> fallback = TeleportUtils.findSafeTeleportSpot(world, rescuePos);
                            if (fallback.isPresent()) {
                                safeX = fallback.get().x;
                                safeY = fallback.get().y;
                                safeZ = fallback.get().z;
                            }
                        }

                        player.teleport(safeX, safeY + 0.2, safeZ, ParticleTypes.PORTAL.shouldAlwaysSpawn());

                        player.setVelocity(0, 0, 0);
                        player.velocityModified = true;
                        player.fallDistance = 0.0f;

                        world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0f, 1.0f);

                        for (int i = 0; i < 4; i++) {
                            ItemStack armorPiece = player.getInventory().getArmorStack(i);

                            if (!armorPiece.isEmpty()) {
                                int maxDamage = armorPiece.getMaxDamage();
                                int damageAmount = maxDamage / 3;
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