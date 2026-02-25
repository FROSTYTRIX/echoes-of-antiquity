package net.frostytrix.echoesofantiquity.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.frostytrix.echoesofantiquity.EchoesOfAntiquity;
import net.frostytrix.echoesofantiquity.block.ModBlocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    public static final ItemGroup THE_FALLEN_HUMANS = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(EchoesOfAntiquity.MOD_ID, "the_fallen_humans"),
            FabricItemGroup.builder().icon(() -> new ItemStack(Items.ENDER_EYE))
                    .displayName(Text.translatable("itemgroup.echoesofantiquity.the_fallen_humans"))
                    .entries((displayContext, entries) -> {
                        entries.add(new ItemStack(ModItems.ANCIENT_SCRIP));
                        entries.add(new ItemStack(ModItems.VOID_TREATED_LEATHER));
                        entries.add(new ItemStack(ModItems.ENDER_BOOTS));
                        entries.add(new ItemStack(ModBlocks.PLACEHOLDER));
                    }).build());


    public static void registerItemGroups() {
        EchoesOfAntiquity.LOGGER.info("Registering Mod Item Groups for" + EchoesOfAntiquity.MOD_ID);
    }
}
