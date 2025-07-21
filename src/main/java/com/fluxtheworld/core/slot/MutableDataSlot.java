package com.fluxtheworld.core.slot;

public interface MutableDataSlot<T> extends DataSlot<T> {
  public void set(T value);
}