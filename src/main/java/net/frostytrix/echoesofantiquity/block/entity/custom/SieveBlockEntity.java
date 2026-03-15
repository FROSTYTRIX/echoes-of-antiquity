package net.frostytrix.echoesofantiquity.block.entity.custom;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.frostytrix.echoesofantiquity.block.entity.ImplementedInventory;
import net.frostytrix.echoesofantiquity.block.entity.ModBlockEntities;
import net.frostytrix.echoesofantiquity.item.ModItems;
import net.frostytrix.echoesofantiquity.recipe.ModRecipes;
import net.frostytrix.echoesofantiquity.recipe.SieveRecipe;
import net.frostytrix.echoesofantiquity.recipe.SieveRecipeInput;
import net.frostytrix.echoesofantiquity.recipe.SieveResult;
import net.frostytrix.echoesofantiquity.screen.custom.SieveScreenHandler;
import net.frostytrix.echoesofantiquity.sound.ModSounds;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class SieveBlockEntity extends BlockEntity implements ImplementedInventory, ExtendedScreenHandlerFactory<BlockPos>, SidedInventory {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(8, ItemStack.EMPTY);

    private static final int INPUT_SLOT = 0;
    private static final int SOUL_FRAGMENT_SLOT = 1;

    protected final PropertyDelegate delegate;
    private int progress = 0;
    private int maxProgress = 72;



    public SieveBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SIEVE_BE, pos, state);
        this.delegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> SieveBlockEntity.this.progress;
                    case 1 -> SieveBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0: SieveBlockEntity.this.progress = value; break; // Added break
                    case 1: SieveBlockEntity.this.maxProgress = value; break; // Added break
                }
            }

            @Override
            public int size() {
                return 2;
            }
        };
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction side) {
        if (side != Direction.DOWN) {
            if (slot == 1 && stack.isOf(ModItems.SOUL_FRAGMENT)){
                return true;
            } else return slot == 0;
        }
        return false;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction side) {
        return side == Direction.DOWN && (slot == 2 || slot == 3 || slot == 4 || slot == 5 || slot == 6 || slot == 7);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        Inventories.writeNbt(nbt, inventory, registryLookup);
        nbt.putInt("sieve.progress", progress);
        nbt.putInt("sieve.max_progress", maxProgress);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        Inventories.readNbt(nbt, inventory, registryLookup);
        progress = nbt.getInt("sieve.progress");
        maxProgress = nbt.getInt("sieve.max_progress");
        super.readNbt(nbt, registryLookup);
    }

    @Override
    public BlockPos getScreenOpeningData(ServerPlayerEntity serverPlayerEntity) {
        return this.pos;
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("block.echoesofantiquity.sieve");
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new SieveScreenHandler(syncId, playerInventory, this, this.delegate);
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

    public void tick(World world, BlockPos pos, BlockState state) {
        if (world.isClient) return;

        if (hasRecipe() && hasFuel()) {
            increaseSiftingProcess();
            markDirty(world, pos, state);

            if (this.progress == 1) {
                world.playSound(
                        null,
                        pos,
                        ModSounds.SIFTING, // Your brand new custom sound!
                        SoundCategory.BLOCKS,
                        1f, // Full volume
                        0.8f  // Normal pitch (no randomization so the length stays exactly the same)
                );
            }

            if (hasSiftingFinished()) {
                craftItem();
                resetProgress();
            }
        } else {
            resetProgress();
            markDirty(world, pos, state);
        }
    }

    private boolean hasFuel() {
        return this.inventory.get(SOUL_FRAGMENT_SLOT).isOf(ModItems.SOUL_FRAGMENT);
    }

    private void resetProgress() {
        this.progress = 0;
    }

    private boolean hasSiftingFinished() {
        return this.progress >= this.maxProgress;
    }

    private void increaseSiftingProcess() {
        this.progress++;
    }

    private boolean hasRecipe() {
        Optional<RecipeEntry<SieveRecipe>> recipe = getCurrentRecipe();
        // The sieve only processes if it has a recipe AND at least some space left
        return recipe.isPresent() && hasOutputSpace();
    }

    private boolean hasOutputSpace() {
        for (int i = 2; i <= 7; i++) {
            ItemStack slotStack = this.inventory.get(i);
            // If we find an empty slot, OR a slot that isn't fully stacked to 64 yet
            if (slotStack.isEmpty() || slotStack.getCount() < slotStack.getMaxCount()) {
                return true;
            }
        }
        // If the loop finishes without returning true, the sieve is 100% full
        return false;
    }

    private Optional<RecipeEntry<SieveRecipe>> getCurrentRecipe() {
        return this.getWorld().getRecipeManager()
                .getFirstMatch(ModRecipes.SIEVE_TYPE, new SieveRecipeInput(inventory.get(INPUT_SLOT)), this.world);
    }

    private void craftItem() {
        Optional<RecipeEntry<SieveRecipe>> recipeEntry = getCurrentRecipe();
        if (recipeEntry.isEmpty()) return;

        SieveRecipe recipe = recipeEntry.get().value();

        // 1. Consume Fuel and Input
        this.inventory.get(SOUL_FRAGMENT_SLOT).decrement(1);
        this.inventory.get(INPUT_SLOT).decrement(1);

        // 2. Roll probabilities for outputs
        for (SieveResult result : recipe.results()) {
            if (this.world.random.nextFloat() <= result.chance()) {
                insertOutput(result.stack().copy());
            }
        }
    }

    private void insertOutput(ItemStack stackToInsert) {
        for (int i = 2; i <= 7; i++) {
            ItemStack slotStack = this.inventory.get(i);

            if (slotStack.isEmpty()) {
                this.inventory.set(i, stackToInsert);
                return; // Placed successfully, stop looking
            } else if (ItemStack.areItemsEqual(slotStack, stackToInsert) && slotStack.getCount() + stackToInsert.getCount() <= slotStack.getMaxCount()) {
                slotStack.increment(stackToInsert.getCount());
                return; // Stacked successfully, stop looking
            }
        }
        // If the code reaches this point, the inventory is full.
        // The item simply vanishes\
    }
}
