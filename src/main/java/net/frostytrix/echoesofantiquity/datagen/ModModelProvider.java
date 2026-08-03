package net.frostytrix.echoesofantiquity.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.frostytrix.echoesofantiquity.block.ModBlocks;
import net.frostytrix.echoesofantiquity.item.ModItems;
import com.google.gson.JsonObject;
import net.minecraft.data.client.*;
import net.minecraft.item.ArmorItem;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {

        // Architect's tools
        blockStateModelGenerator.registerSingleton(ModBlocks.UNCRAFTER, TexturedModel.CUBE_BOTTOM_TOP);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {

        // Fallen Human

        itemModelGenerator.register(ModItems.ANCIENT_SCRIP, Models.GENERATED);

        itemModelGenerator.register(ModItems.END_STEEL_INGOT, Models.GENERATED);
        itemModelGenerator.register(ModItems.VOID_TREATED_LEATHER, Models.GENERATED);

        itemModelGenerator.register(ModItems.RELIC_BlADE, Models.GENERATED);
        itemModelGenerator.register(ModItems.STATIC_PEARL, Models.GENERATED);

        registerTrimmableArmor(itemModelGenerator, (ArmorItem) ModItems.ENDER_BOOTS);
        registerTrimmableArmor(itemModelGenerator, (ArmorItem) ModItems.OBSIDIAN_GOGGLES);

        registerTrimmableArmor(itemModelGenerator, (ArmorItem) ModItems.VOID_CHAINMAIL_HELMET);
        registerTrimmableArmor(itemModelGenerator, (ArmorItem) ModItems.VOID_CHAINMAIL_CHESTPLATE);
        registerTrimmableArmor(itemModelGenerator, (ArmorItem) ModItems.VOID_CHAINMAIL_LEGGINGS);
        registerTrimmableArmor(itemModelGenerator, (ArmorItem) ModItems.VOID_CHAINMAIL_BOOTS);

        itemModelGenerator.register(ModItems.END_STEEL_UPGRADE, Models.GENERATED);

        itemModelGenerator.register(ModItems.CHORUS_HUSK_SPAWN_EGG,
                new Model(Optional.of(Identifier.of("item/template_spawn_egg")), Optional.empty()));

        // Failed clones

        itemModelGenerator.register(ModItems.SOUL_SIPHON, Models.HANDHELD);
        itemModelGenerator.register(ModItems.SOUL_FRAGMENT, Models.GENERATED);

        // Architect's Tools

        itemModelGenerator.register(ModItems.CLIMBING_SPIDER_LEG, Models.HANDHELD_MACE);
        itemModelGenerator.register(ModItems.MEASURING_TAPE, Models.GENERATED);
        itemModelGenerator.register(ModItems.INFINITE_WATER_BUCKET, Models.GENERATED);
        itemModelGenerator.register(ModItems.LEVEL, Models.GENERATED);
        itemModelGenerator.register(ModItems.MAGNET_RING, Models.GENERATED);
        itemModelGenerator.register(ModItems.FOUNDATION_SHERD, Models.GENERATED);
    }

    /**
     * registerArmor only knows the vanilla trim materials. The extra override is added at load time by
     * ArmorTrimOverrideMixin, so all we generate here is the model that override points at.
     */
    private void registerTrimmableArmor(ItemModelGenerator generator, ArmorItem item) {
        generator.registerArmor(item);

        Identifier baseTexture = TextureMap.getId(item);
        Identifier trimModel = ModelIds.getItemModelId(item).withSuffixedPath("_end_steel_trim");
        Identifier trimTexture = Identifier.ofVanilla("trims/items/" + item.getType().getName() + "_trim_end_steel");

        generator.writer.accept(trimModel, () -> {
            JsonObject textures = new JsonObject();
            textures.addProperty("layer0", baseTexture.toString());
            textures.addProperty("layer1", trimTexture.toString());
            JsonObject model = new JsonObject();
            model.addProperty("parent", "minecraft:item/generated");
            model.add("textures", textures);
            return model;
        });
    }
}
