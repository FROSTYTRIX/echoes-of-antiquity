package net.frostytrix.echoesofantiquity.recipe;

import net.frostytrix.echoesofantiquity.EchoesOfAntiquity;
import net.frostytrix.echoesofantiquity.recipe.sieve.SieveRecipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModRecipes {
    public static final RecipeSerializer<SieveRecipe> SIEVE_SERIALIZER = Registry.register(
            Registries.RECIPE_SERIALIZER, Identifier.of(EchoesOfAntiquity.MOD_ID, "sieve"),
                new SieveRecipe.Serializer());
    public static final RecipeType<SieveRecipe> SIEVE_TYPE = Registry.register(
            Registries.RECIPE_TYPE, Identifier.of(EchoesOfAntiquity.MOD_ID, "sieve"),
            new RecipeType<SieveRecipe>() {
                @Override
                public String toString() {
                    return "sieve";
                }
            });

    public static void registerRecipes() {
        EchoesOfAntiquity.LOGGER.info("Registering recipes");
    }
}
