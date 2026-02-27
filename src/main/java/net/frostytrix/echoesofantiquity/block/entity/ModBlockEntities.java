package net.frostytrix.echoesofantiquity.block.entity;

import net.frostytrix.echoesofantiquity.EchoesOfAntiquity;
import net.frostytrix.echoesofantiquity.block.ModBlocks;
import net.frostytrix.echoesofantiquity.block.entity.custom.VoidAnchorBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {
    public static final BlockEntityType<VoidAnchorBlockEntity> VOID_ANCHOR_BE =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(EchoesOfAntiquity.MOD_ID, "void_anchor_be"),
                    BlockEntityType.Builder.create(VoidAnchorBlockEntity::new, ModBlocks.VOID_ANCHOR).build(null));

    public static void registerBlockEntities() {
        EchoesOfAntiquity.LOGGER.info("Registering block entities");
    }
}
