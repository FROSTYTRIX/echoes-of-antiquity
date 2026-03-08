package net.frostytrix.echoesofantiquity.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.frostytrix.echoesofantiquity.block.ModBlocks;
import net.frostytrix.echoesofantiquity.item.ModItems;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
import net.minecraft.data.client.TexturedModel;
import net.minecraft.item.ArmorItem;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.registerSingleton(ModBlocks.PLACEHOLDER, TexturedModel.CUBE_COLUMN); // Comme ca je me rappelle les blocks column
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {

        // Fallen Human

        itemModelGenerator.register(ModItems.ANCIENT_SCRIP, Models.GENERATED);

        itemModelGenerator.register(ModItems.END_STEEL_INGOT, Models.GENERATED);
        itemModelGenerator.register(ModItems.VOID_TREATED_LEATHER, Models.GENERATED);

        itemModelGenerator.register(ModItems.RELIC_BlADE, Models.GENERATED);
        itemModelGenerator.register(ModItems.STATIC_PEARL, Models.GENERATED);

        itemModelGenerator.registerArmor(((ArmorItem) ModItems.ENDER_BOOTS));
        itemModelGenerator.registerArmor(((ArmorItem) ModItems.OBSIDIAN_GOGGLES));

        // Failed clones

        itemModelGenerator.register(ModItems.SOUL_SIPHON, Models.HANDHELD);
        itemModelGenerator.register(ModItems.SOUL_FRAGMENT, Models.GENERATED);

        // Architect's Tools

        itemModelGenerator.register(ModItems.CLIMBING_SPIDER_LEG, Models.HANDHELD_MACE);
        itemModelGenerator.register(ModItems.MEASURING_TAPE, Models.GENERATED);
    }
}
