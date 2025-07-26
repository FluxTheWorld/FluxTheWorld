package com.fluxtheworld.core.recipe;

import java.util.List;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public record MachineRecipeInput(List<ItemStack> inputs) implements RecipeInput {

  @Override
  public ItemStack getItem(int slot) {
    if (slot >= inputs.size()) {
      throw new IllegalArgumentException("No item for index " + slot);
    }

    return inputs.get(slot);
  }

  @Override
  public int size() {
    return inputs.size();
  }

}