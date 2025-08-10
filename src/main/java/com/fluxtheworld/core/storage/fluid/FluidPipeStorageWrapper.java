package com.fluxtheworld.core.storage.fluid;

import javax.annotation.Nullable;

import com.fluxtheworld.core.storage.PipeStackStorage;
import com.fluxtheworld.core.storage.side_access.SideAccessConfig;
import com.fluxtheworld.core.storage.slot_access.FluidSlotAccessConfig;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

/**
 * Pipe wrapper for FluidStorage that implements IFluidHandler interface.
 * This class provides pipe access control for fluid storage systems.
 */
public class FluidPipeStorageWrapper extends PipeStackStorage<FluidStack, FluidStorage, FluidSlotAccessConfig, IFluidHandler> implements IFluidHandler {

  public FluidPipeStorageWrapper(FluidStorage storage, FluidSlotAccessConfig slotAccess, SideAccessConfig sideAccess, @Nullable Direction side) {
    super(storage, slotAccess, sideAccess, side);
  }

  @Override
  public int getTanks() {
    return this.getSlotCount();
  }

  @Override
  public FluidStack getFluidInTank(int tank) {
    return this.getStackInSlot(tank);
  }

  @Override
  public int getTankCapacity(int tank) {
    return this.getSlotLimit(tank);
  }

  @Override
  public boolean isFluidValid(int tank, FluidStack stack) {
    return this.isValid(tank, stack);
  }

  @Override
  public int fill(FluidStack resource, FluidAction action) {
    boolean simulate = action == FluidAction.SIMULATE;
    
    for (int i = 0; i < this.getTanks(); i++) {
      if (!this.canInsertItem(i)) {
        continue;
      }

      if (this.storage.isValid(i, resource)) {
        return this.storage.fill(i, resource, simulate);
      }
    }
    
    return 0;
  }

  @Override
  public FluidStack drain(FluidStack resource, FluidAction action) {
    boolean simulate = action == FluidAction.SIMULATE;
    
    for (int i = 0; i < this.getTanks(); i++) {
      if (!this.canExtractItem(i)) {
        continue;
      }

      FluidStack tankFluid = this.getStackInSlot(i);
      if (tankFluid.isFluidEqual(resource)) {
        return this.storage.drain(i, resource.getAmount(), simulate);
      }
    }
    
    return FluidStack.EMPTY;
  }

  @Override
  public FluidStack drain(int maxDrain, FluidAction action) {
    boolean simulate = action == FluidAction.SIMULATE;
    
    for (int i = 0; i < this.getTanks(); i++) {
      if (!this.canExtractItem(i)) {
        continue;
      }

      FluidStack tankFluid = this.getStackInSlot(i);
      if (!tankFluid.isEmpty()) {
        return this.storage.drain(i, maxDrain, simulate);
      }
    }
    
    return FluidStack.EMPTY;
  }
}