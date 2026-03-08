package net.frostytrix.echoesofantiquity.item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.frostytrix.echoesofantiquity.EchoesOfAntiquity;
import net.frostytrix.echoesofantiquity.item.custom.DragonBowItem;
import net.frostytrix.echoesofantiquity.item.custom.MeasuringTapeItem;
import net.frostytrix.echoesofantiquity.item.custom.SoulSiphonItem;
import net.frostytrix.echoesofantiquity.item.custom.StaticPearlItem;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems{

    // Items Registry

    // Fallen Humans

    public static final Item ANCIENT_SCRIP = registerItem("ancient_scrip", new Item(new Item.Settings()));
    public static final Item END_STEEL_INGOT = registerItem("end_steel_ingot", new Item(new Item.Settings()));
    public static final Item VOID_TREATED_LEATHER = registerItem("void_treated_leather", new Item(new Item.Settings()));


    public static final Item ENDER_BOOTS = registerItem("ender_boots",
            new ArmorItem(ModArmorMaterials.ENDER_ARMOR_MATERIAL, ArmorItem.Type.BOOTS, new Item.Settings().maxDamage(ArmorItem.Type.BOOTS.getMaxDamage(7))));
    public static final Item OBSIDIAN_GOGGLES = registerItem("obsidian_goggles",
            new ArmorItem(ModArmorMaterials.ENDER_ARMOR_MATERIAL, ArmorItem.Type.HELMET, new Item.Settings().maxDamage(ArmorItem.Type.HELMET.getMaxDamage(7))));

    public static final Item RELIC_BlADE = registerItem("relic_blade", new Item(new Item.Settings()));
    public static final Item RELIC_GREATSWORD = registerItem("relic_greatsword", new SwordItem(ModToolMaterials.END_STEEL,new Item.Settings().attributeModifiers(
            SwordItem.createAttributeModifiers(ModToolMaterials.END_STEEL, 4, -3.4f))));

    public static final Item DRAGON_BOW = registerItem("dragon_bow",
            new DragonBowItem(new Item.Settings().maxDamage(2500)));

    public static final Item STATIC_PEARL = registerItem("static_pearl",
            new StaticPearlItem(new Item.Settings().maxDamage(20)));


    // Failed Clones

    public static final Item SOUL_FRAGMENT = registerItem("soul_fragment", new Item(new Item.Settings()));
    public static final Item SOUL_SIPHON = registerItem("soul_siphon", new SoulSiphonItem(ToolMaterials.IRON, new Item.Settings().attributeModifiers(
            SwordItem.createAttributeModifiers(ToolMaterials.IRON, 2, -1.4f))));

    // Architect's Tools

    public static final Item CLIMBING_SPIDER_LEG = registerItem("climbing_spider_leg", new Item(new Item.Settings().maxCount(1)));
    public static final Item MEASURING_TAPE = registerItem("measuring_tape", new MeasuringTapeItem(new Item.Settings().maxCount(1)));

    private static Item registerItem(String name, Item item){
        return Registry.register(Registries.ITEM, Identifier.of(EchoesOfAntiquity.MOD_ID, name), item);
    }

    public static void registerModItems() {
        EchoesOfAntiquity.LOGGER.info("Registering Mod Items for " + EchoesOfAntiquity.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(fabricItemGroupEntries -> {
        });
    }
}
