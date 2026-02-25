package net.frostytrix.echoesofantiquity.item;

import net.frostytrix.echoesofantiquity.EchoesOfAntiquity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

public class ModArmorMaterials {
    public static final RegistryEntry<ArmorMaterial> ENDER_ARMOR_MATERIAL = registerArmorMaterial("ender_armor_material",
            () -> new ArmorMaterial(Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                map.put(ArmorItem.Type.BOOTS, 2);
            }), 20, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, null,
                    List.of(new ArmorMaterial.Layer(Identifier.of(EchoesOfAntiquity.MOD_ID, "ender"))), 0,0));

    public static RegistryEntry<ArmorMaterial> registerArmorMaterial(String name, Supplier<ArmorMaterial> material) {
        return Registry.registerReference(Registries.ARMOR_MATERIAL, Identifier.of(EchoesOfAntiquity.MOD_ID), material.get());
    }
}
