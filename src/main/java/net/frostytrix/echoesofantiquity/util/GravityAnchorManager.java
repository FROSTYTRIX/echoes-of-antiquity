package net.frostytrix.echoesofantiquity.util;

import net.frostytrix.echoesofantiquity.block.custom.GravityAnchorBlock;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GravityAnchorManager {
    // Active anchors per dimension.
    private static final Map<RegistryKey<World>, Set<BlockPos>> ANCHORS = new HashMap<>();

    public static void addAnchor(World world, BlockPos pos) {
        ANCHORS.computeIfAbsent(world.getRegistryKey(), k -> new HashSet<>()).add(pos.toImmutable());
    }

    public static void removeAnchor(World world, BlockPos pos) {
        Set<BlockPos> set = ANCHORS.get(world.getRegistryKey());
        if (set != null) {
            set.remove(pos);
        }
    }

    public static boolean isProtected(World world, BlockPos targetPos) {
        Set<BlockPos> set = ANCHORS.get(world.getRegistryKey());
        if (set == null || set.isEmpty()) return false;

        for (BlockPos anchorPos : set) {
            if (anchorPos.isWithinDistance(targetPos, GravityAnchorBlock.RANGE)) {
                return true;
            }
        }
        return false;
    }
}