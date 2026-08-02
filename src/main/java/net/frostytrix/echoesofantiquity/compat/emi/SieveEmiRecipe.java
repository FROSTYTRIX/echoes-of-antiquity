package net.frostytrix.echoesofantiquity.compat.emi;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.frostytrix.echoesofantiquity.item.ModItems;
import net.frostytrix.echoesofantiquity.recipe.sieve.SievePool;
import net.frostytrix.echoesofantiquity.recipe.sieve.SievePoolEntry;
import net.frostytrix.echoesofantiquity.recipe.sieve.SieveRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class SieveEmiRecipe implements EmiRecipe {
    private final Identifier id;
    private final EmiIngredient input;
    private final List<EmiStack> outputs = new ArrayList<>();
    private final List<Float> chances = new ArrayList<>();

    public SieveEmiRecipe(RecipeEntry<SieveRecipe> entry) {
        this.id = entry.id();
        SieveRecipe recipe = entry.value();
        this.input = EmiIngredient.of(recipe.inputItem());

        recipe.results().forEach(result -> {
            outputs.add(EmiStack.of(result.stack()));
            chances.add(result.chance());
        });

        // A pool draws `rolls` times, so each entry's odds are its weight share times the pool chance,
        // counted once per roll.
        for (SievePool pool : recipe.pools()) {
            int total = pool.totalWeight();
            if (total <= 0) {
                continue;
            }
            for (SievePoolEntry poolEntry : pool.entries()) {
                if (poolEntry.effectiveWeight() <= 0) {
                    continue;
                }
                ItemStack stack = poolEntry.stack();
                float perRoll = pool.chance() * poolEntry.effectiveWeight() / total;
                outputs.add(EmiStack.of(stack));
                chances.add(Math.min(1.0F, perRoll * pool.rolls()));
            }
        }
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return EchoesEmiPlugin.SIEVE_CATEGORY;
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return List.of(input, EmiStack.of(ModItems.SOUL_FRAGMENT));
    }

    @Override
    public List<EmiStack> getOutputs() {
        return outputs;
    }

    @Override
    public int getDisplayWidth() {
        return 144;
    }

    @Override
    public int getDisplayHeight() {
        return 18 + 20 * rows();
    }

    private int rows() {
        return Math.max(1, (outputs.size() + 5) / 6);
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addSlot(input, 0, 0);
        widgets.addSlot(EmiStack.of(ModItems.SOUL_FRAGMENT), 0, 20)
                .appendTooltip(Text.translatable("emi.echoesofantiquity.sieve.fuel"));

        widgets.addTexture(EmiTexture.EMPTY_ARROW, 28, 2);

        for (int i = 0; i < outputs.size(); i++) {
            int x = 62 + (i % 6) * 18;
            int y = (i / 6) * 20;
            widgets.addSlot(outputs.get(i), x, y)
                    .recipeContext(this)
                    .appendTooltip(Text.translatable("emi.echoesofantiquity.sieve.chance",
                            String.format("%.1f", chances.get(i) * 100)));
        }
    }
}
