package net.frostytrix.echoesofantiquity.util;

import net.frostytrix.echoesofantiquity.block.entity.custom.VoidPedestalBlockEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/** Tracks active Void Pedestals (holding an Ender Eye) per dimension. Each one registers from its own tick. */
public class VoidPedestalManager {
    private static final Map<RegistryKey<World>, Set<BlockPos>> PEDESTALS = new HashMap<>();

    public static void addPedestal(World world, BlockPos pos) {
        PEDESTALS.computeIfAbsent(world.getRegistryKey(), k -> new HashSet<>()).add(pos.toImmutable());
    }

    public static void removePedestal(World world, BlockPos pos) {
        Set<BlockPos> set = PEDESTALS.get(world.getRegistryKey());
        if (set != null) {
            set.remove(pos);
        }
    }

    /** @return the nearest active pedestal in range, or null. */
    public static BlockPos findNearestActivePedestal(World world, BlockPos targetPos) {
        Set<BlockPos> set = PEDESTALS.get(world.getRegistryKey());
        if (set == null || set.isEmpty()) return null;

        BlockPos nearest = null;
        double nearestDistance = Double.MAX_VALUE;

        for (BlockPos pedestalPos : set) {
            double distance = pedestalPos.getSquaredDistance(targetPos);
            if (distance < nearestDistance && pedestalPos.isWithinDistance(targetPos, VoidPedestalBlockEntity.noTPRadius)) {
                nearest = pedestalPos;
                nearestDistance = distance;
            }
        }
        return nearest;
    }

    public static boolean isSuppressed(World world, BlockPos targetPos) {
        return findNearestActivePedestal(world, targetPos) != null;
    }
}
