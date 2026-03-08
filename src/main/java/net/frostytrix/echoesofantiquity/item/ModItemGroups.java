package net.frostytrix.echoesofantiquity.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.frostytrix.echoesofantiquity.EchoesOfAntiquity;
import net.frostytrix.echoesofantiquity.block.ModBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    public static final ItemGroup THE_FAILED_CLONES = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(EchoesOfAntiquity.MOD_ID, "the_failed_clones"),
            FabricItemGroup.builder().icon(() -> new ItemStack(Items.LAPIS_LAZULI))
                    .displayName(Text.translatable("itemgroup.echoesofantiquity.the_failed_clones"))
                    .entries((displayContext, entries) -> {
                        entries.add(new ItemStack(ModItems.SOUL_SIPHON));
                        entries.add(new ItemStack(ModItems.SOUL_FRAGMENT));
                    }).build());

    public static final ItemGroup THE_FALLEN_HUMANS = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(EchoesOfAntiquity.MOD_ID, "the_fallen_humans"),
            FabricItemGroup.builder().icon(() -> new ItemStack(Items.ENDER_EYE))
                    .displayName(Text.translatable("itemgroup.echoesofantiquity.the_fallen_humans"))
                    .entries((displayContext, entries) -> {
                        entries.add(new ItemStack(ModItems.VOID_TREATED_LEATHER));
                        entries.add(new ItemStack(ModItems.OBSIDIAN_GOGGLES));
                        entries.add(new ItemStack(ModItems.ENDER_BOOTS));
                        entries.add(new ItemStack(ModItems.END_STEEL_INGOT));
                        entries.add(new ItemStack(ModBlocks.VOID_PEDESTAL));
                        entries.add(new ItemStack(ModItems.RELIC_GREATSWORD));
                        entries.add(new ItemStack(ModItems.DRAGON_BOW));
                        entries.add(new ItemStack(ModItems.STATIC_PEARL));
                        entries.add(new ItemStack(ModBlocks.VOID_ANCHOR));
                    }).build());

    public static final ItemGroup THE_ARCHITECTS_TOOLS = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(EchoesOfAntiquity.MOD_ID, "the_architects_tools"),
            FabricItemGroup.builder().icon(() -> new ItemStack(ModItems.MEASURING_TAPE))
                    .displayName(Text.translatable("itemgroup.echoesofantiquity.the_architects_tools"))
                    .entries((displayContext, entries) -> {
                        entries.add(new ItemStack(ModItems.CLIMBING_SPIDER_LEG));
                        entries.add(new ItemStack(ModItems.MEASURING_TAPE));
                        entries.add(new ItemStack(ModBlocks.UNCRAFTER));
                    }).build());




    public static void registerItemGroups() {
        EchoesOfAntiquity.LOGGER.info("Registering Mod Item Groups for" + EchoesOfAntiquity.MOD_ID);
    }
}
