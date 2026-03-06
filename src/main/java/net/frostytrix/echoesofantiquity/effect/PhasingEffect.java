package net.frostytrix.echoesofantiquity.effect;

import net.frostytrix.echoesofantiquity.block.ModBlocks;
import net.frostytrix.echoesofantiquity.block.custom.VoidPedestalBlock;
import net.frostytrix.echoesofantiquity.block.entity.custom.VoidAnchorBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
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
        Direction dir = entity.getHorizontalFacing();
        // 1. Manually check for a wall by expanding the player's collision check area slightly where they are looking
        Box collisionCheckArea = entity.getBoundingBox().stretch(
                dir.getOffsetX() * 0.2,
                0,
                dir.getOffsetZ() * 0.2
        );
        // 2. See if any solid blocks intersect this tiny area
        boolean isTouchingWall = world.getBlockCollisions(entity, collisionCheckArea).iterator().hasNext();

        double Velocity = entity.getVelocity().lengthSquared();

        if (Velocity > 0 && isTouchingWall) {
            if (entity.getCommandTags().contains("void_anchor_suppressed")) {
                BlockPos pedestalPos = this.findNearestActivePedestal(entity);
                if (pedestalPos != null) {
                    ServerWorld serverWorld = (ServerWorld) entity.getWorld();
                    serverWorld.spawnParticles(ParticleTypes.PORTAL,
                            pedestalPos.getX() + 0.5, pedestalPos.getY() + 1.2, pedestalPos.getZ() + 0.5,
                            20, 0.2, 0.2, 0.2, 0.1);
                }
            } else {
                BlockPos targetPos = entity.getBlockPos().offset(dir, 2);

                // Check for safe landing (Air at feet and head)
                if (world.getBlockState(targetPos).isAir() && world.getBlockState(targetPos.up()).isAir() && !entity.getWorld().isClient) {
                    ServerWorld worldServer = (ServerWorld) world;
                    // Visuals
                    worldServer.spawnParticles(ParticleTypes.PORTAL, entity.getX(), entity.getY() + 1, entity.getZ(), 20, 0.5, 0.5, 0.5, 0.1);
                    // Teleport
                    entity.teleport(targetPos.getX() + 0.5, targetPos.getY(), targetPos.getZ() + 0.5, ParticleTypes.PORTAL.shouldAlwaysSpawn());
                    // Sound
                    world.playSound(null, targetPos, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0f, 1.0f);
                    // Visuals n.2
                    worldServer.spawnParticles(ParticleTypes.PORTAL, entity.getX(), entity.getY() + 1, entity.getZ(), 20, 0.5, 0.5, 0.5, 0.1);
                }
            }
        }
        entity.removeCommandTag("void_anchor_suppressed");
        return super.applyUpdateEffect(entity, amplifier);
    }

    private BlockPos findNearestActivePedestal(Entity entity) {
        BlockPos pos = entity.getBlockPos();
        int r = VoidAnchorBlockEntity.noTPRadius;
        for (BlockPos target : BlockPos.iterate(pos.add(-r, -r, -r), pos.add(r, r, r))) {
            BlockState state = entity.getWorld().getBlockState(target);
            if (state.isOf(ModBlocks.VOID_PEDESTAL) && state.get(VoidPedestalBlock.ACTIVE)) {
                return target.toImmutable();
            }
        }
        return null;

    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}
