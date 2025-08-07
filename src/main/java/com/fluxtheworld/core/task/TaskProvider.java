package com.fluxtheworld.core.task;

public interface TaskProvider {

  /**
   * Retrieves the current task.
   *
   * @return The current {@link SerializableTask}.
   */
  public SerializableTask getCurrentTask();

  /**
   * Creates and sets the next task.
   *
   * @return The new {@link SerializableTask}.
   */
  public SerializableTask createNextTask();


  /**
   * Creates and sets an empty task for serialization.
   *
   * @return An empty {@link SerializableTask}.
   */
  public SerializableTask createEmptyTask();

}
