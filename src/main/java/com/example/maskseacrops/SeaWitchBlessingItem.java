package com.example.maskseacrops;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

/**
 * It WILL return itself after crafting in this mod.
 * DO NOT ADD "remainItem" to any recipes that include this item, for this item!
 * Doing so will cause it to duplicate! Just add it like any normal item!
 */

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