package com.example.maskseacrops;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;

import java.util.Random;

public class SeaWheatBlock extends BushBlock {

    public static final IntegerProperty AGE = BlockStateProperties.AGE_7;
    public static final int MAX_AGE = 7;

    public SeaWheatBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<net.minecraft.world.level.block.Block, BlockState> builder) {
        builder.add(AGE);
    }

    // Only survives on silt underwater
    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState below = level.getBlockState(pos.below());
        FluidState atPos = level.getFluidState(pos);
        FluidState above = level.getFluidState(pos.above());

        boolean isOnSilt = below.is(MaskSeaCrops.SILT.get());
        boolean isUnderwater = atPos.isSourceOfType(Fluids.WATER) || above.isSourceOfType(Fluids.WATER);

        return isOnSilt && isUnderwater;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return Fluids.WATER.getSource(false);
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(MaskSeaCrops.SILT.get());
    }

    // Random growth tick
    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int age = state.getValue(AGE);
        if (age < MAX_AGE && random.nextInt(5) == 0) {
            level.setBlock(pos, state.setValue(AGE, age + 1), 2);
        }
    }
    //Prevents neighbor updates with the water to cause it to pop off via physics
    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, net.minecraft.world.level.block.Block block, BlockPos fromPos, boolean isMoving) {
        //And here, we are doing nothing! This is going to hopefully prevent it from breaking the block with water physics
    }

    // Right click to harvest when fully grown, resets to age 0
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        int age = state.getValue(AGE);
        if (age == MAX_AGE) {
            // Drop wheat
            popResource(level, pos, new ItemStack(net.minecraft.world.item.Items.WHEAT));
            // Reset to age 0 instead of breaking
            level.setBlock(pos, state.setValue(AGE, 0), 2);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    public int getMaxAge() {
        return MAX_AGE;
    }
}