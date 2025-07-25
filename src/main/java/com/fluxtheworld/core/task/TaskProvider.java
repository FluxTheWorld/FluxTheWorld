package com.fluxtheworld.core.task;

public interface TaskProvider {

  public GenericTask getCurrentTask();

  public GenericTask createNextTask();

  public GenericTask createEmptyTask();

}
