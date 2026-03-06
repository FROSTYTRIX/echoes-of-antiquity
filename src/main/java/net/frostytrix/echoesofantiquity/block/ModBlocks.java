package net.frostytrix.echoesofantiquity.block;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.frostytrix.echoesofantiquity.EchoesOfAntiquity;
import net.frostytrix.echoesofantiquity.block.custom.VoidPedestalBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ModBlocks {

    public static final Block PLACEHOLDER = registerBlock("placeholder",
            new Block(AbstractBlock.Settings.create().strength(1f)));

    public static final Block VOID_PEDESTAL = registerBlock("void_pedestal",
            new VoidPedestalBlock(AbstractBlock.Settings.create().nonOpaque().strength(50.0F, 1200.0F).requiresTool()));

    public static final Block VOID_ANCHOR = registerBlock("void_anchor",
            new Block(AbstractBlock.Settings.create().requiresTool().strength(3.0F, 9.0F)){
                public static final BooleanProperty PURPLE = BooleanProperty.of("purple");

                {
                    this.setDefaultState(this.stateManager.getDefaultState().with(PURPLE, false));
                }

                @Override
                protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
                    builder.add(PURPLE);
                }

                @Override
                protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {

                    if (stack.isOf(Items.MAGENTA_DYE) && !state.get(PURPLE)) {
                        if (!world.isClient) {
                            world.setBlockState(pos, state.with(PURPLE, true));
                            world.playSound(null, pos, SoundEvents.ITEM_DYE_USE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                            if (!player.getAbilities().creativeMode) {
                                stack.decrement(1);
                            }
                        }
                        return ItemActionResult.SUCCESS;
                    }


                    if (stack.isOf(Items.WATER_BUCKET) && state.get(PURPLE)) {
                        if (!world.isClient) {
                            world.setBlockState(pos, state.with(PURPLE, false));
                            world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);

                            if (!player.getAbilities().creativeMode) {
                                player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Items.BUCKET)));
                            }
                        }
                        return ItemActionResult.SUCCESS;
                    }

                    return ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
                }
            });



    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(EchoesOfAntiquity.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, Identifier.of(EchoesOfAntiquity.MOD_ID, name),
                new BlockItem(block,new Item.Settings()));
    }

    public static void registerModBlocks() {
        EchoesOfAntiquity.LOGGER.info("Registering Mod Blocks for " + EchoesOfAntiquity.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(fabricItemGroupEntries -> {
        });
    }
}
