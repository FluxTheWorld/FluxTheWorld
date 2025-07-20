package com.fluxtheworld.core.network.sync;

import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

import com.fluxtheworld.core.network.sync.payload.IntDataSlotPayload;

public abstract class IntDataSlot implements DataSlot<Integer>, MutableDataSlot<Integer> {
  private int lastValue;

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
  public boolean isDirty() {
    int currentValue = get();
    boolean dirty = currentValue != lastValue;
    lastValue = currentValue;
    return dirty;
  }

  public IntDataSlotPayload encodePayload() {
      return new IntDataSlotPayload(get());
  }

  public void decodePayload(IntDataSlotPayload payload) {
      set(payload.value());
  }
}