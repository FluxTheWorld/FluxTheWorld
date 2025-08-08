package com.fluxtheworld.core.storage.wrapper;

import com.fluxtheworld.core.storage.item.ItemStorage;
import com.fluxtheworld.core.storage.slot_access.ItemSlotAccessConfig;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandlerModifiable;

/**
 * Menu wrapper for ItemStorage that implements IItemHandlerModifiable interface.
 * This class provides menu/GUI access control for item storage systems.
 * SlotItemHandler requires us to use IItemHandlerModifiable interface.
 */
public class ItemMenuStorageWrapper extends MenuStorageWrapper<ItemStack, ItemStorage, ItemSlotAccessConfig, IItemHandlerModifiable> implements IItemHandlerModifiable {

  public ItemMenuStorageWrapper(ItemStorage storage, ItemSlotAccessConfig slotAccess) {
    super(storage, slotAccess);
  }

  @Override
  public int getSlots() {
    return this.getSlotCount();
  }

  @Override
  public ItemStack getStackInSlot(int slot) {
    return super.getStackInSlot(slot);
  }

  @Override
  public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
    return this.insert(slot, stack, simulate);
  }

  @Override
  public ItemStack extractItem(int slot, int amount, boolean simulate) {
    return this.extract(slot, amount, simulate);
  }

  @Override
  public int getSlotLimit(int slot) {
    return super.getSlotLimit(slot);
  }

  @Override
  public boolean isItemValid(int slot, ItemStack stack) {
    return this.isValid(slot, stack);
  }

  @Override
  public void setStackInSlot(int slot, ItemStack stack) {
    this.storage.setStackInSlot(slot, stack);
  }

}