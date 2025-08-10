package com.fluxtheworld.core.storage.item;

import javax.annotation.Nullable;

import com.fluxtheworld.core.storage.StackStorage;
import com.fluxtheworld.core.storage.side_access.SideAccessConfig;
import com.fluxtheworld.core.storage.slot_access.ItemSlotAccessConfig;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

public class ItemStorage extends StackStorage<ItemStack> {

  private final ItemSlotAccessConfig slotAccess;
  private final @Nullable ChangeListener changeListener;

  public ItemStorage(ItemSlotAccessConfig slotAccess, @Nullable ChangeListener changeListener) {
    super(ItemStackAdapter.INSTANCE, slotAccess.getSlotCount());
    this.slotAccess = slotAccess;
    this.changeListener = changeListener;
  }

  @Override
  protected int getSlotCapacity(int slot) {
    return this.slotAccess.getSlotCapacity(slot);
  }

  @Override
  public boolean isValid(int slot, ItemStack stack) {
    return this.slotAccess.isValid(slot, stack) && super.isValid(slot, stack);
  }

  public Menu getForMenu() {
    return new Menu(this);
  }

  public Pipe getForPipe(SideAccessConfig sideAccess, @Nullable Direction side) {
    return new Pipe(this, sideAccess, side);
  }

  @Override
  protected void onContentsChanged(int slot) {
    if (this.changeListener != null) {
      this.changeListener.onItemStorageChanged(slot);
    }
  }

  public interface ChangeListener {
    void onItemStorageChanged(int slot);
  }

  public interface Provider {
    ItemStorage getItemStorage();

    SideAccessConfig getItemSideAccess();
  }

  public static class Pipe extends ItemStorageHandler {

    private final SideAccessConfig sideAccess;
    private final @Nullable Direction side;

    public Pipe(ItemStorage storage, SideAccessConfig sideAccess, @Nullable Direction side) {
      super(storage);
      this.side = side;
      this.sideAccess = sideAccess;
    }

    public boolean canInsertItem(int slot) {
      if (!storage.slotAccess.canPipeInsert(slot)) {
        return false;
      }

      if (side != null && !sideAccess.getMode(side).canInput()) {
        return false;
      }

      return true;
    }

    public boolean canExtractItem(int slot) {
      if (!storage.slotAccess.canPipeExtract(slot)) {
        return false;
      }

      if (side != null && !sideAccess.getMode(side).canOutput()) {
        return false;
      }

      return true;
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
      if (!canInsertItem(slot)) {
        return stack;
      }

      return super.insertItem(slot, stack, simulate);
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
      if (!canExtractItem(slot)) {
        return ItemStack.EMPTY;
      }

      return super.extractItem(slot, amount, simulate);
    }
  }

  public static class Menu extends ItemStorageHandler {

    public Menu(ItemStorage storage) {
      super(storage);
    }

    public boolean canInsertItem(int slot) {
      if (!storage.slotAccess.canMenuInsert(slot)) {
        return false;
      }

      return true;
    }

    public boolean canExtractItem(int slot) {
      if (!storage.slotAccess.canMenuExtract(slot)) {
        return false;
      }

      return true;
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
      if (!canInsertItem(slot)) {
        return stack;
      }

      return super.insertItem(slot, stack, simulate);
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
      if (!canExtractItem(slot)) {
        return ItemStack.EMPTY;
      }

      return super.extractItem(slot, amount, simulate);
    }
  }
}