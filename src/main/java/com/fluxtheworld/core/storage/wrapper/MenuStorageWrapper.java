package com.fluxtheworld.core.storage.wrapper;

import com.fluxtheworld.core.storage.StackStorage;
import com.fluxtheworld.core.storage.slot_access.SlotAccessConfig;

/**
 * Base abstract class for menu/GUI access wrappers.
 * This class provides common functionality for menu access control
 * that can be used with different storage types (items, fluids, etc.).
 * 
 * @param <T> The type of content stored (e.g., ItemStack, FluidStack)
 * @param <S> The type of storage
 * @param <C> The type of slot access configuration
 * @param <H> The type of handler interface (e.g., IItemHandler, IFluidHandler)
 */
public abstract class MenuStorageWrapper<T, S extends StackStorage<T, C, ?>, C extends SlotAccessConfig<T>, H> {

  protected final S storage;
  protected final C slotAccess;

  protected MenuStorageWrapper(S storage, C slotAccess) {
    this.storage = storage;
    this.slotAccess = slotAccess;
  }

  /**
   * Checks if insertion is allowed for the specified slot from the menu side.
   * 
   * @param slot The slot index to check
   * @return True if insertion is allowed
   */
  protected boolean canInsert(int slot) {
    return this.slotAccess.canMenuInsert(slot);
  }

  /**
   * Checks if extraction is allowed for the specified slot from the menu side.
   * 
   * @param slot The slot index to check
   * @return True if extraction is allowed
   */
  protected boolean canExtract(int slot) {
    return this.slotAccess.canMenuExtract(slot);
  }

  /**
   * Gets the number of slots/tanks in the storage.
   * 
   * @return The number of slots/tanks
   */
  protected int getSlotCount() {
    return this.storage.getSlotCount();
  }

  /**
   * Gets the content of the specified slot.
   * 
   * @param slot The slot index
   * @return The content in the slot
   */
  protected T getStackInSlot(int slot) {
    return this.storage.getStackInSlot(slot);
  }

  /**
   * Gets the capacity limit for the specified slot.
   * 
   * @param slot The slot index
   * @return The slot capacity limit
   */
  protected int getSlotLimit(int slot) {
    return this.storage.getSlotLimit(slot);
  }

  /**
   * Checks if the given content is valid for the specified slot.
   * 
   * @param slot The slot index
   * @param stack The content to validate
   * @return True if the content is valid for this slot
   */
  protected boolean isValid(int slot, T stack) {
    return this.slotAccess.canMenuInsert(slot) && this.storage.isValid(slot, stack);
  }

  /**
   * Inserts content into the specified slot.
   * 
   * @param slot The slot index
   * @param stack The content to insert
   * @param simulate If true, the insertion is only simulated
   * @return The remaining content that could not be inserted
   */
  protected T insert(int slot, T stack, boolean simulate) {
    if (!this.canInsert(slot)) {
      return stack;
    }

    return this.storage.insert(slot, stack, simulate);
  }

  /**
   * Extracts content from the specified slot.
   * 
   * @param slot The slot index
   * @param amount The maximum amount to extract
   * @param simulate If true, the extraction is only simulated
   * @return The extracted content
   */
  protected T extract(int slot, int amount, boolean simulate) {
    if (!this.canExtract(slot)) {
      return this.storage.getEmpty();
    }

    return this.storage.extract(slot, amount, simulate);
  }

  /**
   * Sets the content in the specified slot directly.
   * This method is typically used by menu systems for direct slot manipulation.
   * 
   * @param slot The slot index
   * @param stack The content to set
   */
  protected abstract void setStackInSlot(int slot, T stack);
}