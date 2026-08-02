package net.frostytrix.echoesofantiquity.recipe.sieve;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.math.random.Random;

import java.util.List;

/**
 * A pool fires with {@code chance}, then draws {@code rolls} entries from it, weighted.
 * The same entry can come out twice.
 */
public record SievePool(float chance, int rolls, List<SievePoolEntry> entries) {

    public static final Codec<SievePool> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.FLOAT.optionalFieldOf("chance", 1.0F).forGetter(SievePool::chance),
            Codec.INT.optionalFieldOf("rolls", 1).forGetter(SievePool::rolls),
            SievePoolEntry.CODEC.listOf().fieldOf("entries").forGetter(SievePool::entries)
    ).apply(inst, SievePool::new));

    public static final PacketCodec<RegistryByteBuf, SievePool> STREAM_CODEC = PacketCodec.tuple(
            PacketCodecs.FLOAT, SievePool::chance,
            PacketCodecs.INTEGER, SievePool::rolls,
            SievePoolEntry.STREAM_CODEC.collect(PacketCodecs.toList()), SievePool::entries,
            SievePool::new
    );

    public int totalWeight() {
        int total = 0;
        for (SievePoolEntry entry : entries) {
            total += entry.effectiveWeight();
        }
        return total;
    }

    /** @return a copy of one drawn stack, or empty if the pool has no usable weight. */
    public ItemStack draw(Random random) {
        int total = totalWeight();
        if (total <= 0) {
            return ItemStack.EMPTY;
        }

        int roll = random.nextInt(total);
        for (SievePoolEntry entry : entries) {
            roll -= entry.effectiveWeight();
            if (roll < 0) {
                return entry.stack().copy();
            }
        }
        return ItemStack.EMPTY;
    }
}
