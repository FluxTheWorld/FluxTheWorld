package com.fluxtheworld.core.task;

import javax.annotation.Nullable;

public sealed interface TaskState {

  public static final TaskState.Active Active = new TaskState.Active();
  public static final TaskState.Completed Completed = new TaskState.Completed();

  public record Pending(@Nullable TaskIssue cause) implements TaskState {
  }

  public record Active() implements TaskState {
  }

  public record Aborted(@Nullable TaskIssue cause) implements TaskState {
  }

  public record Completed() implements TaskState {
  }

}
