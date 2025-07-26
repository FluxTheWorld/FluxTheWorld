package com.fluxtheworld.core.storage.item;

import java.util.function.IntConsumer;

import javax.annotation.Nullable;

import com.fluxtheworld.core.storage.side_access.SideAccessConfig;
import com.fluxtheworld.core.storage.slot_access.ItemSlotAccessConfig;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemStackHandler;

// TODO: Create CompositeItemStorage, that contains tagged slots
public class ItemStorage extends ItemStackHandler {

  private final ItemSlotAccessConfig slotAccess;
  private final @Nullable IntConsumer changeListener;

  public ItemStorage(ItemSlotAccessConfig slotAccess) {
    this(slotAccess, null);
  }

  public ItemStorage(ItemSlotAccessConfig slotAccess, @Nullable IntConsumer changeListener) {
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
      this.changeListener.accept(slot);
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