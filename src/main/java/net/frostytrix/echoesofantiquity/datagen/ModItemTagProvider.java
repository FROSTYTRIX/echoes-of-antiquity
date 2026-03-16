package net.frostytrix.echoesofantiquity.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.frostytrix.echoesofantiquity.item.ModItems;
import net.frostytrix.echoesofantiquity.util.ModTags;
import net.minecraft.block.Blocks;
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
                .add(ModItems.VOID_CHAINMAIL_HELMET)
                ;

        getOrCreateTagBuilder(ItemTags.CHEST_ARMOR_ENCHANTABLE)
                .add(ModItems.VOID_CHAINMAIL_CHESTPLATE)
                ;

        getOrCreateTagBuilder(ItemTags.LEG_ARMOR_ENCHANTABLE)
                .add(ModItems.VOID_CHAINMAIL_LEGGINGS)
                ;

        getOrCreateTagBuilder(ItemTags.FOOT_ARMOR_ENCHANTABLE)
                .add(ModItems.VOID_CHAINMAIL_BOOTS)
                .add(ModItems.ENDER_BOOTS)
                ;

        getOrCreateTagBuilder(ItemTags.TRIM_MATERIALS)
                ;

        getOrCreateTagBuilder(ItemTags.TRIM_TEMPLATES)
                ;

        getOrCreateTagBuilder(ItemTags.BOW_ENCHANTABLE)
                .add(ModItems.DRAGON_BOW)
                ;

        getOrCreateTagBuilder(ModTags.Items.GLASS_PANES)
                .add((Blocks.GLASS_PANE.asItem()))
                .add((Blocks.LIGHT_GRAY_STAINED_GLASS_PANE.asItem()))
                .add((Blocks.GRAY_STAINED_GLASS_PANE.asItem()))
                .add((Blocks.BLACK_STAINED_GLASS_PANE.asItem()))
                .add((Blocks.GREEN_STAINED_GLASS_PANE.asItem()))
                .add((Blocks.LIME_STAINED_GLASS_PANE.asItem()))
                .add((Blocks.BLUE_STAINED_GLASS_PANE.asItem()))
                .add((Blocks.LIGHT_BLUE_STAINED_GLASS_PANE.asItem()))
                .add((Blocks.CYAN_STAINED_GLASS_PANE.asItem()))
                .add((Blocks.BROWN_STAINED_GLASS_PANE.asItem()))
                .add((Blocks.MAGENTA_STAINED_GLASS_PANE.asItem()))
                .add((Blocks.ORANGE_STAINED_GLASS_PANE.asItem()))
                .add((Blocks.PINK_STAINED_GLASS_PANE.asItem()))
                .add((Blocks.PURPLE_STAINED_GLASS_PANE.asItem()))
                .add((Blocks.YELLOW_STAINED_GLASS_PANE.asItem()))
                .add((Blocks.RED_STAINED_GLASS_PANE.asItem()))
                .add((Blocks.WHITE_STAINED_GLASS_PANE.asItem()))
        ;

        getOrCreateTagBuilder(ModTags.Items.ALL_STONES)
                .add(Blocks.STONE.asItem())
                .add(Blocks.COBBLESTONE.asItem())
                .add(Blocks.MOSSY_COBBLESTONE.asItem())
                .add(Blocks.DIORITE.asItem())
                .add(Blocks.ANDESITE.asItem())
                .add(Blocks.GRANITE.asItem())
                .add(Blocks.DEEPSLATE.asItem())
                .add(Blocks.COBBLED_DEEPSLATE.asItem())
                .add(Blocks.BLACKSTONE.asItem())
        ;

        getOrCreateTagBuilder(ItemTags.TRIMMABLE_ARMOR)
                .add(ModItems.VOID_CHAINMAIL_HELMET)
                .add(ModItems.VOID_CHAINMAIL_CHESTPLATE)
                .add(ModItems.VOID_CHAINMAIL_LEGGINGS)
                .add(ModItems.VOID_CHAINMAIL_BOOTS)
                .add(ModItems.ENDER_BOOTS)
                ;

        var sherdTag = getOrCreateTagBuilder(ItemTags.DECORATED_POT_SHERDS);
        ModItems.CUSTOM_SHERDS.forEach(sherdTag::add);
    }
}
