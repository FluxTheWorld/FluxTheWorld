package com.fluxtheworld.core.storage.fluid;

import javax.annotation.Nullable;

import com.fluxtheworld.core.storage.AbstractStackStorage;
import com.fluxtheworld.core.storage.side_access.SideAccessConfig;
import com.fluxtheworld.core.storage.slot_access.SlotAccessConfig;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.fluids.FluidStack;

public class FluidStorage extends AbstractStackStorage<FluidStack> {

  private final SlotAccessConfig<FluidStack> slotAccess;
  private final @Nullable ChangeListener changeListener;

  public FluidStorage(SlotAccessConfig<FluidStack> slotAccess, @Nullable ChangeListener changeListener) {
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
}