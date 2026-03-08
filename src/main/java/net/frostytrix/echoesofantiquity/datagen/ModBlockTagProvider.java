package net.frostytrix.echoesofantiquity.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.frostytrix.echoesofantiquity.block.ModBlocks;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends FabricTagProvider.BlockTagProvider
{
    public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
                .add(ModBlocks.VOID_PEDESTAL)
                ;

        getOrCreateTagBuilder(BlockTags.AXE_MINEABLE)
                .add(ModBlocks.UNCRAFTER)
                ;

        getOrCreateTagBuilder(BlockTags.NEEDS_IRON_TOOL)
                ;

        getOrCreateTagBuilder(BlockTags.NEEDS_DIAMOND_TOOL)
                .add(ModBlocks.VOID_PEDESTAL)
                ;

        getOrCreateTagBuilder(BlockTags.FENCES)
                ;
        getOrCreateTagBuilder(BlockTags.FENCE_GATES)
                ;
        getOrCreateTagBuilder(BlockTags.WALLS)
                ;
    }
}
