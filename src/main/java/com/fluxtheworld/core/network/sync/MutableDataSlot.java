package com.fluxtheworld.core.network.sync;

public interface MutableDataSlot<T> extends DataSlot<T> {
  void set(T value);
}