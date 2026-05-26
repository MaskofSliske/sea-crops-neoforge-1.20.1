package com.example.maskseacrops;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class SiltFeature extends Feature<NoneFeatureConfiguration> {

    public SiltFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos pos = context.origin();
        RandomSource random = context.random();

        //Making sure it only replaces sand or gravel
        if (!level.getBlockState(pos.below()).is(Blocks.SAND) && !level.getBlockState(pos.below()).is(Blocks.GRAVEL)) {
            System.out.println("Failed block check!");
            return false;
        }

        //Placing a ribbon of silt
        int length = 9 + random.nextInt(20); //or approx 9-20 blocks long
        int width = 1 + random.nextInt(3); //making it 1-3 blocks wide
        boolean horizontal = random.nextBoolean(); //making the ribbon wobble during generation

        BlockPos base = pos.below();
        for (int a = -length/2; a <= length/2; a++) {
            for (int b = -width/2; b <= width/2; b++) {
                BlockPos current = horizontal ? pos.offset(a, 0, b) : pos.offset(b, 0, a);
                if (level.getBlockState(current).is(Blocks.SAND) || level.getBlockState(current).is(Blocks.GRAVEL) || level.getBlockState(current).is(Blocks.DIRT)) {
                    level.setBlock(current, MaskSeaCrops.SILT.get().defaultBlockState(), 2);
                }
            }
        }
        return true;
    }
}
