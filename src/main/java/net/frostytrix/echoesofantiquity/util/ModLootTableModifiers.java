package net.frostytrix.echoesofantiquity.util;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.frostytrix.echoesofantiquity.item.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemConvertible;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;

public class ModLootTableModifiers {

    public static void modifyLootTables() {
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registry) -> {
            if (EntityType.CAVE_SPIDER.getLootTableId().equals(key)) {
                tableBuilder.pool(chanceDrop(ModItems.CLIMBING_SPIDER_LEG, 0.005F, 1, 1));
            }

            if (LootTables.PILLAGER_OUTPOST_CHEST.equals(key)) {
                tableBuilder.pool(chanceDrop(ModItems.SOUL_FRAGMENT, 0.1F, 1, 2));
            }

            if (LootTables.WOODLAND_MANSION_CHEST.equals(key)) {
                tableBuilder.pool(chanceDrop(ModItems.SOUL_FRAGMENT, 0.14F, 1, 2));
                tableBuilder.pool(chanceDrop(ModItems.SOUL_SIPHON, 0.20F, 1, 1));
            }

            if (LootTables.END_CITY_TREASURE_CHEST.equals(key)) {
                tableBuilder.pool(chanceDrop(ModItems.END_STEEL_UPGRADE, 0.35F, 1, 1));
            }

            if (LootTables.STRONGHOLD_CORRIDOR_CHEST.equals(key)) {
                tableBuilder.pool(chanceDrop(ModItems.END_STEEL_UPGRADE, 0.10F, 1, 1));
            }
        });
    }

    private static LootPool.Builder chanceDrop(ItemConvertible item, float chance, float min, float max) {
        return LootPool.builder()
                .rolls(ConstantLootNumberProvider.create(1))
                .conditionally(RandomChanceLootCondition.builder(chance))
                .with(ItemEntry.builder(item))
                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(min, max)).build());
    }
}
