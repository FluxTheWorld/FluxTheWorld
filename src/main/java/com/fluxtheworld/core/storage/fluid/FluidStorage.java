package com.fluxtheworld.core.storage.fluid;

import javax.annotation.Nullable;

import com.fluxtheworld.core.storage.StackStorage;
import com.fluxtheworld.core.storage.side_access.SideAccessConfig;
import com.fluxtheworld.core.storage.slot_access.FluidSlotAccessConfig;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.fluids.FluidStack;

public class FluidStorage extends StackStorage<FluidStack> {

  private final FluidSlotAccessConfig slotAccess;
  private final @Nullable ChangeListener changeListener;

  public FluidStorage(FluidSlotAccessConfig slotAccess, @Nullable ChangeListener changeListener) {
    super(FluidStackAdapter.INSTANCE, slotAccess.getSlotCount());
    this.slotAccess = slotAccess;
    this.changeListener = changeListener;
  }

  @Override
  protected int getSlotCapacity(int slot) {
    return this.slotAccess.getSlotCapacity(slot);
  }

  @Override
  public boolean isValid(int slot, FluidStack stack) {
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

  public static class Pipe extends FluidStorageHandler {

    private final SideAccessConfig sideAccess;
    private final @Nullable Direction side;

    public Pipe(FluidStorage storage, SideAccessConfig sideAccess, @Nullable Direction side) {
      super(storage);
      this.side = side;
      this.sideAccess = sideAccess;
    }

    public boolean canInsertFluid(int slot) {
      if (!storage.slotAccess.canPipeInsert(slot)) {
        return false;
      }

      if (side != null && !sideAccess.getMode(side).canInput()) {
        return false;
      }

      return true;
    }

    public boolean canExtractFluid(int slot) {
      if (!storage.slotAccess.canPipeExtract(slot)) {
        return false;
      }

      if (side != null && !sideAccess.getMode(side).canOutput()) {
        return false;
      }

      return true;
    }

    @Override
    public FluidStack insertFluid(int slot, FluidStack stack, boolean simulate) {
      if (!canInsertFluid(slot)) {
        return stack;
      }

      return super.insertFluid(slot, stack, simulate);
    }

    @Override
    public FluidStack extractFluid(int slot, int amount, boolean simulate) {
      if (!canExtractFluid(slot)) {
        return FluidStack.EMPTY;
      }

      return super.extractFluid(slot, amount, simulate);
    }
  }

  public static class Menu extends FluidStorageHandler {

    public Menu(FluidStorage storage) {
      super(storage);
    }

    public boolean canInsertFluid(int slot) {
      if (!storage.slotAccess.canMenuInsert(slot)) {
        return false;
      }

      return true;
    }

    public boolean canExtractFluid(int slot) {
      if (!storage.slotAccess.canMenuExtract(slot)) {
        return false;
      }

      return true;
    }

    @Override
    public FluidStack insertFluid(int slot, FluidStack stack, boolean simulate) {
      if (!canInsertFluid(slot)) {
        return stack;
      }

      return super.insertFluid(slot, stack, simulate);
    }

    @Override
    public FluidStack extractFluid(int slot, int amount, boolean simulate) {
      if (!canExtractFluid(slot)) {
        return FluidStack.EMPTY;
      }

      return super.extractFluid(slot, amount, simulate);
    }
  }
}