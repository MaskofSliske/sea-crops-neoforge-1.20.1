package com.example.maskseacrops;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.KelpBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

public class SugarKelpBlock extends KelpBlock {

    public SugarKelpBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected boolean canGrowInto(BlockState state) {
        return state.isAir() || state.getBlock() == MaskSeaCrops.SUGAR_KELP.get();
    }

    @Override
    protected Block getBodyBlock() {
        return MaskSeaCrops.SUGAR_KELP_PLANT.get();
    }

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos pos, BlockPos facingPos) {
        level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        return super.updateShape(state, facing, facingState, level, pos, facingPos);
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        BlockPos above = pos.above();
        level.setBlock(pos, MaskSeaCrops.SUGAR_KELP_PLANT.get().defaultBlockState(), 2);
        level.setBlock(above, MaskSeaCrops.SUGAR_KELP.get().defaultBlockState(), 2);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        BlockPos above = pos.above();
        FluidState aboveFluid = level.getFluidState(above);
        if (aboveFluid.is(Fluids.WATER) || aboveFluid.is(Fluids.FLOWING_WATER)) {
            super.randomTick(state, level, pos, random);
        }
    }
}