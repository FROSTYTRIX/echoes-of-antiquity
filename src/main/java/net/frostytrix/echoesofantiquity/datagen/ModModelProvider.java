package net.frostytrix.echoesofantiquity.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.frostytrix.echoesofantiquity.block.ModBlocks;
import net.frostytrix.echoesofantiquity.item.ModItems;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
import net.minecraft.item.ArmorItem;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.PLACEHOLDER);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModItems.ANCIENT_SCRIP, Models.GENERATED);
        itemModelGenerator.register(ModItems.END_STEEL_INGOT, Models.GENERATED);
        itemModelGenerator.register(ModItems.VOID_TREATED_LEATHER, Models.GENERATED);
        itemModelGenerator.register(ModItems.RELIC_BlADE, Models.GENERATED);
        itemModelGenerator.registerArmor(((ArmorItem) ModItems.ENDER_BOOTS));
    }
}
