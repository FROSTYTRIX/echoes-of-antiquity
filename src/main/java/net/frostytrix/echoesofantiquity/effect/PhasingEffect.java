package net.frostytrix.echoesofantiquity.effect;

import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class PhasingEffect extends StatusEffect {
    public PhasingEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyUpdateEffect(LivingEntity entity, int amplifier) {
        World world = entity.getWorld();
        Direction dir = entity.getHorizontalFacing();
        // 1. Manually check for a wall by expanding the player's "reach" slightly
        // This checks 0.2 blocks in the direction they are facing
        Box collisionCheckArea = entity.getBoundingBox().stretch(
                dir.getOffsetX() * 0.2,
                0,
                dir.getOffsetZ() * 0.2
        );
        // 2. See if any solid blocks intersect this tiny area
        boolean isTouchingWall = world.getBlockCollisions(entity, collisionCheckArea).iterator().hasNext();

        double Velocity = entity.getVelocity().lengthSquared();

        if (Velocity > 0 && isTouchingWall) {

            BlockPos targetPos = entity.getBlockPos().offset(dir, 2);

            // 3. Check for safe landing (Air at feet and head)
            if (world.getBlockState(targetPos).isAir() && world.getBlockState(targetPos.up()).isAir() && !entity.getWorld().isClient) {
                ServerWorld worldServer = (ServerWorld) world;
                // Visuals
                worldServer.spawnParticles(ParticleTypes.PORTAL, entity.getX(), entity.getY() + 1, entity.getZ(), 20, 0.5, 0.5, 0.5, 0.1);
                // Teleport
                entity.teleport(targetPos.getX() + 0.5, targetPos.getY(), targetPos.getZ() + 0.5, ParticleTypes.PORTAL.shouldAlwaysSpawn());
                // Sound
                world.playSound(null, targetPos, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0f, 1.0f);
                worldServer.spawnParticles(ParticleTypes.PORTAL, entity.getX(), entity.getY() + 1, entity.getZ(), 20, 0.5, 0.5, 0.5, 0.1);
            }
        }
        return super.applyUpdateEffect(entity, amplifier);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}
