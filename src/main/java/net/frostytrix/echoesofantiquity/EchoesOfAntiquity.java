package net.frostytrix.echoesofantiquity;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.frostytrix.echoesofantiquity.block.ModBlocks;
import net.frostytrix.echoesofantiquity.block.entity.ModBlockEntities;
import net.frostytrix.echoesofantiquity.component.ModDataComponentTypes;
import net.frostytrix.echoesofantiquity.effect.ModEffects;
import net.frostytrix.echoesofantiquity.entity.ModEntities;
import net.frostytrix.echoesofantiquity.entity.custom.ChorusHuskEntity;
import net.frostytrix.echoesofantiquity.item.ModItemGroups;
import net.frostytrix.echoesofantiquity.item.ModItems;
import net.frostytrix.echoesofantiquity.potion.ModPotions;
import net.frostytrix.echoesofantiquity.recipe.ModRecipes;
import net.frostytrix.echoesofantiquity.screen.ModScreenHandlers;
import net.frostytrix.echoesofantiquity.sound.ModSounds;
import net.frostytrix.echoesofantiquity.util.ModLootTableModifiers;
import net.frostytrix.echoesofantiquity.world.gen.ModWorldGeneration;
import net.frostytrix.echoesofantiquity.world.village.VillageAdditions;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.potion.Potions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.frostytrix.echoesofantiquity.item.ModItems.CHORUS_HUSK_SPAWN_EGG;

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
		ModEntities.registerModEntities();

		FabricBrewingRecipeRegistryBuilder.BUILD.register(builder -> {
			builder.registerPotionRecipe(Potions.AWKWARD, Items.POPPED_CHORUS_FRUIT, ModPotions.PHASING_POTION);
		});

		FabricBrewingRecipeRegistryBuilder.BUILD.register(builder -> {
			builder.registerPotionRecipe(ModPotions.PHASING_POTION, Items.POPPED_CHORUS_FRUIT, ModPotions.REACH_POTION);
		});

		FabricDefaultAttributeRegistry.register(ModEntities.CHORUS_HUSK, ChorusHuskEntity.createChorusHuskAttributes());
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register(entries -> {
			entries.add(CHORUS_HUSK_SPAWN_EGG);	});
	}
}