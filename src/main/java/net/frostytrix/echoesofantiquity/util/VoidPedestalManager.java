package net.frostytrix.echoesofantiquity.util;

import net.frostytrix.echoesofantiquity.block.entity.custom.VoidPedestalBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/** Tracks active Void Pedestals (holding an Ender Eye). Each one registers from its own tick. */
public class VoidPedestalManager {
    private static final BlockPosIndex PEDESTALS = new BlockPosIndex();

    public static void addPedestal(World world, BlockPos pos) {
        PEDESTALS.add(world, pos);
    }

    public static void removePedestal(World world, BlockPos pos) {
        PEDESTALS.remove(world, pos);
    }

    /** @return the nearest active pedestal in range, or null. */
    public static BlockPos findNearestActivePedestal(World world, BlockPos targetPos) {
        return PEDESTALS.findNearest(world, targetPos, VoidPedestalBlockEntity.noTPRadius);
    }

    public static boolean isSuppressed(World world, BlockPos targetPos) {
        return findNearestActivePedestal(world, targetPos) != null;
    }
}
