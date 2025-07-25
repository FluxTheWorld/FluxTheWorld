package com.fluxtheworld.core.util;

public class CountdownTimer {
  private final int initialDelay;
  private int currentDelay;

  public CountdownTimer(int initialDelay) {
    this.initialDelay = initialDelay;
    this.currentDelay = initialDelay;
  }

  public boolean tick() {
    if (this.currentDelay > 0) {
      this.currentDelay--;
    }
    return this.isCompleted();
  }

  public boolean tickAndReset() {
    boolean completed = this.tick();
    if (completed) {
      this.reset();
    }
    return completed;
  }

  public boolean isCompleted() {
    return this.currentDelay <= 0;
  }

  public void reset() {
    this.reset(this.initialDelay);
  }

  public void reset(int newDelay) {
    this.currentDelay = newDelay;
  }
}