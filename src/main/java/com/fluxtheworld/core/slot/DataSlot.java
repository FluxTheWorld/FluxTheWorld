package com.fluxtheworld.core.slot;

import com.fluxtheworld.core.slot.payload.DataSlotPayload;

public interface DataSlot<T> {
  public T get();

  public boolean checkAndClearUpdateFlag();

  public DataSlotPayload encodePayload();

  public void decodePayload(DataSlotPayload payload);
}