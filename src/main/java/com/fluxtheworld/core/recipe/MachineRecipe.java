package com.fluxtheworld.core.recipe;

import javax.annotation.Nullable;

import net.minecraft.world.item.crafting.RecipeInput;

public interface MachineRecipe<T extends RecipeInput> extends GenericRecipe<T> {

  public default int energyUsage(@Nullable T input) {
    return this.energyUsage();
  }

  public int energyUsage();

  public int processingTime();
}