package com.fluxtheworld.core.storage;

import javax.annotation.Nullable;

import com.fluxtheworld.core.storage.side_access.SideAccessConfig;
import com.fluxtheworld.core.storage.slot_access.SlotAccessConfig;
import com.fluxtheworld.core.storage.stack_adapter.StackAdapter;

import net.minecraft.core.Direction;

public class PipeStackStorage<T, H> extends StackStorage<T, H> {

  protected final StackStorage<T, H> storage;
  protected final SideAccessConfig sideAccess;
  protected final @Nullable Direction side;

  protected PipeStackStorage(StackAdapter<T> stackAdapter, StackStorage<T, H> storage, SlotAccessConfig<T> slotAccess,
      SideAccessConfig sideAccess, @Nullable Direction side) {
    super(stackAdapter, slotAccess);
    this.storage = storage;
    this.sideAccess = sideAccess;
    this.side = side;
  }

  @Override
  public boolean canInsert(int slot) {
    if (!this.slotAccess.canPipeInsert(slot)) {
      return false;
    }

    if (side != null && !sideAccess.getMode(side).canInput()) {
      return false;
    }

    return true;
  }

  @Override
  public boolean canExtract(int slot) {
    if (!this.slotAccess.canPipeExtract(slot)) {
      return false;
    }

    if (side != null && !sideAccess.getMode(side).canOutput()) {
      return false;
    }

    return true;
  }

  @Override
  public void setStackInSlot(int slot, T stack) {
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