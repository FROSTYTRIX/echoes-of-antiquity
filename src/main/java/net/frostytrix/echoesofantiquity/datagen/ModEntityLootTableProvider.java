package net.frostytrix.echoesofantiquity.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.frostytrix.echoesofantiquity.entity.ModEntities;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.EnchantedCountIncreaseLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class ModEntityLootTableProvider extends SimpleFabricLootTableProvider {

    private final CompletableFuture<RegistryWrapper.WrapperLookup> registryLookupFuture;

    public ModEntityLootTableProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(output, registryLookup, LootContextTypes.ENTITY);
        this.registryLookupFuture = registryLookup;
    }

    @Override
    public void accept(BiConsumer<RegistryKey<LootTable>, LootTable.Builder> exporter)
    {
        RegistryWrapper.WrapperLookup lookup = this.registryLookupFuture.join();
        RegistryWrapper.Impl<Enchantment> enchantments = lookup.getWrapperOrThrow(RegistryKeys.ENCHANTMENT);

        /// exporter.accept(ModEntities.CHORUS_HUSK.getLootTableId(),
        ///         LootTable.builder()
        ///                 .pool(LootPool.builder()
        ///                         .rolls(ConstantLootNumberProvider.create(1.0F))
        ///                         .with(ItemEntry.builder(Items.CHORUS_FRUIT))
        ///                         .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F)))
        ///      )
        /// );

        exporter.accept(ModEntities.CHORUS_HUSK.getLootTableId(),
                createLootingTable(Items.CHORUS_FRUIT, 1.0F, 3.0F, 1.0F, lookup)
        );

    }

    public LootTable.Builder createLootingTable(Item item, float minDrops, float maxDrops, float lootingMultiplier, RegistryWrapper.WrapperLookup registries) {
        return LootTable.builder()
                .pool(LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0F))
                        .with(ItemEntry.builder(item)
                                // Apply the base min/max drop count
                                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(minDrops, maxDrops)))

                                // FIXED: Pass 'registries' directly here!
                                // It will fetch Looting automatically.
                                .apply(EnchantedCountIncreaseLootFunction.builder(
                                        registries,
                                        UniformLootNumberProvider.create(0.0F, lootingMultiplier)
                                ))
                        )
                );
    }
}