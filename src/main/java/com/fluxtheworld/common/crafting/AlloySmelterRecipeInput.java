package com.fluxtheworld.common.crafting;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public class AlloySmelterRecipeInput implements RecipeInput {
    private final ItemStack itemA;
    private final ItemStack itemB;

    public AlloySmelterRecipeInput(ItemStack itemA, ItemStack itemB) {
        this.itemA = itemA;
        this.itemB = itemB;
    }

    @Override
    public ItemStack getItem(int index) {
        if (index == 0) {
            return itemA;
        } else if (index == 1) {
            return itemB;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public int size() {
        return 2;
    }
}