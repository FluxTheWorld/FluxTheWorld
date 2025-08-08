package com.fluxtheworld.core.storage.fluid;

import javax.annotation.Nullable;

import com.fluxtheworld.core.storage.StackStorage;
import com.fluxtheworld.core.storage.side_access.SideAccessConfig;
import com.fluxtheworld.core.storage.slot_access.FluidSlotAccessConfig;
import com.fluxtheworld.core.storage.slot_access.SlotAccessTag;
import com.fluxtheworld.core.storage.wrapper.FluidPipeStorageWrapper;
import com.fluxtheworld.core.storage.wrapper.FluidMenuStorageWrapper;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class FluidStorage extends StackStorage<FluidStack, FluidSlotAccessConfig, FluidStorageChangeListener> {

  private final FluidStack[] fluids;

  public FluidStorage(FluidSlotAccessConfig slotAccess) {
    this(slotAccess, null);
  }

  public FluidStorage(FluidSlotAccessConfig slotAccess, @Nullable FluidStorageChangeListener changeListener) {
    super(slotAccess, changeListener);
    this.fluids = new FluidStack[slotAccess.getSlotCount()];
    for (int i = 0; i < this.fluids.length; i++) {
      this.fluids[i] = FluidStack.EMPTY;
    }
  }

  // Implementation of abstract methods from Storage base class

  @Override
  public int getSlotCount() {
    return this.slotAccess.getSlotCount();
  }

  @Override
  public FluidStack getStackInSlot(int slot) {
    if (slot < 0 || slot >= this.fluids.length) {
      return FluidStack.EMPTY;
    }
    return this.fluids[slot].copy();
  }

  @Override
  public FluidStack insert(int slot, FluidStack stack, boolean simulate) {
    int filled = this.fillInternal(slot, stack, simulate);
    if (filled == 0) {
      return stack;
    }
    
    FluidStack remaining = stack.copy();
    remaining.shrink(filled);
    return remaining.isEmpty() ? FluidStack.EMPTY : remaining;
  }

  @Override
  public FluidStack extract(int slot, int amount, boolean simulate) {
    return this.drainInternal(slot, amount, simulate);
  }

  @Override
  public boolean isValid(int slot, FluidStack stack) {
    return this.slotAccess.isValid(slot, stack);
  }

  @Override
  public int getSlotLimit(int slot) {
    return this.slotAccess.getStackLimit(slot);
  }

  @Override
  public FluidStack getEmpty() {
    return FluidStack.EMPTY;
  }

  @Override
  public boolean isEmpty(FluidStack stack) {
    return stack.isEmpty();
  }

  @Override
  public int getAmount(FluidStack stack) {
    return stack.getAmount();
  }

  @Override
  public FluidStack extract(SlotAccessTag tag, FluidStack stack, boolean simulate) {
    FluidStack extracted = stack.copyWithAmount(0);

    for (int index : this.slotAccess.getTaggedSlots(tag)) {
      if (extracted.getAmount() == stack.getAmount()) {
        break;
      }

      if (!this.fluids[index].isFluidEqual(stack)) {
        continue;
      }

      FluidStack found = this.extract(index, stack.getAmount() - extracted.getAmount(), simulate);
      extracted.grow(found.getAmount());
    }

    return extracted;
  }

  @Override
  public @Nullable IFluidHandler getForPipe(SideAccessConfig sideAccess, @Nullable Direction side) {
    if (side == null) {
      return new FluidPipeStorageWrapper(this, slotAccess, sideAccess, null);
    }

    if (sideAccess.getMode(side).canConnect()) {
      return new FluidPipeStorageWrapper(this, slotAccess, sideAccess, side);
    }

    return null;
  }

  @Override
  public IFluidHandler getForMenu() {
    return new FluidMenuStorageWrapper(this, slotAccess);
  }

  // Internal methods with boolean simulate parameter

  private int fillInternal(int tank, FluidStack resource, boolean simulate) {
    if (tank < 0 || tank >= this.fluids.length) {
      return 0;
    }

    if (resource.isEmpty()) {
      return 0;
    }

    if (!this.isValid(tank, resource)) {
      return 0;
    }

    int limit = this.getSlotLimit(tank);

    if (this.fluids[tank].isEmpty()) {
      int fillAmount = Math.min(resource.getAmount(), limit);
      if (!simulate) {
        this.fluids[tank] = resource.copyWithAmount(fillAmount);
        this.onContentsChanged(tank);
      }
      return fillAmount;
    } else if (!this.fluids[tank].isFluidEqual(resource)) {
      return 0;
    }

    int filled = Math.min(limit - this.fluids[tank].getAmount(), resource.getAmount());
    if (!simulate && filled > 0) {
      this.fluids[tank].grow(filled);
      this.onContentsChanged(tank);
    }
    return filled;
  }

  private FluidStack drainInternal(int tank, int maxDrain, boolean simulate) {
    if (tank < 0 || tank >= this.fluids.length) {
      return FluidStack.EMPTY;
    }

    if (this.fluids[tank].isEmpty()) {
      return FluidStack.EMPTY;
    }

    int drained = Math.min(this.fluids[tank].getAmount(), maxDrain);
    FluidStack stack = this.fluids[tank].copyWithAmount(drained);

    if (!simulate && drained > 0) {
      this.fluids[tank].shrink(drained);
      if (this.fluids[tank].isEmpty()) {
        this.fluids[tank] = FluidStack.EMPTY;
      }
      this.onContentsChanged(tank);
    }

    return stack;
  }

  // Public methods with boolean simulate parameter

  public int fill(int tank, FluidStack resource, boolean simulate) {
    return this.fillInternal(tank, resource, simulate);
  }

  public FluidStack drain(int tank, int maxDrain, boolean simulate) {
    return this.drainInternal(tank, maxDrain, simulate);
  }

  public int fill(SlotAccessTag tag, FluidStack resource, boolean simulate) {
    int totalFilled = 0;
    FluidStack remaining = resource.copy();

    for (int index : this.slotAccess.getTaggedSlots(tag)) {
      if (remaining.isEmpty()) {
        break;
      }

      int filled = this.fill(index, remaining, simulate);
      totalFilled += filled;
      remaining.shrink(filled);
    }

    return totalFilled;
  }

  public FluidStack drain(SlotAccessTag tag, FluidStack resource, boolean simulate) {
    return this.extract(tag, resource, simulate);
  }

  // NeoForge IFluidHandler compatibility methods (using enum)

  /**
   * Gets a NeoForge-compatible IFluidHandler for this storage.
   * This method provides the standard IFluidHandler interface that uses FluidAction enum.
   * 
   * @return A NeoForge-compatible IFluidHandler
   */
  public IFluidHandler getFluidHandler() {
    return new NeoForgeFluidHandler(this);
  }

  // Backward compatibility methods with IFluidHandler.FluidAction enum

  /**
   * @deprecated Use {@link #fill(int, FluidStack, boolean)} instead
   */
  @Deprecated(forRemoval = true)
  public int fill(int tank, FluidStack resource, IFluidHandler.FluidAction action) {
    return this.fill(tank, resource, action == IFluidHandler.FluidAction.SIMULATE);
  }

  /**
   * @deprecated Use {@link #drain(int, int, boolean)} instead
   */
  @Deprecated(forRemoval = true)
  public FluidStack drain(int tank, int maxDrain, IFluidHandler.FluidAction action) {
    return this.drain(tank, maxDrain, action == IFluidHandler.FluidAction.SIMULATE);
  }

  /**
   * @deprecated Use {@link #fill(String, FluidStack, boolean)} instead
   */
  @Deprecated(forRemoval = true)
  public int fill(String name, FluidStack resource, IFluidHandler.FluidAction action) {
    return this.fill(name, resource, action == IFluidHandler.FluidAction.SIMULATE);
  }

  /**
   * @deprecated Use {@link #drain(String, int, boolean)} instead
   */
  @Deprecated(forRemoval = true)
  public FluidStack drain(String name, int maxDrain, IFluidHandler.FluidAction action) {
    return this.drain(name, maxDrain, action == IFluidHandler.FluidAction.SIMULATE);
  }

  /**
   * @deprecated Use {@link #drain(String, FluidStack, boolean)} instead
   */
  @Deprecated(forRemoval = true)
  public FluidStack drain(String name, FluidStack resource, IFluidHandler.FluidAction action) {
    return this.drain(name, resource, action == IFluidHandler.FluidAction.SIMULATE);
  }

  /**
   * @deprecated Use {@link #fill(SlotAccessTag, FluidStack, boolean)} instead
   */
  @Deprecated(forRemoval = true)
  public int fill(SlotAccessTag tag, FluidStack resource, IFluidHandler.FluidAction action) {
    return this.fill(tag, resource, action == IFluidHandler.FluidAction.SIMULATE);
  }

  /**
   * @deprecated Use {@link #drain(SlotAccessTag, FluidStack, boolean)} instead
   */
  @Deprecated(forRemoval = true)
  public FluidStack drain(SlotAccessTag tag, FluidStack resource, IFluidHandler.FluidAction action) {
    return this.drain(tag, resource, action == IFluidHandler.FluidAction.SIMULATE);
  }

  // New boolean-based methods for named slots

  public int fill(String name, FluidStack resource, boolean simulate) {
    return this.fill(this.getSlotIndex(name), resource, simulate);
  }

  public FluidStack drain(String name, int maxDrain, boolean simulate) {
    return this.drain(this.getSlotIndex(name), maxDrain, simulate);
  }

  public FluidStack drain(String name, FluidStack resource, boolean simulate) {
    return this.drain(this.getSlotIndex(name), resource.getAmount(), simulate);
  }

  // Additional methods for compatibility

  /**
   * Gets the fluid in the specified slot by name.
   * 
   * @param name The slot name
   * @return The fluid in the slot
   */
  public FluidStack getFluidInSlot(String name) {
    return this.getStackInSlot(name);
  }

  /**
   * @deprecated Use {@link #isValid(int, FluidStack)} instead
   */
  @Deprecated(forRemoval = true)
  public boolean isFluidValid(int tank, FluidStack stack) {
    return this.isValid(tank, stack);
  }

  // NeoForge IFluidHandler wrapper that converts between boolean and enum
  private static class NeoForgeFluidHandler implements IFluidHandler {
    private final FluidStorage storage;

    public NeoForgeFluidHandler(FluidStorage storage) {
      this.storage = storage;
    }

    @Override
    public int getTanks() {
      return storage.getSlotCount();
    }

    @Override
    public FluidStack getFluidInTank(int tank) {
      return storage.getStackInSlot(tank);
    }

    @Override
    public int getTankCapacity(int tank) {
      return storage.getSlotLimit(tank);
    }

    @Override
    public boolean isFluidValid(int tank, FluidStack stack) {
      return storage.isValid(tank, stack);
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
      int totalFillAmount = 0;
      FluidStack remaining = resource.copy();
      boolean simulate = action == FluidAction.SIMULATE;

      for (int i = 0; i < storage.getSlotCount(); i++) {
        if (remaining.isEmpty()) {
          break;
        }

        int filled = storage.fill(i, remaining, simulate);
        totalFillAmount += filled;
        remaining.shrink(filled);
      }

      return totalFillAmount;
    }

    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
      boolean simulate = action == FluidAction.SIMULATE;
      for (int i = 0; i < storage.getSlotCount(); i++) {
        if (storage.fluids[i].isFluidEqual(resource)) {
          return storage.drain(i, resource.getAmount(), simulate);
        }
      }
      return FluidStack.EMPTY;
    }

    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
      boolean simulate = action == FluidAction.SIMULATE;
      for (int i = 0; i < storage.getSlotCount(); i++) {
        if (!storage.fluids[i].isEmpty()) {
          return storage.drain(i, maxDrain, simulate);
        }
      }
      return FluidStack.EMPTY;
    }
  }

  // Methods for IFluidHandler compatibility (these delegate to NeoForgeFluidHandler)

  /**
   * Gets the number of tanks in this storage.
   * This method provides compatibility with IFluidHandler API.
   * 
   * @return The number of tanks
   */
  public int getTanks() {
    return this.getSlotCount();
  }

  /**
   * Gets the fluid in the specified tank.
   * This method provides compatibility with IFluidHandler API.
   * 
   * @param tank The tank index
   * @return The fluid in the tank
   */
  public FluidStack getFluidInTank(int tank) {
    return this.getStackInSlot(tank);
  }

  /**
   * Gets the capacity of the specified tank.
   * This method provides compatibility with IFluidHandler API.
   * 
   * @param tank The tank index
   * @return The tank capacity
   */
  public int getTankCapacity(int tank) {
    return this.getSlotLimit(tank);
  }

}