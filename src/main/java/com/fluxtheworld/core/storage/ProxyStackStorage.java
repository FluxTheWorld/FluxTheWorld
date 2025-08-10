package com.fluxtheworld.core.storage;

public class ProxyStackStorage<T> extends AbstractStackStorage<T> {

  protected final AbstractStackStorage<T> storage;

  public ProxyStackStorage(StackAdapter<T> adapter, AbstractStackStorage<T> storage) {
    super(adapter);
    this.storage = storage;
  }

  @Override
  public int getSlotCount() {
    return storage.getSlotCount();
  }

  @Override
  public T getStackInSlot(int slot) {
    return storage.getStackInSlot(slot);
  }

  @Override
  public T insert(int slot, T stack, boolean simulate) {
    return storage.insert(slot, stack, simulate);
  }

  @Override
  public T extract(int slot, int amount, boolean simulate) {
    return storage.extract(slot, amount, simulate);
  }

  @Override
  public void setStackInSlot(int slot, T stack) {
    storage.setStackInSlot(slot, stack);
  }

  @Override
  public int getSlotCapacity(int slot) {
    return storage.getSlotCapacity(slot);
  }

  @Override
  public boolean canExtract(int slot) {
    return storage.canExtract(slot);
  }

  @Override
  public boolean canInsert(int slot) {
    return storage.canInsert(slot);
  }

  @Override
  public boolean isValid(int slot, T stack) {
    return storage.isValid(slot, stack);
  }

}
