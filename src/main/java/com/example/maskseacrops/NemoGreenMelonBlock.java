package com.example.maskseacrops;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class NemoGreenMelonBlock extends NemoVineBlock {

    public NemoGreenMelonBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected Block getFruitBlock() {
        return Blocks.MELON;
    }
}