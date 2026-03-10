package net.frostytrix.echoesofantiquity.block.entity;

import net.frostytrix.echoesofantiquity.EchoesOfAntiquity;
import net.frostytrix.echoesofantiquity.block.ModBlocks;
import net.frostytrix.echoesofantiquity.block.entity.custom.UncrafterBlockEntity;
import net.frostytrix.echoesofantiquity.block.entity.custom.VoidPedestalBlockEntity;
import net.frostytrix.echoesofantiquity.block.entity.custom.WaystoneBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {
    public static final BlockEntityType<VoidPedestalBlockEntity> VOID_PEDESTAL_BE =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(EchoesOfAntiquity.MOD_ID, "void_pedestal_be"),
                    BlockEntityType.Builder.create(VoidPedestalBlockEntity::new, ModBlocks.VOID_PEDESTAL).build(null));

    public static final BlockEntityType<UncrafterBlockEntity> UNCRAFTER_BE =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(EchoesOfAntiquity.MOD_ID, "uncrafter_be"),
                    BlockEntityType.Builder.create(UncrafterBlockEntity::new, ModBlocks.UNCRAFTER).build(null));

    public static final BlockEntityType<WaystoneBlockEntity> WAYSTONE_BE =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(EchoesOfAntiquity.MOD_ID, "waystone_be"),
                    BlockEntityType.Builder.create(WaystoneBlockEntity::new, ModBlocks.WAYSTONE).build(null));




    public static void registerBlockEntities() {
        EchoesOfAntiquity.LOGGER.info("Registering block entities");
    }
}
