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
import net.minecraft.registry.tag.ItemTags;
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

        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ModItems.OBSIDIAN_GOGGLES)
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

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.DRAGON_BOW)
                .input('S', Items.STICK)
                .input('s', Items.STRING)
                .input('O', Blocks.OBSIDIAN)
                .input('L', ModItems.VOID_TREATED_LEATHER)
                .input('I', ModItems.END_STEEL_INGOT)
                .pattern("LSs")
                .pattern("O I")
                .pattern("LSs")
                .criterion(hasItem(ModItems.END_STEEL_INGOT), conditionsFromItem(ModItems.END_STEEL_INGOT))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.DRAGON_BOW)
                .input('S', Items.STICK)
                .input('s', Items.STRING)
                .input('O', Blocks.OBSIDIAN)
                .input('L', ModItems.VOID_TREATED_LEATHER)
                .input('I', ModItems.END_STEEL_INGOT)
                .pattern("sSL")
                .pattern("I O")
                .pattern("sSL")
                .criterion(hasItem(ModItems.END_STEEL_INGOT), conditionsFromItem(ModItems.END_STEEL_INGOT))
                .offerTo(recipeExporter, Identifier.of(EchoesOfAntiquity.MOD_ID, "dragon_bow_flipped"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModBlocks.VOID_PEDESTAL)
                .input('O', Blocks.OBSIDIAN)
                .input('S', ModItems.END_STEEL_INGOT)
                .input('D', Items.DIAMOND)
                .pattern("SOS")
                .pattern("ODO")
                .pattern("SOS")
                .criterion(hasItem(ModItems.END_STEEL_INGOT), conditionsFromItem(ModItems.END_STEEL_INGOT))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.RELIC_BlADE)
                .input('S', ModItems.END_STEEL_INGOT)
                .pattern(" S ")
                .pattern(" S ")
                .pattern(" S ")
                .criterion(hasItem(ModItems.END_STEEL_INGOT), conditionsFromItem(ModItems.END_STEEL_INGOT))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.RELIC_GREATSWORD)
                .input('B', ModItems.RELIC_BlADE)
                .input('S', Items.STICK)
                .input('L', ModItems.VOID_TREATED_LEATHER)
                .pattern(" B ")
                .pattern(" S ")
                .pattern(" L " )
                .criterion(hasItem(ModItems.END_STEEL_INGOT), conditionsFromItem(ModItems.END_STEEL_INGOT))
                .offerTo(recipeExporter);


        SmithingTransformRecipeJsonBuilder.create(Ingredient.ofItems(ModItems.END_STEEL_UPGRADE), Ingredient.ofItems(Items.CHAINMAIL_HELMET), Ingredient.ofItems(ModItems.END_STEEL_INGOT), RecipeCategory.COMBAT, ModItems.VOID_CHAINMAIL_HELMET)
                .criterion(hasItem(ModItems.END_STEEL_INGOT), conditionsFromItem(ModItems.END_STEEL_INGOT))
                .offerTo(recipeExporter, Identifier.of(EchoesOfAntiquity.MOD_ID, "void_chainmail_helmet_smithing"));

        SmithingTransformRecipeJsonBuilder.create(Ingredient.ofItems(ModItems.END_STEEL_UPGRADE), Ingredient.ofItems(Items.CHAINMAIL_CHESTPLATE), Ingredient.ofItems(ModItems.END_STEEL_INGOT), RecipeCategory.COMBAT, ModItems.VOID_CHAINMAIL_CHESTPLATE)
                .criterion(hasItem(ModItems.END_STEEL_INGOT), conditionsFromItem(ModItems.END_STEEL_INGOT))
                .offerTo(recipeExporter, Identifier.of(EchoesOfAntiquity.MOD_ID, "void_chainmail_chestplate_smithing"));

        SmithingTransformRecipeJsonBuilder.create(Ingredient.ofItems(ModItems.END_STEEL_UPGRADE), Ingredient.ofItems(Items.CHAINMAIL_LEGGINGS), Ingredient.ofItems(ModItems.END_STEEL_INGOT), RecipeCategory.COMBAT, ModItems.VOID_CHAINMAIL_LEGGINGS)
                .criterion(hasItem(ModItems.END_STEEL_INGOT), conditionsFromItem(ModItems.END_STEEL_INGOT))
                .offerTo(recipeExporter, Identifier.of(EchoesOfAntiquity.MOD_ID, "void_chainmail_leggings_smithing"));

        SmithingTransformRecipeJsonBuilder.create(Ingredient.ofItems(ModItems.END_STEEL_UPGRADE), Ingredient.ofItems(Items.CHAINMAIL_BOOTS), Ingredient.ofItems(ModItems.END_STEEL_INGOT), RecipeCategory.COMBAT, ModItems.VOID_CHAINMAIL_BOOTS)
                .criterion(hasItem(ModItems.END_STEEL_INGOT), conditionsFromItem(ModItems.END_STEEL_INGOT))
                .offerTo(recipeExporter, Identifier.of(EchoesOfAntiquity.MOD_ID, "void_chainmail_boots_smithing"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.END_STEEL_UPGRADE)
                .input('E', Blocks.END_STONE)
                .input('D', Items.DIAMOND)
                .input('U', ModItems.END_STEEL_UPGRADE)
                .pattern("DUD")
                .pattern("DED")
                .pattern("DDD" )
                .criterion(hasItem(ModItems.END_STEEL_INGOT), conditionsFromItem(ModItems.END_STEEL_INGOT))
                .offerTo(recipeExporter);

        // Failed Clones

        // Architect's Tools

        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ModItems.MEASURING_TAPE)
                .input('I', Items.IRON_INGOT)
                .input('S', Items.STRING)
                .input('C', Items.COPPER_INGOT)
                .input('Y', Items.YELLOW_DYE)
                .pattern("ICI")
                .pattern("SYS")
                .pattern("ICI" )
                .criterion(hasItem(Items.COPPER_INGOT), conditionsFromItem(Items.COPPER_INGOT))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ModItems.INFINITE_WATER_BUCKET)
                .input('I', ModItems.END_STEEL_INGOT)
                .input('H', Items.HEART_OF_THE_SEA)
                .pattern("IHI")
                .pattern(" I ")
                .criterion(hasItem(ModItems.END_STEEL_INGOT), conditionsFromItem(ModItems.END_STEEL_INGOT))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModBlocks.GRAVITY_ANCHOR)
                .input('I', ModItems.END_STEEL_INGOT)
                .input('L', Blocks.LODESTONE)
                .input('O', Blocks.OBSIDIAN)
                .input('E', Items.ECHO_SHARD)
                .pattern("IEI")
                .pattern("ILI")
                .pattern("IOI")
                .criterion(hasItem(ModItems.END_STEEL_INGOT), conditionsFromItem(ModItems.END_STEEL_INGOT))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModBlocks.WAYSTONE)
                .input('S', ModTags.Items.ALL_STONES)
                .input('L', Blocks.LAPIS_BLOCK)
                .input('E', Items.ENDER_PEARL)
                .pattern("SES")
                .pattern("SLS")
                .pattern("SES")
                .criterion(hasItem(Items.LAPIS_LAZULI), conditionsFromItem(Items.LAPIS_LAZULI))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ModBlocks.UNCRAFTER)
                .input('I', ModItems.END_STEEL_INGOT)
                .input('P', ItemTags.PLANKS)
                .input('R', Items.RECOVERY_COMPASS)
                .pattern(" II")
                .pattern(" RP")
                .pattern(" PP")
                .criterion(hasItem(ModItems.END_STEEL_INGOT), conditionsFromItem(ModItems.END_STEEL_INGOT))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ModBlocks.UNCRAFTER)
                .input('I', ModItems.END_STEEL_INGOT)
                .input('P', ItemTags.PLANKS)
                .input('R', Items.RECOVERY_COMPASS)
                .pattern("II ")
                .pattern("PR ")
                .pattern("PP ")
                .criterion(hasItem(ModItems.END_STEEL_INGOT), conditionsFromItem(ModItems.END_STEEL_INGOT))
                .offerTo(recipeExporter, Identifier.of(EchoesOfAntiquity.MOD_ID, "uncrafter_recovery_compass_on_right"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModBlocks.SIEVE)
                .input('S', Items.STRING)
                .input('P', ItemTags.PLANKS)
                .input('L', ItemTags.LOGS)
                .input('C', Blocks.CHEST)
                .pattern("SSS")
                .pattern("LPL")
                .pattern("PCP")
                .criterion(hasItem(Items.STRING), conditionsFromItem(Items.STRING))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ModItems.LEVEL)
                .input('I', ModItems.END_STEEL_INGOT)
                .input('G', Items.GOLD_INGOT)
                .input('C', Items.COMPASS)
                .pattern(" C ")
                .pattern("GIG")
                .criterion(hasItem(ModItems.END_STEEL_INGOT), conditionsFromItem(ModItems.END_STEEL_INGOT))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ModItems.MAGNET_RING)
                .input('E', ModItems.END_STEEL_INGOT)
                .input('C', Items.COMPASS)
                .pattern(" C ")
                .pattern("E E")
                .pattern(" E ")
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
