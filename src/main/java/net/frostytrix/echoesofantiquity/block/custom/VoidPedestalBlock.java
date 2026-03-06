package net.frostytrix.echoesofantiquity.block.custom;

import com.mojang.serialization.MapCodec;
import net.frostytrix.echoesofantiquity.block.entity.ModBlockEntities;
import net.frostytrix.echoesofantiquity.block.entity.custom.VoidAnchorBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class VoidPedestalBlock extends BlockWithEntity implements BlockEntityProvider {
    public static final BooleanProperty ACTIVE = BooleanProperty.of("active");

    private static  final VoxelShape SHAPE =
            VoxelShapes.union(
                    VoxelShapes.cuboid(0.0625, 0, 0.0625, 0.9375, 0.125, 0.9375),
                    VoxelShapes.cuboid(0.1875, 0.125, 0.1875, 0.8125, 0.8125, 0.8125),
                    VoxelShapes.cuboid(0.0625, 0.8125, 0.0625, 0.9375, 1.015625, 0.9375),
                    VoxelShapes.cuboid(0.0625, 1.015625, 0.0625, 0.25, 1.0625, 0.9375),
                    VoxelShapes.cuboid(0.25, 0.9187500000000001, 0.22031250000000005, 0.75, 1.0437499999999997, 0.3453124999999998),
                    VoxelShapes.cuboid(0.75, 1.015625, 0.0625, 0.9375, 1.0625, 0.9375),
                    VoxelShapes.cuboid(0.25, 1.015625, 0.0625, 0.75, 1.0625, 0.25),
                    VoxelShapes.cuboid(0.25, 1.015625, 0.75, 0.75, 1.0625, 0.9375),
                    VoxelShapes.cuboid(0.2203125000000001, 0.9187500000000001, 0.2203124999999998, 0.34531250000000036, 1.0437499999999997, 0.7828124999999998),
                    VoxelShapes.cuboid(0.6562500000000003, 0.9187500000000001, 0.2203124999999998, 0.7796875000000005, 1.0437499999999997, 0.7828124999999998),
                    VoxelShapes.cuboid(0.25, 0.9187500000000001, 0.653125, 0.75, 1.0437499999999997, 0.7781249999999997)
            );

    public static final MapCodec<VoidPedestalBlock> CODEC = VoidPedestalBlock.createCodec(VoidPedestalBlock::new);

    public VoidPedestalBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(ACTIVE, false));;
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new VoidAnchorBlockEntity(pos, state);
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof VoidAnchorBlockEntity){
                ItemScatterer.spawn(world, pos, ((VoidAnchorBlockEntity) blockEntity));
                world.updateComparators(pos, this);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(world.getBlockEntity(pos) instanceof VoidAnchorBlockEntity voidAnchorBlockEntity) {
            if(voidAnchorBlockEntity.isEmpty() && !stack.isEmpty()) {
                voidAnchorBlockEntity.setStack(0, stack.copyWithCount(1));
                world.playSound(player, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1f, 2f);
                stack.decrement(1);

                voidAnchorBlockEntity.markDirty();
                world.updateListeners(pos, state, state, 0);
            } else if(stack.isEmpty() && !player.isSneaking() && !voidAnchorBlockEntity.isEmpty()) {
                ItemStack stackOnPedestal = voidAnchorBlockEntity.getStack(0);
                player.setStackInHand(Hand.MAIN_HAND, stackOnPedestal);
                world.playSound(player, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1f, 1f);
                voidAnchorBlockEntity.clear();

                voidAnchorBlockEntity.markDirty();
                world.updateListeners(pos, state, state, 0);
            }
        }

        return ItemActionResult.SUCCESS;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, ModBlockEntities.VOID_ANCHOR_BE, VoidAnchorBlockEntity::tick);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        // This "registers" the property to this specific block
        builder.add(ACTIVE);
    }
}
