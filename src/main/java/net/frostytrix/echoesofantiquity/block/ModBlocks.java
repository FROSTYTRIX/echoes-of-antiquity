package net.frostytrix.echoesofantiquity.block;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.frostytrix.echoesofantiquity.EchoesOfAntiquity;
import net.frostytrix.echoesofantiquity.block.custom.*;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class ModBlocks {

    public static final Block PLACEHOLDER = registerBlock("placeholder",
            new Block(AbstractBlock.Settings.create().strength(1f)));

    public static final Block VOID_PEDESTAL = registerBlock("void_pedestal",
            new VoidPedestalBlock(AbstractBlock.Settings.create().nonOpaque().strength(50.0F, 1200.0F).requiresTool()));

    public static final Block VOID_ANCHOR = registerBlock("void_anchor",
            new VoidAnchorBlock(AbstractBlock.Settings.create().requiresTool().strength(3.0F, 9.0F)));

    public static final Block UNCRAFTER = registerBlock("uncrafter",
            new UncrafterBlock(AbstractBlock.Settings.create().strength(2.5F).sounds(BlockSoundGroup.WOOD).burnable()));

    public static final Block WAYSTONE = registerBlock("waystone",
            new WaystoneBlock(AbstractBlock.Settings.create()
                    .nonOpaque().requiresTool().strength(1.5F, 6.0F).sounds(BlockSoundGroup.STONE)
                    .luminance(state -> state.get(WaystoneBlock.ACTIVE) ? 9 : 0).requiresTool()));

    public static final Block GRAVITY_ANCHOR = registerBlock("gravity_anchor",
            new GravityAnchorBlock(AbstractBlock.Settings.create()
                    .nonOpaque().strength(5.0F, 6.0F).sounds(BlockSoundGroup.METAL).requiresTool()
                    .luminance(state -> state.get(GravityAnchorBlock.ACTIVE) ? 6 : 0)));

    public static final Block SIEVE = registerBlock("sieve",
            new SieveBlock(AbstractBlock.Settings.create()
                    .nonOpaque().sounds(BlockSoundGroup.WOOD).requiresTool()));






    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(EchoesOfAntiquity.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, Identifier.of(EchoesOfAntiquity.MOD_ID, name),
                new BlockItem(block,new Item.Settings()));
    }

    public static void registerModBlocks() {
        EchoesOfAntiquity.LOGGER.info("Registering Mod Blocks for " + EchoesOfAntiquity.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(fabricItemGroupEntries -> {
        });
    }
}
