package com.fluxtheworld.core.storage.item;

import javax.annotation.Nullable;

import com.fluxtheworld.core.storage.ProxyStackStorage;
import com.fluxtheworld.core.storage.StackStorage;
import com.fluxtheworld.core.storage.side_access.SideAccessConfig;
import com.fluxtheworld.core.storage.slot_access.SlotAccessConfig;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

public class ItemStorage extends StackStorage<ItemStack> {

  private final SlotAccessConfig<ItemStack> slotAccess;
  private final @Nullable ChangeListener changeListener;

  public ItemStorage(SlotAccessConfig<ItemStack> slotAccess, @Nullable ChangeListener changeListener) {
    super(ItemStackAdapter.INSTANCE, slotAccess.getSlotCount());
    this.slotAccess = slotAccess;
    this.changeListener = changeListener;
  }

  @Override
  public int getSlotCapacity(int slot) {
    return this.slotAccess.getSlotCapacity(slot);
  }

  @Override
  public boolean isValid(int slot, ItemStack stack) {
    return this.slotAccess.isValid(slot, stack) && super.isValid(slot, stack);
  }

  public Menu getForMenu() {
    return new Menu(this, this.slotAccess);
  }

  public Pipe getForPipe(SideAccessConfig sideAccess, @Nullable Direction side) {
    return new Pipe(this, this.slotAccess, sideAccess, side);
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

  public static class Pipe extends ProxyStackStorage<ItemStack> {

    private final ItemStorageHandler handler;
    private final SlotAccessConfig<ItemStack> slotAccess;
    private final SideAccessConfig sideAccess;
    private final @Nullable Direction side;

    public Pipe(ItemStorage storage, SlotAccessConfig<ItemStack> slotAccess, SideAccessConfig sideAccess, @Nullable Direction side) {
      super(ItemStackAdapter.INSTANCE, storage);
      this.handler = new ItemStorageHandler(this);
      this.slotAccess = slotAccess;
      this.sideAccess = sideAccess;
      this.side = side;
    }

    public ItemStorageHandler getHandler() {
      return this.handler;
    }

    @Override
    public boolean canExtract(int slot) {
      if (!slotAccess.canPipeInsert(slot)) {
        return false;
      }

      if (side != null && !sideAccess.getMode(side).canInput()) {
        return false;
      }

      return storage.canExtract(slot);
    }

    @Override
    public boolean canInsert(int slot) {
      if (!slotAccess.canPipeExtract(slot)) {
        return false;
      }

      if (side != null && !sideAccess.getMode(side).canOutput()) {
        return false;
      }

      return storage.canInsert(slot);
    }
  }

  public static class Menu extends ProxyStackStorage<ItemStack> {

    private final ItemStorageHandler handler;
    private final SlotAccessConfig<ItemStack> slotAccess;

    public Menu(ItemStorage storage, SlotAccessConfig<ItemStack> slotAccess) {
      super(ItemStackAdapter.INSTANCE, storage);
      this.handler = new ItemStorageHandler(this);
      this.slotAccess = slotAccess;
    }

    public ItemStorageHandler getHandler() {
      return this.handler;
    }

    @Override
    public boolean canExtract(int slot) {
      if (!slotAccess.canMenuInsert(slot)) {
        return false;
      }

      return storage.canExtract(slot);
    }

    @Override
    public boolean canInsert(int slot) {
      if (!slotAccess.canMenuExtract(slot)) {
        return false;
      }

      return storage.canInsert(slot);
    }
  }
}