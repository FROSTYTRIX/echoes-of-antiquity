package net.frostytrix.echoesofantiquity.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.List;

public record SieveRecipe(Ingredient inputItem, List<SieveResult> results) implements Recipe<SieveRecipeInput> {

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        DefaultedList<Ingredient> list = DefaultedList.of();
        list.add(this.inputItem);
        return list;
    }

    @Override
    public boolean matches(SieveRecipeInput input, World world) {
        if (world.isClient) { return false; }
        return inputItem.test(input.getStackInSlot(0));
    }

    // This is called by the standard crafting system, but since we have a custom
    // probability system handled in the BlockEntity, we just return an empty stack here.
    @Override
    public ItemStack craft(SieveRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResult(RegistryWrapper.WrapperLookup registriesLookup) {
        return ItemStack.EMPTY;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.SIEVE_SERIALIZER; // Linked to your registry
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.SIEVE_TYPE; // Linked to your registry
    }

    public static class Serializer implements RecipeSerializer<SieveRecipe> {
        public static final MapCodec<SieveRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Ingredient.DISALLOW_EMPTY_CODEC.fieldOf("ingredient").forGetter(SieveRecipe::inputItem),
                SieveResult.CODEC.listOf().fieldOf("results").forGetter(SieveRecipe::results)
        ).apply(inst, SieveRecipe::new));

        public static final PacketCodec<RegistryByteBuf, SieveRecipe> STREAM_CODEC =
                PacketCodec.tuple(
                        Ingredient.PACKET_CODEC, SieveRecipe::inputItem,
                        SieveResult.STREAM_CODEC.collect(PacketCodecs.toList()), SieveRecipe::results,
                        SieveRecipe::new);

        @Override
        public MapCodec<SieveRecipe> codec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, SieveRecipe> packetCodec() {
            return STREAM_CODEC;
        }
    }
}