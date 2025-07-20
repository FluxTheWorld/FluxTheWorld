package com.fluxtheworld.core.network.sync;

public interface DataSlot<T> {
  T get();

  boolean isDirty();
}