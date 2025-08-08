package com.fluxtheworld.core.storage.wrapper;

import com.fluxtheworld.core.storage.fluid.FluidStorage;
import com.fluxtheworld.core.storage.slot_access.FluidSlotAccessConfig;

import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

/**
 * Menu wrapper for FluidStorage that implements IFluidHandler interface.
 * This class provides menu/GUI access control for fluid storage systems.
 */
public class FluidMenuStorageWrapper extends MenuStorageWrapper<FluidStack, FluidStorage, FluidSlotAccessConfig, IFluidHandler> implements IFluidHandler {

  public FluidMenuStorageWrapper(FluidStorage storage, FluidSlotAccessConfig slotAccess) {
    super(storage, slotAccess);
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
      if (!this.canInsert(i)) {
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
      if (!this.canExtract(i)) {
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
      if (!this.canExtract(i)) {
        continue;
      }

      FluidStack tankFluid = this.getStackInSlot(i);
      if (!tankFluid.isEmpty()) {
        return this.storage.drain(i, maxDrain, simulate);
      }
    }
    
    return FluidStack.EMPTY;
  }

  @Override
  protected void setStackInSlot(int slot, FluidStack stack) {
    // For fluids, we don't typically have direct slot setting like items
    // This would need to be implemented based on specific requirements
    throw new UnsupportedOperationException("Direct fluid slot setting is not supported");
  }
}