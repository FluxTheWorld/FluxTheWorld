package com.fluxtheworld.core.storage.wrapper;

import javax.annotation.Nullable;

import com.fluxtheworld.core.storage.item.ItemStorage;
import com.fluxtheworld.core.storage.side_access.SideAccessConfig;
import com.fluxtheworld.core.storage.slot_access.ItemSlotAccessConfig;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;

/**
 * Pipe wrapper for ItemStorage that implements IItemHandler interface.
 * This class provides pipe access control for item storage systems.
 */
public class ItemPipeStorageWrapper extends PipeStorageWrapper<ItemStack, ItemStorage, ItemSlotAccessConfig, IItemHandler> implements IItemHandler {

  public ItemPipeStorageWrapper(ItemStorage storage, ItemSlotAccessConfig slotAccess, SideAccessConfig sideAccess, @Nullable Direction side) {
    super(storage, slotAccess, sideAccess, side);
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
}