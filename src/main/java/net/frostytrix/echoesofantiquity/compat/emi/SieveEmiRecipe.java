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

    private static final int COLUMNS = 4;
    private static final int SLOT = 18;
    private static final int OUTPUT_X = 62;

    @Override
    public int getDisplayWidth() {
        return OUTPUT_X + COLUMNS * SLOT;
    }

    @Override
    public int getDisplayHeight() {
        // The input column is two slots tall, so never go below that.
        return Math.max(2 * SLOT, rows() * SLOT);
    }

    private int rows() {
        return Math.max(1, (outputs.size() + COLUMNS - 1) / COLUMNS);
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addSlot(input, 0, 0);
        widgets.addSlot(EmiStack.of(ModItems.SOUL_FRAGMENT), 0, SLOT)
                .appendTooltip(Text.translatable("emi.echoesofantiquity.sieve.fuel"));

        widgets.addTexture(EmiTexture.EMPTY_ARROW, 28, 2);

        for (int i = 0; i < outputs.size(); i++) {
            int x = OUTPUT_X + (i % COLUMNS) * SLOT;
            int y = (i / COLUMNS) * SLOT;
            widgets.addSlot(outputs.get(i), x, y)
                    .recipeContext(this)
                    .appendTooltip(Text.translatable("emi.echoesofantiquity.sieve.chance",
                            String.format("%.1f", chances.get(i) * 100)));
        }
    }
}
