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
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.PLACEHOLDER);

        blockStateModelGenerator.registerSingleton(ModBlocks.VOID_ANCHOR, TexturedModel.CUBE_COLUMN);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModItems.ANCIENT_SCRIP, Models.GENERATED);

        itemModelGenerator.register(ModItems.END_STEEL_INGOT, Models.GENERATED);
        itemModelGenerator.register(ModItems.VOID_TREATED_LEATHER, Models.GENERATED);

        itemModelGenerator.register(ModItems.RELIC_BlADE, Models.GENERATED);
        itemModelGenerator.register(ModItems.STATIC_PEARL, Models.GENERATED);

        itemModelGenerator.registerArmor(((ArmorItem) ModItems.ENDER_BOOTS));
        itemModelGenerator.registerArmor(((ArmorItem) ModItems.OBSIDIAN_GOGGLES));

        itemModelGenerator.register(ModItems.SOUL_SIPHON, Models.HANDHELD);
        itemModelGenerator.register(ModItems.SOUL_FRAGMENT, Models.GENERATED);
    }
}
