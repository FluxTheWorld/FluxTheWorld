package com.fluxtheworld.core.slot;


import com.fluxtheworld.FTWMod;
import com.fluxtheworld.core.slot.payload.DataSlotPayload;
import com.fluxtheworld.core.slot.payload.FloatDataSlotPayload;
import com.fluxtheworld.core.util.FloatConsumer;
import com.fluxtheworld.core.util.FloatSupplier;

public abstract class FloatDataSlot implements MutableDataSlot<Float> {
  private float prevValue;

  public static FloatDataSlot standalone() {
    return new FloatDataSlot() {
      private float value;

      @Override
      public Float get() {
        return value;
      }

      @Override
      public void set(Float value) {
        this.value = value;
      }
    };
  }

  public static FloatDataSlot simple(FloatSupplier getter, FloatConsumer setter) {
    return new FloatDataSlot() {
      @Override
      public Float get() {
        return getter.getAsFloat();
      }

      @Override
      public void set(Float value) {
        setter.accept(value);
      }
    };
  }

  public static FloatDataSlot readOnly(FloatSupplier getter) {
    return new FloatDataSlot() {
      @Override
      public Float get() {
        return getter.getAsFloat();
      }

      @Override
      public void set(Float value) {
        FTWMod.LOGGER.warn("Attempted to set a value on a read-only FloatDataSlot. This operation is not permitted.");
      }
    };
  }

  @Override
  public boolean checkAndClearUpdateFlag() {
    float currValue = get();
    boolean dirty = currValue != prevValue;
    prevValue = currValue;
    return dirty;
  }

  @Override
  public DataSlotPayload encodePayload() {
    return new FloatDataSlotPayload(get());
  }

  @Override
  public void decodePayload(DataSlotPayload payload) {
    if (payload instanceof FloatDataSlotPayload it) {
      this.set(it.value());
    }
  }
}