package com.fluxtheworld.core.storage;

import com.fluxtheworld.core.storage.side_access.SideAccessConfig;
import com.fluxtheworld.core.storage.slot_access.SlotAccessConfig;
import com.fluxtheworld.core.storage.stack_adapter.StackAdapter;

public class MenuStackStorage<T> extends StackStorage<T> {

  protected final StackStorage<T> storage;
  protected final SideAccessConfig sideAccess;

  protected MenuStackStorage(StackAdapter<T> stackAdapter, StackStorage<T> storage, SlotAccessConfig<T> slotAccess,
      SideAccessConfig sideAccess) {
    super(stackAdapter, slotAccess);
    this.storage = storage;
    this.sideAccess = sideAccess;
  }

  @Override
  public boolean canInsert(int slot) {
    if (!this.slotAccess.canMenuInsert(slot)) {
      return false;
    }

    return true;
  }

  @Override
  public boolean canExtract(int slot) {
    if (!this.slotAccess.canMenuExtract(slot)) {
      return false;
    }

    return true;
  }

  @Override
  protected void setStackInSlot(int slot, T stack) {
    this.storage.setStackInSlot(slot, stack);
  }

  @Override
  public boolean isValid(int slot, T stack) {
    return this.canInsert(slot) && super.isValid(slot, stack);
  }

  @Override
  public T getStackInSlot(int slot) {
    return this.storage.getStackInSlot(slot);
  }

  @Override
  public T insert(int slot, T stack, boolean simulate) {
    if (!this.canInsert(slot)) {
      return stack;
    }

    return this.storage.insert(slot, stack, simulate);
  }

  @Override
  public T extract(int slot, int amount, boolean simulate) {
    if (!this.canExtract(slot)) {
      return this.stackAdapter.getEmpty();
    }

    return this.storage.extract(slot, amount, simulate);
  }
}