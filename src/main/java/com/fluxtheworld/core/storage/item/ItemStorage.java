package com.fluxtheworld.core.storage.item;

import java.util.function.IntConsumer;

import javax.annotation.Nullable;

import com.fluxtheworld.core.storage.side_access.SideAccessConfig;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

public class ItemStorage extends ItemStackHandler {

  private final ItemStorageLayout layout;
  private final @Nullable IntConsumer changeListener;

  public ItemStorage(ItemStorageLayout layout) {
    this(layout, null);
  }

  public ItemStorage(ItemStorageLayout layout, @Nullable IntConsumer changeListener) {
    super(layout.getSlotCount());
    this.layout = layout;
    this.changeListener = changeListener;
  }

  public final ItemStorageLayout getLayout() {
    return layout;
  }

  @Override
  public boolean isItemValid(int slot, ItemStack stack) {
    return layout.isItemValid(slot, stack);
  }

  @Override
  public int getSlotLimit(int slot) {
    return layout.getStackLimit(slot);
  }

  public @Nullable IItemHandler getForSide(SideAccessConfig config, @Nullable Direction side) {
    if (side == null) {
      return new Wrapper(this, config, null);
    }

    if (config.getMode(side).canConnect()) {
      return new Wrapper(this, config, side);
    }

    return null;
  }

  @Override
  protected void onContentsChanged(int slot) {
    super.onContentsChanged(slot);

    if (this.changeListener != null) {
      this.changeListener.accept(slot);
    }
  }

  private static class Wrapper implements IItemHandler {

    private final ItemStorage origin;
    private final SideAccessConfig config;
    private final @Nullable Direction side;

    public Wrapper(ItemStorage origin, SideAccessConfig config, @Nullable Direction side) {
      this.origin = origin;
      this.side = side;
      this.config = config;
    }

    @Override
    public int getSlots() {
      return origin.getSlots();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
      return origin.getStackInSlot(slot);
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
      if (!origin.getLayout().canInsert(slot)) {
        return stack;
      }

      if (side != null && !config.getMode(side).canInput()) {
        return stack;
      }

      return origin.insertItem(slot, stack, simulate);
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
      if (!origin.getLayout().canExtract(slot)) {
        return ItemStack.EMPTY;
      }

      if (side != null && !config.getMode(side).canOutput()) {
        return ItemStack.EMPTY;
      }

      return origin.extractItem(slot, amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
      return origin.getSlotLimit(slot);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
      return origin.isItemValid(slot, stack);
    }
  }
}