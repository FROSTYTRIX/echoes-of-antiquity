package net.frostytrix.echoesofantiquity.util;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Optional;

public class TeleportUtils {

     /** Finds a safe 2-block-high spot among the 8 neighbours. */
    public static Optional<Vec3d> findSafeTeleportSpot(World world, BlockPos centerPos) {
        BlockPos[] searchOffsets = new BlockPos[]{
                centerPos.north(), centerPos.south(), centerPos.east(), centerPos.west(),
                centerPos.north().east(), centerPos.north().west(),
                centerPos.south().east(), centerPos.south().west()
        };

        for (BlockPos pos : searchOffsets) {
            for (int yOffset = -1; yOffset <= 1; yOffset++) {
                BlockPos checkPos = pos.up(yOffset);

                if (isSafeToStandAt(world, checkPos)) {
                    return Optional.of(new Vec3d(checkPos.getX() + 0.5, checkPos.getY(), checkPos.getZ() + 0.5));
                }
            }
        }

        return Optional.empty(); // No safe spot found anywhere around it!
    }

     /** Solid floor, nothing colliding at foot and head level. */
    private static boolean isSafeToStandAt(World world, BlockPos pos) {
        BlockState floor = world.getBlockState(pos.down());
        BlockState footLevel = world.getBlockState(pos);
        BlockState headLevel = world.getBlockState(pos.up());

        boolean isFloorSolid = floor.isSideSolidFullSquare(world, pos.down(), Direction.UP);

        boolean isFootPassable = footLevel.getCollisionShape(world, pos).isEmpty();
        boolean isHeadPassable = headLevel.getCollisionShape(world, pos.up()).isEmpty();

        boolean isNotHazard = !footLevel.blocksMovement() && footLevel.getFluidState().isEmpty();

        return isFloorSolid && isFootPassable && isHeadPassable && isNotHazard;
    }
}