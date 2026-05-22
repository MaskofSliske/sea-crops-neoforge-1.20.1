package com.example.maskseacrops;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class NemoOrangeMelonBlock extends NemoVineBlock {

    public NemoOrangeMelonBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected Block getFruitBlock() {
        return Blocks.PUMPKIN;
    }
}