package com.fluxtheworld.core.task;

import com.fluxtheworld.core.recipe.MachineRecipe;

public interface TaskProvider<T extends GenericTask> {

  public T getCurrentTask();

  public T createNextTask();

  public T createEmptyTask();

}
