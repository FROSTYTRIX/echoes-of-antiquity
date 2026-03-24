package net.frostytrix.echoesofantiquity.entity;

import net.frostytrix.echoesofantiquity.EchoesOfAntiquity;
import net.frostytrix.echoesofantiquity.entity.custom.ChorusHuskEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {
    public static final EntityType<ChorusHuskEntity>  CHORUS_HUSK = Registry.register(Registries.ENTITY_TYPE,
            Identifier.of(EchoesOfAntiquity.MOD_ID, "chorus_husk"),
            EntityType.Builder.create(ChorusHuskEntity::new, SpawnGroup.MONSTER)
                    .dimensions(1.3f, 3.1f).build());

    public static void registerModEntities(){
        EchoesOfAntiquity.LOGGER.info("[EchoesOfAntiquity] Registering entities");
    }
}
