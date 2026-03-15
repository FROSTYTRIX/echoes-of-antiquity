package net.frostytrix.echoesofantiquity.recipe.sieve;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

import java.util.List;

public record SievePool(float chance, List<ItemStack> items) {

    public static final Codec<SievePool> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.FLOAT.fieldOf("chance").forGetter(SievePool::chance),
            ItemStack.CODEC.listOf().fieldOf("items").forGetter(SievePool::items)
    ).apply(inst, SievePool::new));

    public static final PacketCodec<RegistryByteBuf, SievePool> STREAM_CODEC = PacketCodec.tuple(
            PacketCodecs.FLOAT, SievePool::chance,
            ItemStack.PACKET_CODEC.collect(PacketCodecs.toList()), SievePool::items,
            SievePool::new
    );
}