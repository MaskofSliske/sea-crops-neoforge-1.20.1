package com.example.maskseacrops;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

public abstract class NemoVineBlock extends BushBlock {

    public static final IntegerProperty AGE = BlockStateProperties.AGE_7;
    public static final int MAX_AGE = 7;

    public NemoVineBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    //Returns the fruit block that it is meant to place, this will be overridden by the subclass
    protected abstract Block getFruitBlock();

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState below = level.getBlockState(pos.below());
        FluidState fluid = level.getFluidState(pos);
        FluidState above = level.getFluidState(pos.above());

        boolean isOnValidBlock = below.is(MaskSeaCrops.SILT.get())
                || below.is(net.minecraft.world.level.block.Blocks.SAND)
                || below.is(net.minecraft.world.level.block.Blocks.GRAVEL)
                || below.is(net.minecraft.world.level.block.Blocks.DIRT);

        boolean isUnderwater = fluid.isSourceOfType(Fluids.WATER) || above.isSourceOfType(Fluids.WATER);

        return isOnValidBlock && isUnderwater;
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(MaskSeaCrops.SILT.get())
                || state.is(net.minecraft.world.level.block.Blocks.SAND)
                || state.is(net.minecraft.world.level.block.Blocks.GRAVEL)
                || state.is(net.minecraft.world.level.block.Blocks.DIRT);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int age = state.getValue(AGE);
        BlockPos above = pos.above();
        BlockState aboveState = level.getBlockState(above);

        if (age < MAX_AGE) {
            if (random.nextInt(5) == 0) {
                level.setBlock(pos, state.setValue(AGE, age + 1), 2);
            }
        } else {
            if (aboveState.getBlock() == getFruitBlock()) {
                return;
            }

            FluidState aboveFluid = level.getFluidState(above);
            if (aboveFluid.isSourceOfType(Fluids.WATER) || aboveState.isAir()) {
                level.setBlock(above, getFruitBlock().defaultBlockState(), 2);
                level.setBlock(pos, state.setValue(AGE, MAX_AGE), 2);
            }
        }
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return Fluids.WATER.getSource(false);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        //This is just so it does nothing that way it prevents water physics from destroying the block
    }
}