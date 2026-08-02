package net.frostytrix.echoesofantiquity.component;

import net.frostytrix.echoesofantiquity.EchoesOfAntiquity;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.function.UnaryOperator;


public class ModDataComponentTypes {
    public static final ComponentType<BlockPos> COORDINATES =
            register("coordinates", builder -> builder.codec(BlockPos.CODEC));

    public static final ComponentType<MagnetMode> MAGNET_MODE =
            register("magnet_mode", builder -> builder.codec(MagnetMode.CODEC));

    public static final ComponentType<MeasuringTapeData> MEASURING_TAPE =
            register("measuring_tape", builder -> builder.codec(MeasuringTapeData.CODEC));

    public static final ComponentType<Vec3d> LAST_SAFE_POS =
            register("last_safe_pos", builder -> builder.codec(Vec3d.CODEC));

    private static <T> ComponentType<T> register(String name, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(EchoesOfAntiquity.MOD_ID, name),
                builderOperator.apply(ComponentType.builder()).build());
    }

    public static void registerDataComponents() {
        EchoesOfAntiquity.LOGGER.info("Registering data component types.");
    }
}
