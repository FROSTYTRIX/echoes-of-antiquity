package net.frostytrix.echoesofantiquity.component;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringIdentifiable;

public enum MagnetMode implements StringIdentifiable {
    OFF("off"),
    ATTRACTING("attracting"),
    REPULSING("repulsing");

    public static final Codec<MagnetMode> CODEC = StringIdentifiable.createCodec(MagnetMode::values);

    private final String name;

    MagnetMode(String name) {
        this.name = name;
    }

    public MagnetMode next() {
        return switch (this) {
            case OFF -> ATTRACTING;
            case ATTRACTING -> REPULSING;
            case REPULSING -> OFF;
        };
    }

    public String translationKey() {
        return "tooltip.echoesofantiquity.magnetic_ring." + this.name;
    }

    @Override
    public String asString() {
        return this.name;
    }
}
