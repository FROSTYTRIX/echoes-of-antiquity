package net.frostytrix.echoesofantiquity.block.custom;

import com.mojang.serialization.MapCodec;
import net.frostytrix.echoesofantiquity.block.entity.ModBlockEntities;
import net.frostytrix.echoesofantiquity.block.entity.custom.WaystoneBlockEntity;
import net.frostytrix.echoesofantiquity.item.ModItems;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class WaystoneBlock extends BlockWithEntity implements BlockEntityProvider {
    public static final BooleanProperty ACTIVE = BooleanProperty.of("active");
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
    public static final MapCodec<WaystoneBlock> CODEC = WaystoneBlock.createCodec(WaystoneBlock::new);

    private static final int COST = 10;

    private static final VoxelShape SHAPE_N_S = Block.createCuboidShape(1.0, 0.0, 5.0, 15.0, 18.0, 11.0);
    private static final VoxelShape SHAPE_E_W = Block.createCuboidShape(5.0, 0.0, 1.0, 11.0, 18.0, 15.0);

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction dir = state.get(FACING);

        return switch (dir) {
            case EAST, WEST -> SHAPE_E_W;
            case NORTH, SOUTH -> SHAPE_N_S;
            default -> SHAPE_N_S;
        };
    }

    public WaystoneBlock(Settings settings) {
        super(settings.emissiveLighting((state, world, pos) -> {
            // This checks the 'ACTIVE' property you added to the block
            return state.get(ACTIVE);
        }));
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(ACTIVE, false));
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        // This makes the block face the player when placed
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, ACTIVE);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (player.isCreative()) {
            if (state.get(ACTIVE)) {
                world.setBlockState(pos, state.with(ACTIVE, false));
            } else {
                world.setBlockState(pos, state.with(ACTIVE, true));
            }
        }else {
            if (!state.get(ACTIVE) && player.getMainHandStack().getCount() >= COST && player.getMainHandStack().isOf(ModItems.SOUL_FRAGMENT)) {
                world.setBlockState(pos, state.with(ACTIVE, true));
                player.getMainHandStack().decrement(COST);
            }
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);

        if (!world.isClient && placer instanceof PlayerEntity player) {

            if (world.getBlockEntity(pos) instanceof WaystoneBlockEntity waystoneBE) {

                waystoneBE.setOwner(player.getUuid());
            }
        }
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        if (world.isClient) {
            return null;
        }
        return validateTicker(type, ModBlockEntities.WAYSTONE_BE,
                ((world1, pos, state1, blockEntity) -> blockEntity.tick(world1, pos, state1)));
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new WaystoneBlockEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
}
