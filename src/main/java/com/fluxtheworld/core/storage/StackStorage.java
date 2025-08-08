package com.fluxtheworld.core.storage;

import com.fluxtheworld.core.storage.slot_access.SlotAccessConfig;
import com.fluxtheworld.core.storage.slot_access.SlotAccessTag;
import com.fluxtheworld.core.storage.stack_adapter.StackAdapter;

import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;

/**
 * Base abstract class for storage systems that handle different types of content (items, fluids, etc.).
 * This class provides common functionality for slot-based storage with access control and change notifications.
 * 
 * @param <T>
 *          The type of content stored (e.g., ItemStack, FluidStack)
 */
public abstract class StackStorage<T> implements INBTSerializable<CompoundTag> {

  protected final SlotAccessConfig<T> slotAccess;
  protected final StackAdapter<T> stackAdapter;

  protected StackStorage(SlotAccessConfig<T> slotAccess, StackAdapter<T> stackAdapter) {
    this.slotAccess = slotAccess;
    this.stackAdapter = stackAdapter;
  }

  /**
   * Gets the total number of slots in this storage.
   * 
   * @return The number of slots
   */
  public int getSlotCount() {
    return this.slotAccess.getSlotCount();
  }

  /**
   * Gets the maximum capacity limit for the specified slot.
   * 
   * @param slot
   *          The slot index
   * @return The slot's capacity limit
   */
  public int getSlotLimit(int slot) {
    return this.slotAccess.getStackLimit(slot);
  }

  /**
   * Checks if the given content is valid for the specified slot.
   * 
   * @param slot
   *          The slot index
   * @param stack
   *          The content to validate
   * @return True if the content is valid for this slot
   */
  public boolean isValid(int slot, T stack) {
    return this.slotAccess.isValid(slot, stack);
  }

  /**
   * Called when the contents of a slot have changed.
   * This method notifies the change listener if one is registered.
   * 
   * @param slot
   *          The index of the slot that changed
   */
  protected void onChanged(int slot) {
  }

  /**
   * Gets the content of the specified slot.
   * 
   * @param slot
   *          The slot index
   * @return The content in the slot, or empty if the slot is empty or invalid
   */
  public abstract T getStackInSlot(int slot);

  /**
   * Inserts content into the specified slot.
   * 
   * @param slot
   *          The slot index
   * @param stack
   *          The content to insert
   * @param simulate
   *          If true, the insertion is only simulated and no actual changes are made
   * @return The remaining content that could not be inserted
   */
  public abstract T insert(int slot, T stack, boolean simulate);

  /**
   * Extracts content from the specified slot.
   * 
   * @param slot
   *          The slot index
   * @param amount
   *          The maximum amount to extract
   * @param simulate
   *          If true, the extraction is only simulated and no actual changes are made
   * @return The extracted content
   */
  public abstract T extract(int slot, int amount, boolean simulate);

  // region Utils

  /**
   * Gets the index of a named slot.
   * 
   * @param name
   *          The slot name
   * @return The slot index
   * @throws IllegalArgumentException
   *           if the slot name doesn't exist
   */
  public int getSlotIndex(String name) {
    return this.slotAccess.getSlotIndex(name);
  }

  /**
   * Gets the content of a named slot.
   * 
   * @param name
   *          The slot name
   * @return The content in the slot
   */
  public T getStackInSlot(String name) {
    return this.getStackInSlot(this.getSlotIndex(name));
  }

  /**
   * Inserts content into a named slot.
   * 
   * @param name
   *          The slot name
   * @param stack
   *          The content to insert
   * @param simulate
   *          If true, the insertion is only simulated
   * @return The remaining content that could not be inserted
   */
  public T insert(String name, T stack, boolean simulate) {
    return this.insert(this.getSlotIndex(name), stack, simulate);
  }

  /**
   * Extracts content from a named slot.
   * 
   * @param name
   *          The slot name
   * @param amount
   *          The maximum amount to extract
   * @param simulate
   *          If true, the extraction is only simulated
   * @return The extracted content
   */
  public T extract(String name, int amount, boolean simulate) {
    return this.extract(this.getSlotIndex(name), amount, simulate);
  }

  /**
   * Inserts content into slots with the specified tag.
   * 
   * @param tag
   *          The slot access tag
   * @param stack
   *          The content to insert
   * @param simulate
   *          If true, the insertion is only simulated
   * @return The remaining content that could not be inserted
   */
  public T insert(SlotAccessTag tag, T stack, boolean simulate) {
    T remaining = stack;

    for (int index : this.slotAccess.getTaggedSlots(tag)) {
      if (this.stackAdapter.isEmpty(remaining)) {
        return this.stackAdapter.getEmpty();
      }

      remaining = this.insert(index, remaining, simulate);
    }

    return remaining;
  }

  /**
   * Extracts content from slots with the specified tag.
   * 
   * @param tag
   *          The slot access tag
   * @param stack
   *          The content template to extract (type and amount)
   * @param simulate
   *          If true, the extraction is only simulated
   * @return The extracted content
   */
  public T extract(SlotAccessTag tag, T stack, boolean simulate) {
    var extracted = this.stackAdapter.copyWithAmount(stack, 0);

    for (int index : this.slotAccess.getTaggedSlots(tag)) {
      if (this.stackAdapter.getAmount(extracted) == this.stackAdapter.getAmount(stack)) {
        break;
      }

      if (!this.stackAdapter.isSameContent(this.getStackInSlot(index), stack)) {
        continue;
      }

      var found = this.extract(index, this.stackAdapter.getAmount(stack) - this.stackAdapter.getAmount(extracted), simulate);
      this.stackAdapter.grow(extracted, this.stackAdapter.getAmount(found));
    }

    return extracted;
  }

  // endregion
}