package net.frostytrix.echoesofantiquity.util;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.frostytrix.echoesofantiquity.item.ModItems;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.util.Identifier;

public class ModLootTableModifiers {
    private static final Identifier CAVE_SPIDER_ID =
            Identifier.of("minecraft", "entities/cave_spider");

    public static void modifyLootTables() {
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registry) -> {
            if (CAVE_SPIDER_ID.equals(key.getValue())){
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.005F))
                        .with(ItemEntry.builder(ModItems.CLIMBING_SPIDER_LEG))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 1.0F)).build());
            tableBuilder.pool(poolBuilder.build());
            }

            if (LootTables.PILLAGER_OUTPOST_CHEST.equals(key)){
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.1F))
                        .with(ItemEntry.builder(ModItems.SOUL_FRAGMENT))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 2.0F)).build());
                tableBuilder.pool(poolBuilder.build());
            }

            if (LootTables.WOODLAND_MANSION_CHEST.equals(key)){
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.14F))
                        .with(ItemEntry.builder(ModItems.SOUL_FRAGMENT))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 2.0F)).build());
                tableBuilder.pool(poolBuilder.build());
            }

            if (LootTables.WOODLAND_MANSION_CHEST.equals(key)){
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.20F))
                        .with(ItemEntry.builder(ModItems.SOUL_SIPHON))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 1.0F)).build());
                tableBuilder.pool(poolBuilder.build());
            }
        });
    }

}
