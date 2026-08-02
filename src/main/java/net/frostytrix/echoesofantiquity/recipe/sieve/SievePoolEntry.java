package net.frostytrix.echoesofantiquity.recipe.sieve;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

/** One entry of a {@link SievePool}. The stack carries its own count. */
public record SievePoolEntry(ItemStack stack, int weight) {

    public static final Codec<SievePoolEntry> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            ItemStack.CODEC.fieldOf("item").forGetter(SievePoolEntry::stack),
            Codec.INT.optionalFieldOf("weight", 1).forGetter(SievePoolEntry::weight)
    ).apply(inst, SievePoolEntry::new));

    public static final PacketCodec<RegistryByteBuf, SievePoolEntry> STREAM_CODEC = PacketCodec.tuple(
            ItemStack.PACKET_CODEC, SievePoolEntry::stack,
            PacketCodecs.INTEGER, SievePoolEntry::weight,
            SievePoolEntry::new
    );

    public int effectiveWeight() {
        return Math.max(0, weight);
    }
}
