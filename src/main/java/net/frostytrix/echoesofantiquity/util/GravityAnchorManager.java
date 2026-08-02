package net.frostytrix.echoesofantiquity.util;

import net.frostytrix.echoesofantiquity.block.custom.GravityAnchorBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GravityAnchorManager {
    private static final BlockPosIndex ANCHORS = new BlockPosIndex();

    public static void addAnchor(World world, BlockPos pos) {
        ANCHORS.add(world, pos);
    }

    public static void removeAnchor(World world, BlockPos pos) {
        ANCHORS.remove(world, pos);
    }

    public static boolean isProtected(World world, BlockPos targetPos) {
        return ANCHORS.isWithin(world, targetPos, GravityAnchorBlock.range());
    }
}
