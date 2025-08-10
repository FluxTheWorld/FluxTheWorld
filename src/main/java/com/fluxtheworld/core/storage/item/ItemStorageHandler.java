package com.fluxtheworld.core.storage.item;

import com.fluxtheworld.core.storage.AbstractStackStorage;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandlerModifiable;

public class ItemStorageHandler implements IItemHandlerModifiable {

  protected final AbstractStackStorage<ItemStack> storage;

  public ItemStorageHandler(AbstractStackStorage<ItemStack> storage) {
    this.storage = storage;
  }

  @Override
  public int getSlots() {
    return this.storage.getSlotCount();
  }

  @Override
  public ItemStack getStackInSlot(int slot) {
    return this.storage.getStackInSlot(slot);
  }

  @Override
  public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
    return this.storage.insert(slot, stack, simulate);
  }

  @Override
  public ItemStack extractItem(int slot, int amount, boolean simulate) {
    return this.storage.extract(slot, amount, simulate);
  }

  @Override
  public int getSlotLimit(int slot) {
    return this.storage.getSlotCapacity(slot);
  }

  @Override
  public boolean isItemValid(int slot, ItemStack stack) {
    return this.storage.isValid(slot, stack);
  }

  @Override
  public void setStackInSlot(int slot, ItemStack stack) {
    this.storage.setStackInSlot(slot, stack);
  }

}
