package net.frostytrix.echoesofantiquity.item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.frostytrix.echoesofantiquity.EchoesOfAntiquity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems{

    // Items Registry

    public static final Item ANCIENT_SCRIP = registerItem("ancient_scrip", new Item(new Item.Settings()));
    public static final Item VOID_TREATED_LEATHER = registerItem("void_treated_leather", new Item(new Item.Settings()));
    public static final Item ENDER_BOOTS = registerItem("ender_boots",
            new ArmorItem(ModArmorMaterials.ENDER_ARMOR_MATERIAL, ArmorItem.Type.BOOTS, new Item.Settings().maxDamage(ArmorItem.Type.BOOTS.getMaxDamage(7))));

    private static Item registerItem(String name, Item item){
        return Registry.register(Registries.ITEM, Identifier.of(EchoesOfAntiquity.MOD_ID, name), item);
    }

    public static void registerModItems() {
        EchoesOfAntiquity.LOGGER.info("Registering Mod Items for " + EchoesOfAntiquity.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(fabricItemGroupEntries -> {
        });
    }
}
