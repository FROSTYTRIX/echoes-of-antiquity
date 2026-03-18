package net.frostytrix.echoesofantiquity;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.frostytrix.echoesofantiquity.block.ModBlocks;
import net.frostytrix.echoesofantiquity.block.entity.ModBlockEntities;
import net.frostytrix.echoesofantiquity.component.ModDataComponentTypes;
import net.frostytrix.echoesofantiquity.effect.ModEffects;
import net.frostytrix.echoesofantiquity.item.ModItemGroups;
import net.frostytrix.echoesofantiquity.item.ModItems;
import net.frostytrix.echoesofantiquity.potion.ModPotions;
import net.frostytrix.echoesofantiquity.recipe.ModRecipes;
import net.frostytrix.echoesofantiquity.screen.ModScreenHandlers;
import net.frostytrix.echoesofantiquity.sound.ModSounds;
import net.frostytrix.echoesofantiquity.util.ModLootTableModifiers;
import net.frostytrix.echoesofantiquity.world.gen.ModWorldGeneration;
import net.frostytrix.echoesofantiquity.world.village.VillageAdditions;
import net.minecraft.item.Items;
import net.minecraft.potion.Potions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EchoesOfAntiquity implements ModInitializer {
	public static final String MOD_ID = "echoesofantiquity";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.registerModItems();
		ModItemGroups.registerItemGroups();
		ModBlocks.registerModBlocks();
		ModEffects.registerEffects();
		ModPotions.registerPotions();
		ModBlockEntities.registerBlockEntities();
		ModDataComponentTypes.registerDataComponents();
		ModLootTableModifiers.modifyLootTables();
		ModScreenHandlers.registerScreenHandlers();
		ModSounds.registerSounds();
		VillageAdditions.registerNewVillageStructures();
		ModRecipes.registerRecipes();
		ModWorldGeneration.generateModWorldGen();

		FabricBrewingRecipeRegistryBuilder.BUILD.register(builder -> {
			builder.registerPotionRecipe(Potions.AWKWARD, Items.POPPED_CHORUS_FRUIT, ModPotions.PHASING_POTION);
		});

		FabricBrewingRecipeRegistryBuilder.BUILD.register(builder -> {
			builder.registerPotionRecipe(ModPotions.PHASING_POTION, Items.POPPED_CHORUS_FRUIT, ModPotions.REACH_POTION);
		});
	}
}