package com.fluxtheworld.core.slot;

import com.fluxtheworld.core.slot.payload.DataSlotPayload;

public interface MutableDataSlot<T> extends DataSlot<T> {
  public void set(T value);
}