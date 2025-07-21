package com.fluxtheworld.core.slot;

import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

import com.fluxtheworld.core.slot.payload.DataSlotPayload;
import com.fluxtheworld.core.slot.payload.IntDataSlotPayload;

public abstract class IntDataSlot implements MutableDataSlot<Integer> {
  private int prevValue;

  public static IntDataSlot standalone() {
    return new IntDataSlot() {
      private int value;

      @Override
      public Integer get() {
        return value;
      }

      @Override
      public void set(Integer value) {
        this.value = value;
      }
    };
  }

  public static IntDataSlot simple(IntSupplier getter, IntConsumer setter) {
    return new IntDataSlot() {
      @Override
      public Integer get() {
        return getter.getAsInt();
      }

      @Override
      public void set(Integer value) {
        setter.accept(value);
      }
    };
  }

  @Override
  public boolean checkAndClearUpdateFlag() {
    int currValue = get();
    boolean dirty = currValue != prevValue;
    prevValue = currValue;
    return dirty;
  }

  @Override
  public DataSlotPayload encodePayload() {
    return new IntDataSlotPayload(get());
  }

  @Override
  public void decodePayload(DataSlotPayload payload) {
    if (payload instanceof IntDataSlotPayload it) {
      this.set(it.value());
    }
  }
}