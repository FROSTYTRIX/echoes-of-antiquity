package net.frostytrix.echoesofantiquity.util;

import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Positions of active blocks, bucketed per dimension then per chunk.
 * <p>
 * Lookups only walk the chunks covering the radius instead of every registered position, which matters
 * because they run per falling entity per tick and on every teleport attempt.
 */
public class BlockPosIndex {
    private final Map<RegistryKey<World>, Map<Long, Set<BlockPos>>> byDimension = new HashMap<>();

    public void add(World world, BlockPos pos) {
        byDimension
                .computeIfAbsent(world.getRegistryKey(), k -> new HashMap<>())
                .computeIfAbsent(ChunkPos.toLong(pos), k -> new HashSet<>())
                .add(pos.toImmutable());
    }

    public void remove(World world, BlockPos pos) {
        Map<Long, Set<BlockPos>> chunks = byDimension.get(world.getRegistryKey());
        if (chunks == null) {
            return;
        }

        long key = ChunkPos.toLong(pos);
        Set<BlockPos> inChunk = chunks.get(key);
        if (inChunk != null && inChunk.remove(pos) && inChunk.isEmpty()) {
            chunks.remove(key);
        }
    }

    /** @return the nearest registered position within {@code radius}, or null. */
    public BlockPos findNearest(World world, BlockPos target, int radius) {
        Map<Long, Set<BlockPos>> chunks = byDimension.get(world.getRegistryKey());
        if (chunks == null || chunks.isEmpty()) {
            return null;
        }

        int chunkRadius = (radius >> 4) + 1;
        int centerX = target.getX() >> 4;
        int centerZ = target.getZ() >> 4;

        BlockPos nearest = null;
        double nearestDistance = Double.MAX_VALUE;

        for (int x = centerX - chunkRadius; x <= centerX + chunkRadius; x++) {
            for (int z = centerZ - chunkRadius; z <= centerZ + chunkRadius; z++) {
                Set<BlockPos> inChunk = chunks.get(ChunkPos.toLong(x, z));
                if (inChunk == null) {
                    continue;
                }

                for (BlockPos pos : inChunk) {
                    double distance = pos.getSquaredDistance(target);
                    if (distance < nearestDistance && pos.isWithinDistance(target, radius)) {
                        nearest = pos;
                        nearestDistance = distance;
                    }
                }
            }
        }
        return nearest;
    }

    public boolean isWithin(World world, BlockPos target, int radius) {
        return findNearest(world, target, radius) != null;
    }
}
