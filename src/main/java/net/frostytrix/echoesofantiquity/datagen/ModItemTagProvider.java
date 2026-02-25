package net.frostytrix.echoesofantiquity.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.frostytrix.echoesofantiquity.item.ModItems;
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
                ;
        getOrCreateTagBuilder(ItemTags.DAMPENS_VIBRATIONS)
                .add(ModItems.ENDER_BOOTS)
            ;
    }
}
