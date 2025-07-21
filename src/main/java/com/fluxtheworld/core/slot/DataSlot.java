package com.fluxtheworld.core.slot;

public interface DataSlot<T> {
  T get();

  boolean isDirty();
}