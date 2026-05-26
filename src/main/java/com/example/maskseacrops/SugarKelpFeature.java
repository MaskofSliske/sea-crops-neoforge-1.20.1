package com.example.maskseacrops;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.Fluids;

public class SugarKelpFeature extends Feature<NoneFeatureConfiguration> {

    public SugarKelpFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos pos = context.origin();
        RandomSource random = context.random();

        //This is for checking if the block below is sand or gravel
        BlockPos below = pos.below();
        if (!level.getBlockState(below).is(Blocks.SAND) && !level.getBlockState(below).is(Blocks.GRAVEL)) {
            return false;
        }

        //Water at position check
        if (!level.getFluidState(pos).is(Fluids.WATER)) {
            return false;
        }

        //Placing Sugar Kelp block
        level.setBlock(pos, MaskSeaCrops.SUGAR_KELP_PLANT.get().defaultBlockState(), 2);

        //Making it grow up a few blocks, about 2 to so
        int height = 2 + random.nextInt(4);
        BlockPos current = pos.above();
        for (int i = 0; i < height; i++) {
            if (!level.getFluidState(current).is(Fluids.WATER)) break;
            if (i == height -1) {
                level.setBlock(current, MaskSeaCrops.SUGAR_KELP.get().defaultBlockState(), 2);
            } else {
                level.setBlock(current, MaskSeaCrops.SUGAR_KELP_PLANT.get().defaultBlockState(), 2);
            }
            current = current.above();
        }
        return true;
    }
}
