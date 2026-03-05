package net.frostytrix.echoesofantiquity.util;

import net.frostytrix.echoesofantiquity.EchoesOfAntiquity;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ModTags {

    public static class Blocks {
        public static final TagKey<Block> NEEDS_END_STEEL_TOOL = createTag("needs_end_steel_tool");
        public static final TagKey<Block> INCORRECT_FOR_END_STEEL_TOOL = createTag("incorrect_for_end_steel_tool");

        private static TagKey<Block> createTag(String name) {
            return TagKey.of(RegistryKeys.BLOCK, Identifier.of(EchoesOfAntiquity.MOD_ID, name));
        }

    }

    public static class Items {
        public static final TagKey<Item> GLASS_PANES = createTag("glass_panes");

        private static TagKey<Item> createTag(String name) {
            return TagKey.of(RegistryKeys.ITEM, Identifier.of(EchoesOfAntiquity.MOD_ID, name));
        }
    }
}
