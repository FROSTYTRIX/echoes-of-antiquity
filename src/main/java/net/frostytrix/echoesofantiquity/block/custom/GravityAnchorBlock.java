package net.frostytrix.echoesofantiquity.block.custom;

import com.mojang.serialization.MapCodec;
import net.frostytrix.echoesofantiquity.block.entity.ModBlockEntities;
import net.frostytrix.echoesofantiquity.block.entity.custom.GravityAnchorBlockEntity;
import net.frostytrix.echoesofantiquity.config.ModConfig;
import net.frostytrix.echoesofantiquity.util.GravityAnchorManager;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class GravityAnchorBlock extends BlockWithEntity{
    public static final MapCodec<GravityAnchorBlock> CODEC = GravityAnchorBlock.createCodec(GravityAnchorBlock::new);
    public static final BooleanProperty ACTIVE = BooleanProperty.of("active");
    public static int range() {
        return ModConfig.get().gravityAnchorRadius;
    }

    public GravityAnchorBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultState().with(ACTIVE, false));
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(ACTIVE);
    }

    @Override
    protected void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (!world.isClient) {
            boolean isPowered = world.isReceivingRedstonePower(pos);

            if (state.get(ACTIVE) != isPowered) {
                world.setBlockState(pos, state.with(ACTIVE, isPowered), Block.NOTIFY_ALL);
            }
        }
        super.neighborUpdate(state, world, pos, sourceBlock, sourcePos, notify);
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if (!world.isClient && !oldState.isOf(state.getBlock()) && state.get(ACTIVE)) {
            GravityAnchorManager.addAnchor(world, pos);
        }
        super.onBlockAdded(state, world, pos, oldState, notify);
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!world.isClient) {
            boolean wasActive = state.get(ACTIVE);
            if (!state.isOf(newState.getBlock()) || state.get(ACTIVE) != newState.get(ACTIVE)) {

                if (wasActive) {
                    GravityAnchorManager.removeAnchor(world, pos);
                    forceUpdateFallingBlocks(world, pos); // On réveille le sable !
                } else if (newState.isOf(this) && newState.get(ACTIVE)) {
                    GravityAnchorManager.addAnchor(world, pos);
                }
            }
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    private void forceUpdateFallingBlocks(World world, BlockPos center) {
        int range = range();
        BlockPos.iterate(center.add(-range, -range, -range), center.add(range, range, range)).forEach(pos -> {
            BlockState state = world.getBlockState(pos);

            if (state.getBlock() instanceof FallingBlock) {
                // BlockPos.iterate reuses one mutable instance.
                world.scheduleBlockTick(pos.toImmutable(), state.getBlock(), 2);
            }
        });
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new GravityAnchorBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, ModBlockEntities.GRAVITY_ANCHOR_BE,
                (world1, pos, state1, blockEntity) -> blockEntity.tick(world1, pos, state1));
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (state.get(ACTIVE)) {
            for (int i = 0; i < 2; i++) {
                double x = pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 1.2;
                double y = pos.getY() + 0.5 + (random.nextDouble() - 0.5) * 1.2;
                double z = pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 1.2;

                world.addParticle(ParticleTypes.ELECTRIC_SPARK, x, y, z, 0, 0.02, 0);
            }

            // Throttled to limit particle spam.
            if (random.nextInt(5) == 0) {
                double rx = pos.getX() + (random.nextDouble() - 0.5) * 20;
                double rz = pos.getZ() + (random.nextDouble() - 0.5) * 20;
                double ry = pos.getY() + (random.nextDouble() * 5); // Just above ground level

                world.addParticle(ParticleTypes.WAX_OFF, rx, ry, rz, 0, 0.1, 0);
            }
        }
    }
}
