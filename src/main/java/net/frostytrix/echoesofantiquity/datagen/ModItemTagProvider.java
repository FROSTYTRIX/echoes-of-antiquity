package net.frostytrix.echoesofantiquity.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.frostytrix.echoesofantiquity.item.ModItems;
import net.frostytrix.echoesofantiquity.util.ModTags;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagProvider.ItemTagProvider {
    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {

        getOrCreateTagBuilder(ItemTags.SWORDS)
                .add(ModItems.RELIC_GREATSWORD)
                .add(ModItems.SOUL_SIPHON)
                ;

        getOrCreateTagBuilder(ItemTags.PICKAXES)
        ;

        getOrCreateTagBuilder(ItemTags.AXES)
        ;

        getOrCreateTagBuilder(ItemTags.SHOVELS)
        ;

        getOrCreateTagBuilder(ItemTags.HOES)
        ;

        getOrCreateTagBuilder(ItemTags.TRIMMABLE_ARMOR)
        ;

        getOrCreateTagBuilder(ItemTags.HEAD_ARMOR_ENCHANTABLE)
                ;

        getOrCreateTagBuilder(ItemTags.CHEST_ARMOR_ENCHANTABLE)
                ;

        getOrCreateTagBuilder(ItemTags.LEG_ARMOR_ENCHANTABLE)
                ;

        getOrCreateTagBuilder(ItemTags.FOOT_ARMOR_ENCHANTABLE)
                .add(ModItems.ENDER_BOOTS)
                ;

        getOrCreateTagBuilder(ItemTags.TRIM_MATERIALS)
                ;

        getOrCreateTagBuilder(ItemTags.TRIM_TEMPLATES)
                ;

        getOrCreateTagBuilder(ItemTags.BOW_ENCHANTABLE)
                .add(ModItems.DRAGON_BOW)
                ;
        getOrCreateTagBuilder(ItemTags.DAMPENS_VIBRATIONS)
                .add(ModItems.ENDER_BOOTS)
            ;

        getOrCreateTagBuilder(ModTags.Items.GLASS_PANES)
                .add(Item.fromBlock(Blocks.GLASS_PANE))
                .add(Item.fromBlock(Blocks.LIGHT_GRAY_STAINED_GLASS_PANE))
                .add(Item.fromBlock(Blocks.GRAY_STAINED_GLASS_PANE))
                .add(Item.fromBlock(Blocks.BLACK_STAINED_GLASS_PANE))
                .add(Item.fromBlock(Blocks.GREEN_STAINED_GLASS_PANE))
                .add(Item.fromBlock(Blocks.LIME_STAINED_GLASS_PANE))
                .add(Item.fromBlock(Blocks.BLUE_STAINED_GLASS_PANE))
                .add(Item.fromBlock(Blocks.LIGHT_BLUE_STAINED_GLASS))
                .add(Item.fromBlock(Blocks.CYAN_STAINED_GLASS_PANE))
                .add(Item.fromBlock(Blocks.BROWN_STAINED_GLASS_PANE))
                .add(Item.fromBlock(Blocks.MAGENTA_STAINED_GLASS_PANE))
                .add(Item.fromBlock(Blocks.ORANGE_STAINED_GLASS_PANE))
                .add(Item.fromBlock(Blocks.PINK_STAINED_GLASS_PANE))
                .add(Item.fromBlock(Blocks.PURPLE_STAINED_GLASS_PANE))
                .add(Item.fromBlock(Blocks.YELLOW_STAINED_GLASS_PANE))
                .add(Item.fromBlock(Blocks.RED_STAINED_GLASS_PANE))
                .add(Item.fromBlock(Blocks.WHITE_STAINED_GLASS_PANE))
        ;
    }
}
