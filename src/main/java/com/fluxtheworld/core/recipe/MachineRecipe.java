package com.fluxtheworld.core.recipe;

import javax.annotation.Nullable;

import net.minecraft.world.item.crafting.RecipeInput;

public interface MachineRecipe<T extends RecipeInput> extends GenericRecipe<T> {

  public default int energyCost(@Nullable T input) {
    return this.energyCost();
  }

  public int energyCost();

}
