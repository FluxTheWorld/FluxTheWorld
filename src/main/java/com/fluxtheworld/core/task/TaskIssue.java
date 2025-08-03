package com.fluxtheworld.core.task;

public sealed interface TaskIssue {

  public record Unknown() implements TaskIssue {
  }

}
