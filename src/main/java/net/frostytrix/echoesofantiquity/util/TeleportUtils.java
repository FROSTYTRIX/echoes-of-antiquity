package net.frostytrix.echoesofantiquity.util;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import java.util.Optional;

public class TeleportUtils {

    /**
     * Scans the 8 blocks around the center position to find a safe 2-block high space.
     */
    public static Optional<Vec3d> findSafeTeleportSpot(World world, BlockPos centerPos) {
        // Look at the 8 adjacent blocks around the Waystone on the X/Z plane
        BlockPos[] searchOffsets = new BlockPos[]{
                centerPos.north(), centerPos.south(), centerPos.east(), centerPos.west(),
                centerPos.north().east(), centerPos.north().west(),
                centerPos.south().east(), centerPos.south().west()
        };

        for (BlockPos pos : searchOffsets) {
            // Check the current level, one block down, and one block up
            // This handles slightly uneven terrain around the Waystone
            for (int yOffset = -1; yOffset <= 1; yOffset++) {
                BlockPos checkPos = pos.up(yOffset);

                if (isSafeToStandAt(world, checkPos)) {
                    // Return the exact center of the block for teleporting (adding 0.5 to X and Z)
                    return Optional.of(new Vec3d(checkPos.getX() + 0.5, checkPos.getY(), checkPos.getZ() + 0.5));
                }
            }
        }

        return Optional.empty(); // No safe spot found anywhere around it!
    }

    /**
     * A safe spot requires a solid block underneath, and no collision boxes at foot and head level.
     */
    private static boolean isSafeToStandAt(World world, BlockPos pos) {
        BlockState floor = world.getBlockState(pos.down());
        BlockState footLevel = world.getBlockState(pos);
        BlockState headLevel = world.getBlockState(pos.up());

        // 1. Is the floor solid enough to stand on? (Not air, water, or lava)
        boolean isFloorSolid = floor.isSideSolidFullSquare(world, pos.down(), Direction.UP);

        // 2. Are the foot and head spaces passable? (Collision shape is empty = air, tall grass, signs, etc.)
        boolean isFootPassable = footLevel.getCollisionShape(world, pos).isEmpty();
        boolean isHeadPassable = headLevel.getCollisionShape(world, pos.up()).isEmpty();

        // 3. Prevent teleporting directly into hazardous blocks (like fire or lava)
        boolean isNotHazard = !footLevel.blocksMovement() && footLevel.getFluidState().isEmpty();

        return isFloorSolid && isFootPassable && isHeadPassable && isNotHazard;
    }
}