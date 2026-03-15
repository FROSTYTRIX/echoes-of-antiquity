package net.frostytrix.echoesofantiquity.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

public record SieveResult(ItemStack stack, float chance) {

    // Defines how to read/write this from the Recipe JSON
    public static final Codec<SieveResult> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            ItemStack.CODEC.fieldOf("item").forGetter(SieveResult::stack),
            Codec.FLOAT.fieldOf("chance").forGetter(SieveResult::chance)
    ).apply(inst, SieveResult::new));

    // Defines how to sync this from the server to the client
    public static final PacketCodec<RegistryByteBuf, SieveResult> STREAM_CODEC = PacketCodec.tuple(
            ItemStack.PACKET_CODEC, SieveResult::stack,
            PacketCodecs.FLOAT, SieveResult::chance,
            SieveResult::new
    );
}