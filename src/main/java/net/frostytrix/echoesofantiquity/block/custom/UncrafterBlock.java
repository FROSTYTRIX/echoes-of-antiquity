package net.frostytrix.echoesofantiquity.block.custom;

import com.mojang.serialization.MapCodec;
import net.frostytrix.echoesofantiquity.block.entity.ModBlockEntities;
import net.frostytrix.echoesofantiquity.block.entity.custom.UncrafterBlockEntity;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class UncrafterBlock extends BlockWithEntity implements BlockEntityProvider {
    public static final MapCodec<UncrafterBlock> CODEC = UncrafterBlock.createCodec(UncrafterBlock::new);


    public UncrafterBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new UncrafterBlockEntity(pos, state);

    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof UncrafterBlockEntity){
                ItemScatterer.spawn(world, pos, ((UncrafterBlockEntity) blockEntity));
                world.updateComparators(pos, this);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!world.isClient && world.getBlockEntity(pos) instanceof UncrafterBlockEntity uncrafterBlockEntity) {
            player.openHandledScreen(uncrafterBlockEntity);
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        if (world.isClient) {
            return null;
        }
        return validateTicker(type, ModBlockEntities.UNCRAFTER_BE,
                ((world1, pos, state1, blockEntity) -> blockEntity.tick(world1, pos, state1)));
    }
}
