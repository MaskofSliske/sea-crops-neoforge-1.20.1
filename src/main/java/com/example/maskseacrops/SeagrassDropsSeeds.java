package com.example.maskseacrops;

import com.google.common.base.Supplier;
import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;

public class SeagrassDropsSeeds extends LootModifier {

    public static final Supplier<Codec<SeagrassDropsSeeds>> CODEC = () ->
            Codec.unit(new SeagrassDropsSeeds(new LootItemCondition[0]));

    public SeagrassDropsSeeds(LootItemCondition[] conditions) {
        super(conditions);
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        // Check if the block being broken is seagrass or tall seagrass
        BlockState blockState = context.getParamOrNull(net.minecraft.world.level.storage.loot.parameters.LootContextParams.BLOCK_STATE);
        if (blockState != null) {
            String blockId = blockState.getBlock().builtInRegistryHolder().key().location().toString();
            if ((blockId.equals("minecraft:seagrass") || blockId.equals("minecraft:tall_seagrass"))
                    && context.getRandom().nextFloat() < 0.15f) {
                generatedLoot.add(new ItemStack(MaskSeaCrops.SEA_WHEAT_SEEDS.get()));
            }
        }
        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }

}