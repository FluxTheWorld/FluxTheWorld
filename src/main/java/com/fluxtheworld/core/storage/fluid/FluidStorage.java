package com.fluxtheworld.core.storage.fluid;

import javax.annotation.Nullable;

import com.fluxtheworld.core.storage.ProxyStackStorage;
import com.fluxtheworld.core.storage.StackStorage;
import com.fluxtheworld.core.storage.side_access.SideAccessConfig;
import com.fluxtheworld.core.storage.slot_access.SlotAccessConfig;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.fluids.FluidStack;

public class FluidStorage extends StackStorage<FluidStack> {

  private final SlotAccessConfig<FluidStack> slotAccess;
  private final @Nullable ChangeListener changeListener;

  public FluidStorage(SlotAccessConfig<FluidStack> slotAccess, @Nullable ChangeListener changeListener) {
    super(FluidStackAdapter.INSTANCE, slotAccess.getSlotCount());
    this.slotAccess = slotAccess;
    this.changeListener = changeListener;
  }

  @Override
  public int getSlotCapacity(int slot) {
    return this.slotAccess.getSlotCapacity(slot);
  }

  @Override
  public boolean isValid(int slot, FluidStack stack) {
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
      this.changeListener.onFluidStorageChanged(slot);
    }
  }

  public interface ChangeListener {
    void onFluidStorageChanged(int slot);
  }

  public interface Provider {
    FluidStorage getFluidStorage();

    SideAccessConfig getFluidSideAccess();
  }

  public static class Pipe extends ProxyStackStorage<FluidStack> {

    private final FluidStorageHandler handler;
    private final SlotAccessConfig<FluidStack> slotAccess;
    private final SideAccessConfig sideAccess;
    private final @Nullable Direction side;

    public Pipe(FluidStorage storage, SlotAccessConfig<FluidStack> slotAccess, SideAccessConfig sideAccess, @Nullable Direction side) {
      super(FluidStackAdapter.INSTANCE, storage);
      this.handler = new FluidStorageHandler(this);
      this.slotAccess = slotAccess;
      this.sideAccess = sideAccess;
      this.side = side;
    }

    public FluidStorageHandler getHandler() {
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

  public static class Menu extends ProxyStackStorage<FluidStack> {

    private final FluidStorageHandler handler;
    private final SlotAccessConfig<FluidStack> slotAccess;

    public Menu(FluidStorage storage, SlotAccessConfig<FluidStack> slotAccess) {
      super(FluidStackAdapter.INSTANCE, storage);
      this.handler = new FluidStorageHandler(this);
      this.slotAccess = slotAccess;
    }

    public FluidStorageHandler getHandler() {
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