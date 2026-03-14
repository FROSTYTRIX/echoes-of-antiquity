package net.frostytrix.echoesofantiquity.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.frostytrix.echoesofantiquity.block.ModBlocks;
import net.frostytrix.echoesofantiquity.util.ModTags;
import net.minecraft.block.Blocks;
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
                .add(ModBlocks.VOID_ANCHOR)
                .add(ModBlocks.GRAVITY_ANCHOR)
                .add(ModBlocks.WAYSTONE)
                ;

        getOrCreateTagBuilder(BlockTags.AXE_MINEABLE)
                .add(ModBlocks.UNCRAFTER)
                ;

        getOrCreateTagBuilder(BlockTags.NEEDS_IRON_TOOL)
                .add(ModBlocks.GRAVITY_ANCHOR)
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

        getOrCreateTagBuilder(ModTags.Blocks.NATURAL_BLOCKS_LEVEL)
                .add(Blocks.GRASS_BLOCK)
                .add(Blocks.DIRT)
                .add(Blocks.STONE)
                .add(Blocks.GRANITE)
                .add(Blocks.DIORITE)
                .add(Blocks.ANDESITE)
                .add(Blocks.TUFF)
                .add(Blocks.DEEPSLATE)
                .add(Blocks.ACACIA_LEAVES)
                .add(Blocks.BIRCH_LEAVES)
                .add(Blocks.CHERRY_LEAVES)
                .add(Blocks.JUNGLE_LEAVES)
                .add(Blocks.OAK_LEAVES)
                .add(Blocks.DARK_OAK_LEAVES)
                .add(Blocks.AZALEA_LEAVES)
                .add(Blocks.FLOWERING_AZALEA_LEAVES)
                .add(Blocks.MANGROVE_LEAVES)
                .add(Blocks.SPRUCE_LEAVES)
                .add(Blocks.VINE)
                .add(Blocks.CAVE_VINES)
                .add(Blocks.SAND)
                .add(Blocks.GRAVEL)
                .add(Blocks.SNOW)
                .add(Blocks.SHORT_GRASS)
                .add(Blocks.TALL_GRASS)
            ;
    }
}
