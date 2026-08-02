package net.frostytrix.echoesofantiquity.compat.emi;

import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import net.frostytrix.echoesofantiquity.EchoesOfAntiquity;
import net.frostytrix.echoesofantiquity.block.ModBlocks;
import net.frostytrix.echoesofantiquity.recipe.ModRecipes;
import net.frostytrix.echoesofantiquity.recipe.sieve.SieveRecipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.util.Identifier;

public class EchoesEmiPlugin implements EmiPlugin {
    public static final EmiRecipeCategory SIEVE_CATEGORY = new EmiRecipeCategory(
            Identifier.of(EchoesOfAntiquity.MOD_ID, "sieve"),
            EmiStack.of(ModBlocks.SIEVE));

    @Override
    public void register(EmiRegistry registry) {
        registry.addCategory(SIEVE_CATEGORY);
        registry.addWorkstation(SIEVE_CATEGORY, EmiStack.of(ModBlocks.SIEVE));

        for (RecipeEntry<SieveRecipe> entry : registry.getRecipeManager().listAllOfType(ModRecipes.SIEVE_TYPE)) {
            registry.addRecipe(new SieveEmiRecipe(entry));
        }
    }
}
