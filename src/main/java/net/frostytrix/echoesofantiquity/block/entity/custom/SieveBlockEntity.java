package net.frostytrix.echoesofantiquity.block.entity.custom;

import net.frostytrix.echoesofantiquity.block.entity.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class SieveBlockEntity extends BlockEntity {
    public SieveBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SIEVE_BE, pos, state);
    }
}
