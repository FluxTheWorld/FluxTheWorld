package com.fluxtheworld.core.task;

public interface TaskProvider {

  /**
   * Retrieves the current task.
   *
   * @return The current {@link GenericTask}.
   */
  public GenericTask getCurrentTask();

  /**
   * Creates and sets the next task.
   *
   * @return The new {@link GenericTask}.
   */
  public GenericTask createNextTask();


  /**
   * Creates and sets an empty task for serialization.
   *
   * @return An empty {@link GenericTask}.
   */
  public GenericTask createEmptyTask();

}
