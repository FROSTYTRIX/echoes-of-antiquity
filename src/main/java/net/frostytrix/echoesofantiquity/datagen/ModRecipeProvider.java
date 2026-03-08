package net.frostytrix.echoesofantiquity.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.frostytrix.echoesofantiquity.EchoesOfAntiquity;
import net.frostytrix.echoesofantiquity.block.ModBlocks;
import net.frostytrix.echoesofantiquity.item.ModItems;
import net.frostytrix.echoesofantiquity.util.ModTags;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
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

        // Fallen Humans

        SmithingTransformRecipeJsonBuilder.create(Ingredient.EMPTY, Ingredient.ofItems(Items.LEATHER_BOOTS), Ingredient.ofItems(ModItems.VOID_TREATED_LEATHER), RecipeCategory.COMBAT, ModItems.ENDER_BOOTS)
                .criterion(hasItem(ModItems.VOID_TREATED_LEATHER), conditionsFromItem(ModItems.VOID_TREATED_LEATHER))
                .offerTo(recipeExporter, Identifier.of(EchoesOfAntiquity.MOD_ID, "ender_boots_smithing"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.OBSIDIAN_GOGGLES)
                .input('L', ModItems.VOID_TREATED_LEATHER)
                .input('O', Blocks.OBSIDIAN)
                .input('G', Ingredient.fromTag(ModTags.Items.GLASS_PANES))
                .pattern("O O")
                .pattern("GLG")
                .pattern("O O")
                .criterion(hasItem(Items.CHORUS_FRUIT), conditionsFromItem(Items.CHORUS_FRUIT))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.VOID_TREATED_LEATHER)
                .input('L', Items.LEATHER)
                .input('C', Items.CHORUS_FRUIT)
                .pattern(" C ")
                .pattern("CLC")
                .pattern(" C ")
                .criterion(hasItem(Items.CHORUS_FRUIT), conditionsFromItem(Items.CHORUS_FRUIT))
                .offerTo(recipeExporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.END_STEEL_INGOT)
                .input(Ingredient.ofItems(Items.IRON_INGOT))
                .input(Ingredient.ofItems(Items.POPPED_CHORUS_FRUIT))
                .input(Ingredient.ofItems(Items.ENDER_PEARL))
                .criterion(hasItem(Items.POPPED_CHORUS_FRUIT), conditionsFromItem(Items.POPPED_CHORUS_FRUIT))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModBlocks.VOID_PEDESTAL)
                .input('O', Blocks.OBSIDIAN)
                .input('S', ModItems.END_STEEL_INGOT)
                .input('D', Items.DIAMOND)
                .pattern("SOS")
                .pattern("ODO")
                .pattern("SOS")
                .criterion(hasItem(ModItems.END_STEEL_INGOT), conditionsFromItem(ModItems.END_STEEL_INGOT))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.RELIC_BlADE)
                .input('S', ModItems.END_STEEL_INGOT)
                .pattern(" S ")
                .pattern(" S ")
                .pattern(" S ")
                .criterion(hasItem(ModItems.END_STEEL_INGOT), conditionsFromItem(ModItems.END_STEEL_INGOT))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.RELIC_GREATSWORD)
                .input('B', ModItems.RELIC_BlADE)
                .input('S', Items.STICK)
                .input('L', ModItems.VOID_TREATED_LEATHER)
                .pattern(" B ")
                .pattern(" S ")
                .pattern(" L " )
                .criterion(hasItem(ModItems.END_STEEL_INGOT), conditionsFromItem(ModItems.END_STEEL_INGOT))
                .offerTo(recipeExporter);

        // Failed Clones

        // Architect's Tools

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.MEASURING_TAPE)
                .input('I', Items.IRON_INGOT)
                .input('S', Items.STRING)
                .input('C', Items.COPPER_INGOT)
                .input('Y', Items.YELLOW_DYE)
                .pattern("ICI")
                .pattern("SYS")
                .pattern("ICI" )
                .criterion(hasItem(Items.COPPER_INGOT), conditionsFromItem(Items.COPPER_INGOT))
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
