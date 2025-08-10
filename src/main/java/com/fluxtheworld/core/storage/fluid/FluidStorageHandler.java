package com.fluxtheworld.core.storage.fluid;

import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class FluidStorageHandler implements IFluidHandler {

  protected final FluidStorage storage;

  public FluidStorageHandler(FluidStorage storage) {
    this.storage = storage;
  }

  @Override
  public int getTanks() {
    return this.storage.getSlotCount();
  }

  @Override
  public FluidStack getFluidInTank(int tank) {
    return this.storage.getStackInSlot(tank);
  }

  @Override
  public int getTankCapacity(int tank) {
    return this.storage.getSlotCapacity(tank);
  }

  @Override
  public boolean isFluidValid(int tank, FluidStack stack) {
    return this.storage.isValid(tank, stack);
  }

  @Override
  public int fill(FluidStack resource, FluidAction action) {
    return this.storage.insert(resource, action.simulate());
  }

  @Override
  public FluidStack drain(FluidStack resource, FluidAction action) {
    return this.storage.extract(resource, action.simulate());
  }

  @Override
  public FluidStack drain(int maxDrain, FluidAction action) {
    return this.storage.extract(maxDrain, action.simulate());
  }

}
