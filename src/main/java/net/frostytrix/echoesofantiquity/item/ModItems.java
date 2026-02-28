package net.frostytrix.echoesofantiquity.item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.frostytrix.echoesofantiquity.EchoesOfAntiquity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.SwordItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems{

    // Items Registry

    public static final Item ANCIENT_SCRIP = registerItem("ancient_scrip", new Item(new Item.Settings()));
    public static final Item END_STEEL_INGOT = registerItem("end_steel_ingot", new Item(new Item.Settings()));
    public static final Item VOID_TREATED_LEATHER = registerItem("void_treated_leather", new Item(new Item.Settings()));
    public static final Item ENDER_BOOTS = registerItem("ender_boots",
            new ArmorItem(ModArmorMaterials.ENDER_ARMOR_MATERIAL, ArmorItem.Type.BOOTS, new Item.Settings().maxDamage(ArmorItem.Type.BOOTS.getMaxDamage(7))));
    public static final Item RELIC_BlADE = registerItem("relic_blade", new Item(new Item.Settings()));
    public static final Item RELIC_GREATSWORD = registerItem("relic_greatsword", new SwordItem(ModToolMaterials.END_STEEL,new Item.Settings().attributeModifiers(
            SwordItem.createAttributeModifiers(ModToolMaterials.END_STEEL, 3, -2.4f))));


    private static Item registerItem(String name, Item item){
        return Registry.register(Registries.ITEM, Identifier.of(EchoesOfAntiquity.MOD_ID, name), item);
    }

    public static void registerModItems() {
        EchoesOfAntiquity.LOGGER.info("Registering Mod Items for " + EchoesOfAntiquity.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(fabricItemGroupEntries -> {
        });
    }
}
