package net.frostytrix.echoesofantiquity.block.entity.custom;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.frostytrix.echoesofantiquity.block.custom.UncrafterBlock;
import net.frostytrix.echoesofantiquity.block.entity.ImplementedInventory;
import net.frostytrix.echoesofantiquity.block.entity.ModBlockEntities;
import net.frostytrix.echoesofantiquity.screen.custom.UncrafterScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class UncrafterBlockEntity extends BlockEntity implements ImplementedInventory, ExtendedScreenHandlerFactory<BlockPos> {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(2, ItemStack.EMPTY);

    // Keep track of the input type to avoid re-scanning the entire registry
    private ItemStack cachedInputType = ItemStack.EMPTY;

    // NEW: Cache ALL valid recipes for the item, not just the ingredients of one
    private List<RecipeEntry<?>> cachedValidRecipes = null;

    // Pre-roll the random item so we can check if it fits
    private ItemStack nextOutput = ItemStack.EMPTY;

    private static final int INPUT_SLOT = 0;
    private static final int OUTPUT_SLOT = 1;

    protected final PropertyDelegate delegate;
    private int progress = 0;
    private int maxProgress = 72;

    public UncrafterBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.UNCRAFTER_BE, pos, state);
        this.delegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> UncrafterBlockEntity.this.progress;
                    case 1 -> UncrafterBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0: UncrafterBlockEntity.this.progress = value;
                    case 1: UncrafterBlockEntity.this.maxProgress = value;
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
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        Inventories.writeNbt(nbt, inventory, registryLookup);
        nbt.putInt("uncrafter.progress", progress);
        nbt.putInt("uncrafter.max_progress", maxProgress);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        Inventories.readNbt(nbt, inventory, registryLookup);
        progress = nbt.getInt("uncrafter.progress");
        maxProgress = nbt.getInt("uncrafter.max_progress");
        super.readNbt(nbt, registryLookup);
    }

    @Override
    public BlockPos getScreenOpeningData(ServerPlayerEntity player) {
        return this.pos;
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("block.echoesofantiquity.uncrafter");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new UncrafterScreenHandler(syncId, playerInventory, this, this.delegate);
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
        if (hasRecipe(world)) {
            increaseUncraftingProcess();
            markDirty(world, pos, state);

            if (hasUncraftingFinished()) {
                giveIngredient(world);
                resetProgress();
            }
        } else {
            resetProgress();
        }
    }

    private void resetProgress() {
        this.progress = 0;
        this.maxProgress = 72;
    }

    private boolean hasUncraftingFinished() {
        return progress >= maxProgress;
    }

    private void increaseUncraftingProcess() {
        this.progress++;
    }

    private boolean hasRecipe(World world) {
        ItemStack inputStack = this.getStack(INPUT_SLOT); // Use your input slot index

        // 1. If the input is empty, clear our caches and stop processing
        if (inputStack.isEmpty()) {
            cachedInputType = ItemStack.EMPTY;
            cachedValidRecipes = null;
            nextOutput = ItemStack.EMPTY;
            return false;
        }

        // 2. Cache the RECIPES! Only search the registry if the input item CHANGED.
        if (!inputStack.isOf(cachedInputType.getItem()) || cachedValidRecipes == null) {
            cachedInputType = inputStack.copy();
            cachedValidRecipes = findAllValidRecipes(world, inputStack);
            nextOutput = ItemStack.EMPTY;
        }

        // If there are no valid, non-exploit recipes for this block, do nothing.
        if (cachedValidRecipes.isEmpty()) return false;

        // 3. Pre-roll the next random output if we haven't already
        if (nextOutput.isEmpty()) {
            nextOutput = generateRandomOutput(world);
        }
        if (nextOutput.isEmpty()) return false; // Failsafe

        // 4. Check if the output slot can actually accept this specific random item
        ItemStack outputStack = this.getStack(OUTPUT_SLOT); // Use your output slot index
        if (outputStack.isEmpty()) return true;
        if (!ItemStack.areItemsEqual(outputStack, nextOutput)) return false; // Pauses machine if items don't match

        return outputStack.getCount() + nextOutput.getCount() <= outputStack.getMaxCount();
    }

    private void giveIngredient(World world) {
        if (nextOutput.isEmpty()) return; // Failsafe

        ItemStack inputStack = this.getStack(INPUT_SLOT);
        ItemStack outputStack = this.getStack(OUTPUT_SLOT);

        // 1. Consume 1 item from the input
        inputStack.decrement(1);

        // 2. Give the pre-rolled item to the output slot
        if (outputStack.isEmpty()) {
            this.setStack(OUTPUT_SLOT, nextOutput.copy());
        } else {
            outputStack.increment(nextOutput.getCount());
        }

        // 3. CLEAR the pre-rolled item so hasRecipe() rolls a new random one next tick!
        nextOutput = ItemStack.EMPTY;
    }

    private List<RecipeEntry<?>> findAllValidRecipes(World world, ItemStack inputStack) {
        RecipeManager recipeManager = world.getRecipeManager();

        return recipeManager.values().stream()
                .filter(entry -> {
                    ItemStack result = entry.value().getResult(world.getRegistryManager());
                    if (!result.isOf(inputStack.getItem())) return false;

                    long ingredientCount = entry.value().getIngredients().stream()
                            .filter(ingredient -> !ingredient.isEmpty())
                            .count();

                    return result.getCount() <= ingredientCount;
                })
                .toList(); // Return ALL matching, non-exploit recipes!
    }

    private ItemStack generateRandomOutput(World world) {
        if (cachedValidRecipes == null || cachedValidRecipes.isEmpty()) return ItemStack.EMPTY;

        // 1. Pick a random RECIPE from our cached list for this specific item
        RecipeEntry<?> selectedRecipe = cachedValidRecipes.get(world.random.nextInt(cachedValidRecipes.size()));

        // 2. Get the valid ingredients from the chosen recipe
        List<Ingredient> validIngredients = selectedRecipe.value().getIngredients().stream()
                .filter(ingredient -> !ingredient.isEmpty())
                .toList();

        if (validIngredients.isEmpty()) return ItemStack.EMPTY;

        // 3. Pick a random INGREDIENT from the recipe
        Ingredient randomIngredient = validIngredients.get(world.random.nextInt(validIngredients.size()));

        // 4. Pick a random physical item from that ingredient (resolves tags)
        ItemStack[] matchingStacks = randomIngredient.getMatchingStacks();
        if (matchingStacks.length > 0) {
            ItemStack selected = matchingStacks[world.random.nextInt(matchingStacks.length)].copy();
            selected.setCount(1);
            return selected;
        }

        return ItemStack.EMPTY;
    }
}
