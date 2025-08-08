package com.fluxtheworld.core.storage;

import javax.annotation.Nullable;

import com.fluxtheworld.core.storage.side_access.SideAccessConfig;
import com.fluxtheworld.core.storage.slot_access.SlotAccessConfig;
import com.fluxtheworld.core.storage.slot_access.SlotAccessTag;

import net.minecraft.core.Direction;

/**
 * Base abstract class for storage systems that handle different types of content (items, fluids, etc.).
 * This class provides common functionality for slot-based storage with access control and change notifications.
 * 
 * @param <T> The type of content stored (e.g., ItemStack, FluidStack)
 * @param <C> The type of slot access configuration
 * @param <L> The type of change listener
 */
public abstract class StackStorage<T, C extends SlotAccessConfig<T>, L extends StorageChangeListener> {

  protected final C slotAccess;
  protected final @Nullable L changeListener;

  protected StackStorage(C slotAccess, @Nullable L changeListener) {
    this.slotAccess = slotAccess;
    this.changeListener = changeListener;
  }

  // Abstract methods that subclasses must implement

  /**
   * Gets the total number of slots in this storage.
   * 
   * @return The number of slots
   */
  public abstract int getSlotCount();

  /**
   * Gets the content of the specified slot.
   * 
   * @param slot The slot index
   * @return The content in the slot, or empty if the slot is empty or invalid
   */
  public abstract T getStackInSlot(int slot);

  /**
   * Inserts content into the specified slot.
   * 
   * @param slot The slot index
   * @param stack The content to insert
   * @param simulate If true, the insertion is only simulated and no actual changes are made
   * @return The remaining content that could not be inserted
   */
  public abstract T insert(int slot, T stack, boolean simulate);

  /**
   * Extracts content from the specified slot.
   * 
   * @param slot The slot index
   * @param amount The maximum amount to extract
   * @param simulate If true, the extraction is only simulated and no actual changes are made
   * @return The extracted content
   */
  public abstract T extract(int slot, int amount, boolean simulate);

  /**
   * Checks if the given content is valid for the specified slot.
   * 
   * @param slot The slot index
   * @param stack The content to validate
   * @return True if the content is valid for this slot
   */
  public abstract boolean isValid(int slot, T stack);

  /**
   * Gets the maximum capacity limit for the specified slot.
   * 
   * @param slot The slot index
   * @return The slot's capacity limit
   */
  public abstract int getSlotLimit(int slot);

  /**
   * Creates an empty instance of the content type.
   * 
   * @return An empty content instance
   */
  public abstract T getEmpty();

  /**
   * Checks if the given content is empty.
   * 
   * @param stack The content to check
   * @return True if the content is empty
   */
  public abstract boolean isEmpty(T stack);

  /**
   * Gets the amount/count of the given content.
   * 
   * @param stack The content to measure
   * @return The amount of content
   */
  public abstract int getAmount(T stack);

  // Common implementations that can be shared

  /**
   * Gets the index of a named slot.
   * 
   * @param name The slot name
   * @return The slot index
   * @throws IllegalArgumentException if the slot name doesn't exist
   */
  public int getSlotIndex(String name) {
    return this.slotAccess.getSlotIndex(name);
  }

  /**
   * Gets the content of a named slot.
   * 
   * @param name The slot name
   * @return The content in the slot
   */
  public T getStackInSlot(String name) {
    return this.getStackInSlot(this.getSlotIndex(name));
  }

  /**
   * Inserts content into a named slot.
   * 
   * @param name The slot name
   * @param stack The content to insert
   * @param simulate If true, the insertion is only simulated
   * @return The remaining content that could not be inserted
   */
  public T insert(String name, T stack, boolean simulate) {
    return this.insert(this.getSlotIndex(name), stack, simulate);
  }

  /**
   * Extracts content from a named slot.
   * 
   * @param name The slot name
   * @param amount The maximum amount to extract
   * @param simulate If true, the extraction is only simulated
   * @return The extracted content
   */
  public T extract(String name, int amount, boolean simulate) {
    return this.extract(this.getSlotIndex(name), amount, simulate);
  }

  /**
   * Inserts content into slots with the specified tag.
   * 
   * @param tag The slot access tag
   * @param stack The content to insert
   * @param simulate If true, the insertion is only simulated
   * @return The remaining content that could not be inserted
   */
  public T insert(SlotAccessTag tag, T stack, boolean simulate) {
    T remaining = stack;

    for (int index : this.slotAccess.getTaggedSlots(tag)) {
      if (this.isEmpty(remaining)) {
        return this.getEmpty();
      }

      remaining = this.insert(index, remaining, simulate);
    }

    return remaining;
  }

  /**
   * Extracts content from slots with the specified tag.
   * 
   * @param tag The slot access tag
   * @param stack The content template to extract (type and amount)
   * @param simulate If true, the extraction is only simulated
   * @return The extracted content
   */
  public abstract T extract(SlotAccessTag tag, T stack, boolean simulate);

  /**
   * Gets a handler for pipe access with the given side access configuration.
   * 
   * @param sideAccess The side access configuration
   * @param side The side being accessed (null for internal access)
   * @return A handler for pipe access, or null if access is not allowed
   */
  public abstract @Nullable Object getForPipe(SideAccessConfig sideAccess, @Nullable Direction side);

  /**
   * Gets a handler for menu/GUI access.
   * 
   * @return A handler for menu access
   */
  public abstract Object getForMenu();

  /**
   * Called when the contents of a slot have changed.
   * This method notifies the change listener if one is registered.
   * 
   * @param slot The index of the slot that changed
   */
  protected void onContentsChanged(int slot) {
    if (this.changeListener != null) {
      this.changeListener.onStorageChanged(slot);
    }
  }
}