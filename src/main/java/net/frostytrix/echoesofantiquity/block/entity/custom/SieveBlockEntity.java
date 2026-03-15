package net.frostytrix.echoesofantiquity.block.entity.custom;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.frostytrix.echoesofantiquity.block.entity.ImplementedInventory;
import net.frostytrix.echoesofantiquity.block.entity.ModBlockEntities;
import net.frostytrix.echoesofantiquity.item.ModItems;
import net.frostytrix.echoesofantiquity.recipe.ModRecipes;
import net.frostytrix.echoesofantiquity.recipe.sieve.SievePool;
import net.frostytrix.echoesofantiquity.recipe.sieve.SieveRecipe;
import net.frostytrix.echoesofantiquity.recipe.sieve.SieveRecipeInput;
import net.frostytrix.echoesofantiquity.recipe.sieve.SieveResult;
import net.frostytrix.echoesofantiquity.screen.custom.SieveScreenHandler;
import net.frostytrix.echoesofantiquity.sound.ModSounds;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.Item;
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

    private Item cachedRenderItem = null;
    private boolean cachedRecipeResult = false;

    public boolean hasValidRecipeForRender() {
        if (this.world == null) return false;
        ItemStack currentStack = this.inventory.get(INPUT_SLOT);

        if (currentStack.isEmpty()) {
            this.cachedRenderItem = null;
            return false;
        }

        // If the item in the slot is different from the last time we checked,
        // we ask the RecipeManager. Otherwise, we just return the saved answer!
        if (currentStack.getItem() != this.cachedRenderItem) {
            this.cachedRenderItem = currentStack.getItem();
            this.cachedRecipeResult = this.world.getRecipeManager()
                    .getFirstMatch(ModRecipes.SIEVE_TYPE, new SieveRecipeInput(currentStack), this.world)
                    .isPresent();
        }

        return this.cachedRecipeResult;
    }

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
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);

        // Safely wipe the client memory before reading so the 3D block doesn't get stuck
        for (int i = 0; i < this.inventory.size(); i++) {
            this.inventory.set(i, ItemStack.EMPTY);
        }
        this.progress = nbt.getInt("sieve.progress");
        this.maxProgress = nbt.getInt("sieve.maxProgress");
        Inventories.readNbt(nbt, this.inventory, registryLookup);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        Inventories.writeNbt(nbt, this.inventory, registryLookup);

        nbt.putInt("sieve.progress", this.progress);
        nbt.putInt("sieve.maxProgress", this.maxProgress);
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
        // --- 1. CLIENT SIDE (Visuals Only) ---
        if (world.isClient) {
            if (this.progress > 0 && this.progress < this.maxProgress) {
                this.progress++;
            }
            return;
        }

        // --- 2. SERVER SIDE (Math and Crafting) ---
        if (hasRecipe() && hasOutputSpace() && hasFuel()) {
            increaseSiftingProcess();
            markDirty(world, pos, state);

            if (this.progress == 1) {
                world.playSound(null, pos, ModSounds.SIFTING, SoundCategory.BLOCKS, 1.0f, 0.8f);
                world.updateListeners(pos, state, state, 3);
            }

            if (hasSiftingFinished()) {
                craftItem();
                resetProgress();
                world.updateListeners(pos, state, state, 3);
            }
        } else {
            if (this.progress > 0) {
                resetProgress();
                world.updateListeners(pos, state, state, 3);
            }
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

        // 1. Consume Fuel and Input directly
        this.inventory.get(SOUL_FRAGMENT_SLOT).decrement(1);
        this.inventory.get(INPUT_SLOT).decrement(1);

        // 2. Roll independent probabilities
        for (SieveResult result : recipe.results()) {
            if (this.world.random.nextFloat() <= result.chance()) {
                insertOutput(result.stack().copy());
            }
        }

        // 3. Roll grouped pools
        for (SievePool pool : recipe.pools()) {
            if (!pool.items().isEmpty() && this.world.random.nextFloat() <= pool.chance()) {
                int randomIndex = this.world.random.nextInt(pool.items().size());
                insertOutput(pool.items().get(randomIndex).copy());
            }
        }
    }

    private void insertOutput(ItemStack stackToInsert) {
        for (int i = 2; i <= 7; i++) {
            ItemStack slotStack = this.inventory.get(i);

            if (slotStack.isEmpty()) {
                this.inventory.set(i, stackToInsert);
                return;
            } else if (ItemStack.areItemsEqual(slotStack, stackToInsert) && slotStack.getCount() + stackToInsert.getCount() <= slotStack.getMaxCount()) {
                slotStack.increment(stackToInsert.getCount());
                return;
            }
        }
    }

    public ItemStack getInputStack() {
        return this.inventory.get(INPUT_SLOT);
    }

    public int getProgress() {
        return this.progress;
    }

    public int getMaxProgress() {
        return this.maxProgress;
    }
}
