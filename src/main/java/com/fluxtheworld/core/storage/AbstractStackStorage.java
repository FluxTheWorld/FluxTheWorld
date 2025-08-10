package com.fluxtheworld.core.storage;

public abstract class AbstractStackStorage<T> {

  protected final StackAdapter<T> adapter;

  protected AbstractStackStorage(StackAdapter<T> adapter) {
    this.adapter = adapter;
  }

  public abstract int getSlotCount();

  public abstract T getStackInSlot(int slot);

  public abstract T insert(int slot, T stack, boolean simulate);

  public abstract T extract(int slot, int amount, boolean simulate);

  public abstract void setStackInSlot(int slot, T stack);

  protected abstract int getSlotCapacity(int slot);

  public boolean isValid(int slot, T stack) {
    return this.canInsert(slot);
  }

  public boolean canInsert(int slot) {
    return true;
  }

  public boolean canExtract(int slot) {
    return true;
  }

  protected final int getStackLimit(int slot, T stack) {
    return Math.min(this.getSlotCapacity(slot), adapter.getMaxStackSize(stack));
  }

  protected final void validateSlotIndex(int slot) {
    if (slot < 0 || slot >= getSlotCount()) {
      throw new IllegalArgumentException("Slot " + slot + " not in valid range - [0," + getSlotCount() + ")");
    }
  }
}
