package net.frostytrix.echoesofantiquity.recipe.sieve;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

public record SieveResult(ItemStack stack, float chance) {

    public static final Codec<SieveResult> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            ItemStack.CODEC.fieldOf("item").forGetter(SieveResult::stack),
            Codec.FLOAT.fieldOf("chance").forGetter(SieveResult::chance)
    ).apply(inst, SieveResult::new));

    public static final PacketCodec<RegistryByteBuf, SieveResult> STREAM_CODEC = PacketCodec.tuple(
            ItemStack.PACKET_CODEC, SieveResult::stack,
            PacketCodecs.FLOAT, SieveResult::chance,
            SieveResult::new
    );
}