package com.fluxtheworld.core.storage.fluid;

import com.fluxtheworld.core.storage.AbstractStackStorage;

import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class FluidStorageHandler implements IFluidHandler {

  protected final AbstractStackStorage<FluidStack> storage;

  public FluidStorageHandler(AbstractStackStorage<FluidStack> storage) {
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
    // IFluidHandler.fill() expects the amount that was filled (int)
    // but StackStorage.insert() returns the remaining/overflow stack (FluidStack)
    // So we calculate: filled = requested - remaining
    FluidStack remaining = this.storage.insert(resource, action.simulate());
    return resource.getAmount() - remaining.getAmount();
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