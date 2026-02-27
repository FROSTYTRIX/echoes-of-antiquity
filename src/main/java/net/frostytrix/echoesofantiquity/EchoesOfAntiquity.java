package net.frostytrix.echoesofantiquity;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.frostytrix.echoesofantiquity.block.ModBlocks;
import net.frostytrix.echoesofantiquity.block.entity.ModBlockEntities;
import net.frostytrix.echoesofantiquity.effect.ModEffects;
import net.frostytrix.echoesofantiquity.item.ModItemGroups;
import net.frostytrix.echoesofantiquity.item.ModItems;
import net.frostytrix.echoesofantiquity.potion.ModPotions;
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

		FabricBrewingRecipeRegistryBuilder.BUILD.register(builder -> {
			builder.registerPotionRecipe(Potions.AWKWARD, Items.POPPED_CHORUS_FRUIT, ModPotions.PHASING_POTION);
		});

		FabricBrewingRecipeRegistryBuilder.BUILD.register(builder -> {
			builder.registerPotionRecipe(Potions.WEAKNESS, Items.POPPED_CHORUS_FRUIT, ModPotions.REACH_POTION);
		});
	}
}