package net.frostytrix.echoesofantiquity.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.frostytrix.echoesofantiquity.EchoesOfAntiquity;
import net.frostytrix.echoesofantiquity.block.ModBlocks;
import net.frostytrix.echoesofantiquity.item.ModItems;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.item.ItemConvertible;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ModAdvancementProvider extends FabricAdvancementProvider {
    private static final Identifier BACKGROUND = Identifier.ofVanilla("textures/block/end_stone.png");

    public ModAdvancementProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(output, registryLookup);
    }

    @Override
    public void generateAdvancement(RegistryWrapper.WrapperLookup lookup, Consumer<AdvancementEntry> consumer) {
        AdvancementEntry root = Advancement.Builder.create()
                .display(ModItems.END_STEEL_INGOT, title("root"), description("root"),
                        BACKGROUND, AdvancementFrame.TASK, true, true, false)
                .criterion("has_end_steel", hasItem(ModItems.END_STEEL_INGOT))
                .build(consumer, id("root"));

        // --- The Fallen Humans ---

        AdvancementEntry voidLeather = child(consumer, root, ModItems.VOID_TREATED_LEATHER, "void_treated_leather", AdvancementFrame.TASK);
        child(consumer, voidLeather, ModItems.ENDER_BOOTS, "ender_boots", AdvancementFrame.TASK);
        child(consumer, voidLeather, ModItems.OBSIDIAN_GOGGLES, "obsidian_goggles", AdvancementFrame.TASK);
        child(consumer, voidLeather, ModItems.DRAGON_BOW, "dragon_bow", AdvancementFrame.GOAL);

        AdvancementEntry relicBlade = child(consumer, root, ModItems.RELIC_BlADE, "relic_blade", AdvancementFrame.TASK);
        child(consumer, relicBlade, ModItems.RELIC_GREATSWORD, "relic_greatsword", AdvancementFrame.GOAL);

        AdvancementEntry upgrade = child(consumer, root, ModItems.END_STEEL_UPGRADE, "end_steel_upgrade", AdvancementFrame.TASK);
        Advancement.Builder.create()
                .parent(upgrade)
                .display(ModItems.VOID_CHAINMAIL_CHESTPLATE, title("void_chainmail"), description("void_chainmail"),
                        null, AdvancementFrame.CHALLENGE, true, true, false)
                .criterion("has_helmet", hasItem(ModItems.VOID_CHAINMAIL_HELMET))
                .criterion("has_chestplate", hasItem(ModItems.VOID_CHAINMAIL_CHESTPLATE))
                .criterion("has_leggings", hasItem(ModItems.VOID_CHAINMAIL_LEGGINGS))
                .criterion("has_boots", hasItem(ModItems.VOID_CHAINMAIL_BOOTS))
                .build(consumer, id("void_chainmail"));

        AdvancementEntry pedestal = child(consumer, root, ModBlocks.VOID_PEDESTAL, "void_pedestal", AdvancementFrame.TASK);
        AdvancementEntry anchor = child(consumer, pedestal, ModBlocks.VOID_ANCHOR, "void_anchor", AdvancementFrame.TASK);
        child(consumer, anchor, ModItems.STATIC_PEARL, "static_pearl", AdvancementFrame.GOAL);

        // --- The Failed Clones ---

        AdvancementEntry soulFragment = child(consumer, root, ModItems.SOUL_FRAGMENT, "soul_fragment", AdvancementFrame.TASK);
        child(consumer, soulFragment, ModItems.SOUL_SIPHON, "soul_siphon", AdvancementFrame.TASK);
        child(consumer, soulFragment, ModBlocks.WAYSTONE, "waystone", AdvancementFrame.GOAL);

        // --- The Architect's Tools ---

        AdvancementEntry sieve = child(consumer, soulFragment, ModBlocks.SIEVE, "sieve", AdvancementFrame.TASK);
        child(consumer, sieve, ModItems.FOUNDATION_SHERD, "foundation_sherd", AdvancementFrame.GOAL);
        child(consumer, sieve, ModItems.ANCIENT_SCRIP, "ancient_scrip", AdvancementFrame.TASK);

        AdvancementEntry tools = child(consumer, root, ModItems.MEASURING_TAPE, "measuring_tape", AdvancementFrame.TASK);
        child(consumer, tools, ModItems.LEVEL, "level", AdvancementFrame.TASK);
        child(consumer, tools, ModItems.MAGNET_RING, "magnet_ring", AdvancementFrame.TASK);
        child(consumer, tools, ModItems.INFINITE_WATER_BUCKET, "infinite_water_bucket", AdvancementFrame.GOAL);
        child(consumer, tools, ModBlocks.UNCRAFTER, "uncrafter", AdvancementFrame.TASK);
        child(consumer, tools, ModBlocks.GRAVITY_ANCHOR, "gravity_anchor", AdvancementFrame.GOAL);

        child(consumer, root, ModItems.CLIMBING_SPIDER_LEG, "climbing_spider_leg", AdvancementFrame.GOAL);
    }

    private AdvancementEntry child(Consumer<AdvancementEntry> consumer, AdvancementEntry parent,
                                   ItemConvertible item, String name, AdvancementFrame frame) {
        return Advancement.Builder.create()
                .parent(parent)
                .display(item, title(name), description(name), null, frame, true, true, false)
                .criterion("has_" + name, hasItem(item))
                .build(consumer, id(name));
    }

    private static AdvancementCriterion<InventoryChangedCriterion.Conditions> hasItem(ItemConvertible item) {
        return InventoryChangedCriterion.Conditions.items(item);
    }

    private static Text title(String name) {
        return Text.translatable("advancements." + EchoesOfAntiquity.MOD_ID + "." + name + ".title");
    }

    private static Text description(String name) {
        return Text.translatable("advancements." + EchoesOfAntiquity.MOD_ID + "." + name + ".description");
    }

    private static String id(String name) {
        return EchoesOfAntiquity.MOD_ID + ":" + name;
    }
}
