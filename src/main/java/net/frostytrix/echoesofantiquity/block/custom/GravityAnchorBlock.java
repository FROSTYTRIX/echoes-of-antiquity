package net.frostytrix.echoesofantiquity.block.custom;

import com.mojang.serialization.MapCodec;
import net.frostytrix.echoesofantiquity.block.entity.ModBlockEntities;
import net.frostytrix.echoesofantiquity.block.entity.custom.GravityAnchorBlockEntity;
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
    public static final int RANGE = 10;

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
        // On ne fait ça que côté serveur
        if (!world.isClient) {
            // On demande au monde si notre bloc reçoit de l'énergie (levier, redstone, bloc de redstone, etc.)
            boolean isPowered = world.isReceivingRedstonePower(pos);

            // Si l'état actuel de notre bloc (allumé/éteint) est différent de l'énergie qu'il reçoit
            if (state.get(ACTIVE) != isPowered) {
                // On met à jour le bloc avec le nouvel état
                // Le "Block.NOTIFY_ALL" (qui vaut 3) est crucial : il dit au jeu d'informer les clients et de mettre à jour les voisins
                world.setBlockState(pos, state.with(ACTIVE, isPowered), Block.NOTIFY_ALL);
            }
        }
        super.neighborUpdate(state, world, pos, sourceBlock, sourcePos, notify);
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        // On l'ajoute uniquement s'il vient d'être posé ET qu'il est actif
        if (!world.isClient && !oldState.isOf(state.getBlock()) && state.get(ACTIVE)) {
            GravityAnchorManager.addAnchor(world, pos);
        }
        super.onBlockAdded(state, world, pos, oldState, notify);
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!world.isClient) {
            boolean wasActive = state.get(ACTIVE);
            // On vérifie si le bloc a été cassé OU si sa propriété ACTIVE a changé
            if (!state.isOf(newState.getBlock()) || state.get(ACTIVE) != newState.get(ACTIVE)) {

                if (wasActive) {
                    // Si l'ancre était active et s'éteint ou est cassée
                    GravityAnchorManager.removeAnchor(world, pos);
                    forceUpdateFallingBlocks(world, pos); // On réveille le sable !
                } else if (newState.isOf(this) && newState.get(ACTIVE)) {
                    // Si le bloc n'a pas été cassé, mais qu'il vient de s'allumer
                    GravityAnchorManager.addAnchor(world, pos);
                }
            }
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    private void forceUpdateFallingBlocks(World world, BlockPos center) {
        // On scanne un cube de 21x21x21 autour de l'ancre
        BlockPos.iterate(center.add(-RANGE, -RANGE, -RANGE), center.add(RANGE, RANGE, RANGE)).forEach(pos -> {
            BlockState state = world.getBlockState(pos);

            // Si c'est un bloc soumis à la gravité
            if (state.getBlock() instanceof FallingBlock) {
                // On programme un "tick" pour ce bloc.
                // pos.toImmutable() est vital ici, car BlockPos.iterate recycle le même objet BlockPos !
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
            // 1. Core Particles: Floating around the block itself
            for (int i = 0; i < 2; i++) {
                double x = pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 1.2;
                double y = pos.getY() + 0.5 + (random.nextDouble() - 0.5) * 1.2;
                double z = pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 1.2;

                // Using ELECTRIC_SPARK or GLOW for that "Architect Tech" feel
                world.addParticle(ParticleTypes.ELECTRIC_SPARK, x, y, z, 0, 0.02, 0);
            }

            // 2. Range Indicators: Subtly showing the 10-block radius
            // We only show these occasionally so it doesn't lag the game
            if (random.nextInt(5) == 0) {
                // Pick a random spot in the 10-block radius
                double rx = pos.getX() + (random.nextDouble() - 0.5) * 20;
                double rz = pos.getZ() + (random.nextDouble() - 0.5) * 20;
                double ry = pos.getY() + (random.nextDouble() * 5); // Just above ground level

                world.addParticle(ParticleTypes.WAX_OFF, rx, ry, rz, 0, 0.1, 0);
            }
        }
    }
}
