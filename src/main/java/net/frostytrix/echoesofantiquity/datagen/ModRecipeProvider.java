package net.frostytrix.echoesofantiquity.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.frostytrix.echoesofantiquity.EchoesOfAntiquity;
import net.frostytrix.echoesofantiquity.item.ModItems;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.SmithingTransformRecipeJsonBuilder;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter recipeExporter) {

        SmithingTransformRecipeJsonBuilder.create(Ingredient.EMPTY, Ingredient.ofItems(Items.LEATHER_BOOTS), Ingredient.ofItems(ModItems.VOID_TREATED_LEATHER), RecipeCategory.COMBAT, ModItems.ENDER_BOOTS)
                .criterion(hasItem(ModItems.VOID_TREATED_LEATHER), conditionsFromItem(ModItems.VOID_TREATED_LEATHER))
                .offerTo(recipeExporter, Identifier.of(EchoesOfAntiquity.MOD_ID, "ender_boots_smithing"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.VOID_TREATED_LEATHER)
                .input('L', Items.LEATHER)
                .input('C', Items.CHORUS_FRUIT)
                .pattern(" C ")
                .pattern("CLC")
                .pattern(" C ")
                .criterion(hasItem(Items.CHORUS_FRUIT), conditionsFromItem(Items.CHORUS_FRUIT))
                .offerTo(recipeExporter);
    }

    public void offerSwordRecipe(RecipeExporter recipeExporter, RecipeCategory category, ItemConvertible output, ItemConvertible stick, ItemConvertible material){
        ShapedRecipeJsonBuilder.create(category, output).input('M', Ingredient.ofItems(material)).input('S', Ingredient.ofItems(stick)).pattern(" M ").pattern(" M ").pattern(" S ")
                .criterion(hasItem(material), conditionsFromItem(material)).offerTo(recipeExporter);
    }

    public void offerPickaxeRecipe(RecipeExporter recipeExporter, RecipeCategory category, ItemConvertible output, ItemConvertible stick, ItemConvertible material){
        ShapedRecipeJsonBuilder.create(category, output).input('M', Ingredient.ofItems(material)).input('S', Ingredient.ofItems(stick)).pattern("MMM").pattern(" S ").pattern(" S ")
                .criterion(hasItem(material), conditionsFromItem(material)).offerTo(recipeExporter);
    }

    public void offerAxeRecipe(RecipeExporter recipeExporter, RecipeCategory category, ItemConvertible output, ItemConvertible stick, ItemConvertible material){
        ShapedRecipeJsonBuilder.create(category, output).input('M', Ingredient.ofItems(material)).input('S', Ingredient.ofItems(stick)).pattern(" MM").pattern(" SM").pattern(" S ")
                .criterion(hasItem(material), conditionsFromItem(material)).offerTo(recipeExporter);
        ShapedRecipeJsonBuilder.create(category, output).input('M', Ingredient.ofItems(material)).input('S', Ingredient.ofItems(stick)).pattern("MM ").pattern("MS ").pattern(" S ")
                .criterion(hasItem(material), conditionsFromItem(material)).offerTo(recipeExporter, Identifier.of(EchoesOfAntiquity.MOD_ID, "recipe_axe_left"));
    }

    public void offerShovelRecipe(RecipeExporter recipeExporter, RecipeCategory category, ItemConvertible output, ItemConvertible stick, ItemConvertible material){
        ShapedRecipeJsonBuilder.create(category, output).input('M', Ingredient.ofItems(material)).input('S', Ingredient.ofItems(stick)).pattern(" M ").pattern(" S ").pattern(" S ")
                .criterion(hasItem(material), conditionsFromItem(material)).offerTo(recipeExporter);
    }

    public void offerHoeRecipe(RecipeExporter recipeExporter, RecipeCategory category, ItemConvertible output, ItemConvertible stick, ItemConvertible material){
        ShapedRecipeJsonBuilder.create(category, output).input('M', Ingredient.ofItems(material)).input('S', Ingredient.ofItems(stick)).pattern(" MM").pattern(" S ").pattern(" S ")
                .criterion(hasItem(material), conditionsFromItem(material)).offerTo(recipeExporter);
        ShapedRecipeJsonBuilder.create(category, output).input('M', Ingredient.ofItems(material)).input('S', Ingredient.ofItems(stick)).pattern("MM ").pattern(" S ").pattern(" S ")
                .criterion(hasItem(material), conditionsFromItem(material)).offerTo(recipeExporter, Identifier.of(EchoesOfAntiquity.MOD_ID, "recipe_hoe_left"));
    }
}
