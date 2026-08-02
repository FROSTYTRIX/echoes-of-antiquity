package net.frostytrix.echoesofantiquity.effect;

import net.frostytrix.echoesofantiquity.util.VoidPedestalManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class PhasingEffect extends StatusEffect {
    public PhasingEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyUpdateEffect(LivingEntity entity, int amplifier) {
        World world = entity.getWorld();

        // Server-side only.
        if (!(world instanceof ServerWorld serverWorld)) {
            return super.applyUpdateEffect(entity, amplifier);
        }

        Direction dir = entity.getHorizontalFacing();
        Box collisionCheckArea = entity.getBoundingBox().stretch(
                dir.getOffsetX() * 0.2,
                0,
                dir.getOffsetZ() * 0.2
        );
        boolean isTouchingWall = world.getBlockCollisions(entity, collisionCheckArea).iterator().hasNext();

        // Only true when actually blocked while moving. lengthSquared() > 0 also passed
        // while standing still, since gravity always leaves a vertical component.
        boolean pushingIntoWall = entity.horizontalCollision;

        if (pushingIntoWall && isTouchingWall) {
            BlockPos pedestalPos = VoidPedestalManager.findNearestActivePedestal(world, entity.getBlockPos());

            if (pedestalPos != null) {
                // Active pedestal in range: phasing is cancelled.
                serverWorld.spawnParticles(ParticleTypes.PORTAL,
                        pedestalPos.getX() + 0.5, pedestalPos.getY() + 1.2, pedestalPos.getZ() + 0.5,
                        20, 0.2, 0.2, 0.2, 0.1);
            } else {
                BlockPos targetPos = entity.getBlockPos().offset(dir, 2);

                if (world.getBlockState(targetPos).isAir() && world.getBlockState(targetPos.up()).isAir()) {
                    serverWorld.spawnParticles(ParticleTypes.PORTAL, entity.getX(), entity.getY() + 1, entity.getZ(), 20, 0.5, 0.5, 0.5, 0.1);
                    entity.teleport(targetPos.getX() + 0.5, targetPos.getY(), targetPos.getZ() + 0.5, ParticleTypes.PORTAL.shouldAlwaysSpawn());
                    world.playSound(null, targetPos, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0f, 1.0f);
                    serverWorld.spawnParticles(ParticleTypes.PORTAL, entity.getX(), entity.getY() + 1, entity.getZ(), 20, 0.5, 0.5, 0.5, 0.1);
                }
            }
        }
        return super.applyUpdateEffect(entity, amplifier);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}
