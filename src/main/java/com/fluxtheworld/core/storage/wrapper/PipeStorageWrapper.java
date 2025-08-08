package com.fluxtheworld.core.storage.wrapper;

import javax.annotation.Nullable;

import com.fluxtheworld.core.storage.StackStorage;
import com.fluxtheworld.core.storage.side_access.SideAccessConfig;
import com.fluxtheworld.core.storage.slot_access.SlotAccessConfig;

import net.minecraft.core.Direction;

/**
 * Base abstract class for pipe access wrappers.
 * This class provides common functionality for pipe access control
 * that can be used with different storage types (items, fluids, etc.).
 * 
 * @param <T> The type of content stored (e.g., ItemStack, FluidStack)
 * @param <S> The type of storage
 * @param <C> The type of slot access configuration
 * @param <H> The type of handler interface (e.g., IItemHandler, IFluidHandler)
 */
public abstract class PipeStorageWrapper<T, S extends StackStorage<T, C, ?>, C extends SlotAccessConfig<T>, H> {

  protected final S storage;
  protected final C slotAccess;
  protected final SideAccessConfig sideAccess;
  protected final @Nullable Direction side;

  protected PipeStorageWrapper(S storage, C slotAccess, SideAccessConfig sideAccess, @Nullable Direction side) {
    this.storage = storage;
    this.slotAccess = slotAccess;
    this.sideAccess = sideAccess;
    this.side = side;
  }

  /**
   * Checks if insertion is allowed for the specified slot from the pipe side.
   * 
   * @param slot The slot index to check
   * @return True if insertion is allowed
   */
  protected boolean canInsert(int slot) {
    if (!this.slotAccess.canPipeInsert(slot)) {
      return false;
    }

    if (this.side != null && !this.sideAccess.getMode(this.side).canInput()) {
      return false;
    }

    return true;
  }

  /**
   * Checks if extraction is allowed for the specified slot from the pipe side.
   * 
   * @param slot The slot index to check
   * @return True if extraction is allowed
   */
  protected boolean canExtract(int slot) {
    if (!this.slotAccess.canPipeExtract(slot)) {
      return false;
    }

    if (this.side != null && !this.sideAccess.getMode(this.side).canOutput()) {
      return false;
    }

    return true;
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
    return this.slotAccess.canPipeInsert(slot) && this.storage.isValid(slot, stack);
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
}