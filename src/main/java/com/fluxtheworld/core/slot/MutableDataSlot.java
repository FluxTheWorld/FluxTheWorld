package com.fluxtheworld.core.slot;

public interface MutableDataSlot<T> extends DataSlot<T> {
  void set(T value);
}