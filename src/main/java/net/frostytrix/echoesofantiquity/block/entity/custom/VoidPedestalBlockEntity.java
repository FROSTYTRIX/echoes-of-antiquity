package net.frostytrix.echoesofantiquity.block.entity.custom;

import net.frostytrix.echoesofantiquity.block.custom.VoidPedestalBlock;
import net.frostytrix.echoesofantiquity.block.entity.ImplementedInventory;
import net.frostytrix.echoesofantiquity.block.entity.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class VoidPedestalBlockEntity extends BlockEntity implements ImplementedInventory {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(1, ItemStack.EMPTY);
    private float rotation = 0;
    public static final int noTPRadius = 20;

    public VoidPedestalBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.VOID_PEDESTAL_BE, pos, state);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    public boolean isActivated() {
        return this.inventory.getFirst().getItem() == Items.ENDER_EYE;
    }

    public float getRenderingRotation(BlockPos pos, World world, ItemStack stack) {
        PlayerEntity playerEntity = world.getClosestPlayer(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 3.0, false);

        if (playerEntity != null && isActivated()) {
            double d = playerEntity.getX() - (pos.getX() + 0.5);
            double e = playerEntity.getZ() - (pos.getZ() + 0.5);
            rotation = (float) MathHelper.atan2(e, d);
            return -((rotation + MathHelper.HALF_PI) * 180)/MathHelper.PI;
        } else {
            rotation += 0.5f;
            if (rotation >= 360) {
                rotation = 0;
                return rotation;
            }
        }
        return rotation;
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        Inventories.writeNbt(nbt, inventory, registryLookup);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        Inventories.readNbt(nbt, inventory, registryLookup);
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return createNbt(registryLookup);
    }

    public static void tick(World world, BlockPos pos, BlockState state, VoidPedestalBlockEntity be) {
        if (world.isClient) return;

        // 1. Check for the specific item (e.g., an Ender Eye)
        boolean hasItem = be.getStack(0).isOf(Items.ENDER_EYE);

        // 2. Update BlockState if it changed (optimization: only update on change)
        if (state.get(VoidPedestalBlock.ACTIVE) != hasItem) {
            world.setBlockState(pos, state.with(VoidPedestalBlock.ACTIVE, hasItem), 3);
        }

        // 3. If active, suppress entities
        if (hasItem) {
            Box area = new Box(pos).expand(noTPRadius);
            List<LivingEntity> entities = world.getNonSpectatingEntities(LivingEntity.class, area);

            for (Entity entity : entities) {
                entity.addCommandTag("void_pedestal_suppressed");
            }
        }
    }
}
