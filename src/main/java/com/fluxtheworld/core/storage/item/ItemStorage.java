package com.fluxtheworld.core.storage.item;

import javax.annotation.Nullable;

import com.fluxtheworld.core.storage.StackStorage;
import com.fluxtheworld.core.storage.side_access.SideAccessConfig;
import com.fluxtheworld.core.storage.slot_access.ItemSlotAccessConfig;
import com.fluxtheworld.core.storage.slot_access.SlotAccessTag;
import com.fluxtheworld.core.storage.wrapper.ItemPipeStorageWrapper;
import com.fluxtheworld.core.storage.wrapper.ItemMenuStorageWrapper;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemStackHandler;

public class ItemStorage extends StackStorage<ItemStack, ItemSlotAccessConfig, ItemStorageChangeListener> {

  private final ItemStackHandler itemHandler;

  public ItemStorage(ItemSlotAccessConfig slotAccess) {
    this(slotAccess, null);
  }

  public ItemStorage(ItemSlotAccessConfig slotAccess, @Nullable ItemStorageChangeListener changeListener) {
    super(slotAccess, changeListener);
    this.itemHandler = new ItemStackHandler(slotAccess.getSlotCount()) {
      @Override
      public int getSlotLimit(int slot) {
        return ItemStorage.this.getSlotLimit(slot);
      }

      @Override
      public boolean isItemValid(int slot, ItemStack stack) {
        return ItemStorage.this.isValid(slot, stack);
      }

      @Override
      protected void onContentsChanged(int slot) {
        super.onContentsChanged(slot);
        ItemStorage.this.onContentsChanged(slot);
      }
    };
  }

  // Implementation of abstract methods from Storage base class

  @Override
  public int getSlotCount() {
    return this.slotAccess.getSlotCount();
  }

  @Override
  public ItemStack getStackInSlot(int slot) {
    return this.itemHandler.getStackInSlot(slot);
  }

  @Override
  public ItemStack insert(int slot, ItemStack stack, boolean simulate) {
    return this.itemHandler.insertItem(slot, stack, simulate);
  }

  @Override
  public ItemStack extract(int slot, int amount, boolean simulate) {
    return this.itemHandler.extractItem(slot, amount, simulate);
  }

  @Override
  public boolean isValid(int slot, ItemStack stack) {
    return this.slotAccess.isValid(slot, stack);
  }

  @Override
  public int getSlotLimit(int slot) {
    return this.slotAccess.getStackLimit(slot);
  }

  @Override
  public ItemStack getEmpty() {
    return ItemStack.EMPTY;
  }

  @Override
  public boolean isEmpty(ItemStack stack) {
    return stack.isEmpty();
  }

  @Override
  public int getAmount(ItemStack stack) {
    return stack.getCount();
  }

  @Override
  public ItemStack extract(SlotAccessTag tag, ItemStack stack, boolean simulate) {
    var extracted = stack.copyWithCount(0);

    for (int index : this.slotAccess.getTaggedSlots(tag)) {
      if (extracted.getCount() == stack.getCount()) {
        break;
      }

      if (!this.getStackInSlot(index).is(stack.getItem())) {
        continue;
      }

      var found = this.extract(index, stack.getCount() - extracted.getCount(), simulate);
      extracted.grow(found.getCount());
    }

    return extracted;
  }

  @Override
  public @Nullable IItemHandler getForPipe(SideAccessConfig sideAccess, @Nullable Direction side) {
    if (side == null) {
      return new ItemPipeStorageWrapper(this, slotAccess, sideAccess, null);
    }

    if (sideAccess.getMode(side).canConnect()) {
      return new ItemPipeStorageWrapper(this, slotAccess, sideAccess, side);
    }

    return null;
  }

  @Override
  public IItemHandler getForMenu() {
    return new ItemMenuStorageWrapper(this, slotAccess);
  }

  // Backward compatibility methods (delegate to base class methods with new names)

  /**
   * @deprecated Use {@link #insert(int, ItemStack, boolean)} instead
   */
  @Deprecated(forRemoval = true)
  public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
    return this.insert(slot, stack, simulate);
  }

  /**
   * @deprecated Use {@link #extract(int, int, boolean)} instead
   */
  @Deprecated(forRemoval = true)
  public ItemStack extractItem(int slot, int amount, boolean simulate) {
    return this.extract(slot, amount, simulate);
  }

  /**
   * @deprecated Use {@link #insert(String, ItemStack, boolean)} instead
   */
  @Deprecated(forRemoval = true)
  public ItemStack insertItem(String name, ItemStack stack, boolean simulate) {
    return this.insert(name, stack, simulate);
  }

  /**
   * @deprecated Use {@link #extract(String, int, boolean)} instead
   */
  @Deprecated(forRemoval = true)
  public ItemStack extractItem(String name, int amount, boolean simulate) {
    return this.extract(name, amount, simulate);
  }

  /**
   * @deprecated Use {@link #insert(SlotAccessTag, ItemStack, boolean)} instead
   */
  @Deprecated(forRemoval = true)
  public ItemStack insertItem(SlotAccessTag tag, ItemStack stack, boolean simulate) {
    return this.insert(tag, stack, simulate);
  }

  /**
   * @deprecated Use {@link #extract(SlotAccessTag, ItemStack, boolean)} instead
   */
  @Deprecated(forRemoval = true)
  public ItemStack extractItem(SlotAccessTag tag, ItemStack stack, boolean simulate) {
    return this.extract(tag, stack, simulate);
  }

  /**
   * @deprecated Use {@link #isValid(int, ItemStack)} instead
   */
  @Deprecated(forRemoval = true)
  public boolean isItemValid(int slot, ItemStack stack) {
    return this.isValid(slot, stack);
  }

  // Additional ItemStorage-specific methods

  /**
   * Extracts an item stack matching the given ingredient from the storage.
   * This method first checks if the required item can be extracted in the specified quantity
   * in a single operation. It ensures that only one specific type of item is extracted,
   * even if the ingredient can match multiple suitable item types.
   *
   * @param tag The slot access tag to filter which slots to consider.
   * @param ingredient The sized ingredient representing the item(s) to extract.
   * @param simulate If true, the extraction is simulated and no items are removed from storage.
   * @return The extracted ItemStack, or ItemStack.EMPTY if no matching item could be extracted.
   */
  public ItemStack extractIngredient(SlotAccessTag tag, SizedIngredient ingredient, boolean simulate) {
    for (ItemStack stack : ingredient.getItems()) {
      ItemStack found = this.extract(tag, stack, true);
      if (found.getCount() == stack.getCount()) {
        return simulate ? found : this.extract(tag, stack, false);
      }
    }

    return ItemStack.EMPTY;
  }

  // Methods for direct ItemStackHandler compatibility

  /**
   * Gets the number of slots in the underlying ItemStackHandler.
   * This method provides compatibility with ItemStackHandler API.
   * 
   * @return The number of slots
   */
  public int getSlots() {
    return this.itemHandler.getSlots();
  }

  /**
   * Sets the stack in the specified slot directly.
   * This method provides compatibility with ItemStackHandler API.
   *
   * @param slot The slot index
   * @param stack The stack to set
   */
  public void setStackInSlot(int slot, ItemStack stack) {
    this.itemHandler.setStackInSlot(slot, stack);
  }

  // NBT serialization methods for compatibility with ItemStackHandler

  /**
   * Serializes the storage contents to NBT.
   * This method provides compatibility with ItemStackHandler API.
   *
   * @param registries The holder lookup provider
   * @return The serialized NBT compound tag
   */
  public net.minecraft.nbt.CompoundTag serializeNBT(net.minecraft.core.HolderLookup.Provider registries) {
    return this.itemHandler.serializeNBT(registries);
  }

  /**
   * Deserializes the storage contents from NBT.
   * This method provides compatibility with ItemStackHandler API.
   *
   * @param registries The holder lookup provider
   * @param nbt The NBT compound tag to deserialize from
   */
  public void deserializeNBT(net.minecraft.core.HolderLookup.Provider registries, net.minecraft.nbt.CompoundTag nbt) {
    this.itemHandler.deserializeNBT(registries, nbt);
  }

}