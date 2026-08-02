package net.frostytrix.echoesofantiquity.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;

public record MeasuringTapeData(Optional<BlockPos> first, Optional<BlockPos> second, Mode mode) {

    public static final MeasuringTapeData EMPTY = new MeasuringTapeData(Optional.empty(), Optional.empty(), Mode.VECTOR);

    public static final Codec<MeasuringTapeData> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            BlockPos.CODEC.optionalFieldOf("first").forGetter(MeasuringTapeData::first),
            BlockPos.CODEC.optionalFieldOf("second").forGetter(MeasuringTapeData::second),
            Mode.CODEC.optionalFieldOf("mode", Mode.VECTOR).forGetter(MeasuringTapeData::mode)
    ).apply(inst, MeasuringTapeData::new));

    public MeasuringTapeData withFirst(BlockPos pos) {
        return new MeasuringTapeData(Optional.of(pos), this.second, this.mode);
    }

    public MeasuringTapeData withSecond(BlockPos pos) {
        return new MeasuringTapeData(this.first, Optional.of(pos), this.mode);
    }

    public MeasuringTapeData cleared() {
        return new MeasuringTapeData(Optional.empty(), Optional.empty(), this.mode);
    }

    public MeasuringTapeData toggledMode() {
        return new MeasuringTapeData(this.first, this.second, this.mode.other());
    }

    public enum Mode implements StringIdentifiable {
        VECTOR("vector_distance"),
        MANHATTAN("manhattan_distance");

        public static final Codec<Mode> CODEC = StringIdentifiable.createCodec(Mode::values);

        private final String name;

        Mode(String name) {
            this.name = name;
        }

        public Mode other() {
            return this == VECTOR ? MANHATTAN : VECTOR;
        }

        public String translationKey() {
            return "tooltip.echoesofantiquity.measuring_tape." + this.name;
        }

        @Override
        public String asString() {
            return this.name;
        }
    }
}
