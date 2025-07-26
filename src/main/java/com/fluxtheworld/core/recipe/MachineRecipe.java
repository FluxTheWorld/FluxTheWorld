package com.fluxtheworld.core.recipe;

public interface MachineRecipe extends GenericRecipe {
  public int energyUsage();

  public int processingTime();
}