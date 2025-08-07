package com.fluxtheworld.core.storage.item;

import java.util.function.IntConsumer;

import javax.annotation.Nullable;

import com.fluxtheworld.core.storage.side_access.SideAccessConfig;
import com.fluxtheworld.core.storage.slot_access.ItemSlotAccessConfig;
import com.fluxtheworld.core.storage.slot_access.SlotAccessTag;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemStackHandler;

public class ItemStorage extends ItemStackHandler {

  private final ItemSlotAccessConfig slotAccess;
  private final @Nullable ItemStorageChangeListener changeListener;

  public ItemStorage(ItemSlotAccessConfig slotAccess) {
    this(slotAccess, null);
  }

  public ItemStorage(ItemSlotAccessConfig slotAccess, @Nullable ItemStorageChangeListener changeListener) {
    super(slotAccess.getSlotCount());
    this.slotAccess = slotAccess;
    this.changeListener = changeListener;
  }

  @Override
  public int getSlotLimit(int slot) {
    return this.slotAccess.getStackLimit(slot);
  }

  @Override
  public boolean isItemValid(int slot, ItemStack stack) {
    return this.slotAccess.isItemValid(slot, stack);
  }

  public int getSlotIndex(String name) {
    return this.slotAccess.getSlotIndex(name);
  }

  public ItemStack getStackInSlot(String name) {
    return this.getStackInSlot(this.getSlotIndex(name));
  }

  public ItemStack insertItem(String name, ItemStack stack, boolean simulate) {
    return this.insertItem(this.getSlotIndex(name), stack, simulate);
  }

  public ItemStack extractItem(String name, int amount, boolean simulate) {
    return this.extractItem(this.getSlotIndex(name), amount, simulate);
  }

  public ItemStack insertItem(SlotAccessTag tag, ItemStack stack, boolean simulate) {
    var remaining = stack.copy();

    for (int index : this.slotAccess.getTaggedSlots(tag)) {
      if (remaining.isEmpty()) {
        return ItemStack.EMPTY;
      }

      remaining = super.insertItem(index, remaining, simulate);
    }

    return remaining;
  }

  public ItemStack extractItem(SlotAccessTag tag, ItemStack stack, boolean simulate) {
    var extracted = stack.copyWithCount(0);

    for (int index : this.slotAccess.getTaggedSlots(tag)) {
      if (extracted.getCount() == stack.getCount()) {
        break;
      }

      if (!this.getStackInSlot(index).is(stack.getItem())) {
        continue;
      }

      var found = super.extractItem(index, stack.getCount() - extracted.getCount(), simulate);
      extracted.grow(found.getCount());
    }

    return extracted;
  }

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
      ItemStack found = this.extractItem(tag, stack, true);
      if (found.getCount() == stack.getCount()) {
        return simulate ? found : this.extractItem(tag, stack, false);
      }
    }

    return ItemStack.EMPTY;
  }

  public @Nullable IItemHandler getForPipe(SideAccessConfig sideAccess, @Nullable Direction side) {
    if (side == null) {
      return new Pipe(this, slotAccess, sideAccess, null);
    }

    if (sideAccess.getMode(side).canConnect()) {
      return new Pipe(this, slotAccess, sideAccess, side);
    }

    return null;
  }

  public IItemHandler getForMenu() {
    return new Menu(this, slotAccess);
  }

  @Override
  protected void onContentsChanged(int slot) {
    super.onContentsChanged(slot);

    if (this.changeListener != null) {
      this.changeListener.onItemStorageChanged(slot);
    }
  }

  private static class Pipe implements IItemHandler {

    private final ItemStorage storage;
    private final ItemSlotAccessConfig slotAccess;
    private final SideAccessConfig sideAccess;
    private final @Nullable Direction side;

    public Pipe(ItemStorage storage, ItemSlotAccessConfig slotAccess, SideAccessConfig sideAccess, @Nullable Direction side) {
      this.storage = storage;
      this.side = side;
      this.sideAccess = sideAccess;
      this.slotAccess = slotAccess;
    }

    @Override
    public int getSlots() {
      return storage.getSlots();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
      return storage.getStackInSlot(slot);
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
      if (!this.slotAccess.canPipeInsert(slot)) {
        return stack;
      }

      if (side != null && !sideAccess.getMode(side).canInput()) {
        return stack;
      }

      return storage.insertItem(slot, stack, simulate);
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
      if (!this.slotAccess.canPipeExtract(slot)) {
        return ItemStack.EMPTY;
      }

      if (side != null && !sideAccess.getMode(side).canOutput()) {
        return ItemStack.EMPTY;
      }

      return storage.extractItem(slot, amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
      return storage.getSlotLimit(slot);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
      return this.slotAccess.canPipeInsert(slot) && storage.isItemValid(slot, stack);
    }
  }

  // SlotItemHandler requires us to use IItemHandlerModifiable interface
  private static class Menu implements IItemHandlerModifiable {

    private final ItemStorage storage;
    private final ItemSlotAccessConfig slotAccess;

    public Menu(ItemStorage storage, ItemSlotAccessConfig slotAccess) {
      this.storage = storage;
      this.slotAccess = slotAccess;
    }

    @Override
    public int getSlots() {
      return storage.getSlots();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
      return storage.getStackInSlot(slot);
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
      if (!this.slotAccess.canMenuInsert(slot)) {
        return stack;
      }

      return storage.insertItem(slot, stack, simulate);
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
      if (!this.slotAccess.canMenuExtract(slot)) {
        return ItemStack.EMPTY;
      }

      return storage.extractItem(slot, amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
      return storage.getSlotLimit(slot);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
      return this.slotAccess.canMenuInsert(slot) && storage.isItemValid(slot, stack);
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
      this.storage.setStackInSlot(slot, stack);
    }
  }
}