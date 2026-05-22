package com.example.maskseacrops;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

//It will return itself after crafting in this mod. DO NOT ADD "remainItem" to any recipes, doing so will make it duplicate! Just add like a normal item!

public class SeaWitchBlessingItem extends Item {
    public SeaWitchBlessingItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        return itemStack.copy();
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }
}