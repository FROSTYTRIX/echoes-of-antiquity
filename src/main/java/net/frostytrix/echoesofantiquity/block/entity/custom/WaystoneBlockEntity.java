package net.frostytrix.echoesofantiquity.block.entity.custom;

import net.frostytrix.echoesofantiquity.block.custom.WaystoneBlock;
import net.frostytrix.echoesofantiquity.block.entity.ModBlockEntities;
import net.frostytrix.echoesofantiquity.util.TeleportUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.UUID;

public class WaystoneBlockEntity extends BlockEntity {

    private UUID ownerUuid = null;

    public WaystoneBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.WAYSTONE_BE, pos, state);
    }


    public void setOwner(UUID uuid) {
        this.ownerUuid = uuid;
        this.markDirty();
    }

    public UUID getOwner() {
        return this.ownerUuid;
    }


    public boolean isActive() {
        BlockState state = this.getCachedState();
        if (state.contains(WaystoneBlock.ACTIVE)) {
            return state.get(WaystoneBlock.ACTIVE);
        }
        return false;
    }


    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);

        if (this.ownerUuid != null) {
            nbt.putUuid("WaystoneOwner", this.ownerUuid);
        }
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);

        if (nbt.containsUuid("WaystoneOwner")) {
            this.ownerUuid = nbt.getUuid("WaystoneOwner");
        }
    }

    public void tick(World world, BlockPos pos, BlockState state) {
        if (world.isClient) return;

        if (state.get(WaystoneBlock.ACTIVE) && this.ownerUuid != null) {
            PlayerEntity player = world.getPlayerByUuid(this.ownerUuid);
            if (player != null && player.getHealth() <= 5f && !player.isDead()) {
                Optional<Vec3d> safeSpot = TeleportUtils.findSafeTeleportSpot(world, pos);

                if (safeSpot.isPresent()) {

                    player.fallDistance = 0;
                    performRecall(world, player, safeSpot.get());
                    world.breakBlock(pos, false);
                    world.playSound(null, pos, SoundEvents.BLOCK_STONE_BREAK, SoundCategory.BLOCKS, 1.0f, 0.5f);
                } else {
                    player.sendMessage(Text.literal("§l§cCouldn't find a safe recall spot"), true);
                    world.breakBlock(pos, false);
                    world.playSound(null, pos, SoundEvents.BLOCK_STONE_BREAK, SoundCategory.BLOCKS, 1.0f, 0.5f);
                }
            }
        }
    }

    private static void performRecall(World world, PlayerEntity player, Vec3d destination) {
        // 5 hearts
        player.setHealth(10.0f);

        player.teleport(destination.x, destination.y, destination.z, false);

        world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0f, 1.0f);
    }
}