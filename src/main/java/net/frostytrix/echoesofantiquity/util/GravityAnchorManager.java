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
    // Stocke les positions des ancres actives par dimension (Overworld, Nether, etc.)
    private static final Map<RegistryKey<World>, Set<BlockPos>> ANCHORS = new HashMap<>();

    // Appelé quand le bloc est posé ou allumé
    public static void addAnchor(World world, BlockPos pos) {
        ANCHORS.computeIfAbsent(world.getRegistryKey(), k -> new HashSet<>()).add(pos);
    }

    // Appelé quand le bloc est cassé ou éteint
    public static void removeAnchor(World world, BlockPos pos) {
        Set<BlockPos> set = ANCHORS.get(world.getRegistryKey());
        if (set != null) {
            set.remove(pos);
        }
    }

    // Vérifie si un bloc est dans le rayon d'une ancre
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